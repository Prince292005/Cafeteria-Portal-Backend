package com.dau.cafeteria_portal;

import com.dau.cafeteria_portal.config.SecurityConfig;
import com.dau.cafeteria_portal.controller.admin.AdminCanteenController;
import com.dau.cafeteria_portal.service.CanteenService;
import com.dau.cafeteria_portal.util.JWTUtil;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({AdminCanteenController.class, SecurityConfig.class})
public class AdminSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWTUtil jwtUtil;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    // ❗ REQUIRED MOCK BEAN (fixes the error)
    @MockBean
    private CanteenService canteenService;

    private final String USER_TOKEN = "USER_TOKEN";
    private final String ADMIN_TOKEN = "ADMIN_TOKEN";
    private final String INVALID_TOKEN = "INVALID";
    private final String EXPIRED_TOKEN = "EXPIRED";

    void mockUserDetailsServices() {

        UserDetails user = User.withUsername("user")
                .password("pass")
                .roles("USER")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password("pass")
                .roles("ADMIN")
                .build();

        Mockito.when(userDetailsService.loadUserByUsername("user")).thenReturn(user);
        Mockito.when(userDetailsService.loadUserByUsername("admin")).thenReturn(admin);
    }

    @Test
    void accessAdmin_NoToken_ShouldReturn403() throws Exception {
        mockMvc.perform(put("/admin/canteens/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void accessAdmin_InvalidToken_ShouldReturn403() throws Exception {
        Mockito.when(jwtUtil.validateToken(INVALID_TOKEN)).thenReturn(false);

        mockMvc.perform(put("/admin/canteens/1")
                        .header("Authorization", "Bearer " + INVALID_TOKEN))
                .andExpect(status().isForbidden());
    }

    @Test
    void accessAdmin_ExpiredToken_ShouldReturn403() throws Exception {
        Mockito.when(jwtUtil.validateToken(EXPIRED_TOKEN)).thenReturn(false);

        mockMvc.perform(put("/admin/canteens/1")
                        .header("Authorization", "Bearer " + EXPIRED_TOKEN))
                .andExpect(status().isForbidden());
    }

    @Test
    void accessAdmin_UserToken_ShouldReturn403() throws Exception {
        mockUserDetailsServices();

        Mockito.when(jwtUtil.validateToken(USER_TOKEN)).thenReturn(true);
        Mockito.when(jwtUtil.getUsernameFromToken(USER_TOKEN)).thenReturn("user");
        Mockito.when(jwtUtil.getRolesFromToken(USER_TOKEN)).thenReturn(List.of("ROLE_USER"));

        mockMvc.perform(put("/admin/canteens/1")
                        .header("Authorization", "Bearer " + USER_TOKEN))
                .andExpect(status().isForbidden());
    }

    @Test
    void accessAdmin_AdminToken_ShouldReturn200() throws Exception {
        mockUserDetailsServices();

        Mockito.when(jwtUtil.validateToken(ADMIN_TOKEN)).thenReturn(true);
        Mockito.when(jwtUtil.getUsernameFromToken(ADMIN_TOKEN)).thenReturn("admin");
        Mockito.when(jwtUtil.getRolesFromToken(ADMIN_TOKEN)).thenReturn(List.of("ROLE_ADMIN"));

        mockMvc.perform(put("/admin/canteens/2")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Canteen\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void accessUserEndpoint_WithUserRole_ShouldReturn200() throws Exception {
        mockUserDetailsServices();

        Mockito.when(jwtUtil.validateToken(USER_TOKEN)).thenReturn(true);
        Mockito.when(jwtUtil.getUsernameFromToken(USER_TOKEN)).thenReturn("user");
        Mockito.when(jwtUtil.getRolesFromToken(USER_TOKEN)).thenReturn(List.of("ROLE_USER"));

        mockMvc.perform(get("/user/profile")
                        .header("Authorization", "Bearer " + USER_TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    void accessUserEndpoint_WithAdminRole_ShouldReturn403() throws Exception {
        mockUserDetailsServices();

        Mockito.when(jwtUtil.validateToken(ADMIN_TOKEN)).thenReturn(true);
        Mockito.when(jwtUtil.getUsernameFromToken(ADMIN_TOKEN)).thenReturn("admin");
        Mockito.when(jwtUtil.getRolesFromToken(ADMIN_TOKEN)).thenReturn(List.of("ROLE_ADMIN"));

        mockMvc.perform(get("/user/profile")
                        .header("Authorization", "Bearer " + ADMIN_TOKEN))
                .andExpect(status().isForbidden());
    }
}
