package com.dau.cafeteria_portal.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@Table(name = "user_engagement")
public class UserEngagement {

    @Id
    @Column(name = "user_id")
    private String userId; // matches User.studentId — one row per student

    @Column(nullable = false)
    private int currentStreak = 0;

    @Column(nullable = false)
    private int totalPoints = 0;

    @Column(nullable = false)
    private int totalFeedbackCount = 0;

    private LocalDate lastFeedbackDate;

    public UserEngagement(String userId) {
        this.userId = userId;
    }
}
