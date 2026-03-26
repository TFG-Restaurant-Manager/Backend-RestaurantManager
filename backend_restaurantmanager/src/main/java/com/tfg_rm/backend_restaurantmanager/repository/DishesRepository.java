package com.tfg_rm.backend_restaurantmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tfg_rm.backend_restaurantmanager.entity.DishesEntity;

@Repository
public interface DishesRepository extends JpaRepository<DishesEntity, Long> {

    @Query("SELECT d FROM DishesEntity d LEFT JOIN FETCH d.category WHERE d.restaurant.id = :restaurantId")
    List<DishesEntity> findByRestaurantId(Long restaurantId);
}
