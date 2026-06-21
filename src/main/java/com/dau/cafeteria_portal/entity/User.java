package com.dau.cafeteria_portal.entity;

import com.dau.cafeteria_portal.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.nio.file.FileStore;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "user_id")
    private String studentId;

    @Column(nullable = false)
    private String name;

//    @Id
    @Column(nullable = false,unique = true)
    private String emailId;

    @Column(nullable = false,unique = true)
    private Long mobileNumber;

    @Column(nullable = false)
    private String password;

//    private Role userRole;
    private Role userRole;
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate(){
        this.createdAt=LocalDateTime.now();
    }
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Complaint> complaints;

}
