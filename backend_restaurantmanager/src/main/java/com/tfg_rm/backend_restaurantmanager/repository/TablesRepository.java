package com.tfg_rm.backend_restaurantmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tfg_rm.backend_restaurantmanager.entity.TablesRestaurantEntity;

@Repository
public interface TablesRepository extends JpaRepository<TablesRestaurantEntity, Long> {

    @Query("SELECT t FROM TablesRestaurantEntity t LEFT JOIN FETCH t.restaurant WHERE t.restaurant.id = :restaurantId")
    List<TablesRestaurantEntity> findByRestaurantId(Long restaurantId);
}
