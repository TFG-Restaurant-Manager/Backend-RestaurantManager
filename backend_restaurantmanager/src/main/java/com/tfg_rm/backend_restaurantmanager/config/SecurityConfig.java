package com.tfg_rm.backend_restaurantmanager.config;

import com.tfg_rm.backend_restaurantmanager.shared.security.JwtAuthenticationFilter;
import com.tfg_rm.backend_restaurantmanager.shared.security.JwtService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration class for Spring Security. It defines the security filter chain and the JWT authentication filter.
 */
@Configuration
public class SecurityConfig {

    /** The JWT service for authentication. */
    private final JwtService jwtService;

    /**
    * Constructor for SecurityConfig.
    * @param jwtService the JWT service to be used for authentication
    */
    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    /**
     * Defines the security filter chain for the application. 
     * It configures the HTTP security to disable CSRF, set up authorization rules for different endpoints, and add the JWT authentication filter.
     * @param http the HTTP security configuration object
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs while configuring the security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF since we are using JWT for authentication
            // CSRF is not necessary for stateless APIs that use JWT, and it can be disabled to simplify the security configuration
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // /auth/** and /ws/** are open to everyone
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/ws/**").permitAll()
                /* Tests ----------------------------------------------------------------------------------------------------------- */
                // /a sólo clientes
                .requestMatchers("/a").hasAuthority("ROLE_CLIENTE")
                // /adios-json todos menos clientes
                .requestMatchers("/adios-json").not().hasAuthority("ROLE_CLIENTE")
                // solo camareros pueden pedir su restaurante
                .requestMatchers("/mi-restaurante").hasAuthority("ROLE_CAMARERO")
                /* ----- ----------------------------------------------------------------------------------------------------------- */
                // The rest of the routes require authentication, but no specific role is specified, which means that any authenticated user can access them.
                .anyRequest().authenticated()
            )
            // Add the JWT authentication filter before the UsernamePasswordAuthenticationFilter in the security filter chain
            .addFilterBefore(
                new JwtAuthenticationFilter(jwtService),
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
