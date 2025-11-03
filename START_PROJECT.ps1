# Start Complete Project - Backend + Frontend Integration
# This script ensures backend is running before starting frontend

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "ScrapSail Full Stack Startup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Load environment variables if SET_ENV_VARIABLES.ps1 exists
$envScript = Join-Path $PSScriptRoot "SET_ENV_VARIABLES.ps1"
if (Test-Path $envScript) {
    Write-Host "Loading environment variables from SET_ENV_VARIABLES.ps1..." -ForegroundColor Yellow
    & $envScript
    Write-Host ""
}

$backendDir = $PSScriptRoot
$frontendDir = Join-Path (Split-Path $backendDir) "scrapsail-frontend-new"

# Check frontend directory
if (-not (Test-Path $frontendDir)) {
    Write-Host "⚠️  Frontend not found at: $frontendDir" -ForegroundColor Yellow
    $frontendDir = Read-Host "Enter frontend directory path"
}

Write-Host "[1/5] Checking prerequisites..." -ForegroundColor Yellow

# Check MySQL
$mysqlPath = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
if (Test-Path $mysqlPath) {
    & $mysqlPath -u root -pLikesh@2006 -e "SELECT 1;" 2>&1 | Out-Null
    if ($LASTEXITCODE -eq 0) {
        Write-Host "   ✅ MySQL is running" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  Starting MySQL..." -ForegroundColor Yellow
        Start-Service MySQL80 -ErrorAction SilentlyContinue
        Start-Sleep -Seconds 3
    }
}

# Check and clear port 8080 (Backend)
Write-Host "[2/5] Checking ports..." -ForegroundColor Yellow
$port8080 = netstat -ano | findstr ":8080" | findstr "LISTENING"
if ($port8080) {
    $processId = ($port8080 -split '\s+')[-1]
    Write-Host "   ⚠️  Port 8080 is in use (PID: $processId) - Clearing for backend..." -ForegroundColor Yellow
    taskkill /F /PID $processId 2>$null
    Start-Sleep -Seconds 2
}
Write-Host "   ✅ Port 8080 is available for backend" -ForegroundColor Green

# Check and clear port 3000 (Frontend)
$port3000 = netstat -ano | findstr ":3000" | findstr "LISTENING"
if ($port3000) {
    $processId3000 = ($port3000 -split '\s+')[-1]
    Write-Host "   ⚠️  Port 3000 is in use (PID: $processId3000) - Clearing for frontend..." -ForegroundColor Yellow
    taskkill /F /PID $processId3000 2>$null
    Start-Sleep -Seconds 2
}
Write-Host "   ✅ Port 3000 is available for frontend" -ForegroundColor Green

# Start Backend (using local profile for localhost MySQL)
Write-Host "[3/5] Starting Backend..." -ForegroundColor Yellow
$backendWindow = Start-Process powershell -ArgumentList @(
    "-NoExit",
    "-Command",
    "cd '$backendDir'; `$env:SPRING_PROFILES_ACTIVE='local'; Write-Host '=== BACKEND SERVER (Local Profile) ===' -ForegroundColor Cyan; Write-Host 'Using local MySQL database (localhost:3306/scrapsail)' -ForegroundColor Green; mvn spring-boot:run"
) -WindowStyle Normal -PassThru

Write-Host "   ✅ Backend starting (PID: $($backendWindow.Id))" -ForegroundColor Green

# Wait for backend
Write-Host "[4/5] Waiting for backend to start..." -ForegroundColor Yellow
$backendReady = $false
for ($i = 0; $i -lt 40; $i++) {
    Start-Sleep -Seconds 2
    try {
        $response = Invoke-WebRequest -Uri http://localhost:8080/health -UseBasicParsing -TimeoutSec 2 -ErrorAction Stop
        if ($response.StatusCode -eq 200) {
            Write-Host "   ✅ Backend is ready!" -ForegroundColor Green
            $backendReady = $true
            break
        }
    } catch {
        Write-Host "." -NoNewline -ForegroundColor Gray
    }
}

if (-not $backendReady) {
    Write-Host ""
    Write-Host "   ⚠️  Backend taking longer than expected" -ForegroundColor Yellow
    Write-Host "   Check backend window for status" -ForegroundColor Yellow
}

# Configure Frontend .env
Write-Host "[5/5] Configuring Frontend..." -ForegroundColor Yellow
$envFile = Join-Path $frontendDir ".env"
$envContent = @"
# Backend API Configuration
REACT_APP_API_URL=http://localhost:8080
REACT_APP_API_BASE_URL=http://localhost:8080
VITE_API_URL=http://localhost:8080

# Frontend Port Configuration (Always use port 3000)
PORT=3000

# HTTPS Configuration (disabled for local development)
HTTPS=false
"@

if (-not (Test-Path $envFile)) {
    Set-Content $envFile $envContent
    Write-Host "   ✅ Created .env file with port 3000" -ForegroundColor Green
} else {
    $currentContent = Get-Content $envFile -Raw -ErrorAction SilentlyContinue
    # Always update to ensure PORT=3000 is set
    if ($currentContent -notmatch "PORT=3000") {
        if ($currentContent -notmatch "REACT_APP_API_URL|VITE_API_URL") {
            Set-Content $envFile $envContent
            Write-Host "   ✅ Created .env file with all configurations" -ForegroundColor Green
        } else {
            # Add PORT=3000 if not present
            Add-Content $envFile "`nPORT=3000"
            Write-Host "   ✅ Added PORT=3000 to .env file" -ForegroundColor Green
        }
    } else {
        Write-Host "   ✅ .env file configured (port 3000 set)" -ForegroundColor Green
    }
}

# Start Frontend (Always on port 3000 - NO OTHER PORTS)
Write-Host ""
Write-Host "Starting Frontend on port 3000 ONLY..." -ForegroundColor Yellow

# Set PORT environment variable explicitly in the new PowerShell window
$frontendCommand = @"
cd '$frontendDir'
`$env:PORT='3000'
`$env:BROWSER='none'
Write-Host '=== FRONTEND (Port 3000 ONLY) ===' -ForegroundColor Cyan
Write-Host 'PORT=3000 is set. Frontend will use port 3000 only.' -ForegroundColor Green
npm start
"@

Start-Process powershell -ArgumentList @(
    "-NoExit",
    "-Command",
    $frontendCommand
) -WindowStyle Normal

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "✅ Both servers starting!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Backend:  http://localhost:8080" -ForegroundColor White
Write-Host "Frontend: http://localhost:3000" -ForegroundColor White
Write-Host ""
Write-Host "Watch the windows for:" -ForegroundColor Yellow
Write-Host "  Backend: 'Started BackendApplication'" -ForegroundColor Gray
Write-Host "  Frontend: Browser will open automatically" -ForegroundColor Gray
Write-Host ""

