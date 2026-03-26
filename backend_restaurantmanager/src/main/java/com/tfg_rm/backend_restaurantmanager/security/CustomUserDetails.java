package com.tfg_rm.backend_restaurantmanager.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private Integer userId;
    private Integer restaurantId;
    private String role;
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convertimos el role a GrantedAuthority
        return List.of(() -> "ROLE_" + role);
    }

    @Override
    public String getPassword() {
        return null; // no usamos aquí
    }

    @Override
    public String getUsername() {
        return userId.toString();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

}