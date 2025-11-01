# 🚀 ScrapSail Deployment - Execution Checklist

## ✅ Pre-Deployment Verification (COMPLETED)

- [x] Backend repository: `scrapsail-smart-waste-JAVA-PROJECT-`
- [x] Frontend repository: `Likesh1235/Scrapsail-frontend`
- [x] Procfile created and in repository
- [x] Application.properties configured for Railway
- [x] CORS pre-configured for Vercel
- [x] All code pushed to GitHub

---

## 📋 STEP 1: Deploy Backend on Railway

### ✅ 1.1 Create Railway Account

**ACTION REQUIRED:** 
1. Open: **https://railway.app**
2. Click **"Login"** or **"Start a New Project"**
3. Choose **"Login with GitHub"**
4. Authorize Railway to access your repositories

**Status:** ⏳ Waiting for you to create account

---

### ✅ 1.2 Create New Project

**ACTION REQUIRED:**
1. In Railway dashboard, click **"New Project"**
2. Select **"Deploy from GitHub repo"**
3. Find and select: **`scrapsail-smart-waste-JAVA-PROJECT-`**
4. Railway will auto-detect Java/Maven ✅

**Status:** ⏳ Waiting for project creation

---

### ✅ 1.3 Configure Environment Variables

**ACTION REQUIRED:**
1. Click on your service/project
2. Go to **Settings** → **Variables** tab
3. Click **"New Variable"** and add these **ONE BY ONE**:

   ```
   Variable: MYSQL_URL
   Value: jdbc:mysql://hopper.proxy.rlwy.net:51116/railway
   
   Variable: MYSQLUSER
   Value: root
   
   Variable: MYSQLPASSWORD
   Value: MoxMmvgySDSiKQceRvMQREvioTGxmzOZ
   
   Variable: PORT
   Value: 8080
   ```

4. Click **"Add"** for each variable
5. **Verify all 4 variables are added** ✅

**Status:** ⏳ Waiting for environment variables

---

### ✅ 1.4 Verify Build Settings

**ACTION REQUIRED:**
1. Go to **Settings** → **Deploy** tab
2. Verify:
   - **Build Command:** `mvn clean package -DskipTests` (or auto-detected)
   - **Start Command:** Should use Procfile automatically
   - **Root Directory:** `/` (root)

**If not auto-detected, add:**
- Build Command: `mvn clean package -DskipTests`

**Status:** ⏳ Waiting for verification

---

### ✅ 1.5 Deploy Backend

**ACTION REQUIRED:**
1. Railway should auto-deploy when you connected the repo
2. Or click **"Deploy"** / **"Redeploy"** button
3. **Watch the build logs** - wait 2-3 minutes
4. Look for: `Started BackendApplication in X.XXX seconds` ✅

**Status:** ⏳ Waiting for deployment

**Build should show:**
- ✅ Installing dependencies
- ✅ Building JAR file
- ✅ Starting application
- ✅ Application running on port 8080

---

### ✅ 1.6 Generate Domain and Get Backend URL

**ACTION REQUIRED:**
1. Go to **Settings** → **Networking** tab
2. Click **"Generate Domain"** (if not already generated)
3. **Copy your backend URL** - it will look like:
   ```
   https://scrapsail-backend-production-xxxx.up.railway.app
   ```
4. **SAVE THIS URL** - you'll need it for frontend! 📋

**Status:** ⏳ Waiting for domain generation

**Your Backend URL:** `_________________________________` (Fill this in!)

---

### ✅ 1.7 Verify Backend is Running

**ACTION REQUIRED:**
1. Open your backend URL in browser: `https://your-url.up.railway.app/health`
2. Should see: `{"status":"UP","message":"ScrapSail Backend is healthy and running!"}`
3. ✅ **Backend is deployed!**

**Status:** ⏳ Waiting for verification

---

## 📋 STEP 2: Deploy Frontend on Vercel

### ✅ 2.1 Create Vercel Account

**ACTION REQUIRED:**
1. Open: **https://vercel.com**
2. Click **"Sign Up"**
3. Choose **"Continue with GitHub"**
4. Authorize Vercel to access your repositories

**Status:** ⏳ Waiting for account creation

---

### ✅ 2.2 Import Frontend Project

**ACTION REQUIRED:**
1. After logging in, click **"Add New Project"**
2. Click **"Import Git Repository"**
3. Find and select: **`Likesh1235/Scrapsail-frontend`**
4. Vercel will auto-detect React ✅

**Status:** ⏳ Waiting for project import

---

### ✅ 2.3 Configure Build Settings

**ACTION REQUIRED:**
1. Verify these settings (should be auto-detected):
   - **Framework Preset:** React
   - **Root Directory:** `/` (or leave empty)
   - **Build Command:** `npm run build`
   - **Output Directory:** `build`
   - **Install Command:** `npm install`

