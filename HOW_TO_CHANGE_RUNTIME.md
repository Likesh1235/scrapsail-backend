# 🔧 How to Change Runtime in Render - Step by Step

## Method 1: Change Runtime in Settings (After Service is Created)

### Step-by-Step Instructions:

1. **Open Render Dashboard**
   - Go to: https://dashboard.render.com
   - Sign in to your account

2. **Open Your Service**
   - You'll see a list of your services
   - Click on **`scrapsail-backend`** (or whatever you named it)
   - This opens your service dashboard

3. **Go to Settings Tab**
   - Look at the left sidebar menu
   - You'll see tabs like: "Logs", "Metrics", "Events", "Settings", etc.
   - Click on **"Settings"** (usually near the bottom of the menu)

4. **Find the Runtime Field**
   - Scroll down the Settings page
   - Look for a section called:
     - **"Environment"** OR
     - **"Build & Deploy"** OR
     - **"Configuration"**
   
   - In that section, look for a field/dropdown that says:
     - **"Runtime"** OR
     - **"Environment"** OR
     - **"Language"** OR
     - **"Stack"** OR
     - **"Platform"**

5. **Change the Dropdown Value**
   - You'll see a dropdown menu
   - Currently it probably shows:
     - ❌ "Node" or
     - ❌ "Node.js" or
     - ❌ "Auto-detect" or
     - ❌ "Node.js 22.16.0"
   
   - Click the dropdown and select:
     - ✅ **"Java 17"** (best option)
     - OR **"Java"**
     - OR **"Java Runtime"**

6. **Save Changes**
   - Scroll to the very bottom of the Settings page
   - Click the **"Save Changes"** button (usually blue/green)
   - Wait for confirmation

7. **Redeploy**
   - Go to **"Events"** tab (left sidebar)
   - Click **"Manual Deploy"** button (top right)
   - Select **"Deploy latest commit"**
   - Click **"Deploy"**

8. **Watch Logs**
   - Go to **"Logs"** tab
   - You should now see:
     ```
     ==> Using Java 17 runtime ✅
     ```
   - Build should succeed!

---

## Method 2: Delete and Recreate Service (If Settings Don't Work)

If you CAN'T find the Runtime option in Settings:

### Delete Current Service:
1. Go to your service dashboard
2. Click **"Settings"** tab
3. Scroll to the **very bottom**
4. You'll see a red/dangerous section
5. Click **"Delete Service"** button
6. Type the service name to confirm: `scrapsail-backend`
7. Click **"Delete"** to confirm

### Recreate with Correct Runtime:
1. Click **"New +"** button (top right)
2. Select **"Web Service"**
3. Connect GitHub → Select `Likesh1235/scrapsail-backend`
4. Fill in the form:
   - **Name:** `scrapsail-backend`
   - **Region:** `Singapore`
   - **Branch:** `main`
   
5. **⚠️ IMPORTANT - Look for Runtime Dropdown During Creation:**
   - As you fill the form, look carefully for ANY dropdown
   - It might be:
     - Near the top
     - In "Environment" section
     - In "Build Settings"
   - When you find it:
     - **DO NOT** select "Auto-detect"
     - **DO NOT** select "Node"
     - **SELECT:** "Java 17" or "Java"
   
6. **Continue Configuration:**
   - Build Command: `chmod +x ./mvnw && ./mvnw clean package -DskipTests`
   - Start Command: `java -jar target/*.jar`
   - Add all 6 environment variables
   - Click **"Create Web Service"**

---

## Visual Guide: Where to Find Runtime

### In Settings Page:
```
┌─────────────────────────────────────┐
│ Settings                            │
├─────────────────────────────────────┤
│ Name: scrapsail-backend             │
│ Region: Singapore                   │
│                                     │
│ Environment                         │
│ ┌─────────────────────────────────┐ │
│ │ Runtime: [Node.js ▼]  ← CHANGE   │ │
│ │   - Java 17                      │ │
│ │   - Node.js                      │ │
│ └─────────────────────────────────┘ │
│                                     │
│ Build Command: ...                  │
│ Start Command: ...                  │
│                                     │
│ [Save Changes] ← Click this         │
└─────────────────────────────────────┘
```

---

## What Each Option Looks Like:

### Option A: Separate "Runtime" Dropdown
```
Runtime: [Node.js ▼]
        ↓ Click
        - Java 17
        - Node.js
        - Python
```

### Option B: "Environment" Dropdown
```
Environment: [Node.js ▼]
             ↓ Click
             - Java 17
             - Node.js
```

### Option C: "Language" Dropdown
```
Language: [Node.js ▼]
          ↓ Click
          - Java
          - Node.js
```

---

## Still Can't Find It?

### Try These:
1. **Check All Sections:**
   - Scroll through the ENTIRE Settings page
   - Runtime might be in a different section

2. **Look for "Advanced" or "More Options":**
   - Some settings are hidden
   - Click "Show Advanced" or "More Options"

3. **Check Service Type:**
   - Make sure it's a "Web Service" not "Static Site" or "Background Worker"
   - Runtime options only appear for Web Services

4. **Take a Screenshot:**
   - Screenshot your Settings page
   - I can help identify where the runtime option is

---

## Quick Checklist:

- [ ] Opened service dashboard
- [ ] Clicked "Settings" tab
- [ ] Scrolled through entire page
- [ ] Found Runtime/Environment dropdown
- [ ] Changed from "Node.js" to "Java 17"
- [ ] Clicked "Save Changes"
- [ ] Went to "Events" → "Manual Deploy"
- [ ] Watched logs show "Using Java 17 runtime"

---

## Need More Help?

If you still can't find it, tell me:
1. What sections you see in Settings
2. What the page looks like
3. Take a screenshot if possible

I'll guide you to the exact location! 🎯

