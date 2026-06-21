package com.dau.cafeteria_portal.controller;

import com.dau.cafeteria_portal.dto.MemberDTO;
import com.dau.cafeteria_portal.service.CommitteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/admin/committee")
public class AdminCommitteeController {
    @Autowired
    private CommitteeService service;

    private static final String COMMITTEE_PHOTO_FOLDER = "C:/cafeteria-data/committee_photos/";

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
        String originalFilename = Paths.get(file.getOriginalFilename()).getFileName().toString();
        String filename = "committee_" + id + "_" + originalFilename;
        Path path = Paths.get(COMMITTEE_PHOTO_FOLDER + filename);
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());

        service.updatePhotoUrl(id, "/committee_photos/" + filename);
        return ResponseEntity.ok("Photo uploaded successfully");
    }

}
