/* 2 Restaurantes con 2 empleados cada uno y 2 horarios cada empleado */
INSERT INTO restaurants (prefix, name, description, email, phone, address)
VALUES
('MAD', 'Restaurante Madrid', 'Restaurante de cocina española', 'madrid@rest.com', '912345678', 'Calle Mayor 1, Madrid');

-- Empleados del restaurante Madrid
INSERT INTO employee (restaurant_id, name, role_name, email, phone, start_date, code, password_hash)
VALUES
(1, 'Juan Perez', 'MANAGER', 'juan.mad@rest.com', '600111222', '2023-01-01', '00001', '$2a$10$ciYqjNHjmkCpj7Nr2VCUDeiVm8urZHJUnWnkuszpnkmG6a6DtXWQG'), -- Contraseña: hash1
(1, 'Maria Lopez', 'WAITER', 'maria.mad@rest.com', '600333444', '2023-02-01', '00002', '$2a$10$tnyGmQtphJiHMmFZB1PPUeJ7PSfDbE1HaSfg1iGiZIa3Bb.Gf1qcq'); -- Contraseña: hash2

-- Horarios para empleados del restaurante Madrid
INSERT INTO work_schedules (employee_id, start_datetime, end_datetime)
VALUES
(1, '2026-03-20 09:00:00', '2026-03-20 17:00:00'),
(1, '2026-03-21 10:00:00', '2026-03-21 18:00:00'),
(2, '2026-03-20 12:00:00', '2026-03-20 20:00:00'),
(2, '2026-03-21 14:00:00', '2026-03-21 22:00:00');

-- RESTAURANTE 1 (ID = 1) → 3 CATEGORÍAS y 5 platos por categoría (total 15 platos)
-- CATEGORÍAS
INSERT INTO dishes_categories (restaurant_id, name) VALUES
(1, 'Entrantes'),
(1, 'Platos Principales'),
(1, 'Postres');

-- PLATOS - ENTRANTES (category_id = 1)
INSERT INTO dishes (restaurant_id, category_id, name, description, price, available) VALUES
(1, 1, 'Croquetas caseras', 'Croquetas cremosas de jamón', 6.50, FALSE),
(1, 1, 'Ensalada mixta', 'Lechuga, tomate, cebolla y atún', 5.50, FALSE),
(1, 1, 'Gazpacho andaluz', 'Sopa fría de tomate', 4.50, FALSE),
(1, 1, 'Pan con tomate', 'Pan tostado con tomate y aceite', 3.50, FALSE),
(1, 1, 'Calamares fritos', 'Calamares crujientes', 7.80, FALSE);

-- PLATOS - PRINCIPALES (category_id = 2)
INSERT INTO dishes (restaurant_id, category_id, name, description, price) VALUES
(1, 2, 'Paella valenciana', 'Arroz con pollo y verduras', 12.00),
(1, 2, 'Solomillo al roquefort', 'Carne con salsa de queso', 15.50),
(1, 2, 'Pollo asado', 'Pollo al horno con especias', 10.00),
(1, 2, 'Merluza a la plancha', 'Pescado fresco a la plancha', 13.20),
(1, 2, 'Lasaña casera', 'Lasaña de carne y bechamel', 11.00),
(1, 2, 'Hamburguesa completa', 'Con queso, lechuga y tomate', 9.50);

-- PLATOS - POSTRES (category_id = 3)
INSERT INTO dishes (restaurant_id, category_id, name, description, price) VALUES
(1, 3, 'Tarta de queso', 'Con base de galleta', 5.00),
(1, 3, 'Flan casero', 'Flan tradicional con caramelo', 4.20),
(1, 3, 'Helado variado', '3 bolas de helado', 4.50),
(1, 3, 'Brownie', 'Con chocolate caliente', 5.50),
(1, 3, 'Arroz con leche', 'Postre tradicional', 4.00);

