# 📱 Install & Run the App

## ✅ Build Status: SUCCESS!

The Android app has been built successfully! The APK is ready to install.

---

## 📦 APK Location

Your debug APK is located at:
```
N:\android studio project\Assignment\app\build\outputs\apk\debug\app-debug.apk
```

---

## 🚀 Installation Options

### Option 1: Using Android Studio (Recommended)

1. **Open Android Studio**
2. **Open this project:** `N:\android studio project\Assignment`
3. **Connect your Android device:**
   - Enable USB Debugging on your phone:
     - Settings → About Phone → Tap "Build Number" 7 times
     - Settings → Developer Options → Enable "USB Debugging"
   - Connect phone via USB cable
   - Allow USB debugging prompt on phone

4. **Run the app:**
   - Click the green "Run" button (▶) in Android Studio
   - Or press `Shift + F10`
   - Select your device from the list

### Option 2: Using ADB (Command Line)

1. **Connect your device** (enable USB debugging as above)

2. **Verify device is connected:**
   ```powershell
   adb devices
   ```
   You should see your device listed.

3. **Install the APK:**
   ```powershell
   adb install "N:\android studio project\Assignment\app\build\outputs\apk\debug\app-debug.apk"
   ```

4. **Launch the app:**
   ```powershell
   adb shell am start -n max.ohm.assignment/.MainActivity
   ```

### Option 3: Manual Transfer (No Computer Required)

1. **Copy the APK to your phone:**
   - Connect phone to computer
   - Copy `app-debug.apk` to your phone's Download folder
   - Or upload to Google Drive/Cloud and download on phone

2. **Install on phone:**
   - Open File Manager on your phone
   - Navigate to Downloads folder
   - Tap on `app-debug.apk`
   - Allow installation from unknown sources if prompted
   - Tap "Install"

3. **Grant Permissions:**
   - When app opens, grant Camera permission
   - Grant any other permissions if requested

### Option 4: Using Android Emulator

1. **Start an emulator in Android Studio:**
   - Tools → Device Manager
   - Create a new Virtual Device or start an existing one
   - Wait for emulator to boot

2. **Run the app:**
   - Click "Run" button in Android Studio
   - Select the emulator from device list

---

## 📋 Pre-Installation Checklist

Before installing, ensure:

- [ ] **Web server is running** on your computer
  ```powershell
  cd "N:\android studio project\Assignment\web"
  node server.js
  ```

- [ ] **Both devices on same Wi-Fi network**
  - Computer connected to Wi-Fi
  - Android device connected to the **same** Wi-Fi
  - Not using mobile data!

- [ ] **Know your computer's IP address**
  ```powershell
  ipconfig | Select-String "IPv4"
  ```
  Should show: `192.168.86.130` (already configured in app)

- [ ] **Firewall allows Node.js**
  - Allow Node.js through Windows Firewall when prompted

---

## 🎯 Expected Behavior After Installation

### 1. First Launch:
- App requests Camera permission → **Grant it**
- App starts camera preview
- Shows "Attempting to discover web server..." in logs

### 2. Web Server Connection:
**If server found:**
```
✓ Web server connected: http://192.168.86.130:8080
```

**If server not found:**
```
Web server not available. Running locally only.

To connect:
1. Start web server: node server.js
2. Find your computer's IP: ipconfig
3. Update server URL in code
```

### 3. App Interface:
- Camera preview displays
- FPS counter shows current framerate
- Mode indicator shows "Mode: Grayscale Filter"
- "Toggle Mode" button to switch between modes
- Stats panel shows FPS, Resolution, Processing time

### 4. Web Viewer (if connected):
- Open browser: `http://192.168.86.130:8080`
- Should see live camera frames
- Connection status shows "Connected" (green)
- FPS updates in real-time

---

## 🔧 Troubleshooting

### Issue: "App not installed" error

**Solution:**
1. Uninstall any previous version:
   ```powershell
   adb uninstall max.ohm.assignment
   ```
2. Then reinstall:
   ```powershell
   adb install "N:\android studio project\Assignment\app\build\outputs\apk\debug\app-debug.apk"
   ```

### Issue: Device not showing in `adb devices`

**Solution:**
1. Check USB cable is connected properly
2. Enable USB Debugging on phone:
   - Settings → Developer Options → USB Debugging
3. Try a different USB cable or port
4. Restart adb:
   ```powershell
   adb kill-server
   adb start-server
   adb devices
   ```

### Issue: App crashes on startup

**Solution:**
1. Check Logcat for errors:
   ```powershell
   adb logcat | Select-String "MainActivity"
   ```
2. Make sure device has camera
3. Grant camera permission when prompted

