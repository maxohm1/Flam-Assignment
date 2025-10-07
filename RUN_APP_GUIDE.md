# 🚀 Run Your App - Step by Step Guide

## ✅ Prerequisites Complete!
- ✅ NDK installed
- ✅ CMake installed
- ✅ Project code ready

Now let's build and run! 🎉

---

## 🎯 Method 1: Run in Android Studio (RECOMMENDED)

### Step 1: Open Project in Android Studio

```
1. Open Android Studio
2. File → Open
3. Navigate to: N:\android studio project\Assignment
4. Click OK
5. Wait for project to load
```

---

### Step 2: Sync Project with Gradle

**Automatic Sync:**
- Android Studio will automatically sync when project opens
- Look at bottom right: "Syncing..." → "Sync successful"

**Manual Sync (if needed):**
```
File → Sync Project with Gradle Files
```

**Expected output:**
```
Gradle sync started
> Configure project :app
Checking the license for package NDK (Side by side) 27.0.12077973
License for package NDK (Side by side) 27.0.12077973 accepted.
...
BUILD SUCCESSFUL in Xs
```

**⏱️ Time:** 2-5 minutes first time, 30 seconds after

---

### Step 3: Build the Project

```
Build → Make Project
```

**Or press:** `Ctrl+F9`

**Or click:** 🔨 (Hammer icon) in toolbar

**What happens:**
1. ✅ Gradle downloads dependencies (OpenCV, CameraX)
2. ✅ CMake compiles C++ code
3. ✅ Native libraries built (.so files)
4. ✅ Kotlin code compiled
5. ✅ APK created

**Expected output in Build window:**
```
> Task :app:buildCMakeDebug[arm64-v8a]
C/C++: Build native-lib
C/C++: Build opencv-processor
> Task :app:compileDebugKotlin
> Task :app:mergeDebugResources
> Task :app:assembleDebug
BUILD SUCCESSFUL in Xm Ys
```

**⏱️ Time:** 5-10 minutes first build, 1-2 minutes after

---

### Step 4: Connect Device or Start Emulator

#### Option A: Physical Device (RECOMMENDED)

**Enable Developer Options:**
1. On your phone: Settings → About Phone
2. Tap "Build number" 7 times
3. You'll see "You are now a developer!"

**Enable USB Debugging:**
1. Settings → Developer Options
2. Enable "USB Debugging"
3. Connect phone via USB cable
4. On phone, allow USB debugging when prompted

**Verify in Android Studio:**
- Top toolbar: Device dropdown should show your phone
- Or: View → Tool Windows → Device Manager

#### Option B: Emulator

```
1. Tools → Device Manager
2. Click ▶️ on existing emulator
3. Wait for emulator to boot (2-3 minutes)
```

**If no emulator exists:**
```
1. Tools → Device Manager
2. Click "Create Device"
3. Select: Pixel 6 or Pixel 7
4. Download system image: API 30 or higher
5. Finish and start emulator
```

---

### Step 5: Run the App! 🎉

```
Click the green ▶️ Run button
```

**Or press:** `Shift+F10`

**Or:** Run → Run 'app'

**What happens:**
1. ✅ App builds (if not already built)
2. ✅ APK installed on device/emulator
3. ✅ App launches automatically
4. ✅ You'll see the camera permission dialog

**⏱️ Time:** 30 seconds - 2 minutes

---

### Step 6: Grant Camera Permission

**On Device/Emulator:**

```
┌─────────────────────────────────────┐
│  Allow Assignment to take           │
│  pictures and record video?         │
│                                     │
│  [ Don't Allow ]  [ Allow ]        │
└─────────────────────────────────────┘
```

**Tap: "Allow" or "While using the app"**

---

### Step 7: Test the App! ✅

**You should see:**
1. ✅ Camera feed displaying
2. ✅ FPS counter at top (showing ~15-30 FPS)
3. ✅ Mode text: "Mode: Raw Camera Feed"
4. ✅ Resolution: "640x480" or similar
5. ✅ Toggle button at bottom

