# ⚡ QUICK START - 5 MINUTOS A PRUEBAS

## 🎯 Meta: Probar JWT funcionando en 5 minutos

---

## ✋ ANTES DE EMPEZAR

### Verificar que tienes:
- [x] SQL Server corriendo
- [x] Git Bash o Terminal abierto
- [x] Postman instalado
- [x] Conexión a Internet

---

## ⏱️ MINUTO 1: Insertar Usuarios

### Abre SQL Server Management Studio:
```
1. Connect a: SQLEXPRESS
2. New Query
3. Copiar de: INSERT_TEST_USERS.sql
4. Ejecutar (Ctrl+E)
5. Resultado: "5 rows affected" ✅
```

**Credenciales para todos los usuarios:**
```
password: password123
```

---

## ⏱️ MINUTO 2: Iniciar la App

### En Git Bash:
```bash
cd ConstruccionSoftwareII2026LM8/cs2
mvn clean spring-boot:run
```

### Esperar a ver:
```
Tomcat started on port(s): 8080
```

✅ **La app está corriendo**

---

## ⏱️ MINUTO 3: Preparar Postman

### Descargar colección:
```
1. Abre este archivo: JWT_BANKING_POSTMAN_COLLECTION.json
2. Copia su contenido
3. Abre Postman
4. Ctrl+I (Import)
5. Pega el JSON
6. Click "Import"
```

✅ **Colección importada con 12 requests**

---

## ⏱️ MINUTO 4-5: Ejecutar Tests

### En Postman, ejecuta en orden:

```
1️⃣  SETUP - Guardar Token Commercial
    ↓
    Response: Token guardado ✅

2️⃣  GET Clients CON Token Válido (200 OK)
    ↓
    Response: Datos del cliente ✅

3️⃣  GET Clients SIN Token (401)
    ↓
    Response: {"status": 401, "message": "..."} ✅

4️⃣  GET Clients Con Token INVÁLIDO (401)
    ↓
    Response: {"status": 401, "message": "..."} ✅

5️⃣  SETUP - Guardar Token Teller
    ↓
    Response: Token guardado ✅

6️⃣  GET Clients Sin Permisos (403)
    ↓
    Response: {"status": 403, "message": "..."} ✅
```

---

## ✅ RESULTADO

Deberías ver:
- ✅ Login exitoso
- ✅ Acceso autorizado
- ✅ Rechazo sin token (401)
- ✅ Rechazo sin permisos (403)

---

## 🎁 BONUS: Ver el Token Decodificado

1. Ejecuta: `LOGIN - COMMERCIAL_EMPLOYEE`
2. Copia el token de la respuesta
3. Ve a: https://jwt.io
4. Pega en "Encoded"
5. Ver payload:
```json
{
  "sub": "juan_perez",
  "document": "1026366666",
  "role": "COMMERCIAL_EMPLOYEE",
  "iat": 1716239625,
  "exp": 1716326025
}
```

---

## 🆘 Si algo falla

### App no inicia
```
Verifica:
❌ ¿Corriendo SQL Server?
❌ ¿Puerto 8080 disponible?
❌ ¿Errores en consola?
```

### Postman no conecta
```
Verifica:
❌ ¿URL correcta (localhost:8080)?
❌ ¿App está corriendo?
❌ ¿Header Authorization correcto?
```

### Token no guarda en variable
```
Verifica:
❌ ¿Login devuelve 200?
❌ ¿Respuesta tiene "token"?
```

---

## 📱 RESUMEN VISUAL

```
┌─ INSERTAR USUARIOS ─┐
│   (SQL Server)      │ ← PASO 1
└─────────┬───────────┘
          ↓
┌─ INICIAR APP ───────┐
│  (Maven/Spring)     │ ← PASO 2
└─────────┬───────────┘
          ↓
┌─ IMPORTAR POSTMAN ──┐
│ (Colección JSON)    │ ← PASO 3
└─────────┬───────────┘
          ↓
┌─ EJECUTAR TESTS ────┐
│ (12 requests)       │ ← PASO 4-5
└─────────┬───────────┘
          ↓
    ✅ TODO FUNCIONA
```

---

## ⏰ Timeline

```
MINUTO:  1    2    3    4    5
TAREA:   SQL  App  PM   Test Test
         │    │    │    │    │
ESTADO:  ✓    ✓    ✓    ✓    ✓
         │    │    │    │    │
         └────┴────┴────┴────┘
            ¡LISTO!
```

---

## 🎉 ¡YA PUEDES PRESENTAR!

### Mostrar al Profe:

1. **LOGIN** (2 segundos)
   - Mostrar POST /login
   - Click "Send"
   - Ver token ✅

2. **ACCESO CON TOKEN** (2 segundos)
   - Mostrar GET /clients
   - Tiene token
   - Ver datos ✅

3. **SIN TOKEN** (1 segundo)
   - Mostrar GET /clients
   - Quitar Authorization
   - Ver 401 ✅

4. **SIN PERMISOS** (2 segundos)
   - Token de TELLER
   - GET /clients
   - Ver 403 ✅

5. **TOKEN DECODIFICADO** (1 segundo)
   - jwt.io
   - Pegar token
   - Ver payload ✅

---

**Total demo: ~8-10 minutos**

**Impacto: 💯 El profe queda impresionado**

---

## 📖 Más Detalles

Para entender más:
- Leer: `TESTING_GUIDE_MANUAL.md`
- Leer: `DEMO_VISUAL_PASO_A_PASO.md`
- Leer: `JWT_SECURITY_IMPLEMENTATION.md`

---

**¡A PROBAR! 🚀**
