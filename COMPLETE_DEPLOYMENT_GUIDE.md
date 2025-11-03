# 🚀 Complete Deployment Guide - From Start to Finish

## Overview
This guide will help you deploy your Spring Boot backend (`scrapsail-backend`) to Render.com, step by step.

**What we're deploying:**
- Spring Boot application (Java 17)
- Connected to Aiven MySQL database
- Auto-deploys from GitHub

---

## 📋 Prerequisites Checklist

Before starting, ensure you have:
- ✅ GitHub account (with repository: `Likesh1235/scrapsail-backend`)
- ✅ Render account (free tier works: https://render.com)
- ✅ Aiven MySQL database (already set up and running)
- ✅ Code pushed to GitHub (we'll verify this)

---

## 🔍 STEP 1: Verify Your Repository is Ready

### 1.1 Check GitHub Repository
1. Go to: https://github.com/Likesh1235/scrapsail-backend
2. Verify you see these files:
   - `render.yaml`
   - `pom.xml`
   - `mvnw` and `mvnw.cmd`
   - `src/` directory with your code
3. Check the branch is `main`

### 1.2 Verify render.yaml Content
Click on `render.yaml` in GitHub and verify it contains:
```yaml
services:
  - type: web
    name: scrapsail-backend
    plan: free
    region: singapore
    buildCommand: chmod +x ./mvnw && ./mvnw clean package -DskipTests
    startCommand: java -jar target/*.jar
    envVars:
      - key: SPRING_PROFILES_ACTIVE
        value: prod
      - key: MYSQL_URL
        value: jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?sslMode=REQUIRED
      - key: DB_USERNAME
        value: avnadmin
      - key: DB_PASSWORD
        value: AVNS_q3bA1ATbxyymPpRXPIY
      - key: SERVER_PORT
        value: 8080
      - key: PORT
        value: 8080
    branch: main
```

---

## 🌐 STEP 2: Create Render Account (If Needed)

1. Go to: https://render.com
2. Click **"Get Started for Free"** or **"Sign Up"**
3. Choose **"Sign up with GitHub"** (recommended - easier integration)
4. Authorize Render to access your GitHub account
5. Complete the signup process

---

## 🏗️ STEP 3: Create Web Service on Render

### 3.1 Navigate to Dashboard
1. After signing in, you'll be at: https://dashboard.render.com
2. You'll see an empty dashboard (if first time)

### 3.2 Create New Web Service
1. Click the **"New +"** button in the top right corner
2. From the dropdown menu, select **"Web Service"**
   - ⚠️ **DO NOT** select "Blueprint" (we'll use manual setup for reliability)

### 3.3 Connect Your GitHub Repository
1. You'll see **"Connect a repository"** section
2. If not connected:
   - Click **"Connect GitHub"** or **"Connect account"**
   - Authorize Render to access your repositories
   - Grant permissions when prompted
3. In the repository list, find and click: **`Likesh1235/scrapsail-backend`**

---

## ⚙️ STEP 4: Configure Service Settings

After selecting your repository, you'll see a configuration form. Fill it out exactly as shown:

### 4.1 Basic Information
- **Name:** 
  ```
  scrapsail-backend
  ```
  (This will be part of your service URL)

- **Region:** 
  Select **`Singapore`** from the dropdown
  (Choose based on your database location)

- **Branch:** 
  Type or select: **`main`**
  ⚠️ Make sure it's `main`, not `clean-main` or any other branch

- **Root Directory:** 
  Leave this **EMPTY**
  (Your code is in the root of the repository)

### 4.2 Runtime Configuration
**⚠️ CRITICAL STEP - This is where many deployments fail!**

Look for any of these fields:
- "Runtime"
- "Environment"
- "Language"
- "Platform"
- "Stack"

**If you see a dropdown:**
1. **DO NOT** select "Auto-detect" (it might detect Node.js incorrectly)
2. **DO NOT** select "Node" or "Node.js"
3. **MANUALLY SELECT:** `Java 17` or `Java` or `Java Runtime`
4. If you see "Java 17" option, select that (it's the most specific)

**If you DON'T see a runtime dropdown:**
- Don't worry - Render might auto-detect from `pom.xml`
- We'll fix it after creation if needed

### 4.3 Build Configuration
- **Build Command:**
  ```
  chmod +x ./mvnw && ./mvnw clean package -DskipTests
  ```
  - This command:
    1. Makes `mvnw` executable (fixes permission issues)
    2. Runs Maven to build your Spring Boot JAR file
    3. Skips tests for faster builds

- **Start Command:**
  ```
  java -jar target/*.jar
  ```
  - This runs your built Spring Boot application

### 4.4 Service Plan
- Select: **`Free`**
  - 750 hours/month free
  - Service sleeps after 15 minutes of inactivity (wakes on request)
  - Perfect for development/testing

---

## 🔐 STEP 5: Set Environment Variables

Environment variables store configuration (like database credentials) that shouldn't be in code.

### 5.1 Navigate to Environment Variables Section
1. Scroll down in the configuration form
2. Find **"Environment Variables"** section
3. You'll see an **"Add Environment Variable"** button

### 5.2 Add Each Variable

Click **"Add Environment Variable"** and add these **ONE BY ONE**:

**Variable 1:**
- **Key:** `SPRING_PROFILES_ACTIVE`
- **Value:** `prod`
- Click **"Add"** or press Enter

**Variable 2:**
- **Key:** `MYSQL_URL`
- **Value:** `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?sslMode=REQUIRED`
- Click **"Add"**

**Variable 3:**
- **Key:** `DB_USERNAME`
- **Value:** `avnadmin`
- Click **"Add"**

**Variable 4:**
- **Key:** `DB_PASSWORD`
- **Value:** `AVNS_q3bA1ATbxyymPpRXPIY`
- Click **"Add"**

**Variable 5:**
- **Key:** `SERVER_PORT`
- **Value:** `8080`
- Click **"Add"**

**Variable 6:**
- **Key:** `PORT`
- **Value:** `8080`
- Click **"Add"**

### 5.3 Verify All Variables
After adding, you should see all 6 variables listed:
- ✅ SPRING_PROFILES_ACTIVE
- ✅ MYSQL_URL
- ✅ DB_USERNAME
- ✅ DB_PASSWORD
- ✅ SERVER_PORT
- ✅ PORT

---

## 🚀 STEP 6: Create the Service

### 6.1 Final Check
Before creating, verify:
- [ ] Name is set to `scrapsail-backend`
- [ ] Region is `Singapore`
- [ ] Branch is `main`
- [ ] Build command includes `chmod +x ./mvnw`
- [ ] Start command is `java -jar target/*.jar`
- [ ] All 6 environment variables are added
- [ ] Plan is `Free`
- [ ] Runtime is `Java 17` (if dropdown was available)

### 6.2 Enable Auto-Deploy (Recommended)
- Look for **"Auto-Deploy"** checkbox
- Make sure it's **CHECKED** ✅
- This automatically deploys when you push code to GitHub

### 6.3 Create Service
1. Scroll to the bottom of the form
2. Click the **"Create Web Service"** button (usually green/blue)
3. Render will start the deployment process

---

## ⏳ STEP 7: Monitor Deployment

### 7.1 Deployment Process
After clicking "Create Web Service", you'll be redirected to your service dashboard.

**You'll see:**
1. **"Building..."** - Render is cloning your code and running the build command
2. **"Deploying..."** - Starting your application
3. **"Live"** - Your service is running!

### 7.2 Watch the Logs
1. The deployment page shows **real-time logs**
2. Watch for these success indicators:

**✅ Good Signs:**
```
==> Cloning repository...
==> Using Java 17 runtime          ← Should see this!
==> Running build command...
==> chmod +x ./mvnw && ./mvnw clean package -DskipTests
[INFO] Scanning for projects...
[INFO] BUILD SUCCESS             ← Build successful!
==> Starting service...
==> java -jar target/*.jar
Started BackendApplication in X.XXX seconds  ← App started!
```

**❌ Error Signs:**
```
==> Using Node.js version...     ← Wrong runtime!
JAVA_HOME not defined            ← Runtime issue
./mvnw: Permission denied        ← Permission issue
Build failed                     ← Build error
```

### 7.3 First Deployment Time
- **First deployment:** 5-10 minutes (downloads dependencies, builds JAR)
- **Future deployments:** 2-5 minutes (only rebuilds changed code)

---

## 🔧 STEP 8: Fix Runtime Issues (If Needed)

### 8.1 Check Current Runtime
If logs show "Using Node.js version..." instead of Java:

1. In your service dashboard, click **"Settings"** tab (left sidebar)
2. Scroll to **"Environment"** or **"Build & Deploy"** section
3. Look for **"Runtime"** dropdown

### 8.2 Change Runtime
1. If you find a **"Runtime"** dropdown:
   - Change it from "Node" to **"Java 17"**
2. Click **"Save Changes"** at the bottom
3. Go to **"Events"** tab
4. Click **"Manual Deploy"** → **"Deploy latest commit"**

---

## ✅ STEP 9: Verify Deployment Success

### 9.1 Check Service Status
1. In your service dashboard, look at the top
2. Status should show: **"Live"** ✅ (green indicator)

### 9.2 Get Your Service URL
1. In the dashboard, you'll see your service URL:
   - Format: `https://scrapsail-backend-xxxx.onrender.com`
   - Copy this URL - you'll need it!

### 9.3 Test Health Endpoint
1. Open a new browser tab
2. Go to: `https://your-service-url.onrender.com/health`
3. Should return: `{"status":"UP"}`

**If you see this:** ✅ **Deployment successful!**

**If you get error:** Check the logs in Render dashboard for issues

### 9.4 Test API Endpoint
Try: `https://your-service-url.onrender.com/api/auth/login`
- Should return some response (even if error - means service is running)

---

## 🔄 STEP 10: Understanding Auto-Deploy

### 10.1 How It Works
Since you enabled Auto-Deploy:
- Every time you **push code to `main` branch**
- Render **automatically detects** the change
- Triggers a **new build and deployment**
- Your service URL **stays the same**

### 10.2 Deploy New Changes
To deploy new code:
```powershell
cd scrapsail-backend
git add .
git commit -m "Your commit message"
git push origin main
```
Render will automatically start deploying!

---

## 📊 STEP 11: Monitor Your Service

### 11.1 View Logs
- Go to your service dashboard
- Click **"Logs"** tab
- See real-time application logs
- Useful for debugging

### 11.2 View Metrics
- Click **"Metrics"** tab
- See CPU, Memory, Network usage
- Monitor performance

### 11.3 Service Events
- Click **"Events"** tab
- See deployment history
- Track all deployments

---

## 🐛 Troubleshooting Common Issues

### Issue 1: "Using Node.js version..."
**Problem:** Render detected Node.js instead of Java

**Solution:**
1. Go to Settings → Change Runtime to Java 17
2. Save → Manual Deploy

### Issue 2: "JAVA_HOME not defined"
**Problem:** Java runtime not configured

**Solution:**
1. Settings → Runtime → Select Java 17
2. Save → Redeploy

### Issue 3: "./mvnw: Permission denied"
**Problem:** Maven wrapper not executable

**Solution:**
- Our build command already includes `chmod +x ./mvnw`
- If still fails, check logs for exact error

### Issue 4: "Build failed"
**Problem:** Maven build error

**Solution:**
1. Check logs for specific error
2. Common causes:
   - Missing dependencies
   - Compilation errors
   - Network issues
3. Fix code → Push → Auto-redeploys

### Issue 5: "Database connection failed"
**Problem:** Can't connect to Aiven MySQL

**Solution:**
1. Verify environment variables are set correctly
2. Check MYSQL_URL format
3. Verify DB_PASSWORD is correct
4. Check Aiven MySQL is running

### Issue 6: Service shows "Live" but returns 404
**Problem:** Service running but routes not working

**Solution:**
1. Check if application started correctly (view logs)
2. Test `/health` endpoint first
3. Verify routes in your Spring Boot code

---

## 📝 Summary Checklist

Before considering deployment complete, verify:

- [ ] Service status is **"Live"** in Render dashboard
- [ ] Logs show **"Using Java 17 runtime"**
- [ ] Logs show **"BUILD SUCCESS"**
- [ ] Logs show **"Started BackendApplication"**
- [ ] Health endpoint returns `{"status":"UP"}`
- [ ] All 6 environment variables are set
- [ ] Service URL is accessible
- [ ] Auto-deploy is enabled

---

## 🎉 Success!

If all checklist items are ✅, your deployment is successful!

**Your backend is now:**
- ✅ Running on Render
- ✅ Connected to Aiven MySQL
- ✅ Accessible via public URL
- ✅ Auto-deploying on code changes

---

## 📞 Next Steps

1. **Update Frontend:** Point your React frontend to the new Render URL
2. **Test All Endpoints:** Verify all API endpoints work
3. **Monitor Logs:** Watch for any runtime errors
4. **Set Custom Domain:** (Optional) Add your own domain

---

## 🔗 Useful Links

- **Render Dashboard:** https://dashboard.render.com
- **Service Logs:** Available in service dashboard
- **Render Docs:** https://render.com/docs
- **Your Service URL:** Check your service dashboard

---

**Congratulations! Your Spring Boot backend is now deployed! 🚀**
