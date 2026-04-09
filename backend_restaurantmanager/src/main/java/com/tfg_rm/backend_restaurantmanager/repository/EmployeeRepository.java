package com.tfg_rm.backend_restaurantmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tfg_rm.backend_restaurantmanager.entity.EmployeeEntity;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    @Query("SELECT e FROM EmployeeEntity e LEFT JOIN FETCH e.schedules WHERE e.id = :id")
    Optional<EmployeeEntity> findByIdWithSchedules(@Param("id") Long id);

    List<EmployeeEntity> findByRestaurantId(Long restaurantId);

    @Modifying
    @Query("UPDATE EmployeeEntity e SET e.name = :name, e.roleName = :roleName, e.active = :active, e.email = :email, e.phone = :phone, e.startDate = :startDate, e.endDate = :endDate, e.positionNotes = :positionNotes WHERE e.id = :id AND e.restaurant.id = :restaurantId")
    int updateEmployee(@Param("id") Long id, @Param("restaurantId") Long restaurantId);
}
