package com.tfg_rm.backend_restaurantmanager.employee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg_rm.backend_restaurantmanager.shared.entity.view.RestaurantDishViewEntity;

@Repository
public interface RestaurantDishViewRepository extends JpaRepository<RestaurantDishViewEntity, Integer> {
    List<RestaurantDishViewEntity> findByRestaurantId(Integer restaurantId);
}
