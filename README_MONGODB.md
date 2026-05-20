# 🍃 MongoDB Local - Configuración Completa

## 📌 RESUMEN

Se han creado **18 archivos Java** para conectar MongoDB localmente a tu aplicación bancaria. **MongoDB funciona local sin Docker.**

---

## ⚡ Inicio Rápido (3 pasos)

### 1. Verifica que MongoDB está corriendo

```bash
mongosh
```

Si ves `test>` ✅ sigue al paso 2.

Si ves error, ejecuta:
```bash
mongod
```

### 2. Ejecuta la app

```bash
cd ConstruccionSoftwareII2026LM8\cs2
mvn clean install
mvn spring-boot:run
```

### 3. Prueba

```bash
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "id": "user-001",
  "name": "Juan",
  "email": "juan@bank.com",
  "phone": "+34123456789",
  "role": "CUSTOMER"
}
```

**¡Listo!** ✅

---

## 📂 Archivos Creados

### Java Adaptadores (15 archivos)
```
app/application/adapter/persistence/mongodb/
├── documents/          (5 documentos)
├── repositories/       (5 repositorios)
└── adaptadores         (5 implementaciones)
```

### Documentación (5 archivos)
- `INSTRUCCIONES_MONGODB_LOCAL.md` ⭐ **LEER ESTO PRIMERO**
- `MONGODB_QUICK_START.md` - Quick reference
- `MONGODB_SETUP_GUIDE.md` - Guía técnica
- `MONGODB_IMPLEMENTATION_SUMMARY.md` - Detalles
- `README_MONGODB.md` - Este archivo

---

## 🎯 Archivos Importantes

| Archivo | Qué Hace |
|---------|----------|
| `INSTRUCCIONES_MONGODB_LOCAL.md` | **GUÍA PASO A PASO** |
| `application.yml` | Config lista para MongoDB local |
| `docker-compose.yml` | Opcional (no necesario) |

---

## ✅ Verificación

### MongoDB está corriendo:
```bash
mongosh
db.adminCommand('ping')
```

Deberías ver: `{ ok: 1 }`

### Spring Boot conecta:
```
Logs mostrarán:
"Attempting to connect to MongoDB cluster"
"connection pool created"
```

### Datos se guardan:
```bash
mongosh
use bank_db
db.users.find()
```

---

## 🔧 Configuración

**application.yml ya está configurado:**
```yaml
spring.data.mongodb.uri=mongodb://localhost:27017/bank_db
```

**No necesitas cambiar nada.** Solo asegúrate de que MongoDB está corriendo.

---

## ❌ Problemas Comunes

### "connect ECONNREFUSED"
→ MongoDB no está corriendo. Ejecuta: `mongod`

### "Command not found: mongosh"
→ MongoDB no está en PATH. Reinstala MongoDB.

### "Failed to create IndexOptions"
→ `mvn clean install`

### Connection Refused en Spring Boot
1. Verifica: `mongosh` funciona
2. Revisa: `application.yml` 
3. Reinicia Spring Boot

---

## 📊 Adaptadores Creados

✅ `MongoDbUserRepository` - Gestión de usuarios
✅ `MongoDbBankAccountRepository` - Gestión de cuentas
✅ `MongoDbLoanRepository` - Gestión de préstamos
✅ `MongoDbTransferRepository` - Gestión de transferencias
✅ `MongoDbAuditLogRepository` - Auditoría

---

## 🎁 Bonificaciones

- ✅ Arquitectura hexagonal mantenida
- ✅ Ambos adaptadores (SQL Server + MongoDB) disponibles
- ✅ Índices automáticos en colecciones
- ✅ Totalmente documentado

---

## 📚 Documentación Completa

1. **INSTRUCCIONES_MONGODB_LOCAL.md** ← EMPIEZA AQUÍ
2. MONGODB_QUICK_START.md
3. MONGODB_SETUP_GUIDE.md
4. MONGODB_IMPLEMENTATION_SUMMARY.md

---

## 🚀 Próximos Pasos

```bash
# 1. Leer INSTRUCCIONES_MONGODB_LOCAL.md
# 2. Instalar MongoDB (si no lo tienes)
# 3. Ejecutar: mongod
# 4. Ejecutar: mvn spring-boot:run
# 5. Probar endpoints
```

---

**✅ Todo listo. MongoDB local funciona sin Docker.**

¿Preguntas? Revisa `INSTRUCCIONES_MONGODB_LOCAL.md`
