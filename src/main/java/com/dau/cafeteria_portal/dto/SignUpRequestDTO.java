package com.dau.cafeteria_portal.dto;

import com.dau.cafeteria_portal.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDTO {
    private String studentId;
    private String userName;
    private String emailId;
    private String mobileNumber;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
}
