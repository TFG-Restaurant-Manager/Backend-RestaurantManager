package com.tfg_rm.backend_restaurantmanager.dto;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SchedulesRequest {
    private Long employeeId;
    private Long scheduleId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
