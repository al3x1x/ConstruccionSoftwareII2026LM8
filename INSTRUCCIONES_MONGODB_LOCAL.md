.\mvnw.bat clean install# 🍃 Instrucciones: MongoDB Local en Windows

## ✅ Requisitos Previos Checklist

- [ ] MongoDB Community 7.0+ instalado en Windows
- [ ] Java 17+ instalado
- [ ] Maven 3.8+ instalado
- [ ] Git

---

## 📥 Paso 1: Instalar MongoDB (Si no lo tienes)

### Descargar e instalar

1. Ve a: https://www.mongodb.com/try/download/community
2. Elige:
   - **Version:** 7.0 (o superior)
   - **OS:** Windows
   - **Package:** MSI
3. Ejecuta el instalador `.msi`
4. Selecciona: **"Install MongoDB as a Service"** ✓
5. Completa la instalación

### Verificar instalación

```bash
mongod --version
```

Deberías ver un número de versión.

---

## 🚀 Paso 2: Iniciar MongoDB

### Opción A: Como Servicio (Recomendado)

**PowerShell (como Administrador):**
```bash
Get-Service MongoDB | Start-Service
```

**Verificar que está corriendo:**
```bash
Get-Service MongoDB
```

Deberías ver: `Running`

### Opción B: Iniciar Manualmente

Abre **PowerShell** o **CMD** y ejecuta:

```bash
mongod
```

Deberías ver:
```
[initandlisten] waiting for connections on port 27017
```

---

## 🧪 Paso 3: Verificar Conexión a MongoDB

En otra terminal:

```bash
mongosh
```

Si ves:
```
test>
```

✅ **MongoDB está funcionando correctamente**

Salir de mongosh:
```bash
exit
```

---

## 🔧 Paso 4: Ejecutar la Aplicación

### Abrir PowerShell o CMD

```bash
cd C:\Users\[TuUsuario]\Desktop\bank
cd ConstruccionSoftwareII2026LM8\cs2
```

### Compilar

```bash
mvn clean install
```

Espera a que termine (2-5 minutos la primera vez).

### Ejecutar

```bash
mvn spring-boot:run
```

### Verificar en los logs

Busca algo como:
```
Attempting to connect to MongoDB cluster
connection pool created
Spring started on port 8080
```

Si ves esto ✅ **¡Funcionando!**

---

## 🧪 Paso 5: Probar que Funciona

### Crear un usuario

Abre una nueva terminal y ejecuta:

**PowerShell:**
```bash
$body = @{
    id = "user-001"
    name = "Juan Perez"
    email = "juan@bank.com"
    phone = "+34123456789"
    role = "CUSTOMER"
} | ConvertTo-Json

Invoke-WebRequest -Uri "http://localhost:8080/api/users" `
  -Method POST `
  -Headers @{"Content-Type"="application/json"} `
  -Body $body
```

**CMD (usando curl):**
```bash
curl -X POST http://localhost:8080/api/users ^
  -H "Content-Type: application/json" ^
  -d "{\"id\":\"user-001\",\"name\":\"Juan\",\"email\":\"juan@bank.com\",\"phone\":\"+34123456789\",\"role\":\"CUSTOMER\"}"
```

**Si ves respuesta 200** ✅ **¡Todo funciona!**

---

## 📊 Ver Datos en MongoDB

### Con mongosh

```bash
mongosh

# Cambiar a la base de datos
use bank_db

# Ver usuarios
db.users.find().pretty()

# Ver cuentas
db.bank_accounts.find().pretty()

# Ver todas las colecciones
show collections
```

### Con MongoDB Compass (GUI)

1. Descarga desde: https://www.mongodb.com/try/download/compass
2. Abre Compass
3. Conecta a `mongodb://localhost:27017`
4. Explora visualmente

---

## 🛑 Detener la Aplicación

En la terminal donde corre Spring Boot:
```bash
Ctrl + C
```

En PowerShell (opcional, mantiene MongoDB para próxima vez):
```bash
Get-Service MongoDB | Stop-Service
```

---

## ✅ Checklist Final

- [ ] MongoDB instalado y corriendo
- [ ] `mongosh` se conecta correctamente
- [ ] App Spring Boot compila sin errores
- [ ] Logs muestran conexión a MongoDB
- [ ] POST a `/api/users` retorna 200 OK
- [ ] `db.users.find()` muestra datos insertados

---

## 🔴 Si Algo Falla

### "connect ECONNREFUSED 127.0.0.1:27017"

MongoDB no está corriendo:

```bash
# Inicia MongoDB
mongod

# O como servicio
Get-Service MongoDB | Start-Service
```

### "Connection refused" en Spring Boot

```bash
# Verifica que MongoDB esté corriendo
mongosh

# Si ves error, inicia MongoDB:
mongod
```

### "mvn command not found"

Maven no está en PATH. Instala Maven o usa el wrapper:

```bash
# Usar Maven wrapper que viene con el proyecto
./mvnw spring-boot:run
```

### Los logs dicen "Failed to connect"

1. Verifica: `mongosh` funciona
2. Revisa `application.yml` está correcto
3. Reinicia Spring Boot

---

## 📝 Resumen de Adaptadores Creados

```
✅ MongoDbUserRepository
✅ MongoDbBankAccountRepository
✅ MongoDbLoanRepository
✅ MongoDbTransferRepository
✅ MongoDbAuditLogRepository
```

Todos implementan los puertos del dominio. Spring Boot inyecta automáticamente.

---

## 📚 Archivos Relacionados

- **MONGODB_SETUP_GUIDE.md** - Guía técnica detallada
- **MONGODB_QUICK_START.md** - Quick reference
- **MONGODB_IMPLEMENTATION_SUMMARY.md** - Resumen de implementación
- **application.yml** - Configuración (ya está lista)

---

**✅ ¡Listo para empezar!**

Cualquier pregunta, revisa los logs o ejecuta: `mongosh`
