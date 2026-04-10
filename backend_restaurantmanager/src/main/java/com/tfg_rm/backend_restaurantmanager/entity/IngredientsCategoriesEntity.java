package com.tfg_rm.backend_restaurantmanager.entity;

import jakarta.persistence.Column;
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
import java.util.List;
import jakarta.persistence.OneToMany;

/**
 * Entidad que representa una categoría de productos en el restaurante.
 */
@Data
@Entity
@Table(name = "ingredients_categories")
@NoArgsConstructor
public class IngredientsCategoriesEntity {

    /**
     * Identificador único de la categoría.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Restaurante al que pertenece la categoría.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    /**
     * Nombre de la categoría.
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Lista de productos que pertenecen a esta categoría.
     */
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<IngredientsEntity> dishes;
}
