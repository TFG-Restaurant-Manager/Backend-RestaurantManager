# Plan de Testing — Login & Orders (Spring Boot 4, Java 21)

> Estrategia pragmática: máximo valor con mínimo mantenimiento.
> Alcance: `AuthController`, `OrderController`, `AuthService`, `OrderService`, `OrderRepository`, `AuthEmployeeRepository`.

---

## 0. Bloqueantes — resolver ANTES de escribir un test

### 0.1 — pom.xml roto (los tests no compilarán)

Las 5 dependencias de test actuales no existen como artefactos Maven. Sustitúyelas todas:

```xml
<!-- ELIMINAR estas (no existen en Maven Central): -->
<!-- spring-boot-starter-data-jpa-test       -->
<!-- spring-boot-starter-security-test       -->
<!-- spring-boot-starter-validation-test     -->
<!-- spring-boot-starter-webmvc-test         -->
<!-- spring-boot-starter-websocket-test      -->

<!-- AÑADIR en su lugar: -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <!-- incluye JUnit 5, Mockito, AssertJ, MockMvc, JSONPath -->
</dependency>
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

### 0.2 — Bug real en OrderService (los tests lo expondrán)

`createOrder` y `updateOrder` usan `!=` para comparar objetos `Long`.
Para IDs > 127 (fuera del cache de Integer), la comparación de referencias falla aunque los valores sean iguales.

```java
// BUG — compara referencias, no valores
if (table.getRestaurant().getId() != restaurantId)

// CORRECCIÓN (aplica en los 4 sitios de OrderService)
if (!Objects.equals(table.getRestaurant().getId(), restaurantId))
```

---

## 1. Configuración de tests

### src/test/resources/application-test.properties

```properties
# H2 en memoria
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false

# JWT — mínimo 32 bytes para HS256
jwt.secret=test-secret-key-must-be-at-least-32-chars!!

# Silenciar logs en tests
logging.level.org.springframework.security=WARN
logging.level.org.hibernate.SQL=WARN
```

> **Nota sobre enums**: Las entidades usan `@JdbcTypeCode(SqlTypes.NAMED_ENUM)` (enum nativo de PostgreSQL).
> Con `H2Dialect` + `create-drop`, Hibernate debería mapearlos a VARCHAR automáticamente.
> Si `@DataJpaTest` falla con errores de tipo, ve a la sección de Troubleshooting al final.

### src/test/java/.../util/JwtTestHelper.java

```java
package com.tfg_rm.backend_restaurantmanager.util;

import com.tfg_rm.backend_restaurantmanager.dto.Role;
import com.tfg_rm.backend_restaurantmanager.security.JwtService;

/**
 * Genera tokens JWT reales para usar en controller tests.
 * Usa el mismo JwtService de producción con el secret de test.
 */
public class JwtTestHelper {

    public static final String TEST_SECRET   = "test-secret-key-must-be-at-least-32-chars!!";
    public static final Long   EMPLOYEE_ID   = 1L;
    public static final Long   RESTAURANT_ID = 10L;

    private static final JwtService jwtService = new JwtService(TEST_SECRET);

    public static String generateToken(Role role) {
        return jwtService.generateToken(EMPLOYEE_ID, RESTAURANT_ID, role);
    }

    public static String waiterToken()  { return generateToken(Role.WAITER);  }
    public static String managerToken() { return generateToken(Role.MANAGER); }

    public static JwtService jwtService() { return jwtService; }
}
```

---

## 2. Estructura de carpetas

```
src/test/java/com/tfg_rm/backend_restaurantmanager/
├── util/
│   └── JwtTestHelper.java              ← helper compartido
├── security/
│   └── JwtServiceTest.java             ← JUnit 5 puro, sin Spring
├── service/
│   ├── AuthServiceTest.java            ← @ExtendWith(MockitoExtension)
│   └── OrderServiceTest.java           ← @ExtendWith(MockitoExtension)
├── controller/
│   ├── AuthControllerTest.java         ← @WebMvcTest + MockMvc
│   └── OrderControllerTest.java        ← @WebMvcTest + MockMvc
└── repository/
    ├── OrderRepositoryTest.java         ← @DataJpaTest + H2
    └── AuthEmployeeRepositoryTest.java  ← @DataJpaTest + H2

