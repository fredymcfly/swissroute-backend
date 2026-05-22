# 🚆 SwissRoute Backend

Backend en Spring Boot para planificación de viajes en transporte público, integrado con la Swiss Public Transport API.

---

## 📦 Stack tecnológico

- Java 17
- Spring Boot 3.x
- Spring WebFlux (WebClient)
- PostgreSQL
- Spring Data JPA
- Flyway
- Docker + Docker Compose
- Swagger / OpenAPI

---

## ⚙️ Configuración de variables de entorno

El proyecto utiliza variables de entorno para evitar credenciales hardcodeadas.

Crear un archivo `.env` en la raíz del proyecto:

```env
# ===============================
# SPRING BOOT - DATABASE
# ===============================
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/swissroute
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# ===============================
# POSTGRES (Docker)
# ===============================
POSTGRES_DB=swissroute
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres

# ===============================
# API EXTERNA
# ===============================
SWISS_API_BASE_URL=http://transport.opendata.ch/v1

```


🐳 Ejecutar el proyecto con Docker
🔧 Build + Run (modo desarrollo)
docker compose --env-file .env up --build

🚀 Ejecutar en segundo plano
docker compose --env-file .env up --build -d

🛑 Parar el proyecto
docker compose down

🧹 Reset completo (borra base de datos)
docker compose down -v

🌐 Acceso a la aplicación
API REST: http://localhost:8080
Swagger UI: http://localhost:8080/swagger-ui.html

🗄️ Base de datos

La base de datos se crea automáticamente al levantar el proyecto mediante:

PostgreSQL en Docker
Flyway migrations (db/migration/V1__init_schema.sql)

🧪 Verificación rápida
Logs de la aplicación
docker logs swissroute-app
Ver contenedores activos
docker ps

📌 Arquitectura del proyecto
Controller → API REST
Service → Lógica de negocio
Client → WebClient (API externa)
Repository → Acceso a datos
Entity → Modelo BD
DTO → Comunicación externa/interna

🚀 Ejecución completa (resumen)
git clone <repo>
cd swissroute-backend
docker compose --env-file .env up --build

---
