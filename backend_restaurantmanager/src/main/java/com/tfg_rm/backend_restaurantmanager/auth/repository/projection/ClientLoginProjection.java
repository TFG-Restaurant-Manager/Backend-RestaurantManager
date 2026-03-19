package com.tfg_rm.backend_restaurantmanager.auth.repository.projection;

public interface ClientLoginProjection {
    Integer getId();
    String getPasswordHash();
}
