package com.dau.cafeteria_portal.controller;

import com.dau.cafeteria_portal.service.CanteenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(com.dau.cafeteria_portal.controller.admin.AdminCanteenController.class)
class AdminCanteenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CanteenService canteenService;

    // ------------------------- ADMIN SUCCESS TESTS -------------------------

    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddCanteen_AsAdmin() throws Exception {
        String json = """
        {"canteenName":"Main Canteen","info":"Good"}
        """;

        mockMvc.perform(post("/admin/canteens")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Canteen added successfully!"));

        verify(canteenService).addCanteen(any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testUpdateCanteen_AsAdmin() throws Exception {
        String json = """
        {"canteenName":"Updated","info":"Updated Info"}
        """;

        mockMvc.perform(put("/admin/canteens/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().string("Canteen updated successfully!"));

        verify(canteenService).updateCanteen(eq(1L), any());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void testDeleteCanteen_AsAdmin() throws Exception {
        mockMvc.perform(delete("/admin/canteens/1")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("Canteen deleted successfully!"));

        verify(canteenService).deleteCanteen(1L);
    }

    // ------------------------- USER FORBIDDEN TESTS -------------------------

    @Test
    @WithMockUser(roles = "USER")
    void testAddCanteen_Forbidden() throws Exception {
        String json = """
        {"canteenName":"Main Canteen","info":"Good"}
        """;

        mockMvc.perform(post("/admin/canteens")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testUpdateCanteen_Forbidden() throws Exception {
        String json = """
        {"canteenName":"Updated","info":"Updated Info"}
        """;

        mockMvc.perform(put("/admin/canteens/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void testDeleteCanteen_Forbidden() throws Exception {

        mockMvc.perform(delete("/admin/canteens/1")
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }
}
