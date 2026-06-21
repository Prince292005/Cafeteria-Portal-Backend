package com.dau.cafeteria_portal.repository;

import com.dau.cafeteria_portal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    Optional<User> findByEmailId(String emailId);
    boolean existsByEmailId(String emailId);
}
