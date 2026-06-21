package com.dau.cafeteria_portal.controller;

import com.dau.cafeteria_portal.dto.EngagementResultDTO;
import com.dau.cafeteria_portal.dto.QuickFeedbackDTO;
import com.dau.cafeteria_portal.service.QuickFeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/user/quick-feedback")
@Tag(name = "Quick Feedback", description = "One-tap canteen rating with streak/points")
public class QuickFeedbackController {

    private final QuickFeedbackService quickFeedbackService;

    public QuickFeedbackController(QuickFeedbackService quickFeedbackService) {
        this.quickFeedbackService = quickFeedbackService;
    }

    @PostMapping("/canteen/{canteenId}")
    @Operation(
            summary = "Submit one-tap feedback",
            description = "Submits a single star rating (1-5) for a canteen and updates the student's streak/points"
    )
    public ResponseEntity<EngagementResultDTO> submitQuickFeedback(
            @PathVariable Long canteenId,
            @RequestBody QuickFeedbackDTO dto,
            Principal principal) {

        String emailId = principal.getName();
        EngagementResultDTO result = quickFeedbackService.submit(emailId, canteenId, dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get my current streak and points",
            description = "Returns the logged-in student's current streak/points without requiring a new submission"
    )
    public ResponseEntity<EngagementResultDTO> getMyEngagement(Principal principal) {
        String emailId = principal.getName();
        EngagementResultDTO result = quickFeedbackService.getEngagement(emailId);
        return ResponseEntity.ok(result);
    }
}
