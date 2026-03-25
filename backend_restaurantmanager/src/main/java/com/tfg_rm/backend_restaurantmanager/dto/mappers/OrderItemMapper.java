package com.tfg_rm.backend_restaurantmanager.dto.mappers;

import com.tfg_rm.backend_restaurantmanager.dto.OrderItemResponse;
import com.tfg_rm.backend_restaurantmanager.entity.OrderItemsEntity;

public class OrderItemMapper {

    public static OrderItemsEntity toEntity(OrderItemResponse request) {
        return null;
    }

    public static OrderItemResponse toResponse(OrderItemsEntity entity) {
        OrderItemResponse response = new OrderItemResponse();
        response.setOrderItemId(entity.getId());
        response.setDishId(entity.getDish().getId());
        response.setDishName(entity.getDish().getName());
        response.setItemNotes(entity.getNotes());
        response.setOrderItemPrice(entity.getUnitPrice());
        return response;
    }
}