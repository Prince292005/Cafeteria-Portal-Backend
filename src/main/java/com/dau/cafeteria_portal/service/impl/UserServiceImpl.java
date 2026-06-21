package com.dau.cafeteria_portal.service.impl;
import com.dau.cafeteria_portal.dto.ProfileResponseDTO;
import com.dau.cafeteria_portal.dto.UserDto;
import com.dau.cafeteria_portal.entity.User;
import com.dau.cafeteria_portal.mapper.UserMapper;
import com.dau.cafeteria_portal.repository.UserRepository;
import com.dau.cafeteria_portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        // Map DTO to Entity
        User user = userMapper.mapToUser(userDto);

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user
        User savedUser = userRepository.save(user);

        // Map back to DTO and return
        return userMapper.mapToUserDto(savedUser);
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String emailId) throws UsernameNotFoundException {
        User user = userRepository.findByEmailId(emailId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + emailId));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmailId())
                .password(user.getPassword())
                .roles(user.getUserRole().name().replace("ROLE_", ""))
                .build();
    }
    @Override
    public ProfileResponseDTO getProfile(String emailId) {
        User user = userRepository.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new ProfileResponseDTO(
                user.getEmailId(),
                user.getMobileNumber(),
                user.getName()
        );
    }
}
