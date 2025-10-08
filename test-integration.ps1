# Android Web Integration Verification Script
# Tests all components of the Android-Web communication system

Write-Host "🚀 Android Web Integration Verification" -ForegroundColor Green
Write-Host "=" * 50

# Test 1: Check if server is running
Write-Host "`n1. Testing Web Server..." -ForegroundColor Yellow
try {
    $healthResponse = Invoke-RestMethod -Uri "http://localhost:8081/api/health" -TimeoutSec 5
    if ($healthResponse.success) {
        Write-Host "✅ Web server is running" -ForegroundColor Green
        Write-Host "   Uptime: $([math]::Round($healthResponse.uptime, 1))s" -ForegroundColor Gray
        Write-Host "   Clients: $($healthResponse.clients)" -ForegroundColor Gray
        Write-Host "   Frames: $($healthResponse.frames)" -ForegroundColor Gray
    } else {
        Write-Host "❌ Web server health check failed" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ Web server not accessible at http://localhost:8081" -ForegroundColor Red
    Write-Host "   Start server with: `$env:PORT=8081; npm run server" -ForegroundColor Gray
}

# Test 2: Check network connectivity to your IP
Write-Host "`n2. Testing Network Connectivity..." -ForegroundColor Yellow
$computerIP = "192.168.86.130"
try {
    $pingResult = Test-Connection -ComputerName $computerIP -Count 2 -Quiet
    if ($pingResult) {
        Write-Host "✅ Network connectivity to $computerIP" -ForegroundColor Green
    } else {
        Write-Host "❌ Cannot reach $computerIP" -ForegroundColor Red
    }
} catch {
    Write-Host "❌ Network test failed" -ForegroundColor Red
}

# Test 3: Check web server on network IP
Write-Host "`n3. Testing Web Server on Network IP..." -ForegroundColor Yellow
try {
    $networkHealthResponse = Invoke-RestMethod -Uri "http://$computerIP:8081/api/health" -TimeoutSec 5
    if ($networkHealthResponse.success) {
        Write-Host "✅ Web server accessible from network at http://$computerIP:8081" -ForegroundColor Green
    }
} catch {
    Write-Host "❌ Web server not accessible from network IP" -ForegroundColor Red
    Write-Host "   Check firewall settings for port 8081" -ForegroundColor Gray
}

# Test 4: Check Android WebServerClient file
Write-Host "`n4. Checking Android Integration Files..." -ForegroundColor Yellow
$webClientPath = "N:\android studio project\Assignment\app\src\main\java\max\ohm\assignment\network\WebServerClient.kt"
if (Test-Path $webClientPath) {
    Write-Host "✅ WebServerClient.kt exists" -ForegroundColor Green
    
    # Check if IP is configured correctly
    $content = Get-Content $webClientPath -Raw
    if ($content.Contains("192.168.86.130:8081")) {
        Write-Host "✅ WebServerClient configured with correct IP" -ForegroundColor Green
    } else {
        Write-Host "❌ WebServerClient needs IP configuration" -ForegroundColor Red
    }
} else {
    Write-Host "❌ WebServerClient.kt not found" -ForegroundColor Red
}

# Test 5: Check AndroidManifest permissions
Write-Host "`n5. Checking Android Permissions..." -ForegroundColor Yellow
$manifestPath = "N:\android studio project\Assignment\app\src\main\AndroidManifest.xml"
if (Test-Path $manifestPath) {
    $manifestContent = Get-Content $manifestPath -Raw
    
    if ($manifestContent.Contains("android.permission.INTERNET")) {
        Write-Host "✅ INTERNET permission added" -ForegroundColor Green
    } else {
        Write-Host "❌ INTERNET permission missing" -ForegroundColor Red
    }
    
    if ($manifestContent.Contains("android.permission.ACCESS_NETWORK_STATE")) {
        Write-Host "✅ ACCESS_NETWORK_STATE permission added" -ForegroundColor Green
    } else {
        Write-Host "❌ ACCESS_NETWORK_STATE permission missing" -ForegroundColor Red
    }
} else {
    Write-Host "❌ AndroidManifest.xml not found" -ForegroundColor Red
}

# Test 6: Test API endpoints
Write-Host "`n6. Testing API Endpoints..." -ForegroundColor Yellow
$endpoints = @("/api/health", "/api/stats", "/api/frame")
foreach ($endpoint in $endpoints) {
    try {
        $response = Invoke-WebRequest -Uri "http://localhost:8081$endpoint" -Method GET -TimeoutSec 3
        if ($response.StatusCode -eq 200) {
            Write-Host "✅ $endpoint - OK" -ForegroundColor Green
        } else {
            Write-Host "❌ $endpoint - Status: $($response.StatusCode)" -ForegroundColor Red
        }
    } catch {
        Write-Host "❌ $endpoint - Failed" -ForegroundColor Red
    }
}

# Test 7: Simulate Android frame upload
Write-Host "`n7. Testing Frame Upload..." -ForegroundColor Yellow
try {
    $testFrame = @{
        image = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mNkYPhfDwAChwGA60e6kgAAAABJRU5ErkJggg=="
        fps = 15.0
        resolution = "640x480"
        processingMode = "TEST"
        timestamp = [DateTimeOffset]::Now.ToUnixTimeMilliseconds()
    } | ConvertTo-Json

    $uploadResponse = Invoke-RestMethod -Uri "http://localhost:8081/api/frame" -Method POST -Body $testFrame -ContentType "application/json" -TimeoutSec 5
    
    if ($uploadResponse.success) {
        Write-Host "✅ Frame upload test successful" -ForegroundColor Green
        Write-Host "   Frame ID: $($uploadResponse.frameId)" -ForegroundColor Gray
    }
} catch {
    Write-Host "❌ Frame upload test failed" -ForegroundColor Red
}

# Test 8: Check ADB connection
Write-Host "`n8. Checking Android Device Connection..." -ForegroundColor Yellow
try {
    $adbDevices = adb devices 2>$null
    if ($adbDevices -match "device$") {
        Write-Host "✅ Android device connected via ADB" -ForegroundColor Green
    } else {
        Write-Host "❌ No Android devices connected" -ForegroundColor Red
        Write-Host "   Connect device and enable USB debugging" -ForegroundColor Gray
    }
} catch {
    Write-Host "❌ ADB not found or not working" -ForegroundColor Red
    Write-Host "   Install Android SDK Platform Tools" -ForegroundColor Gray
}

# Summary
Write-Host "`n" + "=" * 50
Write-Host "🎯 Integration Verification Complete!" -ForegroundColor Green
Write-Host "`nNext Steps:" -ForegroundColor Yellow
Write-Host "1. If web server not running: npm run server" -ForegroundColor Gray
Write-Host "2. Open Android Studio and build the project" -ForegroundColor Gray  
Write-Host "3. Run app on your Realme device" -ForegroundColor Gray
Write-Host "4. Check for connection toast in Android app" -ForegroundColor Gray
Write-Host "5. Open web viewer: http://localhost:8081" -ForegroundColor Gray
Write-Host "6. Watch for real-time frames from Android app!" -ForegroundColor Gray

# Open web viewer if server is running
try {
    $testConnection = Invoke-WebRequest -Uri "http://localhost:8081/api/health" -TimeoutSec 2
    if ($testConnection.StatusCode -eq 200) {
        Write-Host "`n🌐 Opening web viewer..." -ForegroundColor Green
        Start-Process "http://localhost:8081"
    }
} catch {
    # Server not running, skip opening browser
}
