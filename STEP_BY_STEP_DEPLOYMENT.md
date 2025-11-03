# 🚀 STEP-BY-STEP DEPLOYMENT GUIDE - FROM ZERO TO LIVE

## 📋 Table of Contents

1. [Prerequisites Check](#step-1-prerequisites-check)
2. [GitHub Setup](#step-2-github-setup)
3. [Render Account Setup](#step-3-render-account-setup)
4. [Create Render Service](#step-4-create-render-service)
5. [Add Environment Variables](#step-5-add-environment-variables)
6. [Deploy](#step-6-deploy)
7. [Verify Deployment](#step-7-verify-deployment)
8. [Test Your API](#step-8-test-your-api)

---

## ✅ STEP 1: Prerequisites Check

### What You Need Before Starting:

- [ ] GitHub account (you have: `Likesh1235`)
- [ ] Code is on GitHub (you have: `Likesh1235/scrapsail-backend`)
- [ ] Aiven MySQL database is created and running
- [ ] You have Aiven MySQL connection details

### Verify Your Code is on GitHub:

1. Open browser: https://github.com/Likesh1235/scrapsail-backend
2. Check you see these files:
   - ✅ `render.yaml`
   - ✅ `pom.xml`
   - ✅ `mvnw` and `mvnw.cmd`
   - ✅ `src/` folder
3. ✅ **If you see these files, you're good to go!**

---

## ✅ STEP 2: GitHub Setup (Already Done ✅)

Your code is already on GitHub at: `https://github.com/Likesh1235/scrapsail-backend`

**Status:** ✅ Ready

---

## ✅ STEP 3: Render Account Setup

### 3.1: Create/Login to Render Account

1. **Open browser:** https://render.com
2. **Click:** "Get Started for Free" (or "Sign In" if you have an account)
3. **Choose:** "Sign up with GitHub" (recommended)
4. **Authorize:** Click "Authorize Render" when prompted
5. **Complete signup:** Follow on-screen instructions

**✅ You're now logged into Render Dashboard**

---

## ✅ STEP 4: Create Render Service

### 4.1: Start Creating a Service

1. In Render Dashboard, look for **"New +"** button (top right corner)
2. **Click:** "New +"
3. From dropdown menu, **click:** "Web Service"

**✅ You're now in the service creation form**

### 4.2: Connect GitHub Repository

1. In the form, you'll see **"Connect a repository"** section
2. If GitHub is not connected:
   - Click **"Connect GitHub"** or **"Configure account"**
   - Authorize Render to access your repositories
3. **In the repository list**, find and click: **`Likesh1235/scrapsail-backend`**
4. **Click on it** to select it

**✅ Repository is now connected**

### 4.3: Configure Service Settings

After selecting the repository, you'll see a configuration form. Fill it **EXACTLY** as shown:

#### Basic Information:

**Name:**
```
scrapsail-backend
```

**Region:**
- Click the dropdown
- Select: **`Singapore`**

**Branch:**
- Type or select: **`main`**
- ⚠️ Make sure it's `main`, not `master`

**Root Directory:**
- Leave **EMPTY** (don't type anything)

#### Runtime Settings:

**Runtime/Environment:**
- Look for dropdown that might say "Auto-detect" or "Node"
- **If you see it:** Select **"Java"** or **"Java 17"** (if available)
- **If you DON'T see it:** That's OK - continue (Render will auto-detect)

#### Build & Start Commands:

**Build Command:**
```
./mvnw clean package -DskipTests
```

**Start Command:**
```
java -jar target/*.jar
```

#### Plan:

**Plan:**
- Select: **`Free`** from dropdown

**✅ Basic configuration is done**

---

## ✅ STEP 5: Add Environment Variables

### 5.1: Find Environment Variables Section

Scroll down in the form to find **"Environment Variables"** section.

### 5.2: Add Each Variable (One by One)

**IMPORTANT:** Add these **ONE BY ONE**, clicking "Add" after each:

#### Variable 1:
1. Click **"Add Environment Variable"** button
2. **Key:** Type: `SPRING_PROFILES_ACTIVE`
3. **Value:** Type: `prod`
4. Press Enter or click outside

#### Variable 2:
1. Click **"Add Environment Variable"** button
2. **Key:** Type: `MYSQL_URL`
3. **Value:** Type: `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?ssl-mode=REQUIRED`
4. Press Enter or click outside

#### Variable 3:
1. Click **"Add Environment Variable"** button
2. **Key:** Type: `DB_USERNAME`
3. **Value:** Type: `avnadmin`
4. Press Enter or click outside

#### Variable 4:
1. Click **"Add Environment Variable"** button
2. **Key:** Type: `DB_PASSWORD`
3. **Value:** Type: `AVNS_q3bA1ATbxyymPpRXPIY`
4. Press Enter or click outside

#### Variable 5:
1. Click **"Add Environment Variable"** button
2. **Key:** Type: `PORT`
3. **Value:** Type: `8080`
4. Press Enter or click outside

#### Variable 6:
1. Click **"Add Environment Variable"** button
2. **Key:** Type: `EMAIL_USERNAME`
3. **Value:** Type: `likeshkanna74@gmail.com`
4. Press Enter or click outside

#### Variable 7:
1. Click **"Add Environment Variable"** button
2. **Key:** Type: `EMAIL_PASSWORD`
3. **Value:** Type: `rvou eevk bdwt iizl`
4. Press Enter or click outside

### 5.3: Verify All 7 Variables

You should now see listed:
- ✅ SPRING_PROFILES_ACTIVE
- ✅ MYSQL_URL
- ✅ DB_USERNAME
- ✅ DB_PASSWORD
- ✅ PORT
- ✅ EMAIL_USERNAME
- ✅ EMAIL_PASSWORD

**⚠️ IMPORTANT:** Check that you have **ONLY ONE** entry for each variable (no duplicates)

---

## ✅ STEP 6: Deploy

### 6.1: Review Settings

Before clicking deploy, verify:
- [ ] Name: `scrapsail-backend`
- [ ] Region: `Singapore`
- [ ] Branch: `main`
- [ ] Build Command: `./mvnw clean package -DskipTests`
- [ ] Start Command: `java -jar target/*.jar`
- [ ] Plan: `Free`
- [ ] All 7 environment variables added
- [ ] No duplicate variables

### 6.2: Create the Service

1. Scroll to bottom of the form
2. Look for **"Create Web Service"** button (usually green/blue)
3. **Click:** "Create Web Service"

**✅ Deployment has started!**

### 6.3: Wait for Build

You'll be redirected to the service dashboard. Watch the logs:

**Status will show:** "Building..." or "Deploying..."

**Expected timeline:**
- First build: **5-10 minutes** (downloads dependencies)
- Subsequent builds: **2-3 minutes**

---

## ✅ STEP 7: Verify Deployment

### 7.1: Watch Build Logs

In the Render dashboard, you'll see logs streaming. Look for:

**✅ GOOD SIGNS:**
```
[INFO] Scanning for projects...
[INFO] Building scrapsail-backend...
[INFO] BUILD SUCCESS
[INFO] Building jar: target/scrapsail-backend-0.0.1-SNAPSHOT.jar
Starting BackendApplication...
✅ MYSQL_URL is set
✅ DB_USERNAME is set
✅ DB_PASSWORD is set
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
Tomcat started on port(s): 8080
╔════════════════════════════════════════════════════════════╗
║  🚀 ScrapSail Backend Started Successfully                ║
╚════════════════════════════════════════════════════════════╝
Started BackendApplication in X.XXX seconds
```

**❌ BAD SIGNS (If you see these, check the fix below):**
```
[ERROR] BUILD FAILURE
Communications link failure
Could not resolve placeholder 'MYSQL_URL'
```

### 7.2: Check Service Status

At the **top of the dashboard**, you'll see:
- **Status:** Should show **"Live"** (green indicator) ✅
- **Service URL:** Will show something like `https://scrapsail-backend-xxxx.onrender.com`

**✅ If status is "Live", your backend is deployed!**

---

## ✅ STEP 8: Test Your API

### 8.1: Get Your Service URL

Copy the URL from the top of Render dashboard:
- Example: `https://scrapsail-backend-xxxx.onrender.com`

### 8.2: Test Health Endpoint

**Open browser or use command line:**

**In Browser:**
```
https://your-backend-url.onrender.com/health
```

**Or in Command Line (PowerShell):**
```powershell
curl https://your-backend-url.onrender.com/health
```

**Expected Response:**
```json
{
  "status": "UP",
  "timestamp": 1234567890,
  "message": "ScrapSail Backend is healthy and running!"
}
```

### 8.3: Test Readiness Endpoint

**In Browser:**
```
https://your-backend-url.onrender.com/ready
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

**Expected Response (if DB not connected):**
```json
{
  "status": "not ready",
  "database": "disconnected",
  "error": "...",
  "timestamp": 1234567890
}
```

### 8.4: Test Root Endpoint

**In Browser:**
```
https://your-backend-url.onrender.com/
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

## 🐛 Troubleshooting Common Issues

### Issue 1: Build Fails

**Symptoms:**
```
[ERROR] BUILD FAILURE
./mvnw: Permission denied
```

**Solution:**
1. The build command should be: `./mvnw clean package -DskipTests`
2. Make sure Maven wrapper (`mvnw`) is in your repository
3. Render should handle permissions automatically

### Issue 2: Environment Variables Not Working

**Symptoms:**
```
Could not resolve placeholder 'MYSQL_URL'
IllegalStateException: Application cannot start
```

**Solution:**
1. Go to Settings → Environment Variables
2. Verify all 7 variables exist
3. Check variable names are EXACT (case-sensitive)
4. Check values have no extra spaces
5. Click "Save Changes"
6. Redeploy

### Issue 3: Database Connection Fails

**Symptoms:**
```
Communications link failure
Connect timed out
HikariPool-1 - Starting... (then fails)
```

**Solution:**
1. **Check Aiven IP Allowlist** (most common cause):
   - Go to: https://console.aiven.io
   - Select your MySQL service
   - Find "IP allowlist" section
   - Add: `0.0.0.0/0`
   - Wait 2 minutes
   - Redeploy on Render

2. **Verify Connection String:**
   - Check MYSQL_URL has `?ssl-mode=REQUIRED`
   - Verify host, port, database name are correct

3. **Check Credentials:**
   - Verify DB_USERNAME and DB_PASSWORD match Aiven exactly

### Issue 4: Duplicate Variables Error

**Symptoms:**
```
Duplicate keys are not allowed
```

**Solution:**
1. Go to Settings → Environment Variables
2. Find the duplicate variable (shows red error)
3. Delete ONE of them (keep the one with correct value)
4. Click "Save Changes"
5. Redeploy

### Issue 5: Service Shows "Live" but Returns 404

**Symptoms:**
- Status: Live ✅
- But endpoints return 404

**Solution:**
1. Check logs - app might not have started correctly
2. Verify port is binding correctly
3. Check security config allows endpoints

---

## ✅ Success Checklist

Your deployment is successful when ALL of these are true:

- [ ] Service status shows **"Live"** (green) ✅
- [ ] Build logs show **"BUILD SUCCESS"** ✅
- [ ] Application logs show **"Started BackendApplication"** ✅
- [ ] `/health` endpoint returns `{"status":"UP"}` ✅
- [ ] `/ready` endpoint returns `{"status":"ready","database":"connected"}` ✅
- [ ] `/` endpoint returns API information ✅
- [ ] No errors in logs (warnings are OK) ✅

---

## 📞 Still Need Help?

If you're stuck at any step:

1. **Share a screenshot** of where you are
2. **Share the error message** (if any)
3. **Tell me which step** you're on (1-8)

**I'll help you get it working!** 🚀

---

## 🎉 Congratulations!

Once all steps are complete and you see:
- ✅ Service Status: "Live"
- ✅ Health endpoint: Returns 200 OK
- ✅ Readiness endpoint: Returns 200 OK

**Your backend is deployed and running!** 🎊

---

**Next Steps:**
- Update your frontend to use the new backend URL
- Test your API endpoints
- Monitor logs for any issues

**Your ScrapSail backend is now live on Render!** 🚀

