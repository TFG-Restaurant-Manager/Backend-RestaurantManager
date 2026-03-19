package com.tfg_rm.backend_restaurantmanager.employee.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeScheduleDto {
    private Integer id;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
}
