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