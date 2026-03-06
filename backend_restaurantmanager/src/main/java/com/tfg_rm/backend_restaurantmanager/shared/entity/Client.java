package com.tfg_rm.backend_restaurantmanager.shared.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "clients")
public class Client {

    @Id
    private Long id;

    private String email;

    @Column(name = "password_hash")
    private String password;

    @Column(name = "restaurant_id")
    private Long restaurantId;
}