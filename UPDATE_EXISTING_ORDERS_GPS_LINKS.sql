-- Update existing orders with GPS links generated from address
-- This script updates orders that don't have GPS links but have addresses
-- Run this after adding the gps_link column to the database

USE scrapsail;

-- Update orders with GPS links based on address (for orders without coordinates)
-- Note: This creates a Google Maps search link from the address text
UPDATE scrap_orders
SET gps_link = CONCAT('https://www.google.com/maps/search/?api=1&query=', REPLACE(TRIM(address), ' ', '+'))
WHERE (gps_link IS NULL OR gps_link = '')
  AND (address IS NOT NULL AND address != '' AND address != 'Not provided');

-- For orders with coordinates but no GPS link, generate coordinate-based link
UPDATE scrap_orders
SET gps_link = CONCAT('https://www.google.com/maps?q=', latitude, ',', longitude)
WHERE (gps_link IS NULL OR gps_link = '')
  AND latitude IS NOT NULL
  AND longitude IS NOT NULL;

-- Verify updates
SELECT id, address, latitude, longitude, gps_link 
FROM scrap_orders 
ORDER BY id;

