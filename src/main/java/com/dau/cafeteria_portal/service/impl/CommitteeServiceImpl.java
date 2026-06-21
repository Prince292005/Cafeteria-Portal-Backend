package com.dau.cafeteria_portal.service.impl;

import com.dau.cafeteria_portal.dto.CommitteeResponseDTO;
import com.dau.cafeteria_portal.dto.MemberDTO;
import com.dau.cafeteria_portal.entity.CommitteeMember;
import com.dau.cafeteria_portal.enums.CommitteeRole;
import com.dau.cafeteria_portal.mapper.MemberMapper;
import com.dau.cafeteria_portal.repository.CommitteeMemberRepository;
import com.dau.cafeteria_portal.service.CommitteeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CommitteeServiceImpl implements CommitteeService {

    @Autowired
    private CommitteeMemberRepository repo;

    @Override
    public CommitteeResponseDTO getCommittee() {

        CommitteeResponseDTO dto = new CommitteeResponseDTO();

        for (CommitteeMember m : repo.findByIsActiveTrueOrderByRoleAscStudentIdAsc()) {

            switch (m.getRole()) {
                case FACULTY_MENTOR ->
                        dto.setFacultyMentor(MemberMapper.toDTO(m));

                case CONVENER ->
                        dto.setConvener(MemberMapper.toDTO(m));

                case DEPUTY_CONVENER ->
                        dto.setDeputyConvener(MemberMapper.toDTO(m));

                case CORE_MEMBER ->
                        dto.getCoreMembers().add(MemberMapper.toDTO(m));
            }
        }
        return dto;
    }

    @Override
    public MemberDTO addMember(MemberDTO dto) {
        CommitteeMember m = new CommitteeMember();
        fillEntity(m, dto);
        return MemberMapper.toDTO(repo.save(m));
    }

    @Override
    public MemberDTO updateMember(Long id, MemberDTO dto) {
        CommitteeMember m = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        fillEntity(m, dto);
        return MemberMapper.toDTO(repo.save(m));
    }

    @Override
    public void deleteMember(Long id) {
        CommitteeMember m = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        m.setActive(false);
        repo.delete(m);
    }


    private void fillEntity(CommitteeMember m, MemberDTO dto) {
        m.setName(dto.getName());
        m.setEmail(dto.getEmail());
        m.setDesignation(dto.getDesignation());
        m.setPhotoUrl(dto.getPhotoUrl());
        m.setRole(CommitteeRole.valueOf(dto.getRole()));
        m.setStudentId(dto.getStudentId());
    }

    // Implementation
    @Override
    public void updatePhotoUrl(Long id, String photoUrl) {
        CommitteeMember m = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        m.setPhotoUrl(photoUrl);
        repo.save(m);
    }

}
