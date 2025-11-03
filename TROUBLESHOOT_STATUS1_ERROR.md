# 🔧 Fix: Exited with status 1

## The Problem
Application starts but exits with status 1. This usually means:
1. **Database connection failed** (most common)
2. **Application startup error**
3. **Missing environment variable**

---

## 🔍 Check Full Logs

**In Render Dashboard:**
1. Go to your service → **"Logs"** tab
2. Scroll to see **ALL error messages**
3. Look for these patterns:

### Database Connection Error:
```
Cannot create PoolableConnectionFactory
Communications link failure
Access denied for user
Unknown database
```

### Missing Environment Variable:
```
Could not resolve placeholder 'MYSQL_URL'
Could not resolve placeholder 'DB_USERNAME'
```

### Port Error:
```
Port 8080 is already in use
BindException
```

---

## ✅ Quick Fixes

### Fix 1: Verify Environment Variables

1. Go to your service → **"Settings"** → **"Environment Variables"**
2. Verify ALL 6 variables exist:
   - `SPRING_PROFILES_ACTIVE` = `prod`
   - `MYSQL_URL` = `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?sslMode=REQUIRED`
   - `DB_USERNAME` = `avnadmin`
   - `DB_PASSWORD` = `AVNS_q3bA1ATbxyymPpRXPIY`
   - `SERVER_PORT` = `8080`
   - `PORT` = `8080`

3. **Check for typos** in variable names
4. **Check values** are correct (especially DB_PASSWORD)

### Fix 2: Check Database Connection

1. Verify Aiven MySQL is running
2. Test connection from your local machine:
   ```bash
   mysql -h scrapsaildb-scrapsaildb.e.aivencloud.com -P 22902 -u avnadmin -p
   ```
3. Check if database `defaultdb` exists

### Fix 3: Improve Error Logging

The application.properties has been updated to:
- Show more detailed errors
- Handle connection timeouts better
- Remove Hibernate deprecation warning

**Push the changes:**
```bash
git add .
git commit -m "Fix startup issues: improve error handling, remove Hibernate warning"
git push origin main
```

Render will auto-redeploy.

---

## 📋 What to Share

To help debug, share:

1. **Full logs** from Render (scroll through all error messages)
2. **Error messages** that appear before "Exited with status 1"
3. **Environment variables** you set (names and if values look correct)

---

## 🎯 Common Solutions

### If Database Connection Fails:
- Verify Aiven MySQL allows connections from Render's IP
- Check SSL mode is correct in connection string
- Verify credentials are correct

### If Environment Variable Missing:
- Double-check variable names (case-sensitive!)
- Make sure all 6 variables are added
- Check for spaces or special characters

### If Application Error:
- Check full stack trace in logs
- Look for specific error message
- Common: Missing dependency, configuration error

---

**The updated application.properties should help show better error messages!**

