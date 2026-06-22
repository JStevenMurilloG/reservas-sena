# AgendaSENA

API REST para gestion de reservas de ambientes de formacion del SENA.

## Stack

- Java 17 + Spring Boot 4.0.6
- Spring Data JPA + MySQL
- Maven (wrapper 3.9.16)
- Lombok + Bean Validation

## Requisitos

- JDK 17+
- MySQL 8+ corriendo en `localhost:3306`
- Base de datos `agenda_sena` creada

## Configuracion de BD

Las credenciales estan en `src/main/resources/application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/agenda_sena
spring.datasource.username=root
spring.datasource.password=root
```

Hibernate crea las tablas automaticamente con `ddl-auto=update`.

## Ejecutar

```bash
mvnw spring-boot:run
```

La aplicacion arranca en `http://localhost:8080`.

Al iniciar, carga 5 ambientes y 5 reservas de ejemplo automaticamente (si la BD esta vacia).

## Endpoints

| Metodo | Ruta | Descripcion |
|--------|------|-------------|
| POST | `/api/ambientes` | Registrar ambiente |
| GET | `/api/ambientes` | Listar ambientes |
| GET | `/api/ambientes/{id}` | Ambiente por ID |
| PUT | `/api/ambientes/{id}` | Actualizar ambiente |
| DELETE | `/api/ambientes/{id}` | Eliminar ambiente |
| POST | `/api/reservas` | Crear reserva (valida 7 reglas de negocio) |
| PATCH | `/api/reservas/{id}/cancelar` | Cancelar reserva |
| GET | `/api/reservas` | Listar reservas |
| GET | `/api/reservas/{id}` | Reserva por ID |
| GET | `/api/ambientes/{id}/reservas?fecha=YYYY-MM-DD` | Reservas activas de un ambiente en una fecha |
| GET | `/api/ambientes/disponibles?inicio=...&fin=...` | Ambientes disponibles en un rango |
| GET | `/api/reportes/ocupacion?fecha=YYYY-MM-DD` | Reporte de horas de ocupacion por ambiente |

## Reglas de negocio

| # | Regla | Codigo HTTP |
|---|-------|-------------|
| 1 | Sin cruces de horario - un ambiente no puede tener dos reservas ACTIVAS que se solapen | 409 |
| 2 | Capacidad - el numero de aprendices no puede superar la capacidad del ambiente | 400 |
| 3 | Horario institucional - solo entre 6:00 y 22:00, duracion entre 1 y 4 horas | 400 |
| 4 | No reservar ambientes inactivos (`activo = false`) | 400 |
| 5 | Maximo 3 reservas ACTIVAS por instructor por dia | 400 |
| 6 | Cancelacion con minimo 2 horas de anticipacion | 400 |
| 7 | No reservar en fechas pasadas | 400 |

## Pruebas

Archivo `pruebas-api.http` con casos de exito y error para cada endpoint y regla.
Abrirlo en IntelliJ o VS Code con REST Client y ejecutar las peticiones.

## Estructura del proyecto

```
controller/   → Recibe HTTP, delega a service
service/      → Logica de negocio y reglas
repository/   → Spring Data JPA + @Query personalizadas
entity/       → Clases JPA (Ambiente, Reserva)
dto/          → Objetos de transferencia request/response
enums/        → TipoAmbiente, TipoEstado
exception/    → Excepciones personalizadas + @RestControllerAdvice
config/       → CommandLineRunner con datos de prueba
```
