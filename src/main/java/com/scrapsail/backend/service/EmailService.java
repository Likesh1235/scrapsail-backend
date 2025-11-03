package com.scrapsail.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public String sendOtpEmail(String toEmail) {
        // Generate 6-digit OTP
        String otp = generateOtp();
        
        // For development, always show OTP in console
        System.out.println("=== EMAIL SENDING ===");
        System.out.println("To: " + toEmail);
        System.out.println("Subject: ScrapSail - OTP Verification");
        System.out.println("OTP: " + otp);
        System.out.println("====================");
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("likeshkanna74@gmail.com");
            message.setTo(toEmail);
            message.setSubject("ScrapSail - OTP Verification");
            message.setText(
                "Dear User,\n\n" +
                "Your OTP for ScrapSail pickup verification is: " + otp + "\n\n" +
                "This OTP is valid for 10 minutes.\n\n" +
                "If you did not request this OTP, please ignore this email.\n\n" +
                "Best regards,\n" +
                "ScrapSail Team"
            );
            
            // Send actual email
            mailSender.send(message);
            System.out.println("✅ Email sent successfully to " + toEmail);
            
            return otp;
        } catch (Exception e) {
            System.err.println("⚠️ Email sending failed: " + e.getMessage());
            e.printStackTrace();
            System.out.println("📧 For development, returning OTP anyway: " + otp);
            // Return OTP even if email fails (for development)
            return otp;
        }
    }

    /**
     * Send a simple plain-text email. Swallows exceptions and logs, so that core
     * flows (like approval) are not blocked if email fails in development.
     */
    public void sendPlainEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("likeshkanna74@gmail.com");
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("✅ Email sent successfully to " + toEmail + " | Subject: " + subject);
        } catch (Exception e) {
            System.err.println("⚠️ Email sending failed to " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }
}
