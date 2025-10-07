# Computer Vision Web Viewer

This TypeScript-based web viewer displays processed camera frames and statistics from the Android CV application.

## Features

- **Frame Display**: Shows processed camera frames as base64 images
- **Statistics Panel**: Displays FPS, resolution, and timestamp information
- **Processing Mode Indicator**: Shows current processing mode (RAW, GRAYSCALE, EDGE_DETECTION)
- **Interactive Controls**: Refresh, toggle modes, and clear data
- **Sample Data Generation**: Creates sample images for testing when Android app data is not available

## Files

- `index.html` - Main HTML page with UI layout and styling
- `viewer.ts` - TypeScript implementation of the CV viewer
- `tsconfig.json` - TypeScript compiler configuration
- `README.md` - This documentation file

## Setup and Usage

### Prerequisites

- TypeScript compiler (`tsc`) installed globally or locally
- Modern web browser with ES2017+ support
- (Optional) Local HTTP server for better development experience

### Building

1. **Compile TypeScript to JavaScript:**
   ```bash
   tsc
   ```
   This generates `viewer.js` from `viewer.ts` using the configuration in `tsconfig.json`.

2. **Open in Browser:**
   Open `index.html` directly in a browser, or serve via HTTP server:
   ```bash
   # Using Python (if available)
   python -m http.server 8000
   
   # Using Node.js live-server (if installed)
   npx live-server
   ```

### Integration with Android App

The viewer exposes a global `cvViewer` object that can be called from Android WebView:

```javascript
// From Android WebView JavaScript interface
window.cvViewer.loadFrameFromAndroid(base64Image, fps, resolution, processingMode);
```

### Sample Data

The viewer automatically loads sample data on startup, including:
- Grayscale gradient image
- Simulated edge detection patterns  
- Colorful raw camera simulation
- Realistic FPS values (8-18 range)
- 1280x720 resolution display

## Architecture

### Classes

- **`CVProcessingViewer`**: Main viewer class that manages UI updates and data display
- **`FrameData`**: Interface defining the structure of frame data

### Key Methods

- `displayFrame(frameData)` - Updates UI with new frame data
- `loadFrameFromAndroid()` - Public method for Android integration
- `refreshData()` - Generates new sample data
- `toggleProcessingMode()` - Cycles through processing modes

## Browser Compatibility

- Chrome/Edge 60+
- Firefox 55+
- Safari 11+
- Modern mobile browsers

## Development

To make changes:

1. Edit `viewer.ts`
2. Run `tsc` to compile
3. Refresh browser to see changes

The TypeScript configuration uses strict mode with comprehensive error checking for robust development.
