package com.tfg_rm.backend_restaurantmanager.auth.dto;

/**
 * Enum representing the different roles that a user can have in the restaurant management system.
 */
public enum Role {
    /** A client of the restaurant. */
    CLIENT,
    /** A manager of the restaurant. */
    MANAGER,
    /** A waiter at the restaurant. */
    WAITER,
    /** A kitchen staff member at the restaurant. */
    KITCHEN,
    /** An administrator of the restaurant management system. */
    ADMIN
}
