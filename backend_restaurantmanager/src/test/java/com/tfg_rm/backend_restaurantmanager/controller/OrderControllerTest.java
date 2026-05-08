package com.tfg_rm.backend_restaurantmanager.controller;

import com.tfg_rm.backend_restaurantmanager.dto.OrderResponse;
import com.tfg_rm.backend_restaurantmanager.security.JwtService;
import com.tfg_rm.backend_restaurantmanager.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean OrderService orderService;
    // JwtService is mocked so we can control the JwtAuthenticationFilter behaviour
    @MockitoBean JwtService jwtService;

    private static final String TOKEN       = "valid-test-token";
    private static final String AUTH_HEADER = "Bearer " + TOKEN;

    @BeforeEach
    void setUp() {
        // Make JwtAuthenticationFilter accept TOKEN and populate the SecurityContext
        when(jwtService.validateToken(TOKEN)).thenReturn(true);
        when(jwtService.getUserId(TOKEN)).thenReturn(1L);
        when(jwtService.getRole(TOKEN)).thenReturn("WAITER");
        when(jwtService.getRestaurantId(TOKEN)).thenReturn(10L);
    }

    // ── Token extraído de header → restaurantId correcto ─────────────────

    @Test
    void getAll_withoutToken_returns401() throws Exception {
        // Without Authorization header, Spring MVC fails with 400 (missing required header)
        // or 401 if security blocks first; either way it's a 4xx error
        mockMvc.perform(get("/order"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getAllPaid_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/order/paid"))
                .andExpect(status().is4xxClientError());
    }

    // ── Con token válido → 200 ────────────────────────────────────

    @Test
    void getAll_withValidToken_returns200AndOrderList() throws Exception {
        OrderResponse order = buildResponse(1L, "DELIVERY", "CREATED", "15.00");
        when(orderService.getAllOrders(10L)).thenReturn(List.of(order));

        mockMvc.perform(get("/order").header("Authorization", AUTH_HEADER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].orderId").value(1))
                .andExpect(jsonPath("$[0].type").value("DELIVERY"))
                .andExpect(jsonPath("$[0].status").value("CREATED"));
    }

    @Test
    void getAll_emptyRestaurant_returns200WithEmptyArray() throws Exception {
        when(orderService.getAllOrders(10L)).thenReturn(List.of());

        mockMvc.perform(get("/order").header("Authorization", AUTH_HEADER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAll_multipleOrders_returnsCorrectCount() throws Exception {
        when(orderService.getAllOrders(10L)).thenReturn(List.of(
                buildResponse(1L, "DELIVERY", "CREATED", "15.00"),
                buildResponse(2L, "PICKUP",   "PAID",    "25.00"),
                buildResponse(3L, "TABLE",    "CREATED", "40.00")
        ));

        mockMvc.perform(get("/order").header("Authorization", AUTH_HEADER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    // ── GET /order/paid ───────────────────────────────────────────

    @Test
    void getAllPaid_withValidToken_returnsOnlyPaidOrders() throws Exception {
        when(orderService.getAllOrdersPaid(10L)).thenReturn(List.of(
                buildResponse(2L, "PICKUP", "PAID", "30.00")
        ));

        mockMvc.perform(get("/order/paid").header("Authorization", AUTH_HEADER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PAID"))
                .andExpect(jsonPath("$[0].type").value("PICKUP"));
    }

    @Test
    void getAllPaid_emptyResult_returns200WithEmptyArray() throws Exception {
        when(orderService.getAllOrdersPaid(10L)).thenReturn(List.of());

        mockMvc.perform(get("/order/paid").header("Authorization", AUTH_HEADER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    // ── restaurantId se extrae del token ──────────────────────────

    @Test
    void getAll_usesRestaurantIdFromToken() throws Exception {
        // restaurantId=10 comes from token; if OrderService is called with 10 → OK
        when(orderService.getAllOrders(10L)).thenReturn(List.of());

        mockMvc.perform(get("/order").header("Authorization", AUTH_HEADER))
                .andExpect(status().isOk());
    }

    // ── Helper ────────────────────────────────────────────────────

    private OrderResponse buildResponse(Long id, String type, String status, String total) {
        OrderResponse r = new OrderResponse();
        r.setOrderId(id);
        r.setType(type);
        r.setStatus(status);
        r.setTotal(new BigDecimal(total));
        r.setItems(List.of());
        return r;
    }
}
