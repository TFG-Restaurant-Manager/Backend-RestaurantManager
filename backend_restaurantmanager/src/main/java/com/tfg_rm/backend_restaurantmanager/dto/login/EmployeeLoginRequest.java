package com.tfg_rm.backend_restaurantmanager.dto.login;

import lombok.Data;

@Data
public class EmployeeLoginRequest {

    private String dni;
    private String password;
}
