package com.tfg_rm.backend_restaurantmanager.dto;

import java.math.BigDecimal;    

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientsResponse {
    private Long id;
    private String name;
    private String unit;
    private BigDecimal stockQuantity;
    private BigDecimal costUnit;
    private BigDecimal minimumStock;
    private String category;
}
