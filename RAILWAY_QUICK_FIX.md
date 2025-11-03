# 🚨 Railway 404 Error - Quick Fix Guide

## The Issue
Getting "Not Found" when accessing `/health` endpoint means the service isn't running or deployed correctly.

---

## ✅ Quick Diagnostic Steps

### 1. Check Railway Deployment Status

**In Railway Dashboard:**
1. Click on your **backend service**
2. Check the **"Deployments"** tab
3. Look at the **latest deployment**

**What you should see:**
- ✅ **Green status** = Successfully deployed
- ⚠️ **Yellow/Deploying** = Still building
- ❌ **Red/Failed** = Build failed

---

### 2. Check Build Logs

1. Click on the **latest deployment**
2. Open **"Build Logs"**
3. Look for:

**✅ Success indicators:**
```
[INFO] BUILD SUCCESS
[INFO] Building jar: target/scrapsail-backend-0.0.1-SNAPSHOT.jar
```

**❌ Failure indicators:**
- Any `ERROR` messages
- `BUILD FAILURE`
- Maven dependency errors

---

### 3. Check Runtime Logs

1. Go to your service → **"Logs"** tab
2. Look for these messages:

**✅ Success:**
```
Started BackendApplication in X.XXX seconds (JVM running for X.XXX)
```

**❌ Common Errors:**

**Database Connection Failed:**
```
Cannot create PoolableConnectionFactory
Communications link failure
```
**Fix:** Add MySQL environment variables

**Port Error:**
```
Port 8080 is already in use
```
**Fix:** Railway handles this, but check PORT env var

**Application Failed to Start:**
```
Failed to start application
```
**Fix:** Check the full error message below

---

### 4. Verify Environment Variables

**Go to:** Your backend service → **"Variables"** tab

**Required variables:**
- [ ] `MYSQLHOST` = `hopper.proxy.rlwy.net`
- [ ] `MYSQLPORT` = `51116`
- [ ] `MYSQLDATABASE` = `railway`
- [ ] `MYSQLUSER` = `root`
- [ ] `MYSQLPASSWORD` = `MoxMmvgySDSiKQceRvMQREvioTGxmzOZ`

**⚠️ IMPORTANT:** These must be in your **BACKEND service**, not MySQL service!

---

### 5. Try These Endpoints

If `/health` doesn't work, try:

1. **Root endpoint:**
   ```
   https://your-backend-url.up.railway.app/
   ```

2. **API test:**
   ```
   https://your-backend-url.up.railway.app/api/test
   ```

If these also give 404, the service definitely isn't running.

---

## 🔧 Common Fixes

### Fix 1: Service Not Deployed
**Problem:** No deployments visible

**Solution:**
1. Make sure you've connected the GitHub repo
2. Push a commit to trigger deployment
3. Or click **"Deploy"** manually

---

### Fix 2: Build Failed
**Problem:** Build logs show errors

**Solution:**
1. Check for Java version issues (should be 17+)
2. Verify `pom.xml` is correct
3. Check Maven dependencies

---

### Fix 3: Database Connection Failed
**Problem:** Logs show database errors

**Solution:**
1. Verify all 5 MySQL environment variables are set
2. Check variables are in **backend service**, not MySQL service
3. Ensure MySQL service is running in Railway

---

### Fix 4: Missing Environment Variables
**Problem:** Application can't connect to database

**Solution:**
1. Go to backend service → Variables
2. Add all 5 MySQL variables
3. Click **"Redeploy"**

---

### Fix 5: Wrong Port
**Problem:** Service starts but not accessible

**Solution:**
- Railway automatically sets `PORT` environment variable
- Your app uses: `server.port=${PORT:8080}`
- Don't override `PORT` variable manually

---

## 📋 Action Items Checklist

Run through these in order:

- [ ] Check deployment status (green/yellow/red)
- [ ] Check build logs for errors
- [ ] Check runtime logs for startup messages
- [ ] Verify all 5 MySQL environment variables are set
- [ ] Ensure variables are in BACKEND service (not MySQL)
- [ ] Try root endpoint: `https://your-url.up.railway.app/`
- [ ] Check domain is generated in Settings → Networking
- [ ] Redeploy if needed

---

## 🆘 Still Not Working?

**Share these with me:**
1. **Screenshot of:** Deployment status (green/yellow/red)
2. **Screenshot of:** Build logs (last 20-30 lines)
3. **Screenshot of:** Runtime logs (last 20-30 lines)
4. **List of:** Environment variable NAMES (not values) set in backend service

Then I can help debug the specific issue!

---

## ✅ Expected Success Flow

1. **Build completes:** `BUILD SUCCESS`
2. **JAR created:** `target/scrapsail-backend-0.0.1-SNAPSHOT.jar`
3. **Application starts:** `Started BackendApplication`
4. **Health check works:** `/health` returns JSON
5. **Service accessible:** Domain works

---

**Try the diagnostic steps above and let me know what you find!**

