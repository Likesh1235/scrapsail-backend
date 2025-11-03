package com.scrapsail.backend.util;

/**
 * Utility class to generate GPS navigation links
 */
public class GpsLinkGenerator {
    
    /**
     * Generate Google Maps link from coordinates
     * Format: https://www.google.com/maps?q=latitude,longitude
     * This uses GPS coordinates directly (not search), opens exact location
     */
    public static String generateFromCoordinates(Double latitude, Double longitude) {
        if (latitude != null && longitude != null) {
            // Use GPS coordinate format - opens exact location, not search
            return "https://www.google.com/maps?q=" + latitude + "," + longitude;
        }
        return null;
    }
    
    /**
     * Generate Google Maps search link from address
     * Format: https://www.google.com/maps/search/?api=1&query=address
     * This is a fallback when coordinates are not available
     */
    public static String generateFromAddress(String address) {
        if (address != null && !address.trim().isEmpty()) {
            // URL encode the address for Google Maps search
            String encodedAddress = address.trim().replace(" ", "+");
            return "https://www.google.com/maps/search/?api=1&query=" + encodedAddress;
        }
        return null;
    }
    
    /**
     * Generate GPS link with fallback to address-based link
     * Priority: coordinates > address
     */
    public static String generate(Double latitude, Double longitude, String address) {
        // First try coordinates
        String link = generateFromCoordinates(latitude, longitude);
        
        // If no coordinates, fall back to address-based search
        if (link == null) {
            link = generateFromAddress(address);
        }
        
        return link;
    }
}

