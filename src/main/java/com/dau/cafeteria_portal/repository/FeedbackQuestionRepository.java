package com.dau.cafeteria_portal.repository;

import com.dau.cafeteria_portal.entity.FeedbackQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackQuestionRepository extends JpaRepository<FeedbackQuestion,Long> {
    List<FeedbackQuestion> findByCanteen_CanteenId(Long canteenId);
}
