package com.tfg_rm.backend_restaurantmanager.dto;

import java.time.LocalDate;
import java.util.List;

import com.tfg_rm.backend_restaurantmanager.entity.RoleEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeWithSchedulesResponse {
    private Long id;
    private String name;
    private RoleEntity roleName;
    private Boolean active;
    private String email;
    private String phone;
    private LocalDate startDate;
    private LocalDate endDate;
    private String positionNotes;
    private String code;
    private String restaurantName;
    private List<EmployeeScheduleResponse> schedules;
}
