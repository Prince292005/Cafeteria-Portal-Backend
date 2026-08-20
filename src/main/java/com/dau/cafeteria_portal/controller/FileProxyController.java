package com.dau.cafeteria_portal.controller;

import com.dau.cafeteria_portal.service.S3Service;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

@RestController
public class FileProxyController {

    private final S3Service s3Service;

    public FileProxyController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    // Matches /files/committee_photos/xyz.jpg, /files/canteen_images/xyz.jpg,
    // etc. The wildcard remainder of the path becomes the exact S3 key that
    // was stored when the file was uploaded.
    @GetMapping("/files/**")
    public ResponseEntity<byte[]> serveFile(HttpServletRequest request) {
        String fullPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String key = fullPath.substring("/files/".length());

        S3Service.S3ObjectData data = s3Service.getObjectBytes(key);

        MediaType mediaType;
        try {
            mediaType = data.contentType != null
                    ? MediaType.parseMediaType(data.contentType)
                    : MediaType.APPLICATION_OCTET_STREAM;
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                // Public assets rarely change once uploaded — cache for a day
                // client-side to avoid re-fetching through the backend on
                // every page view.
                .header(HttpHeaders.CACHE_CONTROL, "public, max-age=86400")
                .body(data.bytes);
    }
}
