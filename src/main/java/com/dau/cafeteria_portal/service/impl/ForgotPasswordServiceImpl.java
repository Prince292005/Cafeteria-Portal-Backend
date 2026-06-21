package com.dau.cafeteria_portal.service.impl;

import com.dau.cafeteria_portal.dto.ForgotPasswordRequest;
import com.dau.cafeteria_portal.dto.ResetPasswordRequest;
import com.dau.cafeteria_portal.dto.VerifyOtpRequest;
import com.dau.cafeteria_portal.entity.OtpVerification;
import com.dau.cafeteria_portal.entity.User;
import com.dau.cafeteria_portal.repository.OtpVerificationRepository;
import com.dau.cafeteria_portal.repository.UserRepository;
import com.dau.cafeteria_portal.service.ForgotPasswordService;
import com.dau.cafeteria_portal.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final UserRepository userRepo;
    private final OtpService otpService;  // your implementation
    private final PasswordEncoder passwordEncoder;
    private final OtpVerificationRepository otpRepo;

    // SEND OTP
    public String sendOtp(ForgotPasswordRequest req) {

        // check if user exists
        userRepo.findByEmailId(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // generate OTP using your existing service
        return otpService.generateOtp(req.getEmail());
    }

    // VERIFY OTP
    public String verifyOtp(VerifyOtpRequest req) {

        // user must exist
        userRepo.findByEmailId(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // call your existing verifyOtp() method
        otpService.verifyOtp(req.getEmail(), req.getOtp());

        return "OTP verified successfully!";
    }

    // RESET PASSWORD
    public String resetPassword(ResetPasswordRequest req) {

        User user = userRepo.findByEmailId(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // check latest OTP status
        OtpVerification otp = otpRepo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("OTP not generated"));

        if (!otp.isVerified()) {
            throw new RuntimeException("OTP not verified!");
        }

        // update password
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepo.save(user);

        // remove OTP entry after reset (recommended)
        otpRepo.delete(otp);

        return "Password reset successfully!";
    }
}
