package com.tfg_rm.backend_restaurantmanager.shared.entity;

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

@Data
@Entity
@Table(name = "order_table")
@NoArgsConstructor
public class OrderTableEntity {

    @Id
    @OneToOne
    @MapsId
    @JoinColumn(name = "order_id")
    private OrdersEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private TablesRestaurantEntity table;
}