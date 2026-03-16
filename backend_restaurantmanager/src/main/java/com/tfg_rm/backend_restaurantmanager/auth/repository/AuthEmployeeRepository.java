package com.tfg_rm.backend_restaurantmanager.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg_rm.backend_restaurantmanager.shared.entity.Employee;

/**
 * Repository for accessing employee records.
 */
@Repository
public interface AuthEmployeeRepository extends JpaRepository<Employee, Long> {

}
