# 🚀 Railway Setup - Action Required NOW

## ⚠️ Current Status from Logs

Your app is starting but needs these environment variables set in Railway:

### ❌ Missing:
- `SPRING_PROFILES_ACTIVE` - App is using "default" profile instead of "prod"
- Database connection variables (SPRING_DATASOURCE_URL or DATABASE_URL)

---

## ✅ IMMEDIATE ACTION: Set Railway Variables

### Go to: Railway Dashboard → Your Service → Variables Tab

### **REQUIRED Variables:**

| Key | Value | Why |
|-----|-------|-----|
| `SPRING_PROFILES_ACTIVE` | `prod` | **CRITICAL** - Uses production profile with optimized settings |
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?sslMode=REQUIRED` | Database connection URL |
| `SPRING_DATASOURCE_USERNAME` | `avnadmin` | Database username |
| `SPRING_DATASOURCE_PASSWORD` | `AVNS_q3bA1ATbxyymPpRXPIY` | Database password |

### **OPTIONAL (Recommended):**

| Key | Value | Why |
|-----|-------|-----|
| `JAVA_TOOL_OPTIONS` | `-Xmx512M` | Prevents memory issues |

### **DO NOT SET:**
- ❌ `PORT` - Railway sets this automatically (don't override)

---

## 📋 Step-by-Step Setup

### 1. Open Railway Dashboard
- Go to: https://railway.app
- Select your **scrapsail** project
- Click on your **backend service**

### 2. Go to Variables Tab
- Click **"Variables"** tab in the left sidebar
- Or click **Settings** → **Variables**

### 3. Add Each Variable

**Click "New Variable" for each:**

```
SPRING_PROFILES_ACTIVE = prod
```

```
SPRING_DATASOURCE_URL = jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?sslMode=REQUIRED
```

```
SPRING_DATASOURCE_USERNAME = avnadmin
```

```
SPRING_DATASOURCE_PASSWORD = AVNS_q3bA1ATbxyymPpRXPIY
```

```
JAVA_TOOL_OPTIONS = -Xmx512M
```

### 4. Save and Redeploy
- After adding all variables, Railway will **auto-redeploy**
- OR manually redeploy: **Deployments** → **Redeploy**

---

## ✅ Expected Logs After Fix

After setting variables, Railway logs should show:

```
✅ The following 1 profile is active: "prod"
✅ Starting BackendApplication
✅ SPRING_DATASOURCE_URL is set (Aiven MySQL detected)
✅ Database connection configured
✅ Started BackendApplication in X.XXX seconds
✅ Tomcat started on port(s): 8080 (or Railway's assigned port)
```

---

## 🐛 If Still Having Issues

### Check Railway Logs For:

1. **"No active profile set"**
   - **Fix:** Set `SPRING_PROFILES_ACTIVE=prod`

2. **"Connection is not available"**
   - **Fix:** Verify `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` are set correctly

3. **"Port already in use"**
   - **Fix:** Don't set PORT manually - Railway handles it

---

## 📊 Verification Checklist

After setting variables, verify:

- [ ] `SPRING_PROFILES_ACTIVE=prod` is set
- [ ] `SPRING_DATASOURCE_URL` is set correctly
- [ ] `SPRING_DATASOURCE_USERNAME` is set
- [ ] `SPRING_DATASOURCE_PASSWORD` is set
- [ ] Railway has redeployed
- [ ] Logs show "prod" profile active
- [ ] App starts successfully
- [ ] Health endpoint works: `/health`

---

## 🎯 Quick Copy-Paste

**For Railway Variables, copy these exact values:**

```
SPRING_PROFILES_ACTIVE
prod

SPRING_DATASOURCE_URL
jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?sslMode=REQUIRED

SPRING_DATASOURCE_USERNAME
avnadmin

SPRING_DATASOURCE_PASSWORD
AVNS_q3bA1ATbxyymPpRXPIY

JAVA_TOOL_OPTIONS
-Xmx512M
```

---

**Set these variables NOW and Railway will auto-redeploy! 🚀**

