package com.dau.cafeteria_portal.repository;

import com.dau.cafeteria_portal.entity.Canteen;
import com.dau.cafeteria_portal.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CanteenRepository extends JpaRepository<Canteen,Long> {

}
