package max.ohm.assignment.network

import android.graphics.Bitmap
import android.util.Base64
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * HTTP client for sending processed frames to the web viewer server
 * Supports both synchronous and asynchronous operations
 */
class WebServerClient(private val baseUrl: String = "http://192.168.1.100:8080") {
    
    companion object {
        private const val TAG = "WebServerClient"
        private const val TIMEOUT_CONNECT = 5000 // 5 seconds
        private const val TIMEOUT_READ = 10000 // 10 seconds
    }
    
    private val executor = Executors.newSingleThreadExecutor()
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    
    /**
     * Send a processed frame to the web server asynchronously
     */
    fun sendFrameAsync(
        bitmap: Bitmap,
        fps: Double,
        resolution: String,
        processingMode: String,
        onSuccess: (() -> Unit)? = null,
        onError: ((String) -> Unit)? = null
    ) {
        coroutineScope.launch {
            try {
                val success = sendFrame(bitmap, fps, resolution, processingMode)
                if (success) {
                    onSuccess?.invoke()
                } else {
                    onError?.invoke("Failed to send frame to server")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error sending frame async", e)
                onError?.invoke("Network error: ${e.message}")
            }
        }
    }
    
    /**
     * Send a processed frame to the web server synchronously
     */
    fun sendFrame(
        bitmap: Bitmap,
        fps: Double,
        resolution: String,
        processingMode: String
    ): Boolean {
        try {
            // Convert bitmap to base64
            val base64Image = bitmapToBase64(bitmap)
            
            // Create JSON payload
            val jsonPayload = JSONObject().apply {
                put("image", base64Image)
                put("fps", fps)
                put("resolution", resolution)
                put("processingMode", processingMode)
                put("timestamp", System.currentTimeMillis())
            }
            
            // Send HTTP POST request
            val url = URL("$baseUrl/api/frame")
            val connection = (url.openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Accept", "application/json")
                connectTimeout = TIMEOUT_CONNECT
                readTimeout = TIMEOUT_READ
                doOutput = true
            }
            
            // Write request body
            connection.outputStream.use { outputStream ->
                outputStream.write(jsonPayload.toString().toByteArray(Charsets.UTF_8))
                outputStream.flush()
            }
            
            // Check response
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().use { it.readText() }
                Log.d(TAG, "Frame sent successfully: $response")
                return true
            } else {
                Log.w(TAG, "Server returned error code: $responseCode")
                return false
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "Error sending frame to web server", e)
            return false
        }
    }
    
    /**
     * Check if the web server is available
     */
    fun checkServerHealth(callback: (Boolean) -> Unit) {
        executor.submit {
            try {
                val url = URL("$baseUrl/api/health")
                val connection = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "GET"
                    connectTimeout = 3000
                    readTimeout = 3000
                }
                
                val responseCode = connection.responseCode
                val isHealthy = responseCode == HttpURLConnection.HTTP_OK
                
                if (isHealthy) {
                    val response = connection.inputStream.bufferedReader().use { it.readText() }
                    Log.d(TAG, "Server health check successful: $response")
                } else {
                    Log.w(TAG, "Server health check failed with code: $responseCode")
                }
                
                callback(isHealthy)
                
            } catch (e: Exception) {
                Log.e(TAG, "Server health check failed", e)
                callback(false)
            }
        }
    }
    
    /**
     * Clear all server data
     */
    fun clearServerData(callback: (Boolean) -> Unit) {
        executor.submit {
            try {
                val url = URL("$baseUrl/api/clear")
                val connection = (url.openConnection() as HttpURLConnection).apply {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    connectTimeout = TIMEOUT_CONNECT
                    readTimeout = TIMEOUT_READ
                }
                
                val responseCode = connection.responseCode
                val success = responseCode == HttpURLConnection.HTTP_OK
                
                if (success) {
                    Log.d(TAG, "Server data cleared successfully")
                } else {
                    Log.w(TAG, "Failed to clear server data: $responseCode")
                }
                
                callback(success)
                
            } catch (e: Exception) {
                Log.e(TAG, "Error clearing server data", e)
                callback(false)
            }
        }
    }
    
    /**
     * Convert bitmap to base64 string
     */
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
    
    /**
     * Update server URL (for different network configurations)
     */
    fun updateServerUrl(newBaseUrl: String) {
        // This is a simple implementation - in a real app you might want to validate the URL
        Log.i(TAG, "Server URL updated to: $newBaseUrl")
    }
    
    /**
     * Clean up resources
     */
    fun cleanup() {
        executor.shutdown()
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow()
            }
        } catch (e: InterruptedException) {
            executor.shutdownNow()
            Thread.currentThread().interrupt()
        }
    }
}

/**
 * Utility class for web server integration configuration
 */
object WebServerConfig {
    // Default server configurations for different environments
    const val LOCALHOST = "http://10.0.2.2:8080"  // For Android emulator
    const val DEFAULT_LOCAL_NETWORK = "http://192.168.1.100:8080"  // For real device on same network
    
    // Automatically detected server URL (updated after first successful connection)
    private var detectedServerUrl: String? = null
    
    /**
     * Common local network IP patterns to try
     * The first URL is your computer's detected IP address
     */
    private val commonServerUrls = listOf(
        "http://192.168.86.130:8080",  // Your computer's IP (detected)
        "http://192.168.1.100:8080",   // Common router range
        "http://192.168.0.100:8080",   // Alternative range
        "http://10.0.0.100:8080",      // Alternative range
        "http://10.0.2.2:8080"         // Android emulator
    )
    
    /**
     * Get appropriate server URL based on environment
     * Priority: Detected URL > Local Network > Localhost
     */
    fun getServerUrl(useLocalNetwork: Boolean = true): String {
        // If we've successfully connected before, use that URL
        detectedServerUrl?.let { return it }
        
        // Otherwise use default based on environment
        return if (useLocalNetwork) DEFAULT_LOCAL_NETWORK else LOCALHOST
    }
    
    /**
     * Set the detected server URL after successful connection
     */
    fun setDetectedServerUrl(url: String) {
        detectedServerUrl = url
        Log.i("WebServerConfig", "Detected server URL: $url")
    }
    
    /**
     * Get list of server URLs to try
     */
    fun getServerUrlsToTry(): List<String> {
        return commonServerUrls
    }
    
    /**
     * Reset detected URL (for testing or manual override)
     */
    fun resetDetectedUrl() {
        detectedServerUrl = null
    }
}
