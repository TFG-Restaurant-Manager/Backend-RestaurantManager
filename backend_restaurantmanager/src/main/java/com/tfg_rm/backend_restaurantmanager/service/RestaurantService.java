package com.tfg_rm.backend_restaurantmanager.service;

import java.time.LocalDate;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tfg_rm.backend_restaurantmanager.dto.RestaurantRequest;
import com.tfg_rm.backend_restaurantmanager.entity.EmployeeEntity;
import com.tfg_rm.backend_restaurantmanager.entity.RestaurantEntity;
import com.tfg_rm.backend_restaurantmanager.entity.RoleEntity;
import com.tfg_rm.backend_restaurantmanager.repository.EmployeeRepository;
import com.tfg_rm.backend_restaurantmanager.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeEntity createRestaurantAndManager(RestaurantRequest request) {
        // Create the Restaurant
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setPrefix(request.getPrefix());
        restaurant.setName(request.getNameRestaurant());
        restaurant.setDescription(request.getDescription());
        restaurant.setEmail(request.getEmailRestaurant());
        restaurant.setPhone(request.getPhoneRestaurant());
        restaurant.setAddress(request.getAddress());

        RestaurantEntity savedRestaurant = restaurantRepository.save(restaurant);

        // Create the Manager Employee
        EmployeeEntity manager = new EmployeeEntity();
        manager.setRestaurant(savedRestaurant);
        manager.setName(request.getNameEmployee());
        manager.setRoleName(RoleEntity.MANAGER);
        manager.setActive(true);
        manager.setEmail(request.getEmailEmployee());
        manager.setPhone(request.getPhoneEmployee());
        manager.setStartDate(LocalDate.now());
        manager.setCode(request.getCode());
        manager.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        return employeeRepository.save(manager);
    }

    public void deleteRestaurant(Long restaurantId) {
        restaurantRepository.deleteById(restaurantId);
    }
}
