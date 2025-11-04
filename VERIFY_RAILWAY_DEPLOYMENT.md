# ✅ Railway Deployment Verification Guide

## 🔍 Quick Verification Checklist

Use this checklist to verify your Railway deployment is working correctly:

### ✅ Configuration Files (Already Verified)

- [x] `railway.json` exists and configured
- [x] `Procfile` exists with correct content
- [x] `application-prod.properties` configured for Railway
- [x] `SecurityConfig.java` allows `/health` endpoint

---

## 📋 Step-by-Step Verification

### STEP 1: Check Railway Dashboard

**Go to:** https://railway.app → Your Service

**Check these:**

1. **Service Status:**
   - [ ] Status shows **"Active"** (green indicator)
   - [ ] Not showing "Deploying" or "Failed"

2. **Latest Deployment:**
   - [ ] Go to **"Deployments"** tab
   - [ ] Latest deployment shows **"Success"** (green checkmark)
   - [ ] Not showing "Failed" (red X)

3. **Build Logs:**
   - [ ] Click on latest deployment
   - [ ] View **"Build Logs"**
   - [ ] Should see: `[INFO] BUILD SUCCESS`
   - [ ] Should see: `Building jar: target/scrapsail-backend-0.0.1-SNAPSHOT.jar`

4. **Deploy Logs:**
   - [ ] View **"Deploy Logs"** (or "Runtime Logs")
   - [ ] Should see: `Starting BackendApplication`
   - [ ] Should see: `The following 1 profile is active: "prod"`
   - [ ] Should see: `Tomcat started on port(s): 8080`
   - [ ] Should see: `Started BackendApplication in X.XXX seconds`

---

### STEP 2: Verify Environment Variables

**Go to:** Railway Dashboard → Your Service → **"Variables"** tab

**Check these 7 variables exist:**

