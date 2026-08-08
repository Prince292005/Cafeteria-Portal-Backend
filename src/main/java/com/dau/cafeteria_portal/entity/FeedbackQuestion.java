package com.dau.cafeteria_portal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.aot.generate.GeneratedMethod;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String questionText;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name="canteen_id")
    private Canteen canteen;


}
