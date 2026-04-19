package com.tfg_rm.backend_restaurantmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tfg_rm.backend_restaurantmanager.entity.TableSectionsEntity;

@Repository
public interface TableSectionsRepository extends JpaRepository<TableSectionsEntity, Long> {
    
    @Query("SELECT ts FROM TableSectionsEntity ts WHERE ts.restaurant.id = :restaurantId")
    List<TableSectionsEntity> findByRestaurantId(Long restaurantId);
}
