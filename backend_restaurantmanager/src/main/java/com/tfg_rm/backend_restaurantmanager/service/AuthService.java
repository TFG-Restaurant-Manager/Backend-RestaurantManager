package com.tfg_rm.backend_restaurantmanager.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tfg_rm.backend_restaurantmanager.dto.Role;
import com.tfg_rm.backend_restaurantmanager.entity.ClientEntity;
import com.tfg_rm.backend_restaurantmanager.entity.EmployeeEntity;
import com.tfg_rm.backend_restaurantmanager.entity.RestaurantEntity;
import com.tfg_rm.backend_restaurantmanager.entity.RoleEntity;
import com.tfg_rm.backend_restaurantmanager.exception.NotFoundException;
import com.tfg_rm.backend_restaurantmanager.repository.AuthClientRepository;
import com.tfg_rm.backend_restaurantmanager.repository.AuthEmployeeRepository;
import com.tfg_rm.backend_restaurantmanager.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

/**
 * Java class used to manage the authentication logic of the application,
 * including checking user credentials and adding new clients to the system.
 */
@RequiredArgsConstructor
@Service
public class AuthService {

    /** The auth client repository */
    private final AuthClientRepository authClientRepository;
    /** The auth employee repository */
    private final AuthEmployeeRepository authEmployeeRepository;
    /** The restaurant */
    private final RestaurantRepository restaurantRepository;

    /** The password encoder */
    private final PasswordEncoder passwordEncoder;

    public Long checkCredentials(Long restaurantId, String email, String password) {
        Long id = -1L;
        Optional<ClientEntity> result = authClientRepository.findByRestaurantIdAndEmail(restaurantId, email);

        if (result.isPresent() && passwordEncoder.matches(password, result.get().getPasswordHash())) {
            id = result.get().getId();
        }

        return id;
    }

    public Long addClient(Long restaurantId, String email, String password) {
        Long id = -1L;

        if (password.length() > 6) {
            String passwordHash = passwordEncoder.encode(password);
            ClientEntity client = new ClientEntity();
            RestaurantEntity restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));
            client.setRestaurant(restaurant);
            client.setEmail(email);
            client.setPasswordHash(passwordHash);
            authClientRepository.save(client);

            id = client.getId();
        }
        return id;
    }

    public Long validateEmployeeAccess(String code, String password) {
        Long id = -1L;
        Optional<EmployeeEntity> result = authEmployeeRepository.findByCode(code);

        if (result.isPresent() && (result.get().getPasswordHash().equals(password))) { //passwordEncoder.matches(password, result.get().getPasswordHash()
            id = result.get().getId();
        }

        return id;
    }

    public Role getEmployeeRole(Long employeeId) {
        Role role = null;
        Optional<EmployeeEntity> result = authEmployeeRepository.findById(employeeId);

        if (result.isPresent()) {
            RoleEntity roleEntity = result.get().getRoleName();
            role = switch (roleEntity) {
                case MANAGER -> Role.MANAGER;
                case WAITER -> Role.WAITER;
                case COOKER -> Role.COOKER;
                case ADMIN -> Role.ADMIN;
            };
        }

        return role;
    }

    public Long getEmployeeRestaurantId(Long employeeId) {
        Long restaurantId = null;
        Optional<EmployeeEntity> result = authEmployeeRepository.findById(employeeId);

        if (result.isPresent()) {
            restaurantId = result.get().getRestaurant().getId();
        }

        return restaurantId;
    }
}
