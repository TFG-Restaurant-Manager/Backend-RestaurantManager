package com.tfg_rm.backend_restaurantmanager.shared.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.OneToMany;

/**
 * Entidad que representa una categoría de productos en el restaurante.
 */
@Data
@Entity
@Table(name = "orders")
@NoArgsConstructor
public class OrdersEntity {

    /**
     * Identificador único de la orden.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Restaurante al que pertenece la orden.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    /**
     * Tipo de orden (mesa, delivery, recogida).
     */
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "type", nullable = false, length = 20)
    private OrderTypeEntity type;

    /**
     * Estado de la orden (creada, en preparación, lista, entregada).
     */
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatusEntity status = OrderStatusEntity.CREATED;

    /**
     * Total de la orden.
     */
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private Double total = 0.00;

    /**
     * Notas adicionales para la orden.
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * Fecha y hora de creación de la orden.
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime created_at;
}