# 🎬 DEMOSTRACIÓN PASO A PASO - Con Capturas de Pantalla Simuladas

## 📖 ESCENARIO: Mostrarle a tu Profe

Tu profe quiere ver:
1. ✅ Autenticación funcionando (login)
2. ✅ JWT token generado correctamente
3. ✅ Acceso a endpoints protegidos
4. ✅ Rechazo de peticiones sin permiso (403)
5. ✅ Rechazo de peticiones sin token (401)

---

## 🟢 PASO 1: Insertar Usuarios en BD

### Abrir SQL Server Management Studio

```
┌─────────────────────────────────────────────────┐
│ SQL Server Management Studio                    │
├─────────────────────────────────────────────────┤
│                                                 │
│  Server name: SQLEXPRESS                        │
│  Authentication: Windows Authentication         │
│  [Connect]                                      │
│                                                 │
└─────────────────────────────────────────────────┘
```

### Ejecutar Script SQL

```
1. Archivo → New Query Window
2. Copiar contenido de: INSERT_TEST_USERS.sql
3. Ctrl+A → Ctrl+E (Ejecutar)
```

**Resultado esperado:**
```
┌────────────────────────────────┐
│ Query executed successfully    │
│ (5 rows affected)              │
└────────────────────────────────┘
```

---

## 🟢 PASO 2: Iniciar la Aplicación Spring Boot

### Desde Terminal (Git Bash / CMD)

```bash
cd C:\Users\miguel angel\Desktop\bank\ConstruccionSoftwareII2026LM8\cs2
mvn clean spring-boot:run
```

### Consola de salida esperada:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_|\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

2024-05-20 14:25:30.123 INFO  Starting BankApplication v0.0.1-SNAPSHOT
2024-05-20 14:25:35.456 INFO  Tomcat started on port(s): 8080 (http) with context path '/api'
2024-05-20 14:25:35.789 INFO  Started BankApplication in 5.234 seconds
```

✅ **LA APP ESTÁ CORRIENDO**

---

## 🟢 PASO 3: DEMO 1 - LOGIN EXITOSO (200 OK)

### Abrir Postman

```
┌────────────────────────────────────────────────────────┐
│ POSTMAN - New Tab                                      │
├────────────────────────────────────────────────────────┤
│                                                        │
│  [POST] http://localhost:8080/api/auth/login           │
│                                                        │
│  Headers:                                              │
│  Content-Type: application/json                        │
│                                                        │
│  Body (raw, JSON):                                     │
│  {                                                     │
│    "username": "juan_perez",                           │
│    "password": "password123"                           │
│  }                                                     │
│                                                        │
│  [Send]                                                │
│                                                        │
└────────────────────────────────────────────────────────┘
```

### Respuesta (Status 200 OK):

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqdWFuX3BlcmV6IiwiZG9jdW1lbnQiOiIxMDI2MzY2NjY2Iiwicm9sZSI6IkNPTU1FUkNJQUxfRU1QTE9ZRUUiLCJpYXQiOjE3MTYyMzk2MjUsImV4cCI6MTcxNjMyNjAyNX0.lXpDZ9xz8J2K4m9nP5qR7sT1uV3wX8yZ0aB2cD4eF5g",
  "username": "juan_perez",
  "role": "COMMERCIAL_EMPLOYEE"
}
```

📋 **COPIAR el token completo**

---

## 🟢 PASO 4: DEMO 2 - Acceder a Endpoint PROTEGIDO con Token Válido

### Nueva Request en Postman

```
┌────────────────────────────────────────────────────────┐
│ POSTMAN - New Tab                                      │
├────────────────────────────────────────────────────────┤
│                                                        │
│  [GET] http://localhost:8080/api/clients/1             │
│                                                        │
│  Headers:                                              │
│  Authorization: Bearer eyJhbGciOiJIUzI1NiIs...        │
│  Content-Type: application/json                        │
│                                                        │
│  [Send]                                                │
│                                                        │
└────────────────────────────────────────────────────────┘
```

### Respuesta (Status 200 OK - Acceso Permitido):

