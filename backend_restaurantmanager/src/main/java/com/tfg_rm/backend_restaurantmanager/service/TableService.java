package com.tfg_rm.backend_restaurantmanager.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.tfg_rm.backend_restaurantmanager.dto.TableResponse;
import com.tfg_rm.backend_restaurantmanager.dto.mappers.TableMapper;
import com.tfg_rm.backend_restaurantmanager.repository.TablesRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TableService {

    private final TablesRepository tablesRepository;

    public List<TableResponse> getTableInfo(Long restaurantId) {
        List<TableResponse> tables = tablesRepository
            .findByRestaurantId(restaurantId)
            .stream()
            .map(TableMapper::toResponse)
            .collect(Collectors.toList());

        return tables;
    }
}
