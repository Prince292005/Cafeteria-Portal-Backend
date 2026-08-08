package com.dau.cafeteria_portal.controller;

import com.dau.cafeteria_portal.dto.AnnouncementDTO;
import com.dau.cafeteria_portal.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AnnouncementController {

    private final AnnouncementService announcementService;

    // ADMIN: Create announcement
    @PostMapping("/admin/announcement/create")
    public ResponseEntity<?> create(@RequestBody AnnouncementDTO dto) {
        return ResponseEntity.ok(announcementService.createAnnouncement(dto));
    }

    // USERS: Get all active announcements
    @GetMapping("/api/announcement/active")
    public ResponseEntity<?> getActiveAnnouncements() {
        return ResponseEntity.ok(announcementService.getActiveAnnouncements());
    }
    @PutMapping("/admin/announcement/{id}/deactivate")
    public ResponseEntity<?> deactivate(@PathVariable Long id) {
        return ResponseEntity.ok(announcementService.deactivateAnnouncement(id));
    }
}
