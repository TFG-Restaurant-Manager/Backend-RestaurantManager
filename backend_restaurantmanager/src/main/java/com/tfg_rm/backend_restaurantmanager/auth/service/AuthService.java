package com.tfg_rm.backend_restaurantmanager.auth.service;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tfg_rm.backend_restaurantmanager.auth.dto.Role;
import com.tfg_rm.backend_restaurantmanager.auth.repository.AuthClientRepository;
import com.tfg_rm.backend_restaurantmanager.auth.repository.AuthEmployeeRepository;
import com.tfg_rm.backend_restaurantmanager.auth.repository.projection.ClientLoginProjection;
import com.tfg_rm.backend_restaurantmanager.employee.dto.EmployeeRegisterRequest;
import com.tfg_rm.backend_restaurantmanager.shared.entity.ClientEntity;
import com.tfg_rm.backend_restaurantmanager.shared.entity.EmployeeEntity;
import com.tfg_rm.backend_restaurantmanager.shared.entity.RoleEntity;

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

    /** The password encoder */
    private final PasswordEncoder passwordEncoder;

    public Integer checkCredentials(Integer restaurantId, String email, String password) {
        Integer id = -1;
        Optional<ClientLoginProjection> result = authClientRepository.findByRestaurantIdAndEmail(restaurantId, email);

        if (result.isPresent() && passwordEncoder.matches(password, result.get().getPasswordHash())) {
            id = result.get().getId();
        }

        return id;
    }

    public Integer addClient(Integer restaurantId, String email, String password) {
        Integer id = -1;

        if (password.length() > 6) {
            String passwordHash = passwordEncoder.encode(password);

            ClientEntity client = new ClientEntity();
            client.setRestaurantId(restaurantId);
            client.setEmail(email);
            client.setPasswordHash(passwordHash);
            authClientRepository.save(client);

            id = client.getId();
        }
        return id;
    }

    public Integer validateEmployeeAccess(String code, String password) {
        Integer id = -1;
        Optional<EmployeeEntity> result = authEmployeeRepository.findByCode(code);

        if (result.isPresent() && (result.get().getPasswordHash().equals(password))) { //passwordEncoder.matches(password, result.get().getPasswordHash()
            id = result.get().getId();
        }

        return id;
    }

    public Role getEmployeeRole(Integer employeeId) {
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

    public Integer getEmployeeRestaurantId(Integer employeeId) {
        Integer restaurantId = null;
        Optional<EmployeeEntity> result = authEmployeeRepository.findById(employeeId);

        if (result.isPresent()) {
            restaurantId = result.get().getRestaurant().getId();
        }

        return restaurantId;
    }
}
