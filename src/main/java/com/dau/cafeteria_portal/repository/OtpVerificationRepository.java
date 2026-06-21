package com.dau.cafeteria_portal.repository;

import com.dau.cafeteria_portal.entity.OtpVerification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
    Optional<OtpVerification> findByEmail(String email);
    @Modifying
    @Transactional
    @Query("DELETE FROM OtpVerification o WHERE o.expiryTime < :now")
    void deleteExpiredOtps(LocalDateTime now);
}
