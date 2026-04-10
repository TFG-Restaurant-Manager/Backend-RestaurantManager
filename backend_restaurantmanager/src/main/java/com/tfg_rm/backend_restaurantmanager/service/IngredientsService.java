package com.tfg_rm.backend_restaurantmanager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tfg_rm.backend_restaurantmanager.dto.IngredientsResponse;
import com.tfg_rm.backend_restaurantmanager.dto.mappers.IngredientsInfoMapper;
import com.tfg_rm.backend_restaurantmanager.entity.IngredientsEntity;
import com.tfg_rm.backend_restaurantmanager.repository.IngredientsRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class IngredientsService {

    private final IngredientsRepository ingredientsRepository;

    public List<IngredientsResponse> getAllIngredients(Long restaurantId) {

        List<IngredientsEntity> ingredients = ingredientsRepository.findByRestaurantId(restaurantId);

        return ingredients.stream()
                .map(IngredientsInfoMapper::toResponse)
                .collect(Collectors.toList());
    }


 
}
