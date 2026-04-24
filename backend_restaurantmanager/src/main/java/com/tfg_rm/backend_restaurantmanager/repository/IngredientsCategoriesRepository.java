package com.tfg_rm.backend_restaurantmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg_rm.backend_restaurantmanager.entity.IngredientsCategoriesEntity;

@Repository
public interface IngredientsCategoriesRepository extends JpaRepository<IngredientsCategoriesEntity, Long> {
}
