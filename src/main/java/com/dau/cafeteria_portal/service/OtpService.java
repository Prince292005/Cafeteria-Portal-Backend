package com.dau.cafeteria_portal.service;

public interface OtpService {
    String generateOtp(String email);
    void sendOtpEmail(String toEmail, String otp);
    boolean verifyOtp(String email, String otp);
}
