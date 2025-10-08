# Real-Time Computer Vision Processing System

A real-time computer vision application featuring Android native processing with OpenCV and a TypeScript-based web viewer for remote monitoring.

## Features Implemented

### Android Application
- **CameraX Integration** - Real-time camera feed capture
- **OpenCV C++ Processing** - Native image processing via JNI
  - Canny Edge Detection
  - Grayscale conversion
  - Raw camera feed
- **OpenGL ES 2.0 Rendering** - Hardware-accelerated rendering
- **Mode Toggle** - Switch between Raw/Grayscale/Canny modes
- **FPS Counter** - Live performance monitoring

### Web Viewer
- **TypeScript Viewer** - Web-based frame viewer
- **WebSocket Server** - Real-time frame streaming
- **REST API** - Frame data and statistics endpoints
- **Express.js Backend** - Node.js web server
- **Live Statistics** - Performance metrics display

## Screenshots

*Screenshots showing the application in action:*

### Android App
- **Raw Camera Feed** - Live camera preview without processing
- **Grayscale Mode** - Converted grayscale image
- **Edge Detection Mode** - Canny edge detection output
- **FPS Counter** - Performance metrics overlay

### Web Viewer
- **Live Streaming** - Real-time frame updates via WebSocket
- **Statistics Dashboard** - Server metrics and frame information

> Note: Run the application to see it in action. The Android app displays camera frames with different processing modes, and the web viewer shows frames sent from the app in real-time.

## Setup Instructions

### Prerequisites
- **Android Studio** Hedgehog (2023.1.1) or later
- **Android NDK** (version 29.0.14206865 or later)
- **CMake** 3.22.1+
- **Node.js** 18+ and npm (for web viewer)
- **Android device** or emulator with camera support

### Android Setup

1. **Install NDK and CMake**
   ```bash
   # In Android Studio:
   # Tools → SDK Manager → SDK Tools tab
   # Check: "NDK (Side by side)" and "CMake"
   # Click Apply to install
   ```

2. **Clone and Open Project**
   ```bash
   git clone https://github.com/maxohm1/Flam-Assignment.git
   cd Assignment
   # Open in Android Studio: File → Open → Select 'Assignment' folder
   ```

3. **OpenCV Dependencies**
   - OpenCV is configured in the project via CMake
   - Native C++ libraries are built automatically with Gradle

4. **Build and Run**
   ```bash
   # Sync and build
   ./gradlew clean build
   
   # Or click the Run ▶️ button in Android Studio
   # Connect Android device via USB or start emulator
   ```

### Web Viewer Setup

1. **Navigate to Web Directory**
   ```bash
   cd web
   ```

2. **Install Dependencies**
   ```bash
   npm install
   ```

3. **Build TypeScript**
   ```bash
   npm run build
   ```

4. **Start Server**
   ```bash
   npm run server
   # Server runs at http://localhost:8080
   ```

5. **Access Viewer**
   - Main viewer: `http://localhost:8080/index.html`
   - Live streaming: `http://localhost:8080/live.html`

## Architecture

### High-Level Overview

```
┌─────────────────────────────────────────┐
│       MainActivity (Kotlin)             │
│   - Camera permissions                  │
│   - UI management                       │
└────────┬────────────────────┬───────────┘
         │                    │
         ▼                    ▼
┌──────────────────┐  ┌──────────────────┐
│  CameraHandler   │  │   GLRenderer     │
│  - CameraX API   │  │   - OpenGL ES    │
│  - YUV→Bitmap    │  │   - Texture      │
└────────┬─────────┘  └────────▲─────────┘
         │                     │
         │ Frame (Bitmap)      │ Processed
         ▼                     │
┌──────────────────────────────┴─────────┐
│      NativeProcessor (JNI)             │
│   - Kotlin ↔ C++ bridge                │
└────────────────┬───────────────────────┘
                 │
                 ▼
┌────────────────────────────────────────┐
│     OpenCV Processor (C++)             │
│   - Canny Edge Detection               │
│   - Grayscale Conversion               │
└────────────────────────────────────────┘
```

### Frame Flow

1. **Capture** - CameraX captures YUV420 frames from camera
2. **Convert** - YUV → Bitmap conversion (Java layer)
3. **Process** - JNI call to native C++ OpenCV processor
4. **Return** - Processed bitmap returned to Kotlin
5. **Render** - OpenGL uploads texture and renders to screen
6. **Display** - FPS counter updates performance metrics

### JNI Bridge

The application uses JNI (Java Native Interface) to communicate between Kotlin and C++:

```kotlin
// Kotlin side - Native method declaration
external fun processFrameCanny(
    inputBitmap: Bitmap,
    outputBitmap: Bitmap,
    threshold1: Double,
    threshold2: Double
)
```

```cpp
// C++ side - JNI implementation
JNIEXPORT void JNICALL
Java_max_ohm_assignment_jni_NativeProcessor_processFrameCanny(
    JNIEnv *env, jobject, 
    jobject inputBitmap, jobject outputBitmap,
    jdouble threshold1, jdouble threshold2
) {
    // Lock bitmaps
    // Process with OpenCV
    // Unlock bitmaps
}
```

### TypeScript Web Viewer

The web viewer architecture consists of:

- **Frontend (TypeScript)**
  - Compiled to JavaScript for browser execution
  - WebSocket client for real-time updates
  - Canvas rendering for frame display
  - HTTP fallback for polling

- **Backend (Node.js + Express)**
  - WebSocket server for real-time streaming
  - REST API endpoints for frame data
  - Frame buffer for history
  - Statistics tracking

**Communication Flow:**
```
Android App → HTTP POST → Express Server
                            ↓
                    WebSocket Broadcast
                            ↓
                    TypeScript Viewer → Canvas Display
```

## Dependencies

### Android
- **CameraX** 1.3.1 - Modern camera API
- **OpenCV** 4.9.0 (C++) - Image processing
- **OpenGL ES** 2.0 - Hardware rendering
- **Kotlin Coroutines** 1.7.3 - Async operations
- **NDK** 29.0.14206865 - Native development

### Web
- **TypeScript** 5.7+ - Type-safe JavaScript
- **Node.js** 18+ - JavaScript runtime
- **Express** 4.x - Web server framework
- **ws** 8.x - WebSocket library

## Project Structure

```
Assignment/
├── app/
│   ├── src/main/
│   │   ├── cpp/                    # Native C++ code
│   │   │   ├── CMakeLists.txt      # CMake configuration
│   │   │   ├── native-lib.cpp      # JNI implementation
│   │   │   ├── opencv-processor.h
│   │   │   └── opencv-processor.cpp
│   │   ├── java/max/ohm/assignment/
│   │   │   ├── MainActivity.kt
│   │   │   ├── jni/NativeProcessor.kt
│   │   │   ├── camera/CameraHandler.kt
│   │   │   ├── gl/GLRenderer.kt
│   │   │   └── utils/FPSCounter.kt
│   │   └── res/layout/activity_main.xml
│   └── build.gradle.kts
└── web/                            # TypeScript web viewer
    ├── server.js                   # Express + WebSocket server
    ├── viewer.ts                   # TypeScript viewer logic
    ├── package.json
    ├── index.html                  # Main viewer
    └── live.html                   # Live streaming viewer
```

---

**Built for Android Computer Vision Processing Assignment**
