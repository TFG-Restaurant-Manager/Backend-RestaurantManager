package com.tfg_rm.backend_restaurantmanager.dto.login;

import lombok.Data;

@Data
public class ClientLoginRequest {

    private String email;
    private String password;
    private int restaurantId;
}
