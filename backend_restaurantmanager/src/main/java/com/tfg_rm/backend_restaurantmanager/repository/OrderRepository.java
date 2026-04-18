package com.tfg_rm.backend_restaurantmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tfg_rm.backend_restaurantmanager.entity.OrderStatusEntity;
import com.tfg_rm.backend_restaurantmanager.entity.OrdersEntity;

@Repository
public interface OrderRepository extends JpaRepository<OrdersEntity, Long> {

    @Query("SELECT o FROM OrdersEntity o WHERE o.restaurant.id = :restaurantId AND o.status = :status")
    List<OrdersEntity> findByRestaurantIdAndStatus(Long restaurantId, OrderStatusEntity status);
}
