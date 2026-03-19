package com.tfg_rm.backend_restaurantmanager.employee.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tfg_rm.backend_restaurantmanager.employee.dto.EmployeeRegisterRequest;
import com.tfg_rm.backend_restaurantmanager.employee.repository.EmployeeRepository;
import com.tfg_rm.backend_restaurantmanager.employee.repository.RestaurantRepository;
import com.tfg_rm.backend_restaurantmanager.shared.entity.EmployeeEntity;
import com.tfg_rm.backend_restaurantmanager.shared.entity.RestaurantEntity;
import com.tfg_rm.backend_restaurantmanager.shared.exception.NotFoundException;
import com.tfg_rm.backend_restaurantmanager.shared.security.CustomUserDetails;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final RestaurantRepository restaurantRepository;

    private final PasswordEncoder passwordEncoder;
 
    public EmployeeEntity registerEmployee(EmployeeRegisterRequest request) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        

System.out.println("AUTH: " + auth);
System.out.println("PRINCIPAL: " + auth.getPrincipal());
System.out.println("CLASS: " + auth.getPrincipal().getClass());

if (auth == null || !(auth.getPrincipal() instanceof CustomUserDetails)) {
    throw new RuntimeException("User not authenticated");
}
        CustomUserDetails currentUser = (CustomUserDetails) auth.getPrincipal();

        currentUser.getRestaurantId();

        RestaurantEntity restaurant = restaurantRepository
            .findById(currentUser.getRestaurantId())
            .orElseThrow(() -> new NotFoundException("Restaurant not found"));

        EmployeeEntity employee = new EmployeeEntity();
        employee.setRestaurant(restaurant);
        employee.setName(request.getName());
        employee.setRoleName(request.getRoleName());
        employee.setActive(request.getActive());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setStartDate(request.getStartDate());
        employee.setEndDate(request.getEndDate());
        employee.setPositionNotes(request.getPositionNotes());
        employee.setCode(request.getCode());
        String passwordHash = passwordEncoder.encode(request.getPassword());
        employee.setPasswordHash(passwordHash);

        return employeeRepository.save(employee);
    }

}
