package com.dau.cafeteria_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuickFeedbackDTO {
    private int rating;       // 1-5 stars
    private String tag;       // optional: "Hygiene" / "Taste" / "Staff" / "Service" — nullable
}
