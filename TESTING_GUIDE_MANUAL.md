# 🧪 Guía de Pruebas Manuales - Sistema de Seguridad JWT

## 📋 Prerequisitos

- ✅ Aplicación Spring Boot corriendo en `http://localhost:8080`
- ✅ Base de datos (SQL Server o MongoDB) configurada
- ✅ **Postman** instalado (descargar de https://www.postman.com/downloads/)
  - Alternativa: usar **cURL** desde terminal

---

## 🚀 PASO 1: Iniciar la Aplicación

### Opción A: Desde IDE (IntelliJ, Eclipse, VS Code)
```
1. Click derecho en proyecto
2. Run 'Application' (o la clase main)
3. Esperar mensaje: "Tomcat started on port(s): 8080"
```

### Opción B: Desde terminal (Maven)
```bash
cd ConstruccionSoftwareII2026LM8/cs2
mvn spring-boot:run
```

✅ Verificar que está corriendo:
```bash
curl http://localhost:8080/api/auth/login
# Si responde 405 Method Not Allowed, ¡está funcionando!
```

---

## 📱 PASO 2: Crear un Usuario de Prueba

Para poder hacer login, primero necesitamos un usuario en la BD.

### Insertar usuario en SQL Server (SQL Server Management Studio)

```sql
-- Abrir SQL Server Management Studio
-- Conectar a: SQLEXPRESS
-- Base de datos: BankDB

INSERT INTO [users] (id, username, password_hash, role, status, email, phone, address, birth_date)
VALUES 
(
    'USR001',                                                              -- id
    'juan_perez',                                                          -- username
    '$2a$10$slYQmyNdGzin7olVMwhK.OPST9/PgBkqquzi.Ss74KUgO7jNCRFom',      -- password_hash (BCrypt de "password123")
    'COMMERCIAL_EMPLOYEE',                                                 -- role
    'ACTIVE',                                                              -- status
    'juan.perez@bank.com',                                                 -- email
    '+57 320 123 4567',                                                    -- phone
    'Calle 123 #45-67, Bogotá',                                           -- address
    '1990-05-15'                                                           -- birth_date
);
```

**Password hasheado es para:** `password123`

### ⚠️ Si prefieres MD5 para pruebas rápidas (NO USAR EN PRODUCCIÓN):
En lugar de insertar manualmente, usa curl para registrar:
```bash
# Aún no tenemos endpoint de registro, así que haremos inserción directa
```

---

## 🔓 PASO 3: Autenticarse (LOGIN) - Obtener Token JWT

### Opción A: Con Postman (RECOMENDADO)

**1. Abrir Postman**

**2. Crear nueva request:**
- Method: **POST**
- URL: `http://localhost:8080/api/auth/login`
- Headers:
  - `Content-Type: application/json`

**3. Body (JSON raw):**
```json
{
  "username": "juan_perez",
  "password": "password123"
}
```

**4. Click "Send"**

**Respuesta esperada (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFuX3BlcmV6IiwiZG9jdW1lbnQiOiIxMDI2MzY2NjY2Iiwicm9sZSI6IkNPTU1FUkNJQUxfRU1QTE9ZRUUiLCJpYXQiOjE3MTYyMzk2MjUsImV4cCI6MTcxNjMyNjAyNX0.abc123...",
  "username": "juan_perez",
  "role": "COMMERCIAL_EMPLOYEE"
}
```

**✅ Guardar el token** (lo usaremos en los siguientes pasos)

---

### Opción B: Con cURL (Terminal)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juan_perez",
    "password": "password123"
  }'
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "juan_perez",
  "role": "COMMERCIAL_EMPLOYEE"
}
```

---

## 🔐 PASO 4: Acceder a Endpoint Protegido CON Token

### Opción A: Postman

**1. Crear nueva request GET:**
- Method: **GET**
- URL: `http://localhost:8080/api/clients/1` (o el ID que tengas)
- Headers:
  ```
  Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
  Content-Type: application/json
  ```

**⚠️ IMPORTANTE:** El header debe ser exactamente:
```
Authorization: Bearer <TOKEN_COMPLETO>
```

**2. Click "Send"**

**Respuesta esperada (200 OK):**
```json
{
  "id": "1",
  "name": "Cliente ABC",
  "email": "cliente@example.com",
  ...
}
```

---

### Opción B: cURL

```bash
TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

curl -X GET http://localhost:8080/api/clients/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json"
```

---

## ❌ PASO 5: Probar Errores - SIN Token (401)

### Opción A: Postman

**1. Crear request GET sin header Authorization:**
- Method: **GET**
- URL: `http://localhost:8080/api/clients/1`
- Headers: Solo `Content-Type: application/json`

**2. Click "Send"**

**Respuesta esperada (401 Unauthorized):**
```json
{
  "status": 401,
  "message": "No autorizado - Token inválido o ausente"
}
```

---

### Opción B: cURL

```bash
curl -X GET http://localhost:8080/api/clients/1 \
  -H "Content-Type: application/json"
```

---

## 🚫 PASO 6: Probar Errores - Token Inválido (401)

### Opcion A: Postman

**1. Usar un token fake:**
```
Authorization: Bearer invalid.token.here
```

**2. Click "Send"**

**Respuesta esperada (401 Unauthorized):**
```json
{
  "status": 401,
  "message": "No autorizado - Token inválido o ausente"
}
```

---

### Opción B: cURL

```bash
curl -X GET http://localhost:8080/api/clients/1 \
  -H "Authorization: Bearer invalid.token.here" \
  -H "Content-Type: application/json"
```

---

## 🔒 PASO 7: Probar Errores - Sin Permisos (403)

Para esto, necesitas un usuario con rol diferente.

### Crear segundo usuario (rol diferente):

```sql
INSERT INTO [users] (id, username, password_hash, role, status, email, phone, address, birth_date)
VALUES 
(
    'USR002',
    'carlos_teller',
    '$2a$10$slYQmyNdGzin7olVMwhK.OPST9/PgBkqquzi.Ss74KUgO7jNCRFom',  -- password123
    'TELLER_EMPLOYEE',  -- Este rol NO tiene acceso a /api/clients
    'ACTIVE',
    'carlos@bank.com',
    '+57 320 987 6543',
    'Calle 456 #78-90, Medellín',
    '1992-08-20'
);
```

### Probar acceso denegado:

**1. Login con el nuevo usuario:**
```json
{
  "username": "carlos_teller",
  "password": "password123"
}
```

**2. Obtener token y guardar**

**3. Intentar acceder a `/api/clients` con ese token:**
- URL: `http://localhost:8080/api/clients/1`
- Header: `Authorization: Bearer <TOKEN_CARLOS>`

**Respuesta esperada (403 Forbidden):**
```json
{
  "status": 403,
  "message": "Acceso denegado - No tienes los permisos requeridos"
}
```

---

## 🧩 PASO 8: Probar Diferentes Roles y Endpoints

### Usuarios de Prueba

```sql
-- COMMERCIAL_EMPLOYEE (acceso a clientes)
INSERT INTO [users] VALUES ('USR001', 'juan_perez', '$2a$10$slYQmyNdGzin7olVMwhK.OPST9/PgBkqquzi.Ss74KUgO7jNCRFom', 'COMMERCIAL_EMPLOYEE', 'ACTIVE', 'juan@bank.com', '+57 320 1111111', 'Calle 1', '1990-01-01');

-- INTERNAL_ANALYST (aprobación de préstamos)
INSERT INTO [users] VALUES ('USR003', 'ana_analyst', '$2a$10$slYQmyNdGzin7olVMwhK.OPST9/PgBkqquzi.Ss74KUgO7jNCRFom', 'INTERNAL_ANALYST', 'ACTIVE', 'ana@bank.com', '+57 320 2222222', 'Calle 2', '1991-02-02');

-- TELLER_EMPLOYEE (solo cuentas)
INSERT INTO [users] VALUES ('USR002', 'carlos_teller', '$2a$10$slYQmyNdGzin7olVMwhK.OPST9/PgBkqquzi.Ss74KUgO7jNCRFom', 'TELLER_EMPLOYEE', 'ACTIVE', 'carlos@bank.com', '+57 320 3333333', 'Calle 3', '1992-03-03');
```

### Tabla de Permisos

| Endpoint | GET | POST | Roles Requeridos |
|----------|-----|------|------------------|
| `/auth/login` | ❌ | ✅ | TODOS |
| `/clients/**` | ✅ | ❌ | COMMERCIAL_EMPLOYEE, COMPANY_SUPERVISOR, INTERNAL_ANALYST |
| `/loans/**/approve` | ❌ | ✅ | INTERNAL_ANALYST, COMPANY_SUPERVISOR |
| `/transfers` | ❌ | ✅ | AUTHENTICATED |
| `/accounts/**` | ✅ | ❌ | TELLER_EMPLOYEE, COMMERCIAL_EMPLOYEE |

---

## 📊 PASO 9: Examinar el Token JWT (Decodificar)

Puedes ver qué hay dentro del token sin secreto.

### Usar https://jwt.io

**1. Ir a https://jwt.io**

**2. Pegar tu token en "Encoded"**

**3. Ver en "Decoded":**
```json
HEADER: {
  "alg": "HS256",
  "typ": "JWT"
}

PAYLOAD: {
  "sub": "juan_perez",           // username
  "document": "1023664266",      // identificationNumber
  "role": "COMMERCIAL_EMPLOYEE", // rol
  "iat": 1716239625,            // issued at
  "exp": 1716326025             // expiration
}
```

---

## 🔍 PASO 10: Ver Logs en la Aplicación

### En la consola de la aplicación (salida estándar):

```
2024-05-20 14:30:45 DEBUG [http-nio-8080-exec-1] 
  Intercepting request to /api/auth/login

2024-05-20 14:30:46 DEBUG [http-nio-8080-exec-1]
  User juan_perez authenticated successfully
  Token generated: eyJhbGciOi...

2024-05-20 14:30:55 DEBUG [http-nio-8080-exec-2]
  JwtAuthenticationFilter: Validating token for user juan_perez
```

---

## 🎬 CHECKLIST DE PRUEBAS PARA TU PROFE

- ✅ **Test 1:** Login exitoso → obtener token (200 OK)
- ✅ **Test 2:** Acceso a endpoint protegido CON token válido (200 OK)
- ✅ **Test 3:** Acceso a endpoint protegido SIN token (401)
- ✅ **Test 4:** Token inválido/expirado (401)
- ✅ **Test 5:** Usuario sin permisos para endpoint (403)
- ✅ **Test 6:** Ver token decodificado en jwt.io
- ✅ **Test 7:** Cambiar rol del usuario en BD y ver que falla acceso
- ✅ **Test 8:** Verificar header Authorization correcto en logs

---

## 💡 Tips Adicionales

### Copiar/Pegar Tokens Fácilmente en Postman

1. En Postman, ir a **Environments** (engranaje)
2. Crear variable: `{{token}}`
3. En login response, ir a **Tests**:
```javascript
var jsonData = pm.response.json();
pm.environment.set("token", jsonData.token);
```
4. En otros requests usar: `Authorization: Bearer {{token}}`

### Expiración de Tokens

Por defecto: **24 horas** (86400000 ms)

Para probar expiración:
- Cambiar en `application.yml`: `expiration: 5000` (5 segundos)
- Esperar 5 segundos
- Intentar usar token → 401

### Generar Hash BCrypt de contraseña

Usar online: https://www.bcryptoniline.com/
- Texto: `password123`
- Rounds: 10
- Hash generado: `$2a$10$slYQmyNdGzin7olVMwhK.OPST9/PgBkqquzi.Ss74KUgO7jNCRFom`

---

## 📸 Ejemplo de Flujo Completo en Postman

```
1. POST /api/auth/login
   BODY: { "username": "juan_perez", "password": "password123" }
   ↓
   RESPONSE: { "token": "eyJ...", "username": "juan_perez", "role": "COMMERCIAL_EMPLOYEE" }

2. GET /api/clients/1
   HEADERS: Authorization: Bearer eyJ...
   ↓
   RESPONSE: { "id": "1", "name": "Cliente ABC", ... } (200 OK)

3. GET /api/clients/1 (SIN header)
   ↓
   RESPONSE: { "status": 401, "message": "No autorizado..." } (401)

4. GET /api/clients/1 (Con token de TELLER_EMPLOYEE)
   ↓
   RESPONSE: { "status": 403, "message": "Acceso denegado..." } (403)
```

---

## 🐛 Troubleshooting

| Error | Causa | Solución |
|-------|-------|----------|
| `curl: (7) Failed to connect` | App no está corriendo | Ejecutar `mvn spring-boot:run` |
| `404 Not Found` | Endpoint no existe | Verificar URL exacta con `/api` prefix |
| `405 Method Not Allowed` | GET en endpoint POST | Cambiar método HTTP |
| `400 Bad Request` | JSON mal formado | Verificar formato JSON |
| `401 Unauthorized` siempre | Token inválido/ausente | Copiar completo desde login response |
| `502 Bad Gateway` | Base de datos desconectada | Iniciar SQL Server o MongoDB |

---

**¡Listo para presentarle a tu profe!** 🎓

Cualquier error, revisa los logs de la aplicación para más detalles.
