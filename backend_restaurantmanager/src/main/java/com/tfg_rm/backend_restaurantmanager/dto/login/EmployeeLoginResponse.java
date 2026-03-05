package com.tfg_rm.backend_restaurantmanager.dto.login;

import java.util.List;

public class EmployeeLoginResponse {
    private Long employeeId;
    private List<Long> restaurantIds;

    public EmployeeLoginResponse(Long employeeId, List<Long> restaurantIds) {
        this.employeeId = employeeId;
        this.restaurantIds = restaurantIds;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public List<Long> getRestaurantIds() {
        return restaurantIds;
    }

    public void setRestaurantIds(List<Long> restaurantIds) {
        this.restaurantIds = restaurantIds;
    }
}
