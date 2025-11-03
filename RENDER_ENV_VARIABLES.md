# 🔐 Render Environment Variables - Aiven MySQL

## 📋 Exact Values from Your Aiven Dashboard

Based on your Aiven MySQL service, here are the **EXACT** environment variables to add in Render:

---

## ✅ Environment Variables to Add in Render

Go to: **Render Dashboard** → **Your Service** → **Settings** → **Environment Variables**

Add these **5 variables** (one by one):

### Variable 1: SPRING_PROFILES_ACTIVE
- **Key:** `SPRING_PROFILES_ACTIVE`
- **Value:** `prod`

### Variable 2: MYSQL_URL
- **Key:** `MYSQL_URL`
- **Value:** `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?ssl-mode=REQUIRED`

**Note:** Uses `ssl-mode=REQUIRED` (with hyphen) as shown in Aiven dashboard.

### Variable 3: DB_USERNAME
- **Key:** `DB_USERNAME`
- **Value:** `avnadmin`

### Variable 4: DB_PASSWORD
- **Key:** `DB_PASSWORD`
- **Value:** `AVNS_q3bA1ATbxyymPpRXPIY`

### Variable 5: PORT
- **Key:** `PORT`
- **Value:** `8080`

---

## 📝 Copy-Paste Format

```
SPRING_PROFILES_ACTIVE=prod

MYSQL_URL=jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?ssl-mode=REQUIRED

DB_USERNAME=avnadmin

DB_PASSWORD=AVNS_q3bA1ATbxyymPpRXPIY

PORT=8080
```

---

## 🎯 Connection Details Summary

| Property | Value |
|---------|-------|
| **Host** | `scrapsaildb-scrapsaildb.e.aivencloud.com` |
| **Port** | `22902` |
| **Database** | `defaultdb` |
| **User** | `avnadmin` |
| **Password** | `AVNS_q3bA1ATbxyymPpRXPIY` |
| **SSL Mode** | `REQUIRED` |

---

## ✅ After Adding Variables

1. **Click "Save Changes"** at the bottom
2. Go to **"Events"** tab
3. Click **"Manual Deploy"**
4. Select **"Clear build cache & Deploy"**
5. Wait 2-3 minutes for deployment

---

## 🔍 Expected Success Logs

When it works, you'll see in Render logs:

```
HikariPool-1 - Starting...
HikariPool-1 - Start completed. ✅
Connected to MySQL: scrapsaildb-scrapsaildb.e.aivencloud.com ✅
Tomcat started on port(s): 8080 ✅
Started BackendApplication in XX seconds ✅
```

---

## 🚨 Important Notes

1. **SSL Certificate:** The `ca.pem` file is already in your project (`src/main/resources/ca.pem`)
2. **SSL Configuration:** Your `application-prod.properties` is already configured with VERIFY_CA
3. **JDBC URL Format:** Aiven shows `ssl-mode=REQUIRED` (with hyphen), use exactly as shown
4. **No Hardcoded Values:** These should be set in Render Dashboard, not in code

---

**All set! Add these 5 environment variables to your Render service! 🚀**