**If not correct, update them manually**

**Status:** ⏳ Waiting for verification

---

### ✅ 2.4 Add Environment Variable (CRITICAL!)

**ACTION REQUIRED:**
1. Scroll down to **"Environment Variables"** section
2. Click **"Add"** or **"+"** button
3. Add this variable:

   ```
   Name: REACT_APP_API_BASE_URL
   Value: https://YOUR-BACKEND-URL.up.railway.app
          (Use the URL from Step 1.6 - NO trailing slash!)
   
   Environments: ✅ Production ✅ Preview ✅ Development
   ```

4. Click **"Add"**
5. **VERIFY the variable appears in the list** ✅

**Example:**
```
REACT_APP_API_BASE_URL=https://scrapsail-backend-production-xxxx.up.railway.app
```

**Status:** ⏳ Waiting for environment variable

**Your Backend URL for Frontend:** `_________________________________` (Use from Step 1.6)

---

### ✅ 2.5 Deploy Frontend

**ACTION REQUIRED:**
1. Click **"Deploy"** button
2. **Watch the build process** - wait 1-2 minutes
3. You'll see:
   - ✅ Installing dependencies
   - ✅ Building application
   - ✅ Deployment complete

**Status:** ⏳ Waiting for deployment

---

### ✅ 2.6 Get Frontend URL

**ACTION REQUIRED:**
1. After deployment completes, you'll see your frontend URL
2. It will be: `https://scrapsail-frontend.vercel.app` (or similar)
3. **Copy this URL** 📋

**Status:** ⏳ Waiting for deployment

**Your Frontend URL:** `_________________________________`

---

## 📋 STEP 3: Update CORS if Needed

### ✅ 3.1 Check if CORS Update Needed

**CHECK:**
- Open your frontend URL
- Open browser DevTools (F12) → Console
- Try logging in
- **Do you see CORS errors?**

**If YES CORS errors:**
- We need to add your exact Vercel URL to backend CORS
- Continue to Step 3.2

**If NO CORS errors:**
- ✅ Skip to Step 4 - Everything is working!

---

### ✅ 3.2 Update Backend CORS (If Needed)

**ACTION REQUIRED:**
1. Open: `src/main/java/com/scrapsail/backend/config/CorsConfig.java`
2. Add your exact Vercel URL to the `ALLOWED_ORIGINS` list
3. Example: Add `"https://scrapsail-frontend.vercel.app"` if different
4. Commit and push:
   ```bash
   git add src/main/java/com/scrapsail/backend/config/CorsConfig.java
   git commit -m "Add exact Vercel URL to CORS"
   git push origin main
   ```
5. Railway will auto-redeploy ✅

**Status:** ⏳ Waiting (only if CORS errors occur)

---

## 📋 STEP 4: Final Verification

### ✅ 4.1 Test Backend

**ACTION REQUIRED:**
1. Open: `https://your-backend.up.railway.app/health`
2. Should return: `{"status":"UP",...}`
3. ✅ Backend is healthy!

**Status:** ⏳ Waiting for test

---

### ✅ 4.2 Test Frontend

**ACTION REQUIRED:**
1. Open: `https://scrapsail-frontend.vercel.app`
2. Should load your React application
3. ✅ Frontend is working!

**Status:** ⏳ Waiting for test

---

### ✅ 4.3 Test Full Integration

**ACTION REQUIRED:**
1. Open frontend in browser
2. Press **F12** → **Network** tab
3. Try logging in with:
   - **Admin:** `admin@scrapsail.com` / `admin123`
   - **Collector:** `collector@scrapsail.com` / `collector123`
4. **Check:**
   - ✅ API calls appear in Network tab
   - ✅ Calls go to your Railway backend URL
   - ✅ No CORS errors in Console
   - ✅ Login works successfully

**Status:** ⏳ Waiting for integration test

---

## 🎉 DEPLOYMENT COMPLETE!

Once all steps are verified:

✅ Backend running on Railway  
✅ Frontend running on Vercel  
✅ API calls working  
✅ No CORS errors  
✅ Full-stack application live!

---

## 📞 Need Help?

**Common Issues:**

1. **Backend build fails:**
   - Check Railway logs
   - Verify environment variables are set
   - Ensure Procfile is in root directory

2. **Frontend can't connect to backend:**
   - Verify `REACT_APP_API_BASE_URL` is set correctly
   - Check backend URL has no trailing slash
   - Ensure backend is running

3. **CORS errors:**
   - Verify Vercel URL is in backend CORS config
   - Check backend CORS changes are deployed
   - Wait for Railway to redeploy

---

## 📝 Save Your URLs

**Backend URL:** `_________________________________`  
**Frontend URL:** `_________________________________`

**Save these for future reference!**

