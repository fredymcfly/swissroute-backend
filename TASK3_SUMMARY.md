# Tarea 3 - Implementación de Login con JWT

## Descripción

Se ha implementado la funcionalidad de login con JWT (JSON Web Token) para usuarios registrados, permitiendo el acceso a endpoints protegidos.

## Cambios Implementados

### 1. Controlador de Autenticación
- `AuthController.java`: Endpoint POST `/api/usuarios/login` que valida credenciales y genera token JWT
- Requiere email y contraseña en el cuerpo de la solicitud

### 2. DTOs
- `LoginRequest.java`: DTO para la entrada de credenciales de login
- `LoginResponse.java`: DTO para la respuesta con el token JWT

### 3. Utilidades
- `JwtUtil.java`: Utilidad para generación y validación de tokens JWT
- `CustomUserDetailsService.java`: Implementación de UserDetailsService para carga de usuarios

### 4. Configuración de Seguridad
- `JwtAuthenticationFilter.java`: Filtro de autenticación JWT para proteger endpoints
- `SecurityConfig.java`: Configuración de seguridad Spring Security

### 5. Configuración
- `application.properties`: Añadidas propiedades para JWT:
  - `jwt.secret`: Clave secreta para firma de tokens (por defecto: "swissroute_jwt_secret_key_that_should_be_changed_in_production")
  - `jwt.expiration`: Tiempo de expiración en segundos (por defecto: 86400 = 24 horas)

## Requisitos Cumplidos

✅ POST endpoint `/api/usuarios/login` que valida credenciales y devuelve JWT  
✅ Devuelve 401 Unauthorized para credenciales inválidas  
✅ Token JWT con expiración configurable desde application.properties  
✅ Todos los otros endpoints requieren token en header Authorization: Bearer  
✅ Integración con repositorio de usuarios existente  
✅ Uso de BCrypt para encriptación de contraseñas  

## Funcionamiento

1. El usuario envía una solicitud POST a `/api/usuarios/login` con email y contraseña
2. El sistema valida las credenciales con AuthenticationManager
3. Si son válidas, se genera un JWT token usando JwtUtil
4. El token se devuelve en la respuesta
5. Para acceder a endpoints protegidos, el cliente debe incluir el token en el header: `Authorization: Bearer <token>`

## Testing

- Se han creado tests unitarios para `JwtUtil` que pasan correctamente
- Se han creado tests para `AuthController` 
- El proyecto compila y se ejecuta correctamente con Docker
- Tests de integración completos funcionan

## Dependencias Agregadas

- jjwt-api, jjwt-impl, jjwt-jackson: Para funcionalidad JWT
- spring-boot-starter-security: Para autenticación y seguridad