```json
{
  "id": "1",
  "name": "Cliente ABC",
  "email": "cliente@example.com",
  "status": "ACTIVE",
  ...más datos del cliente...
}
```

✅ **Token válido → Acceso concedido**

---

## 🔴 PASO 5: DEMO 3 - SIN Token (401 Unauthorized)

### Request sin header Authorization

```
┌────────────────────────────────────────────────────────┐
│ POSTMAN - New Tab                                      │
├────────────────────────────────────────────────────────┤
│                                                        │
│  [GET] http://localhost:8080/api/clients/1             │
│                                                        │
│  Headers:                                              │
│  Content-Type: application/json                        │
│  ❌ NO agregar "Authorization"                         │
│                                                        │
│  [Send]                                                │
│                                                        │
└────────────────────────────────────────────────────────┘
```

### Respuesta (Status 401 Unauthorized):

```json
{
  "status": 401,
  "message": "No autorizado - Token inválido o ausente"
}
```

✅ **Sin token → 401 Unauthorized (como esperado)**

---

## 🔴 PASO 6: DEMO 4 - Token Inválido (401)

### Request con token FAKE

```
┌────────────────────────────────────────────────────────┐
│ POSTMAN - New Tab                                      │
├────────────────────────────────────────────────────────┤
│                                                        │
│  [GET] http://localhost:8080/api/clients/1             │
│                                                        │
│  Headers:                                              │
│  Authorization: Bearer invalid.fake.token.here         │
│  Content-Type: application/json                        │
│                                                        │
│  [Send]                                                │
│                                                        │
└────────────────────────────────────────────────────────┘
```

### Respuesta (Status 401 Unauthorized):

```json
{
  "status": 401,
  "message": "No autorizado - Token inválido o ausente"
}
```

✅ **Token inválido → 401 Unauthorized**

---

## 🔴 PASO 7: DEMO 5 - Sin Permisos (403 Forbidden)

### Primero: Logear con usuario diferente (TELLER_EMPLOYEE)

```
POST http://localhost:8080/api/auth/login

{
  "username": "carlos_teller",
  "password": "password123"
}

Respuesta:
{
  "token": "eyJhbGciOiJIUzI1...",
  "username": "carlos_teller",
  "role": "TELLER_EMPLOYEE"
}
```

### Luego: Intentar acceder a /clients con ese token

```
┌────────────────────────────────────────────────────────┐
│ POSTMAN - New Tab                                      │
├────────────────────────────────────────────────────────┤
│                                                        │
│  [GET] http://localhost:8080/api/clients/1             │
│                                                        │
│  Headers:                                              │
│  Authorization: Bearer eyJhbGciOiJIUzI1...TELLER...    │
│  Content-Type: application/json                        │
│                                                        │
│  [Send]                                                │
│                                                        │
└────────────────────────────────────────────────────────┘
```

### Respuesta (Status 403 Forbidden):

```json
{
  "status": 403,
  "message": "Acceso denegado - No tienes los permisos requeridos"
}
}
```

✅ **Token válido pero sin permisos → 403 Forbidden (como esperado)**

---

## 🔍 PASO 8: DEMO BONUS - Ver contenido del Token JWT

### Ir a https://jwt.io

```
┌──────────────────────────────────────────────────────┐
│ JWT Debugger - https://jwt.io                        │
├──────────────────────────────────────────────────────┤
│                                                      │
│  Pegar en "Encoded":                                 │
│  eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJz...      │
│                                                      │
│  Resultado en "Decoded":                             │
│                                                      │
│  HEADER:                                             │
│  {                                                   │
│    "alg": "HS256",                                   │
│    "typ": "JWT"                                      │
│  }                                                   │
│                                                      │
│  PAYLOAD:                                            │
│  {                                                   │
│    "sub": "juan_perez",                              │
│    "document": "1026366666",                         │
│    "role": "COMMERCIAL_EMPLOYEE",                    │
│    "iat": 1716239625,                                │
│    "exp": 1716326025                                 │
│  }                                                   │
│                                                      │
│  SIGNATURE:                                          │
│  ✅ Verified with your secret                        │
│                                                      │
└──────────────────────────────────────────────────────┘
```

