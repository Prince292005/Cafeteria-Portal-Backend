package com.dau.cafeteria_portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CanteenDTO {
    private Long id;
    private String canteenName;
    private String info;
    private String fssaiCertificateUrl;
    private String imageUrl;
    private String menuFilePath;

}
