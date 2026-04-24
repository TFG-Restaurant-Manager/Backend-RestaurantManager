package com.tfg_rm.backend_restaurantmanager.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import com.tfg_rm.backend_restaurantmanager.entity.OrdersEntity;
import com.tfg_rm.backend_restaurantmanager.repository.AuthClientRepository;
import com.tfg_rm.backend_restaurantmanager.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AuthClientRepository clientRepository;

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
        OrdersEntity ordersEntity = new OrdersEntity();
        
        // if(request.getClientId() != null) {
        //     clientRepository.findById(request.getClientId()).orElseThrow();
        // }
        // request.getClientId();
        // request.getType();
        
        // request.getDeliveryAddress();
        
        // request.getItems();
        
        // request.getNotes();
        
        // request.getPickupTime();
        // request.getTableId();
        
        // for (OrderItemRequest item : request.getItems()) {
            
        // }
        // OrdersEntity savedOrder = orderRepository.save(ordersEntity);
        // return OrderMapper.toResponse(savedOrder);
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setClientId(request.getClientId());
        orderResponse.setCreatedAt(LocalDateTime.now().toString());
        orderResponse.setDeliveryAddress(request.getDeliveryAddress());
        orderResponse.setNotes(request.getNotes());
        orderResponse.setOrderId(2L);
        orderResponse.setPickupTime(request.getPickupTime());
        orderResponse.setStatus("CREATED");
        orderResponse.setTableId(request.getTableId());
        orderResponse.setTotal(BigDecimal.valueOf(2.5));
        orderResponse.setType(request.getType());
        orderResponse.setItems(new ArrayList<>());
        for (OrderItemRequest orderItemRequest : request.getItems()) {
            OrderItemResponse orderItemResponse = new OrderItemResponse();
            orderItemResponse.setDishId(orderItemRequest.getDishId());
            orderItemResponse.setDishName("Name");
            orderItemResponse.setItemNotes(orderItemRequest.getItemNotes());
            orderItemResponse.setOrderItemId(2L);
            orderItemResponse.setOrderItemPrice(BigDecimal.valueOf(4.2));
            orderResponse.getItems().add(orderItemResponse);
        }
        return orderResponse;
    }
}