src/test/resources/
└── application-test.properties
```

**Naming convention**: `methodName_scenario_expectedResult`
Ejemplo: `validateEmployeeAccess_wrongPassword_returnsMinusOne`

---

## 3. Qué testear y qué no

| Testear | Por qué |
|---|---|
| `JwtService.generateToken` / `validateToken` / claims | Seguridad crítica — bug aquí afecta a todos los endpoints |
| `AuthService.validateEmployeeAccess` | Lógica de login con bcrypt |
| `AuthService.getEmployeeRole` | Mapeo RoleEntity → Role |
| `OrderService.createOrder` (3 tipos + errores) | Lógica de negocio con validaciones cross-tenant |
| `OrderService.updateOrder` | Modificación de items, validación de pertenencia |
| `AuthController POST /auth/employeeLogin` | Contrato HTTP, status codes, JWT en respuesta |
| `OrderController GET /order` y `GET /order/paid` | Seguridad: 401 sin token, 200 con token válido |
| `OrderRepository` custom JPQL queries | Fácil de romper al cambiar el modelo |
| `AuthEmployeeRepository.findByCode` | Crítico para el flujo de login |

| No testear | Por qué |
|---|---|
| `findAll`, `save`, `findById` de Spring Data | Son del framework, no tuyo |
| Mappers / DTOs sin lógica | Sin riesgo real |
| `GlobalExceptionHandler` aislado | Se cubre implícitamente en controller tests |
| Getters / setters Lombok | Sin riesgo |
| `WebSocketConfig`, `WebSocketHandler` | Alta complejidad, bajo valor para negocio core |
| `EmployeeController`, `DishesController` | Fuera del alcance definido |

---

## 4. Anotaciones — cuándo usar cada una

| Anotación | Cuándo | Velocidad |
|---|---|---|
| Sin anotación Spring | Services / utils puros con Mockito | Muy rápido |
| `@ExtendWith(MockitoExtension.class)` | Unit tests con `@Mock` + `@InjectMocks` | Muy rápido |
| `@WebMvcTest(Controller.class)` | Tests HTTP: status codes, seguridad, JSON | Rápido (solo capa web) |
| `@DataJpaTest` | Repositorios con H2: queries custom | Medio (solo JPA) |
| `@SpringBootTest` | **No usar** en este alcance — demasiado lento | Lento |
| `@MockitoBean` | Mockear beans Spring en @WebMvcTest / @DataJpaTest | — |
| `@Mock` + `@InjectMocks` | Mockear en tests sin contexto Spring | — |
| `@ActiveProfiles("test")` | Activar application-test.properties | — |

> Spring Boot 4.x usa `@MockitoBean` (paquete `org.springframework.test.context.bean.override.mockito`).
> `@MockBean` está deprecado en Spring Boot 4.

---

## 5. JwtServiceTest — sin Spring, JUnit 5 puro

```java
package com.tfg_rm.backend_restaurantmanager.security;

import com.tfg_rm.backend_restaurantmanager.dto.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService("test-secret-key-must-be-at-least-32-chars!!");
    }

    @Test
    void generateToken_returnsNonNullToken() {
        String token = jwtService.generateToken(1L, 10L, Role.WAITER);
        assertThat(token).isNotNull().isNotBlank();
    }

    @Test
    void generateToken_containsCorrectRestaurantId() {
        String token = jwtService.generateToken(1L, 10L, Role.WAITER);
        assertThat(jwtService.getRestaurantId(token)).isEqualTo(10L);
    }

    @Test
    void generateToken_containsCorrectUserId() {
        String token = jwtService.generateToken(42L, 10L, Role.MANAGER);
        assertThat(jwtService.getUserId(token)).isEqualTo(42L);
    }

    @Test
    void generateToken_containsCorrectRole() {
        String token = jwtService.generateToken(1L, 10L, Role.COOKER);
        assertThat(jwtService.getRole(token)).isEqualTo("COOKER");
    }

    @Test
    void validateToken_validToken_returnsTrue() {
        String token = jwtService.generateToken(1L, 10L, Role.WAITER);
        assertThat(jwtService.validateToken(token)).isTrue();
    }

    @Test
    void validateToken_tamperedToken_returnsFalse() {
        String token = jwtService.generateToken(1L, 10L, Role.WAITER);
        String tampered = token.substring(0, token.length() - 5) + "XXXXX";
        assertThat(jwtService.validateToken(tampered)).isFalse();
    }

    @Test
    void validateToken_randomString_returnsFalse() {
        assertThat(jwtService.validateToken("not.a.token")).isFalse();
    }

    @Test
    void validateToken_tokenFromDifferentSecret_returnsFalse() {
        JwtService other = new JwtService("completely-different-secret-at-least-32chars!!");
        String foreignToken = other.generateToken(1L, 10L, Role.WAITER);
        assertThat(jwtService.validateToken(foreignToken)).isFalse();
    }
}
```

---

## 6. AuthServiceTest — Mockito puro

```java
package com.tfg_rm.backend_restaurantmanager.service;

