# Sistema de Seguridad JWT - Arquitectura Limpia en Spring Boot 3.x

## 📋 Resumen del Flujo de Seguridad Implementado

El sistema de autenticación y autorización ha sido implementado bajo los principios de **Arquitectura Limpia**, separando responsabilidades en capas específicas:

### 1️⃣ **Flujo de Autenticación (Login)**

```
Cliente                    API                          Base de Datos
  │                         │                                │
  ├─ POST /api/auth/login ─→│                                │
  │  {username, password}   │                                │
  │                         ├─ AuthController               │
  │                         ├─ Buscar usuario por username ─→│
  │                         │                                │
  │                         │← Retorna User con datos       │
  │                         ├─ Validar contraseña (BCrypt)  │
  │                         ├─ Generar JWT Token            │
  │                         │  (JwtUtil.generateToken)       │
  │                         │                                │
  │←─ 200 OK {token} ───────┤                                │
  │   {username, role}      │                                │
```

### 2️⃣ **Flujo de Autorización (Request Protegido)**

```
Cliente                    API                    Spring Security
  │                         │                            │
  ├─ GET /api/clients/1 ───→│                            │
  │  Authorization:         │                            │
  │  Bearer <token>         │                            │
  │                         ├─ JwtAuthenticationFilter  │
  │                         ├─ Extraer token            │
  │                         ├─ Validar token (JWT)      │
  │                         ├─ Extraer username, role   │
  │                         ├─ Crear Authentication     │
  │                         ├─ Llenar SecurityContext   │
  │                         │                            │
  │                         ├─ SecurityConfig           │
  │                         ├─ Verificar autorización   │
  │                         ├─ (ROLE_COMMERCIAL_...)    │
  │                         │                            │
  │                         ├─ ClientController         │
  │                         │ (Request ejecutado)        │
  │                         │                            │
  │←─ 200 OK {data} ───────┤                            │
```

---

## 🔐 Componentes Implementados

### **1. JwtUtil.java** 
**Ubicación:** `app.infrastructure.security`

Responsabilidades:
- ✅ Generar tokens JWT con claims personalizados (`document`, `role`)
- ✅ Extraer información de tokens de forma segura
- ✅ Validar integridad y vigencia de tokens
- ✅ Uso de HMAC-SHA con clave segura desde propiedades

**Métodos principales:**
```java
// Genera token con document, username y role
generateToken(String document, String username, UserRole role)

// Extrae todos los claims del payload
extractAllClaims(String token) → Claims

// Métodos de conveniencia
extractUsername(String token) → String
extractDocument(String token) → String
extractRole(String token) → String
isTokenValid(String token) → boolean
```

### **2. JwtAuthenticationFilter.java**
**Ubicación:** `app.infrastructure.security`

Responsabilidades:
- ✅ Interceptar peticiones HTTP (OncePerRequestFilter)
- ✅ Extraer header `Authorization: Bearer <token>`
- ✅ Validar y procesar el token
- ✅ Llenar el `SecurityContextHolder` con datos del usuario
- ✅ NO hace consultas a BD innecesarias (datos vienen en JWT)

**Flujo:**
1. Si no existe header Authorization → delega a siguiente filtro
2. Si token es válido:
   - Extrae `username`, `document`, `role` del payload
   - Crea `SimpleGrantedAuthority` con formato `ROLE_<ROLE>`
   - Construye `UsernamePasswordAuthenticationToken`
   - Lo asigna al `SecurityContextHolder`

### **3. SecurityConfig.java**
**Ubicación:** `app.infrastructure.security`

Responsabilidades:
- ✅ Configuración centralizada de seguridad con lambdas Spring Security 6
- ✅ Estadeless sessions (sin sesiones HTTP, solo JWT)
- ✅ Manejo de excepciones con respuestas JSON limpias
- ✅ Reglas de autorización por endpoints
- ✅ Registro del filtro JWT en la cadena de filtros

**Configuración:**
```
Endpoint                               Autorización
─────────────────────────────────────────────────────────
POST   /api/auth/login                 ✓ Permitido para todos
POST   /api/auth/register              ✓ Permitido para todos
GET    /api/clients/**                 Requiere ROLE_COMMERCIAL_EMPLOYEE
POST   /api/loans/**/approve           Requiere ROLE_INTERNAL_ANALYST
POST   /api/transfers                  Requiere autenticación
GET    /api/accounts/**                Requiere ROLE_TELLER_EMPLOYEE
```

**Manejo de excepciones:**
- `401 Unauthorized`: Token inválido, expirado o ausente
- `403 Forbidden`: Usuario autenticado pero sin permisos suficientes
- Ambas retornan JSON: `{"status": 401/403, "message": "..."}`

### **4. AuthController.java**
**Ubicación:** `app.application.adapter.api.controllers`

