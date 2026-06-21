package com.dau.cafeteria_portal.controller;

import com.dau.cafeteria_portal.dto.*;
import com.dau.cafeteria_portal.entity.OtpVerification;
import com.dau.cafeteria_portal.entity.User;
import com.dau.cafeteria_portal.enums.Role;
import com.dau.cafeteria_portal.repository.OtpVerificationRepository;
import com.dau.cafeteria_portal.service.ForgotPasswordService;
import com.dau.cafeteria_portal.service.OtpService;
import com.dau.cafeteria_portal.service.impl.UserServiceImpl;
import com.dau.cafeteria_portal.util.JWTUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication API", description = "APIs for user registration and login")
public class UserAuthController {

    private final UserServiceImpl userService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final OtpService otpService;
    private final OtpVerificationRepository otpVerificationRepository;

    @Autowired
    public UserAuthController(UserServiceImpl userService,
                              UserDetailsService userDetailsService,
                              AuthenticationManager authenticationManager,
                              JWTUtil jwtUtil,
                              OtpService otpService,
                              OtpVerificationRepository otpVerificationRepository
                             ) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.otpService=otpService;
        this.otpVerificationRepository=otpVerificationRepository;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Registers a user with default role USER",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User registration data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SignUpRequestDTO.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request")
            }
    )
    public ResponseEntity<String> register(@RequestBody User user) {
        // Assign default role USER if not specified
        if (user.getUserRole() == null) {
            user.setUserRole(Role.USER);
        }
        OtpVerification otpEntity = otpVerificationRepository.findByEmail(user.getEmailId())
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (!otpEntity.isVerified()) {
            return ResponseEntity.badRequest().body("Email not verified!");
        }
        userService.save(user);
        return ResponseEntity.ok("User Registered Successfully with role: " + user.getUserRole());
    }


    // send OTP
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody SendOtpRequest request) {
        return ResponseEntity.ok(otpService.generateOtp(request.getEmail()));
    }


    // verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtpRequest req) {
        boolean result = otpService.verifyOtp(req.getEmail(), req.getOtp());

        if (!result) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }


    @PostMapping("/login")
    @Operation(
            summary = "Login user",
            description = "Authenticate user and return JWT token",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User login data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful"),
                    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content)
            }
    )


    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // Authenticate credentials
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getStudentId(), loginRequest.getPassword())
            );

            // Load user details
            UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getStudentId());

            // Extract role from authorities
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            String role = authorities.stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("ROLE_USER");

            // Generate JWT including role claim
            String token = jwtUtil.generateToken(userDetails.getUsername(), role, 60); // 60 minutes

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "studentId", userDetails.getUsername(),
                    "role", role
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }




}
