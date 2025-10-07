# Web Viewer Fix Guide

## Issues Fixed ✅

### 1. Connection Status Indicator (Top Right)
**Problem:** Connection status was not visible in the top-right corner of the web viewer.

**Solution:**
- Added a dedicated connection status element in `index.html`
- Styled it with CSS including a pulsing animation
- Updated `viewer.ts` to properly initialize and update the status element
- The status now shows:
  - 🟢 **Green** = Connected
  - 🟡 **Yellow** = Disconnected
  - 🔴 **Red** = Connection Error

### 2. Live Camera Rendering
**Problem:** Live camera frames from the Android app were not displaying in real-time.

**Solution:**
- Fixed WebSocket connection handling
- Improved frame display logic in the viewer
- Added proper error handling and fallback mechanisms
- The viewer now:
  - Connects automatically via WebSocket
  - Receives frames in real-time
  - Updates stats dynamically
  - Shows server statistics in bottom-left corner

---

## How to Test the Fixes

### Step 1: Start the Web Server

Open PowerShell in the `web` directory and run:

```powershell
node server.js
```

You should see:
```
============================================================
CV Processing Server Started
============================================================
🚀 Server running on http://localhost:8080
🌐 Web Viewer: http://localhost:8080
📡 WebSocket: ws://localhost:8080
============================================================
```

### Step 2: Test the Connection

#### Option A: Use the Test Panel
1. Open your browser and go to: `http://localhost:8080/test-viewer.html`
2. The test panel will automatically check server health
3. Click each test button to verify:
   - ✅ Server Health Check
   - ✅ WebSocket Connection
   - ✅ Send Test Frame

#### Option B: Open the Main Viewer
1. Open your browser and go to: `http://localhost:8080`
2. You should see:
   - **Top-Right Corner:** Connection status indicator
   - **Center:** Sample grayscale image (until real frames arrive)
   - **Stats Panel:** FPS, Resolution, and Last Updated time
   - **Bottom-Left:** Server stats (appears when connected)

### Step 3: Verify Connection Status

The connection status in the **top-right corner** should show:
- Initially: "Connecting..." (gray)
- After 1-2 seconds: "Connected" (green with pulsing dot)

If it shows "Disconnected" or "Connection Error":
1. Make sure the server is running (`node server.js`)
2. Check that port 8080 is not blocked by firewall
3. Verify you're accessing `http://localhost:8080`

### Step 4: Send Test Frames

#### From Test Panel:
1. Open `http://localhost:8080/test-viewer.html`
2. Click "Send Test Frame to Server"
3. Then click "Open Main Viewer" to see the frame

#### From Android App:
The Android app should send frames to `http://YOUR_IP:8080/api/frame`

Example frame data format:
```json
{
  "image": "base64_encoded_image_data",
  "fps": 15.5,
  "resolution": "1920x1080",
  "processingMode": "GRAYSCALE",
  "timestamp": 1234567890
}
```

---

## Visual Indicators

### Top-Right: Connection Status
```
┌─────────────────────┐
│ ● Connected         │  ← Green with pulsing dot
└─────────────────────┘
```

### Bottom-Left: Server Stats
```
┌─────────────────────┐
│ Server Stats        │
│ Frames: 42          │
│ Clients: 1          │
│ Avg FPS: 15.2       │
│ Uptime: 2m 15s      │
└─────────────────────┘
```

---

## Troubleshooting

### Connection Status Not Showing
1. **Hard refresh the page:** Press `Ctrl + Shift + R` (Windows) or `Cmd + Shift + R` (Mac)
2. **Clear browser cache:** 
   - Chrome: `Ctrl + Shift + Delete`
   - Select "Cached images and files"
3. **Check browser console:**
   - Press `F12` to open DevTools
   - Look for errors in the Console tab

### WebSocket Connection Failed
1. **Verify server is running:**
   ```powershell
   # Check if server process is running
   Get-Process -Name "node" -ErrorAction SilentlyContinue
   ```

2. **Check port availability:**
   ```powershell
   # See what's using port 8080
   netstat -ano | findstr :8080
   ```

3. **Restart the server:**
   - Stop: `Ctrl + C` in the server terminal
   - Start: `node server.js`

### No Live Camera Frames
1. **Verify Android app configuration:**
   - Server URL should be `http://YOUR_COMPUTER_IP:8080/api/frame`
   - Replace `YOUR_COMPUTER_IP` with actual IP (not localhost)
   
2. **Check firewall:**
   ```powershell
   # Allow Node.js through Windows Firewall if prompted
   ```

3. **Test with curl:**
   ```powershell
   curl http://localhost:8080/api/health
   ```

### Images Not Displaying
1. **Check server logs:** Look for "Frame received" messages
2. **Verify image format:** Must be base64-encoded PNG/JPEG
3. **Check browser console:** Look for CORS or loading errors

---

## Network Configuration

### For Local Testing (Same Computer)
- Server: `http://localhost:8080`
- WebSocket: `ws://localhost:8080`

### For Android App (Different Device)
1. **Find your computer's IP address:**
   ```powershell
   ipconfig
   # Look for IPv4 Address (e.g., 192.168.1.100)
   ```

2. **Update Android app configuration:**
   - Server URL: `http://192.168.1.100:8080/api/frame`
   - Replace with your actual IP

3. **Ensure devices are on same network:**
   - Both must be connected to the same Wi-Fi

---

## API Endpoints

### GET /api/frame
Get the current/latest frame
```bash
curl http://localhost:8080/api/frame
```

### POST /api/frame
Upload a new frame (from Android app)
```bash
curl -X POST http://localhost:8080/api/frame \
  -H "Content-Type: application/json" \
  -d '{"image":"base64data","fps":15,"resolution":"1920x1080","processingMode":"GRAYSCALE","timestamp":1234567890}'
```

### GET /api/stats
Get server statistics
```bash
curl http://localhost:8080/api/stats
```

### GET /api/health
Health check endpoint
```bash
curl http://localhost:8080/api/health
```

### POST /api/clear
Clear all frame data
```bash
curl -X POST http://localhost:8080/api/clear
```

---

## Files Modified

1. **index.html**
   - Added connection status element in HTML
   - Added CSS styles for connection indicator
   - Added pulsing animation

2. **viewer.ts**
   - Updated `initializeElements()` to get connection status element
   - Improved `updateConnectionStatus()` to use CSS classes
   - Better error handling and logging

3. **viewer.js** (Compiled from viewer.ts)
   - Automatically updated by TypeScript compiler

---

## Quick Commands Reference

```powershell
# Start server
cd "N:\android studio project\Assignment\web"
node server.js

# Compile TypeScript (if you make changes)
npx tsc

# Test server health
curl http://localhost:8080/api/health

# Open viewer in browser
start http://localhost:8080

# Open test panel
start http://localhost:8080/test-viewer.html
```

---

## Success Checklist

- [x] Server starts without errors
- [x] Can access http://localhost:8080
- [x] Connection status shows in top-right (green when connected)
- [x] Sample image displays in center
- [x] Stats panel shows FPS, Resolution, Timestamp
- [x] Server stats appear in bottom-left
- [x] Test frame sends successfully from test panel
- [x] WebSocket connection works
- [x] Refresh button updates data
- [x] Toggle mode changes processing mode
- [x] Clear button clears display

---

## Need More Help?

1. **Check browser console:** Press `F12` → Console tab
2. **Check server logs:** Look at the terminal running `node server.js`
3. **Test with curl:** Use the API endpoints above
4. **Use test panel:** http://localhost:8080/test-viewer.html

The system is now ready for live camera streaming from your Android app! 🎉

