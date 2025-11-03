# ScrapSail Project Startup Script
# This script starts both the backend and frontend servers

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Starting ScrapSail Project" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if MySQL is running
Write-Host "Checking MySQL service..." -ForegroundColor Yellow
$mysqlService = Get-Service -Name "MySQL80" -ErrorAction SilentlyContinue
if ($null -eq $mysqlService -or $mysqlService.Status -ne "Running") {
    Write-Host "MySQL service not running. Attempting to start..." -ForegroundColor Yellow
    try {
        Start-Service MySQL80 -ErrorAction Stop
        Write-Host "MySQL service started successfully!" -ForegroundColor Green
    } catch {
        Write-Host "Warning: Could not start MySQL service. Please start it manually." -ForegroundColor Red
        Write-Host "Run: Start-Service MySQL80" -ForegroundColor Yellow
    }
} else {
    Write-Host "MySQL service is running." -ForegroundColor Green
}

Write-Host ""
Write-Host "Starting Backend (Spring Boot)..." -ForegroundColor Yellow
Write-Host "Backend will run on: http://localhost:8080" -ForegroundColor Cyan
Write-Host ""

# Start backend in a new window
$backendScript = {
    Set-Location $using:PWD
    Set-Location scrapsail-backend
    mvn spring-boot:run
}

Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$PWD\scrapsail-backend'; mvn spring-boot:run"

# Wait a bit for backend to start
Start-Sleep -Seconds 5

Write-Host "Starting Frontend (React)..." -ForegroundColor Yellow
Write-Host "Frontend will run on: http://localhost:3000" -ForegroundColor Cyan
Write-Host ""

# Start frontend in a new window
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$PWD\scrapsail-frontend-new'; npm start"

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "   Both servers are starting!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""
Write-Host "Backend: http://localhost:8080" -ForegroundColor Cyan
Write-Host "Frontend: http://localhost:3000" -ForegroundColor Cyan
Write-Host ""
Write-Host "Note: Two PowerShell windows have opened for backend and frontend." -ForegroundColor Yellow
Write-Host "Close those windows to stop the servers." -ForegroundColor Yellow
Write-Host ""


