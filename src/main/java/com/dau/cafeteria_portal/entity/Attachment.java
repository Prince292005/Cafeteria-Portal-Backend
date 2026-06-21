package com.dau.cafeteria_portal.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Data
@Schema(description = "Attachment entity that stores file metadata for complaints")
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique id", example = "1")
    private Long attachmentId;

    @Schema(description = "Name of the file", example = "complain.pdf")
    private String fileName;

    @Schema(description = "Public URL of the file", example = "https://s3.amazonaws.com/bucket/complain.pdf")
    private String fileUrl;

    @Schema(description = "Type of the file", example = "application/pdf")
    private String fileType;
}
