package com.dau.cafeteria_portal.controller;

import com.dau.cafeteria_portal.dto.ProfileResponseDTO;
import com.dau.cafeteria_portal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
}
