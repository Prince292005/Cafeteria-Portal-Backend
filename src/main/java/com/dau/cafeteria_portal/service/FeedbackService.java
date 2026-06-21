package com.dau.cafeteria_portal.service;

import com.dau.cafeteria_portal.dto.FeedbackQuestionDTO;
import com.dau.cafeteria_portal.dto.FeedbackSubmissionDTO;
import com.dau.cafeteria_portal.dto.QuestionFeedbackMapDTO;
import com.dau.cafeteria_portal.entity.FeedbackQuestion;
import com.dau.cafeteria_portal.entity.FeedbackResponse;

import java.util.List;

public interface FeedbackService {
    // Admin
    FeedbackQuestion addQuestionToCanteen(FeedbackQuestionDTO feedbackQuestionDTO);
    FeedbackQuestion updateQuestion(Long questionId, String newText);
    void deleteQuestion(Long questionId);
    List<FeedbackResponse> getFeedbackForCanteen(Long canteenId);
    List<FeedbackResponse> getFeedbackForCanteenByMonth(Long canteenId, int year, int month);

    // User
    List<FeedbackQuestion> getQuestionsByCanteen(Long canteenId);
    void submitFeedback(Long canteenId, List<FeedbackSubmissionDTO> submissions);
    List<QuestionFeedbackMapDTO> getFeedbackGroupedByQuestionsForMonth(Long canteenId,int month,int year);
}
