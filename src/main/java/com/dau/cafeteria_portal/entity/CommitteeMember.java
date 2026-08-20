package com.dau.cafeteria_portal.entity;


import com.dau.cafeteria_portal.enums.CommitteeRole;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "committee_members")
public class CommitteeMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String designation;
    private String photoUrl;

    @Enumerated(EnumType.STRING)
    private CommitteeRole role;

    private Long studentId;

    private boolean isActive = true;
}
