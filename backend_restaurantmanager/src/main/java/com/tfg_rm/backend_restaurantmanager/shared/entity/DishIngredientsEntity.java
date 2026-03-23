package com.tfg_rm.backend_restaurantmanager.shared.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.UniqueConstraint;

@Data
@Entity
@Table(
    name = "dish_ingredients",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_di_dish_ingredient",
                columnNames = {"dish_id", "ingredient_id"})
    }
)
@NoArgsConstructor
public class DishIngredientsEntity {

    /** 
     * Identificador único del ingrediente del plato.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 
     * Plato al que pertenece el ingrediente.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id", nullable = false)
    private DishesEntity dish;

    /** 
     * Ingrediente utilizado en el plato.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private IngredientsEntity ingredient;

    /**
     * Cantidad del ingrediente utilizada en el plato.
     */
    @Column(name = "quantity", nullable = false)
    private BigDecimal quantity;
}
