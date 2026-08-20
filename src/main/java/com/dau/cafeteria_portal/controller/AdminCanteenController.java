package com.dau.cafeteria_portal.controller;

import com.dau.cafeteria_portal.dto.CanteenDTO;
import com.dau.cafeteria_portal.service.CanteenService;
import com.dau.cafeteria_portal.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/canteens")
public class AdminCanteenController {

    private final CanteenService canteenService;

    @Autowired
    private S3Service s3Service;

    public AdminCanteenController(CanteenService canteenService) {
        this.canteenService = canteenService;
    }

    // Filenames must be unique per upload so re-uploading a file with the
    // same original name (e.g. "photo.jpg") always gets a fresh key —
    // otherwise browsers/CDNs can keep showing a stale cached image even
    // after a successful re-upload.
    private String extensionOf(String originalFilename) {
        if (originalFilename == null) return "";
        int idx = originalFilename.lastIndexOf('.');
        return idx > 0 ? originalFilename.substring(idx) : "";
    }

    @PostMapping
    public ResponseEntity<CanteenDTO> addCanteen(@RequestBody CanteenDTO canteenDTO) {
        CanteenDTO created = canteenService.addCanteen(canteenDTO);
        return ResponseEntity.ok(created);
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
        String key = "canteen_images/" + id + "_" + UUID.randomUUID() + extensionOf(file.getOriginalFilename());
        String publicUrl = s3Service.uploadPublicFile(key, file.getBytes(), file.getContentType());
        canteenService.updateCanteenImage(id, publicUrl);
        return ResponseEntity.ok("Canteen image uploaded successfully");
    }

    @PostMapping("/{id}/upload-fssai")
    public ResponseEntity<String> uploadFssaiCertificate(@PathVariable Long id,
                                                         @RequestParam("file") MultipartFile file) throws IOException {
        String key = "canteen_certificates/" + id + "_" + UUID.randomUUID() + extensionOf(file.getOriginalFilename());
        String publicUrl = s3Service.uploadPublicFile(key, file.getBytes(), file.getContentType());
        canteenService.updateFssaiCertificate(id, publicUrl);
        return ResponseEntity.ok("FSSAI certificate uploaded successfully");
    }

    @PostMapping("/{id}/upload-menu")
    public ResponseEntity<String> uploadMenu(@PathVariable Long id,
                                             @RequestParam("file") MultipartFile file) throws IOException {
        String key = "canteen_menus/" + id + "_" + UUID.randomUUID() + extensionOf(file.getOriginalFilename());
        String publicUrl = s3Service.uploadPublicFile(key, file.getBytes(), file.getContentType());
        canteenService.updateMenuFile(id, publicUrl);
        return ResponseEntity.ok("Menu uploaded successfully");
    }
}
