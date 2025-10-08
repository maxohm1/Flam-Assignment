#include "opencv-processor.h"
#include <android/log.h>
#include <cstring>
#include <algorithm>
#include <cmath>

#define LOG_TAG "OpenCVProcessor"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

void OpenCVProcessor::processCannyEdge(
        void *inputPixels,
        void *outputPixels,
        int width,
        int height,
        double threshold1,
        double threshold2) {
    
    uint8_t* input = (uint8_t*)inputPixels;
    uint8_t* output = (uint8_t*)outputPixels;
    
    // Ultra-fast edge detection using Manhattan distance approximation
    // This is 2-3x faster than Euclidean distance
    int threshold = (int)(threshold1 * 4);  // Scale for Manhattan distance
    
    // Process with memset for borders (much faster)
    memset(output, 0, width * 4);  // Top row
    memset(output + ((height-1) * width * 4), 0, width * 4);  // Bottom row
    
    // Process inner pixels with highly optimized loop
    for (int y = 1; y < height - 1; y++) {
        // Cache row pointers
        const uint8_t* rowTop = input + ((y-1) * width) * 4;
        const uint8_t* rowMid = input + (y * width) * 4;
        const uint8_t* rowBot = input + ((y+1) * width) * 4;
        uint8_t* outRow = output + (y * width) * 4;
        
        // Set left border
        outRow[0] = outRow[1] = outRow[2] = 0;
        outRow[3] = 255;
        
        // Process middle pixels with optimized kernel
        for (int x = 1; x < width - 1; x++) {
            const int xIdx = x * 4;
            const int xLeft = xIdx - 4;
            const int xRight = xIdx + 4;
            
            // Simplified Sobel using only R channel for speed (grayscale-like)
            // Sobel X: [-1 0 1; -2 0 2; -1 0 1]
            const int gx = (rowTop[xRight] + (rowMid[xRight] << 1) + rowBot[xRight])
                         - (rowTop[xLeft] + (rowMid[xLeft] << 1) + rowBot[xLeft]);
            
            // Sobel Y: [-1 -2 -1; 0 0 0; 1 2 1]
            const int gy = (rowBot[xLeft] + (rowBot[xIdx] << 1) + rowBot[xRight])
                         - (rowTop[xLeft] + (rowTop[xIdx] << 1) + rowTop[xRight]);
            
            // Manhattan distance (|gx| + |gy|) - much faster than Euclidean
            const int magnitude = (gx < 0 ? -gx : gx) + (gy < 0 ? -gy : gy);
            
            // Apply threshold with branchless operation
            const uint8_t edgeValue = (magnitude > threshold) ? 255 : 0;
            
            // Write all channels at once using 32-bit write
            *((uint32_t*)&outRow[xIdx]) = (0xFF000000) | (edgeValue << 16) | (edgeValue << 8) | edgeValue;
        }
        
        // Set right border
        const int lastIdx = (width - 1) * 4;
        outRow[lastIdx] = outRow[lastIdx+1] = outRow[lastIdx+2] = 0;
        outRow[lastIdx+3] = 255;
    }
    
    LOGD("Ultra-fast edge detection: %dx%d", width, height);
}

void OpenCVProcessor::processGrayscale(
        void *inputPixels,
        void *outputPixels,
        int width,
        int height) {
    
    // Stub: Manual grayscale conversion
    uint8_t* in = (uint8_t*)inputPixels;
    uint8_t* out = (uint8_t*)outputPixels;
    
    for (int i = 0; i < width * height; i++) {
        int gray = (in[i*4] + in[i*4+1] + in[i*4+2]) / 3;
        out[i*4] = gray;
        out[i*4+1] = gray;
        out[i*4+2] = gray;
        out[i*4+3] = 255;
    }
    LOGD("Grayscale (stub) processed: %dx%d", width, height);
}

std::vector<uint8_t> OpenCVProcessor::processYUVData(
        uint8_t *yuvData,
        size_t dataLength,
        int width,
        int height,
        int processingMode) {
    
    std::vector<uint8_t> result(dataLength);
    // Stub: Just return the data as-is
    memcpy(result.data(), yuvData, dataLength);
    LOGD("YUV processing (stub): mode=%d, %dx%d", processingMode, width, height);
    return result;
}

std::string OpenCVProcessor::getVersion() {
    return "Custom Image Processing v1.0";
}

