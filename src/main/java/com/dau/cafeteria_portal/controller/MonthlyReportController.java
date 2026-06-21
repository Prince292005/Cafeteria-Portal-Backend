package com.dau.cafeteria_portal.controller;

import com.dau.cafeteria_portal.dto.MonthlyReportDTO;
import com.dau.cafeteria_portal.service.LLMReportService;
import com.dau.cafeteria_portal.service.MonthlyReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/admin/reports")
public class MonthlyReportController {

    private final MonthlyReportService monthlyReportService;
    private final LLMReportService llmReportService;
    public MonthlyReportController(MonthlyReportService monthlyReportService,LLMReportService llmReportService) {
        this.monthlyReportService = monthlyReportService;
        this.llmReportService=llmReportService;
    }

    @GetMapping("/monthly")
    public MonthlyReportDTO generateMonthlyReport(
            @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month
    ) {
        return monthlyReportService.generateMonthlyReport(month);
    }
    @GetMapping("/llm-monthly")
    public ResponseEntity<String> generateLLMMonthlyReport(@RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth month){

        MonthlyReportDTO dto = monthlyReportService.generateMonthlyReport(month);

        String report = llmReportService.sendToLLM(dto);

        return ResponseEntity.ok(report);
    }

}
