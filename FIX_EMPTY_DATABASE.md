# Fix Empty Database - Step by Step Solution

## Problem
Database tables exist but are empty (all NULL values).

## Root Cause
The backend hasn't been started yet, so:
- `DataInitializer` hasn't run to create default users
- No users have registered yet

## ✅ Solution Steps

### Step 1: Ensure MySQL is Running

```powershell
# Check if MySQL is running
Start-Service MySQL80

# Or check status
Get-Service MySQL80
```

### Step 2: Verify Database Exists

Open MySQL Workbench and run:

```sql
USE scrapsail;
SHOW TABLES;
```

You should see:
- `users`
- `scrap_orders`
- `carbon_wallet`
- `carbon_credits`

### Step 3: Start Backend Server

**Option A: Use PowerShell Script (Easiest)**
```powershell
cd C:\Users\likes\ScrapSail\scrapsail-backend
.\START_PROJECT.ps1
```

**Option B: Manual Start**
```powershell
mvn spring-boot:run
```

### Step 4: Check Backend Console

When backend starts, you should see these messages:

```
✅ Default admin account created: admin@scrapsail.com / admin123
✅ Default collector account created: collector@scrapsail.com / collector123
✅ Default user account created: user@scrapsail.com / user123
```

**If you DON'T see these messages**, DataInitializer didn't run. Check:
- Is `@Component` enabled in `DataInitializer.java`? (It should be)
- Are there any errors in the console?

### Step 5: Test Database Connection

Open in browser or use PowerShell:

```
http://localhost:8080/test-db
```

**Expected Response:**
```json
{
  "success": true,
  "userCount": 3,
  "adminExists": true,
  "collectorExists": true,
  "status": "✅ Data found in database"
}
```

### Step 6: Verify in MySQL Workbench

Run this query:

```sql
USE scrapsail;

SELECT 
    id, 
    name, 
    email, 
    role, 
    carbon_credits, 
    total_points, 
    created_at 
FROM users 
ORDER BY created_at DESC;
```

**You should now see 3 users:**
- admin@scrapsail.com
- collector@scrapsail.com  
- user@scrapsail.com

### Step 7: Register New User via Frontend

1. Open frontend: http://localhost:3000
2. Click "Register"
3. Fill in details and register
4. Check database again - you should see the new user!

## 🔍 Troubleshooting

### If DataInitializer Still Doesn't Run:

1. **Check DataInitializer.java:**
   - Make sure `@Component` annotation is present (not commented out)
   - File: `src/main/java/com/scrapsail/backend/util/DataInitializer.java`

2. **Check for Errors:**
   - Look in backend console for SQL errors
   - Check for connection errors
   - Verify MySQL credentials in `application.properties`

3. **Manual Database Test:**
   ```sql
   USE scrapsail;
   INSERT INTO users (name, email, password, role) 
   VALUES ('Test User', 'test@test.com', 'password123', 'user');
   
   SELECT * FROM users;
   ```

4. **Check Backend Logs:**
   - Look for: `Hibernate: insert into users...`
   - Look for: `✅ Default admin account created`
   - Look for any ERROR or EXCEPTION messages

### If Still Empty After Backend Start:

1. **Check Database Connection:**
   - Verify MySQL is running on port 3306
   - Test connection: `mysql -u root -p` (password: Likesh@2006)

2. **Check application.properties:**
   ```
   spring.datasource.url=jdbc:mysql://localhost:3306/scrapsail
   spring.datasource.username=root
   spring.datasource.password=Likesh@2006
   ```

3. **Restart Backend:**
   - Stop backend (Ctrl+C)
   - Wait 5 seconds
   - Start again: `mvn spring-boot:run`

## ✅ Success Indicators

You'll know it's working when:

1. ✅ Backend console shows: `✅ Default admin account created`
2. ✅ http://localhost:8080/test-db shows `userCount: 3`
3. ✅ MySQL query shows 3 users in `users` table
4. ✅ You can login with: admin@scrapsail.com / admin123

## 🎯 Quick Fix Command

```powershell
# Complete restart sequence
cd C:\Users\likes\ScrapSail\scrapsail-backend
.\START_PROJECT.ps1
```

Then check:
- Backend console for "✅ Default admin account created"
- Browser: http://localhost:8080/test-db
- MySQL: `SELECT * FROM users;`

**The data WILL save once the backend is running!** 🚀

