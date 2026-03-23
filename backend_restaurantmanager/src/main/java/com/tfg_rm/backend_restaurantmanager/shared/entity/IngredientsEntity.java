package com.tfg_rm.backend_restaurantmanager.shared.entity;

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
    name = "ingredients",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_ingredients_restaurant_name",
                columnNames = {"restaurant_id", "name"})
    }
)
@NoArgsConstructor
public class IngredientsEntity {

    /** 
     * Identificador único del ingrediente.
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
     * Nombre del producto.
     */
    @Column(name = "name", nullable = false, length = 150)
    private String name;

    /**
     * Unidad de medida del ingrediente (por ejemplo, gramos, litros, unidades).
     */
    @Column(name = "unit", nullable = false, length = 50)
    private String unit;

    /** 
     * Cantidad disponible del ingrediente.
     */
    @Column(name = "stock_quantity", nullable = false)
    private BigDecimal stockQuantity = BigDecimal.ZERO;

     /** 
     * Precio por unidad del ingrediente.
     */
    @Column(name = "cost_unit", nullable = false)
    private BigDecimal costPerUnit;

    /* 
     * Lista de platos que utilizan este ingrediente.
     */
    @OneToMany(mappedBy = "ingredient", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DishIngredientsEntity> dishes;
}
