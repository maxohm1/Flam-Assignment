package max.ohm.assignment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import max.ohm.assignment.camera.CameraHandler
import max.ohm.assignment.gl.GLRenderer
import max.ohm.assignment.jni.NativeProcessor
import max.ohm.assignment.jni.ProcessingMode
import max.ohm.assignment.utils.FPSCounter

/**
 * Main Activity - Real-time Edge Detection Viewer
 * 
 * Features:
 * - Camera feed capture using CameraX
 * - OpenCV C++ processing via JNI (Canny edge detection, Grayscale)
 * - OpenGL ES rendering for display
 * - FPS counter and performance monitoring
 * - Toggle between Raw/Grayscale/Edge Detection modes
 */
class MainActivity : ComponentActivity() {

    private val TAG = "MainActivity"
    private val CAMERA_PERMISSION_REQUEST_CODE = 100

    // Components
    private lateinit var glSurfaceView: GLSurfaceView
    private lateinit var glRenderer: GLRenderer
    private lateinit var cameraHandler: CameraHandler
    private lateinit var fpsCounter: FPSCounter

    // UI Elements
    private lateinit var toggleButton: Button
    private lateinit var statsTextView: TextView
    private lateinit var modeTextView: TextView

    private var currentBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize components
        initializeNativeLibrary()
        initializeUI()
        
        // Request camera permission
        if (checkCameraPermission()) {
            startCamera()
        } else {
            requestCameraPermission()
        }
    }

    private fun initializeNativeLibrary() {
        try {
            val initialized = NativeProcessor.initNative()
            val version = NativeProcessor.getOpenCVVersion()
            Log.d(TAG, "Native library initialized: $initialized, OpenCV version: $version")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize native library", e)
            Toast.makeText(this, "Failed to load native library", Toast.LENGTH_LONG).show()
        }
    }

    private fun initializeUI() {
        // Create GLSurfaceView for OpenGL rendering
        glSurfaceView = GLSurfaceView(this).apply {
            setEGLContextClientVersion(2)
            glRenderer = GLRenderer()
            setRenderer(glRenderer)
            renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        }

        // Create UI overlay
        val rootView = layoutInflater.inflate(R.layout.activity_main, null)
        val glContainer = rootView.findViewById<View>(R.id.glSurfaceContainer)
        
        // Add GLSurfaceView to container
        if (glContainer is android.widget.FrameLayout) {
            glContainer.addView(glSurfaceView)
        }

        // Initialize UI elements
        toggleButton = rootView.findViewById(R.id.toggleButton)
        statsTextView = rootView.findViewById(R.id.statsText)
        modeTextView = rootView.findViewById(R.id.modeText)

        toggleButton.setOnClickListener {
            cameraHandler.toggleProcessingMode()
            updateModeText()
        }

        setContentView(rootView)

        // Initialize FPS counter
        fpsCounter = FPSCounter()
    }

    private fun startCamera() {
        cameraHandler = CameraHandler(this, this)
        
        cameraHandler.startCamera(null) { processedBitmap ->
            runOnUiThread {
                // Update FPS counter
                fpsCounter.recordFrame()
                
                // Update OpenGL texture
                glRenderer.updateBitmap(processedBitmap)
                glSurfaceView.requestRender()
                
                // Update stats
                updateStats(processedBitmap)
                
                // Store current bitmap
                currentBitmap?.recycle()
                currentBitmap = processedBitmap
            }
        }
        
        updateModeText()
        Log.d(TAG, "Camera started")
    }

    private fun updateStats(bitmap: Bitmap) {
        val stats = fpsCounter.getStats()
        val statsText = """FPS: ${"%.1f".format(stats.fps)}
Resolution: ${bitmap.width}x${bitmap.height}
Avg Time: ${"%.1f".format(stats.avgFrameTime)}ms"""
        statsTextView.text = statsText
    }

    private fun updateModeText() {
        val mode = cameraHandler.getProcessingMode()
        val modeText = when (mode) {
            ProcessingMode.RAW -> "Mode: Raw Camera Feed"
            ProcessingMode.GRAYSCALE -> "Mode: Grayscale Filter"
            ProcessingMode.CANNY_EDGE -> "Mode: Canny Edge Detection"
        }
        modeTextView.text = modeText
    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Camera permission is required for this app",
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraHandler.stopCamera()
        glRenderer.release()
        currentBitmap?.recycle()
        Log.d(TAG, "Activity destroyed")
    }
}
