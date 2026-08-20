package com.dau.cafeteria_portal.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String name;
    private Long mobileNumber;
}
