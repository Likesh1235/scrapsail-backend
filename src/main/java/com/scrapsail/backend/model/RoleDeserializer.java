package com.scrapsail.backend.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;

public class RoleDeserializer extends JsonDeserializer<Role> {
    
    @Override
    public Role deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        if (value == null || value.trim().isEmpty()) {
            return Role.user; // Default role
        }
        
        try {
            // Convert to lowercase and parse enum
            return Role.valueOf(value.toLowerCase());
        } catch (IllegalArgumentException e) {
            // If invalid, default to user
            return Role.user;
        }
    }
}

