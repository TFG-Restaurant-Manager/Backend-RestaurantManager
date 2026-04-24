package com.tfg_rm.backend_restaurantmanager.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tfg_rm.backend_restaurantmanager.dto.DishIngredientRequest;
import com.tfg_rm.backend_restaurantmanager.dto.DishesRequest;
import com.tfg_rm.backend_restaurantmanager.dto.DishesResponse;
import com.tfg_rm.backend_restaurantmanager.dto.mappers.DishMapper;
import com.tfg_rm.backend_restaurantmanager.entity.DishIngredientsEntity;
import com.tfg_rm.backend_restaurantmanager.entity.DishesCategoriesEntity;
import com.tfg_rm.backend_restaurantmanager.entity.DishesEntity;
import com.tfg_rm.backend_restaurantmanager.entity.IngredientsEntity;
import com.tfg_rm.backend_restaurantmanager.entity.RestaurantEntity;
import com.tfg_rm.backend_restaurantmanager.exception.NotFoundException;
import com.tfg_rm.backend_restaurantmanager.repository.DishesCategoriesRepository;
import com.tfg_rm.backend_restaurantmanager.repository.DishesRepository;
import com.tfg_rm.backend_restaurantmanager.repository.IngredientsRepository;
import com.tfg_rm.backend_restaurantmanager.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DishService {

    private final DishesRepository dishesRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishesCategoriesRepository categoriesRepository;
    private final IngredientsRepository ingredientsRepository;

    public List<DishesResponse> getAllDishes(Long restaurantId) {
        List<DishesResponse> dishes = dishesRepository
            .findByRestaurantId(restaurantId)
            .stream()
            .map(DishMapper::toResponse)
            .collect(Collectors.toList());

        return dishes;
    }

    public DishesResponse getDishById(Long id, Long restaurantId) {
        DishesEntity dish = dishesRepository.findByIdAndRestaurantId(id, restaurantId)
            .orElseThrow(() -> new NotFoundException("Dish not found"));
        return DishMapper.toResponse(dish);
    }

    public DishesResponse createDish(DishesRequest request, Long restaurantId) {
        RestaurantEntity restaurant = restaurantRepository.findById(restaurantId)
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));
            
        DishesCategoriesEntity category = null;
        if (request.getCategoryId() != null) {
            category = categoriesRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
        }
        
        DishesEntity dish = new DishesEntity();
        dish.setRestaurant(restaurant);
        dish.setCategory(category);
        dish.setName(request.getName());
        dish.setDescription(request.getDescription());
        dish.setPrice(request.getPrice());
        dish.setAvailable(request.getAvailable() != null ? request.getAvailable() : true);
        
        List<DishIngredientsEntity> dishIngredients = new ArrayList<>();
        if (request.getIngredients() != null) {
            for (DishIngredientRequest dir : request.getIngredients()) {
                IngredientsEntity ingredient = ingredientsRepository.findById(dir.getIngredient().getId())
                    .orElseThrow(() -> new NotFoundException("Ingredient not found"));
                
                DishIngredientsEntity di = new DishIngredientsEntity();
                di.setDish(dish);
                di.setIngredient(ingredient);
                di.setQuantity(BigDecimal.valueOf(dir.getQuantity()));
                dishIngredients.add(di);
            }
        }
        dish.setIngredients(dishIngredients);
        
        DishesEntity savedDish = dishesRepository.save(dish);
        return DishMapper.toResponse(savedDish);
    }

    public DishesResponse updateDish(Long id, DishesRequest request, Long restaurantId) {
        DishesEntity dish = dishesRepository.findByIdAndRestaurantId(id, restaurantId)
            .orElseThrow(() -> new NotFoundException("Dish not found"));
            
        if (request.getCategoryId() != null) {
            DishesCategoriesEntity category = categoriesRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category not found"));
            dish.setCategory(category);
        }
        
        dish.setName(request.getName());
        dish.setDescription(request.getDescription());
        dish.setPrice(request.getPrice());
        if (request.getAvailable() != null) {
            dish.setAvailable(request.getAvailable());
        }
        
        dish.getIngredients().clear();
        
        if (request.getIngredients() != null) {
            for (DishIngredientRequest dir : request.getIngredients()) {
                IngredientsEntity ingredient = ingredientsRepository.findById(dir.getIngredient().getId())
                    .orElseThrow(() -> new NotFoundException("Ingredient not found"));
                
                DishIngredientsEntity di = new DishIngredientsEntity();
                di.setDish(dish);
                di.setIngredient(ingredient);
                di.setQuantity(BigDecimal.valueOf(dir.getQuantity()));
                dish.getIngredients().add(di);
            }
        }
        
        DishesEntity savedDish = dishesRepository.save(dish);
        return DishMapper.toResponse(savedDish);
    }

    public void deleteDish(Long id, Long restaurantId) {
        DishesEntity dish = dishesRepository.findByIdAndRestaurantId(id, restaurantId)
            .orElseThrow(() -> new NotFoundException("Dish not found"));
        dishesRepository.delete(dish);
    }

    public List<String> getAllCategories(Long restaurantId) {
        List<String> categories = categoriesRepository
            .findByRestaurantId(restaurantId)
            .stream()
            .map(DishesCategoriesEntity::getName)
            .collect(Collectors.toList());

        return categories;
    }
}
