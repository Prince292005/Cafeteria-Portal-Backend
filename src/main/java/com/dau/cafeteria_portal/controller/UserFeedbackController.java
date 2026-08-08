package com.dau.cafeteria_portal.controller;

import com.dau.cafeteria_portal.dto.FeedbackSubmissionDTO;
import com.dau.cafeteria_portal.entity.FeedbackQuestion;
import com.dau.cafeteria_portal.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/feedback")
public class UserFeedbackController {

    private final FeedbackService feedbackService;

    public UserFeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // Get all feedback questions for a canteen
    @GetMapping("/canteen/{canteenId}/questions")
    public ResponseEntity<List<FeedbackQuestion>> getQuestions(@PathVariable Long canteenId) {
        return ResponseEntity.ok(feedbackService.getQuestionsByCanteen(canteenId));
    }

    // Submit feedback for multiple questions
    @PostMapping("/canteen/{canteenId}/submit")
    public ResponseEntity<String> submitFeedback(
            @PathVariable Long canteenId,
            @RequestBody List<FeedbackSubmissionDTO> submissions) {

        feedbackService.submitFeedback(canteenId, submissions);
        return ResponseEntity.ok("Feedback submitted successfully");
    }
}
