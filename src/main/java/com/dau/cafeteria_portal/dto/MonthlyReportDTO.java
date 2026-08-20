package com.dau.cafeteria_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyReportDTO {
    private String month;   // e.g., "January 2025"
    private List<CanteenReportDataDTO> canteenReports;
}
