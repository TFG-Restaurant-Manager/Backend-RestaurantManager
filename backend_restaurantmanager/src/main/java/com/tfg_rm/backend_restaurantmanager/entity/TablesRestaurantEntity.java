package com.tfg_rm.backend_restaurantmanager.entity;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

/** 
 * Entidad que representa una mesa en el restaurante.
 */
@Data
@Entity
@Table(name = "tables_restaurant")
@NoArgsConstructor
public class TablesRestaurantEntity {
    
    /** 
     * Identificador único de la mesa.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 
     * Restaurante al que pertenece la mesa.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    /** 
     * Sección de mesas a la que pertenece la mesa.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private TableSectionsEntity section;

    /** 
     * Nombre de la mesa.
     */
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    /** 
     * Capacidad de la mesa (número de comensales que puede acomodar).
     */
    @Column(name = "capacity", nullable = false)
    private Integer capacity = 2;

    /** 
     * Posición X de la mesa.
     */
    @Column(name = "pos_x", nullable = false)
    private Integer posX = 0;

    /** 
     * Posición Y de la mesa.
     */
    @Column(name = "pos_y", nullable = false)
    private Integer posY = 0;

    /** 
     * Estado de la mesa (disponible, ocupada, reservada, etc.).
     */
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "status", nullable = false)
    private TableStatusEntity status = TableStatusEntity.AVAILABLE;
}
