package com.tfg_rm.backend_restaurantmanager.controller;

import com.tfg_rm.backend_restaurantmanager.dto.TableRequest;
import com.tfg_rm.backend_restaurantmanager.dto.TableResponse;
import com.tfg_rm.backend_restaurantmanager.security.JwtService;
import com.tfg_rm.backend_restaurantmanager.service.TableService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Java class used to manage the tables of the restaurant.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/table")
@Slf4j
public class TableController {

    /** The employee service for managing employee-related operations. */
    private final TableService tableService;

    /** The JWT service for token generation and validation. */
    private final JwtService jwtService;

    @GetMapping
    public ResponseEntity<List<TableResponse>> getAll(
        @RequestHeader("Authorization") String authHeader
    ) {
        /* Extract the token from the Authorization header */
        String token = authHeader.replace("Bearer ", "");

        /* Validate the token and extract user details */
        Long restaurantId = jwtService.getRestaurantId(token);

        List<TableResponse> info = tableService.getTableInfo(restaurantId);
        //log.info("Table info: " + info);
        return ResponseEntity.ok(info);
    }

    @PutMapping
    public ResponseEntity<List<TableResponse>> updateAll(
        @RequestHeader("Authorization") String authHeader,
        @RequestBody List<TableRequest> tableRequests
    ) {
        /* Extract the token from the Authorization header */
        String token = authHeader.replace("Bearer ", "");

        /* Validate the token and extract user details */
        Long restaurantId = jwtService.getRestaurantId(token);

        List<TableResponse> updatedTables = tableService.updateAllTables(restaurantId, tableRequests);
        return ResponseEntity.ok(updatedTables);
    }
}
