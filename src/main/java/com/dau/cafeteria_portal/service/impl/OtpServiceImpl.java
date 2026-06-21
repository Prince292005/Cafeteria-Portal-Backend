package com.dau.cafeteria_portal.service.impl;

import com.dau.cafeteria_portal.entity.OtpVerification;
import com.dau.cafeteria_portal.repository.OtpVerificationRepository;
import com.dau.cafeteria_portal.service.OtpService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.client.RestTemplate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final JavaMailSender mailSender;
    private final OtpVerificationRepository otpRepo;

    @Value("${brevo.api.key}")
    private String brevoApiKey;

public String generateOtp(String email) {

    System.out.println("===== OTP REQUEST RECEIVED =====");
    System.out.println("Email: " + email);

    String otp = String.valueOf(new Random().nextInt(900000) + 100000);

    OtpVerification otpEntity = otpRepo.findByEmail(email)
            .orElse(new OtpVerification());

    otpEntity.setEmail(email);
    otpEntity.setOtp(otp);
    otpEntity.setExpiryTime(LocalDateTime.now().plusMinutes(5));
    otpEntity.setVerified(false);

    otpRepo.save(otpEntity);

    System.out.println("OTP generated: " + otp);
    System.out.println("OTP saved to database.");
    System.out.println("About to send email...");

    sendOtpEmail(email, otp);

    System.out.println("Email sent successfully!");

    return "OTP sent successfully!";
}

public void sendOtpEmail(String toEmail, String otp) {

    RestTemplate restTemplate = new RestTemplate();

    String url = "https://api.brevo.com/v3/smtp/email";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("api-key", brevoApiKey);

    Map<String, Object> body = new HashMap<>();

    Map<String, String> sender = new HashMap<>();
    sender.put("name", "Cafeteria Portal");
    sender.put("email", "princesojitra29@gmail.com");

    Map<String, String> recipient = new HashMap<>();
    recipient.put("email", toEmail);

    body.put("sender", sender);
    body.put("to", java.util.List.of(recipient));
    body.put("subject", "Your OTP for Signup Verification");
    body.put(
            "textContent",
            "Your OTP is: " + otp + "\nIt is valid for 5 minutes."
    );

    HttpEntity<Map<String, Object>> request =
            new HttpEntity<>(body, headers);

    ResponseEntity<String> response =
            restTemplate.postForEntity(
                    url,
                    request,
                    String.class
            );

    System.out.println("Brevo API Response: " + response.getStatusCode());
    System.out.println(response.getBody());
}
    public boolean verifyOtp(String email, String otp) {

        OtpVerification otpEntity = otpRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("OTP not generated"));

        if (otpEntity.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired!");
        }

        if (!otpEntity.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP!");
        }

        otpEntity.setVerified(true);
        otpRepo.save(otpEntity);

        return true;
    }
    @Scheduled(fixedRate = 600000)   // Every 10 minutes
    public void cleanupExpiredOtps() {
        otpRepo.deleteExpiredOtps(LocalDateTime.now());
    }

}

