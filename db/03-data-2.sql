/* 2 Restaurantes con 2 empleados cada uno y 2 horarios cada empleado */
INSERT INTO restaurants (prefix, name, description, email, phone, address)
VALUES
('BCN', 'Restaurante Barcelona', 'Restaurante de tapas y paellas', 'barcelona@rest.com', '934567890', 'Rambla 10, Barcelona');

-- Empleados del restaurante Barcelona
INSERT INTO employee (restaurant_id, name, role_name, email, phone, start_date, code, password_hash)
VALUES
(2, 'Carlos Gomez', 'COOKER', 'carlos.bcn@rest.com', '611111222', '2023-03-01', '00001', '$2a$10$ciYqjNHjmkCpj7Nr2VCUDeiVm8urZHJUnWnkuszpnkmG6a6DtXWQG'), -- Contraseña: hash1
(2, 'Ana Torres', 'WAITER', 'ana.bcn@rest.com', '611333444', '2023-04-01', '00002', '$2a$10$tnyGmQtphJiHMmFZB1PPUeJ7PSfDbE1HaSfg1iGiZIa3Bb.Gf1qcq'); -- Contraseña: hash2

-- Horarios para empleados del restaurante Barcelona
INSERT INTO work_schedules (employee_id, start_datetime, end_datetime)
VALUES
(3, '2026-03-20 08:00:00', '2026-03-20 16:00:00'),
(3, '2026-03-21 09:00:00', '2026-03-21 17:00:00'),
(4, '2026-03-20 13:00:00', '2026-03-20 21:00:00'),
(4, '2026-03-21 15:00:00', '2026-03-21 23:00:00');

-- RESTAURANTE 2 (ID = 2) → 5 CATEGORÍAS y 5 platos por categoría (total 25 platos)
-- CATEGORÍAS
INSERT INTO dishes_categories (restaurant_id, name) VALUES
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
-- RESTAURANTE 2 (ID = 2) → CATEGORÍAS DE INGREDIENTES
-- =========================================
INSERT INTO ingredients_categories (restaurant_id, name) VALUES
(2, 'Verduras frescas'),
(2, 'Carnes y embutidos'),
(2, 'Pescados y mariscos'),
(2, 'Quesos y lácteos'),
(2, 'Salsas y aderezos'),
(2, 'Postres y dulces');

INSERT INTO ingredients (restaurant_id, category_id, name, unit, stock_quantity, cost_unit, minimum_stock) VALUES
(2, (SELECT id FROM ingredients_categories WHERE restaurant_id = 2 AND name = 'Verduras frescas'), 'Tomate cherry', 'kg', 10.00, 1.20, 5.00),
(2, (SELECT id FROM ingredients_categories WHERE restaurant_id = 2 AND name = 'Verduras frescas'), 'Rúcula', 'kg', 6.00, 1.50, 5.00),
(2, (SELECT id FROM ingredients_categories WHERE restaurant_id = 2 AND name = 'Verduras frescas'), 'Pimiento rojo', 'kg', 8.00, 1.00, 5.00),
(2, (SELECT id FROM ingredients_categories WHERE restaurant_id = 2 AND name = 'Carnes y embutidos'), 'Jamón ibérico', 'kg', 4.00, 22.00, 5.00),
(2, (SELECT id FROM ingredients_categories WHERE restaurant_id = 2 AND name = 'Carnes y embutidos'), 'Chorizo', 'kg', 5.00, 10.50, 5.00),
(2, (SELECT id FROM ingredients_categories WHERE restaurant_id = 2 AND name = 'Carnes y embutidos'), 'Panceta', 'kg', 3.50, 7.20, 5.00),
(2, (SELECT id FROM ingredients_categories WHERE restaurant_id = 2 AND name = 'Pescados y mariscos'), 'Pulpo', 'kg', 12.00, 14.00, 5.00),
(2, (SELECT id FROM ingredients_categories WHERE restaurant_id = 2 AND name = 'Pescados y mariscos'), 'Gambas', 'kg', 6.50, 9.80, 5.00),
(2, (SELECT id FROM ingredients_categories WHERE restaurant_id = 2 AND name = 'Pescados y mariscos'), 'Mejillones', 'kg', 7.00, 4.50, 5.00),
(2, (SELECT id FROM ingredients_categories WHERE restaurant_id = 2 AND name = 'Quesos y lácteos'), 'Queso feta', 'kg', 2.50, 12.50, 5.00),
(2, (SELECT id FROM ingredients_categories WHERE restaurant_id = 2 AND name = 'Quesos y lácteos'), 'Mozzarella', 'kg', 3.00, 9.00, 5.00),
(2, (SELECT id FROM ingredients_categories WHERE restaurant_id = 2 AND name = 'Quesos y lácteos'), 'Mantequilla', 'kg', 5.50, 4.80, 5.00),
(2, (SELECT id FROM ingredients_categories WHERE restaurant_id = 2 AND name = 'Salsas y aderezos'), 'Salsa BBQ', 'l', 15.00, 3.20, 5.00),
(2, (SELECT id FROM ingredients_categories WHERE restaurant_id = 2 AND name = 'Salsas y aderezos'), 'Aceite de oliva', 'l', 18.00, 4.20, 5.00),
(2, (SELECT id FROM ingredients_categories WHERE restaurant_id = 2 AND name = 'Salsas y aderezos'), 'Vinagre balsámico', 'l', 10.00, 2.50, 5.00),
(2, (SELECT id FROM ingredients_categories WHERE restaurant_id = 2 AND name = 'Postres y dulces'), 'Azúcar', 'kg', 20.00, 0.90, 5.00),
(2, (SELECT id FROM ingredients_categories WHERE restaurant_id = 2 AND name = 'Postres y dulces'), 'Chocolate', 'kg', 12.00, 6.80, 5.00),
(2, (SELECT id FROM ingredients_categories WHERE restaurant_id = 2 AND name = 'Postres y dulces'), 'Harina', 'kg', 18.00, 0.70, 5.00);

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

