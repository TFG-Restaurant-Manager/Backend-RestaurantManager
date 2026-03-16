package com.tfg_rm.backend_restaurantmanager.auth.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tfg_rm.backend_restaurantmanager.auth.dto.Role;
import com.tfg_rm.backend_restaurantmanager.auth.repository.AuthClientRepository;
import com.tfg_rm.backend_restaurantmanager.auth.repository.AuthEmployeeRepository;
import com.tfg_rm.backend_restaurantmanager.auth.repository.EmployeeRestaurantRepository;
import com.tfg_rm.backend_restaurantmanager.auth.repository.projection.ClientLoginProjection;
import com.tfg_rm.backend_restaurantmanager.shared.entity.Client;
import com.tfg_rm.backend_restaurantmanager.shared.entity.Employee;
import com.tfg_rm.backend_restaurantmanager.shared.entity.EmployeeRestaurant;

import lombok.RequiredArgsConstructor;

/**
 * Java class used to manage the authentication logic of the application,
 * including checking user credentials and adding new clients to the system.
 */
@RequiredArgsConstructor
@Service
public class AuthService {

    /** The auth repository */
    private final AuthClientRepository authClientRepository;
    private final AuthEmployeeRepository authEmployeeRepository;
    private final EmployeeRestaurantRepository employeeRestaurantRepository;

    /** The password encoder */
    private final PasswordEncoder passwordEncoder;

    public Long checkCredentials(Long restaurantId, String email, String password) {
        Long id = -1L;
        Optional<ClientLoginProjection> result = authClientRepository.findByRestaurantIdAndEmail(restaurantId, email);

        if (result.isPresent() && passwordEncoder.matches(password, result.get().getPasswordHash())) {
            id = result.get().getId();
        }

        return id;
    }

    public Long addClient(Long restaurantId, String email, String password) {
        Long id = -1L;

        if (password.length() > 6) {
            String passwordHash = passwordEncoder.encode(password);

            Client client = new Client();
            client.setRestaurantId(restaurantId);
            client.setEmail(email);
            client.setPasswordHash(passwordHash);
            authClientRepository.save(client);

            id = client.getId();
        }
        return id;
    }

    public Role getEmployeeRole(Long employeeId, Long restaurantId) {
        return employeeRestaurantRepository
            .findByEmployeeDniAndRestaurantId(employeeId, restaurantId)
            .map(EmployeeRestaurant::getRoleId)
            .map(this::mapRoleIdToRole)
            .orElse(null);
    }

    public Long getEmployeeId(String dni, String password) {
        Long employeeId = Long.parseLong(dni);
        Optional<Employee> employee = authEmployeeRepository.findById(employeeId);
        if (employee.isPresent() && passwordEncoder.matches(password, employee.get().getPasswordHash())) {
            return employee.get().getId();
        }
        return -1L;
    }

    public List<String> getEmployeeRestaurants(Long employeeId) {
        return employeeRestaurantRepository.findByEmployeeDniAndActiveTrue(employeeId).stream()
            .map(er -> String.valueOf(er.getRestaurantId()))
            .collect(Collectors.toList());
    }

    public boolean validateEmployeeAccess(String dni, String password, Long employeeId, Long restaurantId) {
        if (dni == null || password == null || employeeId == null || restaurantId == null) {
            return false;
        }

        // Authenticate password
        Optional<Employee> employeeOpt = authEmployeeRepository.findById(employeeId);
        if (employeeOpt.isEmpty() || !passwordEncoder.matches(password, employeeOpt.get().getPasswordHash())) {
            return false;
        }

        // Verify user has an active assignment to the requested restaurant
        return employeeRestaurantRepository
            .findByEmployeeDniAndRestaurantId(employeeId, restaurantId)
            .map(EmployeeRestaurant::getActive)
            .orElse(false);
    }

    private Role mapRoleIdToRole(Integer roleId) {
        // Match the IDs from the DB script: 1=MANAGER, 2=WAITER, 3=COOKER, 4=ADMIN
        return switch (roleId) {
            case 1 -> Role.MANAGER;
            case 2 -> Role.WAITER;
            case 3 -> Role.KITCHEN;
            case 4 -> Role.ADMIN;
            case null -> null;
            default -> null;
        };
    }
}
