package com.tfg_rm.backend_restaurantmanager.dto.mappers;

import com.tfg_rm.backend_restaurantmanager.dto.TableResponse;
import com.tfg_rm.backend_restaurantmanager.entity.OrderTableEntity;
import com.tfg_rm.backend_restaurantmanager.entity.TablesRestaurantEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TableMapper {

    public static TablesRestaurantEntity toEntity(TableResponse request) {
        return null;
    }

    public static TableResponse toResponse(TablesRestaurantEntity entity) {
        log.info("Mapping TablesRestaurantEntity to TableResponse for table ID: {}", entity.getId());
        TableResponse response = new TableResponse();
        response.setTableId(entity.getId());
        response.setTableName(entity.getName());
        response.setCapacity(entity.getCapacity());
        response.setPosX(entity.getPosX());
        response.setPosY(entity.getPosY());
        response.setStatus(entity.getStatus().toString());
        response.setSectionTitle(entity.getSection().getTitle());
        response.setSectionId(entity.getSection().getId());
        if (entity.getOrderTables() != null && !entity.getOrderTables().isEmpty()) {
            OrderTableEntity orderTable = entity
                    .getOrderTables()
                    .stream()
                    .filter(order -> {
                        boolean isActive = false;
                        if (order.getOrder().getStatus() != null) {
                            switch (order.getOrder().getStatus()) {
                                case CREATED -> isActive = true;
                                default -> isActive = false;
                            }
                        }
                        log.info("Order status: {}", order.getOrder().getStatus());
                        return isActive;
                    })
                    .toList()
                    .get(0);
            log.info("Active order found: {}", orderTable.getOrder().getId());
            response.setOrderId(orderTable.getOrder().getId());
            response.setOrderStatus(orderTable.getOrder().getStatus().toString());
            response.setOrderTotal(orderTable.getOrder().getTotal());
            response.setOrderNotes(orderTable.getOrder().getNotes());
            response.setOrderCreatedAt(orderTable.getOrder().getCreatedAt().toString());
            response.setOrderItems(
                    orderTable.getOrder().getOrderItems().stream().map(OrderItemMapper::toResponse).toList());
        }

        log.info("Response: {}", response);

        return response;
    }
}