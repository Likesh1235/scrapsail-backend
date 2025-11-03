# 🚂 Railway Deployment Guide - ScrapSail Backend

## ✅ Current Status: READY FOR RAILWAY

Your project is **already configured** for Railway deployment!

---

## 📋 What's Already Configured

### ✅ 1. Procfile (Already Exists)
```
web: java -jar target/*.jar
```
✅ **Already in your repository!**

### ✅ 2. Maven Configuration
- ✅ `pom.xml` configured
- ✅ `mvnw` (Maven wrapper) included
- ✅ `.mvn/wrapper/` directory included

### ✅ 3. Health Endpoints
- ✅ `/health` - Basic health check
- ✅ `/ready` - Readiness check with DB connectivity
- ✅ `/` - Root endpoint with API info

---

## 🚀 RAILWAY DEPLOYMENT STEPS

### STEP 1: Verify Procfile (Already Done ✅)

**Your Procfile is already correct:**
```
web: java -jar target/*.jar
```

**Location:** `scrapsail-backend/Procfile`

✅ **No changes needed!**

---

### STEP 2: Create Railway Account & Project

1. **Go to:** https://railway.app
2. **Sign up/Login:**
   - Click "Login" or "Start a New Project"
   - Sign up with GitHub (recommended)
   - Authorize Railway

3. **Create New Project:**
   - Click **"New Project"**
   - Select **"Deploy from GitHub repo"**
   - Choose repository: `Likesh1235/scrapsail-backend`
   - Click **"Deploy Now"**

✅ **Railway will automatically detect Maven & Java!**

---

### STEP 3: Configure Build & Start Commands

Railway should auto-detect, but verify:

1. **In Railway Dashboard:**
   - Click on your service
   - Go to **"Settings"** tab
   - Scroll to **"Build & Deploy"** section

2. **Verify Build Command:**
   ```
   ./mvnw clean package -DskipTests
   ```
   ⚠️ If not set, add it manually

3. **Verify Start Command:**
   ```
   java -jar target/*.jar
   ```
   ⚠️ If not set, add it manually

4. **Root Directory:**
   - Leave **empty** (or set to `./` if needed)

5. **Click "Save"**

---

### STEP 4: Add Environment Variables

**Go to:** Settings → **"Variables"** tab

**Add these 7 environment variables:**

| Key | Value |
|-----|-------|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `MYSQL_URL` | `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?ssl-mode=REQUIRED` |
| `DB_USERNAME` | `avnadmin` |
| `DB_PASSWORD` | `AVNS_q3bA1ATbxyymPpRXPIY` |
| `PORT` | `8080` |
| `EMAIL_USERNAME` | `likeshkanna74@gmail.com` |
| `EMAIL_PASSWORD` | `rvou eevk bdwt iizl` |

**⚠️ IMPORTANT:**
- Railway uses `PORT` environment variable automatically
- Make sure all 7 variables are added
- Click "Save" after adding each variable

---

### STEP 5: Wait for Deployment

Railway will automatically:
1. ✅ Detect Java/Maven
2. ✅ Run build command: `./mvnw clean package -DskipTests`
3. ✅ Build JAR file
4. ✅ Start application: `java -jar target/*.jar`

**Watch the deployment logs:**
- Click **"Deployments"** tab
- Click on the latest deployment
- Watch **"Build Logs"** and **"Deploy Logs"**

**Expected timeline:** 5-10 minutes (first deployment)

---

### STEP 6: Verify Deployment Success

#### Check Build Logs

**Look for:**
```
[INFO] BUILD SUCCESS
[INFO] Building jar: target/scrapsail-backend-0.0.1-SNAPSHOT.jar
```

#### Check Deploy Logs

**Look for:**
```
Starting BackendApplication v0.0.1-SNAPSHOT
The following 1 profile is active: "prod"
HikariPool-1 - Start completed.
Tomcat started on port(s): 8080
Started BackendApplication in X.XXX seconds
```

#### Check Service Status

**In Railway Dashboard:**
- Service should show **"Active"** ✅
- **Public URL** will be displayed (e.g., `https://scrapsail-backend.up.railway.app`)

---

### STEP 7: Test Your Deployment

#### Get Your Railway URL

