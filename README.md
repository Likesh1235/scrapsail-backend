# 🚢 ScrapSail - Smart Waste Management System

## 📋 Project Overview

ScrapSail is a full-stack waste management application with:
- **Frontend:** React.js (localhost:3000)
- **Backend:** Spring Boot (localhost:8080)
- **Database:** MySQL (localhost:3306)

---

## 🚀 Quick Start

### Prerequisites
- Java 17+
- MySQL 8.0+
- Node.js 16+
- Maven (included via `mvnw`)

---

## 🗄️ Step 1: Setup MySQL

### Install MySQL
1. Download from: https://dev.mysql.com/downloads/mysql/
2. Install and start MySQL service
3. Note your MySQL root password (or leave empty)

### Create Database (Optional)
The app will auto-create the database, but you can create it manually:

```bash
mysql -u root -p
CREATE DATABASE scrapsail;
exit
```

---

## 🔧 Step 2: Configure Backend

### Database Password
If your MySQL root password is not empty, update `application.properties`:

```properties
spring.datasource.password=${DB_PASSWORD:your_password_here}
```

Or set environment variable:
```bash
# Windows PowerShell
$env:DB_PASSWORD="your_password"

# Linux/Mac
export DB_PASSWORD="your_password"
```

---

## 🚀 Step 3: Start Backend

### Navigate to Backend
```bash
cd scrapsail-backend
```

### Start Backend Server

**Windows:**
```bash
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw spring-boot:run
```

**Or use PowerShell script (Windows):**
```bash
.\START_PROJECT.ps1
```

### Verify Backend
Open: http://localhost:8080/health

**Expected:**
```json
{
  "status": "UP",
  "message": "ScrapSail Backend is healthy and running!"
}
```

---

## 🎨 Step 4: Start Frontend

### Navigate to Frontend
```bash
cd scrapsail-frontend-new
```

### Install Dependencies (First Time)
```bash
npm install
```

### Start Frontend
```bash
npm start
```

Frontend will open automatically at: http://localhost:3000

---

## ✅ Default Login Credentials

After first startup, these users are auto-created:

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@scrapsail.com | admin123 |
| Collector | collector@scrapsail.com | collector123 |
| User | user@scrapsail.com | user123 |

---

## 📊 Application URLs

- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080
- **Health Check:** http://localhost:8080/health
- **API Info:** http://localhost:8080/

---

## 🛠️ Development

### Backend
- **Port:** 8080
- **Database:** `scrapsail` database (auto-created)
- **Schema:** Auto-updated by Hibernate
- **Hot Reload:** Requires restart (or use Spring DevTools)

### Frontend
- **Port:** 3000
- **Hot Reload:** Enabled (auto-refreshes on changes)
- **API Base URL:** http://localhost:8080

---

## 📁 Project Structure

```
ScrapSail/
├── scrapsail-backend/          # Spring Boot backend
│   ├── src/main/java/          # Java source code
│   ├── src/main/resources/     # Configuration files
│   └── pom.xml                # Maven dependencies
│
└── scrapsail-frontend-new/     # React frontend
    ├── src/                    # React source code
    ├── public/                 # Static files
    └── package.json           # Node dependencies
```

---

## 🔧 Configuration Files

### Backend
- `application.properties` - Main configuration (localhost MySQL)
- `application-local.properties` - Alternative local config
- `application-prod.properties` - Alternative config

### Frontend
- `src/api.js` - API base URL (http://localhost:8080)
- `src/config/api.js` - API endpoint configuration

---

## 🐛 Troubleshooting

### MySQL Connection Failed
- Check MySQL is running
- Verify password in `application.properties`
- Check MySQL port (default: 3306)

### Port Already in Use
- Stop other applications
- Or change port in `application.properties`

### Frontend Can't Connect
- Verify backend is running on port 8080
- Check browser console for errors
- Verify CORS settings

---

## 📝 Notes

- Database auto-creates on first run
- Tables auto-created by Hibernate
- Default users created by DataInitializer
- All deployment configurations removed - localhost only

---

**See `LOCAL_SETUP.md` for detailed setup instructions!**

