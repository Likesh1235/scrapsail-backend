# Delete and Recreate Service - Step by Step

Since you can't find the runtime option, let's DELETE and RECREATE the service correctly.

## Step 1: Delete Current Service

1. Go to: https://dashboard.render.com
2. Click on your **`scrapsail-backend`** service
3. Scroll to the bottom of the Settings page
4. Click **"Delete Service"** (it's in red, at the very bottom)
5. Type the service name to confirm: `scrapsail-backend`
6. Click **"Delete"** to confirm

## Step 2: Create New Service - FOLLOW EXACTLY

1. In Render dashboard, click **"New +"** button (top right)
2. Select **"Web Service"**

### Connect Repository:
3. Click **"Connect GitHub"** (or "Connect account" if already connected)
4. Authorize Render to access GitHub
5. Select repository: **`Likesh1235/scrapsail-backend`**

### Configure Service:
6. **Name:** `scrapsail-backend`
7. **Region:** Select `Singapore` from dropdown
8. **Branch:** Type or select `clean-main` (NOT main!)
9. **Root Directory:** Leave EMPTY
10. **Runtime:** ⚠️ **IMPORTANT - Click the dropdown and select "Java 17"** (NOT "Node" or "Auto-detect")
11. **Build Command:** Copy this EXACTLY:
   ```
   chmod +x ./mvnw && ./mvnw clean package -DskipTests
   ```
12. **Start Command:** Copy this EXACTLY:
   ```
   java -jar target/*.jar
   ```
13. **Plan:** Select `Free`

### Add Environment Variables:
14. Scroll down to **"Environment Variables"** section
15. Click **"Add Environment Variable"** button
16. Add these ONE BY ONE (click "Add Environment Variable" for each):

   **Variable 1:**
   - Key: `SPRING_PROFILES_ACTIVE`
   - Value: `prod`
   
   **Variable 2:**
   - Key: `MYSQL_URL`
   - Value: `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?sslMode=REQUIRED`
   
   **Variable 3:**
   - Key: `DB_USERNAME`
   - Value: `avnadmin`
   
   **Variable 4:**
   - Key: `DB_PASSWORD`
   - Value: `AVNS_q3bA1ATbxyymPpRXPIY`
   
   **Variable 5:**
   - Key: `SERVER_PORT`
   - Value: `8080`
   
   **Variable 6:**
   - Key: `PORT`
   - Value: `8080`

### Deploy:
17. Check that **"Auto-Deploy"** is enabled (should be checked by default)
18. Click **"Create Web Service"** button at the bottom

## Step 3: Wait for Deployment

- First deployment takes 5-10 minutes
- Watch the build logs
- You should see: "Using Java 17 runtime" ✅
- Build should succeed
- Service will become "Live"

## Step 4: Verify Success

1. Check logs - should show Java/Maven output (NOT Node.js)
2. Test health endpoint: `https://your-service-name.onrender.com/health`
3. Should return: `{"status":"UP"}`

---

## Screenshot Guide Locations

If you can't find options, look for:
- **Runtime dropdown:** Usually near "Root Directory" or in "Environment" section
- **Build/Start commands:** Usually in a section called "Build & Deploy"
- **Environment Variables:** Usually a separate section with "+ Add" button

---

## If Still Can't Find "Runtime" Option

**Option A:** Try clicking "Advanced" or "More Options" button - runtime might be there

**Option B:** After creating service, you can:
1. Go to Settings immediately
2. Look for "Environment" or "Build Settings"
3. Runtime might be changeable there after creation

**Option C:** Contact me with a screenshot of what you see, and I'll guide you specifically

---

**Follow these steps EXACTLY and it WILL work!** ✅

