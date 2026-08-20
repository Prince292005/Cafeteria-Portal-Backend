package com.dau.cafeteria_portal.service;

import com.dau.cafeteria_portal.dto.MonthlyReportDTO;

public interface LLMReportService {
    String sendToLLM(MonthlyReportDTO dto);
}
