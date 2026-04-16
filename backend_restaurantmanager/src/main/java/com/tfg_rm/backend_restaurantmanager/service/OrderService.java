package com.tfg_rm.backend_restaurantmanager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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
}
