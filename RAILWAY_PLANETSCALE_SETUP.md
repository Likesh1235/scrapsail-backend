# 🚀 Railway + PlanetScale Setup Guide

## 🧠 What's Happening

**Error:** `HikariPool-1 - Connection is not available, request timed out`

Spring Boot tried to connect to MySQL (through `spring.datasource.url`), but:
- Either your `MYSQL_URL` variable in Railway is wrong, OR
- Your PlanetScale database is not accessible / not whitelisted

---

## 🧩 Fix it Step-by-Step (Railway + PlanetScale)

### 🥇 Step 1: Get PlanetScale Connection URL

1. **Go to PlanetScale Dashboard:** https://app.planetscale.com
2. **Select your ScrapSail database**
3. **Click "Connect" tab**
4. **Choose "Java (JDBC)"**
5. **Copy the full URL**

The URL looks like this:
```
jdbc:mysql://aws.connect.psdb.cloud/scrapsail?sslMode=VERIFY_IDENTITY&user=YOUR_USER&password=YOUR_PASSWORD
```

**OR** if PlanetScale gives you the format:
```
mysql://username:password@aws.connect.psdb.cloud/scrapsail?sslMode=VERIFY_IDENTITY
```

Convert it to JDBC format:
```
jdbc:mysql://aws.connect.psdb.cloud/scrapsail?sslMode=VERIFY_IDENTITY&user=username&password=password
```

---

### 🥈 Step 2: Set Railway Environment Variables

**Go to:** Railway Dashboard → Your Project → **Variables** tab

**Add/Update these variables:**

| Key | Value | Description |
|-----|-------|-------------|
| `MYSQL_URL` | `jdbc:mysql://aws.connect.psdb.cloud/scrapsail?sslMode=VERIFY_IDENTITY&user=YOUR_USER&password=YOUR_PASSWORD` | **CRITICAL** - Full JDBC URL from PlanetScale |
| `SPRING_PROFILES_ACTIVE` | `prod` | Use production profile (optional, but recommended) |
| `PORT` | *(Railway sets automatically)* | Don't override this |

**⚠️ Important:**
- Replace `YOUR_USER` and `YOUR_PASSWORD` with your actual PlanetScale credentials
- The URL must be in JDBC format: `jdbc:mysql://...`
- URL-encode special characters in password if needed

---

### 🥉 Step 3: Verify PlanetScale Connection

**Test connection from your local PC:**

```bash
mysql -h aws.connect.psdb.cloud -u YOUR_USER -p --ssl-mode=REQUIRED
```

If this fails:
- Check your PlanetScale credentials
- Verify your IP is not blocked
- Check PlanetScale dashboard for connection issues

---

### 🧰 Step 4: Redeploy in Railway

1. **Save all variables** in Railway
2. **Railway will auto-redeploy**, OR
3. **Manually redeploy:** Railway Dashboard → Deployments → "Redeploy"

**Wait for logs to show:**
```
Tomcat started on port(s): 8080
Started BackendApplication in X.XXX seconds
```

**Then visit:**
```
https://<your-app-name>.up.railway.app/api/...
```

---

## ✅ Expected Success Indicators

### Railway Logs Should Show:
```
✅ The following 1 profile is active: "prod" (if SPRING_PROFILES_ACTIVE=prod is set)
✅ Starting BackendApplication
✅ Tomcat started on port(s): 8080
✅ Started BackendApplication in X.XXX seconds
```

### Test Endpoints:
- Health: `https://<your-app>.up.railway.app/health`
- API: `https://<your-app>.up.railway.app/api/...`

---

## 🐛 Troubleshooting

### Issue 1: Still Getting Connection Timeout

**Check:**
1. `MYSQL_URL` is set correctly in Railway Variables
2. URL is in JDBC format (`jdbc:mysql://...`)
3. PlanetScale credentials are correct
4. PlanetScale database is active (not sleeping)

**Fix:**
- Copy fresh URL from PlanetScale "Connect" tab
- Verify password doesn't have special characters that need encoding
- Check PlanetScale dashboard for any connection issues

### Issue 2: "No active profile set"

**Fix:**
- Set `SPRING_PROFILES_ACTIVE=prod` in Railway Variables

### Issue 3: "SSL Error" or "Certificate Error"

**Fix:**
- Ensure `sslMode=VERIFY_IDENTITY` is in the URL
- PlanetScale requires SSL for all connections

---

## 📝 Quick Checklist

Before redeploying, verify:

- [ ] `MYSQL_URL` is set in Railway Variables
- [ ] URL is in JDBC format (`jdbc:mysql://...`)
- [ ] PlanetScale credentials are correct
- [ ] `SPRING_PROFILES_ACTIVE=prod` is set (optional but recommended)
- [ ] All variables are saved in Railway
- [ ] PlanetScale database is active

---

## 🔗 Useful Links

- **Railway Dashboard:** https://railway.app
- **PlanetScale Dashboard:** https://app.planetscale.com
- **PlanetScale Connection Guide:** https://planetscale.com/docs/tutorials/connect-any-application

---

**After fixing, your app should start successfully and connect to PlanetScale! 🎉**

