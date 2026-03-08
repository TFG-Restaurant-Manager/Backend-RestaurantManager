# Backend Restaurant Manager

Este proyecto constituye el núcleo lógico para la gestión de servicios de restauración. Está construido bajo una arquitectura limpia y un sistema de seguridad robusto basado en tokens.

## Arquitectura del Sistema

El proyecto sigue un patrón de diseño por capas para garantizar la escalabilidad y el mantenimiento:

* **Controladores (`controller`):** Exponen los endpoints de la API. Gestionan la entrada y salida de datos utilizando objetos **DTO (Data Transfer Objects)**, clasificados en `Request` y `Response`.
* **Servicios (`service`):** Contienen la lógica de negocio, validaciones y la orquestación de llamadas a la capa de persistencia.
* **Repositorios (`repository`):** Encargados de la comunicación con el backend para la obtención, provisión y persistencia de datos.

---

## Seguridad y Autenticación

La seguridad está implementada mediante **Spring Security** y **JSON Web Tokens (JWT)** para la autenticación y autorización de las peticiones.

### Configuración de Seguridad
El archivo central de configuración es [`SecurityConfig.java`](src/main/java/com/tfg_rm/backend_restaurantmanager/config/SecurityConfig.java). Sus funciones principales incluyen:

* **Deshabilitación de CSRF:** Al utilizar JWT, la protección contra *Cross-Site Request Forgery* no es necesaria.
* **Gestión de Accesos:** * Los endpoints de WebSockets (`ws`) están permitidos (se gestionan de forma independiente).
    * El endpoint de autenticación (`auth`) es público para permitir el inicio de sesión.
    * **Nota:** Se requiere implementar medidas adicionales para mitigar ataques de fuerza bruta en el login.
    * El resto de los endpoints requieren autenticación y pueden estar restringidos por **Roles** integrados en el token.
* **Filtros Personalizados:** Se añade la validación de tokens antes del filtro estándar de Spring:
    ```java
    .addFilterBefore(
        new JwtAuthenticationFilter(jwtService),
        UsernamePasswordAuthenticationFilter.class
    )
    ```

### Gestión de Tokens (JWT)
El servicio [`JwtService`](src/main/java/com/tfg_rm/backend_restaurantmanager/shared/security/JwtService.java) centraliza la lógica de los tokens:

* **Firma:** Utiliza el algoritmo **HMAC-SHA**. La clave secreta se obtiene de variables de entorno (configurada en el `docker-compose.yml` para facilitar el despliegue en desarrollo).
* **Payload:** El token almacena información crítica como el `userId`, el `role` (Cliente, Encargado, Camarero, Cocinero) y el `restaurantId` para identificar el contexto del usuario en la base de datos.



### Filtro de Autenticación
El [`JwtAuthenticationFilter`](src/main/java/com/tfg_rm/backend_restaurantmanager/shared/security/JwtAuthenticationFilter.java) intercepta cada petición para:
1. Extraer y validar el token.
2. Generar un usuario verificado asignándole una autoridad con el prefijo `ROLE_`.
3. Establecer la identidad en el contexto de seguridad de la aplicación.

---

## Comunicación en Tiempo Real (WebSockets)

Para optimizar el rendimiento, los WebSockets utilizan un sistema de validación inicial en el *handshake* en lugar de verificar cada mensaje individual.

### Infraestructura de WebSocket
La configuración se encuentra en [`WebSocketConfig`](src/main/java/com/tfg_rm/backend_restaurantmanager/config/WebSocketConfig.java), donde se registran el `RestaurantWebSocketHandler` y el `JwtHandshakeInterceptor`.

* **Interceptación (`JwtHandshakeInterceptor`):** Antes de establecer la conexión, valida el JWT y transfiere los datos del token (`restaurantId`, `role`, `userId`) a los atributos de la sesión.
* **Gestión de Sesiones:** Se utiliza un mapa concurrente para agrupar conexiones por restaurante:
    ```java
    Map<String, Set<WebSocketSession>> restaurantSessions;
    ```

### Lógica del Handler
El [`RestaurantWebSocketHandler`](src/main/java/com/tfg_rm/backend_restaurantmanager/websocket/handler/RestaurantWebSocketHandler.java) gestiona el ciclo de vida de la conexión:

1.  **Conexión establecida:** Verifica el `restaurantId` en los atributos. Si no existe, cierra la conexión. Si existe, añade la sesión al mapa. Se utiliza una lógica optimizada para la gestión del set de sesiones:
    ```java
    restaurantSessions
        .computeIfAbsent(restaurantId, k -> ConcurrentHashMap.newKeySet())
        .add(session);
    ```
    *Esta instrucción comprueba la existencia del conjunto de sesiones, lo crea si es necesario y añade la nueva sesión en una sola operación atómica.*

2.  **Intercambio de mensajes:** Cuando un cliente envía un mensaje, el sistema identifica su `restaurantId` y retransmite el contenido exclusivamente a todas las sesiones asociadas a ese mismo restaurante, funcionando como un canal de comunicación privado por establecimiento.

3.  **Cierre de conexión:** Al desconectarse, la sesión se elimina del conjunto correspondiente en `restaurantSessions` para mantener actualizada la lista de clientes activos.
