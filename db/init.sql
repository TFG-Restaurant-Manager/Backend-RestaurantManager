CREATE TABLE restaurant (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(255)
);

INSERT INTO restaurant (name, address)
VALUES ('Mi Restaurante', 'Calle Falsa 123');