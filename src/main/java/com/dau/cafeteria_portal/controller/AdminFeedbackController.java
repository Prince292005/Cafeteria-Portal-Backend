package com.dau.cafeteria_portal.controller;

import com.dau.cafeteria_portal.dto.FeedbackQuestionDTO;
import com.dau.cafeteria_portal.dto.QuestionFeedbackMapDTO;
import com.dau.cafeteria_portal.entity.FeedbackQuestion;
import com.dau.cafeteria_portal.entity.FeedbackResponse;
import com.dau.cafeteria_portal.service.FeedbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/feedback")
public class AdminFeedbackController {

    private final FeedbackService feedbackService;

    public AdminFeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // Add a new feedback question
    @PostMapping("/question")
    public ResponseEntity<FeedbackQuestion> addQuestion(@RequestBody FeedbackQuestionDTO dto) {
        return ResponseEntity.ok(feedbackService.addQuestionToCanteen(dto));
    }

    // Update an existing question
    @PutMapping("/question/{id}")
    public ResponseEntity<FeedbackQuestion> updateQuestion(
            @PathVariable("id") Long questionId,
            @RequestBody FeedbackQuestionDTO dto) {
        return ResponseEntity.ok(feedbackService.updateQuestion(questionId, dto.getQuestion()));
    }


    // Delete a question
    @DeleteMapping("/question/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long id) {
        feedbackService.deleteQuestion(id);
        return ResponseEntity.ok("Question deleted successfully");
    }

    // Get all responses for a canteen
    @GetMapping("/canteen/{canteenId}/responses")
    public ResponseEntity<List<FeedbackResponse>> getCanteenFeedback(@PathVariable Long canteenId) {
        return ResponseEntity.ok(feedbackService.getFeedbackForCanteen(canteenId));
    }
//    @GetMapping("/canteen/{canteenId}/responses/month")
//    public ResponseEntity<List<FeedbackResponse>> getMonthlyFeedback(
//            @PathVariable Long canteenId,
//            @RequestParam int year,
//            @RequestParam int month
//    ) {
//        return ResponseEntity.ok(
//                feedbackService.getFeedbackForCanteenByMonth(canteenId, year, month)
//        );
//    }
    @GetMapping("/canteen/{canteenId}/feedback/monthly")
    public ResponseEntity<List<QuestionFeedbackMapDTO>> getMonthlyQuestionWiseFeedback(
            @PathVariable Long canteenId,
            @RequestParam int month,
            @RequestParam int year
    ) {
        return ResponseEntity.ok(
                feedbackService.getFeedbackGroupedByQuestionsForMonth(canteenId, month, year)
        );
    }


}
