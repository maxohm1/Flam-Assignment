package max.ohm.assignment.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Matrix
import android.graphics.Rect
import android.graphics.YuvImage
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import max.ohm.assignment.jni.NativeProcessor
import max.ohm.assignment.jni.ProcessingMode
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.min

/**
 * Camera handler using CameraX for frame capture
 */
class CameraHandler(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) {
    private val TAG = "CameraHandler"

    private var camera: Camera? = null
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var cameraProvider: ProcessCameraProvider? = null

    private val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    private var frameCallback: ((Bitmap) -> Unit)? = null
    private var processingMode = ProcessingMode.RAW

    @Volatile
    private var isProcessing = false

    /**
     * Start camera with processing callback
     */
    fun startCamera(
        surfaceProvider: Preview.SurfaceProvider?,
        onFrameProcessed: (Bitmap) -> Unit
    ) {
        this.frameCallback = onFrameProcessed

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            try {
                cameraProvider = cameraProviderFuture.get()
                bindCamera(surfaceProvider)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to bind camera", e)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    private fun bindCamera(surfaceProvider: Preview.SurfaceProvider?) {
        val cameraProvider = cameraProvider ?: return

        // Preview use case
        preview = Preview.Builder()
            .build()

        surfaceProvider?.let {
            preview?.setSurfaceProvider(it)
        }

        // Image analysis use case
        imageAnalyzer = ImageAnalysis.Builder()
            .setTargetResolution(android.util.Size(640, 480))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
            .build()
            .apply {
                setAnalyzer(cameraExecutor, FrameAnalyzer())
            }

        // Select back camera
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            // Unbind all use cases before rebinding
            cameraProvider.unbindAll()

            // Bind use cases to camera
            camera = cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageAnalyzer
            )

            Log.d(TAG, "Camera bound successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to bind camera use cases", e)
        }
    }

    /**
     * Set processing mode
     */
    fun setProcessingMode(mode: ProcessingMode) {
        this.processingMode = mode
        Log.d(TAG, "Processing mode changed to: $mode")
    }

    /**
     * Get current processing mode
     */
    fun getProcessingMode(): ProcessingMode = processingMode

    /**
     * Toggle between processing modes
     */
    fun toggleProcessingMode() {
        processingMode = when (processingMode) {
            ProcessingMode.RAW -> ProcessingMode.GRAYSCALE
            ProcessingMode.GRAYSCALE -> ProcessingMode.CANNY_EDGE
            ProcessingMode.CANNY_EDGE -> ProcessingMode.RAW
        }
        Log.d(TAG, "Toggled processing mode to: $processingMode")
    }

    /**
     * Stop camera and release resources
     */
    fun stopCamera() {
        cameraProvider?.unbindAll()
        cameraExecutor.shutdown()
        Log.d(TAG, "Camera stopped")
    }

    /**
     * Frame analyzer for processing camera frames
     */
    private inner class FrameAnalyzer : ImageAnalysis.Analyzer {
        override fun analyze(image: ImageProxy) {
            if (isProcessing) {
                image.close()
                return
            }

            isProcessing = true

            try {
                val processedBitmap = when (processingMode) {
                    ProcessingMode.RAW -> imageProxyToBitmap(image)
                    ProcessingMode.GRAYSCALE -> processGrayscale(image)
                    ProcessingMode.CANNY_EDGE -> processCannyEdge(image)
                }

                frameCallback?.invoke(processedBitmap)
            } catch (e: Exception) {
                Log.e(TAG, "Frame processing error", e)
            } finally {
                isProcessing = false
                image.close()
            }
        }

        private fun imageProxyToBitmap(image: ImageProxy): Bitmap {
            val yBuffer = image.planes[0].buffer
            val uBuffer = image.planes[1].buffer
            val vBuffer = image.planes[2].buffer

            val yStride = image.planes[0].rowStride
            val uvStride = image.planes[1].rowStride
            val uvPixelStride = image.planes[1].pixelStride

            val width = image.width
            val height = image.height

            // Create RGB array
            val rgbArray = IntArray(width * height)
            
            // Convert YUV to RGB
            convertYuv420ToRgb(yBuffer, uBuffer, vBuffer, rgbArray, width, height, yStride, uvStride, uvPixelStride)
            
            // Create bitmap from RGB array
            val bitmap = Bitmap.createBitmap(rgbArray, width, height, Bitmap.Config.ARGB_8888)
            
            // Handle rotation
            val rotationDegrees = image.imageInfo.rotationDegrees
            return if (rotationDegrees != 0) {
                rotateBitmap(bitmap, rotationDegrees.toFloat())
            } else {
                bitmap
            }
        }

        private fun convertYuv420ToRgb(
            yBuffer: ByteBuffer, uBuffer: ByteBuffer, vBuffer: ByteBuffer,
            rgbArray: IntArray, width: Int, height: Int,
            yStride: Int, uvStride: Int, uvPixelStride: Int
        ) {
            val yArray = ByteArray(yBuffer.remaining())
            val uArray = ByteArray(uBuffer.remaining())
            val vArray = ByteArray(vBuffer.remaining())
            
            yBuffer.get(yArray)
            uBuffer.get(uArray)
            vBuffer.get(vArray)
            
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val yIndex = y * yStride + x
                    val uvIndex = (y / 2) * uvStride + (x / 2) * uvPixelStride
                    
                    if (yIndex >= yArray.size || uvIndex >= uArray.size || uvIndex >= vArray.size) {
                        continue
                    }
                    
                    val Y = yArray[yIndex].toInt() and 0xFF
                    val U = uArray[uvIndex].toInt() and 0xFF
                    val V = vArray[uvIndex].toInt() and 0xFF
                    
                    // YUV to RGB conversion
                    val C = Y - 16
                    val D = U - 128
                    val E = V - 128
                    
                    var R = (298 * C + 409 * E + 128) shr 8
                    var G = (298 * C - 100 * D - 208 * E + 128) shr 8
                    var B = (298 * C + 516 * D + 128) shr 8
                    
                    // Clamp to [0, 255]
                    R = R.coerceIn(0, 255)
                    G = G.coerceIn(0, 255)
                    B = B.coerceIn(0, 255)
                    
                    rgbArray[y * width + x] = (0xFF shl 24) or (R shl 16) or (G shl 8) or B
                }
            }
        }

        private fun processGrayscale(image: ImageProxy): Bitmap {
            val inputBitmap = imageProxyToBitmap(image)
            val outputBitmap = Bitmap.createBitmap(
                inputBitmap.width,
                inputBitmap.height,
                Bitmap.Config.ARGB_8888
            )

            NativeProcessor.processFrameGrayscale(inputBitmap, outputBitmap)
            inputBitmap.recycle()

            return outputBitmap
        }

        private fun processCannyEdge(image: ImageProxy): Bitmap {
            val inputBitmap = imageProxyToBitmap(image)
            val outputBitmap = Bitmap.createBitmap(
                inputBitmap.width,
                inputBitmap.height,
                Bitmap.Config.ARGB_8888
            )

            NativeProcessor.processFrameCanny(inputBitmap, outputBitmap, 50.0, 150.0)
            inputBitmap.recycle()

            return outputBitmap
        }

        private fun rotateBitmap(bitmap: Bitmap, degrees: Float): Bitmap {
            if (degrees == 0f) return bitmap

            val matrix = Matrix()
            matrix.postRotate(degrees)

            val rotated = Bitmap.createBitmap(
                bitmap, 0, 0,
                bitmap.width, bitmap.height,
                matrix, true
            )

            if (rotated != bitmap) {
                bitmap.recycle()
            }

            return rotated
        }
    }
}

