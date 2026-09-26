# TalentoMDD — Backend

Backend de **TalentoMDD**, plataforma de empleabilidad juvenil que conecta
estudiantes/egresados de la UNAMAD con empresas de Perú, mediante un
sistema de matching de habilidades entre estudiantes y vacantes.

Este README documenta la parte de **backend**, responsabilidad de Juan
(coordinador del proyecto e integrador general).

## 1. Qué es TalentoMDD

TalentoMDD conecta: `ESTUDIANTE + HABILIDADES + VACANTE + MATCHING + POSTULACIÓN`.
La Fase 1 se enfoca en estudiantes/egresados UNAMAD; las empresas pueden
estar en cualquier parte del Perú y publicar vacantes presenciales,
híbridas o remotas.

## 2. Tecnologías

- Java 21 (LTS)
- Spring Boot 3.3.x
- Maven
- Spring Web, Spring Data JPA (Hibernate)
- PostgreSQL
- Spring Security + JWT (JJWT)
- Jakarta Validation (Bean Validation)
- springdoc-openapi (Swagger UI)
- Lombok
- JUnit 5 + Mockito + AssertJ (pruebas)

Arquitectura en capas, sin microservicios:

```
Controller -> Service -> Repository -> PostgreSQL
```

## 3. Requisitos para ejecutar

- JDK 21
- Maven 3.9+ (o usar el wrapper si se agrega `mvnw` más adelante)
- PostgreSQL 14+ corriendo localmente o accesible por red
- (Opcional) Postman / Insomnia / Swagger UI para probar la API

## 4. Cómo clonar

```bash
git clone <url-del-repositorio>
cd TalentoMDD/backend
```

## 5. Cómo configurar variables de entorno

1. Copiar `.env.example` como referencia de las variables necesarias.
2. Definirlas como variables de entorno del sistema/IDE, o mediante un
   archivo `.env` cargado por tu herramienta preferida (el `.env` real
   **nunca** debe subirse a git; ya está en `.gitignore`).

Variables principales:

| Variable            | Descripción                                    |
|---------------------|-------------------------------------------------|
| `DB_URL`            | URL JDBC de PostgreSQL                          |
| `DB_USERNAME`       | Usuario de la base de datos                     |
| `DB_PASSWORD`       | Contraseña de la base de datos                  |
| `JWT_SECRET`        | Secreto para firmar los JWT (mínimo 32 caracteres) |
| `JWT_EXPIRATION_MS` | Duración del token en milisegundos              |
| `FRONTEND_ORIGIN`   | Origen permitido por CORS (URL del frontend)    |
| `DDL_AUTO`          | Estrategia de Hibernate (`update` recomendado)  |

3. Crear la base de datos en PostgreSQL:

```sql
CREATE DATABASE talentomdd;
```

## 6. Cómo ejecutar

```bash
cd backend
mvn spring-boot:run
```

La API quedará disponible en `http://localhost:8080`.

Swagger UI: `http://localhost:8080/swagger-ui.html`

## 7. Cómo ejecutar pruebas

```bash
mvn test
```

Actualmente incluye pruebas unitarias de `AuthService`. Ver
`src/test/java/com/talentomdd/controller/README-tests.md` para las
pruebas de integración pendientes (Etapa 16 del plan).

## 8. Estructura del backend

```
backend/
├── src/main/java/com/talentomdd/
│   ├── TalentoMddApplication.java
│   ├── config/         (SecurityConfig, OpenApiConfig)
│   ├── controller/      (Auth, Student, Company, Vacancy, Application, Match)
│   ├── dto/             (requests/responses)
│   ├── entity/          (User, Student, Company, Skill, Vacancy, Application, ...)
│   ├── repository/      (Spring Data JPA)
│   ├── service/         (lógica de negocio)
│   ├── security/        (JWT, filtros, UserDetails)
│   ├── exception/       (excepciones + GlobalExceptionHandler)
│   ├── mapper/          (Entity <-> DTO)
│   ├── matching/         (contrato MatchingService + implementación provisional)
│   └── util/
├── src/main/resources/
│   ├── application.properties
│   └── application-dev.properties
├── src/test/
├── pom.xml
├── .env.example
└── .gitignore
```

## 9. Endpoints principales

