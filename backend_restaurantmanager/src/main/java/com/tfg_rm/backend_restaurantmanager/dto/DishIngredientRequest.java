package com.tfg_rm.backend_restaurantmanager.dto;

import lombok.Data;

@Data
public class DishIngredientRequest {
    private IngredientsResponse ingredient;
    private Double quantity;
}
