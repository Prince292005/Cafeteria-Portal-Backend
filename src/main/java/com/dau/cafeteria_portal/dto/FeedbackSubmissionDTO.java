package com.dau.cafeteria_portal.dto;

import com.dau.cafeteria_portal.enums.FeedbackOption;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class FeedbackSubmissionDTO {
    private Long questionId;
    private FeedbackOption option;
    private String reason;
}
