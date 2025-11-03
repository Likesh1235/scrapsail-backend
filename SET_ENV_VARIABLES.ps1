# ============================================
# ScrapSail Environment Variables Setup Script
# ============================================
# Run this script to set environment variables for the current PowerShell session
# For permanent setup, set these in System Environment Variables

Write-Host "Setting ScrapSail Environment Variables..." -ForegroundColor Green

# Database Configuration
$env:DB_HOST = "localhost"
$env:DB_PORT = "3306"
$env:DB_NAME = "scrapsail"
$env:DB_USERNAME = "root"
$env:DB_PASSWORD = "Likesh@2006"  # CHANGE THIS TO YOUR ACTUAL PASSWORD

# Email Configuration
$env:MAIL_HOST = "smtp.gmail.com"
$env:MAIL_PORT = "587"
$env:MAIL_USERNAME = "likeshkanna74@gmail.com"  # CHANGE THIS IF NEEDED
$env:MAIL_PASSWORD = "rvoueevkbdwtiizl"  # CHANGE THIS TO YOUR ACTUAL APP PASSWORD

# Logging Configuration (Production)
$env:LOG_LEVEL = "INFO"
$env:HIBERNATE_SQL_LOG = "WARN"
$env:HIBERNATE_BINDER_LOG = "WARN"

# Error Handling (Production)
$env:INCLUDE_STACKTRACE = "never"
$env:INCLUDE_ERROR_MESSAGE = "on_param"
$env:INCLUDE_BINDING_ERRORS = "on_param"

Write-Host "Environment variables set successfully!" -ForegroundColor Green
Write-Host ""
Write-Host "To verify, run: Get-ChildItem Env: | Where-Object { `$_.Name -like 'DB_*' -or `$_.Name -like 'MAIL_*' }" -ForegroundColor Yellow
Write-Host ""
Write-Host "⚠️  IMPORTANT: Update DB_PASSWORD and MAIL_PASSWORD in this script before production!" -ForegroundColor Red

