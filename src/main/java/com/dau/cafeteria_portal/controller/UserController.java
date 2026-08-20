package com.dau.cafeteria_portal.controller;

import com.dau.cafeteria_portal.dto.ProfileResponseDTO;
import com.dau.cafeteria_portal.dto.UpdateProfileRequest;
import com.dau.cafeteria_portal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponseDTO> getProfile(Principal principal) {
        String emailId = principal.getName(); // Assuming email is stored in JWT
        ProfileResponseDTO profile = userService.getProfile(emailId);
        return ResponseEntity.ok(profile);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(Principal principal, @RequestBody UpdateProfileRequest request) {
        String emailId = principal.getName();
        try {
            ProfileResponseDTO updated = userService.updateProfile(emailId, request.getName(), request.getMobileNumber());
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }
    }
}
