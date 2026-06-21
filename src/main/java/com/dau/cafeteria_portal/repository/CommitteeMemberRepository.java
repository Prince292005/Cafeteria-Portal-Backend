package com.dau.cafeteria_portal.repository;

import com.dau.cafeteria_portal.entity.CommitteeMember;
import com.dau.cafeteria_portal.enums.CommitteeRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommitteeMemberRepository extends JpaRepository<CommitteeMember,Long> {

//    List<CommitteeMember> findByIsActiveTrueOrderByRoleAscOrderIndexAsc();
    List<CommitteeMember> findByIsActiveTrueOrderByRoleAscStudentIdAsc();

    List<CommitteeMember> findByRoleAndIsActiveTrue(CommitteeRole role);

}