-- =========================================
-- RESTAURANTE 1 (ID = 1) → CATEGORÍAS DE INGREDIENTES
-- =========================================
INSERT INTO ingredients_categories (restaurant_id, name) VALUES
(1, 'Verduras y hortalizas'),
(1, 'Carnes y aves'),
(1, 'Pescados y mariscos'),
(1, 'Lácteos y huevos'),
(1, 'Especias y condimentos'),
(1, 'Pan y cereales');

INSERT INTO ingredients (restaurant_id, category_id, name, unit, stock_quantity, cost_unit, minimum_stock) VALUES
(1, (SELECT id FROM ingredients_categories WHERE restaurant_id = 1 AND name = 'Verduras y hortalizas'), 'Tomate', 'kg', 12.50, 0.90, 5.00),
(1, (SELECT id FROM ingredients_categories WHERE restaurant_id = 1 AND name = 'Verduras y hortalizas'), 'Lechuga', 'kg', 8.00, 0.80, 3.00),
(1, (SELECT id FROM ingredients_categories WHERE restaurant_id = 1 AND name = 'Verduras y hortalizas'), 'Cebolla', 'kg', 15.00, 0.40, 4.00),
(1, (SELECT id FROM ingredients_categories WHERE restaurant_id = 1 AND name = 'Carnes y aves'), 'Pollo', 'kg', 20.00, 3.50, 50.00),
(1, (SELECT id FROM ingredients_categories WHERE restaurant_id = 1 AND name = 'Carnes y aves'), 'Cerdo', 'kg', 18.00, 3.20, 9.00),
(1, (SELECT id FROM ingredients_categories WHERE restaurant_id = 1 AND name = 'Carnes y aves'), 'Ternera', 'kg', 22.00, 4.10, 15.00),
(1, (SELECT id FROM ingredients_categories WHERE restaurant_id = 1 AND name = 'Pescados y mariscos'), 'Merluza', 'kg', 10.00, 6.00, 5.00),
(1, (SELECT id FROM ingredients_categories WHERE restaurant_id = 1 AND name = 'Pescados y mariscos'), 'Gambas', 'kg', 6.00, 9.50, 2.00),
(1, (SELECT id FROM ingredients_categories WHERE restaurant_id = 1 AND name = 'Pescados y mariscos'), 'Calamares', 'kg', 8.00, 7.20, 3.00),
(1, (SELECT id FROM ingredients_categories WHERE restaurant_id = 1 AND name = 'Lácteos y huevos'), 'Queso manchego', 'kg', 7.00, 8.80, 2.00),
(1, (SELECT id FROM ingredients_categories WHERE restaurant_id = 1 AND name = 'Lácteos y huevos'), 'Nata', 'l', 20.00, 2.00, 5.00),
(1, (SELECT id FROM ingredients_categories WHERE restaurant_id = 1 AND name = 'Lácteos y huevos'), 'Huevos', 'unidad', 200.00, 0.15, 50.00),
(1, (SELECT id FROM ingredients_categories WHERE restaurant_id = 1 AND name = 'Especias y condimentos'), 'Pimentón', 'kg', 6.00, 12.00, 2.00),
(1, (SELECT id FROM ingredients_categories WHERE restaurant_id = 1 AND name = 'Especias y condimentos'), 'Ajo', 'kg', 5.00, 3.00, 1.00),
(1, (SELECT id FROM ingredients_categories WHERE restaurant_id = 1 AND name = 'Especias y condimentos'), 'Aceite de oliva', 'l', 25.00, 4.50, 200.00),
(1, (SELECT id FROM ingredients_categories WHERE restaurant_id = 1 AND name = 'Pan y cereales'), 'Pan', 'unidad', 100.00, 0.35, 50.00),
(1, (SELECT id FROM ingredients_categories WHERE restaurant_id = 1 AND name = 'Pan y cereales'), 'Arroz', 'kg', 30.00, 1.20, 10.00),
(1, (SELECT id FROM ingredients_categories WHERE restaurant_id = 1 AND name = 'Pan y cereales'), 'Pasta', 'kg', 25.00, 1.10, 50.00);

