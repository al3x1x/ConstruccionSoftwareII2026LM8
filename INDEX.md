# 📚 ÍNDICE MAESTRO - Sistema de Seguridad JWT

Bienvenido. Aquí encontrarás todos los recursos para entender, implementar y probar el sistema de seguridad JWT.

---

## 🚀 COMIENZA AQUÍ

### Para Personas en Prisa (5 min)
1. Lee: **`RESUMEN_PARA_PROFE.md`** ← Comienza aquí
2. Sigue: **`TESTING_GUIDE_MANUAL.md`** - Paso 1 a Paso 3
3. Importa: **`JWT_BANKING_POSTMAN_COLLECTION.json`** en Postman
4. Ejecuta: Los primeros 3 tests

### Para Entender Completamente (30 min)
1. Lee: **`JWT_SECURITY_IMPLEMENTATION.md`** - Entiende el flujo
2. Lee: **`TESTING_GUIDE_MANUAL.md`** - Aprende a probar
3. Lee: **`DEMO_VISUAL_PASO_A_PASO.md`** - Ve ejemplos visuales
4. Lee: **`ARQUITECTURA_HEXAGONAL_COMPLETADA.md`** - Entiende la arquitectura

---

## 📖 DOCUMENTACIÓN TÉCNICA

### Implementación
📄 **`JWT_SECURITY_IMPLEMENTATION.md`**
- Flujo completo de autenticación
- Componentes implementados
- Dependencias agregadas
- Configuración de propiedades
- Uso en controladores

📄 **`ARQUITECTURA_HEXAGONAL_COMPLETADA.md`**
- Separación de capas
- Responsabilidades de cada capa
- Patrón Hexagonal/Limpia

### Casos de Uso
📄 **`CASOS_DE_USO_DETALLADOS.md`**
- Casos de uso del sistema
- Actores involucrados
- Flujos principales

---

## 🧪 RECURSOS DE TESTING

### Para Postman (RECOMENDADO)
📄 **`JWT_BANKING_POSTMAN_COLLECTION.json`** ⭐ **USAR ESTO**
- 14 requests pre-configurados
- Auto-guarda tokens
- Incluye setup automático
- Listo para importar

### Guías de Testing
📄 **`TESTING_GUIDE_MANUAL.md`** - Guía paso a paso completa
- Todos los pasos con screenshots
- Troubleshooting
- Checklist de pruebas

📄 **`DEMO_VISUAL_PASO_A_PASO.md`** - Guía visual con ejemplos
- Capturas de pantalla ASCII
- Demostración paso a paso
- Guión para presentar al profe

📄 **`RESUMEN_PARA_PROFE.md`** ⭐ **RESUMEN EJECUTIVO**
- Lo que el profe quiere ver
- Preguntas posibles
- Tiempo de demostración

### Para Base de Datos
📄 **`INSERT_TEST_USERS.sql`** - Usuarios de prueba
- 5 usuarios con diferentes roles
- Contraseña: password123
- Listos para SQL Server

### Para Terminal/Bash
📄 **`TEST_CURL_COMMANDS.sh`** - Comandos cURL
- 9 tests diferentes
- Listos para copiar/pegar
- Alternativa a Postman

📄 **`START_TESTING.sh`** - Script de inicio
- Verifica configuración
- Inicia la app
- Automatiza checks

---

## 💻 CÓDIGO IMPLEMENTADO

### Seguridad (Infraestructura)
```
ConstruccionSoftwareII2026LM8/cs2/src/main/java/app/infrastructure/security/

📄 JwtUtil.java
   - Generación de tokens JWT
   - Extracción segura de datos
   - Validación de tokens
   - Métodos: generateToken(), extractUsername(), extractRole()...

📄 JwtAuthenticationFilter.java
   - Filtro que intercepta requests
   - Procesa header Authorization
   - Valida tokens
   - Llena SecurityContextHolder

📄 SecurityConfig.java
   - Configuración centralizada
   - Lambdas Spring Security 6
   - Reglas de autorización por endpoint
   - Manejo de excepciones (401/403)
```

### Autenticación (Application)
```
ConstruccionSoftwareII2026LM8/cs2/src/main/java/app/application/adapter/

📁 api/controllers/
   📄 AuthController.java
      - Endpoint POST /api/auth/login
      - Valida credenciales
      - Genera JWT

📁 api/dto/
   📄 LoginDTO.java - Request de login
   📄 AuthResponseDTO.java - Response con token
```

### Configuración
```
📄 pom.xml - Agregada dependencia JJWT 0.12.3
📄 application.yml - Propiedades JWT
```

---

## 🎯 FLUJOS RÁPIDOS

### Flujo de Login
```
Usuario → POST /api/auth/login
       ↓
AuthController → Buscar usuario
       ↓
Validar contraseña (BCrypt)
       ↓
JwtUtil.generateToken()
       ↓
Retornar { token, username, role } (200)
```

### Flujo de Request Protegido
```
Cliente → GET /api/clients/1
       ├─ Header: Authorization: Bearer <token>
       ↓
JwtAuthenticationFilter
       ├─ Valida token
       ├─ Extrae username, role, document
       ├─ Llena SecurityContextHolder
       ↓
SecurityConfig
       ├─ Verifica roles
       ├─ Autoriza según endpoint
       ↓
Controller → Retorna 200 o 403
```

---

## ✅ CHECKLIST PARA PROFE

