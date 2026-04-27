package com.tfg_rm.backend_restaurantmanager.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tfg_rm.backend_restaurantmanager.dto.Role;
import com.tfg_rm.backend_restaurantmanager.entity.EmployeeEntity;
import com.tfg_rm.backend_restaurantmanager.entity.RoleEntity;
import com.tfg_rm.backend_restaurantmanager.repository.AuthEmployeeRepository;

import lombok.RequiredArgsConstructor;

/**
 * Java class used to manage the authentication logic of the application,
 * including checking user credentials and adding new clients to the system.
 */
@RequiredArgsConstructor
@Service
public class AuthService {

    /** The auth employee repository */
    private final AuthEmployeeRepository authEmployeeRepository;

    /** The password encoder */
    private final PasswordEncoder passwordEncoder;

    public Long validateEmployeeAccess(String code, String password) {
        Long id = -1L;
        Optional<EmployeeEntity> result = authEmployeeRepository.findByCode(code);

        if (result.isPresent() && passwordEncoder.matches(password, result.get().getPasswordHash())) {
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