**Test modes:**
1. **Raw Mode** (default): Normal camera view
2. **Tap "Toggle Mode"** → Grayscale (black & white)
3. **Tap again** → Canny Edge Detection (white edges)
4. **Tap again** → Back to Raw

---

## 🔍 Monitor Logs (Logcat)

### View Logs in Android Studio:

```
1. Click "Logcat" tab at bottom
2. Filter by: "max.ohm.assignment"
3. Watch for these messages:
```

**Expected logs:**
```
MainActivity: Native library initialized: true, OpenCV version: 4.9.0
CameraHandler: Camera bound successfully
GLRenderer: OpenGL surface created
OpenCVProcessor: Canny edge detection processed: 640x480
CameraHandler: Processing mode changed to: CANNY_EDGE
```

**FPS logs:**
```
FPSCounter: Current FPS: 25.3
```

---

## 🎯 Method 2: Build from Command Line

### Step 1: Clean Build

```powershell
.\gradlew.bat clean
```

### Step 2: Build Debug APK

```powershell
.\gradlew.bat assembleDebug
```

**⏱️ Time:** 5-10 minutes first time

**APK Location:**
```
app\build\outputs\apk\debug\app-debug.apk
```

### Step 3: Install on Device

**Check device connected:**
```powershell
adb devices
```

**Install APK:**
```powershell
.\gradlew.bat installDebug
```

**Or manually:**
```powershell
adb install app\build\outputs\apk\debug\app-debug.apk
```

### Step 4: Launch App

**On device:** Find "Assignment" app and tap to open

**Or via adb:**
```powershell
adb shell am start -n max.ohm.assignment/.MainActivity
```

---

## ✅ Success Indicators

### In Build Output:
```
BUILD SUCCESSFUL in Xm Ys
34 actionable tasks: 34 executed
```

### In Logcat:
```
✅ Native library initialized
✅ OpenCV version: 4.9.0
✅ Camera bound successfully
✅ OpenGL surface created
✅ Frame processing started
```

### On Device:
- ✅ Camera feed visible
- ✅ FPS counter showing numbers
- ✅ No crashes
- ✅ Toggle button works

---

## 🐛 Troubleshooting

### Issue 1: Build Fails with OpenCV Error

**Error:** `Could not resolve: org.opencv:opencv:4.9.0`

**Solution:** Update to stable version in `app/build.gradle.kts`:
```kotlin
// Change from:
implementation("org.opencv:opencv:4.9.0")

// To:
implementation("org.opencv:opencv:4.8.0")
```

Then: File → Sync Project with Gradle Files

---

### Issue 2: Native Build Failed

**Error:** `Build command failed` or `CMake error`

**Check:**
1. NDK is installed: Tools → SDK Manager → SDK Tools → NDK checked
2. CMake is installed: Same location, CMake checked

**Solution:**
```
Build → Clean Project
Build → Rebuild Project
```

---

### Issue 3: App Crashes on Launch

**Check Logcat for:**
- `UnsatisfiedLinkError` → Native library not loaded

**Solution:**
1. Build → Clean Project
2. Build → Rebuild Project
3. Run → Run 'app'

**Verify .so files exist:**
```powershell
dir app\build\intermediates\cmake\debug\obj
```

Should show folders: arm64-v8a, armeabi-v7a, x86, x86_64

---

### Issue 4: Camera Permission Denied

**On device:**
```
Settings → Apps → Assignment → Permissions → Camera → Allow
```

Then relaunch app.

---

### Issue 5: Black Screen After Permission

**Possible causes:**
- Camera in use by another app
- OpenGL initialization failed

**Solution:**
1. Close other camera apps
2. Check Logcat for errors
3. Restart app

---

### Issue 6: No Device Found

**Physical Device:**
- Enable USB Debugging on phone
- Connect USB cable
- On phone: Allow debugging
- Run: `adb devices` to verify

**Emulator:**
- Tools → Device Manager
- Start an emulator
- Wait for full boot

---

## 📊 Performance Expectations

