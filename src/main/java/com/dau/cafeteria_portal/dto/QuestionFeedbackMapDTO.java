package com.dau.cafeteria_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class QuestionFeedbackMapDTO {
    private Long questionId;
    private String questionText;
    private List<FeedbackResponseMapDTO> responses;
}
