package com.dau.cafeteria_portal.repository;

import com.dau.cafeteria_portal.entity.FeedbackResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FeedbackResponseRepository extends JpaRepository<FeedbackResponse,Long> {
    List<FeedbackResponse> findByCanteen_CanteenId(Long canteenId);

    List<FeedbackResponse> findAllByCanteen_CanteenIdAndCreatedAtBetween(
            Long canteenId,
            LocalDateTime start,
            LocalDateTime end
    );
    List<FeedbackResponse> findByQuestion_Id(Long questionId);
    List<FeedbackResponse>findAllByQuestion_IdAndCreatedAtBetween(Long questionId,LocalDateTime start,LocalDateTime end);
}
