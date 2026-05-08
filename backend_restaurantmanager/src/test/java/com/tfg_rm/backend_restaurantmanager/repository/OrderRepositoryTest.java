package com.tfg_rm.backend_restaurantmanager.repository;

import com.tfg_rm.backend_restaurantmanager.entity.OrderStatusEntity;
import com.tfg_rm.backend_restaurantmanager.entity.OrderTypeEntity;
import com.tfg_rm.backend_restaurantmanager.entity.OrdersEntity;
import com.tfg_rm.backend_restaurantmanager.entity.RestaurantEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Sql("/test-defaults.sql")
class OrderRepositoryTest {

    @Autowired
    TestEntityManager em;
    @Autowired
    OrderRepository orderRepository;

    private RestaurantEntity restaurant1;
    private RestaurantEntity restaurant2;

    @BeforeEach
    void setUp() {
        restaurant1 = persistRestaurant("R1", "Restaurante 1", "r1@test.com");
        restaurant2 = persistRestaurant("R2", "Restaurante 2", "r2@test.com");
    }

    // ── findByRestaurantId ─────────────────────────────────────────

    @Test
    void findByRestaurantId_returnsOnlyOrdersOfThatRestaurant() {
        persistOrder(restaurant1, OrderStatusEntity.CREATED, OrderTypeEntity.DELIVERY);
        persistOrder(restaurant1, OrderStatusEntity.PAID, OrderTypeEntity.PICKUP);
        persistOrder(restaurant2, OrderStatusEntity.CREATED, OrderTypeEntity.DELIVERY);
        em.flush();

        List<OrdersEntity> result = orderRepository.findByRestaurantId(restaurant1.getId());

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(o -> o.getRestaurant().getId().equals(restaurant1.getId()));
    }

    @Test
    void findByRestaurantId_noOrders_returnsEmptyList() {
        assertThat(orderRepository.findByRestaurantId(restaurant1.getId())).isEmpty();
    }

    @Test
    void findByRestaurantId_doesNotReturnOtherRestaurantOrders() {
        persistOrder(restaurant2, OrderStatusEntity.CREATED, OrderTypeEntity.DELIVERY);
        em.flush();

        assertThat(orderRepository.findByRestaurantId(restaurant1.getId())).isEmpty();
    }

    // ── findByRestaurantIdAndStatus ────────────────────────────────

    @Test
    void findByRestaurantIdAndStatus_returnsOnlyPaidOrders() {
        persistOrder(restaurant1, OrderStatusEntity.CREATED, OrderTypeEntity.DELIVERY);
        persistOrder(restaurant1, OrderStatusEntity.PAID, OrderTypeEntity.PICKUP);
        persistOrder(restaurant1, OrderStatusEntity.PAID, OrderTypeEntity.DELIVERY);
        em.flush();

        List<OrdersEntity> result = orderRepository.findByRestaurantIdAndStatus(
                restaurant1.getId(), OrderStatusEntity.PAID);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(o -> o.getStatus() == OrderStatusEntity.PAID);
    }

    @Test
    void findByRestaurantIdAndStatus_differentRestaurant_returnsEmpty() {
        persistOrder(restaurant2, OrderStatusEntity.PAID, OrderTypeEntity.DELIVERY);
        em.flush();

        List<OrdersEntity> result = orderRepository.findByRestaurantIdAndStatus(
                restaurant1.getId(), OrderStatusEntity.PAID);

        assertThat(result).isEmpty();
    }

    @Test
    void findByRestaurantIdAndStatus_noMatchingStatus_returnsEmpty() {
        persistOrder(restaurant1, OrderStatusEntity.CREATED, OrderTypeEntity.DELIVERY);
        em.flush();

        List<OrdersEntity> result = orderRepository.findByRestaurantIdAndStatus(
                restaurant1.getId(), OrderStatusEntity.PAID);

        assertThat(result).isEmpty();
    }

    // ── Helpers ───────────────────────────────────────────────────

    private RestaurantEntity persistRestaurant(String prefix, String name, String email) {
        RestaurantEntity r = new RestaurantEntity();
        r.setPrefix(prefix);
        r.setName(name);
        r.setEmail(email);
        r.setPhone("600000000");
        r.setAddress("Test Address 1");
        r.setCreatedAt(LocalDateTime.now());
        return em.persist(r);
    }

    private OrdersEntity persistOrder(RestaurantEntity restaurant,
            OrderStatusEntity status,
            OrderTypeEntity type) {
        OrdersEntity o = new OrdersEntity();
        o.setRestaurant(restaurant);
        o.setStatus(status);
        o.setType(type);
        o.setTotal(BigDecimal.TEN);
        o.setCreatedAt(LocalDateTime.now());
        return em.persist(o);
    }
}
