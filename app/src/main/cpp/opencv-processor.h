#ifndef ASSIGNMENT_OPENCV_PROCESSOR_H
#define ASSIGNMENT_OPENCV_PROCESSOR_H

#include <string>
#include <vector>
#include <cstdint>

/**
 * OpenCV image processing utilities
 */
class OpenCVProcessor {
public:
    /**
     * Process frame with Canny edge detection
     */
    static void processCannyEdge(
            void *inputPixels,
            void *outputPixels,
            int width,
            int height,
            double threshold1,
            double threshold2
    );

    /**
     * Convert frame to grayscale
     */
    static void processGrayscale(
            void *inputPixels,
            void *outputPixels,
            int width,
            int height
    );

    /**
     * Process YUV data from camera
     */
    static std::vector<uint8_t> processYUVData(
            uint8_t *yuvData,
            size_t dataLength,
            int width,
            int height,
            int processingMode
    );

    /**
     * Get OpenCV version
     */
    static std::string getVersion();
};

#endif //ASSIGNMENT_OPENCV_PROCESSOR_H

