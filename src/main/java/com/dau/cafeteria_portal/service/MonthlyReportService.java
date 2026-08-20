package com.dau.cafeteria_portal.service;

import com.dau.cafeteria_portal.dto.MonthlyReportDTO;

import java.time.YearMonth;

public interface MonthlyReportService {
    MonthlyReportDTO generateMonthlyReport(YearMonth month);
    // canteenId == null -> every canteen (existing "all cafeterias" report)
    MonthlyReportDTO generateMonthlyReport(YearMonth month, Long canteenId);
}
