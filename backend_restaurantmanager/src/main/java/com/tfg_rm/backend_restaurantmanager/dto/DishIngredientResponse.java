package com.tfg_rm.backend_restaurantmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DishIngredientResponse {
    private IngredientsResponse ingredient;
    private Double quantity;
}
