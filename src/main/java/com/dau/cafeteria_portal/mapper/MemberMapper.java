package com.dau.cafeteria_portal.mapper;


import com.dau.cafeteria_portal.dto.MemberDTO;
import com.dau.cafeteria_portal.entity.CommitteeMember;

public class MemberMapper {

    public static MemberDTO toDTO(CommitteeMember m) {
        MemberDTO dto = new MemberDTO();
        dto.setId(m.getId());
        dto.setName(m.getName());
        dto.setEmail(m.getEmail());
        dto.setDesignation(m.getDesignation());
        dto.setPhotoUrl(m.getPhotoUrl());
        dto.setRole(m.getRole().name());
        return dto;
    }
}
