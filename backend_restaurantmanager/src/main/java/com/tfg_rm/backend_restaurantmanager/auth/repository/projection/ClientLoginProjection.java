package com.tfg_rm.backend_restaurantmanager.auth.repository.projection;

public interface ClientLoginProjection {
    Long getId();
    String getPasswordHash();
}