-- =========================================
-- RESTAURANTE 1 (ID = 1) → 3 SECCIONES
-- =========================================

-- SECCIONES
INSERT INTO table_sections (restaurant_id, title) VALUES
(1, 'Terraza'),
(1, 'Salón Principal'),
(1, 'Zona VIP');

-- =========================================
-- MESAS - TERRAZA (section_id = 1)
-- =========================================
INSERT INTO tables_restaurant (restaurant_id, section_id, name, capacity, pos_x, pos_y) VALUES
(1, 1, 'T1', 2, 1, 1),
(1, 1, 'T2', 2, 2, 1),
(1, 1, 'T3', 4, 3, 1),
(1, 1, 'T4', 4, 4, 1),
(1, 1, 'T5', 6, 5, 1),
(1, 1, 'T6', 2, 6, 1);

-- =========================================
-- MESAS - SALÓN PRINCIPAL (section_id = 2)
-- =========================================
INSERT INTO tables_restaurant (restaurant_id, section_id, name, capacity, pos_x, pos_y) VALUES
(1, 2, 'S1', 4, 1, 3),
(1, 2, 'S2', 4, 2, 3),
(1, 2, 'S3', 6, 3, 3),
(1, 2, 'S4', 2, 4, 3),
(1, 2, 'S5', 2, 5, 3),
(1, 2, 'S6', 8, 6, 3),
(1, 2, 'S7', 4, 7, 3);

-- =========================================
-- MESAS - ZONA VIP (section_id = 3)
-- =========================================
INSERT INTO tables_restaurant (restaurant_id, section_id, name, capacity, pos_x, pos_y) VALUES
(1, 3, 'V1', 6, 1, 5),
(1, 3, 'V2', 6, 2, 5),
(1, 3, 'V3', 8, 3, 5),
(1, 3, 'V4', 10, 4, 5),
(1, 3, 'V5', 4, 5, 5);

-- =========================================
-- PEDIDOS DE PRUEBA RESTAURANTE 1 (TABLE, DELIVERY, PICKUP)
-- =========================================
INSERT INTO orders (restaurant_id, type, status, total, notes) VALUES
(1, 'TABLE', 'CREATED', 23.50, 'Pedido desde mesa T1'),
(1, 'DELIVERY', 'CREATED', 25.80, 'Pedido delivery sin cliente'),
(1, 'PICKUP', 'CREATED', 10.00, 'Pedido para recogida en mostrador'),
(1, 'TABLE', 'CREATED', 33.20, 'Pedido desde mesa T2'),
(1, 'TABLE', 'CREATED', 19.70, 'Pedido desde mesa S1'),
(1, 'DELIVERY', 'CREATED', 27.30, 'Pedido delivery urgent'),
(1, 'DELIVERY', 'CREATED', 15.90, 'Pedido delivery local'),
(1, 'PICKUP', 'CREATED', 12.40, 'Pedido para recogida express'),
(1, 'PICKUP', 'CREATED', 18.60, 'Pedido para recogida tarde');

INSERT INTO orders (restaurant_id, type, status, total, notes) VALUES
(1, 'TABLE', 'PAID', 23.50, 'Pedido desde mesa T1'),
(1, 'DELIVERY', 'PAID', 25.80, 'Pedido delivery sin cliente'),
(1, 'PICKUP', 'PAID', 10.00, 'Pedido para recogida en mostrador'),
(1, 'TABLE', 'PAID', 33.20, 'Pedido desde mesa T2'),
(1, 'TABLE', 'PAID', 19.70, 'Pedido desde mesa S1'),
(1, 'DELIVERY', 'PAID', 27.30, 'Pedido delivery urgent'),
(1, 'DELIVERY', 'PAID', 15.90, 'Pedido delivery local'),
(1, 'PICKUP', 'PAID', 12.40, 'Pedido para recogida express'),
(1, 'PICKUP', 'PAID', 18.60, 'Pedido para recogida tarde');

INSERT INTO order_table (order_id, table_id) VALUES
(1, 1),
(4, 2),
(5, 7);

