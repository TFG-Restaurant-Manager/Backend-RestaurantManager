package com.tfg_rm.backend_restaurantmanager.shared.entity;

import jakarta.persistence.Table;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
@Table(name = "order_items")
@NoArgsConstructor
public class OrderItemsEntity {

    /** 
     * Identificador único del ítem de la orden.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 
     * Identificador de la orden asociada al ítem de la orden.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrdersEntity order;

    /**
     * Identificador del producto asociado al ítem de la orden.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dishes_id", nullable = false)
    private DishesEntity dishes;

    /**
     * Notas adicionales para el ítem de la orden.
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * Total de la orden.
     */
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;
}
