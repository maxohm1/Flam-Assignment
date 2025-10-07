# 🚀 Build and Test Guide

## ⚙️ Prerequisites Check

### 1. Android Studio Setup
- ✅ Android Studio installed
- ✅ Android SDK installed (detected at: `C:\Users\Amit Mandal\AppData\Local\Android\Sdk`)
- ⏳ NDK installation in progress (will download automatically)

### 2. Required Components
Make sure these are installed via Android Studio SDK Manager:
1. **Android SDK Platform 28+** (API 28+)
2. **Android SDK Build-Tools 34+**
3. **NDK (Side by side)** - Latest version (downloading now)
4. **CMake** - Version 3.22.1+

## 📦 Building the Project

### Option 1: Using Android Studio (RECOMMENDED)

This is the easiest and most reliable method:

1. **Open Project in Android Studio**
   ```
   File → Open → Select "N:\android studio project\Assignment"
   ```

2. **Wait for Gradle Sync**
   - Android Studio will automatically sync Gradle
   - It will download NDK if not present
   - Downloads OpenCV, CameraX, and other dependencies
   - **First sync may take 10-15 minutes**

3. **Build the Project**
   - Click **Build → Make Project** (Ctrl+F9)
   - Or click the hammer icon 🔨
   - Wait for "BUILD SUCCESSFUL" message

4. **Check for Errors**
   - If errors appear in the "Build" tab, read them carefully
   - Most common issues are resolved by:
     - Tools → SDK Manager → Install missing components
     - File → Invalidate Caches / Restart
     - Build → Clean Project, then Build → Rebuild Project

### Option 2: Using Command Line

**⚠️ Note:** First build will take 15-30 minutes due to NDK download (~1GB)

1. **Clean Previous Builds**
   ```powershell
   .\gradlew.bat clean
   ```

2. **Build Debug APK**
   ```powershell
   .\gradlew.bat assembleDebug
   ```

3. **Check Build Output**
   - APK location: `app\build\outputs\apk\debug\app-debug.apk`
   - If successful, you'll see "BUILD SUCCESSFUL"

## 🔧 Troubleshooting Common Issues

### Issue 1: NDK Not Found
**Error:** `NDK is not installed`

**Solution:**
1. Open Android Studio
2. Tools → SDK Manager
3. SDK Tools tab
4. Check "NDK (Side by side)"
5. Click Apply → OK
6. Wait for download to complete

### Issue 2: CMake Not Found
**Error:** `CMake is not found`

**Solution:**
1. Tools → SDK Manager → SDK Tools
2. Check "CMake"
3. Apply and install

### Issue 3: OpenCV Not Found
**Error:** `Could not resolve org.opencv:opencv:4.9.0`

**Solution:**
This might happen if OpenCV 4.9.0 is not available. Update `app/build.gradle.kts`:
```kotlin
// Change from:
implementation("org.opencv:opencv:4.9.0")

// To:
implementation("org.opencv:opencv:4.8.0")
```

### Issue 4: Gradle Sync Failed
**Solution:**
1. File → Invalidate Caches / Restart
2. Close Android Studio
3. Delete `.gradle` folder in project root
4. Reopen Android Studio
5. Let it sync again

### Issue 5: Native Build Failed
**Error:** Issues with C++ compilation

**Solution:**
1. Check NDK is installed
2. Verify CMake version in `app/build.gradle.kts`:
   ```kotlin
   externalNativeBuild {
       cmake {
           version = "3.22.1"  // Make sure this version is installed
       }
   }
   ```

## 📱 Running on Device/Emulator

### Option A: Using Android Studio

1. **Connect Device** (Physical Device)
   - Enable Developer Options on your phone
   - Enable USB Debugging
   - Connect via USB
   - Allow USB debugging when prompted

2. **Or Start Emulator**
   - Tools → Device Manager
   - Click ▶️ on an existing emulator
   - Or create new one: "Create Device" → Pixel 6 → API 30+

3. **Run the App**
   - Select device from dropdown at top
   - Click Run ▶️ button (or Shift+F10)
   - Wait for app to install and launch

### Option B: Using Command Line

1. **Check Connected Devices**
   ```powershell
   adb devices
   ```

2. **Install APK**
   ```powershell
   .\gradlew.bat installDebug
   ```

3. **Launch App Manually**
   - Find "Assignment" app on device
   - Tap to open

## ✅ Testing Checklist

### 1. Initial Launch
- [ ] App launches without crashing
- [ ] Camera permission dialog appears
- [ ] Grant camera permission

### 2. Camera Feed
- [ ] Camera feed displays on screen
- [ ] Feed is smooth (not frozen)
- [ ] Proper orientation

