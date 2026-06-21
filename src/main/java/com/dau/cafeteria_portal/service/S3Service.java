package com.dau.cafeteria_portal.service;

import java.io.File;

public interface S3Service {
    String generatePresignedUploadUrl(String key);
    String generatePresignedDownloadUrl(String key);
    String buildKeyForComplaint(Long complaintId, String originalFileName);
    File downloadToTempFile(String key);
}
