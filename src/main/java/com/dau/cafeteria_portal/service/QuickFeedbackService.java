package com.dau.cafeteria_portal.service;

import com.dau.cafeteria_portal.dto.EngagementResultDTO;
import com.dau.cafeteria_portal.dto.QuickFeedbackDTO;
import com.dau.cafeteria_portal.entity.Canteen;
import com.dau.cafeteria_portal.entity.QuickFeedback;
import com.dau.cafeteria_portal.entity.User;
import com.dau.cafeteria_portal.entity.UserEngagement;
import com.dau.cafeteria_portal.repository.CanteenRepository;
import com.dau.cafeteria_portal.repository.QuickFeedbackRepository;
import com.dau.cafeteria_portal.repository.UserEngagementRepository;
import com.dau.cafeteria_portal.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional
public class QuickFeedbackService {

    private static final int POINTS_PER_SUBMISSION = 10;
    private static final int STREAK_BONUS_POINTS = 5; // extra points when the streak actually extends

    private final QuickFeedbackRepository quickFeedbackRepo;
    private final UserEngagementRepository engagementRepo;
    private final UserRepository userRepo;
    private final CanteenRepository canteenRepo;

    public QuickFeedbackService(QuickFeedbackRepository quickFeedbackRepo,
                                 UserEngagementRepository engagementRepo,
                                 UserRepository userRepo,
                                 CanteenRepository canteenRepo) {
        this.quickFeedbackRepo = quickFeedbackRepo;
        this.engagementRepo = engagementRepo;
        this.userRepo = userRepo;
        this.canteenRepo = canteenRepo;
    }

    public EngagementResultDTO submit(String emailId, Long canteenId, QuickFeedbackDTO dto) {
        if (dto.getRating() < 1 || dto.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        User user = userRepo.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Canteen canteen = canteenRepo.findById(canteenId)
                .orElseThrow(() -> new RuntimeException("Canteen not found"));

        QuickFeedback feedback = new QuickFeedback();
        feedback.setUser(user);
        feedback.setCanteen(canteen);
        feedback.setRating(dto.getRating());
        feedback.setTag(dto.getTag());
        quickFeedbackRepo.save(feedback);

        return updateEngagement(user.getStudentId());
    }

    public EngagementResultDTO getEngagement(String emailId) {
        User user = userRepo.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return engagementRepo.findById(user.getStudentId())
                .map(e -> new EngagementResultDTO(e.getCurrentStreak(), e.getTotalPoints(), false))
                .orElse(new EngagementResultDTO(0, 0, false));
    }

    private EngagementResultDTO updateEngagement(String studentId) {
        UserEngagement engagement = engagementRepo.findById(studentId)
                .orElseGet(() -> new UserEngagement(studentId));

        LocalDate today = LocalDate.now();
        LocalDate lastDate = engagement.getLastFeedbackDate();

        boolean streakExtended = false;

        if (lastDate == null) {
            // First ever submission
            engagement.setCurrentStreak(1);
            streakExtended = true;
        } else if (lastDate.equals(today)) {
            // Already submitted today — points for the action, streak unchanged
            streakExtended = false;
        } else if (lastDate.equals(today.minusDays(1))) {
            // Submitted yesterday — streak continues
            engagement.setCurrentStreak(engagement.getCurrentStreak() + 1);
            streakExtended = true;
        } else {
            // Gap of 2+ days — streak resets to 1
            engagement.setCurrentStreak(1);
            streakExtended = true;
        }

        int pointsEarned = POINTS_PER_SUBMISSION + (streakExtended ? STREAK_BONUS_POINTS : 0);
        engagement.setTotalPoints(engagement.getTotalPoints() + pointsEarned);
        engagement.setTotalFeedbackCount(engagement.getTotalFeedbackCount() + 1);
        engagement.setLastFeedbackDate(today);

        engagementRepo.save(engagement);

        return new EngagementResultDTO(
                engagement.getCurrentStreak(),
                engagement.getTotalPoints(),
                streakExtended
        );
    }
}

