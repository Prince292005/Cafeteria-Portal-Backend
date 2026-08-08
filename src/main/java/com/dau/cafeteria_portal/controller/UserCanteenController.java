package com.dau.cafeteria_portal.controller;

import com.dau.cafeteria_portal.dto.CanteenDTO;
import com.dau.cafeteria_portal.service.CanteenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/canteens")
public class UserCanteenController {

    private final CanteenService canteenService;

    public UserCanteenController(CanteenService canteenService) {
        this.canteenService = canteenService;
    }

    @GetMapping
    public ResponseEntity<List<CanteenDTO>> getAllCanteens() {
        List<CanteenDTO> canteens = canteenService.getAllCanteens();
        return ResponseEntity.ok(canteens);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CanteenDTO> getCanteenById(@PathVariable Long id) {
        CanteenDTO canteen = canteenService.getCanteenById(id);
        return ResponseEntity.ok(canteen);
    }
}