### Normal Performance:
| Metric | Expected |
|--------|----------|
| FPS | 15-30 |
| Resolution | 640x480 |
| Frame Time | 30-60ms |
| Memory | 60-100MB |

### If FPS is Low (<10):
- Try on physical device (better than emulator)
- Check if other apps are running
- Restart device

---

## 🎬 Complete First Run Walkthrough

### 1️⃣ **Open Android Studio**
```
Start → Android Studio
```

### 2️⃣ **Open Project**
```
File → Open → "N:\android studio project\Assignment" → OK
```

### 3️⃣ **Wait for Sync**
```
Bottom status bar: "Syncing..." → "Sync successful" (2-5 min)
```

### 4️⃣ **Build Project**
```
Build → Make Project (or Ctrl+F9)
Wait for "BUILD SUCCESSFUL" (5-10 min first time)
```

### 5️⃣ **Connect Device**
```
Physical phone: Enable USB debugging, connect USB
OR
Emulator: Tools → Device Manager → Start emulator
```

### 6️⃣ **Run App**
```
Click green ▶️ button (or Shift+F10)
Wait for installation (30 sec)
```

### 7️⃣ **Grant Permission**
```
On device: Tap "Allow" for camera
```

### 8️⃣ **Test Features**
```
✓ See camera feed
✓ Check FPS counter
✓ Tap "Toggle Mode" button
✓ Verify Grayscale works
✓ Verify Canny Edge works
```

### 9️⃣ **Success!** 🎉
```
App is running perfectly!
```

---

## 📱 Testing Checklist

Go through this checklist to verify everything works:

- [ ] App builds without errors
- [ ] App installs on device
- [ ] Camera permission granted
- [ ] Camera feed displays
- [ ] FPS counter shows (10+ FPS)
- [ ] Resolution text shows
- [ ] Toggle button visible
- [ ] Raw mode works (default)
- [ ] Tap toggle → Grayscale mode works
- [ ] Tap toggle → Canny Edge mode works
- [ ] Tap toggle → Back to Raw mode
- [ ] No crashes after 1 minute
- [ ] Rotating device works
- [ ] Pressing back button exits app

**All checked?** ✅ **Perfect! Your app is working!** 🎉

---

## 🔄 Quick Commands Reference

### Build and Run:
```powershell
# Clean and build
.\gradlew.bat clean assembleDebug

# Install on device
.\gradlew.bat installDebug

# Uninstall and reinstall
.\gradlew.bat uninstallDebug installDebug

# View logs
adb logcat -s MainActivity CameraHandler GLRenderer
```

### Shortcuts in Android Studio:
```
Ctrl+F9       Build Project
Shift+F10     Run App
Alt+4         Open Run window
Alt+6         Open Logcat
Ctrl+Shift+A  Find Action
```

---

## 🎓 What's Happening Behind the Scenes

When you click Run:

1. **Gradle Build**
   - Compiles Kotlin code
   - Runs CMake to compile C++ code
   - Links OpenCV libraries
   - Creates native .so files
   - Packages everything into APK

2. **ADB Install**
   - Transfers APK to device
   - Installs application
   - Grants app permissions

3. **App Launch**
   - MainActivity starts
   - Loads native library (libnative-lib.so)
   - Initializes OpenCV
   - Requests camera permission
   - Sets up OpenGL renderer
   - Starts camera feed
   - Begins processing loop

---

## 💡 Pro Tips

1. **Use Physical Device** - Much better performance than emulator
2. **Watch Logcat** - See what's happening in real-time
3. **Good Lighting** - Edge detection works better with light
4. **Point at Objects** - Edges show up better with contrast
5. **Build Incrementally** - After first build, builds are much faster

---

## 🎉 Success!

**If you see the camera feed with the FPS counter, congratulations!** 

You've successfully:
✅ Set up Android NDK development
✅ Integrated OpenCV C++ processing
✅ Implemented OpenGL ES rendering
✅ Created a real-time image processing app

**This is a professional-grade Android R&D project!** 🚀

---

**Ready to run?** Follow the steps above and you'll see your app in action! 📱✨

