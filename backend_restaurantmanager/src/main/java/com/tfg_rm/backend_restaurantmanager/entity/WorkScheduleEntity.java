package com.tfg_rm.backend_restaurantmanager.entity;

import java.time.LocalDateTime;

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

/** 
 * Entidad que representa un horario de trabajo.
 */
@Data
@Entity
@Table(name = "work_schedules")
@NoArgsConstructor
public class WorkScheduleEntity {

    /** 
     * Identificador único del horario de trabajo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 
     * Empleado al que pertenece el horario de trabajo.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    /** 
     * Fecha y hora de inicio del horario de trabajo.
     */
    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDatetime;

    /** 
     * Fecha y hora de finalización del horario de trabajo.
     */
    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDatetime;
}