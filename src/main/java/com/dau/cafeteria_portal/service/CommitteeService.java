package com.dau.cafeteria_portal.service;

import com.dau.cafeteria_portal.dto.CommitteeResponseDTO;
import com.dau.cafeteria_portal.dto.MemberDTO;

public interface CommitteeService {

    CommitteeResponseDTO getCommittee();
    MemberDTO addMember(MemberDTO dto);
    MemberDTO updateMember(Long id, MemberDTO dto);
    void deleteMember(Long id);
    void updatePhotoUrl(Long id, String photoUrl);

}
