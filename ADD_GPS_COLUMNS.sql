-- Add GPS coordinates and GPS link columns to existing scrap_orders table
-- Run this if you already have the scrap_orders table created

USE scrapsail;

-- Check if columns exist, and add them if they don't
-- MySQL doesn't support IF NOT EXISTS in ALTER TABLE, so we need to check first
-- This script adds the columns - if they already exist, you'll get an error (which you can ignore)

-- Add latitude column
ALTER TABLE scrap_orders 
ADD COLUMN latitude DOUBLE NULL AFTER user_email;

-- Add longitude column  
ALTER TABLE scrap_orders 
ADD COLUMN longitude DOUBLE NULL AFTER latitude;

-- Add GPS link column
ALTER TABLE scrap_orders 
ADD COLUMN gps_link VARCHAR(500) NULL AFTER longitude;

-- Verify columns were added
DESCRIBE scrap_orders;

-- Alternative: If the above fails because columns exist, run this instead:
-- ALTER TABLE scrap_orders MODIFY COLUMN latitude DOUBLE NULL;
-- ALTER TABLE scrap_orders MODIFY COLUMN longitude DOUBLE NULL;
-- ALTER TABLE scrap_orders MODIFY COLUMN gps_link VARCHAR(500) NULL;

