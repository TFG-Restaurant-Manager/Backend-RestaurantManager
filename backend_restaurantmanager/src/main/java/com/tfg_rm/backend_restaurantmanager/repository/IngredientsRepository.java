package com.tfg_rm.backend_restaurantmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg_rm.backend_restaurantmanager.entity.IngredientsEntity;

@Repository
public interface IngredientsRepository extends JpaRepository<IngredientsEntity, Long> {

    List<IngredientsEntity> findByRestaurantId(Long restaurantId);
}