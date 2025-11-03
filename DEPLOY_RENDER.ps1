# ScrapSail Backend - Render Deployment Script
# This script automates the deployment to Render

param(
    [switch]$InstallCLI,
    [switch]$Deploy,
    [switch]$Status
)

$ErrorActionPreference = "Stop"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   ScrapSail Backend - Render Deploy" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check if Render CLI is installed
$renderInstalled = $false
try {
    $null = Get-Command render -ErrorAction Stop
    $renderInstalled = $true
    $version = render --version 2>&1
    Write-Host "✅ Render CLI found: $version" -ForegroundColor Green
} catch {
    Write-Host "❌ Render CLI not installed" -ForegroundColor Yellow
}

# Install Render CLI if requested
if ($InstallCLI -or (-not $renderInstalled)) {
    Write-Host ""
    Write-Host "Installing Render CLI..." -ForegroundColor Yellow
    try {
        npm install -g @render/cli
        Write-Host "✅ Render CLI installed successfully!" -ForegroundColor Green
        $renderInstalled = $true
    } catch {
        Write-Host "❌ Failed to install Render CLI. Please install manually:" -ForegroundColor Red
        Write-Host "   npm install -g @render/cli" -ForegroundColor Yellow
        exit 1
    }
}

# Verify repository is ready
Write-Host ""
Write-Host "Checking repository setup..." -ForegroundColor Yellow

if (-not (Test-Path "render.yaml")) {
    Write-Host "❌ render.yaml not found!" -ForegroundColor Red
    exit 1
}
Write-Host "✅ render.yaml found" -ForegroundColor Green

if (-not (Test-Path ".mvnw" -PathType Leaf) -and -not (Test-Path "mvnw.cmd")) {
    Write-Host "⚠️  Maven wrapper not found. Generating..." -ForegroundColor Yellow
    mvn wrapper:wrapper
}
Write-Host "✅ Maven wrapper ready" -ForegroundColor Green

if (-not (Test-Path "pom.xml")) {
    Write-Host "❌ pom.xml not found!" -ForegroundColor Red
    exit 1
}
Write-Host "✅ pom.xml found" -ForegroundColor Green

# Check git status
try {
    $gitStatus = git status --porcelain 2>&1
    if ($gitStatus) {
        Write-Host ""
        Write-Host "⚠️  You have uncommitted changes:" -ForegroundColor Yellow
        Write-Host $gitStatus -ForegroundColor Yellow
        $response = Read-Host "Commit changes before deploying? (y/n)"
        if ($response -eq 'y') {
            git add .
            $commitMsg = Read-Host "Enter commit message"
            git commit -m $commitMsg
            Write-Host "✅ Changes committed" -ForegroundColor Green
        }
    }
} catch {
    Write-Host "⚠️  Not a git repository or git not available" -ForegroundColor Yellow
}

# Deployment options
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Deployment Options" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Since Render requires authentication, please choose:" -ForegroundColor White
Write-Host ""
Write-Host "1. Deploy via Render Dashboard (Recommended)" -ForegroundColor Yellow
Write-Host "   → Go to: https://dashboard.render.com" -ForegroundColor Cyan
Write-Host "   → New + → Web Service" -ForegroundColor Cyan
Write-Host "   → Connect GitHub repo: Likesh1235/scrapsail-backend" -ForegroundColor Cyan
Write-Host "   → Use render.yaml blueprint OR configure manually" -ForegroundColor Cyan
Write-Host ""
Write-Host "2. Deploy via Render CLI (if authenticated)" -ForegroundColor Yellow
if ($renderInstalled) {
    Write-Host "   → Run: render deploy" -ForegroundColor Cyan
    Write-Host "   → Or: render services:create --blueprint render.yaml" -ForegroundColor Cyan
} else {
    Write-Host "   → First install: npm install -g @render/cli" -ForegroundColor Cyan
    Write-Host "   → Then login: render login" -ForegroundColor Cyan
}
Write-Host ""

# Show configuration summary
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   Deployment Configuration" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Repository: https://github.com/Likesh1235/scrapsail-backend.git" -ForegroundColor White
Write-Host "Branch: main" -ForegroundColor White
Write-Host "Runtime: Java 17" -ForegroundColor White
Write-Host "Build: ./mvnw clean package -DskipTests" -ForegroundColor White
Write-Host "Start: java -jar target/*.jar" -ForegroundColor White
Write-Host "Region: Singapore" -ForegroundColor White
Write-Host "Plan: Free" -ForegroundColor White
Write-Host ""
Write-Host "Environment Variables:" -ForegroundColor White
Write-Host "  SPRING_PROFILES_ACTIVE=prod" -ForegroundColor Gray
Write-Host "  MYSQL_URL=jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?sslMode=REQUIRED" -ForegroundColor Gray
Write-Host "  DB_USERNAME=avnadmin" -ForegroundColor Gray
Write-Host "  DB_PASSWORD=AVNS_q3bA1ATbxyymPpRXPIY" -ForegroundColor Gray
Write-Host "  SERVER_PORT=8080" -ForegroundColor Gray
Write-Host ""

# If CLI is available and Deploy flag is set
if ($renderInstalled -and $Deploy) {
    Write-Host "Attempting to deploy via Render CLI..." -ForegroundColor Yellow
    Write-Host ""
    
    # Check if logged in
    try {
        render whoami 2>&1 | Out-Null
        Write-Host "✅ Authenticated with Render" -ForegroundColor Green
    } catch {
        Write-Host "❌ Not logged in. Please login first:" -ForegroundColor Red
        Write-Host "   render login" -ForegroundColor Yellow
        exit 1
    }
    
    # Deploy using blueprint
    Write-Host "Deploying service from render.yaml..." -ForegroundColor Yellow
    try {
        render services:create --blueprint render.yaml
        Write-Host ""
        Write-Host "✅ Deployment initiated!" -ForegroundColor Green
        Write-Host "Check status at: https://dashboard.render.com" -ForegroundColor Cyan
    } catch {
        Write-Host "❌ Deployment failed. Error:" -ForegroundColor Red
        Write-Host $_.Exception.Message -ForegroundColor Red
        Write-Host ""
        Write-Host "Try deploying manually via Render Dashboard" -ForegroundColor Yellow
    }
} else {
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host "   Next Steps" -ForegroundColor Cyan
    Write-Host "========================================" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "1. Push your code to GitHub (if not already):" -ForegroundColor White
    Write-Host "   git push origin main" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "2. Deploy via Render Dashboard:" -ForegroundColor White
    Write-Host "   → https://dashboard.render.com" -ForegroundColor Cyan
    Write-Host "   → See DEPLOY_TO_RENDER.md for detailed instructions" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "3. Or use Render CLI (after installation):" -ForegroundColor White
    Write-Host "   render login" -ForegroundColor Cyan
    Write-Host "   render services:create --blueprint render.yaml" -ForegroundColor Cyan
    Write-Host ""
}

Write-Host "========================================" -ForegroundColor Green
Write-Host "   Ready for Deployment!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green

