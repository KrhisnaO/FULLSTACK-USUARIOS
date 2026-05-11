# Microservicio de Usuarios — fullstack3

Microservicio REST desarrollado con **Spring Boot 3.5** que gestiona los usuarios del sistema ComputerStore. Forma parte del proyecto Full Stack III (DSY2205).

---

## Tecnologías

- Java 17
- Spring Boot 3.5 (Web, Data JPA, Validation, Actuator)
- Oracle Cloud (Wallet BBDDFS3)
- Lombok
- Docker
- JUnit 5 + Mockito

---

## Puerto

| Entorno | Puerto |
|---------|--------|
| Local   | `8081` |
| Docker  | `8081` |

---

## Endpoints disponibles

| Método | Ruta | Descripción |
|--------|------|-------------|
| `GET` | `/api/usuarios` | Lista todos los usuarios |
| `GET` | `/api/usuarios/{id}` | Obtiene un usuario por ID |
| `POST` | `/api/usuarios` | Crea un nuevo usuario |
| `POST` | `/api/usuarios/login` | Inicio de sesión (correo + password) |
| `PUT` | `/api/usuarios/{id}` | Actualiza un usuario existente |
| `DELETE` | `/api/usuarios/{id}` | Elimina un usuario |

---

## Estructura del proyecto

```
src/
├── main/java/com/fullstack3/fullstack3/
│   ├── controller/     UsuarioController.java
│   ├── service/        UsuarioService.java, UsuarioServiceImpl.java
│   ├── repository/     UsuarioRepository.java
│   ├── model/          Usuario.java
│   └── exception/      ResourceNotFoundException.java, GlobalExceptionHandler.java
└── test/java/com/fullstack3/fullstack3/
    ├── UsuarioServiceImplTest.java
    └── UsuarioControllerTest.java
```

---

## Levantar en local

### 1. Requisitos previos

- Java 17+
- Maven 3.8+
- Carpeta `Wallet_BBDDFS3/` presente en la raíz del proyecto

### 2. Compilar

```bash
mvn clean package -DskipTests
```

### 3. Ejecutar

```bash
mvn spring-boot:run
```

La API queda disponible en: `http://localhost:8081/api/usuarios`

---

## Levantar con Docker

```bash
# 1. Compilar primero
mvn clean package -DskipTests

# 2. Levantar contenedor
docker compose up --build
```

---

## Ejecutar pruebas unitarias

```bash
mvn test
```

Los tests cubren los métodos `listar`, `buscarPorId`, `guardar`, `actualizar`, `eliminar` y `login`, tanto a nivel de servicio (Mockito) como de controlador (MockMvc).

---

## Variables de entorno (Docker)

| Variable | Valor |
|----------|-------|
| `SPRING_DATASOURCE_URL` | `jdbc:oracle:thin:@bbddfs3_tp?TNS_ADMIN=/app/wallet` |
| `SPRING_DATASOURCE_USERNAME` | `ADMIN` |
| `SPRING_DATASOURCE_PASSWORD` | `BBDD_fullstack2026` |

---

## Roles de usuario

| Rol | Descripción |
|-----|-------------|
| `ADMIN` | Gestión completa de usuarios y productos |
| `CLIENTE` | Compra de productos y consulta de historial |
