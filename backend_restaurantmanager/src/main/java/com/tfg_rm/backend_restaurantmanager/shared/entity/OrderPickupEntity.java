package com.tfg_rm.backend_restaurantmanager.shared.entity;

import java.time.LocalDateTime;

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
 * Entidad que representa una orden de recogida.
 */
@Data
@Entity
@Table(name = "order_pickup")
@NoArgsConstructor
public class OrderPickupEntity {

    /**
     * Identificador único de la orden de recogida, que coincide con el ID de la orden principal.
     */
    @Id
    @OneToOne
    @MapsId
    @JoinColumn(name = "order_id")
    private OrdersEntity order;

    /**
     * Fecha y hora programada para la recogida de la orden.
     */
    @Column(name = "pickup_time", nullable = false)
    private LocalDateTime pickupTime;

    /**
     * Cliente asociado a la orden de delivery.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private ClientEntity client;
}