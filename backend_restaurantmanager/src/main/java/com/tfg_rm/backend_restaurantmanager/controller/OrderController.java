package com.tfg_rm.backend_restaurantmanager.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tfg_rm.backend_restaurantmanager.dto.OrderResponse;
import com.tfg_rm.backend_restaurantmanager.exception.UnauthorizedException;
import com.tfg_rm.backend_restaurantmanager.security.JwtService;
import com.tfg_rm.backend_restaurantmanager.service.OrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order")
public class OrderController {

    /** The dish service for managing dish-related operations. */
    private final OrderService orderService;

    /** The JWT service for token generation and validation. */
    private final JwtService jwtService;

    @GetMapping("/paid")
    public ResponseEntity<List<OrderResponse>> getAllPaid(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long restaurantId = jwtService.getRestaurantId(token);

        List<OrderResponse> orders = orderService.getAllOrdersPaid(restaurantId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        Long restaurantId = jwtService.getRestaurantId(token);
        Long userId = jwtService.getUserId(token);
        String role = jwtService.getRole(token);

        if (!role.equals("CLIENTE"))
            throw new UnauthorizedException("You are not authorized to perform this action");

        List<OrderResponse> orders = orderService.getMyOrders(userId, restaurantId);
        return ResponseEntity.ok(orders);
    }
}