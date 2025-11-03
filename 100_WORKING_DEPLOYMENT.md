# 🚀 100% WORKING Deployment Method - Step by Step

This guide guarantees your deployment will work. Follow every step exactly.

---

## 📋 BEFORE YOU START - Verify This

1. ✅ Your code is on GitHub: `Likesh1235/scrapsail-backend`
2. ✅ Branch `main` has all files (render.yaml, pom.xml, mvnw, etc.)
3. ✅ You have Aiven MySQL database credentials

---

## 🎯 STEP 1: Create/Sign In to Render Account

1. Go to: **https://render.com**
2. Click **"Get Started for Free"** (or "Sign Up")
3. Choose **"Sign up with GitHub"** ⭐ (Easiest)
4. Authorize Render to access your GitHub
5. Complete signup

**✅ You're now at:** https://dashboard.render.com

---

## 🎯 STEP 2: Delete Existing Service (If Any)

**IMPORTANT:** If you already created a service that's failing:

1. In Render dashboard, click on **`scrapsail-backend`** service (if exists)
2. Click **"Settings"** tab (left sidebar)
3. Scroll to **VERY BOTTOM** of page
4. Click **"Delete Service"** (red button)
5. Type: `scrapsail-backend` to confirm
6. Click **"Delete"**

**Why?** We want to start fresh with correct settings.

---

## 🎯 STEP 3: Create New Web Service

1. In Render dashboard, click **"New +"** button (top right corner)
2. From dropdown menu, click **"Web Service"**
   - ⚠️ **DO NOT** click "Blueprint" - we're doing manual setup

---

## 🎯 STEP 4: Connect GitHub Repository

1. You'll see **"Connect a repository"** section
2. If GitHub not connected:
   - Click **"Connect GitHub"** or **"Configure account"**
   - Authorize Render (if prompted)
3. In repository list, find and click: **`Likesh1235/scrapsail-backend`**
4. Click on it to select

---

## 🎯 STEP 5: Configure Service - EXACT VALUES

After selecting repository, you'll see a configuration form. Fill it **EXACTLY** as shown:

### 5.1 Basic Information Section

**Name:**
```
scrapsail-backend
```

**Region:**
- Click dropdown → Select **`Singapore`**

**Branch:**
- Type or select: **`main`**
- ⚠️ Make sure it's `main`, not `master` or `clean-main`

