package com.tfg_rm.backend_restaurantmanager.shared.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 * Entidad que representa un cliente del restaurante.
 */
@Data
@Entity
@Table(
    name = "clients",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_clients_restaurant_email",
                columnNames = {"restaurant_id", "email"})
    }
)
@NoArgsConstructor
public class ClientEntity {

    /** 
     * Identificador único del cliente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 
     * Restaurante al que pertenece el cliente.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    /** 
     * Nombre del cliente.
     */
    @Column(name = "name")
    private String name;

    /** 
     * Email del cliente.
     */
    @Column(name = "email", nullable = false)
    private String email;

    /** 
     * Hash de la contraseña del cliente.
     */
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    /** 
     * Fecha de creación del cliente.
     */
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Lista de órdenes de delivery asociadas al cliente.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDeliveryEntity> deliveries;

    /**
     * Lista de órdenes de recogida asociadas al cliente.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderPickupEntity> pickups;
}