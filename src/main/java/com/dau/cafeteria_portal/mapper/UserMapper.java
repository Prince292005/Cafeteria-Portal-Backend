package com.dau.cafeteria_portal.mapper;

import com.dau.cafeteria_portal.dto.UserDto;
import com.dau.cafeteria_portal.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Entity → DTO
    public UserDto mapToUserDto(User user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setStudentId(user.getStudentId());
        dto.setName(user.getName());
        dto.setEmailId(user.getEmailId());
        dto.setMobileNumber(user.getMobileNumber());
        dto.setUserRole(user.getUserRole());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    // DTO → Entity
    public User mapToUser(UserDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setStudentId(dto.getStudentId());
        user.setName(dto.getName());
        user.setEmailId(dto.getEmailId());
        user.setMobileNumber(dto.getMobileNumber());
        user.setUserRole(dto.getUserRole());
        user.setCreatedAt(dto.getCreatedAt());
        return user;
    }
}
