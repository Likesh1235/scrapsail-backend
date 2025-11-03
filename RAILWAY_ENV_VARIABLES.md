# 🔐 Railway Environment Variables Setup

## Your MySQL Database Credentials

Based on your Railway MySQL service, here are your credentials:

| Variable | Value |
|----------|-------|
| `MYSQLHOST` | `hopper.proxy.rlwy.net` |
| `MYSQLPORT` | `51116` |
| `MYSQLDATABASE` | `railway` |
| `MYSQLUSER` | `root` |
| `MYSQLPASSWORD` | `MoxMmvgySDSiKQceRvMQREvioTGxmzOZ` |

---

## 🚀 How to Add to Railway Backend Service

### Step 1: Go to Your Backend Service
1. In Railway dashboard, click on your **backend service** (not MySQL service)
2. Go to **"Variables"** tab

### Step 2: Add Environment Variables

Add these **5 variables** to your backend service:

#### Variable 1: MYSQLHOST
- **Name:** `MYSQLHOST`
- **Value:** `hopper.proxy.rlwy.net`
- Click **"Add"**

#### Variable 2: MYSQLPORT
- **Name:** `MYSQLPORT`
- **Value:** `51116`
- Click **"Add"**

#### Variable 3: MYSQLDATABASE
- **Name:** `MYSQLDATABASE`
- **Value:** `railway`
- Click **"Add"**

#### Variable 4: MYSQLUSER
- **Name:** `MYSQLUSER`
- **Value:** `root`
- Click **"Add"**

#### Variable 5: MYSQLPASSWORD
- **Name:** `MYSQLPASSWORD`
- **Value:** `MoxMmvgySDSiKQceRvMQREvioTGxmzOZ`
- Click **"Add"** (⚠️ Keep this secret!)

### Step 3: Redeploy
After adding all variables, Railway will automatically redeploy your service.

---

## ✅ Verification

Once deployed, your backend will automatically:
1. Construct the JDBC URL: `jdbc:mysql://hopper.proxy.rlwy.net:51116/railway`
2. Connect to the MySQL database
3. Start on port 8080

Test the health endpoint:
```
https://your-backend-url.up.railway.app/health
```

---

## 🔗 Quick Reference

**Connection String:**
```
jdbc:mysql://hopper.proxy.rlwy.net:51116/railway
```

**Username:** `root`  
**Password:** `MoxMmvgySDSiKQceRvMQREvioTGxmzOZ`

---

**All set! Add these 5 variables to your Railway backend service and deploy! 🚀**

