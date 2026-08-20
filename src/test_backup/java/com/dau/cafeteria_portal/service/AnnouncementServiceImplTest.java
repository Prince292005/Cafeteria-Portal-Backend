package com.dau.cafeteria_portal.service;

import com.dau.cafeteria_portal.dto.AnnouncementDTO;
import com.dau.cafeteria_portal.entity.Announcement;
import com.dau.cafeteria_portal.repository.AnnouncementRepository;
import com.dau.cafeteria_portal.service.impl.AnnouncementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnnouncementServiceImplTest {

    @Mock
    private AnnouncementRepository announcementRepository;

    @InjectMocks
    private AnnouncementServiceImpl announcementService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // -----------------------------------------------------
    // TEST: createAnnouncement()
    // -----------------------------------------------------
    @Test
    void testCreateAnnouncement() {
        AnnouncementDTO dto = new AnnouncementDTO();
        dto.setTitle("Test Announcement");
        dto.setMessage("This is a test message");

        Announcement savedAnnouncement = new Announcement();
        savedAnnouncement.setId(1L);
        savedAnnouncement.setTitle("Test Announcement");
        savedAnnouncement.setMessage("This is a test message");
        savedAnnouncement.setActive(true);
        savedAnnouncement.setCreatedAt(LocalDateTime.now());

        when(announcementRepository.save(any(Announcement.class)))
                .thenReturn(savedAnnouncement);

        Announcement result = announcementService.createAnnouncement(dto);

        assertNotNull(result);
        assertEquals("Test Announcement", result.getTitle());
        assertEquals("This is a test message", result.getMessage());
        assertTrue(result.isActive());
        verify(announcementRepository, times(1)).save(any(Announcement.class));
    }

    // -----------------------------------------------------
    // TEST: getActiveAnnouncements()
    // -----------------------------------------------------
    @Test
    void testGetActiveAnnouncements() {
        List<Announcement> list = Arrays.asList(
                new Announcement(1L, "Title1", "Msg1", LocalDateTime.now(), true),
                new Announcement(2L, "Title2", "Msg2", LocalDateTime.now(), true)
        );

        when(announcementRepository.findByActiveTrueOrderByCreatedAtDesc())
                .thenReturn(list);

        List<Announcement> result = announcementService.getActiveAnnouncements();

        assertEquals(2, result.size());
        verify(announcementRepository, times(1))
                .findByActiveTrueOrderByCreatedAtDesc();
    }

    // -----------------------------------------------------
    // TEST: deactivateAnnouncement() success
    // -----------------------------------------------------
    @Test
    void testDeactivateAnnouncementSuccess() {
        Announcement ann = new Announcement(1L, "Title", "Msg", LocalDateTime.now(), true);

        when(announcementRepository.findById(1L))
                .thenReturn(Optional.of(ann));

        String response = announcementService.deactivateAnnouncement(1L);

        assertFalse(ann.isActive());
        assertEquals("Announcement deactivated successfully!", response);

        verify(announcementRepository, times(1)).findById(1L);
        verify(announcementRepository, times(1)).save(ann);
    }

    // -----------------------------------------------------
    // TEST: deactivateAnnouncement() throws exception
    // -----------------------------------------------------
    @Test
    void testDeactivateAnnouncementNotFound() {
        when(announcementRepository.findById(5L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> announcementService.deactivateAnnouncement(5L)
        );

        assertEquals("Announcement not found", exception.getMessage());
        verify(announcementRepository, times(1)).findById(5L);
    }
}
