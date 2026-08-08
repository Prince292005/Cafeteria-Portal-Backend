package com.dau.cafeteria_portal.service;

import com.dau.cafeteria_portal.dto.ProfileResponseDTO;
import com.dau.cafeteria_portal.dto.UserDto;

public interface UserService {
    UserDto createUser(UserDto userDto);
    ProfileResponseDTO getProfile(String emailId);
}
