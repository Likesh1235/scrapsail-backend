# 🚀 Step-by-Step Deployment Instructions

Follow these steps in order to deploy your full-stack application.

---

## ✅ STEP 1: Deploy Backend on Railway

### 1.1 Go to Railway
- Visit: **https://railway.app**
- Click **"Login"** → Sign up with **GitHub**
- Authorize Railway access

### 1.2 Create Project
1. Click **"New Project"**
2. Select **"Deploy from GitHub repo"**
3. Choose: `scrapsail-smart-waste-JAVA-PROJECT-`
4. Railway auto-detects Java/Maven ✅

### 1.3 Add Environment Variables
Go to your service → **Settings** → **Variables** → Add:

```
MYSQL_URL = jdbc:mysql://hopper.proxy.rlwy.net:51116/railway
MYSQLUSER = root
MYSQLPASSWORD = MoxMmvgySDSiKQceRvMQREvioTGxmzOZ
PORT = 8080
```

### 1.4 Deploy & Get URL
1. Railway auto-deploys when you push, or click **"Deploy"**
2. Wait 2-3 minutes
3. Go to **Settings** → **Networking** → **Generate Domain**
4. **Copy your backend URL**: `https://xxxxx.up.railway.app` 📋

### 1.5 Verify Backend
Open: `https://your-backend-url.up.railway.app/health`
Should return: `{"status":"UP",...}`

---

## ✅ STEP 2: Deploy Frontend on Vercel

### 2.1 Go to Vercel
- Visit: **https://vercel.com**
- Click **"Sign Up"** → Sign up with **GitHub**
- Authorize Vercel access

### 2.2 Import Project
1. Click **"Add New Project"**
2. Click **"Import Git Repository"**
3. Select: `Likesh1235/Scrapsail-frontend`
4. Vercel auto-detects React ✅

### 2.3 Configure Build
Verify these (should be auto-detected):
- Framework: React
- Build Command: `npm run build`
- Output Directory: `build`

### 2.4 Add Environment Variable
**CRITICAL STEP!**

1. Scroll to **"Environment Variables"**
2. Click **"Add"**
3. Add:
   - **Name:** `REACT_APP_API_BASE_URL`
   - **Value:** `https://your-backend-url.up.railway.app` (use URL from Step 1.4)
   - **Environments:** ✅ Production, ✅ Preview, ✅ Development
4. Click **"Add"**

### 2.5 Deploy
1. Click **"Deploy"**
2. Wait 1-2 minutes
3. **Copy your frontend URL**: `https://scrapsail-frontend.vercel.app` 📋

---

## ✅ STEP 3: Update Backend CORS with Vercel URL

### 3.1 Get Your Exact Vercel URL
After Step 2.5, note your exact Vercel URL (e.g., `https://scrapsail-frontend.vercel.app`)

### 3.2 Update CORS Config
1. Open: `src/main/java/com/scrapsail/backend/config/CorsConfig.java`
2. Add your exact Vercel URL to the `ALLOWED_ORIGINS` list
3. Example: Add `"https://scrapsail-frontend.vercel.app"` if not already there

### 3.3 Commit and Push
```bash
git add src/main/java/com/scrapsail/backend/config/CorsConfig.java
git commit -m "Add Vercel frontend URL to CORS"
git push origin main
```

Railway will auto-redeploy with updated CORS ✅

---

## ✅ STEP 4: Final Verification

### Test Backend:
- ✅ Health: `https://your-backend.up.railway.app/health`
- ✅ Should return: `{"status":"UP",...}`

### Test Frontend:
- ✅ Open: `https://scrapsail-frontend.vercel.app`
- ✅ Should load React app
- ✅ Try logging in:
  - Admin: `admin@scrapsail.com` / `admin123`
  - Collector: `collector@scrapsail.com` / `collector123`

### Test API Connection:
1. Open frontend in browser
2. Press F12 → Network tab
3. Try logging in
4. Check API calls go to Railway backend
5. Verify no CORS errors in console ✅

---

## 🎉 Success!

Your full-stack app is now live:
- ✅ Backend: Railway
- ✅ Frontend: Vercel
- ✅ Connected and working!

---

## 📝 Quick Reference

**Backend Repository:** `scrapsail-smart-waste-JAVA-PROJECT-`  
**Frontend Repository:** `Likesh1235/Scrapsail-frontend`  
**Backend URL:** `https://xxxxx.up.railway.app`  
**Frontend URL:** `https://scrapsail-frontend.vercel.app`

---

**Need help? Check COMPLETE_DEPLOYMENT_GUIDE.md for detailed troubleshooting!**

