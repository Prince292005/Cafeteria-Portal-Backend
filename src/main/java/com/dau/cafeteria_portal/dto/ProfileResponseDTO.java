package com.dau.cafeteria_portal.dto;

import com.dau.cafeteria_portal.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponseDTO {
    private String emailId;
    private Long mobileNumber;
    private String name;
}