**Root Directory:**
- Leave **EMPTY** (don't type anything)

### 5.2 Runtime Configuration ⚠️ CRITICAL

**This is the MOST IMPORTANT step - where most people fail!**

Scroll through the form and look for ANY of these fields:

1. **"Runtime"** dropdown
2. **"Environment"** dropdown  
3. **"Language"** dropdown
4. **"Platform"** dropdown
5. **"Stack"** dropdown

**What to look for:**
- It might be near the top
- It might be in middle section
- It might say "Auto-detect" or show "Node.js"

**WHEN YOU FIND IT:**

1. **DO NOT** leave it as "Auto-detect"
2. **DO NOT** select "Node" or "Node.js"
3. **CLICK the dropdown**
4. **SELECT:** `Java 17` (if available)
   - OR `Java` (if Java 17 not available)
   - OR `Java Runtime` (if that's the option)

**Visual Example:**
```
Runtime: [Auto-detect ▼]  ← Click this
         ↓
         Select: "Java 17" ✅
```

**If you DON'T see any runtime dropdown:**
- That's OK - continue with next steps
- We'll set it after service creation (Step 10)

### 5.3 Build Configuration

**Build Command:**
Copy this EXACTLY (don't change anything):
```
chmod +x ./mvnw && ./mvnw clean package -DskipTests
```

**Start Command:**
Copy this EXACTLY:
```
java -jar target/*.jar
```

### 5.4 Plan Selection

**Plan:**
- Select **`Free`** from dropdown

---

## 🎯 STEP 6: Add Environment Variables

Scroll down to **"Environment Variables"** section.

### 6.1 Add Each Variable

Click **"Add Environment Variable"** button and add these **ONE BY ONE**:

**Variable 1:**
- **Key:** `SPRING_PROFILES_ACTIVE`
- **Value:** `prod`
- Press Enter or click outside

**Variable 2:**
- **Key:** `MYSQL_URL`
- **Value:** `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?sslMode=REQUIRED`
- Press Enter or click outside

**Variable 3:**
- **Key:** `DB_USERNAME`
- **Value:** `avnadmin`
- Press Enter or click outside

**Variable 4:**
- **Key:** `DB_PASSWORD`
- **Value:** `AVNS_q3bA1ATbxyymPpRXPIY`
- Press Enter or click outside

**Variable 5:**
- **Key:** `SERVER_PORT`
- **Value:** `8080`
- Press Enter or click outside

**Variable 6:**
- **Key:** `PORT`
- **Value:** `8080`
- Press Enter or click outside

### 6.2 Verify All 6 Variables

You should see listed:
- ✅ SPRING_PROFILES_ACTIVE
- ✅ MYSQL_URL
- ✅ DB_USERNAME
- ✅ DB_PASSWORD
- ✅ SERVER_PORT
- ✅ PORT

---

## 🎯 STEP 7: Enable Auto-Deploy

1. Look for **"Auto-Deploy"** checkbox or toggle
2. Make sure it's **CHECKED** ✅
   - This auto-deploys when you push code

---

## 🎯 STEP 8: Create the Service

1. Scroll to bottom of form
2. Review everything one more time:
   - ✅ Name: scrapsail-backend
   - ✅ Region: Singapore
   - ✅ Branch: main
   - ✅ Runtime: Java 17 (if you found the dropdown)
   - ✅ Build Command: chmod +x ./mvnw && ./mvnw clean package -DskipTests
   - ✅ Start Command: java -jar target/*.jar
   - ✅ All 6 environment variables added
   - ✅ Plan: Free
   - ✅ Auto-Deploy: Enabled

3. Click **"Create Web Service"** button (usually green/blue, at bottom)

---

## 🎯 STEP 9: Wait for Initial Build

1. You'll be redirected to service dashboard
2. Status will show: **"Building..."** or **"Deploying..."**

**Watch the logs (they appear automatically):**

### ✅ GOOD SIGNS:
```
==> Cloning repository...
==> Using Java 17 runtime          ← Should see this!
==> Running build command...
==> chmod +x ./mvnw && ./mvnw clean package -DskipTests
[INFO] Scanning for projects...
[INFO] Building scrapsail-backend...
[INFO] BUILD SUCCESS              ← Success!
==> Starting service...
==> java -jar target/*.jar
Started BackendApplication        ← App running!
```

### ❌ BAD SIGNS (If you see these):
```
==> Using Node.js version...      ← Wrong runtime!
JAVA_HOME not defined             ← Need to fix runtime
```

**If you see Node.js error:** Go to Step 10 immediately!

---

## 🎯 STEP 10: Fix Runtime (If Still Showing Node.js)

**ONLY do this if logs show "Using Node.js version..."**

1. In service dashboard, click **"Settings"** tab (left sidebar)
2. Scroll down looking for **"Runtime"** dropdown
   - It might be in "Environment" section
   - It might be in "Build & Deploy" section
3. **Find the dropdown** that shows "Node.js"
4. **Change it to:** `Java 17` or `Java`
5. Scroll to bottom, click **"Save Changes"**
6. Go to **"Events"** tab
7. Click **"Manual Deploy"** → **"Deploy latest commit"**
8. Watch logs - should now show "Using Java 17 runtime"

---

## 🎯 STEP 11: Verify Deployment Success

### 11.1 Check Service Status
- At top of dashboard, status should be: **"Live"** ✅ (green indicator)

### 11.2 Get Your Service URL
- You'll see your service URL like:
- `https://scrapsail-backend-xxxx.onrender.com`
- Copy this URL

### 11.3 Test Health Endpoint
1. Open new browser tab
2. Go to: `https://your-service-url.onrender.com/health`
3. Should return: `{"status":"UP"}`

**If you see `{"status":"UP"}`:** ✅ **SUCCESS! Your backend is deployed!**

### 11.4 Test API Endpoint
Try: `https://your-service-url.onrender.com/api/auth/login`
- Should return some response (even if error - means service is running)

---

## ✅ SUCCESS CHECKLIST

Your deployment is successful when:
- [ ] Service status shows **"Live"** (green)
- [ ] Logs show **"Using Java 17 runtime"**
- [ ] Logs show **"BUILD SUCCESS"**
- [ ] Logs show **"Started BackendApplication"**
- [ ] `/health` endpoint returns `{"status":"UP"}`
- [ ] Service URL is accessible

---

## 🐛 Troubleshooting

### Problem 1: Still Shows "Using Node.js"
**Solution:** 
- Go to Settings → Find Runtime dropdown → Change to Java 17
- Save → Manual Deploy

### Problem 2: Can't Find Runtime in Settings
**Solution:**
- Delete service → Recreate
- Look VERY CAREFULLY for Runtime dropdown during creation
- It might be in a different section

### Problem 3: Build Fails with "Permission denied"
**Solution:**
- Our build command already includes `chmod +x`
- If still fails, check logs for exact error

### Problem 4: Database Connection Failed
**Solution:**
- Verify all 6 environment variables are set
- Check MYSQL_URL format is correct
- Verify DB_PASSWORD value

### Problem 5: Service Live but Returns 404
**Solution:**
- Check logs - app might not have started correctly
- Test `/health` endpoint first
- Verify routes in your Spring Boot code

---

## 📝 Important Notes

1. **Runtime MUST be Java 17** - This is the #1 cause of failures
2. **Build command includes chmod** - Fixes permission issues automatically
3. **All 6 env vars required** - Database won't connect without them
4. **First build takes 5-10 min** - Be patient, it downloads dependencies
5. **Auto-deploy enabled** - Future pushes auto-deploy

---

## 🎉 You're Done!

If all checklist items are ✅, your Spring Boot backend is:
- ✅ Running on Render
- ✅ Connected to Aiven MySQL
- ✅ Accessible via public URL
- ✅ Auto-deploying on code changes

**Your service URL:** `https://scrapsail-backend-xxxx.onrender.com`

**Next step:** Update your frontend to use this new backend URL!

---

## 📞 Still Need Help?

If you're stuck at any step:
1. Tell me which step you're on
2. What you see on your screen
3. Any error messages

I'll help you get it working! 🚀

