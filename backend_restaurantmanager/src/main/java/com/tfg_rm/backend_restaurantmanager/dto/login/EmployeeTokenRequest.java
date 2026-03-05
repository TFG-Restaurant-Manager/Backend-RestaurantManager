package com.tfg_rm.backend_restaurantmanager.dto.login;

public class EmployeeTokenRequest {
    private Long employeeId;
    private Long restaurantId;

    public EmployeeTokenRequest() {}

    public EmployeeTokenRequest(Long employeeId, Long restaurantId) {
        this.employeeId = employeeId;
        this.restaurantId = restaurantId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
}
