package com.tfg_rm.backend_restaurantmanager.shared.entity;

/** 
 * Enumeración que representa el estado de una mesa en el restaurante.
 */
public enum TableStatusEntity {
    /** 
     * La mesa está disponible.
     */
    AVAILABLE,
    /** 
     * La mesa no está disponible.
     */
    UNAVAILABLE,
    /** 
     * La mesa está ocupada.
     */
    OCCUPIED,
    /** 
     * La mesa está reservada.
     */
    RESERVED
}
