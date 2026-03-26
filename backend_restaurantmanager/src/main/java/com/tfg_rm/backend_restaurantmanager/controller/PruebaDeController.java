package com.tfg_rm.backend_restaurantmanager.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PruebaDeController {

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

    @GetMapping("/hola")
    public String holaMundo() {
        return "Hola Mundo desde Spring Boot y Docker!";
    }

    @GetMapping("/hola-json")
    public Map<String, String> holaJson() {
        return Map.of("mensaje", "Hola Mundo desde Spring Boot y Docker");
    }
}