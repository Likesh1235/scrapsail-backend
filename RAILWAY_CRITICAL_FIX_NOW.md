# 🚨 CRITICAL FIX - Railway Deployment Failure

## ❌ Problem from Your Logs

**Your Railway logs show:**
```
No active profile set, falling back to 1 default profile: "default"
org.hibernate.exception.JDBCConnectionException: unable to obtain isolated JDBC connection
Communications link failure
```

**This means:**
1. ❌ **`SPRING_PROFILES_ACTIVE` is NOT SET in Railway Variables**
2. ❌ App is using default profile (not `prod`)
3. ❌ Hibernate is trying to connect to database on startup
4. ❌ Connection fails, causing app to crash

---

## ✅ IMMEDIATE ACTION REQUIRED

### STEP 1: Set Environment Variables in Railway (CRITICAL!)

**Go to:** Railway Dashboard → Your Service → **Variables** tab

**You MUST add these 7 variables:**

| Key | Value | Critical? |
|-----|-------|-----------|
| `SPRING_PROFILES_ACTIVE` | `prod` | ⚠️ **YES - MOST CRITICAL** |
| `MYSQL_URL` | `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?ssl-mode=REQUIRED` | ✅ Yes |
| `DB_USERNAME` | `avnadmin` | ✅ Yes |
| `DB_PASSWORD` | `AVNS_q3bA1ATbxyymPpRXPIY` | ✅ Yes |
| `PORT` | (Railway sets automatically - don't set manually) | ⚠️ Don't override |
| `EMAIL_USERNAME` | `likeshkanna74@gmail.com` | Optional |
| `EMAIL_PASSWORD` | `rvou eevk bdwt iizl` | Optional |

**⚠️ WITHOUT `SPRING_PROFILES_ACTIVE=prod`, THE APP WILL:**
- Use default profile
- Try to connect to localhost:3306
- Fail to start

---

### STEP 2: Fix Aiven IP Allowlist

**The "Communications link failure" suggests Aiven is blocking Railway IPs.**

1. **Go to:** https://console.aiven.io
2. **Select:** Your MySQL service (`scrapsaildb`)
3. **Go to:** "IP allowlist" or "Network" section
4. **Add:** `0.0.0.0/0` (allows all IPs)
5. **Wait:** 2-3 minutes
6. **Redeploy:** On Railway

---

### STEP 3: Verify Railway Settings

**Go to:** Railway Dashboard → Your Service → **Settings**

**Verify:**
- **Build Command:** `./mvnw clean package -DskipTests`
- **Start Command:** `java -jar target/*.jar`
- **Root Directory:** Empty (or `./`)

---

## 📊 What the Fixes Do

### 1. LazyDataSourceConfig
- Creates DataSource but doesn't validate connection on startup
- `minimum-idle=0` - No connections created on startup
- `connection-test-query=null` - No connection test on startup
- Connections created only when first used

### 2. JPA Lazy Loading
- `ddl-auto=none` - Hibernate doesn't try to update schema on startup
- `bootstrap-mode=lazy` - Repositories initialized on first use
- App starts even if DB is not available

### 3. Environment Variables
- `SPRING_PROFILES_ACTIVE=prod` - Uses production profile
- Production profile has all the lazy loading settings
- App won't try to connect to localhost

---

## ✅ Expected Behavior After Fix

### Railway Logs Should Show:
```
The following 1 profile is active: "prod"
Starting BackendApplication
✅ Application is ready to accept requests
Started BackendApplication in X.XXX seconds
```

**NOT:**
```
No active profile set, falling back to 1 default profile: "default"
```

### Health Endpoints Should Work:
- `/health` → Returns `{"status":"UP",...}`
- `/ready` → Returns `{"status":"ready",...}`
- `/` → Returns API information

---

## 🎯 Quick Checklist

Before redeploying, verify:

- [ ] `SPRING_PROFILES_ACTIVE=prod` is set in Railway Variables
- [ ] `MYSQL_URL` is set correctly
- [ ] `DB_USERNAME` is set correctly
- [ ] `DB_PASSWORD` is set correctly
- [ ] Aiven IP allowlist includes `0.0.0.0/0`
- [ ] All variables are saved in Railway

---

## 🚀 After Setting Variables

1. **Railway will auto-redeploy** (or manually redeploy)
2. **Check logs** - Should see "prod" profile active
3. **Test endpoints** - `/health` should return 200 OK
4. **Verify** - App starts successfully even if DB fails

---

**THE MOST CRITICAL FIX: SET `SPRING_PROFILES_ACTIVE=prod` IN RAILWAY VARIABLES!** 🚨

Without this, all other fixes won't work because the app uses the wrong profile!