INSERT INTO orders (restaurant_id, type, status, total, notes) VALUES
(2, 'TABLE', 'PAID', 24.80, 'Pedido desde mesa T1'),
(2, 'DELIVERY', 'PAID', 27.00, 'Pedido delivery sin cliente'),
(2, 'PICKUP', 'PAID', 10.40, 'Pedido para recogida en mostrador'),
(2, 'TABLE', 'PAID', 29.60, 'Pedido desde mesa T2'),
(2, 'TABLE', 'PAID', 21.10, 'Pedido desde mesa S2'),
(2, 'DELIVERY', 'PAID', 30.80, 'Pedido delivery tarde'),
(2, 'DELIVERY', 'PAID', 18.30, 'Pedido delivery flash'),
(2, 'PICKUP', 'PAID', 13.20, 'Pedido pickup 20:00'),
(2, 'PICKUP', 'PAID', 16.90, 'Pedido pickup 21:00');

INSERT INTO order_table (order_id, table_id) VALUES
(19, 20),
(22, 32),
(23, 43);

INSERT INTO order_table (order_id, table_id) VALUES
(28, 20),
(31, 32),
(32, 43);

INSERT INTO order_delivery (order_id, delivery_address) VALUES
(20, 'Rambla 50, Barcelona'),
(24, 'Carrer Balmes 100, Barcelona'),
(25, 'C/ Provença 88, Barcelona');

INSERT INTO order_delivery (order_id, delivery_address) VALUES
(29, 'Rambla 50, Barcelona'),
(33, 'Carrer Balmes 100, Barcelona'),
(34, 'C/ Provença 88, Barcelona');

INSERT INTO order_pickup (order_id, pickup_time) VALUES
(21, '2026-03-24 20:30:00'),
(26, '2026-03-24 21:00:00'),
(27, '2026-03-24 21:30:00');

INSERT INTO order_pickup (order_id, pickup_time) VALUES
(30, '2026-03-24 20:30:00'),
(35, '2026-03-24 21:00:00'),
(36, '2026-03-24 21:30:00');

INSERT INTO order_items (order_id, dish_id, notes, unit_price) VALUES
-- order 10 TABLE
(19, 16, 'Extra guacamole', 7.00),
(19, 18, 'Sin cebolla', 5.80),
(19, 26, 'Al punto', 12.00),
-- order 11 DELIVERY
(20, 21, 'Con ajo', 14.50),
(20, 23, 'Doble salsa', 16.50),
-- order 12 PICKUP
(21, 35, 'Sin nata', 5.80),
(21, 40, 'Con chocolate extra', 5.20),
-- order 13 TABLE
(22, 17, 'Muy crujiente', 8.50),
(22, 25, 'Sin tomar', 19.00),
-- order 14 TABLE
(23, 18, 'Poco picante', 5.80),
(23, 24, 'A la plancha', 15.00),
(23, 30, 'Bien hecho', 13.80),
-- order 15 DELIVERY
(24, 16, 'Rápido', 7.00),
(24, 19, 'Extra limón', 7.20),
-- order 16 DELIVERY
(25, 22, 'Con aceitunas', 8.00),
(25, 26, 'Al punto', 12.00),
-- order 17 PICKUP
(26, 32, 'Muy frío', 15.00),
(26, 40, 'Sin hielo', 5.20),
-- order 18 PICKUP
(27, 37, 'Ligero', 5.50),
(27, 39, 'Con salsa extra', 4.90);

INSERT INTO order_items (order_id, dish_id, notes, unit_price) VALUES
-- order 10 TABLE
(28, 16, 'Extra guacamole', 7.00),
(28, 18, 'Sin cebolla', 5.80),
(28, 26, 'Al punto', 12.00),
-- order 11 DELIVERY
(29, 21, 'Con ajo', 14.50),
(29, 23, 'Doble salsa', 16.50),
-- order 12 PICKUP
(30, 35, 'Sin nata', 5.80),
(30, 40, 'Con chocolate extra', 5.20),
-- order 13 TABLE
(31, 17, 'Muy crujiente', 8.50),
(31, 25, 'Sin tomar', 19.00),
-- order 14 TABLE
(32, 18, 'Poco picante', 5.80),
(32, 24, 'A la plancha', 15.00),
(32, 30, 'Bien hecho', 13.80),
-- order 15 DELIVERY
(33, 16, 'Rápido', 7.00),
(33, 19, 'Extra limón', 7.20),
-- order 16 DELIVERY
(34, 22, 'Con aceitunas', 8.00),
(34, 26, 'Al punto', 12.00),
-- order 17 PICKUP
(35, 32, 'Muy frío', 15.00),
(35, 40, 'Sin hielo', 5.20),
-- order 18 PICKUP
(36, 37, 'Ligero', 5.50),
(36, 39, 'Con salsa extra', 4.90);