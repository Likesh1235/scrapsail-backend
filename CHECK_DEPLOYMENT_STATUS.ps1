# ============================================
# ScrapSail Deployment Status Checker
# ============================================

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "ScrapSail Deployment Status Check" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check environment variables
Write-Host "[1/4] Checking Environment Variables..." -ForegroundColor Yellow
$dbPasswordSet = [bool]$env:DB_PASSWORD
$mailPasswordSet = [bool]$env:MAIL_PASSWORD

if ($dbPasswordSet) {
    Write-Host "   ✅ DB_PASSWORD is set" -ForegroundColor Green
} else {
    Write-Host "   ❌ DB_PASSWORD is NOT set" -ForegroundColor Red
}

if ($mailPasswordSet) {
    Write-Host "   ✅ MAIL_PASSWORD is set" -ForegroundColor Green
} else {
    Write-Host "   ⚠️  MAIL_PASSWORD is NOT set (email may not work)" -ForegroundColor Yellow
}

Write-Host ""

# Check if JAR exists
Write-Host "[2/4] Checking Build..." -ForegroundColor Yellow
$jarPath = "target/scrapsail-backend-0.0.1-SNAPSHOT.jar"
if (Test-Path $jarPath) {
    $jarInfo = Get-Item $jarPath
    Write-Host "   ✅ JAR file exists: $($jarInfo.Name)" -ForegroundColor Green
    Write-Host "   📦 Size: $([math]::Round($jarInfo.Length / 1MB, 2)) MB" -ForegroundColor Gray
    Write-Host "   📅 Modified: $($jarInfo.LastWriteTime)" -ForegroundColor Gray
} else {
    Write-Host "   ❌ JAR file not found. Run: mvn clean package" -ForegroundColor Red
}

Write-Host ""

# Check if port 8080 is in use
Write-Host "[3/4] Checking Port 8080..." -ForegroundColor Yellow
$port8080 = netstat -ano | findstr ":8080" | findstr "LISTENING"
if ($port8080) {
    $processId = ($port8080 -split '\s+')[-1]
    $process = Get-Process -Id $processId -ErrorAction SilentlyContinue
    if ($process) {
        Write-Host "   ✅ Port 8080 is in use by PID: $processId ($($process.ProcessName))" -ForegroundColor Green
        Write-Host "   🕐 Process started: $($process.StartTime)" -ForegroundColor Gray
    } else {
        Write-Host "   ⚠️  Port 8080 is in use by PID: $processId (process info unavailable)" -ForegroundColor Yellow
    }
} else {
    Write-Host "   ❌ Port 8080 is NOT in use - Application may not be running" -ForegroundColor Red
}

Write-Host ""

# Test health endpoint
Write-Host "[4/4] Testing Application Health..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri http://localhost:8080/api/health -UseBasicParsing -TimeoutSec 5 -ErrorAction Stop
    Write-Host "   ✅ Application is responding!" -ForegroundColor Green
    Write-Host "   📡 Status Code: $($response.StatusCode)" -ForegroundColor Gray
    Write-Host "   🌐 URL: http://localhost:8080" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "✅ DEPLOYMENT SUCCESSFUL!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
} catch {
    $errorMessage = $_.Exception.Message
    if ($errorMessage -match "connection") {
        Write-Host "   ⏳ Application is starting... (Connection refused - this is normal during startup)" -ForegroundColor Yellow
        Write-Host "   Tip: Wait 30-60 seconds and run this script again" -ForegroundColor Yellow
    } elseif ($errorMessage -match "404") {
        Write-Host "   ⚠️  Application is running but /api/health endpoint not found" -ForegroundColor Yellow
        Write-Host "   🌐 Try: http://localhost:8080" -ForegroundColor Cyan
    } else {
        Write-Host "   ❌ Cannot connect to application: $errorMessage" -ForegroundColor Red
    }
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Yellow
    Write-Host "⏳ DEPLOYMENT IN PROGRESS" -ForegroundColor Yellow
    Write-Host "========================================" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "📝 Next Steps:" -ForegroundColor Cyan
Write-Host "   • If application is running, access it at: http://localhost:8080" -ForegroundColor White
Write-Host "   • To view logs, check the background process window" -ForegroundColor White
Write-Host "   • To restart, run: .\START_PRODUCTION.ps1" -ForegroundColor White
Write-Host ""

