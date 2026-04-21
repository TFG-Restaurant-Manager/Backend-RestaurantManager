package com.tfg_rm.backend_restaurantmanager.dto.mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.tfg_rm.backend_restaurantmanager.dto.DishIngredientResponse;
import com.tfg_rm.backend_restaurantmanager.dto.DishesResponse;
import com.tfg_rm.backend_restaurantmanager.entity.DishesEntity;

public class DishMapper {

    public static DishesEntity toEntity(DishesResponse request) {
        return null;
    }

    public static DishesResponse toResponse(DishesEntity entity) {
        DishesResponse response = new DishesResponse();
        response.setId(entity.getId());
        response.setRestaurantId(entity.getRestaurant() != null ? entity.getRestaurant().getId() : null);
        response.setCategoryId(entity.getCategory() != null ? entity.getCategory().getId() : null);
        response.setName(entity.getName());
        response.setDescription(entity.getDescription());
        response.setCategoryName(entity.getCategory() != null ? entity.getCategory().getName() : null);
        response.setPrice(entity.getPrice());
        response.setAvailable(entity.getAvailable());
        
        if (entity.getIngredients() != null) {
            List<DishIngredientResponse> ingredientResponses = entity.getIngredients().stream()
                .map(di -> new DishIngredientResponse(
                    IngredientsInfoMapper.toResponse(di.getIngredient()),
                    di.getQuantity().doubleValue()
                ))
                .collect(Collectors.toList());
            response.setIngredients(ingredientResponses);
        } else {
            response.setIngredients(Collections.emptyList());
        }

        return response;
    }
}
