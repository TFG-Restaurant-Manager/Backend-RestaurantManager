package com.tfg_rm.backend_restaurantmanager.dto;

import lombok.Data;

@Data
public class RestaurantRequest {
    private String prefix;
    private String nameRestaurant;
    private String description;
    private String emailRestaurant;
    private String phoneRestaurant;
    private String address;
    private String nameEmployee;
    private String emailEmployee;
    private String phoneEmployee;
    private String code;
    private String password;
}
