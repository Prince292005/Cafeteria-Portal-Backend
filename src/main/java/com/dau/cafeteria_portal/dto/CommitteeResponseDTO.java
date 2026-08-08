package com.dau.cafeteria_portal.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommitteeResponseDTO {
    private MemberDTO facultyMentor;
    private MemberDTO convener;
    private MemberDTO deputyConvener;
    private List<MemberDTO> coreMembers = new ArrayList<>();
}