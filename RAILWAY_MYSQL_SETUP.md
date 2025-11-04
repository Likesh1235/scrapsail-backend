# 🚀 Railway MySQL Setup Guide

## 🧠 What's Happening

**Error:** `HikariPool-1 - Connection is not available, request timed out`

Spring Boot tried to connect to MySQL, but the database connection isn't configured correctly.

---

## ✅ Step-by-Step Setup (Railway MySQL)

### 🥇 Step 1: Add MySQL Service in Railway

1. **Go to Railway Dashboard:** https://railway.app
2. **Select your project** (scrapsail)
3. **Click "+ New"** → **"Database"** → **"Add MySQL"**
4. Railway will automatically create a MySQL database
5. Railway will **automatically set `DATABASE_URL`** environment variable

**That's it!** Railway handles the connection automatically.

---

### 🥈 Step 2: Verify Environment Variables

**Go to:** Railway Dashboard → Your Project → **Variables** tab

**Railway automatically provides:**
- ✅ `DATABASE_URL` - Auto-set by Railway (format: `mysql://user:password@host:port/database`)
- ✅ `PORT` - Auto-set by Railway

**Optional (recommended):**
- `SPRING_PROFILES_ACTIVE=prod` - Use production profile

**You don't need to set:**
- ❌ `MYSQL_URL` - Not needed (Railway provides `DATABASE_URL`)
- ❌ `DB_USERNAME` - Not needed (included in `DATABASE_URL`)
- ❌ `DB_PASSWORD` - Not needed (included in `DATABASE_URL`)

---

### 🥉 Step 3: How It Works

The `RailwayDatabaseConfig` class automatically:
1. Reads Railway's `DATABASE_URL` environment variable
2. Converts it from `mysql://user:pass@host:port/db` to `jdbc:mysql://host:port/db?user=user&password=pass`
3. Spring Boot uses this JDBC URL to connect

**You don't need to do anything!** Railway handles it all.

---

### 🧰 Step 4: Redeploy

After adding the MySQL service:
1. Railway will **auto-redeploy** your app
2. **Check logs** - Should see:
   ```
   ✅ Started BackendApplication in X.XXX seconds
   ✅ Tomcat started on port(s): 8080
   ```

---

## 📊 Expected Behavior

### ✅ Railway Logs Should Show:
```
✅ Starting BackendApplication
✅ HikariPool-1 - Start completed
✅ Started BackendApplication in X.XXX seconds
✅ Tomcat started on port(s): 8080
```

### ✅ Test Endpoints:
- Health: `https://<your-app>.up.railway.app/health`
- API: `https://<your-app>.up.railway.app/api/...`

---

## 🐛 Troubleshooting

### Issue 1: Still Getting Connection Timeout

**Check:**
1. MySQL service is added in Railway (check Services tab)
2. MySQL service is running (not paused)
3. `DATABASE_URL` is visible in Variables tab (Railway sets it automatically)

**Fix:**
- Make sure MySQL service is added and running
- Railway automatically sets `DATABASE_URL` - you don't need to set it manually

### Issue 2: "No active profile set"

**Fix:**
- Set `SPRING_PROFILES_ACTIVE=prod` in Railway Variables (optional but recommended)

### Issue 3: Database Connection Fails

**Check Railway Logs:**
- Look for specific error messages
- Verify MySQL service is running in Railway dashboard

---

## 📝 Quick Checklist

Before deploying:

- [ ] MySQL service is added in Railway (Services tab)
- [ ] MySQL service is running (not paused)
- [ ] `DATABASE_URL` appears in Variables tab (auto-set by Railway)
- [ ] `SPRING_PROFILES_ACTIVE=prod` is set (optional but recommended)

---

## 🎯 Key Points

1. **Railway automatically provides `DATABASE_URL`** when you add MySQL service
2. **No manual configuration needed** - RailwayDatabaseConfig handles conversion
3. **Don't set `MYSQL_URL` manually** unless you're using external database
4. **Railway's MySQL is free tier** - perfect for development

---

## 🔗 Useful Links

- **Railway Dashboard:** https://railway.app
- **Railway MySQL Docs:** https://docs.railway.app/databases/mysql

---

**After adding MySQL service, Railway handles everything automatically! 🎉**