INSERT INTO order_table (order_id, table_id) VALUES
(10, 1),
(13, 2),
(14, 7);

INSERT INTO order_delivery (order_id, delivery_address) VALUES
(2, 'Calle Mayor 10, Madrid'),
(6, 'Calle Gran Vía 22, Madrid'),
(7, 'Calle Alcalá 45, Madrid');

INSERT INTO order_delivery (order_id, delivery_address) VALUES
(11, 'Calle Mayor 10, Madrid'),
(15, 'Calle Gran Vía 22, Madrid'),
(16, 'Calle Alcalá 45, Madrid');

INSERT INTO order_pickup (order_id, pickup_time) VALUES
(3, '2026-03-24 19:00:00'),
(8, '2026-03-24 20:00:00'),
(9, '2026-03-24 20:30:00');

INSERT INTO order_pickup (order_id, pickup_time) VALUES
(12, '2026-03-24 19:00:00'),
(17, '2026-03-24 20:00:00'),
(18, '2026-03-24 20:30:00');

INSERT INTO order_items (order_id, dish_id, notes, unit_price) VALUES
-- order 1 TABLE
(1, 1, 'Una ración extra de salsa', 6.50),
(1, 6, 'Sin cebolla', 12.00),
(1, 11, 'Sin frutos secos', 5.00),
-- order 2 DELIVERY
(2, 1, 'Sin gluten', 6.50),
(2, 5, 'Extra limón', 7.80),
(2, 12, 'Bien hecho', 11.00),
-- order 3 PICKUP
(3, 13, 'En bolsa separada', 5.50),
(3, 15, 'Con extra canela', 4.00),
-- order 4 TABLE
(4, 2, 'Sin pepino', 5.50),
(4, 7, 'Guarnición extra', 15.50),
(4, 10, 'Sin piel', 13.20),
-- order 5 TABLE
(5, 3, 'Frío', 4.50),
(5, 8, 'Extra patatas', 10.00),
-- order 6 DELIVERY
(6, 4, 'Rápido', 3.50),
(6, 11, 'Doble porción', 5.00),
(6, 14, 'Con nata', 5.50),
-- order 7 DELIVERY
(7, 6, 'Sin champiñones', 12.00),
(7, 9, 'Muy picante', 13.20),
-- order 8 PICKUP
(8, 2, 'Sin cebolla', 5.50),
(8, 5, 'Ligero', 7.80),
-- order 9 PICKUP
(9, 12, 'Extra queso', 11.00),
(9, 15, 'Caliente', 4.00);

INSERT INTO order_items (order_id, dish_id, notes, unit_price) VALUES
-- order 1 TABLE
(10, 1, 'Una ración extra de salsa', 6.50),
(10, 6, 'Sin cebolla', 12.00),
(10, 11, 'Sin frutos secos', 5.00),
-- order 2 DELIVERY
(11, 1, 'Sin gluten', 6.50),
(11, 5, 'Extra limón', 7.80),
(11, 12, 'Bien hecho', 11.00),
-- order 3 PICKUP
(12, 13, 'En bolsa separada', 5.50),
(12, 15, 'Con extra canela', 4.00),
-- order 4 TABLE
(13, 2, 'Sin pepino', 5.50),
(13, 7, 'Guarnición extra', 15.50),
(13, 10, 'Sin piel', 13.20),
-- order 5 TABLE
(14, 3, 'Frío', 4.50),
(14, 8, 'Extra patatas', 10.00),
-- order 6 DELIVERY
(15, 4, 'Rápido', 3.50),
(15, 11, 'Doble porción', 5.00),
(15, 14, 'Con nata', 5.50),
-- order 7 DELIVERY
(16, 6, 'Sin champiñones', 12.00),
(16, 9, 'Muy picante', 13.20),
-- order 8 PICKUP
(17, 2, 'Sin cebolla', 5.50),
(17, 5, 'Ligero', 7.80),
-- order 9 PICKUP
(18, 12, 'Extra queso', 11.00),
(18, 15, 'Caliente', 4.00);