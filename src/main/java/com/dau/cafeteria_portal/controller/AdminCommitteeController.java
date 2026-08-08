package com.dau.cafeteria_portal.controller;

import com.dau.cafeteria_portal.dto.MemberDTO;
import com.dau.cafeteria_portal.service.CommitteeService;
import com.dau.cafeteria_portal.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/admin/committee")
public class AdminCommitteeController {
    @Autowired
    private CommitteeService service;

    @Autowired
    private S3Service s3Service;

    @PostMapping
    public MemberDTO addMember(@RequestBody MemberDTO dto) {
        return service.addMember(dto);
    }

    @PutMapping("/{id}")
    public MemberDTO update(@PathVariable Long id, @RequestBody MemberDTO dto) {
        return service.updateMember(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteMember(id);
    }

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<String> uploadPhoto(@PathVariable Long id,
                                              @RequestParam("file") MultipartFile file) throws IOException {
        String originalFilename = Paths.get(
                file.getOriginalFilename() != null ? file.getOriginalFilename() : "photo"
        ).getFileName().toString();
        String ext = "";
        int idx = originalFilename.lastIndexOf('.');
        if (idx > 0) ext = originalFilename.substring(idx);

        String key = "committee_photos/" + id + "_" + UUID.randomUUID() + ext;
        String publicUrl = s3Service.uploadPublicFile(key, file.getBytes(), file.getContentType());

        service.updatePhotoUrl(id, publicUrl);
        return ResponseEntity.ok("Photo uploaded successfully");
    }

}
