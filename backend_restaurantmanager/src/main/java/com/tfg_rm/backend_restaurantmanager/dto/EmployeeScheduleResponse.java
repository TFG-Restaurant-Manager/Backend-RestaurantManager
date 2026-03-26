package com.tfg_rm.backend_restaurantmanager.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeScheduleResponse {
    private Long id;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
}
