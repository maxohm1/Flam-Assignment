package max.ohm.assignment.gl

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * OpenGL ES 2.0 Renderer for displaying processed camera frames
 */
class GLRenderer : GLSurfaceView.Renderer {

    private val TAG = "GLRenderer"

    private var program = 0
    private var textureId = 0
    private var vertexBuffer: FloatBuffer
    private var textureBuffer: FloatBuffer

    private var positionHandle = 0
    private var texCoordHandle = 0
    private var textureHandle = 0

    @Volatile
    private var currentBitmap: Bitmap? = null

    // Vertex coordinates (full screen quad)
    private val vertexCoords = floatArrayOf(
        -1.0f, -1.0f,  // Bottom left
        1.0f, -1.0f,   // Bottom right
        -1.0f, 1.0f,   // Top left
        1.0f, 1.0f     // Top right
    )

    // Texture coordinates (flipped vertically for correct orientation)
    private val textureCoords = floatArrayOf(
        0.0f, 1.0f,  // Bottom left
        1.0f, 1.0f,  // Bottom right
        0.0f, 0.0f,  // Top left
        1.0f, 0.0f   // Top right
    )

    init {
        // Initialize vertex buffer
        vertexBuffer = ByteBuffer.allocateDirect(vertexCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(vertexCoords)
                position(0)
            }
        }

        // Initialize texture buffer
        textureBuffer = ByteBuffer.allocateDirect(textureCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(textureCoords)
                position(0)
            }
        }
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        // Create shader program
        program = createProgram(VERTEX_SHADER, FRAGMENT_SHADER)
        if (program == 0) {
            Log.e(TAG, "Failed to create shader program")
            return
        }

        // Get shader handles
        positionHandle = GLES20.glGetAttribLocation(program, "aPosition")
        texCoordHandle = GLES20.glGetAttribLocation(program, "aTexCoord")
        textureHandle = GLES20.glGetUniformLocation(program, "uTexture")

        // Generate texture
        val textures = IntArray(1)
        GLES20.glGenTextures(1, textures, 0)
        textureId = textures[0]

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)

        Log.d(TAG, "OpenGL surface created")
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        Log.d(TAG, "Surface changed: ${width}x${height}")
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // Only draw if we have a valid texture
        val bitmap = currentBitmap
        if (bitmap == null || bitmap.isRecycled) {
            return
        }

        // Update texture if new bitmap is available
        try {
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0)
            
            // Use shader program
            GLES20.glUseProgram(program)

            // Enable vertex arrays
            GLES20.glEnableVertexAttribArray(positionHandle)
            GLES20.glEnableVertexAttribArray(texCoordHandle)

            // Set vertex data
            GLES20.glVertexAttribPointer(
                positionHandle, 2, GLES20.GL_FLOAT, false, 0, vertexBuffer
            )
            GLES20.glVertexAttribPointer(
                texCoordHandle, 2, GLES20.GL_FLOAT, false, 0, textureBuffer
            )

            // Bind texture
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId)
            GLES20.glUniform1i(textureHandle, 0)

            // Draw
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

            // Disable vertex arrays
            GLES20.glDisableVertexAttribArray(positionHandle)
            GLES20.glDisableVertexAttribArray(texCoordHandle)
            
        } catch (e: Exception) {
            Log.e(TAG, "Error rendering frame", e)
        }
    }

    /**
     * Update the bitmap to be rendered
     */
    fun updateBitmap(bitmap: Bitmap) {
        currentBitmap = bitmap
    }

    /**
     * Release resources
     */
    fun release() {
        currentBitmap?.recycle()
        currentBitmap = null
        if (textureId != 0) {
            GLES20.glDeleteTextures(1, intArrayOf(textureId), 0)
            textureId = 0
        }
        if (program != 0) {
            GLES20.glDeleteProgram(program)
            program = 0
        }
    }

    private fun createProgram(vertexSource: String, fragmentSource: String): Int {
        val vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource)
        if (vertexShader == 0) return 0

        val fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource)
        if (fragmentShader == 0) return 0

        var program = GLES20.glCreateProgram()
        if (program == 0) {
            Log.e(TAG, "Failed to create program")
            return 0
        }

        GLES20.glAttachShader(program, vertexShader)
        GLES20.glAttachShader(program, fragmentShader)
        GLES20.glLinkProgram(program)

        val linkStatus = IntArray(1)
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0)
        if (linkStatus[0] != GLES20.GL_TRUE) {
            Log.e(TAG, "Failed to link program: ${GLES20.glGetProgramInfoLog(program)}")
            GLES20.glDeleteProgram(program)
            program = 0
        }

        return program
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        val shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)

        val compiled = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] == 0) {
            Log.e(TAG, "Failed to compile shader: ${GLES20.glGetShaderInfoLog(shader)}")
            GLES20.glDeleteShader(shader)
            return 0
        }

        return shader
    }

    companion object {
        // Simple vertex shader
        private const val VERTEX_SHADER = """
            attribute vec4 aPosition;
            attribute vec2 aTexCoord;
            varying vec2 vTexCoord;
            
            void main() {
                gl_Position = aPosition;
                vTexCoord = aTexCoord;
            }
        """

        // Simple fragment shader
        private const val FRAGMENT_SHADER = """
            precision mediump float;
            
            uniform sampler2D uTexture;
            varying vec2 vTexCoord;
            
            void main() {
                gl_FragColor = texture2D(uTexture, vTexCoord);
            }
        """
    }
}

