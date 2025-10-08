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

### CheckOut the Assignment Demo Video

<a href="https://drive.google.com/file/d/13QaEKrK8w1gY3QuNUYkWejyT0P_AqQFM/view?usp=sharing" target="_blank">
    <img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/371907120_YOUTUBE_ICON_400px.gif" width="150" />
</a>




## Screenshots


<div align="center">

<table>
  <tr>
    <td><img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/.idea/phone%20raw.jpg" width="250" alt="OneAI Home Screen"></td>
    <td><img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/.idea/phone%20gray.jpg" width="250" alt="Text to Image Generation"></td>
    <td><img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/.idea/phone%20edge.jpg" width="250" alt="Image Transformation"></td>
  </tr>
  <tr>
    <td align="center"><b>Phone</b><br><sub>Raw Camera Feed</sub></td>
    <td align="center"><b>Phone</b><br><sub>Grayscale Filter</sub></td>
    <td align="center"><b>Phone</b><br><sub>Edge Detection</sub></td>
  </tr>
  <tr>
    <td><img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/.idea/phone%20edge2.jpg" width="250" alt="Main Interface"></td>
    <td><img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/.idea/phone%20edge3.jpg" width="250" alt="AI Chat Interface"></td>
    <td><img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/.idea/phone%20edge4.jpg" width="250" alt="Gallery View"></td>
  </tr>
  <tr>
    <td align="center"><b>Phone</b><br><sub>Edge Detection</sub></td>
    <td align="center"><b>Phone</b><br><sub>Edge Detection</sub></td>
    <td align="center"><b>Phone</b><br><sub>Edge Detection</sub></td>
  </tr>
    <!-- <tr>
    <td><img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/inpaint.png" width="250" alt="AI Chat Interface"></td>
    <td><img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/try%20(1).png" width="250" alt="Gallery View"></td>
    <td><img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/try(2).png" width="250" alt="Gallery View"></td>
  </tr>
  <tr>
    <td align="center"><b>Brush Magic</b><br><sub>Chat with AI models</sub></td>
    <td align="center"><b>Fashion Try On</b><br><sub>Your masterpieces</sub></td>
   <td align="center"><b>Fashion Try On</b><br><sub>Your masterpieces</sub></td>
  </tr>
    </tr>
    <tr>
    <td><img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/video%20show.png" width="250" alt="AI Chat Interface"></td>
    <td><img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/head.png" width="250" alt="Gallery View"></td>
    <td><img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/ghibli.png" width="250" alt="Gallery View"></td>
  </tr>
  <tr>
    <td align="center"><b>Video generation</b><br><sub>Chat with AI models</sub></td>
    <td align="center"><b>Headshot</b><br><sub>Your masterpieces</sub></td>
   <td align="center"><b>Ghibli</b><br><sub>Your masterpieces</sub></td>
  </tr> -->
</table>

<table>
 <tr>
    <td><img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/.idea/web%20raw.png" width="5550" alt="OneAI Home Screen"></td>
  </tr>
  <tr>
    <td align="center"><b>Web</b><br><sub>Raw Camera Feed</sub></td>
  </tr>

  <tr>
    <td><img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/.idea/web%20gray.png" width="5550" alt="OneAI Home Screen"></td>
  </tr>
  <tr>
    <td align="center"><b>Web</b><br><sub>Grayscale Live from Phone</sub></td>
  </tr>

  <tr>
    <td><img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/.idea/web%20edge.png" width="5550" alt="OneAI Home Screen"></td>
  </tr>
  <tr>
    <td align="center"><b>Web</b><br><sub>Edge Detection Live from Phone</sub></td>
  </tr>

  <tr>
    <td><img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/.idea/fps%20show.png" width="5550" alt="OneAI Home Screen"></td>
  </tr>
  <tr>
    <td align="center"><b>Web</b><br><sub>FPS show in Web</sub></td>
  </tr>



  
</table>
</div>


### Android App
- **Raw Camera Feed** - Live camera preview without processing
- **Grayscale Mode** - Converted grayscale image
- **Edge Detection Mode** - Canny edge detection output
- **FPS Counter** - Performance metrics overlay

### Web Viewer
- **Live Streaming** - Real-time frame updates via WebSocket
- **Statistics Dashboard** - Server metrics and frame information

> Note: Run the application to see it in action. The Android app displays camera frames with different processing modes using the Toggle Mode button, and the web viewer shows frames sent from the app in real-time.

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
   
   # Or click the Run button in Android Studio
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
│       - Camera permissions              │
│          - UI management                │
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
│         NativeProcessor (JNI)          │
│        - Kotlin ↔ C++ bridge           │
└────────────────┬───────────────────────┘
                 │
                 ▼
┌────────────────────────────────────────┐
│         OpenCV Processor (C++)         │
│        - Canny Edge Detection          │
│        - Grayscale Conversion          │
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

<div align="center">

**I Hope you <img src="https://images.emojiterra.com/google/noto-emoji/animated-emoji/2764.gif" height="30" alt="love" /> this Assignment CheckOut Out More <img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/200w.gif" height="40" />**

<br><br>

<a href="https://www.linkedin.com/in/om-prakash-mandal-a253a12a6/" target="_blank">
    <img src="https://github.com/maxohm1/OneAI-ScreenShot/blob/main/372102050_LINKEDIN_ICON_TRANSPARENT_1080.gif" width="150" />
</a>

<a href="https://play.google.com/store/apps/details?id=max.ohm.oneai&hl=en" target="_blank">
    <img src="https://user-images.githubusercontent.com/74038190/212281763-e6ecd7ef-c4aa-45b6-a97c-f33f6bb592bd.gif" width="100" />
</a>

<a href="https://github.com/Android-by-Kotlin/OneAI-2.0" target="_blank">
    <img src="https://user-images.githubusercontent.com/74038190/212257468-1e9a91f1-b626-4baa-b15d-5c385dfa7ed2.gif" width="140" />
</a>

</div>

