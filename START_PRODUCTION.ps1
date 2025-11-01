# ============================================
# ScrapSail Production Startup Script
# ============================================
# This script starts the application with environment variables
# Make sure to set all required environment variables before running

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "ScrapSail Production Startup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check for required environment variables
$requiredVars = @("DB_PASSWORD")
$missingVars = @()

foreach ($var in $requiredVars) {
    if (-not (Get-ChildItem Env: | Where-Object { $_.Name -eq $var })) {
        $missingVars += $var
    }
}

if ($missingVars.Count -gt 0) {
    Write-Host "❌ Missing required environment variables:" -ForegroundColor Red
    foreach ($var in $missingVars) {
        Write-Host "   - $var" -ForegroundColor Red
    }
    Write-Host ""
    Write-Host "Please run SET_ENV_VARIABLES.ps1 first or set them manually." -ForegroundColor Yellow
    Write-Host "Example:" -ForegroundColor Yellow
    Write-Host "   `$env:DB_PASSWORD = 'your_password'" -ForegroundColor Gray
    Write-Host "   `$env:MAIL_PASSWORD = 'your_mail_password'" -ForegroundColor Gray
    exit 1
}

Write-Host "✅ Environment variables configured" -ForegroundColor Green
Write-Host ""

# Show current configuration (masked passwords)
Write-Host "Current Configuration:" -ForegroundColor Yellow
$dbHost = if ($env:DB_HOST) { $env:DB_HOST } else { "localhost (default)" }
$dbName = if ($env:DB_NAME) { $env:DB_NAME } else { "scrapsail (default)" }
$dbUser = if ($env:DB_USERNAME) { $env:DB_USERNAME } else { "root (default)" }
$dbPass = if ($env:DB_PASSWORD) { "***" } else { "NOT SET" }
$mailUser = if ($env:MAIL_USERNAME) { $env:MAIL_USERNAME } else { "NOT SET" }
$mailPass = if ($env:MAIL_PASSWORD) { "***" } else { "NOT SET" }
$logLevel = if ($env:LOG_LEVEL) { $env:LOG_LEVEL } else { "INFO (default)" }

Write-Host "   Database: $dbUser@$dbHost/$dbName (Password: $dbPass)" -ForegroundColor Gray
Write-Host "   Email: $mailUser (Password: $mailPass)" -ForegroundColor Gray
Write-Host "   Log Level: $logLevel" -ForegroundColor Gray
Write-Host ""

# Check MySQL connection
Write-Host "[1/3] Checking MySQL connection..." -ForegroundColor Yellow
$mysqlPath = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
if (Test-Path $mysqlPath) {
    $dbPassword = if ($env:DB_PASSWORD) { $env:DB_PASSWORD } else { "" }
    if ($dbPassword) {
        & $mysqlPath -u $dbUser -p$dbPassword -e "SELECT 1;" 2>&1 | Out-Null
        if ($LASTEXITCODE -eq 0) {
            Write-Host "   ✅ MySQL connection successful" -ForegroundColor Green
        } else {
            Write-Host "   ⚠️  MySQL connection failed - check credentials" -ForegroundColor Yellow
        }
    } else {
        Write-Host "   ⚠️  DB_PASSWORD not set" -ForegroundColor Yellow
    }
} else {
    Write-Host "   ⚠️  MySQL not found at default path" -ForegroundColor Yellow
}

# Check and clear port 8080
Write-Host "[2/3] Checking port 8080..." -ForegroundColor Yellow
$port8080 = netstat -ano | findstr ":8080" | findstr "LISTENING"
if ($port8080) {
    $processId = ($port8080 -split '\s+')[-1]
    Write-Host "   ⚠️  Port 8080 is in use (PID: $processId) - Clearing..." -ForegroundColor Yellow
    taskkill /F /PID $processId 2>$null
    Start-Sleep -Seconds 2
}
Write-Host "   ✅ Port 8080 is available" -ForegroundColor Green

# Build project
Write-Host "[3/3] Building project..." -ForegroundColor Yellow
$buildResult = mvn clean package -DskipTests 2>&1 | Select-String -Pattern "BUILD SUCCESS|BUILD FAILURE" | Select-Object -First 1
if ($buildResult -match "BUILD SUCCESS") {
    Write-Host "   ✅ Build successful" -ForegroundColor Green
} else {
    Write-Host "   ❌ Build failed - check errors above" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Starting application..." -ForegroundColor Yellow
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "✅ Application Starting!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Start the application
java -jar target/scrapsail-backend-0.0.1-SNAPSHOT.jar

