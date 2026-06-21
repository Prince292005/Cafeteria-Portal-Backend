package com.dau.cafeteria_portal.controller;

import com.dau.cafeteria_portal.dto.CanteenDTO;
import com.dau.cafeteria_portal.service.CanteenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/admin/canteens")
public class AdminCanteenController {

    private final CanteenService canteenService;
    private static final String IMAGE_FOLDER = "C:/cafeteria-data/images/";
    private static final String CERTIFICATE_FOLDER = "C:/cafeteria-data/certificates/";
    private static final String MENU_FOLDER = "C:/cafeteria-data/menus/";

    public AdminCanteenController(CanteenService canteenService) {
        this.canteenService = canteenService;
    }

    @PostMapping
    public ResponseEntity<String> addCanteen(@RequestBody CanteenDTO canteenDTO) {
        canteenService.addCanteen(canteenDTO);
        return ResponseEntity.ok("Canteen added successfully!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateCanteen(@PathVariable Long id, @RequestBody CanteenDTO updatedCanteen) {
        canteenService.updateCanteen(id, updatedCanteen);
        return ResponseEntity.ok("Canteen updated successfully!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCanteen(@PathVariable Long id) {
        canteenService.deleteCanteen(id);
        return ResponseEntity.ok("Canteen deleted successfully!");
    }
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<String> uploadCanteenImage(@PathVariable Long id,
                                                     @RequestParam("file") MultipartFile file) throws IOException {
        String filename = "canteen_" + id + "_" + file.getOriginalFilename();
        Path path = Paths.get(IMAGE_FOLDER + filename);
        Files.write(path, file.getBytes());
        canteenService.updateCanteenImage(id, "/images/" + filename);
        return ResponseEntity.ok("Canteen image uploaded successfully");
    }

    @PostMapping("/{id}/upload-fssai")
    public ResponseEntity<String> uploadFssaiCertificate(@PathVariable Long id,
                                                         @RequestParam("file") MultipartFile file) throws IOException {
        String filename = "fssai_" + id + "_" + file.getOriginalFilename();
        Path path = Paths.get(CERTIFICATE_FOLDER + filename);
        Files.write(path, file.getBytes());
        canteenService.updateFssaiCertificate(id, "/certificates/" + filename);
        return ResponseEntity.ok("FSSAI certificate uploaded successfully");
    }

    @PostMapping("/{id}/upload-menu")
    public ResponseEntity<String> uploadMenu(@PathVariable Long id,
                                             @RequestParam("file") MultipartFile file) throws IOException {
        String filename = "menu_" + id + "_" + file.getOriginalFilename();
        Path path = Paths.get(MENU_FOLDER + filename);
        Files.write(path, file.getBytes());
        canteenService.updateMenuFile(id, "/menus/" + filename);
        return ResponseEntity.ok("Menu uploaded successfully");
    }
}
