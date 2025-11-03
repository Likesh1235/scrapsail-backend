package com.scrapsail.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MainController {

    @GetMapping("/test")
    public Map<String, String> testAPI() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "✅ ScrapSail Backend Connected!");
        response.put("status", "success");
        return response;
    }
}

