package com.dau.cafeteria_portal.service.impl;

import com.dau.cafeteria_portal.dto.FeedbackQuestionDTO;
import com.dau.cafeteria_portal.dto.FeedbackResponseMapDTO;
import com.dau.cafeteria_portal.dto.FeedbackSubmissionDTO;
import com.dau.cafeteria_portal.dto.QuestionFeedbackMapDTO;
import com.dau.cafeteria_portal.entity.Canteen;
import com.dau.cafeteria_portal.entity.FeedbackQuestion;
import com.dau.cafeteria_portal.entity.FeedbackResponse;
import com.dau.cafeteria_portal.repository.CanteenRepository;
import com.dau.cafeteria_portal.repository.FeedbackQuestionRepository;
import com.dau.cafeteria_portal.repository.FeedbackResponseRepository;
import com.dau.cafeteria_portal.service.FeedbackService;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackQuestionRepository questionRepo;
    private final FeedbackResponseRepository responseRepo;
    private final CanteenRepository canteenRepo;

    public FeedbackServiceImpl(FeedbackQuestionRepository questionRepo,
                               FeedbackResponseRepository responseRepo,
                               CanteenRepository canteenRepo) {
        this.questionRepo = questionRepo;
        this.responseRepo = responseRepo;
        this.canteenRepo = canteenRepo;
    }

    // 🟢 Admin: Add question to canteen
    @Override
    public FeedbackQuestion addQuestionToCanteen(FeedbackQuestionDTO dto) {
        Canteen canteen = canteenRepo.findById(dto.getCanteenId())
                .orElseThrow(() -> new RuntimeException("Canteen not found"));

        FeedbackQuestion question = new FeedbackQuestion();
        question.setQuestionText(dto.getQuestion());
        question.setCanteen(canteen);

        return questionRepo.save(question);
    }

    //  Admin: Update question text
    @Override
    public FeedbackQuestion updateQuestion(Long questionId, String newText) {
        FeedbackQuestion question = questionRepo.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        question.setQuestionText(newText);
        return questionRepo.save(question);
    }

    // 🟢 Admin: Delete question
    @Override
    public void deleteQuestion(Long questionId) {
        if (!questionRepo.existsById(questionId)) {
            throw new RuntimeException("Question not found");
        }
        questionRepo.deleteById(questionId);
    }

    // 🟢 Admin: View all feedback responses for a canteen
    @Override
    public List<FeedbackResponse> getFeedbackForCanteen(Long canteenId) {
        return responseRepo.findByCanteen_CanteenId(canteenId);
    }

        // 🟢 User: Get all feedback questions for a canteen
    @Override
    public List<FeedbackQuestion> getQuestionsByCanteen(Long canteenId) {
        return questionRepo.findByCanteen_CanteenId(canteenId);
    }
    @Override
    public List<FeedbackResponse> getFeedbackForCanteenByMonth(Long canteenId, int year, int month) {

        LocalDateTime startDate = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime endDate = startDate.plusMonths(1);

        return responseRepo.findAllByCanteen_CanteenIdAndCreatedAtBetween(
                canteenId,
                startDate,
                endDate
        );
    }


    @Override
    public List<QuestionFeedbackMapDTO> getFeedbackGroupedByQuestionsForMonth(
            Long canteenId,
            int month,
            int year
    ) {

        // 1. Calculate date range for the month
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        LocalDateTime startDT = start.atStartOfDay();
        LocalDateTime endDT = end.atTime(23, 59, 59);

        // 2. Get all questions of the canteen
        List<FeedbackQuestion> questions = questionRepo.findByCanteen_CanteenId(canteenId);

        List<QuestionFeedbackMapDTO> result = new ArrayList<>();

        for (FeedbackQuestion q : questions) {

            List<FeedbackResponse> responses =
                    responseRepo.findAllByQuestion_IdAndCreatedAtBetween(
                            q.getId(),
                            startDT,
                            endDT
                    );

            // 4. Convert responses to DTO
            List<FeedbackResponseMapDTO> responseDTOs = responses.stream()
                    .map(r -> {
                        FeedbackResponseMapDTO dto = new FeedbackResponseMapDTO();
                        dto.setId(r.getId());
                        dto.setOption(r.getOption().toString());
                        dto.setReason(r.getReason());
                        dto.setCreatedAt(r.getCreatedAt());
                        return dto;
                    }).toList();

            // 5. Create Question DTO
            QuestionFeedbackMapDTO qdto = new QuestionFeedbackMapDTO();
            qdto.setQuestionId(q.getId());
            qdto.setQuestionText(q.getQuestionText());
            qdto.setResponses(responseDTOs);

            result.add(qdto);
        }

        return result;
    }

    // User: Submit feedback
    @Override
    public void submitFeedback(Long canteenId, List<FeedbackSubmissionDTO> submissions) {

        Canteen canteen = canteenRepo.findById(canteenId)
                .orElseThrow(() -> new RuntimeException("Canteen not found"));

        for (FeedbackSubmissionDTO dto : submissions) {

            FeedbackQuestion question = questionRepo.findById(dto.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Question not found"));

            // Validate: question must belong to canteen
            if (!question.getCanteen().getCanteenId().equals(canteenId)) {
                throw new RuntimeException("Question " + dto.getQuestionId() +
                        " does not belong to canteen " + canteenId);
            }

            FeedbackResponse response = new FeedbackResponse();
            response.setQuestion(question);
            response.setOption(dto.getOption());
            response.setReason(dto.getReason());
            response.setCreatedAt(LocalDateTime.now());
            response.setCanteen(question.getCanteen());
            responseRepo.save(response);
        }
    }
}
