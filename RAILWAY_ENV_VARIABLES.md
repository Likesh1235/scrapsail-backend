# 🔧 Railway Environment Variables Guide

## ✅ Verified Configuration

### 1. Port Configuration ✅
**File:** `application.properties`
```properties
server.port=${PORT:8080}
```
✅ **Correct** - Uses Railway's PORT environment variable with fallback to 8080

---

## 📋 Database Connection Options

The app supports **3 ways** to connect to MySQL:

### Option 1: Aiven MySQL (Recommended for Aiven)
**Use these variables:**
```bash
SPRING_DATASOURCE_URL=jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?sslMode=REQUIRED
SPRING_DATASOURCE_USERNAME=avnadmin
SPRING_DATASOURCE_PASSWORD=AVNS_q3bA1ATbxyymPpRXPIY
```

**Priority:** Highest (used first if set)

---

### Option 2: Railway MySQL Service (Auto-configured)
**Railway automatically provides:**
```bash
DATABASE_URL=mysql://user:password@host:port/database
```
**No manual setup needed** - Just add MySQL service in Railway:
1. Railway Dashboard → + New → Database → Add MySQL
2. Railway automatically sets `DATABASE_URL`

---

### Option 3: Manual Override
**Use these variables:**
```bash
MYSQL_URL=jdbc:mysql://host:port/database?sslMode=REQUIRED
DB_USERNAME=username
DB_PASSWORD=password
```

---

## 🚀 Railway Environment Variables Setup

### Go to: Railway Dashboard → Your Service → Variables

### Required Variables (Choose ONE option):

#### **Option A: Aiven MySQL (Current Setup)**
| Key | Value | Required |
|-----|-------|----------|
| `SPRING_DATASOURCE_URL` | `jdbc:mysql://scrapsaildb-scrapsaildb.e.aivencloud.com:22902/defaultdb?sslMode=REQUIRED` | ✅ Yes |
| `SPRING_DATASOURCE_USERNAME` | `avnadmin` | ✅ Yes |
| `SPRING_DATASOURCE_PASSWORD` | `AVNS_q3bA1ATbxyymPpRXPIY` | ✅ Yes |

#### **Option B: Railway MySQL Service**
- Add MySQL service in Railway
- Railway automatically sets `DATABASE_URL`
- No manual variables needed

### Optional Variables:
| Key | Value | Purpose |
|-----|-------|---------|
| `SPRING_PROFILES_ACTIVE` | `prod` | Use production profile |
| `JAVA_TOOL_OPTIONS` | `-Xmx512M` | Limit JVM memory (prevents crashes) |
| `PORT` | *(auto-set)* | Don't override - Railway sets this |

---

## ⚠️ Important Notes

### Remove Old Variables:
If you have these old variables, **remove them**:
- ❌ `MYSQL_URL` (only if using Aiven - use SPRING_DATASOURCE_URL instead)
- ❌ `DB_USERNAME` (use SPRING_DATASOURCE_USERNAME)
- ❌ `DB_PASSWORD` (use SPRING_DATASOURCE_PASSWORD)

### Spring Boot Standard:
Spring Boot expects `SPRING_DATASOURCE_*` pattern by default. Using this ensures:
- ✅ Automatic property binding
- ✅ No configuration needed
- ✅ Works with Spring Boot auto-configuration

---

## 🧪 Verification

After setting variables, check Railway logs for:
```
✅ SPRING_DATASOURCE_URL is set (Aiven MySQL detected)
✅ Database connection configured
✅ Started BackendApplication in X.XXX seconds
```

---

## 📝 Quick Setup Checklist

### For Aiven MySQL:
- [ ] `SPRING_DATASOURCE_URL` is set
- [ ] `SPRING_DATASOURCE_USERNAME` is set
- [ ] `SPRING_DATASOURCE_PASSWORD` is set
- [ ] `JAVA_TOOL_OPTIONS=-Xmx512M` is set (optional but recommended)
- [ ] `SPRING_PROFILES_ACTIVE=prod` is set (optional)

### For Railway MySQL:
- [ ] MySQL service added in Railway
- [ ] `DATABASE_URL` appears in Variables (auto-set by Railway)
- [ ] `JAVA_TOOL_OPTIONS=-Xmx512M` is set (optional but recommended)

---

## 🔄 After Setting Variables

1. **Redeploy** in Railway
2. **Check logs** for successful startup
3. **Test health endpoint:** `https://<your-app>.up.railway.app/health`

---

**Your app is now configured correctly! 🎉**