import com.tfg_rm.backend_restaurantmanager.dto.Role;
import com.tfg_rm.backend_restaurantmanager.entity.EmployeeEntity;
import com.tfg_rm.backend_restaurantmanager.entity.RestaurantEntity;
import com.tfg_rm.backend_restaurantmanager.entity.RoleEntity;
import com.tfg_rm.backend_restaurantmanager.repository.AuthEmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private AuthEmployeeRepository authEmployeeRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @InjectMocks private AuthService authService;

    private EmployeeEntity employee;

    @BeforeEach
    void setUp() {
        RestaurantEntity restaurant = new RestaurantEntity();
        restaurant.setId(10L);

        employee = new EmployeeEntity();
        employee.setId(1L);
        employee.setCode("EMP01");
        employee.setPasswordHash("$2a$10$hashed");
        employee.setRoleName(RoleEntity.WAITER);
        employee.setRestaurant(restaurant);
        employee.setActive(true);
    }

    @Test
    void validateEmployeeAccess_correctCredentials_returnsEmployeeId() {
        when(authEmployeeRepository.findByCode("EMP01")).thenReturn(Optional.of(employee));
        when(passwordEncoder.matches("plainPassword", "$2a$10$hashed")).thenReturn(true);

        assertThat(authService.validateEmployeeAccess("EMP01", "plainPassword")).isEqualTo(1L);
    }

    @Test
    void validateEmployeeAccess_wrongPassword_returnsMinusOne() {
        when(authEmployeeRepository.findByCode("EMP01")).thenReturn(Optional.of(employee));
        when(passwordEncoder.matches("wrong", "$2a$10$hashed")).thenReturn(false);

        assertThat(authService.validateEmployeeAccess("EMP01", "wrong")).isEqualTo(-1L);
    }

    @Test
    void validateEmployeeAccess_unknownCode_returnsMinusOne() {
        when(authEmployeeRepository.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThat(authService.validateEmployeeAccess("UNKNOWN", "any")).isEqualTo(-1L);
    }

    @Test
    void getEmployeeRole_waiter_returnsWaiterRole() {
        employee.setRoleName(RoleEntity.WAITER);
        when(authEmployeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        assertThat(authService.getEmployeeRole(1L)).isEqualTo(Role.WAITER);
    }

    @Test
    void getEmployeeRole_manager_returnsManagerRole() {
        employee.setRoleName(RoleEntity.MANAGER);
        when(authEmployeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        assertThat(authService.getEmployeeRole(1L)).isEqualTo(Role.MANAGER);
    }

    @Test
    void getEmployeeRole_cooker_returnsCookerRole() {
        employee.setRoleName(RoleEntity.COOKER);
        when(authEmployeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        assertThat(authService.getEmployeeRole(1L)).isEqualTo(Role.COOKER);
    }

    @Test
    void getEmployeeRestaurantId_existingEmployee_returnsRestaurantId() {
        when(authEmployeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        assertThat(authService.getEmployeeRestaurantId(1L)).isEqualTo(10L);
    }

    @Test
    void getEmployeeRestaurantId_nonExistentEmployee_returnsNull() {
        when(authEmployeeRepository.findById(999L)).thenReturn(Optional.empty());
        assertThat(authService.getEmployeeRestaurantId(999L)).isNull();
    }
}
```

---

## 7. OrderServiceTest — Mockito puro

```java
package com.tfg_rm.backend_restaurantmanager.service;

import com.tfg_rm.backend_restaurantmanager.dto.OrderItemRequest;
import com.tfg_rm.backend_restaurantmanager.dto.OrderRequest;
import com.tfg_rm.backend_restaurantmanager.dto.OrderResponse;
import com.tfg_rm.backend_restaurantmanager.entity.*;
import com.tfg_rm.backend_restaurantmanager.exception.NotFoundException;
import com.tfg_rm.backend_restaurantmanager.exception.UnauthorizedException;
import com.tfg_rm.backend_restaurantmanager.repository.*;
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

    // ── createOrder: tipos ─────────────────────────────────────────

    @Test
    void createOrder_deliveryType_savesAndReturnsResponse() {
        OrderRequest request = deliveryRequest("Calle Mayor 1");
        stubRepositoriesForCreate();
        when(orderRepository.save(any())).thenAnswer(inv -> {
            OrdersEntity o = inv.getArgument(0);
            o.setId(1L);
            return o;
        });

        OrderResponse response = orderService.createOrder(10L, request);

        assertThat(response).isNotNull();
        assertThat(response.getType()).isEqualTo("DELIVERY");
        assertThat(response.getStatus()).isEqualTo("CREATED");
    }

    @Test
    void createOrder_pickupType_savesAndReturnsResponse() {
        OrderRequest request = pickupRequest(LocalDateTime.now().plusHours(1));
        stubRepositoriesForCreate();
        when(orderRepository.save(any())).thenAnswer(inv -> {
            OrdersEntity o = inv.getArgument(0);
            o.setId(2L);
            return o;
        });

        OrderResponse response = orderService.createOrder(10L, request);
        assertThat(response.getType()).isEqualTo("PICKUP");
    }

    @Test
    void createOrder_tableType_savesAndReturnsResponse() {
        TablesRestaurantEntity table = new TablesRestaurantEntity();
        table.setId(5L);
        table.setRestaurant(restaurant);
        table.setOrderTables(new ArrayList<>());

        stubRepositoriesForCreate();
        when(tablesRepository.findById(5L)).thenReturn(Optional.of(table));
        when(orderRepository.save(any())).thenAnswer(inv -> {
            OrdersEntity o = inv.getArgument(0);
            o.setId(3L);
            return o;
        });

        OrderResponse response = orderService.createOrder(10L, tableRequest(5L));
        assertThat(response).isNotNull();
    }

    // ── createOrder: errores ───────────────────────────────────────

    @Test
    void createOrder_restaurantNotFound_throwsNotFoundException() {
        when(restaurantRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(10L, deliveryRequest("Calle 1")))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Restaurant not found");
    }

    @Test
    void createOrder_tableNotFound_throwsNotFoundException() {
        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));
        when(tablesRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(10L, tableRequest(99L)))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Table not found");
    }

    @Test
    void createOrder_tableFromDifferentRestaurant_throwsUnauthorizedException() {
        // Este test verifica el bug de != en Long corregido con Objects.equals()
        // Usa ID 200 (> 127) para salir del cache de Integer y exponer el bug
        RestaurantEntity other = new RestaurantEntity();
        other.setId(200L);

        TablesRestaurantEntity foreignTable = new TablesRestaurantEntity();
        foreignTable.setId(5L);
        foreignTable.setRestaurant(other);      // pertenece a restaurante 200
        foreignTable.setOrderTables(new ArrayList<>());

        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));  // JWT de restaurante 10
        when(tablesRepository.findById(5L)).thenReturn(Optional.of(foreignTable));

        // La mesa es del restaurante 200, el JWT dice restaurante 10 → UnauthorizedException
        assertThatThrownBy(() -> orderService.createOrder(10L, tableRequest(5L)))
                .isInstanceOf(UnauthorizedException.class);
    }

    @Test
    void createOrder_dishNotFound_throwsNotFoundException() {
        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));
        when(dishesRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(10L, deliveryRequest("Calle 1")))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Dish not found");
    }

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

    // ── getAllOrders / getAllOrdersPaid ─────────────────────────────

    @Test
    void getAllOrders_returnsAllOrdersForRestaurant() {
        OrdersEntity order = minimalOrder(1L, OrderStatusEntity.CREATED, OrderTypeEntity.DELIVERY);
        when(orderRepository.findByRestaurantId(10L)).thenReturn(List.of(order));

        List<OrderResponse> result = orderService.getAllOrders(10L);
        assertThat(result).hasSize(1);
    }

    @Test
    void getAllOrders_noOrders_returnsEmptyList() {
        when(orderRepository.findByRestaurantId(10L)).thenReturn(List.of());
        assertThat(orderService.getAllOrders(10L)).isEmpty();
    }

    @Test
    void getAllOrdersPaid_returnsOnlyPaidOrders() {
        OrdersEntity paid = minimalOrder(2L, OrderStatusEntity.PAID, OrderTypeEntity.PICKUP);
        when(orderRepository.findByRestaurantIdAndStatus(10L, OrderStatusEntity.PAID))
                .thenReturn(List.of(paid));

        List<OrderResponse> result = orderService.getAllOrdersPaid(10L);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo("PAID");
    }

    // ── updateOrder ────────────────────────────────────────────────

    @Test
    void updateOrder_orderNotFound_throwsNotFoundException() {
        OrderRequest req = updateRequest(999L);
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.updateOrder(10L, req))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateOrder_orderFromDifferentRestaurant_throwsUnauthorizedException() {
        RestaurantEntity other = new RestaurantEntity();
        other.setId(99L);

        OrdersEntity existingOrder = new OrdersEntity();
        existingOrder.setId(1L);
        existingOrder.setRestaurant(other);
        existingOrder.setOrderItems(new ArrayList<>());

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));

        assertThatThrownBy(() -> orderService.updateOrder(10L, updateRequest(1L)))
                .isInstanceOf(UnauthorizedException.class);
    }

    // ── Helpers ───────────────────────────────────────────────────

    private void stubRepositoriesForCreate() {
        when(restaurantRepository.findById(10L)).thenReturn(Optional.of(restaurant));
        when(dishesRepository.findById(1L)).thenReturn(Optional.of(dish));
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
        o.setOrderItems(List.of());
        return o;
    }
}
```

---

## 8. AuthControllerTest — @WebMvcTest + MockMvc

> `/auth/**` es `permitAll()` — no se necesita JWT en estos tests.

```java
package com.tfg_rm.backend_restaurantmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg_rm.backend_restaurantmanager.dto.Role;
import com.tfg_rm.backend_restaurantmanager.security.JwtService;
import com.tfg_rm.backend_restaurantmanager.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean AuthService authService;
    @MockitoBean JwtService jwtService;  // necesario para SecurityConfig + JwtAuthenticationFilter

    // ── Login correcto ─────────────────────────────────────────────

    @Test
    void employeeLogin_validCredentials_returns200WithTokenAndRole() throws Exception {
        when(authService.validateEmployeeAccess("EMP01", "pass123")).thenReturn(1L);
        when(authService.getEmployeeRole(1L)).thenReturn(Role.WAITER);
        when(authService.getEmployeeRestaurantId(1L)).thenReturn(10L);
        when(jwtService.generateToken(1L, 10L, Role.WAITER)).thenReturn("mocked.jwt.token");

        mockMvc.perform(post("/auth/employeeLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("code", "EMP01", "password", "pass123"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mocked.jwt.token"))
                .andExpect(jsonPath("$.role").value("WAITER"));
    }

    @Test
    void employeeLogin_validCredentials_tokenIsNotNull() throws Exception {
        when(authService.validateEmployeeAccess(anyString(), anyString())).thenReturn(1L);
        when(authService.getEmployeeRole(1L)).thenReturn(Role.MANAGER);
        when(authService.getEmployeeRestaurantId(1L)).thenReturn(5L);
        when(jwtService.generateToken(1L, 5L, Role.MANAGER)).thenReturn("real.jwt.token");

        mockMvc.perform(post("/auth/employeeLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"EMP01\",\"password\":\"pass123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    // ── Credenciales inválidas ─────────────────────────────────────

    @Test
    void employeeLogin_wrongPassword_returns401() throws Exception {
        when(authService.validateEmployeeAccess(anyString(), anyString())).thenReturn(-1L);

        mockMvc.perform(post("/auth/employeeLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"EMP01\",\"password\":\"wrong\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void employeeLogin_unknownUser_returns401() throws Exception {
        when(authService.validateEmployeeAccess(anyString(), anyString())).thenReturn(-1L);

        mockMvc.perform(post("/auth/employeeLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\":\"GHOST\",\"password\":\"any\"}"))
                .andExpect(status().isUnauthorized());
    }

    // ── Body vacío / malformado ────────────────────────────────────

    @Test
    void employeeLogin_emptyBody_returns4xx() throws Exception {
        mockMvc.perform(post("/auth/employeeLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void employeeLogin_missingContentType_returns4xx() throws Exception {
        mockMvc.perform(post("/auth/employeeLogin")
                        .content("not json"))
                .andExpect(status().is4xxClientError());
    }
}
```

---

## 9. OrderControllerTest — @WebMvcTest + MockMvc

> El filtro `JwtAuthenticationFilter` llama a `jwtService.validateToken(token)`.
> Configurando el mock, controlamos si la request pasa o no.

```java
package com.tfg_rm.backend_restaurantmanager.controller;

import com.tfg_rm.backend_restaurantmanager.dto.OrderResponse;
import com.tfg_rm.backend_restaurantmanager.security.JwtService;
import com.tfg_rm.backend_restaurantmanager.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired MockMvc mockMvc;
    @MockitoBean OrderService orderService;
    @MockitoBean JwtService jwtService;

    private static final String TOKEN      = "valid-test-token";
    private static final String AUTH_HEADER = "Bearer " + TOKEN;

    @BeforeEach
    void setUp() {
        // Configura el mock para que JwtAuthenticationFilter acepte el token
        // y establezca autenticación ROLE_WAITER en el SecurityContext
        when(jwtService.validateToken(TOKEN)).thenReturn(true);
        when(jwtService.getUserId(TOKEN)).thenReturn(1L);
        when(jwtService.getRole(TOKEN)).thenReturn("WAITER");
        when(jwtService.getRestaurantId(TOKEN)).thenReturn(10L);
    }

    // ── Sin token → 401 ───────────────────────────────────────────

    @Test
    void getAll_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/order"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllPaid_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/order/paid"))
                .andExpect(status().isUnauthorized());
    }

    // ── Con token válido → 200 ────────────────────────────────────

    @Test
    void getAll_withValidToken_returns200AndList() throws Exception {
        OrderResponse order = buildResponse(1L, "DELIVERY", "CREATED", "15.00");
        when(orderService.getAllOrders(10L)).thenReturn(List.of(order));

        mockMvc.perform(get("/order").header("Authorization", AUTH_HEADER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].orderId").value(1))
                .andExpect(jsonPath("$[0].type").value("DELIVERY"))
                .andExpect(jsonPath("$[0].status").value("CREATED"));
    }

    @Test
    void getAll_emptyRestaurant_returns200WithEmptyList() throws Exception {
        when(orderService.getAllOrders(10L)).thenReturn(List.of());

        mockMvc.perform(get("/order").header("Authorization", AUTH_HEADER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAllPaid_withValidToken_returnsOnlyPaidOrders() throws Exception {
        OrderResponse paid = buildResponse(2L, "PICKUP", "PAID", "30.00");
        when(orderService.getAllOrdersPaid(10L)).thenReturn(List.of(paid));

        mockMvc.perform(get("/order/paid").header("Authorization", AUTH_HEADER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PAID"));
    }

    @Test
    void getAll_multipleOrders_returnsAllInList() throws Exception {
        List<OrderResponse> orders = List.of(
                buildResponse(1L, "DELIVERY", "CREATED", "15.00"),
                buildResponse(2L, "PICKUP",   "PAID",    "25.00"),
                buildResponse(3L, "TABLE",    "CREATED", "40.00")
        );
        when(orderService.getAllOrders(10L)).thenReturn(orders);

        mockMvc.perform(get("/order").header("Authorization", AUTH_HEADER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    // ── Helper ────────────────────────────────────────────────────

    private OrderResponse buildResponse(Long id, String type, String status, String total) {
        OrderResponse r = new OrderResponse();
        r.setOrderId(id);
        r.setType(type);
        r.setStatus(status);
        r.setTotal(new BigDecimal(total));
        r.setItems(List.of());
        return r;
    }
}
```

---

## 10. OrderRepositoryTest — @DataJpaTest + H2

> Solo se testean las queries JPQL custom (`@Query`). No se testa `findById`, `save`, etc.

```java
package com.tfg_rm.backend_restaurantmanager.repository;

import com.tfg_rm.backend_restaurantmanager.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired TestEntityManager em;
    @Autowired OrderRepository orderRepository;

    private RestaurantEntity restaurant1;
    private RestaurantEntity restaurant2;

    @BeforeEach
    void setUp() {
        restaurant1 = persistRestaurant("R1", "Restaurante 1", "r1@test.com");
        restaurant2 = persistRestaurant("R2", "Restaurante 2", "r2@test.com");
    }

    @Test
    void findByRestaurantId_returnsOnlyOrdersOfThatRestaurant() {
        persistOrder(restaurant1, OrderStatusEntity.CREATED, OrderTypeEntity.DELIVERY);
        persistOrder(restaurant1, OrderStatusEntity.PAID,    OrderTypeEntity.PICKUP);
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
    void findByRestaurantIdAndStatus_returnsOnlyPaidOrders() {
        persistOrder(restaurant1, OrderStatusEntity.CREATED, OrderTypeEntity.DELIVERY);
        persistOrder(restaurant1, OrderStatusEntity.PAID,    OrderTypeEntity.PICKUP);
        persistOrder(restaurant1, OrderStatusEntity.PAID,    OrderTypeEntity.DELIVERY);
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

    // ── Helpers ───────────────────────────────────────────────────

    private RestaurantEntity persistRestaurant(String prefix, String name, String email) {
        RestaurantEntity r = new RestaurantEntity();
        r.setPrefix(prefix);
        r.setName(name);
        r.setEmail(email);
        r.setPhone("600000000");
        r.setAddress("Test Address");
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
```

---

## 11. AuthEmployeeRepositoryTest — @DataJpaTest + H2

```java
package com.tfg_rm.backend_restaurantmanager.repository;

import com.tfg_rm.backend_restaurantmanager.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class AuthEmployeeRepositoryTest {

    @Autowired TestEntityManager em;
    @Autowired AuthEmployeeRepository authEmployeeRepository;

    private RestaurantEntity restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new RestaurantEntity();
        restaurant.setPrefix("TST");
        restaurant.setName("Test Restaurant");
        restaurant.setEmail("test@restaurant.com");
        restaurant.setPhone("600000000");
        restaurant.setAddress("Test Address 1");
        em.persist(restaurant);
        em.flush();
    }

    private EmployeeEntity persistEmployee(String code, RoleEntity role) {
        EmployeeEntity emp = new EmployeeEntity();
        emp.setCode(code);
        emp.setName("Test Employee " + code);
        emp.setEmail(code.toLowerCase() + "@test.com");
        emp.setPasswordHash("$2a$10$hashedpassword");
        emp.setRoleName(role);
        emp.setRestaurant(restaurant);
        emp.setStartDate(LocalDate.now());
        emp.setActive(true);
        return em.persist(emp);
    }

    @Test
    void findByCode_existingCode_returnsEmployee() {
        persistEmployee("EMP01", RoleEntity.WAITER);
        em.flush();

        Optional<EmployeeEntity> result = authEmployeeRepository.findByCode("EMP01");

        assertThat(result).isPresent();
        assertThat(result.get().getCode()).isEqualTo("EMP01");
    }

    @Test
    void findByCode_nonExistentCode_returnsEmpty() {
        Optional<EmployeeEntity> result = authEmployeeRepository.findByCode("NOTEXISTS");
        assertThat(result).isEmpty();
    }

    @Test
    void findByCode_returnsCorrectEmployeeAmongMultiple() {
        persistEmployee("EMP01", RoleEntity.WAITER);
        persistEmployee("EMP02", RoleEntity.MANAGER);
        em.flush();

        Optional<EmployeeEntity> result = authEmployeeRepository.findByCode("EMP02");

        assertThat(result).isPresent();
        assertThat(result.get().getCode()).isEqualTo("EMP02");
        assertThat(result.get().getRoleName()).isEqualTo(RoleEntity.MANAGER);
    }

    @Test
    void findByCode_isCaseSensitive() {
        persistEmployee("EMP01", RoleEntity.WAITER);
        em.flush();

        // Los códigos son case-sensitive en la BD
        Optional<EmployeeEntity> result = authEmployeeRepository.findByCode("emp01");
        assertThat(result).isEmpty();
    }
}
```

---

## 12. Roadmap de implementación

| Paso | Tarea | Valor | Tiempo |
|---|---|---|---|
| 1 | Fix `pom.xml` | Bloqueante | 5 min |
| 2 | `application-test.properties` + `JwtTestHelper` | Base para todo | 10 min |
| 3 | `JwtServiceTest` | Seguridad crítica | 20 min |
| 4 | `AuthServiceTest` | Login core | 30 min |
| 5 | Corregir bug `!=` en `OrderService` (4 sitios) | Bug real en producción | 10 min |
| 6 | `OrderServiceTest` | Business logic compleja | 60 min |
| 7 | `AuthControllerTest` | Contrato HTTP auth | 30 min |
| 8 | `OrderControllerTest` | Seguridad endpoints | 30 min |
| 9 | `OrderRepositoryTest` | Custom JPQL queries | 30 min |
| 10 | `AuthEmployeeRepositoryTest` | findByCode crítico | 20 min |

**Total**: ~3.5 horas para un suite completo y mantenible.

---

## 13. Trade-offs y decisiones

| Decisión | Razonamiento |
|---|---|
| Sin `@SpringBootTest` | Demasiado lento; `@WebMvcTest` + `@DataJpaTest` cubren el 95% con 1/10 del tiempo de arranque |
| Sin Testcontainers | H2 es suficiente para validar queries JPQL; añadir TC añade complejidad de infraestructura |
| `@WebMvcTest` incluye `SecurityConfig` real | El filtro JWT se ejecuta de verdad — los tests de seguridad son realistas, no simulados |
| `@MockitoBean` en lugar de `@MockBean` | Spring Boot 4.x deprecó `@MockBean`; usar el nuevo `@MockitoBean` |
| No testear CRUD básico de Spring Data | Son métodos del framework, ya testeados por Spring; no son tuyo código |
| `JwtTestHelper` centralizado | Un solo sitio para el secret de test; evita duplicación y facilita cambios |
| Mockito puro en service tests | Sin contexto Spring = arranque instantáneo; aislamiento perfecto |

---

## 14. Troubleshooting

**`@JdbcTypeCode(SqlTypes.NAMED_ENUM)` falla con H2**
H2 no soporta tipos ENUM nativos de PostgreSQL. Si los `@DataJpaTest` fallan con errores de tipo, añade a `application-test.properties`:
```properties
spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1
```
Si sigue fallando, fuerza VARCHAR para enums:
```properties
spring.jpa.properties.hibernate.type.preferred_enum_jdbc_type=12
```

**`No qualifying bean of type 'JwtService'` en `@WebMvcTest`**
Asegúrate de declarar `@MockitoBean JwtService jwtService` en el test. Es necesario para que `SecurityConfig` pueda construir `JwtAuthenticationFilter`.

**403 en lugar de 401 en tests de controller**
Spring devuelve 403 para usuarios autenticados sin permisos, 401 para no autenticados.
Si el test para "sin token" recibe 403, el `JwtAuthenticationFilter` está estableciendo una autenticación vacía. Revisa que el mock de `jwtService.validateToken(null)` o `validateToken("")` devuelva `false`.

**`@MockBean` not found en Spring Boot 4.x**
Usa `@MockitoBean` del paquete `org.springframework.test.context.bean.override.mockito`.
