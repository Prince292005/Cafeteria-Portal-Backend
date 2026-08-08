package com.dau.cafeteria_portal.service;

import java.io.File;

public interface S3Service {
    String generatePresignedUploadUrl(String key);
    String generatePresignedDownloadUrl(String key);
    String buildKeyForComplaint(Long complaintId, String originalFileName);
    File downloadToTempFile(String key);
    // Direct server-side upload for admin-managed assets (committee photos,
    // canteen images/certs/menus). Objects are stored privately (this
    // bucket has ACLs disabled) and served back through FileProxyController
    // using the backend's own S3 credentials, rather than via a public
    // bucket policy or object ACL.
    String uploadPublicFile(String key, byte[] bytes, String contentType);
    // Fetches raw object bytes + content type directly, for streaming
    // straight back to the browser without touching local disk at all.
    S3ObjectData getObjectBytes(String key);

    class S3ObjectData {
        public final byte[] bytes;
        public final String contentType;
        public S3ObjectData(byte[] bytes, String contentType) {
            this.bytes = bytes;
            this.contentType = contentType;
        }
    }
}
