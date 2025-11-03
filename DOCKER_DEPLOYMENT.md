# 🐳 Docker Deployment on Render - Step by Step

Since Render doesn't have native Java support, we use Docker instead. This is actually better!

---

## ✅ STEP 1: Configure Language

In the Render form where you see the **Language** dropdown:

1. Click the **Language** dropdown (currently shows "Node")
2. Select **"Docker"** from the list ✅
   - Options you'll see: Node, **Docker**, Elixir, Go, Python 3, Ruby, Rust
   - **Select: Docker**

---

## ✅ STEP 2: Configure Other Settings

After selecting "Docker", the form will update. Configure:

### Basic Settings:
- **Name:** `scrapsail-backend`
- **Region:** `Singapore`
- **Branch:** `main`
- **Root Directory:** (leave empty)

### Build & Start Commands:
**IMPORTANT:** When you select "Docker", these fields might disappear or change. That's OK!

- **Build Command:** (Docker handles this automatically - you can leave empty or remove `$ yarn`)
- **Start Command:** (Docker handles this automatically - you can leave empty or remove `$ yarn start`)

**If the fields are still there:**
- Clear them or leave empty
- Docker will use the Dockerfile automatically

---

## ✅ STEP 3: Add Environment Variables

Add these 6 environment variables (same as before):

1. `SPRING_PROFILES_ACTIVE` = `prod`
2. `MYSQL_URL` = `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?sslMode=REQUIRED`
3. `DB_USERNAME` = `avnadmin`
4. `DB_PASSWORD` = `AVNS_q3bA1ATbxyymPpRXPIY`
5. `SERVER_PORT` = `8080`
6. `PORT` = `8080`

---

## ✅ STEP 4: Create Service

1. Enable **Auto-Deploy** (if checkbox is visible)
2. Click **"Create Web Service"**
3. Wait for deployment (10-15 minutes first time - Docker needs to build)

---

## ✅ STEP 5: Watch Deployment

Watch the logs. You should see:

```
==> Cloning repository...
==> Building Docker image...
==> Using Dockerfile: Dockerfile
==> Building with Docker...
==> Step 1/7 : FROM openjdk:17-jdk-slim
==> Step 2/7 : WORKDIR /app
...
==> Build completed successfully ✅
==> Starting container...
==> Started BackendApplication ✅
```

---

## 🎯 Why Docker Works

1. **Java 17 included** - Dockerfile uses `openjdk:17-jdk-slim`
2. **Full control** - We control the entire environment
3. **Consistent** - Works the same everywhere
4. **No runtime issues** - Docker handles Java for us

---

## ✅ What We Created

- ✅ `Dockerfile` - Builds and runs your Spring Boot app with Java 17
- ✅ `.dockerignore` - Excludes unnecessary files from Docker build
- ✅ Updated `render.yaml` - Set to use Docker

---

## 🚀 Next Steps

1. **Select "Docker"** in Language dropdown
2. **Clear Build/Start commands** (or leave empty)
3. **Add all 6 environment variables**
4. **Click "Create Web Service"**
5. **Wait for Docker build to complete**

---

**This WILL work! Docker gives us Java 17 support that Render doesn't have natively.** ✅

