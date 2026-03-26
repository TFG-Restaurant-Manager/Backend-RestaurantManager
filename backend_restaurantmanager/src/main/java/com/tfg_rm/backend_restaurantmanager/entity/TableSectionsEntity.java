package com.tfg_rm.backend_restaurantmanager.entity;

import java.util.List;

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
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 * Entidad que representa una sección de mesas en el restaurante.
 */
@Data
@Entity
@Table(name = "table_sections")
@NoArgsConstructor
public class TableSectionsEntity {

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
     * Nombre de la sección de mesas.
     */
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    /**
     * Lista de mesas que pertenecen a esta sección.
     */
    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY)
    private List<TablesRestaurantEntity> tables;
}
