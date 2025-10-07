#include <jni.h>
#include <string>
#include <android/log.h>
#include <android/bitmap.h>
#include "opencv-processor.h"

#define LOG_TAG "NativeLib"
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

extern "C" {

/**
 * Initialize OpenCV and native processing
 */
JNIEXPORT jboolean JNICALL
Java_max_ohm_assignment_jni_NativeProcessor_initNative(
        JNIEnv *env,
        jobject /* this */) {
    LOGD("Native library initialized");
    return JNI_TRUE;
}

/**
 * Process image frame with Canny edge detection
 */
JNIEXPORT void JNICALL
Java_max_ohm_assignment_jni_NativeProcessor_processFrameCanny(
        JNIEnv *env,
        jobject /* this */,
        jobject inputBitmap,
        jobject outputBitmap,
        jdouble threshold1,
        jdouble threshold2) {

    AndroidBitmapInfo inputInfo;
    AndroidBitmapInfo outputInfo;
    void *inputPixels;
    void *outputPixels;

    // Get input bitmap info
    if (AndroidBitmap_getInfo(env, inputBitmap, &inputInfo) < 0) {
        LOGE("Failed to get input bitmap info");
        return;
    }

    // Get output bitmap info
    if (AndroidBitmap_getInfo(env, outputBitmap, &outputInfo) < 0) {
        LOGE("Failed to get output bitmap info");
        return;
    }

    // Lock input pixels
    if (AndroidBitmap_lockPixels(env, inputBitmap, &inputPixels) < 0) {
        LOGE("Failed to lock input pixels");
        return;
    }

    // Lock output pixels
    if (AndroidBitmap_lockPixels(env, outputBitmap, &outputPixels) < 0) {
        LOGE("Failed to lock output pixels");
        AndroidBitmap_unlockPixels(env, inputBitmap);
        return;
    }

    // Process with OpenCV
    OpenCVProcessor::processCannyEdge(
            inputPixels,
            outputPixels,
            inputInfo.width,
            inputInfo.height,
            threshold1,
            threshold2
    );

    // Unlock pixels
    AndroidBitmap_unlockPixels(env, inputBitmap);
    AndroidBitmap_unlockPixels(env, outputBitmap);
}

/**
 * Process image frame to grayscale
 */
JNIEXPORT void JNICALL
Java_max_ohm_assignment_jni_NativeProcessor_processFrameGrayscale(
        JNIEnv *env,
        jobject /* this */,
        jobject inputBitmap,
        jobject outputBitmap) {

    AndroidBitmapInfo inputInfo;
    AndroidBitmapInfo outputInfo;
    void *inputPixels;
    void *outputPixels;

    // Get bitmap info
    if (AndroidBitmap_getInfo(env, inputBitmap, &inputInfo) < 0 ||
        AndroidBitmap_getInfo(env, outputBitmap, &outputInfo) < 0) {
        LOGE("Failed to get bitmap info");
        return;
    }

    // Lock pixels
    if (AndroidBitmap_lockPixels(env, inputBitmap, &inputPixels) < 0 ||
        AndroidBitmap_lockPixels(env, outputBitmap, &outputPixels) < 0) {
        LOGE("Failed to lock pixels");
        return;
    }

    // Process with OpenCV
    OpenCVProcessor::processGrayscale(
            inputPixels,
            outputPixels,
            inputInfo.width,
            inputInfo.height
    );

    // Unlock pixels
    AndroidBitmap_unlockPixels(env, inputBitmap);
    AndroidBitmap_unlockPixels(env, outputBitmap);
}

/**
 * Process YUV image (direct from camera)
 */
JNIEXPORT jbyteArray JNICALL
Java_max_ohm_assignment_jni_NativeProcessor_processYUVFrame(
        JNIEnv *env,
        jobject /* this */,
        jbyteArray yuvData,
        jint width,
        jint height,
        jint processingMode) {

    jbyte *yuv = env->GetByteArrayElements(yuvData, nullptr);
    jsize yuvLength = env->GetArrayLength(yuvData);

    // Process YUV data
    std::vector<uint8_t> processedData = OpenCVProcessor::processYUVData(
            reinterpret_cast<uint8_t *>(yuv),
            yuvLength,
            width,
            height,
            processingMode
    );

    env->ReleaseByteArrayElements(yuvData, yuv, JNI_ABORT);

    // Convert result to jbyteArray
    jbyteArray result = env->NewByteArray(processedData.size());
    env->SetByteArrayRegion(result, 0, processedData.size(),
                            reinterpret_cast<const jbyte *>(processedData.data()));

    return result;
}

/**
 * Get library version
 */
JNIEXPORT jstring JNICALL
Java_max_ohm_assignment_jni_NativeProcessor_getOpenCVVersion(
        JNIEnv *env,
        jobject /* this */) {
    return env->NewStringUTF(OpenCVProcessor::getVersion().c_str());
}

} // extern "C"

