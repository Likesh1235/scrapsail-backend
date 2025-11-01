# 🚀 Complete ScrapSail Deployment Guide

This guide will help you deploy both **backend (Railway)** and **frontend (Vercel)** completely.

---

## 📋 Prerequisites Checklist

- [x] ✅ Backend code pushed to GitHub
- [x] ✅ Frontend code pushed to GitHub  
- [x] ✅ Procfile created for Railway
- [x] ✅ Application.properties configured for environment variables
- [ ] 🔄 Railway account created
- [ ] 🔄 Vercel account created

---

## 🎯 Part 1: Backend Deployment on Railway

### Step 1.1: Create Railway Account

1. Go to **https://railway.app**
2. Click **"Login"** → Sign up with **GitHub**
3. Authorize Railway to access your repositories

### Step 1.2: Create New Project

1. Click **"New Project"**
2. Select **"Deploy from GitHub repo"**
3. Select your backend repository: `scrapsail-smart-waste-JAVA-PROJECT-`
4. Railway will auto-detect it's a Java/Maven project ✅

### Step 1.3: Configure Environment Variables

1. Go to your service → **Settings** → **Variables**
2. Add these environment variables:

   | Variable Name | Value |
   |--------------|-------|
   | `MYSQL_URL` | `jdbc:mysql://hopper.proxy.rlwy.net:51116/railway` |
   | `MYSQLUSER` | `root` |
   | `MYSQLPASSWORD` | `MoxMmvgySDSiKQceRvMQREvioTGxmzOZ` |
   | `PORT` | `8080` |

3. Click **"Add"** for each variable

### Step 1.4: Configure Build Settings (If Needed)

Railway should auto-detect Maven, but verify:

1. Go to **Settings** → **Deploy**
2. **Build Command:** `mvn clean package -DskipTests` (if not auto-detected)
3. **Start Command:** (Should use Procfile automatically)
4. **Root Directory:** `/` (root)

### Step 1.5: Deploy

1. Railway will automatically deploy when you push to GitHub
2. Or click **"Deploy"** / **"Redeploy"** manually
3. Wait 2-3 minutes for build and startup

### Step 1.6: Get Your Backend URL

1. Go to **Settings** → **Networking**
2. Click **"Generate Domain"** (if not already generated)
3. Your backend URL will be: `https://<service-name>.up.railway.app`
4. **Copy this URL** - you'll need it for the frontend! 📋

### Step 1.7: Verify Backend is Running

1. Open: `https://your-backend-url.up.railway.app/health`
2. Should return: `{"status":"UP","message":"ScrapSail Backend is healthy and running!"}`
3. ✅ Backend is deployed!

---

## 🎯 Part 2: Update Backend CORS for Vercel

### Step 2.1: Update CORS Configuration

Your backend needs to allow requests from your Vercel frontend domain.

1. Edit: `src/main/java/com/scrapsail/backend/config/CorsConfig.java`
2. Add your Vercel domain to allowed origins

### Step 2.2: Commit and Push CORS Update

```bash
git add src/main/java/com/scrapsail/backend/config/CorsConfig.java
git commit -m "Update CORS for Vercel frontend"
git push origin main
```

Railway will auto-redeploy when it detects the push.

---

## 🎯 Part 3: Frontend Deployment on Vercel

### Step 3.1: Create Vercel Account

1. Go to **https://vercel.com**
2. Click **"Sign Up"** → Sign up with **GitHub**
3. Authorize Vercel to access your repositories

### Step 3.2: Import Frontend Project

1. Click **"Add New Project"**
2. Click **"Import Git Repository"**
3. Select: `Likesh1235/Scrapsail-frontend`
4. Vercel auto-detects React ✅

### Step 3.3: Configure Build Settings

Verify these settings (should be auto-detected):

- **Framework Preset:** React
- **Root Directory:** `/` (or leave empty)
- **Build Command:** `npm run build`
- **Output Directory:** `build`
- **Install Command:** `npm install`

