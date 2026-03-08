package com.tfg_rm.backend_restaurantmanager.employee.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HolaController {

    @GetMapping("/hola")
    public String holaMundo() {
        return "Hola Mundo desde Spring Boot y Docker!";
    }

    @GetMapping("/hola-json")
    public Map<String, String> holaJson() {
        return Map.of("mensaje", "Hola Mundo desde Spring Boot y Docker");
    }
}