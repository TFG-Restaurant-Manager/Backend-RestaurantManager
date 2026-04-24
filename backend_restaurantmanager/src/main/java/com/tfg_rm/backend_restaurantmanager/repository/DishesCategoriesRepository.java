package com.tfg_rm.backend_restaurantmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tfg_rm.backend_restaurantmanager.entity.DishesCategoriesEntity;

@Repository
public interface DishesCategoriesRepository extends JpaRepository<DishesCategoriesEntity, Long> {

    @Query("SELECT DISTINCT dc FROM DishesCategoriesEntity dc WHERE dc.restaurant.id = :restaurantId")
    List<DishesCategoriesEntity> findByRestaurantId(Long restaurantId);
}
