package com.dau.cafeteria_portal.repository;

import com.dau.cafeteria_portal.entity.QuickFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface QuickFeedbackRepository extends JpaRepository<QuickFeedback, Long> {

    boolean existsByUser_StudentIdAndCanteen_CanteenIdAndCreatedAtBetween(
            String studentId, Long canteenId, LocalDateTime start, LocalDateTime end
    );

    List<QuickFeedback> findByCanteen_CanteenIdAndCreatedAtBetween(
            Long canteenId, LocalDateTime start, LocalDateTime end
    );

    @Query("select avg(q.rating) from QuickFeedback q where q.canteen.canteenId = :canteenId")
    Double findAverageRatingByCanteenId(@Param("canteenId") Long canteenId);

    long countByCanteen_CanteenId(Long canteenId);

    /**
     * One query for every canteen's rating instead of firing an avg() and a
     * count() query per canteen (2N queries) on every canteen list load.
     */
    @Query("select q.canteen.canteenId as canteenId, avg(q.rating) as avgRating, count(q) as ratingCount " +
            "from QuickFeedback q group by q.canteen.canteenId")
    List<CanteenRatingAgg> findRatingAggregates();

    interface CanteenRatingAgg {
        Long getCanteenId();
        Double getAvgRating();
        Long getRatingCount();
    }
}
