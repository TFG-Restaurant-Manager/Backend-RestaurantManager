package com.tfg_rm.backend_restaurantmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg_rm.backend_restaurantmanager.auth.controller.AuthController;
import com.tfg_rm.backend_restaurantmanager.auth.dto.ClientLoginRequest;
import com.tfg_rm.backend_restaurantmanager.auth.dto.EmployeeLoginRequest;
import com.tfg_rm.backend_restaurantmanager.auth.service.AuthService;
import com.tfg_rm.backend_restaurantmanager.shared.security.JwtService;

import jakarta.servlet.ServletException;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private final JwtService jwtService = new JwtService("clave_super_larga_y_segura_para_firmar_tokens_123456");
    private final AuthService authService = null;

    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(jwtService, authService)).build();

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void clientShouldLoginSuccessfully() throws Exception {

        ClientLoginRequest request = new ClientLoginRequest();
        request.setEmail("test@test.com");
        request.setPassword("1234");
        request.setRestaurantId(5L);

        mockMvc.perform(post("/auth/clientLogin")
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void clientShouldFailLogin() throws Exception {

        ClientLoginRequest request = new ClientLoginRequest();
        request.setEmail("wrong@test.com");
        request.setPassword("1234");

        assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/auth/clientLogin")
                    .contentType("application/json")
                    .content(mapper.writeValueAsString(request)));
        });
    }

    @Test
    void employeeShouldLoginSuccessfully() throws Exception {
        EmployeeLoginRequest request = new EmployeeLoginRequest();
        request.setDni("12345678");
        request.setPassword("1234");

        mockMvc.perform(post("/auth/employeeLogin")
                .contentType("application/json")
                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeId").exists())
                .andExpect(jsonPath("$.restaurantIds").exists());
    }

    @Test
    void employeeShouldFailLogin() throws Exception {

        EmployeeLoginRequest request = new EmployeeLoginRequest();
        request.setDni("222");
        request.setPassword("1020");

        assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/auth/employeeLogin")
                    .contentType("application/json")
                    .content(mapper.writeValueAsString(request)));
        });
    }
}
