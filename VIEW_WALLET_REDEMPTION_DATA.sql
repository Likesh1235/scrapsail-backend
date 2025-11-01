-- ==================================================
-- Quick Queries to View Wallet & Redemption Data
-- Copy and paste these into MySQL Workbench
-- ==================================================

USE scrapsail;

-- 1. View All Wallet Data (Points, Balance, Redeemed)
SELECT 
    u.id AS 'User ID',
    u.name AS 'User Name',
    u.email AS 'Email',
    COALESCE(cw.points, 0) AS 'Wallet Points',
    COALESCE(cw.balance, 0) AS 'Wallet Balance (₹)',
    COALESCE(cw.total_redeemed, 0) AS 'Total Redeemed (₹)',
    cw.last_redeem AS 'Last Redemption Date'
FROM users u
LEFT JOIN carbon_wallet cw ON u.id = cw.user_id
ORDER BY u.id;

-- 2. View All Carbon Credits Data
SELECT 
    u.id AS 'User ID',
    u.name AS 'User Name',
    u.email AS 'Email',
    COALESCE(cc.earned, 0) AS 'Credits Earned',
    COALESCE(cc.redeemed, 0) AS 'Credits Redeemed',
    COALESCE(cc.balance, 0) AS 'Credits Balance'
FROM users u
LEFT JOIN carbon_credits cc ON u.id = cc.user_id
ORDER BY u.id;

-- 3. COMPREHENSIVE VIEW - Complete Wallet & Redemption Info
SELECT 
    u.id AS 'ID',
    u.name AS 'Name',
    u.email AS 'Email',
    u.role AS 'Role',
    u.total_points AS 'Total Points',
    u.carbon_credits AS 'Carbon Credits',
    COALESCE(cw.points, 0) AS 'Wallet Points',
    COALESCE(cw.balance, 0) AS 'Wallet Balance (₹)',
    COALESCE(cw.total_redeemed, 0) AS 'Total Redeemed (₹)',
    cw.last_redeem AS 'Last Redemption',
    COALESCE(cc.earned, 0) AS 'Credits Earned',
    COALESCE(cc.redeemed, 0) AS 'Credits Redeemed',
    COALESCE(cc.balance, 0) AS 'Credits Balance'
FROM users u
LEFT JOIN carbon_wallet cw ON u.id = cw.user_id
LEFT JOIN carbon_credits cc ON u.id = cc.user_id
ORDER BY u.id;

-- 4. Simple: View All Data from carbon_wallet table
SELECT * FROM carbon_wallet;

-- 5. Simple: View All Data from carbon_credits table
SELECT * FROM carbon_credits;

-- 6. View Users with Redemption History
SELECT 
    u.id AS 'User ID',
    u.name AS 'User Name',
    u.email AS 'Email',
    COALESCE(cw.total_redeemed, 0) AS 'Total Redeemed (₹)',
    COALESCE(cw.balance, 0) AS 'Current Balance (₹)',
    COALESCE(cw.points, 0) AS 'Current Points',
    cw.last_redeem AS 'Last Redemption Date'
FROM users u
INNER JOIN carbon_wallet cw ON u.id = cw.user_id
WHERE cw.total_redeemed > 0
ORDER BY cw.total_redeemed DESC;

