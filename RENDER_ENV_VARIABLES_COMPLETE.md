# 🔐 Complete Render Environment Variables Guide

## 📋 All Environment Variables to Add in Render

Go to: **Render Dashboard** → **Your Service** → **Settings** → **Environment Variables**

---

## ✅ Required Variables (Must Add)

### 1. Database Configuration

| Key | Value |
|-----|-------|
| `SPRING_PROFILES_ACTIVE` | `prod` |
| `MYSQL_URL` | `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?ssl-mode=REQUIRED` |
| `DB_USERNAME` | `avnadmin` |
| `DB_PASSWORD` | `AVNS_q3bA1ATbxyymPpRXPIY` |

### 2. Server Configuration

| Key | Value |
|-----|-------|
| `PORT` | `8080` |

---

## 📧 Email Configuration (Required if using email features)

| Key | Value |
|-----|-------|
| `EMAIL_USERNAME` | `likeshkanna74@gmail.com` |
| `EMAIL_PASSWORD` | `rvou eevk bdwt iizl` |

**Optional (defaults are set, but you can override):**
- `EMAIL_HOST` = `smtp.gmail.com` (default)
- `EMAIL_PORT` = `587` (default)

---

## 🌐 Optional Variables

| Key | Value | Purpose |
|-----|-------|---------|
| `FRONTEND_URL` | `https://your-frontend-url.vercel.app` | CORS configuration - allows your frontend to call the API |
| `SENTRY_DSN` | Your Sentry DSN | Error tracking (if using Sentry) |

---

## 📝 Quick Copy-Paste Format

```
SPRING_PROFILES_ACTIVE=prod

MYSQL_URL=jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?ssl-mode=REQUIRED

DB_USERNAME=avnadmin

DB_PASSWORD=AVNS_q3bA1ATbxyymPpRXPIY

PORT=8080

EMAIL_USERNAME=likeshkanna74@gmail.com

EMAIL_PASSWORD=rvou eevk bdwt iizl
```

---

## 🎯 Step-by-Step: Adding Variables in Render

1. **Go to Render Dashboard**
   - https://dashboard.render.com
   - Click your `scrapsail-backend` service

2. **Open Settings**
   - Click **"Settings"** tab (left sidebar)
   - Scroll to **"Environment Variables"** section

3. **Add Each Variable**
   - Click **"Add Environment Variable"**
   - Enter **Key** (exact name from table above)
   - Enter **Value** (exact value from table above)
   - Press Enter or click outside
   - Repeat for all 7 variables

4. **Save Changes**
   - Scroll to bottom
   - Click **"Save Changes"**

5. **Redeploy**
   - Go to **"Events"** tab
   - Click **"Manual Deploy"**
   - Select **"Clear build cache & Deploy"**

---

## ✅ Verification Checklist

After adding all variables, verify:

- [ ] All 7 variables are listed in Render Settings
- [ ] Variable names match EXACTLY (case-sensitive)
- [ ] Values are correct (no extra spaces)
- [ ] "Save Changes" was clicked
- [ ] Manual deploy was triggered
- [ ] Deployment succeeds in logs
- [ ] `/health` endpoint returns 200
- [ ] `/ready` endpoint returns 200 (or 503 if DB not connected yet)

---

## 🚨 Important Notes

1. **Variable Names Are Case-Sensitive**
   - ✅ `MYSQL_URL` (correct)
   - ❌ `mysql_url` (wrong)
   - ❌ `Mysql_Url` (wrong)

2. **No Spaces in Values**
   - ✅ `prod` (correct)
   - ❌ ` prod ` (wrong - has spaces)

3. **Email Password is an App Password**
   - Use the Gmail App Password, not your regular Gmail password
   - Current value: `rvou eevk bdwt iizl`

4. **Sensitive Variables Use `sync: false`**
   - These won't appear in `render.yaml` (security)
   - Must be added manually in Render Dashboard

---

**🎉 Once all variables are added and deployed, your backend will be fully configured!**

