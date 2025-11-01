# 🚂 Railway Deployment Guide for ScrapSail Backend

## ✅ Pre-Deployment Checklist

- [x] ✅ Procfile configured: `web: java -jar target/*.jar`
- [x] ✅ Build command: `mvn clean package`
- [x] ✅ Port configured: 8080 (via PORT environment variable)
- [x] ✅ MySQL connection configured for Railway environment variables
- [x] ✅ Repository: `Likesh1235/scrapsail`

---

## 🚀 Step 1: Create Railway Account & Project

1. Go to **https://railway.app**
2. Click **"Login"** → Sign up with **GitHub**
3. Authorize Railway to access your repositories

---

## 🚀 Step 2: Deploy Your Backend

1. Click **"New Project"**
2. Select **"Deploy from GitHub repo"**
3. Choose repository: **`scrapsail`** (or `Likesh1235/scrapsail`)
4. Railway will auto-detect it's a Java/Maven project ✅

---

## 🚀 Step 3: Configure Build Settings

Railway should auto-detect Maven, but verify:

1. Go to your service → **Settings** → **Deploy**
2. **Build Command:** `mvn clean package`
3. **Start Command:** (Leave empty - Procfile will be used automatically)
4. **Root Directory:** `/` (root)

---

## 🚀 Step 4: Add MySQL Database

1. In your Railway project, click **"+ New"**
2. Select **"Database"** → **"Add MySQL"**
3. Railway will create a MySQL database automatically
4. Note the database connection details

---

## 🚀 Step 5: Configure Environment Variables

Go to your backend service → **Settings** → **Variables**

### Required Environment Variables:

Add these **ONE BY ONE**:

#### Variable 1: MYSQL_URL
- **Name:** `MYSQL_URL`
- **Value:** Railway provides this automatically when you add MySQL database
- **Or manually:** Copy from MySQL service → **Variables** → `MYSQL_URL`
- Format: `mysql://user:password@host:port/railway`

#### Variable 2: MYSQLUSER
- **Name:** `MYSQLUSER`
- **Value:** Railway MySQL username (usually `root`)
- Copy from MySQL service → **Variables** → `MYSQLUSER`

#### Variable 3: MYSQLPASSWORD
- **Name:** `MYSQLPASSWORD`
- **Value:** Railway MySQL password
- Copy from MySQL service → **Variables** → `MYSQLPASSWORD`

#### Variable 4: PORT
- **Name:** `PORT`
- **Value:** `8080`
- Railway sets this automatically, but you can set it explicitly

---

## 🚀 Step 6: Get MySQL Connection Details

If you need to set environment variables manually:

1. Click on your **MySQL service** in Railway
2. Go to **"Variables"** tab
3. Copy these values:
   - `MYSQL_URL` → Use for `MYSQL_URL` in backend service
   - `MYSQLUSER` → Use for `MYSQLUSER` in backend service
   - `MYSQLPASSWORD` → Use for `MYSQLPASSWORD` in backend service
   - `MYSQLPORT` → Port number
   - `MYSQLHOST` → Host address
   - `MYSQLDATABASE` → Database name (usually `railway`)

---

## 🚀 Step 7: Deploy

1. Railway will automatically start deploying when you:
   - Push to GitHub, OR
   - Click **"Deploy"** / **"Redeploy"**
2. Watch the build logs (takes 2-3 minutes)
3. Look for: `Started BackendApplication in X.XXX seconds`

---

## 🚀 Step 8: Generate Domain & Get URL

1. Go to **Settings** → **Networking**
2. Click **"Generate Domain"**
3. Your backend URL will be: `https://xxxxx.up.railway.app`
4. **Copy this URL** - You'll need it for frontend! 📋

---

## ✅ Step 9: Verify Deployment

1. Test health endpoint:
   ```
   https://your-backend-url.up.railway.app/health
   ```
2. Should return:
   ```json
   {"status":"UP","message":"ScrapSail Backend is healthy and running!"}
   ```

---

## 🔧 Troubleshooting

### Build Fails
- Check Railway build logs
- Ensure `pom.xml` is in root directory
- Verify Java version (should be 17)

### Database Connection Fails
- Verify all MySQL environment variables are set
- Check `MYSQL_URL` format is correct
- Ensure MySQL service is running in Railway

### Port Issues
- Railway automatically sets `PORT` environment variable
- Your app reads: `server.port=${PORT:8080}`
- Should work automatically

### Application Won't Start
- Check Railway logs for error messages
- Verify environment variables are set correctly
- Ensure Procfile exists: `web: java -jar target/*.jar`

---

## 📝 Quick Reference

### Repository:
- **GitHub:** `Likesh1235/scrapsail`
- **Branch:** `main`

### Build Settings:
- **Build Command:** `mvn clean package`
- **Start Command:** (Procfile: `web: java -jar target/*.jar`)
- **Port:** `8080` (via PORT env var)

### Environment Variables Needed:
- `MYSQL_URL` (from Railway MySQL service)
- `MYSQLUSER` (from Railway MySQL service)
- `MYSQLPASSWORD` (from Railway MySQL service)
- `PORT` (set automatically by Railway)

---

## 🎉 Deployment Complete!

Once deployed, your backend will be live at:
```
https://your-service-name.up.railway.app
```

Use this URL as your `REACT_APP_API_BASE_URL` in Vercel frontend deployment!

