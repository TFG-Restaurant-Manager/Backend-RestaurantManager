package com.tfg_rm.backend_restaurantmanager.auth.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tfg_rm.backend_restaurantmanager.auth.repository.AuthRepository;
import com.tfg_rm.backend_restaurantmanager.auth.repository.projection.ClientLoginProjection;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {

    /** The auth repository */
    private final AuthRepository authRepository;

    /** The password encoder */
    private final PasswordEncoder passwordEncoder;

    public Long checkCredentials(Long restaurantId, String email, String password) {
        Long id = -1L;
        Optional<ClientLoginProjection> result = authRepository.findByRestaurantIdAndEmail(restaurantId, email);

        if (result.isPresent() && passwordEncoder.matches(password, result.get().getPassword())) {
            id = result.get().getId();
        }

        return id;
    }
}
