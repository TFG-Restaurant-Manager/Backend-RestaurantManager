package com.tfg_rm.backend_restaurantmanager.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tfg_rm.backend_restaurantmanager.dto.OrderItemRequest;
import com.tfg_rm.backend_restaurantmanager.dto.OrderRequest;
import com.tfg_rm.backend_restaurantmanager.dto.OrderResponse;
import com.tfg_rm.backend_restaurantmanager.dto.mappers.OrderMapper;
import com.tfg_rm.backend_restaurantmanager.entity.DishesEntity;
import com.tfg_rm.backend_restaurantmanager.entity.OrderDeliveryEntity;
import com.tfg_rm.backend_restaurantmanager.entity.OrderItemsEntity;
import com.tfg_rm.backend_restaurantmanager.entity.OrderItemsStatusEntity;
import com.tfg_rm.backend_restaurantmanager.entity.OrderPickupEntity;
import com.tfg_rm.backend_restaurantmanager.entity.OrderStatusEntity;
import com.tfg_rm.backend_restaurantmanager.entity.OrderTableEntity;
import com.tfg_rm.backend_restaurantmanager.entity.OrderTypeEntity;
import com.tfg_rm.backend_restaurantmanager.entity.OrdersEntity;
import com.tfg_rm.backend_restaurantmanager.entity.RestaurantEntity;
import com.tfg_rm.backend_restaurantmanager.entity.TablesRestaurantEntity;
import com.tfg_rm.backend_restaurantmanager.exception.NotFoundException;
import com.tfg_rm.backend_restaurantmanager.exception.UnauthorizedException;
import com.tfg_rm.backend_restaurantmanager.repository.DishesRepository;
import com.tfg_rm.backend_restaurantmanager.repository.OrderRepository;
import com.tfg_rm.backend_restaurantmanager.repository.RestaurantRepository;
import com.tfg_rm.backend_restaurantmanager.repository.TablesRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final TablesRepository tablesRepository;
    private final DishesRepository dishesRepository;
    private final RestaurantRepository restaurantRepository;

    public List<OrderResponse> getAllOrdersPaid(Long restaurantId) {
        List<OrderResponse> dishes = orderRepository
                .findByRestaurantIdAndStatus(restaurantId, OrderStatusEntity.PAID)
                .stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());

        return dishes;
    }

    @Transactional
    public OrderResponse createOrder(Long restaurantId, OrderRequest request) {
        OrdersEntity ordersEntity = new OrdersEntity();
        ordersEntity.setCreatedAt(LocalDateTime.now());
        RestaurantEntity restaurant = restaurantRepository
                .findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));
        ordersEntity.setRestaurant(restaurant);
        ordersEntity.setCreatedAt(request.getCreatedAt());
        ordersEntity.setNotes(request.getNotes());
        ordersEntity.setStatus(OrderStatusEntity.CREATED);

        OrderTypeEntity type = OrderTypeEntity.valueOf(request.getType().toUpperCase());
        ordersEntity.setType(type);
        switch (type) {
            case OrderTypeEntity.TABLE -> {
                OrderTableEntity orderTableEntity = new OrderTableEntity();
                TablesRestaurantEntity table = tablesRepository
                        .findById(request.getTableId())
                        .orElseThrow(() -> new NotFoundException("Table not found"));
                if (table.getRestaurant().getId() != restaurantId)
                    throw new UnauthorizedException("You are not authrized");
                orderTableEntity.setTable(table);
                orderTableEntity.setOrder(ordersEntity);
                table.getOrderTables().add(orderTableEntity);
                ordersEntity.setTableOrder(orderTableEntity);
            }
            case OrderTypeEntity.DELIVERY -> {
                OrderDeliveryEntity orderDeliveryEntity = new OrderDeliveryEntity();
                orderDeliveryEntity.setDeliveryAddress(request.getDeliveryAddress());
                orderDeliveryEntity.setOrder(ordersEntity);
                ordersEntity.setDeliveryOrder(orderDeliveryEntity);
            }
            case OrderTypeEntity.PICKUP -> {
                OrderPickupEntity orderDeliveryEntity = new OrderPickupEntity();
                orderDeliveryEntity.setPickupTime(request.getPickupTime());
                orderDeliveryEntity.setOrder(ordersEntity);
                ordersEntity.setPickupOrder(orderDeliveryEntity);
            }
        }

        ordersEntity.setOrderItems(new ArrayList<OrderItemsEntity>());
        for (OrderItemRequest orderItemRequest : request.getItems()) {
            OrderItemsEntity orderItemsEntity = new OrderItemsEntity();
            DishesEntity dishesEntity = dishesRepository
                    .findById(orderItemRequest.getDishId())
                    .orElseThrow(() -> new NotFoundException("Dish not found"));
            if (dishesEntity.getRestaurant().getId() != restaurantId)
                throw new UnauthorizedException("You are not authrized");
            orderItemsEntity.setDish(dishesEntity);
            orderItemsEntity.setNotes(orderItemRequest.getItemNotes());
            orderItemsEntity.setOrder(ordersEntity);
            OrderItemsStatusEntity status = OrderItemsStatusEntity.valueOf(orderItemRequest.getStatus().toUpperCase());
            orderItemsEntity.setStatus(status);
            orderItemsEntity.setUnitPrice(dishesEntity.getPrice());

            ordersEntity.getOrderItems().add(orderItemsEntity);
            ordersEntity.setTotal(ordersEntity.getTotal().add(orderItemsEntity.getUnitPrice()));
        }

        OrdersEntity savedOrder = orderRepository.save(ordersEntity);
        return OrderMapper.toResponse(savedOrder);
    }

    @Transactional
    public OrderResponse updateOrder(Long restaurantId, OrderRequest request) {
        OrdersEntity ordersEntity = orderRepository
                        .findById(request.getId())
                        .orElseThrow(() -> new NotFoundException("Table not found"));
        if (ordersEntity.getRestaurant().getId() != restaurantId)
            throw new UnauthorizedException("You are not authrized");
        ordersEntity.setTotal(BigDecimal.ZERO);
        ordersEntity.setNotes(request.getNotes());

        ordersEntity.setStatus(OrderStatusEntity.CREATED);

        for (OrderItemRequest orderItemRequest : request.getItems()) {
            Set<Long> requestIds = request.getItems().stream()
                    .map(OrderItemRequest::getId)
                    .collect(Collectors.toSet());

            ordersEntity.getOrderItems().removeIf(id -> !requestIds.contains(id.getId()));
            if(orderItemRequest.getId() == 0) {
                OrderItemsEntity orderItemsEntity = new OrderItemsEntity();
                DishesEntity dishesEntity = dishesRepository
                        .findById(orderItemRequest.getDishId())
                        .orElseThrow(() -> new NotFoundException("Dish not found"));
                if (dishesEntity.getRestaurant().getId() != restaurantId)
                    throw new UnauthorizedException("You are not authrized");
                orderItemsEntity.setDish(dishesEntity);
                orderItemsEntity.setNotes(orderItemRequest.getItemNotes());
                orderItemsEntity.setOrder(ordersEntity);
                OrderItemsStatusEntity status = OrderItemsStatusEntity.valueOf(orderItemRequest.getStatus().toUpperCase());
                orderItemsEntity.setStatus(status);
                orderItemsEntity.setUnitPrice(dishesEntity.getPrice());

                ordersEntity.getOrderItems().add(orderItemsEntity);
                ordersEntity.setTotal(ordersEntity.getTotal().add(orderItemsEntity.getUnitPrice()));
            } else {
                OrderItemsEntity item = ordersEntity.getOrderItems().stream()
                        .filter(n -> Objects.equals(n.getId(), orderItemRequest.getId()))
                        .findFirst()
                        .orElseThrow(() -> new NotFoundException("Item not found"));
                OrderItemsStatusEntity status = OrderItemsStatusEntity.valueOf(orderItemRequest.getStatus().toUpperCase());
                item.setStatus(status);
                item.setNotes(orderItemRequest.getItemNotes());
            }
        }

        OrdersEntity savedOrder = orderRepository.save(ordersEntity);
        return OrderMapper.toResponse(savedOrder);
    }

    public List<OrderResponse> getAllOrders(Long restaurantId) {
        List<OrderResponse> dishes = orderRepository
                .findByRestaurantId(restaurantId)
                .stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());

        return dishes;
    }
}