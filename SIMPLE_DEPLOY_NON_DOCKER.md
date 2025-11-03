# 🚀 SIMPLE DEPLOYMENT - Non-Docker (JAR Deploy)

## ✅ Current Status: READY FOR NON-DOCKER DEPLOYMENT

Your project is **already configured** for Render's native Java runtime (no Docker needed)!

---

## 📋 What's Already Configured

### ✅ 1. render.yaml (Already Set)
```yaml
env: java                    # ← Using native Java runtime
buildCommand: ./mvnw clean package -DskipTests
startCommand: java -jar target/*.jar
```

### ✅ 2. Procfile (Already Exists)
```
web: java -jar target/*.jar
```

### ✅ 3. application.properties (Already Configured)
- Uses `${MYSQL_URL}`, `${DB_USERNAME}`, `${DB_PASSWORD}` from environment
- Port binding: `server.port=${PORT:8080}`

### ✅ 4. pom.xml (Maven Project)
- Java 17 configured
- Spring Boot 3.3.4
- All dependencies included

---

## 🚀 DEPLOYMENT STEPS (Quick Guide)

### STEP 1: Verify Local Build Works

**Test locally first:**

```powershell
cd scrapsail-backend
./mvnw clean package -DskipTests
```

**Expected output:**
```
[INFO] BUILD SUCCESS
[INFO] Building jar: target/scrapsail-backend-0.0.1-SNAPSHOT.jar
```

✅ **If you see BUILD SUCCESS, you're ready!**

---

### STEP 2: Commit and Push

```powershell
git add .
git commit -m "Render deploy setup - Non-Docker"
git push origin main
```

---

### STEP 3: Configure on Render Dashboard

#### Option A: Create New Service (If Not Created Yet)

1. Go to: https://dashboard.render.com
2. Click **"New +"** → **"Web Service"**
3. Connect repository: `Likesh1235/scrapsail-backend`
4. Fill configuration:

   **Name:**
   ```
   scrapsail-backend
   ```

   **Region:**
   ```
   Singapore
   ```

   **Branch:**
   ```
   main
   ```

   **Root Directory:**
   ```
   (leave empty)
   ```

   **Environment:**
   ```
   Java
   ```
   ⚠️ **IMPORTANT:** Select "Java" from dropdown (NOT Docker, NOT Node)

   **Build Command:**
   ```
   ./mvnw clean package -DskipTests
   ```

   **Start Command:**
   ```
   java -jar target/*.jar
   ```

   **Plan:**
   ```
   Free
   ```

#### Option B: Update Existing Service

If service already exists:

1. Go to your service dashboard
2. Click **"Settings"** tab
3. Scroll to **"Build & Deploy"** section
4. **Update these fields:**

   **Environment:**
   - Change to: **"Java"** (if currently Docker or Node)

   **Build Command:**
   ```
   ./mvnw clean package -DskipTests
   ```

   **Start Command:**
   ```
   java -jar target/*.jar
   ```

5. Click **"Save Changes"**

---

### STEP 4: Add Environment Variables (CRITICAL)

**Go to:** Settings → Environment Variables

**Add these 7 variables:**

| Key | Value |
|-----|-------|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `MYSQL_URL` | `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?ssl-mode=REQUIRED` |
| `DB_USERNAME` | `avnadmin` |
| `DB_PASSWORD` | `AVNS_q3bA1ATbxyymPpRXPIY` |
| `PORT` | `8080` |
| `EMAIL_USERNAME` | `likeshkanna74@gmail.com` |
| `EMAIL_PASSWORD` | `rvou eevk bdwt iizl` |

**⚠️ IMPORTANT:**
- Click **"Save Changes"** after adding all variables
- No duplicate variables (delete any duplicates)

---

### STEP 5: Deploy

1. Click **"Events"** tab
2. Click **"Manual Deploy"** button
3. Select **"Clear build cache & Deploy"**
4. Wait 5-10 minutes

---

## ✅ Success Indicators

### Build Logs Should Show:
```
==> Using Java runtime
==> Running build command...
[INFO] Scanning for projects...
[INFO] Building scrapsail-backend...
[INFO] BUILD SUCCESS
[INFO] Building jar: target/scrapsail-backend-0.0.1-SNAPSHOT.jar
```

### Startup Logs Should Show:
```
Starting BackendApplication v0.0.1-SNAPSHOT
The following 1 profile is active: "prod"
✅ MYSQL_URL is set
HikariPool-1 - Starting...
HikariPool-1 - Start completed.
Tomcat started on port(s): 8080
Started BackendApplication in X.XXX seconds
```

### Service Status:
- Status: **"Live"** (green) ✅

### Test Endpoints:
- `https://your-backend.onrender.com/health` → Returns `{"status":"UP"}`
- `https://your-backend.onrender.com/ready` → Returns `{"status":"ready"}`

---

## 🐛 Troubleshooting

### Issue 1: "Using Node.js version..." in logs

**Problem:** Render detected wrong runtime

**Fix:**
1. Settings → Change **Environment** to **"Java"**
2. Save → Redeploy

### Issue 2: "./mvnw: Permission denied"

**Fix:** Already handled by render.yaml, but if still fails:
- Verify `mvnw` file exists in repository
- Check `.mvn/wrapper/` directory exists
- Both should already be committed ✅

### Issue 3: "BUILD FAILURE"

**Check logs for specific error:**
- Missing dependencies? (unlikely - pom.xml is complete)
- Compilation errors? (check code locally)
- Network issues? (try "Clear build cache" and redeploy)

### Issue 4: "Could not resolve placeholder 'MYSQL_URL'"

**Problem:** Environment variables not set

**Fix:**
- Follow **STEP 4** above to add all 7 variables in Render Dashboard
- Make sure to click **"Save Changes"**

### Issue 5: "Communications link failure"

**Problem:** Database connection issue

**Fix:**
1. Verify environment variables are correct
2. Check Aiven MySQL IP allowlist:
   - Go to: https://console.aiven.io
   - Select MySQL service
   - Add `0.0.0.0/0` to IP allowlist
   - Wait 2 minutes
   - Redeploy

---

## 📋 Checklist

Before deploying, verify:

- [ ] `render.yaml` has `env: java`
- [ ] Build command: `./mvnw clean package -DskipTests`
- [ ] Start command: `java -jar target/*.jar`
- [ ] `Procfile` exists with: `web: java -jar target/*.jar`
- [ ] Local build succeeds: `./mvnw clean package -DskipTests`
- [ ] All 7 environment variables added in Render Dashboard
- [ ] Environment set to "Java" (not Docker/Node)
- [ ] Code pushed to GitHub main branch

---

## 🎉 Benefits of Non-Docker Deployment

✅ **Simpler:** No Dockerfile needed
✅ **Faster builds:** Render handles Java runtime automatically
✅ **Easier debugging:** Direct Java logs
✅ **Less configuration:** Just JAR deployment

---

## 💡 Quick Reference

**Render Dashboard:** https://dashboard.render.com
**Aiven Console:** https://console.aiven.io
**Build Command:** `./mvnw clean package -DskipTests`
**Start Command:** `java -jar target/*.jar`

---

**Your project is ready for non-Docker deployment! Just follow the steps above.** 🚀

