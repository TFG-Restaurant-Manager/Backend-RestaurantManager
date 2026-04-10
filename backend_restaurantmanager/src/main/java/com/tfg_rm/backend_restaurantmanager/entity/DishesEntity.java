package com.tfg_rm.backend_restaurantmanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;

@Data
@Entity
@Table(
    name = "dishes",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_dishes_restaurant_name",
                columnNames = {"restaurant_id", "name"})
    }
)
@NoArgsConstructor
public class DishesEntity {

    /** 
     * Identificador único de la sección de mesas.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 
     * Restaurante al que pertenece la sección de mesas.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    /**
     * Categoría a la que pertenece el producto.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private DishesCategoriesEntity category;

    /** 
     * Nombre del producto.
     */
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    /**
     * Descripción del producto.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /** 
     * Precio del producto.
     */
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    /**
     * Muestra si el producto está disponible para su venta.
     */
    @Column(name = "available", nullable = false)
    private Boolean available = true;

    /** 
     * Lista de ingredientes que componen el producto.
     */
    @OneToMany(mappedBy = "dish", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DishIngredientsEntity> ingredients;

    /**
     * Lista de ítems de orden asociados al producto.
     */
    @OneToMany(mappedBy = "dish", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemsEntity> orderItems;
}
