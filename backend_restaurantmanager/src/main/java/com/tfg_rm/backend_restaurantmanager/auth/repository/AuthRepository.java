package com.tfg_rm.backend_restaurantmanager.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tfg_rm.backend_restaurantmanager.auth.repository.projection.ClientLoginProjection;
import com.tfg_rm.backend_restaurantmanager.shared.entity.Client;


/**
 * Java interface used to manage the queries to the database related to the authentication of the users of the application
 */
@Repository
public interface AuthRepository extends JpaRepository<Client, Long> {
    Optional<ClientLoginProjection> findByRestaurantIdAndEmail(Long restaurantId, String email);
}
