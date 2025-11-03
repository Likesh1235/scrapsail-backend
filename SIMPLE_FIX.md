# 🚀 SIMPLE FIX - Two Options

## OPTION 1: Use Render Blueprint (EASIEST) ⭐ RECOMMENDED

If you can't find runtime settings, use Render's Blueprint feature:

### Steps:

1. **Delete your current service:**
   - Go to: https://dashboard.render.com
   - Click on `scrapsail-backend`
   - Scroll to bottom → Click **"Delete Service"**
   - Confirm deletion

2. **Create via Blueprint:**
   - Click **"New +"** (top right)
   - Select **"Blueprint"** (NOT "Web Service")
   - Connect your GitHub account
   - Select repository: `Likesh1235/scrapsail-backend`
   - Render will automatically read `render.yaml`
   - Click **"Apply"** or **"Create"**
   - Render will create service with correct Java settings!

3. **Add DB_PASSWORD manually:**
   - After service is created, go to Settings
   - Find "Environment Variables"
   - Click "Add Environment Variable"
   - Key: `DB_PASSWORD`
   - Value: `AVNS_q3bA1ATbxyymPpRXPIY`
   - Save

4. **Done!** It will auto-deploy with Java 17

---

## OPTION 2: Delete & Recreate Manually

If Blueprint doesn't work:

### Step 1: Delete Current Service
1. Go to Render dashboard
2. Click `scrapsail-backend` service
3. Scroll to very bottom of Settings
4. Click **"Delete Service"** (red button)
5. Type service name to confirm
6. Click Delete

### Step 2: Create New Service
1. Click **"New +"** → **"Web Service"**
2. Connect GitHub → Select `Likesh1235/scrapsail-backend`
3. Fill in these EXACT values:

**Basic Settings:**
- Name: `scrapsail-backend`
- Region: `Singapore`
- Branch: `clean-main` ⚠️ IMPORTANT!
- Root Directory: (leave empty)

**Build & Deploy Section:**
Look for these fields (they might be labeled differently):

- **"Runtime"** or **"Environment"** or **"Language"**: 
  - Select: `Java 17` or `Java`
  - ⚠️ DO NOT select "Auto-detect" or "Node"

- **"Build Command"**: 
  ```
  chmod +x ./mvnw && ./mvnw clean package -DskipTests
  ```

- **"Start Command"**: 
  ```
  java -jar target/*.jar
  ```

**Environment Variables:**
Click "Add Environment Variable" for each:

1. `SPRING_PROFILES_ACTIVE` = `prod`
2. `MYSQL_URL` = `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?sslMode=REQUIRED`
3. `DB_USERNAME` = `avnadmin`
4. `DB_PASSWORD` = `AVNS_q3bA1ATbxyymPpRXPIY`
5. `SERVER_PORT` = `8080`
6. `PORT` = `8080`

4. Click **"Create Web Service"**

---

## If You Still Don't See "Runtime" Option

The field might be named differently. Look for:
- "Environment"
- "Language" 
- "Runtime Environment"
- "Platform"
- "Stack"

OR it might be under:
- "Advanced Settings"
- "Additional Options"
- Click "Show Advanced" button

---

## Quick Help: What Do You See?

Please tell me:
1. When you click "New +" → "Web Service", what fields do you see?
2. What options are in any dropdown menus?
3. Can you take a screenshot or describe what's on your screen?

This will help me give you exact instructions!

---

## My Recommendation

**Use OPTION 1 (Blueprint)** - It's the easiest and will use the render.yaml file automatically with Java 17! ✅

