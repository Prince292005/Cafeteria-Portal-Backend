package com.dau.cafeteria_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CanteenReportDataDTO {
    private Long canteenId;
    private String canteenName;
    private List<ReportComplaintDTO> complaints;
    private List<ReportFeedbackDTO> feedbackSummary;
}