Responsabilidades:
- ✅ Endpoint POST `/api/auth/login` para autenticación
- ✅ Validar credenciales contra BD
- ✅ Generar y retornar JWT token

**Lógica:**
1. Busca usuario por username en repositorio
2. Valida contraseña con BCrypt
3. Verifica que usuario esté activo
4. Genera token JWT con datos del usuario
5. Retorna token + metadata en JSON

---

## ⚙️ Dependencias Agregadas

**pom.xml** - JJWT 0.12.3 (versión no obsoleta):
```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
<!-- jjwt-impl y jjwt-jackson con scope runtime -->
```

---

## 🛠️ Configuración de Propiedades

**application.yml** - Propiedades JWT:
```yaml
app:
  jwt:
    secret: ${JWT_SECRET:your-super-secret-key-...}  # Min 32 chars
    expiration: 86400000                              # 24 horas en ms
```

**Variables de entorno (Producción):**
- `JWT_SECRET`: Clave segura para firmar tokens (mínimo 32 caracteres)
- Cambiar valor por defecto en producción

---

## 🔄 Extensiones al Repositorio

Se han actualizado las siguientes clases para soportar búsqueda por username:

1. **UserRepository.java** (dominio)
   - Nuevo método: `Optional<User> findByUsername(String username)`

2. **InMemoryUserRepository.java** (adaptador en memoria)
   - Implementación con stream/filter

3. **UserMongoRepository.java** (Spring Data MongoDB)
   - Nuevo método: `Optional<UserDocument> findByUsername(String username)`

4. **MongoDbUserRepository.java** (adaptador MongoDB)
   - Implementación delegando a MongoDB repository
   - Método `toDocument()` ahora incluye username

5. **UserDocument.java** (documento MongoDB)
   - Nuevo campo: `String username`
   - Getter/setter incluidos

---

## 📝 DTOs para Autenticación

### LoginDTO
```java
{
  "username": "juan_perez",
  "password": "password123"
}
```

### AuthResponseDTO (Respuesta exitosa)
```java
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "juan_perez",
  "role": "COMMERCIAL_EMPLOYEE"
}
```

### ErrorResponse (401/403)
```java
{
  "status": 401,
  "message": "No autorizado - Token inválido o ausente"
}
```

---

## 🚀 Uso en los Controladores

### Acceso a datos del usuario autenticado:
```java
@GetMapping("/profile")
public ResponseEntity<?> getProfile(Authentication authentication) {
    String username = authentication.getName();  // Del token JWT
    String document = (String) authentication.getDetails();  // Del token
    UserRole role = // Extraer de authorities
    
    // Usar datos para lógica de negocio...
}
```

### Extracción desde SecurityContextHolder:
```java
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
String username = auth.getName();
SimpleGrantedAuthority role = (SimpleGrantedAuthority) auth.getAuthorities().iterator().next();
```

---

## ✅ Checklist de Implementación

- ✅ Dependencia JJWT 0.12.3 agregada al pom.xml
- ✅ JwtUtil.java implementado con métodos seguros
- ✅ JwtAuthenticationFilter.java extendiendo OncePerRequestFilter
- ✅ SecurityConfig.java con lambdas Spring Security 6
- ✅ AuthController.java con endpoint /auth/login
- ✅ Manejo de excepciones con respuestas JSON
- ✅ Reglas de autorización por endpoints
- ✅ Configuración de propiedades JWT en application.yml
- ✅ Repositorio extendido con findByUsername()
- ✅ DTOs para Login y Response

---

## 🔍 Notas de Seguridad

1. **Secreto JWT**: Cambiar `JWT_SECRET` en producción (mínimo 32 caracteres)
2. **Expiración**: Configurado a 24 horas (86400000 ms) - ajustar según requerimientos
3. **CSRF**: Deshabilitado por ser API REST stateless
4. **CORS**: Puedes habilitarlo según necesidades con @CrossOrigin
5. **Contraseñas**: Se validan con BCryptPasswordEncoder
6. **No se validan credenciales en cada request**: El token es la fuente de verdad

---

## 📚 Referencias a Archivos

| Archivo | Ubicación |
|---------|-----------|
| JwtUtil.java | `app/infrastructure/security/` |
| JwtAuthenticationFilter.java | `app/infrastructure/security/` |
| SecurityConfig.java | `app/infrastructure/security/` |
| AuthController.java | `app/application/adapter/api/controllers/` |
| LoginDTO.java | `app/application/adapter/api/dto/` |
| AuthResponseDTO.java | `app/application/adapter/api/dto/` |
| pom.xml | Raíz del proyecto |
| application.yml | `src/main/resources/` |

---

**¡Sistema de seguridad completamente implementado y listo para usar!** 🎉
