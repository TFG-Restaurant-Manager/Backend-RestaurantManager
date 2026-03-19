package com.tfg_rm.backend_restaurantmanager.employee.dto;

import java.time.LocalDate;
import java.util.List;

import com.tfg_rm.backend_restaurantmanager.shared.entity.RoleEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeWithSchedulesResponse {
    private Integer id;
    private String name;
    private RoleEntity roleName;
    private Boolean active;
    private String email;
    private String phone;
    private LocalDate startDate;
    private LocalDate endDate;
    private String positionNotes;
    private String code;
    private Integer restaurantId;
    private String restaurantName;
    private List<EmployeeScheduleDto> schedules;
}
