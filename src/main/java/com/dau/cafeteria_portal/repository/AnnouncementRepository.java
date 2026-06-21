package com.dau.cafeteria_portal.repository;


import com.dau.cafeteria_portal.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {

    List<Announcement> findByActiveTrueOrderByCreatedAtDesc();
    @Modifying
    @Query("UPDATE Announcement a SET a.active = false WHERE a.id = :id")
    void deactivateAnnouncement(@Param("id") Long id);

}
