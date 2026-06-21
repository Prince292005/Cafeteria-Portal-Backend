package com.dau.cafeteria_portal.repository;

import com.dau.cafeteria_portal.entity.QuickFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface QuickFeedbackRepository extends JpaRepository<QuickFeedback, Long> {

    boolean existsByUser_StudentIdAndCanteen_CanteenIdAndCreatedAtBetween(
            String studentId, Long canteenId, LocalDateTime start, LocalDateTime end
    );

    List<QuickFeedback> findByCanteen_CanteenIdAndCreatedAtBetween(
            Long canteenId, LocalDateTime start, LocalDateTime end
    );
}
