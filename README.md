# Restaurant Manager API

### English en / [Español es](README_ES)

Backend REST API for managing the operations of a restaurant.
Built with **Spring Boot**, **PostgreSQL**, and **Docker Compose**.

The API handles core restaurant management features such as:

- Dishes and categories
- Orders and order items
- Employees and roles
- Tables and restaurants
- Work schedules

---

## Tech Stack

- **Java / Spring Boot**
- **Spring Data JPA**
- **PostgreSQL**
- **PGAdmin**
- **Docker & Docker Compose**
- **Maven**

---

## Features

- CRUD operations for restaurant entities
- Order management system
- Employee and role management
- Work schedule tracking
- PostgreSQL persistence
- Containerized environment with Docker

---

## Project Structure

```
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

---

## Running the Project

### 1. Clone the repository

```
git clone https://github.com/TFG-Restaurant-Manager/Backend-RestaurantManager.git
cd Backend-RestaurantManager
```

---

### 2. Start with Docker Compose

```
docker compose up --build
```

This will start:

- **Spring Boot API**
- **PostgreSQL database**
- **PGAdmin PostgreSQL Monitoring**

---

### 3. API Access

Once running, the API will be available at:

```
http://localhost:8080
```

---

## Database

### The application uses **PostgreSQL**.

### Example Entities

Main entities managed by the API:

- **Restaurants**
- **Tables**
- **Dishes**
- **Orders**
- **Order Items**
- **Employees**
- **Roles**
- **Work Schedules**

---
