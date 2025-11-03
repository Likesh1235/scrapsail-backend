# ✅ Deployment Execution Checklist

Follow this checklist step-by-step. Check off each item as you complete it.

---

## 🎯 PHASE 1: Backend Deployment on Railway

### Step 1.1: Create Railway Account
- [ ] Go to https://railway.app
- [ ] Click "Login" → Sign up with GitHub
- [ ] Authorize Railway to access repositories
- [ ] **Status:** ✅ Account created

### Step 1.2: Create New Project
- [ ] Click "New Project"
- [ ] Select "Deploy from GitHub repo"
- [ ] Select repository: `scrapsail-smart-waste-JAVA-PROJECT-`
- [ ] Railway auto-detects Java/Maven
- [ ] **Status:** ✅ Project created

### Step 1.3: Add Environment Variables
Go to: Service → Settings → Variables

Add these variables (one by one):
- [ ] `MYSQL_URL` = `jdbc:mysql://hopper.proxy.rlwy.net:51116/railway`
- [ ] `MYSQLUSER` = `root`
- [ ] `MYSQLPASSWORD` = `MoxMmvgySDSiKQceRvMQREvioTGxmzOZ`
- [ ] `PORT` = `8080`
- [ ] **Status:** ✅ All variables added

### Step 1.4: Verify Build Settings
Go to: Settings → Deploy

- [ ] Build Command: `mvn clean package -DskipTests` (or auto-detected)
- [ ] Start Command: Uses Procfile automatically
- [ ] Root Directory: `/`
- [ ] **Status:** ✅ Settings verified

### Step 1.5: Deploy
- [ ] Railway starts building automatically
- [ ] Wait 2-3 minutes for build to complete
- [ ] Check deployment logs for success
- [ ] **Status:** ✅ Backend deployed

### Step 1.6: Get Backend URL
Go to: Settings → Networking

- [ ] Click "Generate Domain" (if needed)
- [ ] Copy your backend URL: `https://________.up.railway.app`
- [ ] **BACKEND URL:** `_________________________________` 📋

### Step 1.7: Verify Backend
- [ ] Open: `https://your-backend-url.up.railway.app/health`
- [ ] Should return: `{"status":"UP","message":"ScrapSail Backend is healthy and running!"}`
- [ ] **Status:** ✅ Backend verified and running

---

## 🎯 PHASE 2: Frontend Deployment on Vercel

### Step 2.1: Create Vercel Account
- [ ] Go to https://vercel.com
- [ ] Click "Sign Up" → Sign up with GitHub
- [ ] Authorize Vercel to access repositories
- [ ] **Status:** ✅ Account created

### Step 2.2: Import Frontend Project
- [ ] Click "Add New Project"
- [ ] Click "Import Git Repository"
- [ ] Select: `Likesh1235/Scrapsail-frontend`
- [ ] Vercel auto-detects React
- [ ] **Status:** ✅ Project imported

### Step 2.3: Verify Build Settings
Verify these are auto-detected:
- [ ] Framework Preset: React
- [ ] Root Directory: `/` (or empty)
- [ ] Build Command: `npm run build`
- [ ] Output Directory: `build`
- [ ] Install Command: `npm install`
- [ ] **Status:** ✅ Settings verified

### Step 2.4: Add Environment Variable (CRITICAL!)
Scroll to "Environment Variables" section

- [ ] Click "Add" button
- [ ] Name: `REACT_APP_API_BASE_URL`
- [ ] Value: `https://________.up.railway.app` (use URL from Step 1.6)
- [ ] Select: ✅ Production, ✅ Preview, ✅ Development
- [ ] Click "Add"
- [ ] **Status:** ✅ Environment variable added

### Step 2.5: Deploy Frontend
- [ ] Click "Deploy" button
- [ ] Wait 1-2 minutes for build
- [ ] Check build logs for success
- [ ] **Status:** ✅ Frontend deployed

### Step 2.6: Get Frontend URL
- [ ] Copy your frontend URL: `https://________.vercel.app`
- [ ] **FRONTEND URL:** `_________________________________` 📋

---

## 🎯 PHASE 3: Connect Backend and Frontend

### Step 3.1: Update Backend CORS
If your exact Vercel URL is different from the ones already in CORS config:

- [ ] Open: `src/main/java/com/scrapsail/backend/config/CorsConfig.java`
- [ ] Add your exact Vercel URL to `ALLOWED_ORIGINS` list
- [ ] Save file
- [ ] **Status:** ✅ CORS updated

### Step 3.2: Commit and Push CORS Update
```bash
git add src/main/java/com/scrapsail/backend/config/CorsConfig.java
git commit -m "Add Vercel frontend URL to CORS"
git push origin main
```

- [ ] Committed changes
- [ ] Pushed to GitHub
- [ ] Railway auto-redeploys backend
- [ ] **Status:** ✅ CORS update deployed

---

## 🎯 PHASE 4: Final Verification

### Step 4.1: Test Backend
- [ ] Open: `https://your-backend.up.railway.app/health`
- [ ] Returns: `{"status":"UP",...}`
- [ ] **Status:** ✅ Backend working

### Step 4.2: Test Frontend
- [ ] Open: `https://your-frontend.vercel.app`
- [ ] Page loads successfully
- [ ] **Status:** ✅ Frontend working

### Step 4.3: Test API Connection
- [ ] Open frontend in browser
- [ ] Press F12 → Open Network tab
- [ ] Try logging in:
  - Email: `admin@scrapsail.com`
  - Password: `admin123`
- [ ] Check Network tab:
  - [ ] API calls go to Railway backend URL
  - [ ] Requests return 200 OK
  - [ ] No CORS errors in console
- [ ] **Status:** ✅ Connection working

### Step 4.4: Test Full Flow
- [ ] Can login as Admin
- [ ] Can login as Collector
- [ ] Can register new user
- [ ] API calls work correctly
- [ ] No errors in browser console
- [ ] **Status:** ✅ Full stack working

---

## 🎉 DEPLOYMENT COMPLETE!

### Your Live URLs:
- **Backend:** `https://________.up.railway.app`
- **Frontend:** `https://________.vercel.app`

### Default Login Credentials:
- **Admin:** `admin@scrapsail.com` / `admin123`
- **Collector:** `collector@scrapsail.com` / `collector123`

---

## 📞 Issues?

Check troubleshooting in `COMPLETE_DEPLOYMENT_GUIDE.md`

---

**Fill in your URLs above as you complete each step!**

