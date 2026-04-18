package com.tfg_rm.backend_restaurantmanager.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TableRequest {
    private Long tableId;
    private String tableName;
    private Integer capacity;
    private Integer posX;
    private Integer posY;
    private Long sectionId;
}
