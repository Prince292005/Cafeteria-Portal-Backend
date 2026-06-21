package com.dau.cafeteria_portal.service.impl;

import com.dau.cafeteria_portal.dto.CanteenReportDataDTO;
import com.dau.cafeteria_portal.dto.MonthlyReportDTO;
import com.dau.cafeteria_portal.dto.ReportComplaintDTO;
import com.dau.cafeteria_portal.dto.ReportFeedbackDTO;
import com.dau.cafeteria_portal.entity.Canteen;
import com.dau.cafeteria_portal.entity.Complaint;
import com.dau.cafeteria_portal.entity.FeedbackResponse;
import com.dau.cafeteria_portal.repository.CanteenRepository;
import com.dau.cafeteria_portal.repository.ComplaintRepository;
import com.dau.cafeteria_portal.repository.FeedbackResponseRepository;
import com.dau.cafeteria_portal.service.MonthlyReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@Service
public class MonthlyReportServiceImpl implements MonthlyReportService {

    private final ComplaintRepository complaintRepository;
    private final FeedbackResponseRepository feedbackResponseRepository;
    private final CanteenRepository canteenRepository;

    public MonthlyReportServiceImpl(ComplaintRepository complaintRepository,
                                    FeedbackResponseRepository feedbackResponseRepository,
                                    CanteenRepository canteenRepository) {
        this.complaintRepository = complaintRepository;
        this.feedbackResponseRepository = feedbackResponseRepository;
        this.canteenRepository = canteenRepository;
    }

    @Override
    public MonthlyReportDTO generateMonthlyReport(YearMonth month) {

        String monthName = month.getMonth().name() + " " + month.getYear();
        LocalDateTime end = month.atEndOfMonth().atTime(23, 59, 59);
        LocalDateTime start = month.atDay(1).atStartOfDay();
        List<Canteen> canteens = canteenRepository.findAll();

        List<CanteenReportDataDTO> reportList = new ArrayList<>();


        for (Canteen canteen : canteens) {


            // Fetch complaints
            List<Complaint> complaints =
                    complaintRepository.findAllByCanteen_CanteenIdAndCreatedAtBetween(
                            canteen.getCanteenId(), start, end);


            List<ReportComplaintDTO> complaintDTOs = complaints.stream()
                    .map(c -> {
                        System.out.println(" - Complaint: " + c.getTitle() + " | " + c.getDescription());
                        return new ReportComplaintDTO(c.getTitle(), c.getDescription());
                    })
                    .toList();


            // Fetch feedback responses
            List<FeedbackResponse> responses =
                    feedbackResponseRepository.findAllByCanteen_CanteenIdAndCreatedAtBetween(
                            canteen.getCanteenId(), start, end);


            Map<String, List<Integer>> questionRatings = new HashMap<>();
            Map<String, List<String>> questionReasons = new HashMap<>();

            for (FeedbackResponse fr : responses) {

                String qText = fr.getQuestion().getQuestionText();
                int rating = fr.getOption().getRating();


                questionRatings.computeIfAbsent(qText, k -> new ArrayList<>()).add(rating);

                if (fr.getReason() != null && !fr.getReason().isBlank()) {
                    questionReasons.computeIfAbsent(qText, k -> new ArrayList<>()).add(fr.getReason());
                }
            }

            // Build feedback summary
            List<ReportFeedbackDTO> feedbackSummary = new ArrayList<>();


            for (String question : questionRatings.keySet()) {

                List<Integer> ratings = questionRatings.get(question);
                double avg = ratings.stream().mapToInt(i -> i).average().orElse(0.0);


                feedbackSummary.add(
                        new ReportFeedbackDTO(
                                question,
                                avg,
                                questionReasons.getOrDefault(question, List.of())
                        )
                );
            }

            // Build final canteen report section
            CanteenReportDataDTO canteenReport = new CanteenReportDataDTO(
                    canteen.getCanteenId(),
                    canteen.getCanteenName(),
                    complaintDTOs,
                    feedbackSummary
            );


            reportList.add(canteenReport);
        }


        return new MonthlyReportDTO(monthName, reportList);
    }
}