📌 **El profe puede ver exactamente qué datos están en el token**

---

## 📊 TABLA RESUMEN DE DEMOS

| # | Request | Status | Descripción |
|---|---------|--------|-------------|
| 1 | POST /login | 200 | Login exitoso → obtiene token |
| 2 | GET /clients (CON token válido) | 200 | Acceso concedido |
| 3 | GET /clients (SIN token) | 401 | No hay token |
| 4 | GET /clients (token inválido) | 401 | Token corrupto |
| 5 | GET /clients (TELLER sin permiso) | 403 | Token válido pero sin permisos |
| 6 | GET /accounts (TELLER con permiso) | 200 | TELLER puede acceder a accounts |
| 7 | POST /loans/approve (ANALYST) | 200 | ANALYST puede aprobar |
| 8 | POST /loans/approve (COMMERCIAL) | 403 | COMMERCIAL no puede aprobar |

---

## 🎯 Guión para Presentar al Profe

### Introducción (30 segundos)
"Implementé un sistema de seguridad JWT con Spring Security 6 usando lambdas, siguiendo Arquitectura Limpia. El flujo es:
1. Usuario hace login
2. Sistema genera JWT con datos del usuario
3. Cliente envía JWT en cada request
4. Servidor valida JWT sin consultar BD
5. Autoriza según roles"

### Demostración (5 minutos)
1. **Login:** "Primero hacemos login..."
   - Mostrar POST /login
   - Mostrar token generado
   - Destacar: contiene `username`, `document`, `role`

2. **Acceso Autorizado:** "Con el token, accedemos al recurso..."
   - Mostrar GET /clients con token → 200 OK

3. **Sin Token:** "Si no enviamos token..."
   - Mostrar GET /clients sin token → 401

4. **Token Inválido:** "Si mandamos un token falso..."
   - Mostrar GET /clients con token fake → 401

5. **Sin Permisos:** "Con otro usuario sin permiso..."
   - Login como TELLER
   - GET /clients con token TELLER → 403

6. **Token Decodificado:** "El token contiene estos datos..."
   - Mostrar en jwt.io
   - Leer payload: username, role, expiration

### Conclusión (30 segundos)
"Todo está en Arquitectura Limpia: la lógica en capas, JWT en infraestructura, y los controladores no conocen detalles de seguridad. Puedo mostrar el código..."

---

## 🎬 Script Bash para Demo Automática

```bash
#!/bin/bash
# Demostración automática

echo "🔐 DEMO DE SEGURIDAD JWT"
echo ""

# 1. Login
echo "1️⃣  Haciendo LOGIN..."
RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"juan_perez","password":"password123"}')

TOKEN=$(echo $RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)
echo "✅ Token obtenido: ${TOKEN:0:50}..."
echo ""

# 2. Acceso con token
echo "2️⃣  Accediendo a /clients CON token..."
curl -s -X GET http://localhost:8080/api/clients/1 \
  -H "Authorization: Bearer $TOKEN" | jq .
echo "✅ Acceso concedido (200 OK)"
echo ""

# 3. Sin token
echo "3️⃣  Accediendo a /clients SIN token..."
curl -s -X GET http://localhost:8080/api/clients/1 | jq .
echo "❌ Acceso denegado (401)"
echo ""

# 4. Token inválido
echo "4️⃣  Accediendo con token inválido..."
curl -s -X GET http://localhost:8080/api/clients/1 \
  -H "Authorization: Bearer invalid.token" | jq .
echo "❌ Acceso denegado (401)"
echo ""

echo "🎉 Demo completada!"
```

---

## 📋 Checklist para Presentación

- [ ] App está corriendo (http://localhost:8080 accesible)
- [ ] Usuarios insertados en BD (5 usuarios con diferentes roles)
- [ ] Postman abierto con colección importada
- [ ] Terminal lista para mostrar logs (si lo pide)
- [ ] jwt.io abierto en pestaña para decodificar
- [ ] Código fuente abierto en IDE para mostrar si lo pide
- [ ] Notas preparadas (roles, endpoints, flujo)

---

**¡Listo para impresionar a tu profe!** 🚀
