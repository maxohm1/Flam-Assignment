package max.ohm.assignment.jni

import android.graphics.Bitmap

/**
 * JNI Bridge for native C++ OpenCV processing
 */
object NativeProcessor {

    init {
        try {
            System.loadLibrary("native-lib")
        } catch (e: UnsatisfiedLinkError) {
            throw RuntimeException("Failed to load native library", e)
        }
    }

    /**
     * Initialize native processing
     */
    external fun initNative(): Boolean

    /**
     * Process frame with Canny edge detection
     * @param inputBitmap Source bitmap
     * @param outputBitmap Destination bitmap
     * @param threshold1 First threshold for hysteresis (default: 50.0)
     * @param threshold2 Second threshold for hysteresis (default: 150.0)
     */
    external fun processFrameCanny(
        inputBitmap: Bitmap,
        outputBitmap: Bitmap,
        threshold1: Double = 50.0,
        threshold2: Double = 150.0
    )

    /**
     * Process frame to grayscale
     * @param inputBitmap Source bitmap
     * @param outputBitmap Destination bitmap
     */
    external fun processFrameGrayscale(
        inputBitmap: Bitmap,
        outputBitmap: Bitmap
    )

    /**
     * Process YUV frame data directly
     * @param yuvData YUV byte array from camera
     * @param width Frame width
     * @param height Frame height
     * @param processingMode 0=raw, 1=grayscale, 2=canny
     * @return Processed RGB byte array
     */
    external fun processYUVFrame(
        yuvData: ByteArray,
        width: Int,
        height: Int,
        processingMode: Int
    ): ByteArray

    /**
     * Get OpenCV version
     */
    external fun getOpenCVVersion(): String
}

/**
 * Processing modes for YUV frames
 */
enum class ProcessingMode(val value: Int) {
    RAW(0),
    GRAYSCALE(1),
    CANNY_EDGE(2)
}

