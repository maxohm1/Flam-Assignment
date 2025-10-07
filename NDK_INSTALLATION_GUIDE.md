# 📦 NDK Installation Guide for Android Studio

## 🎯 Quick Guide - Install NDK in 5 Minutes

### Method 1: Through SDK Manager (RECOMMENDED)

#### Step 1: Open SDK Manager

**Option A - From Welcome Screen:**
```
1. Open Android Studio
2. Click "More Actions" (3 dots) at bottom right
3. Click "SDK Manager"
```

**Option B - From Open Project:**
```
1. Open Android Studio
2. Click "Tools" in top menu
3. Click "SDK Manager"
```

**Option C - Using Settings:**
```
1. Press Ctrl+Alt+S (or File → Settings)
2. Search for "SDK" in search box
3. Click "Android SDK"
```

---

#### Step 2: Navigate to SDK Tools Tab

In the SDK Manager window:
```
1. You'll see two tabs at top: "SDK Platforms" and "SDK Tools"
2. Click on "SDK Tools" tab
```

---

#### Step 3: Select NDK

In the SDK Tools list, find and check these items:

```
☑ NDK (Side by side)            ← CHECK THIS (Most Important!)
☑ CMake                          ← Also check this
☑ Android SDK Build-Tools        ← Should already be checked
☑ Android SDK Command-line Tools ← Should already be checked
```

**Important Notes:**
- **NDK (Side by side)** allows multiple NDK versions
- If you see just "NDK" (without "Side by side"), check that instead
- Latest version is usually best (27.x or 28.x as of 2024-2025)

---

#### Step 4: Apply and Install

```
1. Click "Apply" button at bottom right
2. A "Confirm Change" dialog will appear showing:
   - Package name
   - Download size (~1GB for NDK)
   - Install location
3. Click "OK" to confirm
4. Accept license agreements if prompted
5. Wait for download and installation (10-20 minutes)
```

**Progress Indicators:**
- You'll see a progress bar
- Download speed shown
- "Installing..." message

---

#### Step 5: Verify Installation

After installation completes:

```
1. Check that "NDK (Side by side)" has a checkmark ✓
2. Click "OK" to close SDK Manager
3. Note the install path shown (usually):
   C:\Users\Amit Mandal\AppData\Local\Android\Sdk\ndk\27.x.xxxxx
```

**You're done!** ✅

---

## 🔍 Verify NDK is Installed

### Check in Android Studio:

1. **Open Terminal in Android Studio** (bottom tab)
2. **Run this command:**

```powershell
# On Windows
dir "C:\Users\Amit Mandal\AppData\Local\Android\Sdk\ndk"
```

You should see folder(s) like: `27.0.12077973` or similar

### Alternative Check:

```powershell
# In PowerShell, check if NDK_HOME is set
$env:ANDROID_NDK_HOME
```

---

## 🛠️ Method 2: Let Gradle Install It Automatically

If you prefer, Gradle can download NDK automatically:

1. **Open your project** in Android Studio
2. **Let Gradle sync** - it will detect NDK is needed
3. **You'll see a notification:** "NDK not configured"
4. **Click "Install NDK"** in the notification
5. **Wait for download** (happens in background)

This is what was happening when you ran `gradlew.bat` earlier!

---

## 📋 Detailed Step-by-Step with What You'll See

### 🖥️ Visual Guide

#### Screen 1: Android Studio Welcome
```
┌─────────────────────────────────────────┐
│  Android Studio                          │
│                                          │
│  Projects                                │
│  ├─ Recent Project 1                    │
│  ├─ Recent Project 2                    │
│  └─ Assignment                          │
│                                          │
│  [ More Actions ▼ ]                     │
│                                          │
└─────────────────────────────────────────┘
```
**Click:** "More Actions" → "SDK Manager"

---

