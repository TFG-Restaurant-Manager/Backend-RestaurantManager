/* 2 Restaurantes con 2 empleados cada uno y 2 horarios cada empleado */
INSERT INTO restaurants (prefix, name, description, email, phone, address, logo_url)
VALUES
('BCN', 'Restaurante Barcelona', 'Restaurante de tapas y paellas', 'barcelona@rest.com', '934567890', 'Rambla 10, Barcelona', 'https://example.com/logo_bcn.png');

-- Empleados del restaurante Barcelona
INSERT INTO employee (restaurant_id, name, role_name, email, phone, start_date, code, password_hash)
VALUES
(2, 'Carlos Gomez', 'COOKER', 'carlos.bcn@rest.com', '611111222', '2023-03-01', '00001', 'hash3'),
(2, 'Ana Torres', 'WAITER', 'ana.bcn@rest.com', '611333444', '2023-04-01', '00002', 'hash4');

-- Horarios para empleados del restaurante Barcelona
INSERT INTO work_schedules (employee_id, start_datetime, end_datetime)
VALUES
(3, '2026-03-20 08:00:00', '2026-03-20 16:00:00'),
(3, '2026-03-21 09:00:00', '2026-03-21 17:00:00'),
(4, '2026-03-20 13:00:00', '2026-03-20 21:00:00'),
(4, '2026-03-21 15:00:00', '2026-03-21 23:00:00');

-- RESTAURANTE 2 (ID = 2) → 5 CATEGORÍAS y 5 platos por categoría (total 25 platos)
-- CATEGORÍAS
INSERT INTO categories (restaurant_id, name) VALUES
(2, 'Entrantes'),
(2, 'Ensaladas'),
(2, 'Carnes'),
(2, 'Pescados'),
(2, 'Postres');

-- PLATOS - ENTRANTES (category_id = 4)
INSERT INTO dishes (restaurant_id, category_id, name, description, price) VALUES
(2, 4, 'Nachos con queso', 'Nachos gratinados', 7.00),
(2, 4, 'Alitas BBQ', 'Alitas con salsa barbacoa', 8.50),
(2, 4, 'Patatas bravas', 'Con salsa picante', 5.80),
(2, 4, 'Aros de cebolla', 'Crujientes', 4.90),
(2, 4, 'Tequeños', 'Rellenos de queso', 6.50);

-- PLATOS - ENSALADAS (category_id = 5)
INSERT INTO dishes (restaurant_id, category_id, name, description, price) VALUES
(2, 5, 'Ensalada César', 'Pollo, lechuga y parmesano', 8.50),
(2, 5, 'Ensalada griega', 'Queso feta y aceitunas', 7.80),
(2, 5, 'Ensalada de quinoa', 'Con verduras frescas', 9.00),
(2, 5, 'Ensalada caprese', 'Tomate y mozzarella', 7.20),
(2, 5, 'Ensalada de atún', 'Con huevo cocido', 8.00);

-- PLATOS - CARNES (category_id = 6)
INSERT INTO dishes (restaurant_id, category_id, name, description, price) VALUES
(2, 6, 'Entrecot', 'A la parrilla', 18.00),
(2, 6, 'Costillas BBQ', 'Con salsa barbacoa', 16.50),
(2, 6, 'Chuleta de cerdo', 'A la plancha', 12.00),
(2, 6, 'Hamburguesa gourmet', 'Con bacon y queso', 11.50),
(2, 6, 'Solomillo de ternera', 'Con guarnición', 19.00);

-- PLATOS - PESCADOS (category_id = 7)
INSERT INTO dishes (restaurant_id, category_id, name, description, price) VALUES
(2, 7, 'Salmón al horno', 'Con verduras', 14.50),
(2, 7, 'Bacalao al pil pil', 'Receta tradicional', 15.00),
(2, 7, 'Atún a la plancha', 'Fresco', 13.80),
(2, 7, 'Gambas al ajillo', 'Con ajo y aceite', 12.50),
(2, 7, 'Pulpo a la gallega', 'Con pimentón', 16.00);

-- PLATOS - POSTRES (category_id = 8)
INSERT INTO dishes (restaurant_id, category_id, name, description, price) VALUES
(2, 8, 'Cheesecake', 'Tarta cremosa', 5.50),
(2, 8, 'Tiramisú', 'Postre italiano', 5.80),
(2, 8, 'Helado artesanal', 'Sabores variados', 4.80),
(2, 8, 'Crema catalana', 'Con azúcar quemado', 4.90),
(2, 8, 'Churros con chocolate', 'Tradicional', 5.20),
(2, 8, 'Mousse de chocolate', 'Ligero y cremoso', 4.70);

-- =========================================
-- RESTAURANTE 2 (ID = 2) → 5 SECCIONES
-- =========================================

-- SECCIONES
INSERT INTO table_sections (restaurant_id, title) VALUES
(2, 'Terraza'),
(2, 'Salón'),
(2, 'Bar'),
(2, 'Privado'),
(2, 'Jardín');

-- =========================================
-- MESAS - TERRAZA (section_id = 4)
-- =========================================
INSERT INTO tables_restaurant (restaurant_id, section_id, name, capacity, pos_x, pos_y) VALUES
(2, 4, 'T1', 2, 1, 1),
(2, 4, 'T2', 2, 2, 1),
(2, 4, 'T3', 4, 3, 1),
(2, 4, 'T4', 4, 4, 1),
(2, 4, 'T5', 6, 5, 1);

