package com.tfg_rm.backend_restaurantmanager.shared.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the relationship between an employee and the restaurants they have access to.
 *
 * <p>This mirrors the employee_restaurants table defined in the database.</p>
 */
@Data
@Entity
@Table(name = "employee_restaurants")
@NoArgsConstructor
public class EmployeeRestaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Employee DNI / ID (references employees.id). */
    @Column(name = "employee_dni", nullable = false)
    private Long employeeDni;

    /** Restaurant id. */
    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    /** Role id (references roles.id). */
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "position_notes")
    private String positionNotes;
}
