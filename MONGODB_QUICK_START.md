# ⚡ Quick Start - MongoDB Local

## 🚀 Iniciar en 3 pasos

### 1️⃣ Verificar que MongoDB está corriendo

```bash
mongosh
```

Si ves error "connect ECONNREFUSED", inicia MongoDB:

**PowerShell (como administrador):**
```bash
Get-Service MongoDB | Start-Service
```

**O manualmente:**
```bash
mongod
```

Vuelve a intentar `mongosh`. Si ves `test>` ¡está listo!

### 2️⃣ Compilar y ejecutar la aplicación

```bash
cd ConstruccionSoftwareII2026LM8/cs2
mvn clean install
mvn spring-boot:run
```

Busca en los logs:
```
Attempting to connect to MongoDB cluster
connection pool created
```

### 3️⃣ Probar que funciona

**PowerShell/CMD:**
```bash
curl -X POST http://localhost:8080/api/users `
  -H "Content-Type: application/json" `
  -d '{
    "id": "user-001",
    "name": "Juan",
    "email": "juan@bank.com",
    "phone": "+34123456789",
    "role": "CUSTOMER"
  }'
```

**Respuesta esperada:**
```json
200 OK
```

---

## 🧪 Verificar Datos en MongoDB

### Ver usuarios creados:

```bash
mongosh
use bank_db
db.users.find().pretty()
```

Deberías ver tu usuario.

---

## 📁 Adaptadores Disponibles

| Adaptador | Ubicación |
|-----------|-----------|
| `MongoDbUserRepository` | `mongodb/MongoDbUserRepository.java` |
| `MongoDbBankAccountRepository` | `mongodb/MongoDbBankAccountRepository.java` |
| `MongoDbLoanRepository` | `mongodb/MongoDbLoanRepository.java` |
| `MongoDbTransferRepository` | `mongodb/MongoDbTransferRepository.java` |
| `MongoDbAuditLogRepository` | `mongodb/MongoDbAuditLogRepository.java` |

---

## 📚 Documentación

- 📖 **MONGODB_SETUP_GUIDE.md** - Guía completa
- 📋 **MONGODB_IMPLEMENTATION_SUMMARY.md** - Resumen
- 🔧 **DATABASE_CONNECTIONS_GUIDE.md** - Cambiar BD

---

## 🎁 Interfaz Gráfica (Opcional)

Descarga **MongoDB Compass** para ver los datos visualmente:

https://www.mongodb.com/try/download/compass

Conecta a: `mongodb://localhost:27017`

---

**¡Listo! MongoDB local está funcionando.** ✅
