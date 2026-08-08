package com.dau.cafeteria_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportComplaintDTO {
    private String title;
    private String description;
}
