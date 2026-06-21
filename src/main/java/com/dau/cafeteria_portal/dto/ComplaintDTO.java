package com.dau.cafeteria_portal.dto;

import com.dau.cafeteria_portal.enums.ComplaintStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintDTO {
    private Long complainId;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private ComplaintStatus complaintStatus;
    private String emailId;
    private Long CanteenId;
    // for user uploading
    private String uploadUrl;   // returned after create
    // for user downloading..
    private String downloadUrl; // returned for admin
    private String imageKey;
}
