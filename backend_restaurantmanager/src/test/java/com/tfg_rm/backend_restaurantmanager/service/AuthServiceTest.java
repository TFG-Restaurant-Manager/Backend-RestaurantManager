package com.tfg_rm.backend_restaurantmanager.service;

import com.tfg_rm.backend_restaurantmanager.dto.Role;
import com.tfg_rm.backend_restaurantmanager.entity.EmployeeEntity;
import com.tfg_rm.backend_restaurantmanager.entity.RestaurantEntity;
import com.tfg_rm.backend_restaurantmanager.entity.RoleEntity;
import com.tfg_rm.backend_restaurantmanager.repository.AuthEmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private AuthEmployeeRepository authEmployeeRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private AuthService authService;

    private EmployeeEntity employee;

    @BeforeEach
    void setUp() {
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(10L);

        employee = new EmployeeEntity();
        employee.setId(1L);
        employee.setCode("EMP01");
        employee.setPasswordHash("$2a$10$hashed");
        employee.setRoleName(RoleEntity.WAITER);
        employee.setRestaurant(restaurant);
        employee.setActive(true);
        employee.setName("Test Employee");
        employee.setEmail("emp01@test.com");
        employee.setStartDate(LocalDate.now());
    }

    // ── validateEmployeeAccess ─────────────────────────────────────

    @Test
    void validateEmployeeAccess_correctCredentials_returnsEmployeeId() {
        when(authEmployeeRepository.findByCode("EMP01")).thenReturn(Optional.of(employee));
        when(passwordEncoder.matches("plainPassword", "$2a$10$hashed")).thenReturn(true);

        assertThat(authService.validateEmployeeAccess("EMP01", "plainPassword")).isEqualTo(1L);
    }

    @Test
    void validateEmployeeAccess_wrongPassword_returnsMinusOne() {
        when(authEmployeeRepository.findByCode("EMP01")).thenReturn(Optional.of(employee));
        when(passwordEncoder.matches("wrong", "$2a$10$hashed")).thenReturn(false);

        assertThat(authService.validateEmployeeAccess("EMP01", "wrong")).isEqualTo(-1L);
    }

    @Test
    void validateEmployeeAccess_unknownCode_returnsMinusOne() {
        when(authEmployeeRepository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThat(authService.validateEmployeeAccess("UNKNOWN", "any")).isEqualTo(-1L);
    }

    // ── getEmployeeRole ────────────────────────────────────────────

    @Test
    void getEmployeeRole_waiter_returnsWaiterRole() {
        employee.setRoleName(RoleEntity.WAITER);
        when(authEmployeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertThat(authService.getEmployeeRole(1L)).isEqualTo(Role.WAITER);
    }

    @Test
    void getEmployeeRole_manager_returnsManagerRole() {
        employee.setRoleName(RoleEntity.MANAGER);
        when(authEmployeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertThat(authService.getEmployeeRole(1L)).isEqualTo(Role.MANAGER);
    }

    @Test
    void getEmployeeRole_cooker_returnsCookerRole() {
        employee.setRoleName(RoleEntity.COOKER);
        when(authEmployeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertThat(authService.getEmployeeRole(1L)).isEqualTo(Role.COOKER);
    }

    @Test
    void getEmployeeRole_admin_returnsAdminRole() {
        employee.setRoleName(RoleEntity.ADMIN);
        when(authEmployeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertThat(authService.getEmployeeRole(1L)).isEqualTo(Role.ADMIN);
    }

    @Test
    void getEmployeeRole_nonExistentEmployee_returnsNull() {
        when(authEmployeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThat(authService.getEmployeeRole(999L)).isNull();
    }

    // ── getEmployeeRestaurantId ────────────────────────────────────

    @Test
    void getEmployeeRestaurantId_existingEmployee_returnsRestaurantId() {
        when(authEmployeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        assertThat(authService.getEmployeeRestaurantId(1L)).isEqualTo(10L);
    }

    @Test
    void getEmployeeRestaurantId_nonExistentEmployee_returnsNull() {
        when(authEmployeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThat(authService.getEmployeeRestaurantId(999L)).isNull();
    }
}
