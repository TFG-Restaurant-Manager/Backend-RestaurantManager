package com.tfg_rm.backend_restaurantmanager.employee.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tfg_rm.backend_restaurantmanager.employee.dto.EmployeeScheduleDto;
import com.tfg_rm.backend_restaurantmanager.employee.dto.EmployeeWithSchedulesResponse;
import com.tfg_rm.backend_restaurantmanager.employee.dto.EmployeeRegisterRequest;
import com.tfg_rm.backend_restaurantmanager.employee.dto.RestaurantDishView;
import com.tfg_rm.backend_restaurantmanager.employee.dto.RestaurantTableOrderView;
import com.tfg_rm.backend_restaurantmanager.employee.repository.EmployeeRepository;
import com.tfg_rm.backend_restaurantmanager.employee.repository.RestaurantDishViewRepository;
import com.tfg_rm.backend_restaurantmanager.employee.repository.RestaurantTableOrderViewRepository;
import com.tfg_rm.backend_restaurantmanager.shared.entity.EmployeeEntity;
import com.tfg_rm.backend_restaurantmanager.shared.entity.RestaurantEntity;
import com.tfg_rm.backend_restaurantmanager.shared.exception.NotFoundException;
import com.tfg_rm.backend_restaurantmanager.shared.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final RestaurantRepository restaurantRepository;

    private final RestaurantDishViewRepository restaurantDishViewRepository;

    private final RestaurantTableOrderViewRepository restaurantTableOrderViewRepository;

    private final PasswordEncoder passwordEncoder;
 
    public EmployeeEntity registerEmployee(EmployeeRegisterRequest request, Long restaurantId) {

        RestaurantEntity restaurant = restaurantRepository
            .findById(restaurantId)
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

    public EmployeeWithSchedulesResponse getEmployeeInfo(Long restaurantId, Long employeeId) {
        EmployeeEntity employee = employeeRepository.findByIdWithSchedules(employeeId)
            .orElseThrow(() -> new NotFoundException("Employee not found"));

        if (employee.getRestaurant() == null || !restaurantId.equals(employee.getRestaurant().getId())) {
            throw new NotFoundException("Employee not found in this restaurant");
        }

        List<EmployeeScheduleDto> schedules = employee.getSchedules().stream()
            .map(s -> new EmployeeScheduleDto(s.getId(), s.getStartDatetime(), s.getEndDatetime()))
            .collect(Collectors.toList());

        return new EmployeeWithSchedulesResponse(
            employee.getId(),
            employee.getName(),
            employee.getRoleName(),
            employee.getActive(),
            employee.getEmail(),
            employee.getPhone(),
            employee.getStartDate(),
            employee.getEndDate(),
            employee.getPositionNotes(),
            employee.getCode(),
            employee.getRestaurant().getId(),
            employee.getRestaurant().getName(),
            schedules
        );
    }

    public List<RestaurantDishView> getRestaurantDishesFromView(Long restaurantId) {
        return restaurantDishViewRepository.findByRestaurantId(restaurantId).stream()
            .map(e -> new RestaurantDishView(
                e.getId(),
                e.getName(),
                e.getCategoryName(),
                e.getDescription(),
                e.getPrice(),
                e.getAvailable(),
                e.getRestaurantId()
            ))
            .collect(Collectors.toList());
    }

    public List<RestaurantTableOrderView> getRestaurantTableOrdersFromView(Long restaurantId) {
        return restaurantTableOrderViewRepository.findByRestaurantId(restaurantId).stream()
            .map(e -> new RestaurantTableOrderView(
                e.getOrderId(),
                e.getRestaurantId(),
                e.getTableId(),
                e.getStatusName(),
                e.getTotal(),
                e.getNotes(),
                e.getCreatedAt(),
                e.getDishId()
            ))
            .collect(Collectors.toList());
    }

}
