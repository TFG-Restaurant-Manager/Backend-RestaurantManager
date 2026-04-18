package com.tfg_rm.backend_restaurantmanager.dto.mappers;

import com.tfg_rm.backend_restaurantmanager.dto.IngredientsResponse;
import com.tfg_rm.backend_restaurantmanager.entity.IngredientsEntity;

public class IngredientsInfoMapper {


    public static IngredientsResponse toResponse(IngredientsEntity entity) {
        IngredientsResponse response = new IngredientsResponse();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setUnit(entity.getUnit());
        response.setStockQuantity(entity.getStockQuantity());
        response.setCostUnit(entity.getCostPerUnit());
        response.setMinimumStock(entity.getMinimumStock());
        response.setCategory(entity.getCategory().getName());

        return response;
    }
}
