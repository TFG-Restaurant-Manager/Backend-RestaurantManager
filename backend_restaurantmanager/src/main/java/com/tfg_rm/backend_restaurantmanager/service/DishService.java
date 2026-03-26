package com.tfg_rm.backend_restaurantmanager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tfg_rm.backend_restaurantmanager.dto.DishesResponse;
import com.tfg_rm.backend_restaurantmanager.dto.mappers.DishMapper;
import com.tfg_rm.backend_restaurantmanager.repository.DishesRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DishService {

    private final DishesRepository dishesRepository;

    public List<DishesResponse> getDishesInfo(Long restaurantId) {
        List<DishesResponse> dishes = dishesRepository
            .findByRestaurantId(restaurantId)
            .stream()
            .map(DishMapper::toResponse)
            .collect(Collectors.toList());

        return dishes;
    }
}
