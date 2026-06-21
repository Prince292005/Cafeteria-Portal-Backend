package com.dau.cafeteria_portal.controller;

import com.dau.cafeteria_portal.dto.ForgotPasswordRequest;
import com.dau.cafeteria_portal.dto.ResetPasswordRequest;
import com.dau.cafeteria_portal.dto.VerifyOtpRequest;
import com.dau.cafeteria_portal.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/forgot-password")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody ForgotPasswordRequest req) {
        return ResponseEntity.ok(forgotPasswordService.sendOtp(req));
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest req) {
        return ResponseEntity.ok(forgotPasswordService.verifyOtp(req));
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest req) {
        return ResponseEntity.ok(forgotPasswordService.resetPassword(req));
    }
}
