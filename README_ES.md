# API de Restaurant Manager

### Español es / [English en](README.md)

API REST de backend para gestionar las operaciones de un restaurante.
Construida con **Spring Boot**, **PostgreSQL** y **Docker Compose**.

La API maneja las funcionalidades principales de la gestión del restaurante, tales como:

- Platos y categorías
- Pedidos y elementos del pedido
- Empleados y roles
- Mesas y restaurantes
- Horarios de trabajo

---

## Stack Tecnológico

- **Java / Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
- **PGAdmin**
- **Docker y Docker Compose**
- **Maven**

---

## Características

- Operaciones CRUD para las entidades del restaurante
- Sistema de gestión de pedidos
- Gestión de empleados y roles
- Seguimiento de horarios de trabajo
- Persistencia con PostgreSQL
- Entorno contenerizado con Docker

---

## Estructura del Proyecto

```text
backend_restaurantmanager
└── main
    ├── java
    │   └── com.tfg_rm.backend_restaurantmanager
    │       ├── BackendRestaurantManagerApplication
    |       ├── auth
    |       ├── config
    |       ├── shared
    |       └── websocket
    └── resources
            └── application.yml
db
└── init
```

## Ejecución del Proyecto

### 1. Clonar el repositorio

```
git clone https://github.com/TFG-Restaurant-Manager/Backend-RestaurantManager.git
cd Backend-RestaurantManager
```

### 2. Iniciar con Docker Compose

```
docker compose up --build
```

Esto iniciará:

- La API de Spring Boot

- La base de datos PostgreSQL

- El monitor de PostgreSQL PGAdmin

### 3. Acceso a la API

Una vez en ejecución, la API estará disponible en:

```
http://localhost:8080
```

## Base de Datos

### La aplicación utiliza **PostgreSQL**.

### Entidades de Ejemplo

Entidades principales gestionadas por la API:

- **Restaurantes**

- **Mesas**

- **Platos**

- **Pedidos**

- **Platos del Pedido**

- **Empleados**

- **Roles**

- **Horarios de Trabajo**
