package com.tfg_rm.backend_restaurantmanager.dto.mappers;

import com.tfg_rm.backend_restaurantmanager.dto.DishesResponse;
import com.tfg_rm.backend_restaurantmanager.entity.DishesEntity;

public class DisheMapper {

    public static DishesEntity toEntity(DishesResponse request) {
        return null;
    }

    public static DishesResponse toResponse(DishesEntity entity) {
        DishesResponse response = new DishesResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setCategoryName(entity.getCategory().getName());
        response.setPrice(entity.getPrice());
        response.setAvailable(entity.getAvailable());
        return response;
    }
}
