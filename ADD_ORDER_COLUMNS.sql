-- ==================================================
-- Add New Columns to Existing scrap_orders Table
-- ==================================================
-- Run this if you already have scrap_orders table and want to add new columns
-- Without dropping existing data
-- ==================================================

USE scrapsail;

-- Add detailed pickup address fields
ALTER TABLE scrap_orders 
ADD COLUMN IF NOT EXISTS pickup_street_address VARCHAR(500) AFTER address,
ADD COLUMN IF NOT EXISTS pickup_city VARCHAR(255) AFTER pickup_street_address,
ADD COLUMN IF NOT EXISTS pickup_state VARCHAR(255) AFTER pickup_city,
ADD COLUMN IF NOT EXISTS pickup_zip_code VARCHAR(50) AFTER pickup_state,
ADD COLUMN IF NOT EXISTS pickup_country VARCHAR(255) AFTER pickup_zip_code,
ADD COLUMN IF NOT EXISTS pickup_landmark VARCHAR(500) AFTER pickup_country;

-- Add user contact details fields
ALTER TABLE scrap_orders 
ADD COLUMN IF NOT EXISTS user_name VARCHAR(255) AFTER user_phone,
ADD COLUMN IF NOT EXISTS user_alternate_phone VARCHAR(255) AFTER user_name;

-- Add collector details
ALTER TABLE scrap_orders 
ADD COLUMN IF NOT EXISTS collector_name VARCHAR(255) AFTER collector_email;

-- Add pickup instructions
ALTER TABLE scrap_orders 
ADD COLUMN IF NOT EXISTS pickup_instructions TEXT AFTER admin_notes;

-- Add wallet/redemption tracking fields
ALTER TABLE scrap_orders 
ADD COLUMN IF NOT EXISTS points_earned DOUBLE DEFAULT 0.0 AFTER longitude,
ADD COLUMN IF NOT EXISTS credits_awarded DOUBLE DEFAULT 0.0 AFTER points_earned,
ADD COLUMN IF NOT EXISTS user_wallet_balance_at_order DOUBLE DEFAULT 0.0 AFTER credits_awarded;

-- Add indexes for better query performance
CREATE INDEX IF NOT EXISTS idx_pickup_city ON scrap_orders(pickup_city);
CREATE INDEX IF NOT EXISTS idx_pickup_state ON scrap_orders(pickup_state);

-- Verify new columns added
SHOW COLUMNS FROM scrap_orders;

