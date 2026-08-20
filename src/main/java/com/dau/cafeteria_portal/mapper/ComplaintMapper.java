package com.dau.cafeteria_portal.mapper;

import com.dau.cafeteria_portal.dto.ComplaintDTO;
import com.dau.cafeteria_portal.entity.Canteen;
import com.dau.cafeteria_portal.entity.Complaint;
import com.dau.cafeteria_portal.entity.User;

public class ComplaintMapper {

    public static ComplaintDTO toDTO(Complaint c) {
        if (c == null) return null;
        ComplaintDTO dto = new ComplaintDTO();
        dto.setComplainId(c.getComplainId());
        dto.setTitle(c.getTitle());
        dto.setDescription(c.getDescription());
        dto.setCreatedAt(c.getCreatedAt());
        dto.setComplaintStatus(c.getComplaintStatus());
        dto.setImageKey(c.getImageKey());
        if (c.getCanteen() != null) {
            dto.setCanteenId(c.getCanteen().getCanteenId());
            dto.setCanteenName(c.getCanteen().getCanteenName());
        }
        // Real filer contact info, pulled from the already-linked User —
        // previously this was never set at all, so every complaint showed
        // no filer information to the committee.
        if (c.getUser() != null) {
            dto.setEmailId(c.getUser().getEmailId());
            dto.setStudentName(c.getUser().getName());
            dto.setStudentId(c.getUser().getStudentId());
            dto.setMobileNumber(c.getUser().getMobileNumber());
        }
        return dto;
    }

    public static Complaint toEntity(ComplaintDTO dto, User user, Canteen canteen) {
        if (dto == null) {
            return null;
        }
        Complaint complaint = new Complaint();
        complaint.setComplainId(dto.getComplainId()); // optional, usually null for new
        complaint.setTitle(dto.getTitle());
        complaint.setDescription(dto.getDescription());
        complaint.setCreatedAt(dto.getCreatedAt());
        complaint.setComplaintStatus(dto.getComplaintStatus());
        complaint.setUser(user);
        complaint.setCanteen(canteen);
        return complaint;
    }
}