**Auth** (público)
- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`

**Students** (rol `STUDENT`)
- `GET /api/v1/students/me`
- `PUT /api/v1/students/me`
- `GET /api/v1/students/me/skills`
- `POST /api/v1/students/me/skills`
- `DELETE /api/v1/students/me/skills/{skillId}`
- `GET /api/v1/students/me/applications`

**Companies** (rol `COMPANY`)
- `GET /api/v1/companies/me`
- `PUT /api/v1/companies/me`
- `GET /api/v1/companies/me/applications`

**Vacancies** (lectura pública, escritura `COMPANY`)
- `GET /api/v1/vacancies`
- `GET /api/v1/vacancies/{id}`
- `POST /api/v1/vacancies`
- `PUT /api/v1/vacancies/{id}`
- `DELETE /api/v1/vacancies/{id}`

**Applications**
- `POST /api/v1/applications` (rol `STUDENT`)

**Matching**
- `GET /api/v1/vacancies/{vacancyId}/match` (rol `STUDENT`)
- `GET /api/v1/vacancies/{vacancyId}/candidates` (rol `COMPANY`)

Todas las respuestas usan JSON. Errores siguen el formato estándar
(ver sección 11).

## 10. Roles

`STUDENT`, `COMPANY`, `ADMIN` (ADMIN no puede autoregistrarse; se crea
manualmente en base de datos por ahora — pendiente de definir un flujo
formal con el equipo).

## 11. Autenticación

- `POST /api/v1/auth/register` crea el `User` + su perfil (`Student` o
  `Company`) y devuelve un JWT.
- `POST /api/v1/auth/login` valida credenciales (password hasheado con
  BCrypt) y devuelve un JWT.
- El JWT incluye `sub` (email), `userId` y `role`, y se envía en cada
  petición protegida como `Authorization: Bearer <token>`.
- `JwtAuthFilter` valida el token en cada request antes de llegar a los
  controllers; `@PreAuthorize("hasRole('...')")` controla autorización
  por rol en cada endpoint.

Formato estándar de error:

```json
{
  "timestamp": "2026-09-25T12:00:00",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Los datos enviados no son válidos",
  "path": "/api/v1/vacancies"
}
```

## 12. Integración con matching (Antony)

`matching/MatchingService` define el contrato de entrada/salida
(`calculateMatch`, `findCandidatesByMatch`, ver `MatchResponse` /
`CandidateMatchResponse`). Actualmente `MatchingServiceStubImpl` calcula
un porcentaje simple de intersección de habilidades **solo como
implementación provisional** mientras Antony entrega el motor
definitivo. Ver comentarios en esa clase para el procedimiento de
reemplazo.

## 13. Integración con PostgreSQL

El modelo de entidades (`entity/`) es **provisional**: Mijail es
responsable del esquema definitivo de base de datos. `spring.jpa.hibernate.ddl-auto`
está en `update` por defecto (nunca usar `create` en un entorno
compartido). Cuando exista el esquema definitivo, adaptar las entidades
y coordinar cualquier cambio de relaciones críticas con el equipo.

---

## Etapas de implementación (según el plan acordado)

- [x] Etapa 1-4: Proyecto Spring Boot, Maven, estructura de paquetes, PostgreSQL
- [x] Etapa 5: Seguridad (JWT, filtros, BCrypt, CORS)
- [x] Etapa 6: Autenticación (registro/login)
- [x] Etapa 7: Usuarios y roles
- [x] Etapa 8: Estudiante (perfil)
- [x] Etapa 9: Empresa (perfil)
- [x] Etapa 10: Habilidades
- [x] Etapa 11: Vacantes
- [x] Etapa 12: Postulaciones
- [x] Etapa 13: Interfaz de matching
- [x] Etapa 14: Integración de matching (versión provisional; pendiente motor real de Antony)
- [x] Etapa 15: Documentación (README + OpenAPI/Swagger anotado)
- [~] Etapa 16: Pruebas (unitarias de ejemplo listas; integración pendiente)
- [ ] Etapa 17: Preparar integración con frontend (pendiente definir origen real con Alexandro)

## Pendiente de coordinar con el equipo

- **Mijail**: validar/reemplazar el modelo de entidades por el esquema
  definitivo de base de datos; revisar recomendaciones de seguridad.
- **Antony**: confirmar el contrato de entrada/salida del matching y
  reemplazar `MatchingServiceStubImpl` por su implementación.
- **Alexandro**: confirmar el origen (`FRONTEND_ORIGIN`) para CORS y
  validar los contratos JSON de cada endpoint.
- **Yeremi**: validar que los endpoints y reglas de negocio cubran los
  requisitos e historias de usuario definidos.
