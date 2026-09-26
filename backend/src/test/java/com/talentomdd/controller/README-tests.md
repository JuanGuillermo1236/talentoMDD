# Pruebas de integración pendientes

Ubicar aquí, en la Etapa 16 del plan, pruebas de integración con
`@SpringBootTest` + `MockMvc` para al menos:

- Login correcto → 200
- Login incorrecto → 401
- Endpoint protegido sin token → 401
- Usuario sin permiso (rol incorrecto) → 403
- Recurso inexistente → 404
- Registro duplicado → 409
- Postulación duplicada → 409

Se recomienda usar una base de datos H2 en memoria o Testcontainers con
PostgreSQL solo para el perfil de test (no afecta la BD de Mijail).
