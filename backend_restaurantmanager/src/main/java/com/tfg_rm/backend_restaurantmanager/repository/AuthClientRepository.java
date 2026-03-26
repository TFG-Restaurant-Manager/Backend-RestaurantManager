package com.tfg_rm.backend_restaurantmanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tfg_rm.backend_restaurantmanager.entity.ClientEntity;


/**
 * Java interface used to manage the queries to the database related to the authentication of the users of the application
 */
@Repository
public interface AuthClientRepository extends JpaRepository<ClientEntity, Long> {
    Optional<ClientEntity> findByRestaurantIdAndEmail(Long restaurantId, String email);
}
