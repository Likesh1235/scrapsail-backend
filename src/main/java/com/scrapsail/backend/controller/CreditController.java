package com.scrapsail.backend.controller;

import com.scrapsail.backend.model.CarbonCredit;
import com.scrapsail.backend.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/credits")
public class CreditController {

    @Autowired
    private CreditService creditService;

    @GetMapping("/balance/{userId}")
    public ResponseEntity<Map<String, Object>> getUserBalance(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Double balance = creditService.getUserBalance(userId);
            response.put("success", true);
            response.put("userId", userId);
            response.put("balance", balance);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving balance: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/earn")
    public ResponseEntity<Map<String, Object>> earnCredits(@RequestBody Map<String, Object> earnRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long userId = Long.valueOf(earnRequest.get("userId").toString());
            Double amount = Double.valueOf(earnRequest.get("amount").toString());
            
            if (amount <= 0) {
                response.put("success", false);
                response.put("message", "Amount must be greater than 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            CarbonCredit carbonCredit = creditService.earnCredits(userId, amount);
            response.put("success", true);
            response.put("message", "Credits earned successfully");
            response.put("carbonCredit", carbonCredit);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error earning credits: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/redeem")
    public ResponseEntity<Map<String, Object>> redeemCredits(@RequestBody Map<String, Object> redeemRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            Long userId = Long.valueOf(redeemRequest.get("userId").toString());
            Double amount = Double.valueOf(redeemRequest.get("amount").toString());
            
            if (amount <= 0) {
                response.put("success", false);
                response.put("message", "Amount must be greater than 0");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            CarbonCredit carbonCredit = creditService.redeemCredits(userId, amount);
            response.put("success", true);
            response.put("message", "Successfully redeemed ₹" + amount + "! Amount credited to your account.");
            response.put("redeemedAmount", amount);
            response.put("carbonCredit", carbonCredit);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error redeeming credits: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserCredits(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<CarbonCredit> credits = creditService.getUserCredits(userId);
            response.put("success", true);
            response.put("userId", userId);
            response.put("credits", credits);
            response.put("count", credits.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error retrieving credits: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
