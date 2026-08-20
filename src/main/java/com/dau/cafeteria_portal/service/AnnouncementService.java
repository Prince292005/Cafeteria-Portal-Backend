package com.dau.cafeteria_portal.service;

import com.dau.cafeteria_portal.dto.AnnouncementDTO;
import com.dau.cafeteria_portal.entity.Announcement;

import java.util.List;

public interface AnnouncementService {

    Announcement createAnnouncement(AnnouncementDTO dto);
    List<Announcement> getActiveAnnouncements();
    String deactivateAnnouncement(Long id);
}