- [ ] `SPRING_PROFILES_ACTIVE` = `prod`
- [ ] `MYSQL_URL` = `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?ssl-mode=REQUIRED`
- [ ] `DB_USERNAME` = `avnadmin`
- [ ] `DB_PASSWORD` = `AVNS_q3bA1ATbxyymPpRXPIY`
- [ ] `PORT` = `8080` (Railway sets this automatically, but verify it's there)
- [ ] `EMAIL_USERNAME` = `likeshkanna74@gmail.com`
- [ ] `EMAIL_PASSWORD` = `rvou eevk bdwt iizl`

⚠️ **If any are missing, add them and redeploy!**

---

### STEP 3: Get Your Railway URL

**In Railway Dashboard:**
- Click on your service
- Look for **"Settings"** tab
- Find **"Public Domain"** or **"Custom Domain"** section
- Copy your Railway URL (e.g., `https://scrapsail-backend.up.railway.app`)

---

### STEP 4: Test Health Endpoints

**Open your browser or use PowerShell:**

#### Test 1: Health Endpoint

**URL:** `https://your-backend.up.railway.app/health`

**Expected Response:**
```json
{
  "status": "UP",
  "timestamp": 1234567890,
  "message": "ScrapSail Backend is healthy and running!"
}
```

**PowerShell Test:**
```powershell
Invoke-WebRequest -Uri "https://your-backend.up.railway.app/health" -UseBasicParsing
```

**✅ SUCCESS:** Status code 200, response contains `"status":"UP"`

---

#### Test 2: Readiness Endpoint

**URL:** `https://your-backend.up.railway.app/ready`

**Expected Response (if DB connected):**
```json
{
  "status": "ready",
  "database": "connected",
  "timestamp": 1234567890,
  "message": "Backend is ready and database is accessible"
}
```

**Expected Response (if DB not connected):**
```json
{
  "status": "not ready",
  "database": "disconnected",
  "error": "...",
  "timestamp": 1234567890
}
```

**PowerShell Test:**
```powershell
Invoke-WebRequest -Uri "https://your-backend.up.railway.app/ready" -UseBasicParsing
```

**✅ SUCCESS:** Status code 200 (even if DB not connected, endpoint should respond)

---

#### Test 3: Root Endpoint

**URL:** `https://your-backend.up.railway.app/`

**Expected Response:**
```json
{
  "application": "ScrapSail Backend API",
  "version": "1.0.0",
  "status": "✅ Running Successfully",
  "message": "Welcome to ScrapSail Smart Waste Management System",
  "availableEndpoints": {...}
}
```

**PowerShell Test:**
```powershell
Invoke-WebRequest -Uri "https://your-backend.up.railway.app/" -UseBasicParsing
```

**✅ SUCCESS:** Status code 200, response contains API information

---

## 🎯 Deployment Status Summary

### ✅ DEPLOYMENT SUCCESSFUL If:

- [x] Railway service status: **"Active"**
- [x] Latest deployment: **"Success"**
- [x] Build logs: `BUILD SUCCESS`
- [x] Deploy logs: `Started BackendApplication`
- [x] `/health` endpoint: Returns 200 OK with `{"status":"UP"}`
- [x] `/ready` endpoint: Returns 200 OK (even if DB not connected)
- [x] `/` endpoint: Returns 200 OK with API info
- [x] All 7 environment variables set

---

### ❌ DEPLOYMENT FAILED If:

- [ ] Service status: "Failed" or "Deploying" (stuck)
- [ ] Build logs: `BUILD FAILURE`
- [ ] Deploy logs: No "Started BackendApplication" message
- [ ] Health endpoints: Return 404, 500, or timeout
- [ ] Missing environment variables

---

## 🐛 Common Issues & Fixes

### Issue 1: Health Check Failing

**Symptoms:**
- Railway shows "Unhealthy"
- `/health` returns 404 or timeout

**Fixes:**
1. Check Railway logs for startup errors
2. Verify `SecurityConfig.java` allows `/health` endpoint (already done ✅)
3. Check if app is actually starting (look for "Started BackendApplication" in logs)
4. Verify PORT environment variable is set

---

### Issue 2: Database Connection Failing

**Symptoms:**
- `/ready` returns `{"database":"disconnected"}`
- Logs show "Communications link failure"

**Fixes:**
1. **Check Aiven IP Allowlist:**
   - Go to: https://console.aiven.io
   - Select MySQL service
   - Go to "IP allowlist"
   - Add: `0.0.0.0/0` (allows all IPs)
   - Wait 2 minutes
   - Redeploy on Railway

2. **Verify Environment Variables:**
   - Check `MYSQL_URL`, `DB_USERNAME`, `DB_PASSWORD` are correct
   - No extra spaces or typos

3. **Check Aiven Service Status:**
   - Ensure MySQL service is "Running" in Aiven dashboard

**Note:** App should still start even if DB connection fails. The `/health` endpoint should work regardless.

---

### Issue 3: Build Fails

**Symptoms:**
- Build logs show `BUILD FAILURE`
- Maven errors

**Fixes:**
1. Verify build command: `./mvnw clean package -DskipTests`
2. Check Maven wrapper (`mvnw`) exists in repository
3. Try "Clear build cache" and redeploy

---

### Issue 4: Using Docker Instead of Java

**Symptoms:**
- Build logs mention "Dockerfile"
- Health check fails

**Fixes:**
1. Verify `railway.json` exists and has `"builder": "NIXPACKS"`
2. Check Railway Settings → Build command should be: `./mvnw clean package -DskipTests`
3. Check Railway Settings → Start command should be: `java -jar target/*.jar`
4. If Railway still uses Docker, delete any Dockerfile references and redeploy

---

## ✅ Final Verification

**Run this PowerShell script to test all endpoints:**

```powershell
$baseUrl = "https://your-backend.up.railway.app"  # Replace with your Railway URL

Write-Host "🧪 Testing Railway Deployment..." -ForegroundColor Cyan
Write-Host ""

# Test Health
Write-Host "1. Testing /health endpoint..." -ForegroundColor Yellow
try {
    $health = Invoke-WebRequest -Uri "$baseUrl/health" -UseBasicParsing
    Write-Host "   ✅ Health: $($health.StatusCode) - $($health.Content)" -ForegroundColor Green
} catch {
    Write-Host "   ❌ Health failed: $_" -ForegroundColor Red
}

# Test Ready
Write-Host "2. Testing /ready endpoint..." -ForegroundColor Yellow
try {
    $ready = Invoke-WebRequest -Uri "$baseUrl/ready" -UseBasicParsing
    Write-Host "   ✅ Ready: $($ready.StatusCode) - $($ready.Content)" -ForegroundColor Green
} catch {
    Write-Host "   ❌ Ready failed: $_" -ForegroundColor Red
}

# Test Root
Write-Host "3. Testing / endpoint..." -ForegroundColor Yellow
try {
    $root = Invoke-WebRequest -Uri "$baseUrl/" -UseBasicParsing
    Write-Host "   ✅ Root: $($root.StatusCode) - API is responding" -ForegroundColor Green
} catch {
    Write-Host "   ❌ Root failed: $_" -ForegroundColor Red
}

Write-Host ""
Write-Host "✅ Verification complete!" -ForegroundColor Green
```

---

## 📊 Deployment Health Status

**If all checks pass:**
- ✅ **Your Railway deployment is CORRECT and WORKING!**
- ✅ Backend is live and accessible
- ✅ Health endpoints are responding
- ✅ Ready for frontend integration

**If any checks fail:**
- ❌ Follow the troubleshooting steps above
- ❌ Check Railway logs for specific errors
- ❌ Verify environment variables are set
- ❌ Redeploy if needed

---

**Your Railway deployment configuration is correct! Use this guide to verify it's working.** 🚂✅



