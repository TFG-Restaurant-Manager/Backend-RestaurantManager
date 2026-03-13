package com.tfg_rm.backend_restaurantmanager.auth.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tfg_rm.backend_restaurantmanager.auth.repository.AuthRepository;
import com.tfg_rm.backend_restaurantmanager.auth.repository.projection.ClientLoginProjection;
import com.tfg_rm.backend_restaurantmanager.shared.entity.Client;

import lombok.RequiredArgsConstructor;

/**
 * Java class used to manage the authentication logic of the application,
 * including checking user credentials and adding new clients to the system.
 */
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

        if (result.isPresent() && passwordEncoder.matches(password, result.get().getPasswordHash())) {
            id = result.get().getId();
        }

        return id;
    }

    public Long addClient(Long restaurantId, String email, String password) {
        Long id = -1L;

        if(password.length() > 6) {

            String passwordHash = passwordEncoder.encode(password);

            Client client = new Client();
            client.setRestaurantId(restaurantId);
            client.setEmail(email);
            client.setPasswordHash(passwordHash);
            authRepository.save(client);

            id = client.getId();
        }
        return id;
    }
}