**From Railway Dashboard:**
- Click on your service
- Find **"Public URL"** or **"Domain"**
- Example: `https://scrapsail-backend.up.railway.app`

#### Test Health Endpoint

**In browser or terminal:**
```
https://your-backend.up.railway.app/health
```

**Expected Response:**
```json
{
  "status": "UP",
  "timestamp": 1234567890,
  "message": "ScrapSail Backend is healthy and running!"
}
```

#### Test Readiness Endpoint

```
https://your-backend.up.railway.app/ready
```

**Expected Response (if DB connected):**
```json
{
  "status": "ready",
  "database": "connected",
  "timestamp": 1234567890,
  "message": "Backend is ready and database is accessible"
}
```

#### Test Root Endpoint

```
https://your-backend.up.railway.app/
```

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

---

## 🐛 Troubleshooting

### Issue 1: Build Fails

**Symptoms:**
```
[ERROR] BUILD FAILURE
./mvnw: Permission denied
```

**Fix:**
1. Verify build command: `./mvnw clean package -DskipTests`
2. Check Maven wrapper exists in repository
3. Try "Clear build cache" and redeploy

### Issue 2: "Could not resolve placeholder 'MYSQL_URL'"

**Problem:** Environment variables not set

**Fix:**
1. Go to Settings → Variables
2. Add all 7 environment variables
3. Save → Redeploy

### Issue 3: "Communications link failure"

**Problem:** Database connection issue

**Fix:**
1. Verify Aiven MySQL is running
2. Check IP allowlist in Aiven:
   - Go to: https://console.aiven.io
   - Select MySQL service
   - Add `0.0.0.0/0` to IP allowlist
   - Wait 2 minutes
   - Redeploy

### Issue 4: Port Already in Use

**Problem:** Railway handles PORT automatically

**Fix:**
- Make sure `server.port=${PORT:8080}` in `application.properties`
- Railway sets PORT automatically - don't override it

### Issue 5: Service Shows "Active" but Returns 404

**Fix:**
1. Check deploy logs for startup errors
2. Verify all endpoints are accessible
3. Test `/health` first (simplest endpoint)

---

## ✅ Success Checklist

Your deployment is successful when:

- [ ] Build logs show **"BUILD SUCCESS"**
- [ ] Deploy logs show **"Started BackendApplication"**
- [ ] Service status shows **"Active"**
- [ ] Public URL is accessible
- [ ] `/health` returns `{"status":"UP"}`
- [ ] `/ready` returns `{"status":"ready","database":"connected"}`
- [ ] `/` returns API information

---

## 📋 Railway vs Render Comparison

| Feature | Railway | Render |
|---------|---------|--------|
| **Auto-detection** | ✅ Excellent | ✅ Good |
| **Procfile** | ✅ Required | ✅ Optional |
| **Build Command** | Auto-detects | Manual setup |
| **Start Command** | Auto-detects | Manual setup |
| **Free Tier** | ✅ Yes | ✅ Yes |
| **Environment Variables** | Easy UI | Easy UI |
| **Health Checks** | Automatic | Manual setup |

---

## 🎯 Quick Reference

**Railway Dashboard:** https://railway.app
**Aiven Console:** https://console.aiven.io
**Health Endpoint:** `https://your-backend.up.railway.app/health`
**Ready Endpoint:** `https://your-backend.up.railway.app/ready`
**Root Endpoint:** `https://your-backend.up.railway.app/`

---

## 💡 Pro Tips

1. **Auto-Deploy:** Railway automatically deploys on every push to `main` branch
2. **Custom Domain:** Railway allows custom domains in paid plans
3. **Logs:** Access real-time logs in Railway dashboard
4. **Metrics:** Monitor CPU, Memory, Network usage
5. **Rollback:** Railway keeps deployment history for easy rollback

---

## 🎉 Next Steps After Deployment

1. ✅ **Update Frontend:** Point frontend to Railway URL
2. ✅ **Test All Endpoints:** Verify all API endpoints work
3. ✅ **Monitor Logs:** Watch for any errors
4. ✅ **Set Up Alerts:** Configure Railway alerts for downtime

---

**Your ScrapSail backend is now ready to deploy on Railway!** 🚂

The Procfile is already configured, so you can start deploying immediately!

