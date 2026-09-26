# TalentoMDD

Plataforma web de empleabilidad juvenil que conecta estudiantes y
egresados de la UNAMAD con empresas de todo el Perú, mediante un
sistema de matching de habilidades entre el perfil del estudiante y
los requisitos de cada vacante.

```
ESTUDIANTE + HABILIDADES + VACANTE + MATCHING + POSTULACIÓN
```

## Estructura del repositorio

```
TalentoMDD/
├── backend/    Backend Java/Spring Boot (responsabilidad de Juan) — ver backend/README.md
├── frontend/   Frontend (responsabilidad de Alexandro)
├── database/   Modelo/scripts de base de datos definitivos (responsabilidad de Mijail)
├── docs/       Documentación funcional, requisitos, historias de usuario (Yeremi)
└── README.md
```

## Equipo

| Integrante | Responsabilidad |
|---|---|
| Juan | Coordinador del proyecto, backend e integración general |
| Antony | Motor de matching |
| Mijail | Base de datos y parte de la seguridad |
| Alexandro | Frontend |
| Yeremi | Requisitos funcionales e idea del producto |

## Empezar

Toda la documentación técnica del backend (arquitectura, cómo
ejecutar, endpoints, roles, autenticación) está en
[`backend/README.md`](backend/README.md).

## Git

- `main`: rama principal (protegida, no se trabaja directamente sobre ella)
- `develop`: rama de integración
- `feature/*`: ramas de funcionalidad, ej. `feature/backend-auth`,
  `feature/backend-student`, `feature/backend-company`,
  `feature/backend-vacancy`, `feature/backend-application`,
  `feature/backend-matching-integration`

El código se integra mediante pull requests.
