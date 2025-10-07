# ⚡ Quick Start Guide

## 🎯 TL;DR - Fastest Way to Build & Test

### Using Android Studio (RECOMMENDED - 5 Steps)

```
1. Open Android Studio
2. File → Open → "N:\android studio project\Assignment"
3. Wait for Gradle sync (10-15 min first time)
4. Click Run ▶️ button
5. Grant camera permission when app launches
```

**That's it!** Android Studio handles everything automatically.

---

## 🔍 What to Expect

### First Time Setup
- ⏱️ **Gradle Sync:** 10-15 minutes (downloading NDK ~1GB + dependencies)
- 🔨 **First Build:** 5-10 minutes (compiling native code)
- ✅ **Subsequent Builds:** 30 seconds - 2 minutes

### When App Runs
1. **Permission Dialog** → Tap "Allow" for camera
2. **Black Screen** → Should quickly show camera feed
3. **FPS Counter** → Appears at top showing ~15-30 FPS
4. **Toggle Button** → Bottom center, tap to switch modes

---

## 📱 The 3 Modes

| Mode | What You See | How to Get There |
|------|-------------|------------------|
| **Raw** | Normal camera feed | Default on launch |
| **Grayscale** | Black & white | Tap "Toggle Mode" once |
| **Canny Edge** | White edges on black | Tap "Toggle Mode" twice |

---

## 🚨 Quick Troubleshooting

### Problem: Gradle Sync Taking Forever
- **Normal!** First sync downloads ~1.5GB
- Check internet connection
- Wait patiently (can take 20-30 minutes)

### Problem: Build Fails with NDK Error
```
Solution: Tools → SDK Manager → SDK Tools → Check "NDK (Side by side)" → Apply
```

### Problem: OpenCV Error
```kotlin
// In app/build.gradle.kts, change:
implementation("org.opencv:opencv:4.9.0")
// To:
implementation("org.opencv:opencv:4.8.0")
```

### Problem: App Crashes on Launch
1. Check Logcat for error
2. Build → Clean Project
3. Build → Rebuild Project
4. Try again

### Problem: Black Screen After Launch
- Grant camera permission
- Check Logcat for errors
- Make sure camera isn't being used by another app

---

## ✅ Success Indicators

You'll know it's working when you see:

**In Android Studio Build:**
```
BUILD SUCCESSFUL in Xm Ys
```

**In Logcat:**
```
MainActivity: Native library initialized: true, OpenCV version: 4.x.x
CameraHandler: Camera bound successfully
GLRenderer: OpenGL surface created
```

**On Device:**
- Camera feed visible
- FPS counter showing (10+ FPS)
- Toggle button works
- All 3 modes display correctly

---

## 📋 Pre-Flight Checklist

Before building, make sure:
- [ ] Android Studio is open
- [ ] Project is at `N:\android studio project\Assignment`
- [ ] Internet connection active (for downloads)
- [ ] ~5GB free disk space (for NDK and dependencies)
- [ ] Device/emulator ready with camera

---

## 🎬 Step-by-Step First Run

### Step 1: Open Project
```
Android Studio → File → Open → Browse to:
N:\android studio project\Assignment
→ Click OK
```

### Step 2: Wait for Sync
Look at bottom right of Android Studio:
- "Syncing..." - Just wait
- "Download NDK..." - Will take 10-15 min
- "Build finished successfully" - Ready to proceed!

### Step 3: Check SDK Components
```
Tools → SDK Manager → SDK Tools tab

Make sure these are checked:
✅ Android SDK Build-Tools
✅ NDK (Side by side)
✅ CMake
✅ Android SDK Platform (API 28 or higher)
```

### Step 4: Build
```
Build → Make Project (Ctrl+F9)

Or click the hammer icon: 🔨

Wait for "BUILD SUCCESSFUL"
```

### Step 5: Connect Device
**Physical Phone:**
1. Settings → About Phone → Tap "Build number" 7 times
2. Settings → Developer Options → Enable "USB Debugging"
3. Connect USB cable
4. Allow debugging on phone

**Or Emulator:**
1. Tools → Device Manager
2. Click ▶️ on existing device
3. Wait for emulator to boot

### Step 6: Run
```
Click green ▶️ button (or Shift+F10)

App will:
1. Build (if needed)
2. Install on device
3. Launch automatically
```

### Step 7: Grant Permission
When app opens:
```
Dialog: "Allow Assignment to take pictures and record video?"
→ Tap "Allow" or "While using the app"
```

### Step 8: Test Features
```
1. See camera feed? ✓
2. FPS showing at top? ✓
3. Tap "Toggle Mode" → Grayscale? ✓
4. Tap again → Edge detection? ✓
5. Tap again → Back to raw? ✓
```

**🎉 SUCCESS! Your app is working!**

---

## 🆘 Emergency Commands

If Android Studio acts weird:

```
File → Invalidate Caches / Restart → Invalidate and Restart
```

If build keeps failing:

```powershell
# In PowerShell at project root:
.\gradlew.bat clean
.\gradlew.bat build --info
```

If app won't install:

```powershell
adb uninstall max.ohm.assignment
.\gradlew.bat installDebug
```

---

## 💾 Project Files You Created

All in: `N:\android studio project\Assignment\`

```
✅ app/build.gradle.kts          - Dependencies config
✅ app/src/main/cpp/             - Native C++ code (4 files)
✅ app/src/main/java/.../        - Kotlin code (5 files)
✅ app/src/main/res/layout/      - UI layout
✅ AndroidManifest.xml           - Permissions
✅ README.md                     - Full documentation
✅ BUILD_TEST_GUIDE.md           - Detailed guide
✅ QUICK_START.md                - This file
```

---

## 📞 Need More Help?

- **Detailed Build Guide:** Read `BUILD_TEST_GUIDE.md`
- **Full Documentation:** Read `README.md`
- **Implementation Details:** Read `IMPLEMENTATION_SUMMARY.md`

---

## 🎓 What This App Does

This is a **professional Android R&D assessment project** demonstrating:

✅ **CameraX** - Modern camera API  
✅ **OpenCV C++** - Native image processing via JNI  
✅ **OpenGL ES 2.0** - Hardware-accelerated rendering  
✅ **Real-time Processing** - 15-30 FPS edge detection  
✅ **Professional Architecture** - Modular, documented, testable  

**Tech Stack:** Android + Kotlin + C++ + OpenCV + OpenGL + JNI + CMake

---

**You've got this! 💪 Follow the steps and you'll be running in no time!**

