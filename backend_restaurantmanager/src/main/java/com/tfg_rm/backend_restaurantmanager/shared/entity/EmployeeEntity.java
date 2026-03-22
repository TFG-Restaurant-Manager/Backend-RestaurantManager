package com.tfg_rm.backend_restaurantmanager.shared.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.UniqueConstraint;

/** 
 * Entidad que representa un empleado del restaurante.
 */
@Data
@NoArgsConstructor
@Entity
@Table(
    name = "employee",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_employee_code_per_restaurant",
                columnNames = {"restaurant_id", "code"})
    }
)
public class EmployeeEntity {

    /** 
     * Identificador único del empleado.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 
     * Restaurante al que pertenece el empleado.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    /** 
     * Nombre del empleado.
     */
    @Column(nullable = false, length = 150)
    private String name;

    /** 
     * Rol del empleado.
     */
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "role_name", nullable = false)
    private RoleEntity roleName;

    /** 
     * Indica si el empleado está activo o no.
     */
    @Column(nullable = false)
    private Boolean active = true;

    /** 
     * Email del empleado.
     */
    @Column(nullable = false, length = 150)
    private String email;

    /** 
     * Teléfono del empleado.
     */
    @Column(length = 25)
    private String phone;

    /** 
     * Fecha de inicio del empleado en el restaurante.
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /** 
     * Fecha de finalización del empleado en el restaurante.
     */
    @Column(name = "end_date")
    private LocalDate endDate;

    /** 
     * Notas sobre la posición del empleado.
     */
    @Column(name = "position_notes", columnDefinition = "TEXT")
    private String positionNotes;

    /** 
     * Código único del empleado.
     */
    @Column(length = 5, nullable = false)
    private String code;

    /** 
     * Hash de la contraseña del empleado.
     */
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    /** 
     * Fecha de creación del empleado.
     */
    @Column(name = "created_at", nullable = false,
            insertable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 
     * Lista de horarios de trabajo del empleado.
     */
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkScheduleEntity> schedules;
}