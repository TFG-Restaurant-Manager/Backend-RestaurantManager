package com.tfg_rm.backend_restaurantmanager.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg_rm.backend_restaurantmanager.shared.entity.EmployeeEntity;

/**
 * Repository for accessing employee records.
 */
@Repository
public interface AuthEmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    Optional<EmployeeEntity> findByCode(String code);

}
