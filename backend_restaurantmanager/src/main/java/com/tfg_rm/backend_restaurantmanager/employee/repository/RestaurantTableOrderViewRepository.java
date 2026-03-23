package com.tfg_rm.backend_restaurantmanager.employee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg_rm.backend_restaurantmanager.shared.entity.view.RestaurantTableOrderViewEntity;

@Repository
public interface RestaurantTableOrderViewRepository extends JpaRepository<RestaurantTableOrderViewEntity, Long> {
    List<RestaurantTableOrderViewEntity> findByRestaurantId(Long restaurantId);
}
