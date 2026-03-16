package com.tfg_rm.backend_restaurantmanager.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg_rm.backend_restaurantmanager.shared.entity.EmployeeRestaurant;

/**
 * Repository for the employee_restaurants table.
 */
@Repository
public interface EmployeeRestaurantRepository extends JpaRepository<EmployeeRestaurant, Long> {

    Optional<EmployeeRestaurant> findByEmployeeDniAndRestaurantId(Long employeeDni, Long restaurantId);

    List<EmployeeRestaurant> findByEmployeeDniAndActiveTrue(Long employeeDni);
}
