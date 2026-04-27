package com.tfg_rm.backend_restaurantmanager.dto.mappers;

import com.tfg_rm.backend_restaurantmanager.dto.OrderResponse;
import com.tfg_rm.backend_restaurantmanager.entity.OrdersEntity;

public class OrderMapper {

    public static OrdersEntity toEntity(OrderResponse request) {
        return null;
    }

    public static OrderResponse toResponse(OrdersEntity entity) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(entity.getId());
        response.setType(entity.getType().toString());
        response.setStatus(entity.getStatus().toString());
        response.setTotal(entity.getTotal());
        response.setNotes(entity.getNotes());
        response.setCreatedAt(entity.getCreatedAt().toString());

        if (entity.getOrderItems() != null && !entity.getOrderItems().isEmpty()) {
            response.setItems(entity.getOrderItems().stream().map(OrderItemMapper::toResponse).toList());
        }

        if (entity.getTableOrder() != null) {
            response.setTableId((entity.getTableOrder().getTable().getId()));
        }

        if (entity.getDeliveryOrder() != null) {
            response.setDeliveryAddress(entity.getDeliveryOrder().getDeliveryAddress());
        }

        if (entity.getPickupOrder() != null) {
            response.setPickupTime(entity.getPickupOrder().getPickupTime());
        }

        return response;
    }
}
