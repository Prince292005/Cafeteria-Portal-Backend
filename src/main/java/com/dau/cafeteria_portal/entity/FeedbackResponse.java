package com.dau.cafeteria_portal.entity;

import com.dau.cafeteria_portal.enums.FeedbackOption;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="question_id")
    private FeedbackQuestion question;

    @Enumerated(EnumType.STRING)
    private FeedbackOption option;

    private String reason;
    private LocalDateTime createdAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "canteen_id")
    private Canteen canteen;
}
