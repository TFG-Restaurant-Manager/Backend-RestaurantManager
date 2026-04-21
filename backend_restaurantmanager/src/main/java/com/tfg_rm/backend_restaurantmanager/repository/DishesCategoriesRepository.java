package com.tfg_rm.backend_restaurantmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg_rm.backend_restaurantmanager.entity.DishesCategoriesEntity;

@Repository
public interface DishesCategoriesRepository extends JpaRepository<DishesCategoriesEntity, Long> {
}
