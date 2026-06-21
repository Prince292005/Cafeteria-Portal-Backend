package com.dau.cafeteria_portal.service;

import com.dau.cafeteria_portal.dto.FeedbackQuestionDTO;
import com.dau.cafeteria_portal.dto.FeedbackSubmissionDTO;
import com.dau.cafeteria_portal.entity.Canteen;
import com.dau.cafeteria_portal.entity.FeedbackQuestion;
import com.dau.cafeteria_portal.entity.FeedbackResponse;
import com.dau.cafeteria_portal.enums.FeedbackOption;
import com.dau.cafeteria_portal.repository.CanteenRepository;
import com.dau.cafeteria_portal.repository.FeedbackQuestionRepository;
import com.dau.cafeteria_portal.repository.FeedbackResponseRepository;
import com.dau.cafeteria_portal.service.impl.FeedbackServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedbackServiceImplTest {

    @Mock
    private FeedbackQuestionRepository questionRepo;

    @Mock
    private FeedbackResponseRepository responseRepo;

    @Mock
    private CanteenRepository canteenRepo;

    @InjectMocks
    private FeedbackServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------------------------------------------
    // 1️⃣ addQuestionToCanteen()
    // ---------------------------------------------

    @Test
    void testAddQuestionToCanteen_success() {
        FeedbackQuestionDTO dto = new FeedbackQuestionDTO(1L, "How is the food?");
        Canteen canteen = new Canteen();
        canteen.setCanteenId(1L);

        when(canteenRepo.findById(1L)).thenReturn(Optional.of(canteen));

        FeedbackQuestion saved = new FeedbackQuestion();
        saved.setQuestionText("How is the food?");
        when(questionRepo.save(any())).thenReturn(saved);

        FeedbackQuestion result = service.addQuestionToCanteen(dto);

        assertEquals("How is the food?", result.getQuestionText());
        verify(questionRepo, times(1)).save(any());
    }

    @Test
    void testAddQuestionToCanteen_canteenNotFound() {
        FeedbackQuestionDTO dto = new FeedbackQuestionDTO(1L, "Test");
        when(canteenRepo.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.addQuestionToCanteen(dto));

        assertEquals("Canteen not found", ex.getMessage());
    }

    // ---------------------------------------------
    // 2️⃣ updateQuestion()
    // ---------------------------------------------

    @Test
    void testUpdateQuestion_success() {
        FeedbackQuestion q = new FeedbackQuestion();
        q.setId(10L);
        q.setQuestionText("Old");

        when(questionRepo.findById(10L)).thenReturn(Optional.of(q));
        when(questionRepo.save(any())).thenReturn(q);

        FeedbackQuestion result = service.updateQuestion(10L, "New Question");

        assertEquals("New Question", result.getQuestionText());
        verify(questionRepo).save(q);
    }

    @Test
    void testUpdateQuestion_notFound() {
        when(questionRepo.findById(5L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.updateQuestion(5L, "New"));

        assertEquals("Question not found", ex.getMessage());
    }

    // ---------------------------------------------
    // 3️⃣ deleteQuestion()
    // ---------------------------------------------

    @Test
    void testDeleteQuestion_success() {
        when(questionRepo.existsById(3L)).thenReturn(true);

        service.deleteQuestion(3L);

        verify(questionRepo, times(1)).deleteById(3L);
    }

    @Test
    void testDeleteQuestion_notFound() {
        when(questionRepo.existsById(3L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.deleteQuestion(3L));

        assertEquals("Question not found", ex.getMessage());
    }

    // ---------------------------------------------
    // 4️⃣ getFeedbackForCanteen()
    // ---------------------------------------------

    @Test
    void testGetFeedbackForCanteen() {
        FeedbackResponse r = new FeedbackResponse();
        when(responseRepo.findByQuestionId(2L)).thenReturn(List.of(r));

        List<FeedbackResponse> result = service.getFeedbackForCanteen(2L);

        assertEquals(1, result.size());
    }

    // ---------------------------------------------
    // 5️⃣ getQuestionsByCanteen()
    // ---------------------------------------------

    @Test
    void testGetQuestionsByCanteen() {
        FeedbackQuestion q = new FeedbackQuestion();
        when(questionRepo.findByCanteen_CanteenId(1L)).thenReturn(List.of(q));

        List<FeedbackQuestion> result = service.getQuestionsByCanteen(1L);

        assertEquals(1, result.size());
    }

    // ---------------------------------------------
    // 6️⃣ submitFeedback()
    // ---------------------------------------------

    @Test
    void testSubmitFeedback_success() {
        Long canteenId = 1L;

        Canteen canteen = new Canteen();
        canteen.setCanteenId(1L);

        FeedbackQuestion question = new FeedbackQuestion();
        question.setId(5L);
        question.setCanteen(canteen);

        FeedbackSubmissionDTO dto =
                new FeedbackSubmissionDTO(5L, FeedbackOption.GOOD, "Good service");

        when(canteenRepo.findById(canteenId))
                .thenReturn(Optional.of(canteen));

        when(questionRepo.findById(5L))
                .thenReturn(Optional.of(question));

        service.submitFeedback(canteenId, List.of(dto));

        verify(responseRepo, times(1)).save(any(FeedbackResponse.class));
    }

    @Test
    void testSubmitFeedback_questionDoesNotBelongToCanteen() {
        Long canteenId = 1L;

        Canteen c1 = new Canteen();
        c1.setCanteenId(1L);

        Canteen c2 = new Canteen();
        c2.setCanteenId(2L);

        FeedbackQuestion wrongQuestion = new FeedbackQuestion();
        wrongQuestion.setId(5L);
        wrongQuestion.setCanteen(c2);

        FeedbackSubmissionDTO dto =
                new FeedbackSubmissionDTO(5L, FeedbackOption.AVERAGE, "Ok");

        when(canteenRepo.findById(1L)).thenReturn(Optional.of(c1));
        when(questionRepo.findById(5L)).thenReturn(Optional.of(wrongQuestion));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.submitFeedback(1L, List.of(dto)));

        assertTrue(ex.getMessage().contains("does not belong to canteen"));
    }

    @Test
    void testSubmitFeedback_questionNotFound() {
        FeedbackSubmissionDTO dto =
                new FeedbackSubmissionDTO(9L, FeedbackOption.POOR, "Bad");

        when(canteenRepo.findById(1L))
                .thenReturn(Optional.of(new Canteen()));

        when(questionRepo.findById(9L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.submitFeedback(1L, List.of(dto)));

        assertEquals("Question not found", ex.getMessage());
    }

    @Test
    void testSubmitFeedback_canteenNotFound() {
        FeedbackSubmissionDTO dto =
                new FeedbackSubmissionDTO(1L, FeedbackOption.VERY_GOOD, "Nice");

        when(canteenRepo.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> service.submitFeedback(99L, List.of(dto)));

        assertEquals("Canteen not found", ex.getMessage());
    }
}