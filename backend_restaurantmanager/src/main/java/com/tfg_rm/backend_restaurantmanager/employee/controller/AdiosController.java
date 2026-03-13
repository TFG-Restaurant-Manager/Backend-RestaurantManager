package com.tfg_rm.backend_restaurantmanager.employee.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdiosController {

    @GetMapping("/adios")
    public String adiosMindo() {
        return "Adios Mundo desde Spring Boot y Docker!";
    }

    @GetMapping("/adios-json")
    public Map<String, String> adiosJson() {
        return Map.of("mensaje", "Adios Mundo desde Spring Boot y Docker");
    }

    @GetMapping("/a")
    public Map<String, String> a() {
        return Map.of("mensaje", "A");
    }
}