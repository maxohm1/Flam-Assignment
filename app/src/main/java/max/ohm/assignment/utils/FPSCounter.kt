package max.ohm.assignment.utils

import kotlin.math.roundToInt

/**
 * FPS Counter for measuring frame processing performance
 */
class FPSCounter(private val sampleSize: Int = 30) {
    private val frameTimes = mutableListOf<Long>()
    private var lastFrameTime = System.currentTimeMillis()
    
    private var _fps: Double = 0.0
    val fps: Double get() = _fps
    
    private var _avgProcessingTime: Double = 0.0
    val avgProcessingTime: Double get() = _avgProcessingTime
    
    /**
     * Record a new frame
     */
    fun recordFrame() {
        val currentTime = System.currentTimeMillis()
        val deltaTime = currentTime - lastFrameTime
        lastFrameTime = currentTime
        
        frameTimes.add(deltaTime)
        
        if (frameTimes.size > sampleSize) {
            frameTimes.removeAt(0)
        }
        
        updateMetrics()
    }
    
    /**
     * Record frame with custom processing time
     */
    fun recordFrame(processingTimeMs: Long) {
        recordFrame()
        _avgProcessingTime = processingTimeMs.toDouble()
    }
    
    private fun updateMetrics() {
        if (frameTimes.isEmpty()) return
        
        val avgFrameTime = frameTimes.average()
        _fps = if (avgFrameTime > 0) 1000.0 / avgFrameTime else 0.0
        _avgProcessingTime = avgFrameTime
    }
    
    /**
     * Get formatted FPS string
     */
    fun getFPSString(): String {
        return "${_fps.roundToInt()} FPS"
    }
    
    /**
     * Get frame statistics
     */
    fun getStats(): FrameStats {
        return FrameStats(
            fps = _fps,
            avgFrameTime = _avgProcessingTime,
            minFrameTime = frameTimes.minOrNull()?.toDouble() ?: 0.0,
            maxFrameTime = frameTimes.maxOrNull()?.toDouble() ?: 0.0
        )
    }
    
    /**
     * Reset counter
     */
    fun reset() {
        frameTimes.clear()
        _fps = 0.0
        _avgProcessingTime = 0.0
        lastFrameTime = System.currentTimeMillis()
    }
}

/**
 * Frame statistics data class
 */
data class FrameStats(
    val fps: Double,
    val avgFrameTime: Double,
    val minFrameTime: Double,
    val maxFrameTime: Double
) {
    override fun toString(): String {
        return "FPS: ${"%.1f".format(fps)} | Avg: ${"%.1f".format(avgFrameTime)}ms | " +
                "Min: ${"%.1f".format(minFrameTime)}ms | Max: ${"%.1f".format(maxFrameTime)}ms"
    }
}