- [ ] Usuarios insertados en BD
- [ ] App corriendo en localhost:8080
- [ ] Postman con colección importada
- [ ] Test 1: Login → Token obtenido (200)
- [ ] Test 2: GET con token → Acceso concedido (200)
- [ ] Test 3: GET sin token → Rechazado (401)
- [ ] Test 4: GET token inválido → Rechazado (401)
- [ ] Test 5: GET sin permisos → Rechazado (403)
- [ ] Token decodificado en jwt.io
- [ ] Mostrar código (3 clases)
- [ ] Explicar Arquitectura Limpia

---

## 📞 PREGUNTAS FRECUENTES

### ¿Por dónde empiezo?
1. Lee: `RESUMEN_PARA_PROFE.md`
2. Sigue: `TESTING_GUIDE_MANUAL.md` (Pasos 1-3)
3. Usa: `JWT_BANKING_POSTMAN_COLLECTION.json`

### ¿Cómo inserto los usuarios?
Ejecuta `INSERT_TEST_USERS.sql` en SQL Server Management Studio.

### ¿Cuál es la contraseña de los usuarios?
`password123` (hasheada en la BD)

### ¿Qué versión de JJWT usé?
0.12.3 (la más actual, no obsoleta)

### ¿Es seguro para producción?
No completamente. Recomendaciones en `RESUMEN_PARA_PROFE.md`.

### ¿Cómo se ve el token?
Es un string base64 con 3 partes: Header.Payload.Signature
Decodifica en: https://jwt.io

### ¿Qué roles puedo probar?
- COMMERCIAL_EMPLOYEE (acceso a clientes)
- INTERNAL_ANALYST (aprobación de préstamos)
- TELLER_EMPLOYEE (solo cuentas)
- COMPANY_SUPERVISOR (múltiples permisos)
- NATURAL_PERSON_CLIENT (cliente)

### ¿Cuánto tiempo toma la demo?
- Setup: 5 min
- Demo en vivo: 6 min
- Preguntas: 5 min
- Total: ~16 minutos

---

## 📊 ESTADÍSTICAS

| Métrica | Valor |
|---------|-------|
| Clases de Seguridad | 3 |
| Métodos en JwtUtil | 6 |
| Tests disponibles | 12+ |
| Roles soportados | 7 |
| Usuarios de prueba | 5 |
| Documentación | 10 archivos |
| Líneas de código | ~400 |

---

## 🏗️ ARQUITECTURA IMPLEMENTADA

```
┌─────────────────────────────────────────┐
│           HTTP Layer                    │
├─────────────────────────────────────────┤
│  Application Layer                      │
│  ├─ Controllers (Delgados)              │
│  ├─ DTOs (Login/Response)               │
│  └─ Use Cases (Lógica)                  │
├─────────────────────────────────────────┤
│  Infrastructure Layer                   │
│  ├─ JwtUtil (Generación/Validación)    │
│  ├─ JwtAuthenticationFilter             │
│  └─ SecurityConfig (Autorización)       │
├─────────────────────────────────────────┤
│  Domain Layer                           │
│  ├─ Models (User, UserRole)             │
│  ├─ Ports (Repositories)                │
│  └─ Exceptions                          │
├─────────────────────────────────────────┤
│  Persistence Layer                      │
│  ├─ SQL Server (JPA)                    │
│  └─ MongoDB                             │
└─────────────────────────────────────────┘
```

---

## 🔐 Seguridad Implementada

✅ **Autenticación JWT**
- Generación con firma HMAC-SHA256
- Claims personalizados (username, role, document)
- Expiración de tokens (24 horas)

✅ **Autorización por Roles**
- SecurityContext poblado
- Validación en cada request
- Respuestas 403 para permisos insuficientes

✅ **Manejo de Errores**
- 401: Token inválido o ausente
- 403: Sin permisos
- JSON estructurado

✅ **Arquitectura Limpia**
- Lógica de negocio sin dependencias HTTP/JWT
- Capas claramente separadas
- Testeable

---

## 📚 Documentos Relacionados

Otros documentos del proyecto:
- `README.md` - Descripción general
- `ESTRUCTURA_DEL_PROYECTO.md` - Estructura de carpetas
- `DATABASE_CONNECTIONS_GUIDE.md` - Conexiones a BD
- `INSTALACION_BD_WINDOWS.md` - Setup de BD
- `MONGODB_QUICK_START.md` - Setup de MongoDB

---

## 🎓 Para Aprender Más

### Sobre JWT
- https://jwt.io - Decodificar tokens
- https://tools.ietf.org/html/rfc7519 - Especificación RFC

### Sobre Spring Security
- https://spring.io/projects/spring-security - Documentación oficial
- Spring Security 6 con lambdas - Nuevas características

### Sobre Arquitectura Limpia
- Robert C. Martin - Clean Architecture
- Patrón Hexagonal

---

## 💡 Tips Finales

1. **Postman es tu amigo** - Más fácil que cURL para demo
2. **jwt.io es tu aliado** - Para ver qué hay dentro del token
3. **Los logs son clave** - Ver qué está pasando en la consola
4. **Cambiar rol fácilmente** - Actualizar en BD: `UPDATE [users] SET role = 'NUEVO_ROL'`
5. **Probar expiraciones** - Cambiar `expiration: 5000` en yml

---

## 🎉 ¡LISTO PARA EMPEZAR!

### Próximo Paso:
**Lee:** `RESUMEN_PARA_PROFE.md` (5 min)

**Luego ejecuta:**
1. Insertar usuarios (SQL)
2. Iniciar app (Maven)
3. Importar Postman
4. Ejecutar tests

---

**Última actualización:** 2024-05-20

**Versión:** 1.0 - Complete Implementation

**Estado:** ✅ Ready for Demo