#### Screen 2: SDK Manager - SDK Platforms Tab
```
┌─────────────────────────────────────────┐
│  Settings for New Projects               │
│  ┌─────────────────────────────────┐   │
│  │ System Settings                  │   │
│  │ > Appearance & Behavior          │   │
│  │ > Android SDK    ←── YOU ARE HERE │   │
│  └─────────────────────────────────┘   │
│                                          │
│  Android SDK Location:                   │
│  C:\Users\Amit Mandal\AppData\Local\    │
│      Android\Sdk                         │
│                                          │
│  [SDK Platforms] [SDK Tools] ←── TABS   │
│                                          │
│  ☑ Android 13 (API 33)                  │
│  ☑ Android 12 (API 31)                  │
│  ☐ Android 11 (API 30)                  │
│                                          │
│            [Cancel] [Apply] [OK]         │
└─────────────────────────────────────────┘
```
**Click:** "SDK Tools" tab

---

#### Screen 3: SDK Manager - SDK Tools Tab
```
┌─────────────────────────────────────────┐
│  Settings for New Projects               │
│                                          │
│  [SDK Platforms] [SDK Tools] ←── NOW HERE│
│                                          │
│  Show Package Details ☐                 │
│                                          │
│  Name                         Installed │
│  ────────────────────────────────────── │
│  ☑ Android SDK Build-Tools       ✓     │
│  ☑ NDK (Side by side)            -   ←──│
│  ☑ CMake                          -   ←──│
│  ☑ Android Emulator               ✓     │
│  ☑ Android SDK Platform-Tools     ✓     │
│  ☐ Google Play services           -     │
│  ☐ Intel x86 Emulator Accelerator -     │
│                                          │
│            [Cancel] [Apply] [OK]         │
└─────────────────────────────────────────┘
```
**Check:** "NDK (Side by side)" and "CMake"  
**Click:** "Apply"

---

#### Screen 4: Confirm Change Dialog
```
┌─────────────────────────────────────────┐
│  Confirm Change                          │
│                                          │
│  The following components will be        │
│  installed:                              │
│                                          │
│  📦 NDK (Side by side) 27.0.12077973    │
│     Size: 1.2 GB                         │
│                                          │
│  📦 CMake 3.22.1                         │
│     Size: 85 MB                          │
│                                          │
│  Total download: ~1.3 GB                 │
│                                          │
│              [Cancel] [OK]               │
└─────────────────────────────────────────┘
```
**Click:** "OK"

---

#### Screen 5: License Agreement
```
┌─────────────────────────────────────────┐
│  Licenses                                │
│                                          │
│  Review licenses that have not been      │
│  accepted (1 of 1)                       │
│                                          │
│  ○ android-sdk-license                   │
│                                          │
│  ┌─────────────────────────────────┐   │
│  │ Terms and Conditions             │   │
│  │ ...license text...               │   │
│  │                                  │   │
│  │                                  │   │
│  └─────────────────────────────────┘   │
│                                          │
│  ○ Accept  ○ Decline                    │
│                                          │
│            [Cancel] [Next]               │
└─────────────────────────────────────────┘
```
**Select:** "Accept"  
**Click:** "Next"

---

#### Screen 6: Installing...
```
┌─────────────────────────────────────────┐
│  Component Installer                     │
│                                          │
│  Downloading...                          │
│                                          │
│  NDK (Side by side) 27.0.12077973       │
│  ████████████░░░░░░░░░░ 60%            │
│                                          │
│  Downloaded: 720 MB / 1200 MB           │
│  Speed: 5.2 MB/s                         │
│  Time remaining: ~2 minutes              │
│                                          │
│  [Background]                            │
└─────────────────────────────────────────┘
```
**Wait...** (Can take 10-20 minutes depending on internet speed)

---

#### Screen 7: Installation Complete
```
┌─────────────────────────────────────────┐
│  Component Installer                     │
│                                          │
│  ✓ NDK (Side by side) installed         │
│  ✓ CMake installed                       │
│                                          │
│  All components successfully installed!  │
│                                          │
│                           [Finish]       │
└─────────────────────────────────────────┘
```
**Click:** "Finish"

