package com.tfg_rm.backend_restaurantmanager.entity;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

/** 
 * Entidad que representa un restaurante en la base de datos.
 * Contiene información básica del restaurante, como su nombre, descripción, email, teléfono, dirección y logo.
 * Se utiliza para mapear la tabla "restaurants" en la base de datos y gestionar las operaciones CRUD relacionadas con los restaurantes
 */
@Data
@Entity
@Table(
    name = "restaurants",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_restaurants_prefix",
                columnNames = {"prefix"}),
        @UniqueConstraint(name = "uq_restaurants_email",
                columnNames = {"email"})
    }
)
@NoArgsConstructor
public class RestaurantEntity {

    /** 
     * Identificador único del restaurante.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 
     * Prefijo único del restaurante.
     */
    @Column(nullable = false, length = 5)
    private String prefix;

    /** 
     * Nombre del restaurante.
     */
    @Column(nullable = false, length = 150)
    private String name;

    /** 
     * Descripción del restaurante.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    /** 
     * Email del restaurante.
     */
    @Column(nullable = false, length = 150)
    private String email;

    /** 
     * Teléfono del restaurante.
     */
    @Column(nullable = false, length = 25)
    private String phone;

    /** 
     * Dirección del restaurante.
     */
    @Column(nullable = false, length = 255)
    private String address;

    /** 
     * URL del logo del restaurante.
     */
    @Column(name = "logo_url")
    private String logoUrl;

    /** 
     * Fecha de creación del restaurante.
     */
    @Column(name = "created_at", nullable = false, updatable = false,
            insertable = false)
    private LocalDateTime createdAt;

    // Relaciones con otras entidades

    /** 
     * Lista de empleados del restaurante.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeEntity> employees;

    /** 
     * Lista de clientes del restaurante.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClientEntity> clients;

    /** 
     * Lista de secciones de mesas del restaurante.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TableSectionsEntity> tableSections;

    /** 
     * Lista de mesas del restaurante.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TablesRestaurantEntity> tables;

    /** 
     * Lista de categorías de productos del restaurante.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoriesEntity> categories;

    /** 
     * Lista de productos del restaurante.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DishesEntity> dishes;

    /**
     * Lista de ingredientes del restaurante.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<IngredientsEntity> ingredients;

    /** 
     * Lista de órdenes del restaurante.
     */
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdersEntity> orders;
}