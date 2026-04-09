package com.tfg_rm.backend_restaurantmanager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tfg_rm.backend_restaurantmanager.dto.EmployeeRegisterRequest;
import com.tfg_rm.backend_restaurantmanager.dto.EmployeeWithSchedulesResponse;
import com.tfg_rm.backend_restaurantmanager.dto.SchedulesRequest;
import com.tfg_rm.backend_restaurantmanager.dto.mappers.EmployeeInfoMapper;
import com.tfg_rm.backend_restaurantmanager.entity.EmployeeEntity;
import com.tfg_rm.backend_restaurantmanager.entity.RestaurantEntity;
import com.tfg_rm.backend_restaurantmanager.exception.NotFoundException;
import com.tfg_rm.backend_restaurantmanager.repository.EmployeeRepository;
import com.tfg_rm.backend_restaurantmanager.repository.RestaurantRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final RestaurantRepository restaurantRepository;

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
        EmployeeEntity employee = employeeRepository
            .findByIdWithSchedules(employeeId)
            .orElseThrow(() -> new NotFoundException("Employee not found"));

        if (employee.getRestaurant() == null || !restaurantId.equals(employee.getRestaurant().getId())) {
            throw new NotFoundException("Employee not found in this restaurant");
        }

        return EmployeeInfoMapper.toResponseWithSchedules(employee);
    }

    public List<EmployeeWithSchedulesResponse> getAllEmployees(Long restaurantId) {

        List<EmployeeEntity> employees = employeeRepository.findByRestaurantId(restaurantId);

        return employees.stream()
                .map(EmployeeInfoMapper::toResponseWithSchedules)
                .collect(Collectors.toList());
    }

    @Transactional
    public Boolean updateEmployee(EmployeeRegisterRequest request, Long id, Long restaurantId) {
        
        if(id == null) {
            throw new NotFoundException("Employee ID is required for update");
        }

        if(id == request.getId()) {
            throw new IllegalArgumentException("Employee ID in path and request body must match");
        }

        int employee = employeeRepository.updateEmployee(id, restaurantId);

        if (employee == 0) {
            throw new NotFoundException("Employee not found or no changes detected");
        }

        employeeRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found after update"));

        return true;
    }

    public Boolean deleteEmployee(Long id, Long restaurantId) {
        
        EmployeeEntity employee = employeeRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        if (employee.getRestaurant().getId() != restaurantId) {
            throw new NotFoundException("Employee not found");
        }
        
        employeeRepository.deleteById(id);

        return true;
    }

    public Boolean updatePassword(String request, Long id, Long restaurantId) {
                
        if(id == null) {
            throw new NotFoundException("Employee ID is required for update");
        }

        EmployeeEntity employee = employeeRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        if (employee.getRestaurant().getId() != restaurantId) {
            throw new NotFoundException("Employee not found");
        }

        String passwordHash = passwordEncoder.encode(request);
        employee.setPasswordHash(passwordHash);
        employeeRepository.save(employee);

        return true;
    }

    public Boolean updateSchedules(List<SchedulesRequest> request, Long id, Long restaurantId) {

        if(id == null) {
            throw new NotFoundException("Employee ID is required for update");
        }

        EmployeeEntity employee = employeeRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found"));

        if (employee.getRestaurant().getId() != restaurantId) {
            throw new NotFoundException("Employee not found");
        }

        request.forEach(entry ->{
            
            if(entry.getScheduleId() == null) {
                employee.getSchedules().add(EmployeeInfoMapper.toScheduleEntity(entry));
            } else {
                employee.getSchedules().stream()
                        .filter(schedule -> schedule.getId().equals(entry.getScheduleId()))
                        .findFirst()
                        .ifPresent(schedule -> {
                            schedule.setStartDatetime(entry.getStartTime());
                            schedule.setEndDatetime(entry.getEndTime());
                        });
            }
        });

        // employee.getSchedules().stream()
        //         .filter(schedule -> schedule.getId().equals(request.getScheduleId()))
        //         .findFirst()
        //         .ifPresent(schedule -> {
        //             schedule.getEntries().clear();
        //             request.getScheduleEntries().forEach(entry -> {
        //                 schedule.getEntries().add(EmployeeInfoMapper.toScheduleEntryEntity(entry));
        //             });
        //         });
        employeeRepository.save(employee);

        return true;
    }
}
