package com.tfg_rm.backend_restaurantmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tfg_rm.backend_restaurantmanager.entity.DishesEntity;

@Repository
public interface DishesRepository extends JpaRepository<DishesEntity, Long> {

    @Query("SELECT DISTINCT d FROM DishesEntity d LEFT JOIN FETCH d.category LEFT JOIN FETCH d.ingredients i LEFT JOIN FETCH i.ingredient WHERE d.restaurant.id = :restaurantId")
    List<DishesEntity> findByRestaurantId(Long restaurantId);

    @Query("SELECT DISTINCT d FROM DishesEntity d LEFT JOIN FETCH d.category LEFT JOIN FETCH d.ingredients i LEFT JOIN FETCH i.ingredient WHERE d.id = :id AND d.restaurant.id = :restaurantId")
    Optional<DishesEntity> findByIdAndRestaurantId(Long id, Long restaurantId);
}
