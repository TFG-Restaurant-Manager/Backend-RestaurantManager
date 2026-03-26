package com.tfg_rm.backend_restaurantmanager.dto.mappers;

import com.tfg_rm.backend_restaurantmanager.dto.EmployeeScheduleResponse;
import com.tfg_rm.backend_restaurantmanager.dto.EmployeeWithSchedulesResponse;
import com.tfg_rm.backend_restaurantmanager.entity.EmployeeEntity;

public class EmployeeInfoMapper {

    public static EmployeeEntity toEntity(EmployeeWithSchedulesResponse request) {
        EmployeeEntity entity = new EmployeeEntity();
        // entity.setId(request.getId());
        // entity.setName(request.getName());
        // entity.setRoleName(request.getRoleName());
        // entity.setActive(request.getActive());
        // entity.setEmail(request.getEmail());
        // entity.setPhone(request.getPhone());
        // entity.setStartDate(request.getStartDate());
        // entity.setEndDate(request.getEndDate());
        // entity.setPositionNotes(request.getPositionNotes());
        // entity.setCode(request.getCode());
        // entity.setSchedules(
        //     request.getSchedules().stream()
        //         .map( schedule -> {
        //             WorkScheduleEntity scheduleEntity = new WorkScheduleEntity();
        //             scheduleEntity.setId(schedule.getId());
        //             scheduleEntity.setStartDatetime(schedule.getStartDatetime());
        //             scheduleEntity.setEndDatetime(schedule.getEndDatetime());
        //             return scheduleEntity;
        //         })
        //         .toList()
        // );
        return entity;
    }

    public static EmployeeWithSchedulesResponse toResponse(EmployeeEntity entity) {
        EmployeeWithSchedulesResponse response = new EmployeeWithSchedulesResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setRoleName(entity.getRoleName());
        response.setActive(entity.getActive());
        response.setEmail(entity.getEmail());
        response.setPhone(entity.getPhone());
        response.setStartDate(entity.getStartDate());
        response.setEndDate(entity.getEndDate());
        response.setPositionNotes(entity.getPositionNotes());
        response.setCode(entity.getCode());
        response.setRestaurantName(entity.getRestaurant().getName());
        response.setSchedules(
            entity.getSchedules().stream()
                .map( schedule -> {
                    EmployeeScheduleResponse scheduleResponse = new EmployeeScheduleResponse();
                    scheduleResponse.setId(schedule.getId());
                    scheduleResponse.setStartDatetime(schedule.getStartDatetime());
                    scheduleResponse.setEndDatetime(schedule.getEndDatetime());
                    return scheduleResponse;
                })
                .toList()
        );
        return response;
    }
}
