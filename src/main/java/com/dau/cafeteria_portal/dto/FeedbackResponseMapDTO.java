package com.dau.cafeteria_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponseMapDTO {
    private Long id;
    private String option;
    private String reason;
    private LocalDateTime createdAt;
}
