package com.tfg_rm.backend_restaurantmanager.dto;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IngredientRequest {
    private String name;
    private String unit;
    private BigDecimal stockQuantity;
    private BigDecimal costUnit;
    private BigDecimal minimumStock;
    private Long categoryId;
    private Long categoryName;
}
