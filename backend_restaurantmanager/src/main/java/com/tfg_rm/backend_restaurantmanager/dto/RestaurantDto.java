package com.tfg_rm.backend_restaurantmanager.dto;

public class RestaurantDto {
    private Long id;
    private String name;

    public RestaurantDto() {}

    public RestaurantDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}