# ✅ Permanent Full-Stack Setup Guide

This guide ensures your ScrapSail project is permanently configured and stable.

## 📁 Step 1: Folder Structure

**Current Structure:**
```
ScrapSail/
 ├── scrapsail-backend/
 └── scrapsail-frontend-new/
```

**Recommended (Optional Rename):**
```
ScrapSail/
 ├── backend/
 └── frontend/
```

⚠️ **Note:** Rename folders manually if desired. The VS Code tasks.json is configured for `backend/` and `frontend/`, but will work with current names if you update the paths.

## ⚙️ Step 2: Backend Configuration

### ✅ application.properties
**Location:** `src/main/resources/application.properties`

Already configured with:
- `server.port=8080`
- MySQL connection
- JPA/Hibernate settings
- `spring.main.web-application-type=servlet`

### ✅ CORS Configuration
**Location:** `src/main/java/com/scrapsail/backend/config/CorsConfig.java`

Permanent CORS rules configured:
- **Allowed Origins:** 
  - HTTP: `http://localhost:3000`, `http://localhost:3001`, `http://localhost:3006`, `http://localhost:5173`
  - HTTPS: `https://localhost:3000`, `https://localhost:3001`, `https://localhost:3006`, `https://localhost:5173`
- **Allowed Methods:** All (GET, POST, PUT, DELETE, OPTIONS, PATCH, HEAD)
- **Allowed Headers:** All (*)
- **Credentials:** Enabled
- **Max Age:** 3600 seconds (1 hour)

✅ **Global CORS applies to ALL endpoints - no individual controller annotations needed!**
✅ **Supports both HTTP and HTTPS for localhost development**
✅ **No need to edit CORS again - permanently configured!**

## 💻 Step 3: Frontend Configuration

### ✅ .env File
**Location:** `scrapsail-frontend-new/.env`

```
# Backend API Configuration
REACT_APP_API_BASE_URL=http://localhost:8080
REACT_APP_API_URL=http://localhost:8080

# Frontend Port Configuration (Always use port 3000)
PORT=3000

# Enable HTTPS (prevents security warnings)
HTTPS=true
```

### ✅ Centralized API Config
**Location:** `scrapsail-frontend-new/src/api.js`

```javascript
const API = process.env.REACT_APP_API_BASE_URL || "http://localhost:8080";
export default API;
```

**Additional Config:** `scrapsail-frontend-new/src/config/api.js`
- Unified API configuration
- Supports multiple environment variables for compatibility
- All components use consistent API URL

**Usage in components:**
```javascript
import API from '../api';
// or
import API_CONFIG from '../config/api';
fetch(`${API}/api/auth/login`, {...})
```

## 🗄️ Step 4: MySQL Setup

1. Open MySQL Workbench
2. Create database:
   ```sql
   CREATE DATABASE scrapsail;
   ```
3. Grant privileges:
   ```sql
   GRANT ALL PRIVILEGES ON scrapsail.* TO 'root'@'localhost';
   FLUSH PRIVILEGES;
   ```

✅ Backend will auto-connect on startup.

## 🚀 Step 5: Starting the Application

### Option 1: One Command (Recommended - Easiest)

**From frontend directory:**
```bash
cd scrapsail-frontend-new
npm run start-both
```

This unified script will:
1. ✅ Start the Spring Boot backend (port 8080)
2. ✅ Wait for backend to be ready (health check)
3. ✅ Start the React frontend (port 3000)

**No need for multiple terminals!** Everything starts together.

### Option 2: VS Code Tasks

**Location:** `.vscode/tasks.json`

Created with three tasks:
- **Start Backend** - Starts Spring Boot on port 8080
- **Start Frontend** - Starts React on port 3000
- **Start Both** - Starts both servers

**Usage:**
1. Open VS Code Command Palette: `Ctrl+Shift+P`
2. Type: `Tasks: Run Task`
3. Select: `Start Backend` or `Start Frontend`
4. Or select `Start Both` to start everything

✅ Both servers start automatically in separate terminals!

### Option 3: PowerShell Script

**From backend directory:**
```powershell
.\START_PROJECT.ps1
```

This script:
- ✅ Checks and starts MySQL
- ✅ Clears ports 8080 and 3000 if in use
- ✅ Starts backend and waits for it to be ready
- ✅ Configures frontend .env automatically
- ✅ Starts frontend on port 3000

## 🔄 Step 6: Manual Startup (Alternative - Two Terminals)

If you prefer to control each server separately:

### Terminal 1 - Backend:
```powershell
cd scrapsail-backend
mvn clean install
mvn spring-boot:run
```
**Wait for:** `Started BackendApplication in X.XXX seconds`

### Terminal 2 - Frontend:
```powershell
cd scrapsail-frontend-new
npm start
```

**Important:** Always start backend first, then frontend!

---

## 📚 Additional Documentation

- **Quick Start Guide:** See `scrapsail-frontend-new/START_GUIDE.md` for detailed startup instructions
- **Connection Troubleshooting:** All connection issues have been resolved - see fixes in `src/config/api.js`

## ✅ Verification

### Test Backend:
- **URL:** http://localhost:8080/health
- **Expected:** `{"status":"UP","message":"ScrapSail Backend is healthy and running!"}`
- **PowerShell Test:**
  ```powershell
  Invoke-WebRequest -Uri http://localhost:8080/health -UseBasicParsing
  ```

### Test Frontend:
- **HTTP:** http://localhost:3000
- **HTTPS:** https://localhost:3000 (if HTTPS enabled)
- Should open automatically when started
- **Note:** First time with HTTPS, you'll need to accept the self-signed certificate

### Test Connection:
1. Open frontend login page
2. Open Browser DevTools → Network tab
3. Try logging in
4. Verify requests go to `http://localhost:8080/api/...`
5. Check for 200 OK responses
6. **No CORS errors should appear!**

## 📋 Default Credentials

- **Admin:** `admin@scrapsail.com` / `admin123`
- **Collector:** `collector@scrapsail.com` / `collector123`
- **User:** Register new account

## 🎯 Summary

✅ **Backend:** Port 8080 (locked)  
✅ **Frontend:** Port 3000 (locked)  
✅ **MySQL:** Port 3306 (auto-connect)  
✅ **CORS:** Permanent rules configured (HTTP + HTTPS support)  
✅ **API Config:** Centralized and unified across all files  
✅ **Startup Options:** 
   - `npm run start-both` (easiest - one command)
   - VS Code Tasks (one-click)
   - PowerShell script (`START_PROJECT.ps1`)
   - Manual (two terminals)
✅ **HTTPS:** Enabled for frontend (prevents security warnings)  
✅ **Connection Issues:** All resolved and tested  

## 🔧 Troubleshooting

### Backend Won't Start
1. ✅ Check if MySQL is running: `Start-Service MySQL80`
2. ✅ Verify database credentials in `application.properties`
3. ✅ Check if port 8080 is available: `netstat -ano | findstr :8080`

### Frontend Can't Connect
1. ✅ **Always start backend first!**
2. ✅ Verify backend is running: Check http://localhost:8080/health
3. ✅ Check `.env` file has correct `REACT_APP_API_BASE_URL=http://localhost:8080`
4. ✅ Clear browser cache and restart frontend

### Port Conflicts
```powershell
# Check what's using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID with actual process ID)
taskkill /F /PID <process_id>

# Same for port 3000
netstat -ano | findstr :3000
taskkill /F /PID <process_id>
```

**Your environment is now permanently stable and all connection errors are resolved!** 🎉