### 3. Processing Modes
- [ ] **RAW Mode**: Normal camera feed displays
- [ ] **Toggle Button**: Tap once → switches to Grayscale
- [ ] **Grayscale Mode**: Feed is in black and white
- [ ] **Toggle Button**: Tap again → switches to Canny Edge
- [ ] **Canny Edge Mode**: Edges are detected and displayed

### 4. UI Elements
- [ ] FPS counter shows at top
- [ ] FPS value updates in real-time
- [ ] Resolution displays correctly
- [ ] Mode text changes when toggling
- [ ] Toggle button responds to taps

### 5. Performance
- [ ] FPS is 10+ (minimum requirement)
- [ ] Ideally 15-30 FPS
- [ ] No lag or stuttering
- [ ] Processing time < 100ms

### 6. Stability
- [ ] No crashes after 1 minute of use
- [ ] Toggle between modes works smoothly
- [ ] Press back button → app closes gracefully
- [ ] Reopen app → works again

## 📊 Monitoring & Debugging

### View Logs (Android Studio)
1. Open **Logcat** tab (bottom of screen)
2. Filter by package: `max.ohm.assignment`
3. Look for tags:
   - `MainActivity` - App lifecycle
   - `CameraHandler` - Camera operations
   - `GLRenderer` - OpenGL rendering
   - `NativeLib` - Native C++ logs
   - `OpenCVProcessor` - OpenCV operations

### Important Log Messages
```
✅ "Native library initialized"
✅ "OpenCV version: 4.x.x"
✅ "Camera bound successfully"
✅ "OpenGL surface created"
✅ "Canny edge detection processed: 640x480"
✅ "Processing mode changed to: CANNY_EDGE"
```

### Check Memory Usage
1. Run → Profile 'app'
2. Select "Memory" profiler
3. Monitor for memory leaks
4. Should stay under 100MB

## 🐛 Common Runtime Issues

### Issue 1: App Crashes on Launch
**Check Logcat for:**
- `UnsatisfiedLinkError` → Native library not loaded
- `ClassNotFoundException` → Missing dependency

**Solution:**
- Rebuild project: Build → Clean Project → Rebuild Project
- Check NDK was included in build

### Issue 2: Black Screen
**Possible Causes:**
- OpenGL initialization failed
- Camera permission denied

**Solution:**
- Check Logcat for OpenGL errors
- Uninstall app, reinstall, grant permissions

### Issue 3: Camera Not Working
**Check:**
- Camera permission granted
- Device has camera
- No other app using camera

**Solution:**
- Settings → Apps → Assignment → Permissions → Camera → Allow
- Close other camera apps

### Issue 4: Edge Detection Not Working
**Check Logcat for:**
- "Failed to process frame"
- OpenCV errors

**Solution:**
- Verify OpenCV dependency downloaded
- Check if native library loads successfully

## 📈 Expected Results

### Performance Targets
| Metric | Target | Good | Excellent |
|--------|--------|------|-----------|
| FPS | 10+ | 15-25 | 25-30 |
| Processing Time | <100ms | <50ms | <30ms |
| Memory Usage | <150MB | <100MB | <80MB |
| APK Size | <80MB | <60MB | <50MB |

### Visual Results

**Raw Mode:**
- Clear camera feed
- Natural colors
- Smooth motion

**Grayscale Mode:**
- Black and white feed
- Same smoothness as raw

**Canny Edge Mode:**
- White edges on black background
- Clear object boundaries
- Detects movement

## 🎯 Success Criteria

Your implementation is successful if:
1. ✅ App builds without errors
2. ✅ App installs on device/emulator
3. ✅ Camera permission can be granted
4. ✅ Camera feed displays
5. ✅ All 3 modes work (Raw, Grayscale, Canny)
6. ✅ Toggle button switches modes
7. ✅ FPS counter shows 10+ FPS
8. ✅ No crashes during normal use

## 🔄 Quick Rebuild Commands

```powershell
# Clean and rebuild everything
.\gradlew.bat clean assembleDebug

# Install on device
.\gradlew.bat installDebug

# Uninstall first, then install
.\gradlew.bat uninstallDebug installDebug

# Build and install in one command
.\gradlew.bat clean build installDebug
```

## 💡 Tips

1. **First Build Takes Time** - Be patient, NDK download is ~1GB
2. **Use Android Studio** - Better error messages and debugging
3. **Check Logcat** - Most issues are visible in logs
4. **Physical Device Preferred** - Better performance than emulator
5. **Good Lighting** - Helps with edge detection
6. **Point at Objects** - Edges are more visible with contrast

## 📞 Need Help?

If you encounter issues:
1. Check the error message carefully
2. Search error in Logcat
3. Verify all SDK components installed
4. Try "Invalidate Caches / Restart" in Android Studio
5. Clean and rebuild project

---

**Good luck with building and testing! 🚀**

