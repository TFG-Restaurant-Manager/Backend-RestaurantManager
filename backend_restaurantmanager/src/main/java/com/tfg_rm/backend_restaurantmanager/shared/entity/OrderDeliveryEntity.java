package com.tfg_rm.backend_restaurantmanager.shared.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa una orden de delivery en el restaurante.
 */
@Data
@Entity
@Table(name = "order_delivery")
@NoArgsConstructor
public class OrderDeliveryEntity {

    /**
     * Identificador único de la orden de delivery, que coincide con el ID de la orden principal.
     */
    @Id
    @OneToOne
    @MapsId
    @JoinColumn(name = "order_id")
    private OrdersEntity order;

    /**
     * Dirección de entrega para la orden de delivery.
     */
    @Column(name = "delivery_address", nullable = false, length = 255)
    private String deliveryAddress;

    /**
     * Cliente asociado a la orden de delivery.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;
}