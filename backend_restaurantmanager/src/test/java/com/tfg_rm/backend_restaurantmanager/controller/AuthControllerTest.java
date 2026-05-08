package com.tfg_rm.backend_restaurantmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg_rm.backend_restaurantmanager.dto.Role;
import com.tfg_rm.backend_restaurantmanager.security.JwtService;
import com.tfg_rm.backend_restaurantmanager.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Both beans are needed: AuthService for business logic, JwtService for
    // SecurityConfig to construct the JwtAuthenticationFilter
    @MockitoBean AuthService authService;
    @MockitoBean JwtService jwtService;

    // ── Login correcto ─────────────────────────────────────────────

    @Test
    void employeeLogin_validCredentials_returns200WithTokenAndRole() throws Exception {
        when(authService.validateEmployeeAccess("EMP01", "pass123")).thenReturn(1L);
        when(authService.getEmployeeRole(1L)).thenReturn(Role.WAITER);
        when(authService.getEmployeeRestaurantId(1L)).thenReturn(10L);
        when(jwtService.generateToken(1L, 10L, Role.WAITER)).thenReturn("mocked.jwt.token");

        mockMvc.perform(post("/auth/employeeLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("code", "EMP01", "password", "pass123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked.jwt.token"))
                .andExpect(jsonPath("$.role").value("WAITER"));
    }

    @Test
    void employeeLogin_validCredentials_tokenFieldIsPresent() throws Exception {
        when(authService.validateEmployeeAccess(anyString(), anyString())).thenReturn(1L);
        when(authService.getEmployeeRole(1L)).thenReturn(Role.MANAGER);
        when(authService.getEmployeeRestaurantId(1L)).thenReturn(5L);
        when(jwtService.generateToken(1L, 5L, Role.MANAGER)).thenReturn("real.jwt.token");

        mockMvc.perform(post("/auth/employeeLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"EMP01\",\"password\":\"pass123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    // ── Credenciales inválidas ─────────────────────────────────────

    @Test
    void employeeLogin_wrongPassword_returns401() throws Exception {
        when(authService.validateEmployeeAccess(anyString(), anyString())).thenReturn(-1L);

        mockMvc.perform(post("/auth/employeeLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"EMP01\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void employeeLogin_unknownUser_returns401() throws Exception {
        when(authService.validateEmployeeAccess(anyString(), anyString())).thenReturn(-1L);

        mockMvc.perform(post("/auth/employeeLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"GHOST\",\"password\":\"any\"}"))
                .andExpect(status().isUnauthorized());
    }

    // ── Acceso sin autenticación a endpoint protegido ──────────────

    @Test
    void protectedEndpoint_unknownRoute_returns4xx() throws Exception {
        // /order is not loaded in this WebMvcTest slice (only AuthController)
        // so it returns 404, which is still a 4xx client error
        mockMvc.perform(post("/order"))
                .andExpect(status().is4xxClientError());
    }

    // ── Body vacío / malformado ────────────────────────────────────

    @Test
    void employeeLogin_emptyBody_returns4xx() throws Exception {
        // Empty body means code=null, password=null → invalid credentials
        when(authService.validateEmployeeAccess(null, null)).thenReturn(-1L);

        mockMvc.perform(post("/auth/employeeLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void employeeLogin_missingContentType_returns4xx() throws Exception {
        mockMvc.perform(post("/auth/employeeLogin")
                        .content("not json"))
                .andExpect(status().is4xxClientError());
    }
}