-- =========================================
-- MESAS - SALÓN (section_id = 5)
-- =========================================
INSERT INTO tables_restaurant (restaurant_id, section_id, name, capacity, pos_x, pos_y) VALUES
(2, 5, 'S1', 4, 1, 3),
(2, 5, 'S2', 4, 2, 3),
(2, 5, 'S3', 6, 3, 3),
(2, 5, 'S4', 2, 4, 3),
(2, 5, 'S5', 2, 5, 3),
(2, 5, 'S6', 8, 6, 3);

-- =========================================
-- MESAS - BAR (section_id = 6)
-- =========================================
INSERT INTO tables_restaurant (restaurant_id, section_id, name, capacity, pos_x, pos_y) VALUES
(2, 6, 'B1', 2, 1, 5),
(2, 6, 'B2', 2, 2, 5),
(2, 6, 'B3', 2, 3, 5),
(2, 6, 'B4', 2, 4, 5),
(2, 6, 'B5', 2, 5, 5);

-- =========================================
-- MESAS - PRIVADO (section_id = 7)
-- =========================================
INSERT INTO tables_restaurant (restaurant_id, section_id, name, capacity, pos_x, pos_y) VALUES
(2, 7, 'P1', 8, 1, 7),
(2, 7, 'P2', 10, 2, 7),
(2, 7, 'P3', 12, 3, 7),
(2, 7, 'P4', 6, 4, 7),
(2, 7, 'P5', 8, 5, 7);

-- =========================================
-- MESAS - JARDÍN (section_id = 8)
-- =========================================
INSERT INTO tables_restaurant (restaurant_id, section_id, name, capacity, pos_x, pos_y) VALUES
(2, 8, 'J1', 4, 1, 9),
(2, 8, 'J2', 4, 2, 9),
(2, 8, 'J3', 6, 3, 9),
(2, 8, 'J4', 6, 4, 9),
(2, 8, 'J5', 8, 5, 9),
(2, 8, 'J6', 2, 6, 9);

-- =========================================
-- PEDIDOS DE PRUEBA RESTAURANTE 2 (TABLE, DELIVERY, PICKUP)
-- =========================================
INSERT INTO orders (restaurant_id, type, status, total, notes) VALUES
(2, 'TABLE', 'CREATED', 24.80, 'Pedido desde mesa T1'),
(2, 'DELIVERY', 'CREATED', 27.00, 'Pedido delivery sin cliente'),
(2, 'PICKUP', 'CREATED', 10.40, 'Pedido para recogida en mostrador'),
(2, 'TABLE', 'CREATED', 29.60, 'Pedido desde mesa T2'),
(2, 'TABLE', 'CREATED', 21.10, 'Pedido desde mesa S2'),
(2, 'DELIVERY', 'CREATED', 30.80, 'Pedido delivery tarde'),
(2, 'DELIVERY', 'CREATED', 18.30, 'Pedido delivery flash'),
(2, 'PICKUP', 'CREATED', 13.20, 'Pedido pickup 20:00'),
(2, 'PICKUP', 'CREATED', 16.90, 'Pedido pickup 21:00');

INSERT INTO order_table (order_id, table_id) VALUES
(10, 4),
(13, 5),
(14, 11);

INSERT INTO order_delivery (order_id, delivery_address, client_id) VALUES
(11, 'Rambla 50, Barcelona', NULL),
(15, 'Carrer Balmes 100, Barcelona', NULL),
(16, 'C/ Provença 88, Barcelona', NULL);

INSERT INTO order_pickup (order_id, pickup_time, client_id) VALUES
(12, '2026-03-24 20:30:00', NULL),
(17, '2026-03-24 21:00:00', NULL),
(18, '2026-03-24 21:30:00', NULL);

INSERT INTO order_items (order_id, dish_id, notes, unit_price) VALUES
-- order 10 TABLE
(10, 16, 'Extra guacamole', 7.00),
(10, 18, 'Sin cebolla', 5.80),
(10, 26, 'Al punto', 12.00),
-- order 11 DELIVERY
(11, 21, 'Con ajo', 14.50),
(11, 23, 'Doble salsa', 16.50),
-- order 12 PICKUP
(12, 35, 'Sin nata', 5.80),
(12, 40, 'Con chocolate extra', 5.20),
-- order 13 TABLE
(13, 17, 'Muy crujiente', 8.50),
(13, 25, 'Sin tomar', 19.00),
-- order 14 TABLE
(14, 18, 'Poco picante', 5.80),
(14, 24, 'A la plancha', 15.00),
(14, 30, 'Bien hecho', 13.80),
-- order 15 DELIVERY
(15, 16, 'Rápido', 7.00),
(15, 19, 'Extra limón', 7.20),
-- order 16 DELIVERY
(16, 22, 'Con aceitunas', 8.00),
(16, 26, 'Al punto', 12.00),
-- order 17 PICKUP
(17, 32, 'Muy frío', 15.00),
(17, 40, 'Sin hielo', 5.20),
-- order 18 PICKUP
(18, 37, 'Ligero', 5.50),
(18, 39, 'Con salsa extra', 4.90);