### Step 3.4: Add Environment Variable (CRITICAL!)

1. Scroll to **"Environment Variables"** section
2. Click **"Add"** / **"+"**
3. Add:

   **Name:** `REACT_APP_API_BASE_URL`  
   **Value:** `https://your-backend-url.up.railway.app` (use your Railway URL from Step 1.6)
   
   **Example:**
   ```
   REACT_APP_API_BASE_URL=https://scrapsail-backend-production.up.railway.app
   ```

4. Select environments: ✅ Production, ✅ Preview, ✅ Development
5. Click **"Add"**

### Step 3.5: Deploy

1. Click **"Deploy"**
2. Wait 1-2 minutes for build and deployment
3. ✅ Frontend is deployed!

### Step 3.6: Get Your Frontend URL

1. After deployment, you'll see: `https://scrapsail-frontend.vercel.app` (or similar)
2. **Copy this URL** 📋

---

## 🎯 Part 4: Update Backend CORS with Vercel URL

### Step 4.1: Update CORS Configuration

Now that you have your Vercel URL, update backend CORS:

1. Edit: `src/main/java/com/scrapsail/backend/config/CorsConfig.java`
2. Add your Vercel frontend URL

### Step 4.2: Commit and Push

```bash
git add src/main/java/com/scrapsail/backend/config/CorsConfig.java
git commit -m "Add Vercel frontend to CORS allowed origins"
git push origin main
```

---

## ✅ Final Verification

### Test Backend:
- Health: `https://your-backend.up.railway.app/health`
- Should return: `{"status":"UP",...}`

### Test Frontend:
- Open: `https://scrapsail-frontend.vercel.app`
- Should load your React app
- Try logging in with:
  - Admin: `admin@scrapsail.com` / `admin123`
  - Collector: `collector@scrapsail.com` / `collector123`

### Test Connection:
- Open browser DevTools (F12) → Network tab
- Try logging in from frontend
- Check if API calls go to your Railway backend
- Verify no CORS errors in console

---

## 🔧 Troubleshooting

### Backend Issues

**Build fails on Railway:**
- Check build logs in Railway dashboard
- Ensure `pom.xml` is in root directory
- Verify Java version (should be 17)

**Backend won't start:**
- Check environment variables are set correctly
- Verify MySQL connection string
- Check Railway logs for error messages

### Frontend Issues

**API calls fail:**
- Verify `REACT_APP_API_BASE_URL` is set in Vercel
- Check if backend URL is correct (no trailing slash)
- Ensure backend is running

**CORS errors:**
- Verify backend CORS includes your Vercel domain
- Check backend CORS config is pushed and deployed
- Restart backend service after CORS update

### Connection Issues

**Can't connect to backend:**
- Verify Railway backend URL is accessible
- Check backend health endpoint works
- Ensure environment variable in Vercel is correct

---

## 📝 Quick Reference

### Backend (Railway):
- URL: `https://<service-name>.up.railway.app`
- Health: `https://<service-name>.up.railway.app/health`
- Repository: `scrapsail-smart-waste-JAVA-PROJECT-`

### Frontend (Vercel):
- URL: `https://scrapsail-frontend.vercel.app` (or your custom domain)
- Repository: `Likesh1235/Scrapsail-frontend`

### Environment Variables:

**Railway (Backend):**
- `MYSQL_URL`
- `MYSQLUSER`
- `MYSQLPASSWORD`
- `PORT`

**Vercel (Frontend):**
- `REACT_APP_API_BASE_URL`

---

## 🎉 Deployment Complete!

Once both are deployed and connected:

✅ Backend running on Railway  
✅ Frontend running on Vercel  
✅ API calls working  
✅ No CORS errors  
✅ Full-stack application live!

---

**Need help? Check the troubleshooting section or verify each step above!**

