package com.dau.cafeteria_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EngagementResultDTO {
    private int currentStreak;
    private int totalPoints;
    private boolean streakExtended; // true if this submission grew the streak (vs same-day repeat)
}
