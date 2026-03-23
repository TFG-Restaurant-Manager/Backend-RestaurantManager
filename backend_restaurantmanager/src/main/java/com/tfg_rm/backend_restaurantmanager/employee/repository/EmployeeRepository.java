package com.tfg_rm.backend_restaurantmanager.employee.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tfg_rm.backend_restaurantmanager.shared.entity.EmployeeEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    @Query("SELECT e FROM EmployeeEntity e LEFT JOIN FETCH e.schedules WHERE e.id = :id")
    Optional<EmployeeEntity> findByIdWithSchedules(@Param("id") Long id);
}
