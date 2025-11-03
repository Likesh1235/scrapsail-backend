# 🚂 Railway Health Check Fix

## ❌ Problem
Railway deployment is failing with:
- Using Docker instead of native Java
- Health check failing: `/health` endpoint returning "service unavailable"
- App not starting properly

## ✅ Solution

### 1. Created `railway.json`
This file forces Railway to:
- Use **NIXPACKS** builder (not Docker)
- Use native Java runtime
- Build with: `./mvnw clean package -DskipTests`
- Start with: `java -jar target/*.jar`

### 2. Updated SecurityConfig
- Moved `/health` and `/ready` to the top of permitAll list
- Ensured all endpoints are accessible

### 3. Required Environment Variables

**In Railway Dashboard → Variables, add these:**

| Key | Value |
|-----|-------|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `MYSQL_URL` | `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?ssl-mode=REQUIRED` |
| `DB_USERNAME` | `avnadmin` |
| `DB_PASSWORD` | `AVNS_q3bA1ATbxyymPpRXPIY` |
| `PORT` | `8080` |
| `EMAIL_USERNAME` | `likeshkanna74@gmail.com` |
| `EMAIL_PASSWORD` | `rvou eevk bdwt iizl` |

⚠️ **CRITICAL:** Railway sets `PORT` automatically - don't override it, but include it for reference.

### 4. Railway Settings

**In Railway Dashboard → Settings:**

1. **Root Directory:** Leave empty (or `./`)
2. **Build Command:** `./mvnw clean package -DskipTests`
3. **Start Command:** `java -jar target/*.jar`
4. **Environment:** Should auto-detect as Java (verify this)

### 5. Health Check Configuration

Railway will automatically check `/health` endpoint. Make sure:
- ✅ Endpoint is accessible (SecurityConfig permits it)
- ✅ Application starts successfully
- ✅ Database connection works (or app should start even if DB fails initially)

---

## 🔍 Troubleshooting

### If Health Check Still Fails:

1. **Check Railway Logs:**
   - Go to Railway Dashboard → Your Service → Logs
   - Look for startup errors
   - Check for "Started BackendApplication" message

2. **Common Issues:**

   **Issue 1: Database Connection Fails**
   ```
   Communications link failure
   ```
   **Fix:** 
   - Check Aiven MySQL IP allowlist (add `0.0.0.0/0`)
   - Verify environment variables are correct
   - App should start even if DB fails (check logs)

   **Issue 2: Missing Environment Variables**
   ```
   Could not resolve placeholder 'MYSQL_URL'
   ```
   **Fix:** Add all 7 environment variables in Railway

   **Issue 3: Port Binding Issue**
   ```
   Port already in use
   ```
   **Fix:** Railway handles PORT automatically - don't override

3. **Test Health Endpoint Manually:**
   ```
   curl https://your-backend.up.railway.app/health
   ```
   Should return: `{"status":"UP",...}`

---

## ✅ Success Indicators

Your deployment is successful when:
- ✅ Build logs show: `[INFO] BUILD SUCCESS`
- ✅ Deploy logs show: `Started BackendApplication in X.XXX seconds`
- ✅ Health check passes: `/health` returns 200 OK
- ✅ Service status: "Active" in Railway dashboard

---

**After pushing these changes, Railway should:**
1. Use native Java runtime (not Docker)
2. Build successfully
3. Start the application
4. Pass health checks

