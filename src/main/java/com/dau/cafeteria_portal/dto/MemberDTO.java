package com.dau.cafeteria_portal.dto;


import lombok.Data;

@Data
public class MemberDTO {
    private Long id;
    private String name;
    private String email;
    private String designation;
    private String photoUrl;
    private String role;
    private Long studentId;
}
