package com.dau.cafeteria_portal.service;

import com.dau.cafeteria_portal.dto.ComplaintDTO;
import com.dau.cafeteria_portal.enums.ComplaintStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ComplaintService {
    ComplaintDTO createComplaint(ComplaintDTO dto, String emailId, Long canteenId);
    List<ComplaintDTO> getMyComplaints(String emailId);
    Optional<ComplaintDTO> getComplaintDetails(Long id);
    void attachFile(Long complaintId, String fileKey, String emailId);
    // for admin only.
     List<ComplaintDTO> getAllComplaints();
     void updateComplaintStatus(Long complaintId, ComplaintStatus complaintStatus);
    String escalateComplaint(Long complaintId);
    String getAdminDownloadUrl(Long id);

}
