package com.dau.cafeteria_portal.service;

import com.dau.cafeteria_portal.dto.ForgotPasswordRequest;
import com.dau.cafeteria_portal.dto.ResetPasswordRequest;
import com.dau.cafeteria_portal.dto.VerifyOtpRequest;

public interface ForgotPasswordService {
//    String sendOtpForForgotPassword(String email);
    String sendOtp(ForgotPasswordRequest req);
    String verifyOtp(VerifyOtpRequest req);
    String resetPassword(ResetPasswordRequest req);
}

