package com.dau.cafeteria_portal.entity;

import com.dau.cafeteria_portal.enums.ComplaintStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Complaint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long complainId;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    private LocalDateTime createdAt;
    // later update it using enums
    private ComplaintStatus complaintStatus;
    // add attachment later.
    // first i will implement simple APIs
    @PrePersist
    public void onCreate(){
        this.createdAt=LocalDateTime.now();
    }


    @ManyToOne(fetch = FetchType.LAZY)   // many complaints belong to one user
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch=FetchType.LAZY) // one canteen can have many complaints.
    @JoinColumn(name="canteen_id",nullable = false)
    private Canteen canteen;

    // Only store S3 object key (not full URL)
    private String imageKey;
}

