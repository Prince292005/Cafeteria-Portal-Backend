package com.dau.cafeteria_portal.service.impl;


import com.dau.cafeteria_portal.dto.AnnouncementDTO;
import com.dau.cafeteria_portal.entity.Announcement;
import com.dau.cafeteria_portal.repository.AnnouncementRepository;
import com.dau.cafeteria_portal.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    public Announcement createAnnouncement(AnnouncementDTO dto) {
        Announcement ann = new Announcement();
        ann.setTitle(dto.getTitle());
        ann.setMessage(dto.getMessage());
        ann.setCreatedAt(LocalDateTime.now());
        ann.setActive(true);
        return announcementRepository.save(ann);
    }

    public List<Announcement> getActiveAnnouncements() {
        return announcementRepository.findByActiveTrueOrderByCreatedAtDesc();
    }
    public String deactivateAnnouncement(Long id) {
        Announcement announcement = announcementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Announcement not found"));

        announcement.setActive(false);
        announcementRepository.save(announcement);

        return "Announcement deactivated successfully!";
    }

}
