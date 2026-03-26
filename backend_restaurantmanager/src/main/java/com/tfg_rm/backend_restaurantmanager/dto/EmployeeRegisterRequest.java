package com.tfg_rm.backend_restaurantmanager.dto;

import java.time.LocalDate;

import com.tfg_rm.backend_restaurantmanager.entity.RoleEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeRegisterRequest {
    private String name;
    private RoleEntity roleName;
    private Boolean active = true;
    private String email;
    private String phone;
    private LocalDate startDate;
    private LocalDate endDate;
    private String positionNotes;
    private String code;
    private String password;
}
