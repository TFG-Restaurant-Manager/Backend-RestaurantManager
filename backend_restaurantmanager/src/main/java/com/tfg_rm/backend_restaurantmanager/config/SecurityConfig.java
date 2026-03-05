package com.tfg_rm.backend_restaurantmanager.config;

import com.tfg_rm.backend_restaurantmanager.security.JwtAuthenticationFilter;
import com.tfg_rm.backend_restaurantmanager.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtService jwtService;

    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // desactiva CSRF
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/ws/**").permitAll()
                    //.requestMatchers("/hola", "/hola-json").authenticated() // Protege estos endpoints
                    .anyRequest().authenticated() // Todos los demás requieren autenticación
                )
                .addFilterBefore(
                    new JwtAuthenticationFilter(jwtService),
                    UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
