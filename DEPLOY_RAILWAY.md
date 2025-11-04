# 🚀 Railway Deployment Checklist

## ✅ Pre-Deployment Verification

### 1. Code is Ready
- [x] All configuration files updated
- [x] RailwayDatabaseConfig handles DATABASE_URL conversion
- [x] LazyDataSourceConfig prevents startup connection failures
- [x] EnvironmentValidator is non-blocking
- [x] No linter errors

### 2. Railway Configuration
- [x] `railway.json` configured correctly
- [x] Build command: `./mvnw clean package -DskipTests`
- [x] Start command: `java -jar target/*.jar`

---

## 📋 Step-by-Step Deployment

### Step 1: Add MySQL Service (If Not Added)

1. **Go to Railway Dashboard:** https://railway.app
2. **Select your project** (scrapsail)
3. **Click "+ New"** → **"Database"** → **"Add MySQL"**
4. Railway automatically:
   - Creates MySQL database
   - Sets `DATABASE_URL` environment variable
   - Connects it to your service

### Step 2: Set Optional Environment Variables

**Go to:** Railway Dashboard → Your Service → **Variables** tab

**Optional (recommended):**
- `SPRING_PROFILES_ACTIVE=prod`

**You DON'T need to set:**
- ❌ `DATABASE_URL` - Railway sets this automatically
- ❌ `MYSQL_URL` - Not needed (Railway provides DATABASE_URL)
- ❌ `DB_USERNAME` - Included in DATABASE_URL
- ❌ `DB_PASSWORD` - Included in DATABASE_URL
- ❌ `PORT` - Railway sets this automatically

### Step 3: Deploy

**Option A: Automatic Deploy (Recommended)**
- Push your code to Git (GitHub/GitLab/Bitbucket)
- Railway automatically detects changes and deploys

**Option B: Manual Deploy**
1. Go to Railway Dashboard → Deployments
2. Click "Redeploy" or "Deploy"

---

## ✅ Expected Success Indicators

### Railway Logs Should Show:
```
✅ Starting BackendApplication
✅ Validating environment variables...
✅ DATABASE_URL is set (Railway MySQL service detected)
✅ HikariPool-1 - Start completed
✅ Started BackendApplication in X.XXX seconds
✅ Tomcat started on port(s): 8080 (http)
```

### Test Endpoints:
- **Health Check:** `https://<your-app>.up.railway.app/health`
- **API:** `https://<your-app>.up.railway.app/api/...`

---

## 🐛 Troubleshooting

### Issue 1: "Connection is not available"

**Check:**
1. MySQL service is added in Railway (Services tab)
2. MySQL service is running (not paused)
3. `DATABASE_URL` is visible in Variables tab

**Fix:**
- Add MySQL service: Railway → + New → Database → Add MySQL
- Railway automatically sets `DATABASE_URL`

### Issue 2: "No active profile set"

**Fix:**
- Set `SPRING_PROFILES_ACTIVE=prod` in Railway Variables (optional)

### Issue 3: Build Fails

**Check:**
- Railway logs for Maven build errors
- Ensure `railway.json` has correct build command
- Verify `pom.xml` is valid

### Issue 4: App Crashes on Startup

**Check Railway Logs For:**
- Database connection errors
- Missing environment variables
- Java version issues

**Common Fixes:**
- Add MySQL service if missing
- Check Railway logs for specific error messages

---

## 🎯 Key Points

1. **Railway automatically provides `DATABASE_URL`** when you add MySQL service
2. **No manual database configuration needed** - RailwayDatabaseConfig handles conversion
3. **App starts even if database connection fails** - won't crash on startup
4. **Health checks work** - app responds even without database

---

## 📊 Deployment Status

After deployment, check:

- [ ] App starts successfully (check Railway logs)
- [ ] Health endpoint returns 200: `/health`
- [ ] Database connection works (test API endpoints)
- [ ] No errors in Railway logs

---

**Your app is now ready to deploy! 🎉**

Just add the MySQL service in Railway and push your code!

