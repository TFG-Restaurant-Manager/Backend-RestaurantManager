/* 2 Restaurantes con 2 empleados cada uno y 2 horarios cada empleado */
INSERT INTO restaurants (prefix, name, description, email, phone, address, logo_url)
VALUES
('MAD', 'Restaurante Madrid', 'Restaurante de cocina española', 'madrid@rest.com', '912345678', 'Calle Mayor 1, Madrid', 'https://example.com/logo_mad.png');

-- Empleados del restaurante Madrid
INSERT INTO employee (restaurant_id, name, role_name, email, phone, start_date, code, password_hash)
VALUES
(1, 'Juan Perez', 'MANAGER', 'juan.mad@rest.com', '600111222', '2023-01-01', '00001', 'hash1'),
(1, 'Maria Lopez', 'WAITER', 'maria.mad@rest.com', '600333444', '2023-02-01', '00002', 'hash2');

-- Horarios para empleados del restaurante Madrid
INSERT INTO work_schedules (employee_id, start_datetime, end_datetime)
VALUES
(1, '2026-03-20 09:00:00', '2026-03-20 17:00:00'),
(1, '2026-03-21 10:00:00', '2026-03-21 18:00:00'),
(2, '2026-03-20 12:00:00', '2026-03-20 20:00:00'),
(2, '2026-03-21 14:00:00', '2026-03-21 22:00:00');

-- RESTAURANTE 1 (ID = 1) → 3 CATEGORÍAS y 5 platos por categoría (total 15 platos)
-- CATEGORÍAS
INSERT INTO categories (restaurant_id, name) VALUES
(1, 'Entrantes'),
(1, 'Platos Principales'),
(1, 'Postres');

-- PLATOS - ENTRANTES (category_id = 1)
INSERT INTO dishes (restaurant_id, category_id, name, description, price) VALUES
(1, 1, 'Croquetas caseras', 'Croquetas cremosas de jamón', 6.50),
(1, 1, 'Ensalada mixta', 'Lechuga, tomate, cebolla y atún', 5.50),
(1, 1, 'Gazpacho andaluz', 'Sopa fría de tomate', 4.50),
(1, 1, 'Pan con tomate', 'Pan tostado con tomate y aceite', 3.50),
(1, 1, 'Calamares fritos', 'Calamares crujientes', 7.80);

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