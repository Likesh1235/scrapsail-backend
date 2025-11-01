# 🚀 Complete Project Run Guide - ScrapSail

## 📋 Step-by-Step Instructions

### Step 1: Setup Database (First Time Only)

1. **Open MySQL Workbench**
2. **Run the complete setup SQL:**
   - Open file: `COMPLETE_MYSQL_SETUP.sql`
   - Copy **ENTIRE** file content
   - Paste and execute in MySQL Workbench
   - ✅ You should see: Tables created, and initial data queries

**Or manually run:**
```sql
USE scrapsail;

-- View all users
SELECT id, name, email, role, carbon_credits, total_points, created_at 
FROM users 
ORDER BY created_at DESC;

-- View all orders
SELECT id, user_email, item_type, weight, status, created_at 
FROM scrap_orders 
ORDER BY created_at DESC;
```

---

### Step 2: Start Full Project (Easiest Method)

**Option A: Use PowerShell Script (RECOMMENDED)**
```powershell
cd C:\Users\likes\ScrapSail\scrapsail-backend
.\START_PROJECT.ps1
```

This script will:
- ✅ Check MySQL is running
- ✅ Clear ports 8080 and 3000 if in use
- ✅ Start backend server
- ✅ Wait for backend to be ready
- ✅ Configure frontend .env
- ✅ Start frontend server

**Two PowerShell windows will open:**
- **Window 1**: Backend (Spring Boot) - Port 8080
- **Window 2**: Frontend (React) - Port 3000

**Wait for:**
- Backend: `Started ScrapSailApplication in X.XXX seconds`
- Frontend: `Compiled successfully!` and browser opens automatically

---

### Step 3: Verify Backend is Running

**Test in browser:**
```
http://localhost:8080/health
```

**Should return:** `{"status":"UP"}`

**Or in PowerShell:**
```powershell
Invoke-WebRequest -Uri http://localhost:8080/health -UseBasicParsing
```

---

### Step 4: Test the Application

#### A. Register New User

1. **Open Frontend:** http://localhost:3000
2. **Click "Register" or go to Registration page**
3. **Fill in details:**
   - Name: Test User
   - Email: test@example.com (any email)
   - Password: test123
   - Phone: 1234567890
4. **Click Register**
5. ✅ Should see: "Registration successful"

#### B. Create Pickup Request

1. **Login with your new user** (or use default: user@scrapsail.com / user123)
2. **Go to "Request Pickup" or "Create Order"**
3. **Fill in pickup details:**
   - Waste Type: Plastic
   - Weight: 5.5 kg
   - Address: Your address
   - (Optional) GPS coordinates
4. **Submit Request**
5. ✅ Should see: "Pickup request submitted successfully"

#### C. Admin Approve Order

1. **Login as Admin:**
   - Email: `admin@scrapsail.com`
   - Password: `admin123`
2. **Go to Admin Dashboard**
3. **Find your pickup request**
4. **Click "Accept"** and assign collector
5. ✅ Order status changes to "APPROVED"
6. ✅ User receives credits automatically

---

### Step 5: Check Data in Database

**Open MySQL Workbench and run:**

```sql
USE scrapsail;

-- 1. View ALL users (including newly registered)
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

-- 2. View ALL orders (including pickup requests)
SELECT 
    id, 
    user_email, 
    item_type, 
    weight, 
    status, 
    created_at 
FROM scrap_orders 
ORDER BY created_at DESC;

-- 3. View user wallets and credits
SELECT 
    u.name AS 'User',
    u.email AS 'Email',
    w.points AS 'Wallet Points',
    w.balance AS 'Wallet Balance',
    c.balance AS 'Carbon Credits'
FROM users u
LEFT JOIN carbon_wallet w ON u.id = w.user_id
LEFT JOIN carbon_credits c ON u.id = c.user_id
ORDER BY u.created_at DESC;

-- 4. Detailed user view with order counts
SELECT 
    id AS 'ID',
    name AS 'Name',
    email AS 'Email',
    role AS 'Role',
    phone AS 'Phone',
    carbon_credits AS 'Credits',
    total_points AS 'Points',
    total_recycled AS 'Recycled (kg)',
    account_status AS 'Status',
    CASE WHEN is_verified = 1 THEN 'Yes' ELSE 'No' END AS 'Verified',
    created_at AS 'Registered On',
    (SELECT COUNT(*) FROM scrap_orders WHERE user_id = users.id) AS 'Orders',
    (SELECT COALESCE(SUM(weight), 0) FROM scrap_orders WHERE user_id = users.id) AS 'Total Weight (kg)'
FROM users
ORDER BY created_at DESC;
```

---

### Step 6: Verify Data Flow

After you register a user and create a pickup request, you should see:

**In `users` table:**
- ✅ New user row with your registration details
- ✅ `role` = 'user'
- ✅ `carbon_credits` = 0 (initially)
- ✅ `total_points` = 0 (initially)

**In `scrap_orders` table:**
- ✅ New order row with your pickup request
- ✅ `status` = 'PENDING_APPROVAL'
- ✅ `user_email` = your registered email

**After admin approves:**
- ✅ `scrap_orders.status` = 'APPROVED'
- ✅ `users.carbon_credits` = increased (1 credit per kg)
- ✅ `users.total_points` = increased
- ✅ `carbon_wallet.points` = increased
- ✅ `carbon_credits.balance` = increased

---

## 🔍 Troubleshooting

### Backend Not Starting?

**Check:**
1. MySQL is running: `Start-Service MySQL80`
2. Port 8080 is free: `netstat -ano | findstr ":8080"`
3. Database exists: Run `USE scrapsail;` in MySQL

### Frontend Can't Connect?

**Check:**
1. Backend is running: http://localhost:8080/health
2. Frontend .env has: `REACT_APP_API_BASE_URL=http://localhost:8080`
3. Both servers are on correct ports (Backend: 8080, Frontend: 3000)

### No Data in Database?

**Verify:**
1. Backend console shows: `✅ Default admin account created`
2. Registration was successful in frontend
3. Run the SQL queries in Step 5 to check
4. Backend logs show: `Hibernate: insert into users...`

---

## 📊 Quick Test Checklist

- [ ] Database setup SQL executed successfully
- [ ] Backend starts without errors
- [ ] Backend health check: http://localhost:8080/health returns `{"status":"UP"}`
- [ ] Frontend opens at http://localhost:3000
- [ ] Can register new user
- [ ] Can create pickup request
- [ ] Admin can approve order
- [ ] Data appears in database queries
- [ ] Credits are awarded after approval
- [ ] Leaderboard shows user points

---

## 🎯 Default Login Credentials

**Admin:**
- Email: `admin@scrapsail.com`
- Password: `admin123`

**Collector:**
- Email: `collector@scrapsail.com`
- Password: `collector123`

**Test User:**
- Email: `user@scrapsail.com`
- Password: `user123`

---

## ✅ Success Indicators

You'll know everything is working when:

1. ✅ Backend console shows: `Started ScrapSailApplication in X seconds`
2. ✅ Frontend opens in browser automatically
3. ✅ Can register and login
4. ✅ Can create pickup requests
5. ✅ Admin dashboard shows pending orders
6. ✅ Database queries show all your data
7. ✅ Credits update after admin approval
8. ✅ Leaderboard shows user points

---

## 🚀 Next Steps

Once everything is running:
1. Register multiple users
2. Create multiple pickup requests
3. Test admin approval workflow
4. Test collector assignment
5. Verify leaderboard updates
6. Check all data in MySQL Workbench

**Happy Testing! 🎉**

