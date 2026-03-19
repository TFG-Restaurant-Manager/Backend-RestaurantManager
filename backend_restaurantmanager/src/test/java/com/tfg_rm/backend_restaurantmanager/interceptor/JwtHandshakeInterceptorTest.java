package com.tfg_rm.backend_restaurantmanager.interceptor;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;

import com.tfg_rm.backend_restaurantmanager.auth.dto.Role;
import com.tfg_rm.backend_restaurantmanager.shared.security.JwtService;
import com.tfg_rm.backend_restaurantmanager.websocket.interceptor.JwtHandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtHandshakeInterceptorTest {
        private final JwtService jwtService =
            new JwtService("clave_super_larga_y_segura_para_firmar_tokens_123456");

        @Test
        void shouldAllowValidToken() {

                String token = jwtService.generateToken(
                                1,
                                5,
                                Role.ADMIN);

                JwtHandshakeInterceptor interceptor = new JwtHandshakeInterceptor(jwtService);

                HttpServletRequest servletRequest = Mockito.mock(HttpServletRequest.class);
                Mockito.when(servletRequest.getHeader("Authorization"))
                                .thenReturn("Bearer " + token);

                ServletServerHttpRequest request = new ServletServerHttpRequest(servletRequest);

                Map<String, Object> attributes = new HashMap<>();

                boolean result = interceptor.beforeHandshake(
                                request,
                                Mockito.mock(org.springframework.http.server.ServerHttpResponse.class),
                                Mockito.mock(WebSocketHandler.class),
                                attributes);

                assertTrue(result);
                assertEquals(5L, attributes.get("restaurantId"));
                assertEquals("ADMIN", attributes.get("role"));
        }
}