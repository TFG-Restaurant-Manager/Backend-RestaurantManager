package com.tfg_rm.backend_restaurantmanager.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class DishesRequest {
    private Long restaurantId;
    private Long categoryId;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean available;
    private List<DishIngredientRequest> ingredients;
}
