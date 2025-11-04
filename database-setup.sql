-- ==================================================
-- ScrapSail Complete MySQL Setup - Single Query
-- ==================================================
-- Copy this ENTIRE query and run in MySQL Workbench
-- This creates all tables with correct schema matching Java models
-- Includes GPS coordinates, GPS link, leaderboard, and pickup_requests tables
-- ==================================================

CREATE DATABASE IF NOT EXISTS scrapsail;

USE scrapsail;

-- Disable foreign key checks temporarily to drop all tables
SET FOREIGN_KEY_CHECKS = 0;

-- Drop all existing tables
DROP TABLE IF EXISTS leaderboard;
DROP TABLE IF EXISTS pickup_requests;
DROP TABLE IF EXISTS scrap_orders;
DROP TABLE IF EXISTS carbon_wallet;
DROP TABLE IF EXISTS users;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- Create Users Table
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('user', 'collector', 'admin') DEFAULT 'user',
    phone VARCHAR(255),
    address TEXT,
    total_points INT DEFAULT 0,
    total_recycled DECIMAL(10,2) DEFAULT 0.0,
    account_status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE',
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create Scrap Orders Table (includes GPS coordinates and GPS link)
CREATE TABLE scrap_orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_order_number INT DEFAULT NULL COMMENT 'Per-user order number (starts from 1 for each user)',
    item_type VARCHAR(255),
    weight DOUBLE,
    status VARCHAR(255),
    address TEXT,
    user_email VARCHAR(255),
    latitude DOUBLE,
    longitude DOUBLE,
    gps_link VARCHAR(500),
    created_at TIMESTAMP NULL,
    approved_at TIMESTAMP NULL,
    assigned_at TIMESTAMP NULL,
    user_id BIGINT,
    collector_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (collector_id) REFERENCES users(id) ON DELETE SET NULL,
    INDEX idx_user_id (user_id),
    INDEX idx_collector_id (collector_id),
    INDEX idx_status (status),
    INDEX idx_user_order_number (user_id, user_order_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create Carbon Wallet Table
CREATE TABLE carbon_wallet (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    points DOUBLE DEFAULT 0.0,
    balance DOUBLE DEFAULT 0.0,
    total_redeemed DOUBLE DEFAULT 0.0,
    user_id BIGINT NOT NULL,
    last_redeem TIMESTAMP NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_wallet (user_id),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create Pickup Requests Table
CREATE TABLE pickup_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    waste_type VARCHAR(255),
    weight_kg DOUBLE,
    pickup_date DATE,
    status VARCHAR(255),
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Create Leaderboard Table
CREATE TABLE leaderboard (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    rank_position INT NOT NULL,
    points INT DEFAULT 0,
    total_recycled DECIMAL(10,2) DEFAULT 0.0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_leaderboard (user_id),
    INDEX idx_rank_position (rank_position),
    INDEX idx_points (points),
    INDEX idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Verify tables created
SHOW TABLES;

-- ==================================================
-- VIEW DATA FROM ALL TABLES
-- ==================================================

-- View all users
SELECT * FROM users ORDER BY created_at DESC;

-- View all scrap orders (with GPS coordinates and GPS link)
SELECT * FROM scrap_orders ORDER BY created_at DESC;

-- View all carbon wallet data
SELECT * FROM carbon_wallet ORDER BY points DESC;

-- View all pickup requests
SELECT * FROM pickup_requests ORDER BY pickup_date DESC;

-- View all leaderboard entries
SELECT * FROM leaderboard ORDER BY rank_position ASC;

