package com.dau.cafeteria_portal.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackQuestionDTO {
    @NotNull
    private Long canteenId;
    private String question;

}
