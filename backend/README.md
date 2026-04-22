# Backend

Spring Boot REST API for the Berlin Clock kata.

## Stack

- Java 21, Spring Boot 3.4.4
- Virtual threads (`spring.threads.virtual.enabled=true`)
- springdoc-openapi for the Swagger UI
- JUnit 5 + Mockito + AssertJ for tests

## Architecture

Clean Architecture (simplified) with four layers, structure:

```
villanidev.berlinclock/
├── domain/          Domain objects and business logic
├── application/     Use cases, orchestrates domain objects
├── api/             REST controllers, DTOs, exception handler
└── infrastructure/  Spring config, SSE service, CORS, scheduler
```

### Domain

`BerlinClockTime` is a record holding the five rows of the clock as strings. `RowCalculator` is the interface each row implements. The calculators (`SecondsLampCalculator`, `FiveHoursRowCalculator`, etc.).

`RowUtils` is a utility class with shared constants (`Y`, `R`, `O`) and helpers used by the calculators.

### Application

`BerlinClockService` converts a `LocalTime` to a `BerlinClockTime` and back. `LiveClockService` defines the contract for broadcasting live clock events.

### API

`BerlinClockController` exposes three endpoints:

| Method | Path                              | Description                        |
| ------ | --------------------------------- | ---------------------------------- |
| GET    | `/api/v1/berlin-clock/to-berlin`  | Digital time → Berlin Clock string |
| GET    | `/api/v1/berlin-clock/to-digital` | Berlin Clock string → digital time |
| GET    | `/api/v1/berlin-clock/live`       | SSE stream, one event per second   |

`GlobalExceptionHandler` maps validation errors and illegal arguments to structured 400 responses.

Swagger UI is available at `/swagger-ui.html` (disabled in prod).

### Infrastructure

`SseLiveClockService` manages a list of `SseEmitter` instances. A scheduler fires every second to broadcast the current time and every 15 seconds for a heartbeat.

`CorsConfig` reads allowed origins from `cors.allowed-origins` (set per profile). `SchedulerConfig` creates a dedicated two-thread pool for SSE tasks.

## Profiles

| Profile | Purpose                                                 |
| ------- | ------------------------------------------------------- |
| `local` | docker-compose — verbose logging, `show-details=always` |
| `prod`  | Railway — CORS and timezone injected via env vars       |

Activate with `SPRING_PROFILES_ACTIVE=local` (already set in `docker-compose.yml`).

## Running locally without Docker

```bash
cd backend
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
```

API available at `http://localhost:8080`.

## Tests

```bash
./mvnw test
```

74 tests across all four layers. Each layer has its own test class. Domain and application tests are pure unit tests. The API test uses `@SpringBootTest` with `MockMvc`.
