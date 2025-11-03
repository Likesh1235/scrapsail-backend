# ⚠️ URGENT: Fix Runtime in Render Dashboard

## The Problem
Render is detecting Node.js instead of Java, causing "JAVA_HOME not defined" error.

## ✅ IMMEDIATE FIX (Do This Now)

### Step 1: Go to Your Service Settings
1. Open: https://dashboard.render.com
2. Click on your **`scrapsail-backend`** service
3. Click **"Settings"** tab (left sidebar)

### Step 2: Change Runtime to Java
1. Scroll down to **"Environment"** or **"Build & Deploy"** section
2. Look for **"Runtime"** field (it might be labeled as):
   - "Runtime"
   - "Environment"
   - "Language"
   - "Stack"
3. **FIND THE DROPDOWN** that currently says:
   - "Node" or "Node.js" or "Auto-detect"
4. **CHANGE IT TO:**
   - **"Java 17"** or **"Java"** or **"Java Runtime"**
5. Scroll to bottom, click **"Save Changes"**

### Step 3: Redeploy
1. Go to **"Events"** tab
2. Click **"Manual Deploy"** button
3. Select **"Deploy latest commit"**
4. Wait for deployment

### Step 4: Verify
Watch the logs. You should NOW see:
```
==> Using Java 17 runtime ✅
==> Running build command...
```

---

## If You CAN'T Find Runtime Setting

### Option A: Delete and Recreate
1. **Delete Service:**
   - Settings → Scroll to bottom → "Delete Service"
   - Confirm deletion

2. **Recreate with Correct Settings:**
   - "New +" → "Web Service"
   - Connect GitHub → Select repo
   - **Branch:** `main`
   - **When creating, look carefully for ANY dropdown that mentions:**
     - "Runtime"
     - "Environment" 
     - "Language"
   - **Select Java 17 BEFORE clicking "Create"**

### Option B: Contact Render Support
If runtime option doesn't exist at all:
- Email: support@render.com
- Explain: "Service auto-detected Node.js but it's a Java/Spring Boot app"
- Request: Manual runtime change to Java 17

---

## Why This Happens
Render auto-detects runtime. Sometimes it incorrectly detects Node.js even for Java projects. The ONLY way to fix is to manually set it in Settings.

---

**DO THIS NOW - Change Runtime in Settings!**

