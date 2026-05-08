package com.tfg_rm.backend_restaurantmanager.service;

import com.tfg_rm.backend_restaurantmanager.dto.OrderItemRequest;
import com.tfg_rm.backend_restaurantmanager.dto.OrderRequest;
import com.tfg_rm.backend_restaurantmanager.dto.OrderResponse;
import com.tfg_rm.backend_restaurantmanager.entity.DishesEntity;
import com.tfg_rm.backend_restaurantmanager.entity.OrderItemsStatusEntity;
import com.tfg_rm.backend_restaurantmanager.entity.OrderStatusEntity;
import com.tfg_rm.backend_restaurantmanager.entity.OrderTypeEntity;
import com.tfg_rm.backend_restaurantmanager.entity.OrdersEntity;
import com.tfg_rm.backend_restaurantmanager.entity.RestaurantEntity;
import com.tfg_rm.backend_restaurantmanager.entity.TablesRestaurantEntity;
import com.tfg_rm.backend_restaurantmanager.exception.NotFoundException;
import com.tfg_rm.backend_restaurantmanager.exception.UnauthorizedException;
import com.tfg_rm.backend_restaurantmanager.repository.DishesRepository;
import com.tfg_rm.backend_restaurantmanager.repository.OrderRepository;
import com.tfg_rm.backend_restaurantmanager.repository.RestaurantRepository;
import com.tfg_rm.backend_restaurantmanager.repository.TablesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private TablesRepository tablesRepository;
    @Mock private DishesRepository dishesRepository;
    @Mock private RestaurantRepository restaurantRepository;
    @InjectMocks private OrderService orderService;

    private RestaurantEntity restaurant;
    private DishesEntity dish;
    private OrderItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        restaurant = new RestaurantEntity();
        restaurant.setId(10L);

        dish = new DishesEntity();
        dish.setId(1L);
        dish.setName("Paella");
        dish.setPrice(new BigDecimal("12.50"));
        dish.setRestaurant(restaurant);

        itemRequest = new OrderItemRequest();
        itemRequest.setDishId(1L);
        itemRequest.setStatus("CREATED");
        itemRequest.setItemNotes("Sin sal");
    }

    // ── createOrder: delivery ──────────────────────────────────────

    @Test
    void createOrder_deliveryType_savesAndReturnsResponse() {
        stubRepositoriesForCreate();
        when(orderRepository.save(any())).thenAnswer(inv -> savedWithId(inv.getArgument(0), 1L));

        OrderResponse response = orderService.createOrder(10L, deliveryRequest("Calle Mayor 1"));

        assertThat(response).isNotNull();
        assertThat(response.getType()).isEqualTo("DELIVERY");
        assertThat(response.getStatus()).isEqualTo("CREATED");
        assertThat(response.getTotal()).isEqualByComparingTo("12.50");
    }

    // ── createOrder: pickup ────────────────────────────────────────

    @Test
    void createOrder_pickupType_savesAndReturnsResponse() {
        stubRepositoriesForCreate();
        when(orderRepository.save(any())).thenAnswer(inv -> savedWithId(inv.getArgument(0), 2L));

        OrderResponse response = orderService.createOrder(10L, pickupRequest(LocalDateTime.now().plusHours(1)));

        assertThat(response.getType()).isEqualTo("PICKUP");
        assertThat(response.getStatus()).isEqualTo("CREATED");
    }

    // ── createOrder: table ─────────────────────────────────────────

    @Test
    void createOrder_tableType_savesAndReturnsResponse() {
        TablesRestaurantEntity table = new TablesRestaurantEntity();
        table.setId(5L);
        table.setRestaurant(restaurant);
        table.setOrderTables(new ArrayList<>());

        stubRepositoriesForCreate();
        when(tablesRepository.findById(5L)).thenReturn(Optional.of(table));
        when(orderRepository.save(any())).thenAnswer(inv -> savedWithId(inv.getArgument(0), 3L));

        OrderResponse response = orderService.createOrder(10L, tableRequest(5L));

        assertThat(response).isNotNull();
        assertThat(response.getType()).isEqualTo("TABLE");
    }

    // ── createOrder: restaurant not found ─────────────────────────

    @Test
    void createOrder_restaurantNotFound_throwsNotFoundException() {
        when(restaurantRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(10L, deliveryRequest("Calle 1")))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Restaurant not found");
    }

    // ── createOrder: table not found ──────────────────────────────

    @Test
    void createOrder_tableNotFound_throwsNotFoundException() {
        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));
        when(tablesRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(10L, tableRequest(99L)))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Table not found");
    }

    // ── createOrder: table from different restaurant ───────────────

    @Test
    void createOrder_tableFromDifferentRestaurant_throwsUnauthorizedException() {
        // Uses ID 200 (>127) to leave the Integer cache and prove == would fail
        RestaurantEntity other = new RestaurantEntity();
        other.setId(200L);

        TablesRestaurantEntity foreignTable = new TablesRestaurantEntity();
        foreignTable.setId(5L);
        foreignTable.setRestaurant(other);
        foreignTable.setOrderTables(new ArrayList<>());

        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));
        when(tablesRepository.findById(5L)).thenReturn(Optional.of(foreignTable));

        assertThatThrownBy(() -> orderService.createOrder(10L, tableRequest(5L)))
                .isInstanceOf(UnauthorizedException.class);
    }

    // ── createOrder: dish not found ────────────────────────────────

    @Test
    void createOrder_dishNotFound_throwsNotFoundException() {
        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));
        when(dishesRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(10L, deliveryRequest("Calle 1")))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Dish not found");
    }

    // ── createOrder: dish from different restaurant ────────────────

    @Test
    void createOrder_dishFromDifferentRestaurant_throwsUnauthorizedException() {
        RestaurantEntity other = new RestaurantEntity();
        other.setId(20L);

        DishesEntity foreignDish = new DishesEntity();
        foreignDish.setId(1L);
        foreignDish.setRestaurant(other);
        foreignDish.setPrice(new BigDecimal("10.00"));

        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));
        when(dishesRepository.findById(1L)).thenReturn(Optional.of(foreignDish));

        assertThatThrownBy(() -> orderService.createOrder(10L, deliveryRequest("Calle 1")))
                .isInstanceOf(UnauthorizedException.class);
    }

    // ── getAllOrders ───────────────────────────────────────────────

    @Test
    void getAllOrders_returnsAllOrdersForRestaurant() {
        when(orderRepository.findByRestaurantId(10L))
                .thenReturn(List.of(minimalOrder(1L, OrderStatusEntity.CREATED, OrderTypeEntity.DELIVERY)));

        List<OrderResponse> result = orderService.getAllOrders(10L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getType()).isEqualTo("DELIVERY");
    }

    @Test
    void getAllOrders_noOrders_returnsEmptyList() {
        when(orderRepository.findByRestaurantId(10L)).thenReturn(List.of());

        assertThat(orderService.getAllOrders(10L)).isEmpty();
    }

    // ── getAllOrdersPaid ───────────────────────────────────────────

    @Test
    void getAllOrdersPaid_returnsOnlyPaidOrders() {
        when(orderRepository.findByRestaurantIdAndStatus(10L, OrderStatusEntity.PAID))
                .thenReturn(List.of(minimalOrder(2L, OrderStatusEntity.PAID, OrderTypeEntity.PICKUP)));

        List<OrderResponse> result = orderService.getAllOrdersPaid(10L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo("PAID");
    }

    @Test
    void getAllOrdersPaid_noResults_returnsEmptyList() {
        when(orderRepository.findByRestaurantIdAndStatus(10L, OrderStatusEntity.PAID))
                .thenReturn(List.of());

        assertThat(orderService.getAllOrdersPaid(10L)).isEmpty();
    }

    // ── updateOrder ────────────────────────────────────────────────

    @Test
    void updateOrder_orderNotFound_throwsNotFoundException() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.updateOrder(10L, updateRequest(999L)))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateOrder_orderFromDifferentRestaurant_throwsUnauthorizedException() {
        RestaurantEntity other = new RestaurantEntity();
        other.setId(99L);

        OrdersEntity existingOrder = minimalOrder(1L, OrderStatusEntity.CREATED, OrderTypeEntity.DELIVERY);
        existingOrder.setRestaurant(other);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));

        assertThatThrownBy(() -> orderService.updateOrder(10L, updateRequest(1L)))
                .isInstanceOf(UnauthorizedException.class);
    }

    // ── Helpers ───────────────────────────────────────────────────

    private void stubRepositoriesForCreate() {
        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));
        when(dishesRepository.findById(1L)).thenReturn(Optional.of(dish));
    }

    private OrdersEntity savedWithId(OrdersEntity entity, Long id) {
        entity.setId(id);
        if (entity.getOrderItems() == null) {
            entity.setOrderItems(new ArrayList<>());
        }
        return entity;
    }

    private OrderRequest deliveryRequest(String address) {
        OrderRequest req = new OrderRequest();
        req.setType("DELIVERY");
        req.setDeliveryAddress(address);
        req.setCreatedAt(LocalDateTime.now());
        req.setItems(List.of(itemRequest));
        return req;
    }

    private OrderRequest pickupRequest(LocalDateTime time) {
        OrderRequest req = new OrderRequest();
        req.setType("PICKUP");
        req.setPickupTime(time);
        req.setCreatedAt(LocalDateTime.now());
        req.setItems(List.of(itemRequest));
        return req;
    }

    private OrderRequest tableRequest(Long tableId) {
        OrderRequest req = new OrderRequest();
        req.setType("TABLE");
        req.setTableId(tableId);
        req.setCreatedAt(LocalDateTime.now());
        req.setItems(List.of(itemRequest));
        return req;
    }

    private OrderRequest updateRequest(Long orderId) {
        OrderRequest req = new OrderRequest();
        req.setId(orderId);
        req.setStatus("CREATED");
        req.setItems(List.of());
        return req;
    }

    private OrdersEntity minimalOrder(Long id, OrderStatusEntity status, OrderTypeEntity type) {
        OrdersEntity o = new OrdersEntity();
        o.setId(id);
        o.setRestaurant(restaurant);
        o.setStatus(status);
        o.setType(type);
        o.setTotal(BigDecimal.TEN);
        o.setCreatedAt(LocalDateTime.now());
        o.setOrderItems(new ArrayList<>());
        return o;
    }
}
