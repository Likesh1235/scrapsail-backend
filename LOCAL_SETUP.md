# 🏠 Local Development Setup Guide

## 📋 Prerequisites

1. **Java 17** - Install from https://adoptium.net/
2. **MySQL** - Install from https://dev.mysql.com/downloads/mysql/
3. **Node.js** - Install from https://nodejs.org/ (for frontend)
4. **Maven** - Comes with `mvnw` wrapper (included)

---

## 🗄️ Step 1: Setup MySQL Database

### Install MySQL

1. Download and install MySQL from https://dev.mysql.com/downloads/mysql/
2. During installation, set root password (or leave empty)
3. Start MySQL service

### Create Database

**Option A: Using MySQL Command Line**
```bash
mysql -u root -p
CREATE DATABASE scrapsail;
exit
```

**Option B: Using MySQL Workbench**
- Open MySQL Workbench
- Connect to localhost
- Create new schema named `scrapsail`

**Option C: Auto-create (Recommended)**
- The app will auto-create the database if it doesn't exist
- Just make sure MySQL is running

---

## 🔧 Step 2: Configure Backend

### Database Configuration

The backend is already configured for local MySQL in `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/scrapsail?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=  # Set your MySQL root password here if needed
```

**If your MySQL password is not empty:**

1. Open: `scrapsail-backend/src/main/resources/application.properties`
2. Update line 7:
   ```properties
   spring.datasource.password=${DB_PASSWORD:your_password_here}
   ```

**Or set environment variable:**
```bash
# Windows PowerShell
$env:DB_PASSWORD="your_password"

# Linux/Mac
export DB_PASSWORD="your_password"
```

---

## 🚀 Step 3: Start Backend

### Navigate to Backend Directory
```bash
cd scrapsail-backend
```

### Start Backend Server

**Option A: Using Maven Wrapper (Recommended)**
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux/Mac
./mvnw spring-boot:run
```

**Option B: Build and Run JAR**
```bash
# Build
.\mvnw.cmd clean package -DskipTests

# Run
java -jar target/scrapsail-backend-0.0.1-SNAPSHOT.jar
```

**Option C: Using PowerShell Script (Windows)**
```bash
.\START_PROJECT.ps1
```

### Verify Backend is Running

Open browser: http://localhost:8080/health

**Expected Response:**
```json
{
  "status": "UP",
  "timestamp": 1234567890,
  "message": "ScrapSail Backend is healthy and running!"
}
```

---

## 🎨 Step 4: Start Frontend

### Navigate to Frontend Directory
```bash
cd scrapsail-frontend-new
```

### Install Dependencies (First Time Only)
```bash
npm install
```

### Start Frontend Development Server
```bash
npm start
```

### Verify Frontend is Running

Open browser: http://localhost:3000

The React app should load automatically.

---

## ✅ Step 5: Verify Everything Works

### Test Backend Endpoints

1. **Health Check:**
   - http://localhost:8080/health

2. **API Info:**
   - http://localhost:8080/

3. **Test Database:**
   - http://localhost:8080/test-db

### Test Frontend

1. Open http://localhost:3000
2. Try to login or register
3. Check browser console for API calls

---

## 🔧 Configuration Summary

### Backend Configuration
- **Port:** 8080
- **Database:** localhost:3306/scrapsail
- **Auto-create DB:** Yes (if doesn't exist)
- **Schema Update:** Auto (ddl-auto=update)

### Frontend Configuration
- **Port:** 3000 (default React)
- **API URL:** http://localhost:8080
- **Auto-opens browser:** Yes

---

## 🐛 Troubleshooting

### Issue 1: MySQL Connection Failed

**Error:** `Communications link failure`

**Fixes:**
1. Check MySQL is running:
   ```bash
   # Windows
   services.msc → MySQL
   
   # Linux/Mac
   sudo systemctl status mysql
   ```

2. Verify MySQL credentials in `application.properties`

3. Check MySQL port (default: 3306)

### Issue 2: Port Already in Use

**Error:** `Port 8080 is already in use`

**Fixes:**
1. Stop other applications using port 8080
2. Or change port in `application.properties`:
   ```properties
   server.port=8081
   ```
3. Update frontend API URL to match new port

### Issue 3: Frontend Can't Connect to Backend

**Error:** `Network Error` or `CORS Error`

**Fixes:**
1. Verify backend is running on port 8080
2. Check `src/api.js` has correct URL:
   ```javascript
   const API_BASE_URL = "http://localhost:8080";
   ```
3. Check browser console for specific errors

### Issue 4: Database Schema Not Created

**Error:** Tables not found

**Fixes:**
1. Check `application.properties` has:
   ```properties
   spring.jpa.hibernate.ddl-auto=update
   ```
2. Restart backend - it will auto-create tables
3. Check MySQL logs for errors

---

## 📊 Default Users Created

After first startup, these users are auto-created:

| Email | Password | Role |
|-------|----------|------|
| admin@scrapsail.com | admin123 | admin |
| collector@scrapsail.com | collector123 | collector |
| user@scrapsail.com | user123 | user |

---

## 🎯 Quick Start Commands

### Terminal 1: Backend
```bash
cd scrapsail-backend
.\mvnw.cmd spring-boot:run
```

### Terminal 2: Frontend
```bash
cd scrapsail-frontend-new
npm start
```

---

## 📝 Notes

- **Database:** Auto-created on first run if doesn't exist
- **Tables:** Auto-created by Hibernate on first run
- **Users:** Default users created by DataInitializer
- **Hot Reload:** Frontend has hot reload enabled
- **Backend Reload:** Requires restart (or use Spring DevTools)

---

**You're all set! Both frontend and backend should now be running on localhost! 🎉**

