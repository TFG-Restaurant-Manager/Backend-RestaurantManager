package com.tfg_rm.backend_restaurantmanager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tfg_rm.backend_restaurantmanager.dto.IngredientRequest;
import com.tfg_rm.backend_restaurantmanager.dto.IngredientsResponse;
import com.tfg_rm.backend_restaurantmanager.dto.mappers.IngredientsInfoMapper;
import com.tfg_rm.backend_restaurantmanager.entity.IngredientsCategoriesEntity;
import com.tfg_rm.backend_restaurantmanager.entity.IngredientsEntity;
import com.tfg_rm.backend_restaurantmanager.entity.RestaurantEntity;
import com.tfg_rm.backend_restaurantmanager.exception.NotFoundException;
import com.tfg_rm.backend_restaurantmanager.repository.IngredientsCategoriesRepository;
import com.tfg_rm.backend_restaurantmanager.repository.IngredientsRepository;
import com.tfg_rm.backend_restaurantmanager.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class IngredientsService {

    private final IngredientsRepository ingredientsRepository;
    private final RestaurantRepository restaurantRepository;
    private final IngredientsCategoriesRepository ingredientsCategoriesRepository;

    public List<IngredientsResponse> getAllIngredients(Long restaurantId) {
        List<IngredientsEntity> ingredients = ingredientsRepository.findByRestaurantId(restaurantId);
        return ingredients.stream()
                .map(IngredientsInfoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public IngredientsResponse createIngredient(Long restaurantId, IngredientRequest request) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        IngredientsCategoriesEntity category = ingredientsCategoriesRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        IngredientsEntity entity = new IngredientsEntity();
        updateEntityFromRequest(entity, request, restaurant, category);

        IngredientsEntity saved = ingredientsRepository.save(entity);
        return IngredientsInfoMapper.toResponse(saved);
    }

    @Transactional
    public IngredientsResponse updateIngredient(Long restaurantId, Long ingredientId, IngredientRequest request) {
        IngredientsEntity entity = ingredientsRepository.findById(ingredientId)
                .orElseThrow(() -> new NotFoundException("Ingredient not found"));

        if (!entity.getRestaurant().getId().equals(restaurantId)) {
            throw new RuntimeException("Unauthorized");
        }

        IngredientsCategoriesEntity category = ingredientsCategoriesRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));

        updateEntityFromRequest(entity, request, entity.getRestaurant(), category);

        IngredientsEntity saved = ingredientsRepository.save(entity);
        return IngredientsInfoMapper.toResponse(saved);
    }

    @Transactional
    public void deleteIngredient(Long restaurantId, Long ingredientId) {
        IngredientsEntity entity = ingredientsRepository.findById(ingredientId)
                .orElseThrow(() -> new NotFoundException("Ingredient not found"));

        if (!entity.getRestaurant().getId().equals(restaurantId)) {
            throw new RuntimeException("Unauthorized");
        }

        ingredientsRepository.delete(entity);
    }

    private void updateEntityFromRequest(IngredientsEntity entity, IngredientRequest request, RestaurantEntity restaurant, IngredientsCategoriesEntity category) {
        entity.setRestaurant(restaurant);
        entity.setCategory(category);
        entity.setName(request.getName());
        entity.setUnit(request.getUnit());
        entity.setStockQuantity(request.getStockQuantity());
        entity.setCostPerUnit(request.getCostUnit());
        entity.setMinimumStock(request.getMinimumStock());
    }
}
