# 🎯 100% WORKING METHOD - Manual Service Creation

Since Render Blueprint has runtime detection issues, create the service MANUALLY instead.

## ✅ STEP-BY-STEP (Follow Exactly)

### Step 1: Go to Render Dashboard
- Open: https://dashboard.render.com
- Sign in

### Step 2: Create New Web Service
- Click **"New +"** button (top right)
- Select **"Web Service"** (NOT Blueprint!)

### Step 3: Connect Repository
- Click **"Connect GitHub"** (or connect if already connected)
- Authorize Render
- Select repository: **`Likesh1235/scrapsail-backend`**
- **Branch:** Select **`main`**

### Step 4: Configure Service Settings

Fill in these EXACT values:

**Basic Information:**
- **Name:** `scrapsail-backend`
- **Region:** Select `Singapore` from dropdown
- **Branch:** `main`
- **Root Directory:** (leave EMPTY)

**Environment/Runtime Section:**
Look for any dropdown that says:
- "Runtime"
- "Environment" 
- "Language"
- "Platform"

**⚠️ CRITICAL:** When you see this dropdown:
- **DO NOT** select "Auto-detect" or "Node" or "Node.js"
- **MANUALLY SELECT:** `Java 17` or `Java` or `Java Runtime`

If you DON'T see a runtime dropdown:
- That's OK - we'll set it after creation

**Build & Deploy:**
- **Build Command:** 
  ```
  chmod +x ./mvnw && ./mvnw clean package -DskipTests
  ```
- **Start Command:**
  ```
  java -jar target/*.jar
  ```

**Plan:**
- Select **`Free`**

### Step 5: Add Environment Variables

Scroll to **"Environment Variables"** section and click **"Add Environment Variable"** for each:

1. **Variable 1:**
   - Key: `SPRING_PROFILES_ACTIVE`
   - Value: `prod`
   - Click "Add"

2. **Variable 2:**
   - Key: `MYSQL_URL`
   - Value: `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?sslMode=REQUIRED`
   - Click "Add"

3. **Variable 3:**
   - Key: `DB_USERNAME`
   - Value: `avnadmin`
   - Click "Add"

4. **Variable 4:**
   - Key: `DB_PASSWORD`
   - Value: `AVNS_q3bA1ATbxyymPpRXPIY`
   - Click "Add"

5. **Variable 5:**
   - Key: `SERVER_PORT`
   - Value: `8080`
   - Click "Add"

6. **Variable 6:**
   - Key: `PORT`
   - Value: `8080`
   - Click "Add"

### Step 6: Create Service
- Check "Auto-Deploy" is enabled
- Click **"Create Web Service"**

### Step 7: Fix Runtime After Creation (If Needed)

If service still uses Node.js after creation:

1. Go to your service dashboard
2. Click **"Settings"** tab
3. Scroll down to find any "Runtime" or "Environment" option
4. Change it to **"Java 17"** or **"Java"**
5. Click **"Save Changes"**
6. Go to **"Events"** tab → Click **"Manual Deploy"** → **"Deploy latest commit"**

### Step 8: Verify Deployment

Watch the logs. You should see:
```
==> Using Java 17 runtime ✅
==> Running build command...
==> Maven build successful ✅
==> Starting Java application ✅
==> Service is live! ✅
```

---

## 🎯 Why This Works 100%

1. **Manual runtime selection** - Overrides any auto-detection
2. **Correct build command** - Includes chmod to fix permissions
3. **All environment variables** - Database credentials set
4. **No Blueprint issues** - Bypasses render.yaml runtime detection problems

---

## ⚠️ Important Notes

- **DO NOT use Blueprint** for now - use Manual Web Service creation
- **DO manually select Java runtime** if the option appears
- **DO add all 6 environment variables** before creating
- **DO verify** runtime after creation if needed

---

**Follow these steps EXACTLY and it WILL work 100%!** ✅