### Issue: "Web server not available"

**Solution:**
1. Make sure server is running:
   ```powershell
   Get-Process -Name "node"
   ```
2. Check both devices on same Wi-Fi
3. Test from phone browser: `http://192.168.86.130:8080`
4. If IP changed, update in code and rebuild

---

## 📱 Testing the Full Pipeline

Once app is installed and running:

### 1. Test Local Functionality:
- [ ] Camera preview works
- [ ] FPS counter updates
- [ ] Toggle Mode button switches between:
  - Raw Camera Feed
  - Grayscale Filter
  - Canny Edge Detection
- [ ] Stats panel updates

### 2. Test Web Server Connection:
- [ ] Toast shows "✓ Web server connected"
- [ ] Check Logcat for connection logs:
   ```powershell
   adb logcat -s MainActivity:I WebServerClient:D
   ```

### 3. Test Web Viewer:
- [ ] Open `http://192.168.86.130:8080`
- [ ] Connection status shows green "Connected"
- [ ] Live camera frames appear
- [ ] FPS updates in real-time
- [ ] Toggle mode in app → frames update in viewer

### 4. Test Different Modes:
- [ ] RAW mode - Shows color camera feed
- [ ] GRAYSCALE - Shows black & white
- [ ] EDGE_DETECTION - Shows edge-detected image

---

## 🎥 Monitor App Activity

### View App Logs:
```powershell
# All app logs
adb logcat -s MainActivity:* WebServerClient:* CameraHandler:*

# Just connection logs
adb logcat -s MainActivity:I WebServerClient:D

# Clear logs first, then run
adb logcat -c && adb logcat -s MainActivity:*
```

### Monitor Web Server:
The server console will show:
```
New WebSocket client connected. Total clients: 1
Frame received: GRAYSCALE (1920x1080) - Total: 1
Frame received: GRAYSCALE (1920x1080) - Total: 2
...
```

---

## 🔄 Rebuilding the App

If you make code changes:

```powershell
# Clean and rebuild
cd "N:\android studio project\Assignment"
.\gradlew.bat clean assembleDebug

# Uninstall old version
adb uninstall max.ohm.assignment

# Install new version
adb install "app\build\outputs\apk\debug\app-debug.apk"
```

Or use Android Studio:
1. Build → Clean Project
2. Build → Rebuild Project
3. Run → Run 'app'

---

## 📊 Performance Expectations

On a typical Android device:
- **Camera FPS:** 15-30 FPS
- **Processing Time:** 10-50ms per frame
- **Web Viewer FPS:** 10-20 FPS (network limited)
- **Memory Usage:** ~100-200 MB
- **Battery Impact:** Moderate (camera + processing)

---

## 🎉 Success Indicators

Everything is working when you see:

**On Android Device:**
- Camera preview smooth
- FPS counter updating (10-30 FPS)
- Toast: "✓ Web server connected: http://192.168.86.130:8080"
- No error messages

**In Web Viewer:**
- Green "Connected" status (top-right)
- Live camera frames appearing
- FPS matching app (with slight delay)
- Frame counter incrementing

**In Server Console:**
```
Status: 1 clients, 150 frames processed, avg FPS: 15.2
```

---

## 📚 Additional Resources

- **Android Code:** `app/src/main/java/max/ohm/assignment/`
- **Web Viewer:** `web/index.html`
- **Server:** `web/server.js`
- **Troubleshooting:** `ANDROID_CONNECTION_FIX.md`
- **Web Guide:** `web/FIX_GUIDE.md`

---

## 🚀 Quick Commands

```powershell
# Check if device connected
adb devices

# Install APK
adb install "N:\android studio project\Assignment\app\build\outputs\apk\debug\app-debug.apk"

# Launch app
adb shell am start -n max.ohm.assignment/.MainActivity

# View logs
adb logcat -s MainActivity:I

# Uninstall app
adb uninstall max.ohm.assignment

# Check server running
Get-Process -Name "node"

# Test server
curl http://192.168.86.130:8080/api/health
```

---

## 💡 Pro Tips

1. **Keep screen on during testing:**
   - Settings → Developer Options → Stay Awake

2. **Use good lighting:**
   - Edge detection works best with good lighting

3. **Monitor battery:**
   - Camera + processing = battery drain
   - Keep charger nearby

4. **Multiple devices:**
   - Can connect multiple phones to same server
   - Each sends frames independently

5. **Web viewer on phone:**
   - Open Chrome on Android phone
   - Navigate to `http://192.168.86.130:8080`
   - View live feed from another phone

---

**Your app is ready to install! 🎉**

Choose your preferred installation method above and get started!

