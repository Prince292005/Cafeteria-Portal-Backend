package com.dau.cafeteria_portal.dto;

import com.dau.cafeteria_portal.enums.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {
    private String studentId;
    private String name;
    private String emailId;
    private Long mobileNumber;
    private Role userRole;
    private LocalDateTime createdAt;
}
