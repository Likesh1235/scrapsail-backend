# 🔧 FIX: Deployment Error - Environment Variables Not Working

## ❌ Current Error

```
Unable to determine Dialect without JDBC metadata
```

**This means:** Environment variables (`MYSQL_URL`, `DB_USERNAME`, `DB_PASSWORD`) are **NOT reaching your Docker container**.

---

## ✅ SOLUTION: Verify Environment Variables in Render Dashboard

### CRITICAL: This Must Be Done in Render Dashboard

Even though `render.yaml` has environment variables defined, **they must also be set in Render's dashboard** for Docker deployments.

---

## 📋 STEP-BY-STEP FIX

### Step 1: Open Render Dashboard

1. Go to: **https://dashboard.render.com**
2. Click on your **`scrapsail-backend`** service

### Step 2: Go to Settings

1. Click **"Settings"** tab (left sidebar)
2. Scroll down to **"Environment Variables"** section

### Step 3: Check All 6 Variables

**You MUST see exactly these 6 variables listed (case-sensitive!):**

1. ✅ `SPRING_PROFILES_ACTIVE`
2. ✅ `MYSQL_URL`
3. ✅ `DB_USERNAME`
4. ✅ `DB_PASSWORD`
5. ✅ `SERVER_PORT`
6. ✅ `PORT`

### Step 4: If Variables Are Missing or Wrong

#### Option A: Delete and Re-add (Recommended)

1. **For each incorrect/missing variable:**
   - If it exists but is wrong: Click the **trash/delete icon** (🗑️) next to it
   - If it doesn't exist: Skip to adding

2. **Add each variable one by one:**
   - Click **"Add Environment Variable"** button
   - Type **exact name** (copy-paste to avoid typos)
   - Type **exact value** (copy-paste to avoid typos)
   - Press Enter or click outside

**Variable 1:**
- **Key:** `SPRING_PROFILES_ACTIVE`
- **Value:** `prod`

**Variable 2:**
- **Key:** `MYSQL_URL`
- **Value:** `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?sslMode=REQUIRED`

**Variable 3:**
- **Key:** `DB_USERNAME`
- **Value:** `avnadmin`

**Variable 4:**
- **Key:** `DB_PASSWORD`
- **Value:** `AVNS_q3bA1ATbxyymPpRXPIY`

**Variable 5:**
- **Key:** `SERVER_PORT`
- **Value:** `8080`

**Variable 6:**
- **Key:** `PORT`
- **Value:** `8080`

3. **After adding all 6:**
   - Scroll to bottom of page
   - Click **"Save Changes"** button

#### Option B: Edit Existing Variables

1. If a variable exists but has wrong value:
   - Click on the variable
   - Edit the value
   - Press Enter or click outside

### Step 5: Verify Variables Are Correct

Click on each variable to verify its value matches exactly:

✅ **SPRING_PROFILES_ACTIVE** = `prod`
✅ **MYSQL_URL** = `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?sslMode=REQUIRED`
✅ **DB_USERNAME** = `avnadmin`
✅ **DB_PASSWORD** = `AVNS_q3bA1ATbxyymPpRXPIY`
✅ **SERVER_PORT** = `8080`
✅ **PORT** = `8080`

### Step 6: Redeploy

1. Click **"Events"** tab (left sidebar)
2. Click **"Manual Deploy"** button
3. Select **"Deploy latest commit"**
4. Wait for deployment to complete

---

## 🔍 How to Verify It's Working

### Check Logs After Redeploy

**✅ GOOD SIGNS (success):**
```
The following 1 profile is active: "prod"              ← Profile working!
HikariPool-1 - Starting...                             ← Connection starting!
HikariPool-1 - Start completed.                         ← Connected!
Tomcat started on port 8080                             ← Server started!
```

**❌ BAD SIGNS (still broken):**
```
Unable to determine Dialect without JDBC metadata       ← Still not working!
No active profile set                                   ← SPRING_PROFILES_ACTIVE missing!
Driver claims to not accept jdbcUrl, ${MYSQL_URL}       ← Variables not resolved!
```

---

## 🐛 Common Issues

### Issue 1: Variable Names Have Typos

**Wrong:**
- ❌ `mysql_url` (lowercase)
- ❌ `Mysql_Url` (mixed case)
- ❌ `MYSQL-URL` (hyphen instead of underscore)

**Correct:**
- ✅ `MYSQL_URL` (all uppercase, underscore)

### Issue 2: Variable Values Have Typos

**Common mistakes:**
- ❌ Missing `?sslMode=REQUIRED` from MYSQL_URL
- ❌ Extra spaces before/after values
- ❌ Wrong port number

### Issue 3: Variables Not Saved

**Solution:**
- Make sure you click **"Save Changes"** at bottom of page
- Wait for confirmation message

### Issue 4: Service Uses Blueprint vs Manual Setup

**If you're using Blueprint (render.yaml):**
- Variables in `render.yaml` should auto-sync
- **BUT** if they're not working, manually add them in Settings
- Sometimes Blueprint sync fails - manual is more reliable

**If you created service manually:**
- Variables MUST be manually set in Settings
- `render.yaml` is ignored for manual services

---

## ✅ What I Fixed in Code

I've also updated the code to be more robust:

1. **Updated Dockerfile:** Changed to shell form ENTRYPOINT to ensure env vars are available
2. **Already configured:** `application-prod.properties` correctly uses `${MYSQL_URL}`, `${DB_USERNAME}`, `${DB_PASSWORD}`
3. **Already configured:** Hibernate dialect is explicitly set

**But the code fixes won't help if Render isn't passing the environment variables to Docker!**

---

## 🎯 Next Steps

1. ✅ Go to Render Dashboard → Settings → Environment Variables
2. ✅ Verify all 6 variables exist with correct names and values
3. ✅ Save Changes
4. ✅ Manual Deploy
5. ✅ Check logs - should see "HikariPool-1 - Start completed"

---

## 📞 Still Not Working?

If you've verified all 6 variables are set correctly in Render Settings and it's still failing:

1. **Screenshot the Environment Variables section** and share it
2. **Share the latest deployment logs** (the full error)
3. **Check if service was created from Blueprint or manually**

---

**The #1 cause of this error is environment variables not being set in Render Settings. Fix this first!**

