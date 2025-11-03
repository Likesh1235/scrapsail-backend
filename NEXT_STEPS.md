# Next Steps to Complete Deployment

## Step 1: Resolve GitHub Secret Protection

GitHub blocked the push because it detected a database password in your commit history. 

**👉 Action Required:** 
1. Click this link to allow the secret (one-time only):
   **https://github.com/Likesh1235/scrapsail-backend/security/secret-scanning/unblock-secret/34yYNUn28OA0o7c1qbVbhuv4cVm**

2. After clicking and allowing, come back here and run:
   ```powershell
   cd scrapsail-backend
   git push origin main
   ```

## Step 2: Deploy to Render

Once the push succeeds, deploy to Render:

### Option A: Via Render Dashboard (Recommended - 5 minutes)

1. **Go to Render Dashboard:**
   - Open: https://dashboard.render.com
   - Sign in (or create free account)

2. **Create New Web Service:**
   - Click **"New +"** button (top right)
   - Select **"Web Service"**

3. **Connect Your Repository:**
   - Click **"Connect GitHub"** or **"Connect account"**
   - Authorize Render to access your GitHub
   - Select repository: **`Likesh1235/scrapsail-backend`**
   - Select branch: **`main`**

4. **Configure Service:**
   - **Name:** `scrapsail-backend` (or any name you prefer)
   - **Region:** `Singapore`
   - **Branch:** `main`
   - **Root Directory:** (leave empty)
   - **Runtime:** `Java 17` ⚠️ **IMPORTANT: Select Java 17**
   - **Build Command:** `./mvnw clean package -DskipTests`
   - **Start Command:** `java -jar target/*.jar`
   - **Plan:** `Free`

5. **Set Environment Variables:**
   - Scroll down to **"Environment Variables"** section
   - Click **"Add Environment Variable"** and add these **ONE BY ONE**:
   
   ```
   Key: SPRING_PROFILES_ACTIVE
   Value: prod
   ```
   
   ```
   Key: MYSQL_URL
   Value: jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?sslMode=REQUIRED
   ```
   
   ```
   Key: DB_USERNAME
   Value: avnadmin
   ```
   
   ```
   Key: DB_PASSWORD
   Value: AVNS_q3bA1ATbxyymPpRXPIY
   ```
   
   ```
   Key: SERVER_PORT
   Value: 8080
   ```

6. **Deploy:**
   - Check **"Auto-Deploy"** is enabled (should be by default)
   - Click **"Create Web Service"** button at the bottom

7. **Wait for Deployment:**
   - First deployment takes 5-10 minutes
   - Watch the build logs in real-time
   - You'll see: "Building..." → "Deploying..." → "Live"

8. **Get Your Service URL:**
   - Once "Live", you'll see your service URL like:
   - `https://scrapsail-backend-xxxx.onrender.com`

### Option B: Use Blueprint (render.yaml)

If Render auto-detects your `render.yaml` file:
1. Go to https://dashboard.render.com
2. Click **"New +"** → **"Blueprint"**
3. Connect your repository
4. Render will read `render.yaml` automatically
5. **⚠️ IMPORTANT:** You'll still need to manually set `DB_PASSWORD` in environment variables (since it's `sync: false`)

## Step 3: Verify Deployment

After deployment completes:

1. **Check Health Endpoint:**
   ```
   https://your-service-name.onrender.com/health
   ```
   Should return: `{"status":"UP"}`

2. **Test API:**
   ```
   https://your-service-name.onrender.com/api/auth/login
   ```

3. **Check Logs:**
   - In Render dashboard, click on your service
   - Go to "Logs" tab
   - Verify no errors

## Troubleshooting

### Build Fails
- ✅ Ensure Java 17 is selected (not Java 11 or 19)
- ✅ Check Build Command is: `./mvnw clean package -DskipTests`
- ✅ Verify `mvnw` files are in repository

### Service Won't Start
- ✅ Check logs for errors
- ✅ Verify all environment variables are set
- ✅ Ensure database is accessible

### Database Connection Issues
- ✅ Verify `DB_PASSWORD` is set correctly
- ✅ Check `MYSQL_URL` format is correct
- ✅ Ensure Aiven MySQL is running

## Success Checklist

- [ ] GitHub push completed successfully
- [ ] Render service created
- [ ] Environment variables set
- [ ] Service status is "Live"
- [ ] Health endpoint returns `{"status":"UP"}`
- [ ] Database connected (check logs)

## What Happens Next

Once deployed:
- ✅ Auto-deploy enabled: Every push to `main` automatically deploys
- ✅ Service URL stays the same
- ✅ Logs available in Render dashboard
- ✅ Update frontend to use new backend URL

---

**Ready? Start with Step 1 above!** 🚀


