package com.dau.cafeteria_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportFeedbackDTO {
    private String question;
    private double averageRating;
    private List<String> reasons;
}