---

## 🎯 After Installation - Build Your Project

Now that NDK is installed:

### Option 1: Build in Android Studio
```
1. File → Sync Project with Gradle Files
2. Wait for sync (should be faster now)
3. Build → Make Project (Ctrl+F9)
4. Should build successfully! ✅
```

### Option 2: Build from Command Line
```powershell
# In PowerShell at project root:
.\gradlew.bat clean build
```

Should complete without NDK errors! ✅

---

## ⚙️ Advanced: Check NDK Version

After installation, you can check the exact version:

```powershell
# List installed NDK versions
dir "C:\Users\Amit Mandal\AppData\Local\Android\Sdk\ndk"

# Output example:
# 27.0.12077973
# 26.1.10909125  (if you have multiple versions)
```

---

## 🐛 Troubleshooting

### Issue 1: "NDK not found in SDK Manager"

**Solution:**
- Update Android Studio: Help → Check for Updates
- Make sure you're on SDK Tools tab (not SDK Platforms)
- Look for "NDK (Side by side)" specifically

### Issue 2: Download Fails or Stalls

**Solution:**
- Check internet connection
- Try again (it will resume)
- Download manually: https://developer.android.com/ndk/downloads
- Extract to: `C:\Users\Amit Mandal\AppData\Local\Android\Sdk\ndk\[version]`

### Issue 3: License Agreement Doesn't Show

**Solution:**
```powershell
# Accept licenses via command line:
cd "C:\Users\Amit Mandal\AppData\Local\Android\Sdk\tools\bin"
.\sdkmanager --licenses
```

### Issue 4: "NDK location not found"

**Solution:**
Add to `local.properties`:
```properties
ndk.dir=C\:\\Users\\Amit Mandal\\AppData\\Local\\Android\\Sdk\\ndk\\27.0.12077973
```

---

## 📊 Install Size Requirements

Make sure you have enough disk space:

| Component | Size |
|-----------|------|
| NDK | ~1.2 GB |
| CMake | ~85 MB |
| Build artifacts | ~500 MB |
| **Total** | **~2 GB** |

---

## ✅ Verification Checklist

After installation, verify:

- [ ] ✅ SDK Manager shows NDK with checkmark
- [ ] ✅ CMake is also checked
- [ ] ✅ Folder exists: `C:\Users\Amit Mandal\AppData\Local\Android\Sdk\ndk\`
- [ ] ✅ Version folder inside (e.g., `27.0.12077973`)
- [ ] ✅ Gradle sync completes without NDK errors
- [ ] ✅ Project builds successfully

---

## 🚀 Next Steps After NDK Installation

1. ✅ **Sync Gradle** - File → Sync Project with Gradle Files
2. ✅ **Build Project** - Build → Make Project
3. ✅ **Run on Device** - Click ▶️ to install and test
4. ✅ **Check Logcat** - Verify native library loads

---

## 💡 Pro Tips

- **Multiple Versions:** You can install multiple NDK versions side by side
- **Specific Version:** Click "Show Package Details" to choose specific NDK version
- **Background Install:** You can continue working while NDK downloads
- **Command Line:** Use `sdkmanager` for scriptable installation
- **Updates:** Check for NDK updates periodically

---

## 🎓 Understanding NDK

**What is NDK?**
- **N**ative **D**evelopment **K**it
- Allows C/C++ code in Android apps
- Your project uses it for OpenCV C++ integration
- Required for JNI (Java Native Interface)

**Why do you need it?**
- Your app has C++ code in `app/src/main/cpp/`
- OpenCV processing is in C++
- CMake builds the native libraries
- NDK provides the compiler and tools

---

**You're all set! 🎉 Install NDK and your project will build successfully!**

