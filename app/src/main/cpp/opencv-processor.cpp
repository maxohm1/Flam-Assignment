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
    
    // Simple edge detection using Sobel operator
    // First convert to grayscale
    for (int y = 1; y < height - 1; y++) {
        for (int x = 1; x < width - 1; x++) {
            int idx = (y * width + x) * 4;
            
            // Sobel X kernel
            int gx = -input[((y-1)*width + (x-1))*4] + input[((y-1)*width + (x+1))*4]
                   + -2*input[(y*width + (x-1))*4] + 2*input[(y*width + (x+1))*4]
                   + -input[((y+1)*width + (x-1))*4] + input[((y+1)*width + (x+1))*4];
            
            // Sobel Y kernel  
            int gy = -input[((y-1)*width + (x-1))*4] + -2*input[((y-1)*width + x)*4] + -input[((y-1)*width + (x+1))*4]
                   + input[((y+1)*width + (x-1))*4] + 2*input[((y+1)*width + x)*4] + input[((y+1)*width + (x+1))*4];
            
            // Calculate gradient magnitude
            int magnitude = (int)sqrt(gx*gx + gy*gy);
            magnitude = std::min(255, std::max(0, magnitude));
            
            // Apply threshold
            if (magnitude > threshold1) {
                output[idx] = 255;     // R
                output[idx+1] = 255;   // G  
                output[idx+2] = 255;   // B
            } else {
                output[idx] = 0;       // R
                output[idx+1] = 0;     // G
                output[idx+2] = 0;     // B
            }
            output[idx+3] = 255;       // A
        }
    }
    
    LOGD("Canny edge detection processed: %dx%d", width, height);
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

