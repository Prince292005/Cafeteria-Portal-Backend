package com.dau.cafeteria_portal.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.dau.cafeteria_portal.service.S3Service;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${s3.presign.upload.expiryMinutes:5}")
    private long uploadExpiryMinutes;

    @Value("${s3.presign.download.expiryMinutes:5}")
    private long downloadExpiryMinutes;

    @Override
    public String generatePresignedUploadUrl(String key) {
        Date expiration = new Date(System.currentTimeMillis() + uploadExpiryMinutes * 60L * 1000L);

        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucket, key)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

       URL url = amazonS3.generatePresignedUrl(req);
       System.out.println("Generated S3 Upload URL:");
       System.out.println(url);
       return url.toString();
    }

    @Override
    public String generatePresignedDownloadUrl(String key) {
        Date expiration = new Date(System.currentTimeMillis() + downloadExpiryMinutes * 60L * 1000L);

        GeneratePresignedUrlRequest req = new GeneratePresignedUrlRequest(bucket, key)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);

        URL url = amazonS3.generatePresignedUrl(req);
        return url.toString();
    }

    @Override
    public String buildKeyForComplaint(Long complaintId, String originalFileName) {
        String ext = "";
        int idx = originalFileName.lastIndexOf('.');
        if (idx > 0) ext = originalFileName.substring(idx);

        String uuid = UUID.randomUUID().toString();
        return String.format("complaints/%d/%s%s", complaintId, uuid, ext);
    }

    @Override
    public File downloadToTempFile(String key) {
        try {
            // Extract filename from key: complaints/12/abc.png → abc.png
            String fileName = key.substring(key.lastIndexOf('/') + 1);

            // Create temp file with actual filename
            File temp = new File(System.getProperty("java.io.tmpdir"), fileName);

            S3Object s3Object = amazonS3.getObject(bucket, key);

            try (S3ObjectInputStream input = s3Object.getObjectContent();
                 FileOutputStream output = new FileOutputStream(temp)) {

                byte[] buffer = new byte[1024];
                int len;
                while ((len = input.read(buffer)) != -1) {
                    output.write(buffer, 0, len);
                }
            }

            return temp;

        } catch (Exception e) {
            throw new RuntimeException("Failed to download from S3: " + e.getMessage());
        }
    }

    @Override
    public String uploadPublicFile(String key, byte[] bytes, String contentType) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(bytes.length);
        if (contentType != null) metadata.setContentType(contentType);

        // No ACL here on purpose: this bucket has "Bucket Owner Enforced"
        // object ownership (AWS's current default for new buckets), which
        // rejects any request that tries to set an object ACL at all — that
        // was the exact cause of the 400 AccessControlListNotSupported
        // error. Objects stay private; FileProxyController serves them
        // back using the backend's own S3 credentials instead.
        PutObjectRequest request = new PutObjectRequest(
                bucket, key, new ByteArrayInputStream(bytes), metadata
        );

        amazonS3.putObject(request);
        // Return a stable backend-relative URL. The frontend already knows
        // how to resolve relative paths against the API base URL.
        return "/files/" + key;
    }

    @Override
    public S3ObjectData getObjectBytes(String key) {
        try (S3Object s3Object = amazonS3.getObject(bucket, key);
             S3ObjectInputStream input = s3Object.getObjectContent()) {

            byte[] bytes = input.readAllBytes();
            String contentType = s3Object.getObjectMetadata().getContentType();
            return new S3ObjectData(bytes, contentType);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch from S3: " + e.getMessage());
        }
    }

}
