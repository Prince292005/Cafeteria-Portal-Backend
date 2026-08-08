package com.dau.cafeteria_portal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Canteen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long canteenId;

    private String canteenName;
    private String info;
    private String tagline;
    private String cuisine;
    private String location;
    private String hours;
    private Integer priceLevel = 2; // 1=Budget, 2=Moderate, 3=Premium
    // attachment photo of canteen..
    private String fssaiCertificateUrl;
    private String imageUrl;
    private String menuFilePath; // Menu file path
    private String accent = "terracotta"; // UI color theme, chosen by admin per canteen
    @OneToMany(mappedBy = "canteen", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<FeedbackQuestion> feedbackQuestions;

    @OneToMany(mappedBy = "canteen",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Complaint> complaints;
    // add isAvailable/ visible for user(general complaints )
}
