package com.tfg_rm.backend_restaurantmanager.repository;

import com.tfg_rm.backend_restaurantmanager.entity.EmployeeEntity;
import com.tfg_rm.backend_restaurantmanager.entity.RestaurantEntity;
import com.tfg_rm.backend_restaurantmanager.entity.RoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Sql("/test-employee-defaults.sql")
class AuthEmployeeRepositoryTest {

    @Autowired
    TestEntityManager em;
    @Autowired
    AuthEmployeeRepository authEmployeeRepository;

    private RestaurantEntity restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new RestaurantEntity();
        restaurant.setPrefix("TST");
        restaurant.setName("Test Restaurant");
        restaurant.setEmail("test@restaurant.com");
        restaurant.setPhone("600000000");
        restaurant.setAddress("Test Address 1");
        restaurant.setCreatedAt(LocalDateTime.now());
        em.persist(restaurant);
        em.flush();
    }

    // ── findByCode ─────────────────────────────────────────────────

    @Test
    void findByCode_existingCode_returnsEmployee() {
        persistEmployee("EMP01", RoleEntity.WAITER);
        em.flush();

        Optional<EmployeeEntity> result = authEmployeeRepository.findByCode("EMP01");

        assertThat(result).isPresent();
        assertThat(result.get().getCode()).isEqualTo("EMP01");
    }

    @Test
    void findByCode_nonExistentCode_returnsEmpty() {
        Optional<EmployeeEntity> result = authEmployeeRepository.findByCode("NOTEXISTS");
        assertThat(result).isEmpty();
    }

    @Test
    void findByCode_returnsCorrectEmployeeAmongMultiple() {
        persistEmployee("EMP01", RoleEntity.WAITER);
        persistEmployee("EMP02", RoleEntity.MANAGER);
        em.flush();

        Optional<EmployeeEntity> result = authEmployeeRepository.findByCode("EMP02");

        assertThat(result).isPresent();
        assertThat(result.get().getCode()).isEqualTo("EMP02");
        assertThat(result.get().getRoleName()).isEqualTo(RoleEntity.MANAGER);
    }

    @Test
    void findByCode_includesPasswordHash() {
        persistEmployee("EMP01", RoleEntity.WAITER);
        em.flush();

        Optional<EmployeeEntity> result = authEmployeeRepository.findByCode("EMP01");

        assertThat(result).isPresent();
        assertThat(result.get().getPasswordHash()).isNotBlank();
    }

    @Test
    void findByCode_includesRestaurantAssociation() {
        persistEmployee("EMP01", RoleEntity.WAITER);
        em.flush();

        Optional<EmployeeEntity> result = authEmployeeRepository.findByCode("EMP01");

        assertThat(result).isPresent();
        assertThat(result.get().getRestaurant().getId()).isEqualTo(restaurant.getId());
    }

    // ── Helper ────────────────────────────────────────────────────

    private EmployeeEntity persistEmployee(String code, RoleEntity role) {
        EmployeeEntity emp = new EmployeeEntity();
        emp.setCode(code);
        emp.setName("Test Employee " + code);
        emp.setEmail(code.toLowerCase() + "@test.com");
        emp.setPasswordHash("$2a$10$hashedpasswordvalue");
        emp.setRoleName(role);
        emp.setRestaurant(restaurant);
        emp.setStartDate(LocalDate.now());
        emp.setActive(true);
        return em.persist(emp);
    }
}
