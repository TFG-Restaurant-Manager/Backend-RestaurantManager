package com.tfg_rm.backend_restaurantmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import com.tfg_rm.backend_restaurantmanager.dto.OrderItemRequest;
import com.tfg_rm.backend_restaurantmanager.dto.OrderItemResponse;
import com.tfg_rm.backend_restaurantmanager.dto.OrderRequest;
import com.tfg_rm.backend_restaurantmanager.dto.OrderResponse;
import com.tfg_rm.backend_restaurantmanager.dto.mappers.OrderMapper;
import com.tfg_rm.backend_restaurantmanager.entity.OrderStatusEntity;
import com.tfg_rm.backend_restaurantmanager.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public List<OrderResponse> getAllOrdersPaid(Long restaurantId) {
        List<OrderResponse> dishes = orderRepository
                .findByRestaurantIdAndStatus(restaurantId, OrderStatusEntity.PAID)
                .stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());

        return dishes;
    }

    public List<OrderResponse> getMyOrders(Long userId, Long restaurantId) {
        List<OrderResponse> orders = orderRepository
                .findByClientIdAndRestaurantId(userId, restaurantId)
                .stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());

        return orders;
    }

    public OrderResponse createOrder(Long restaurantId, OrderRequest request) {
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setClientId(request.getClientId());
        orderResponse.setCreatedAt(request.getCreatedAt());
        orderResponse.setDeliveryAddress(request.getDeliveryAddress());
        orderResponse.setNotes(request.getNotes());
        orderResponse.setOrderId(request.getOrderId());
        orderResponse.setPickupTime(request.getPickupTime());
        orderResponse.setStatus(request.getStatus());
        orderResponse.setTableId(request.getTableId());
        orderResponse.setTotal(request.getTotal());
        orderResponse.setType(request.getType());
        orderResponse.setItems(new ArrayList<>());

        for (OrderItemRequest orderItemRequest : request.getItems()) {
            OrderItemResponse orderItemResponse = new OrderItemResponse();
            orderItemResponse.setDishId(orderItemRequest.getDishId());
            orderItemResponse.setDishName(orderItemRequest.getDishName());
            orderItemResponse.setItemNotes(orderItemRequest.getItemNotes());
            orderItemResponse.setOrderItemId(orderItemRequest.getOrderItemId());
            orderItemResponse.setOrderItemPrice(orderItemRequest.getOrderItemPrice());

            orderResponse.getItems().add(orderItemResponse);
        }
        return orderResponse;
    }
}