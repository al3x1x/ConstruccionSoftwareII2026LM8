# 🍃 Guía de Instalación y Configuración de MongoDB (Local Windows)

## 📋 Requisitos Previos

- Java 17+
- Spring Boot 4.0.3+
- Maven 3.8+
- MongoDB Community 7.0+ (local en Windows)

---

## 🚀 Instalación de MongoDB Local en Windows

### Paso 1: Descargar MongoDB

1. Ve a: https://www.mongodb.com/try/download/community
2. Selecciona:
   - **Version:** 7.0 o superior
   - **OS:** Windows
   - **Package:** MSI
3. Descarga el instalador

### Paso 2: Instalar MongoDB

1. Ejecuta el instalador `.msi`
2. Acepta los términos de licencia
3. Elige **"Complete" (Instalación completa)**
4. En "Service Configuration" selecciona:
   - ☑️ **"Install MongoDB as a Service"**
   - **Service Name:** `MongoDB`
   - ☑️ **"Run the service as Network Service user"**
5. Completa la instalación

**Ubicación por defecto:** `C:\Program Files\MongoDB\Server\7.0\`

### Paso 3: Verificar Instalación

Abre **PowerShell** o **CMD** y ejecuta:

```bash
mongod --version
```

Deberías ver algo como:
```
db version v7.0.x
Build Info: ...
```

### Paso 4: Iniciar MongoDB

#### Opción A: Como Servicio (Recomendado - Automático)

MongoDB ya está configurado como servicio de Windows. Verifica que esté corriendo:

```bash
# Abrir Services.msc
services.msc

# Busca "MongoDB" y verifica que esté "Started"
```

O desde PowerShell:

```bash
Get-Service MongoDB | Start-Service
```

#### Opción B: Iniciar Manualmente desde Terminal

```bash
mongod
```

Deberías ver en la terminal:
```
[initandlisten] waiting for connections on port 27017
```

### Paso 5: Conectar y Verificar

En otra terminal, ejecuta:

```bash
mongosh
```

Deberías ver:
```
test>
```

Prueba un comando:

```bash
db.adminCommand('ping')
```

Resultado esperado:
```json
{ ok: 1 }
```

---

## ⚙️ Configuración en application.yml (LOCAL)

El archivo `application.yml` está configurado para MongoDB local por defecto:

```yaml
spring:
  data:
    mongodb:
      uri: ${MONGO_URI:mongodb://localhost:27017/bank_db}
```

### ✅ Esta configuración ya funciona con MongoDB local

**No necesitas cambiar nada** si MongoDB está corriendo localmente en `localhost:27017`

### Alternativa: Usar propiedades individuales

En lugar de URI, puedes usar:

```yaml
spring:
  data:
    mongodb:
      host: localhost
      port: 27017
      database: bank_db
      # Descomenta si MongoDB tiene autenticación:
      # username: admin
      # password: admin_password
      # authentication-database: admin
```

### Variable de Entorno

También puedes sobrescribir desde la terminal:

```bash
# PowerShell
$env:MONGO_URI="mongodb://localhost:27017/bank_db"

# CMD
set MONGO_URI=mongodb://localhost:27017/bank_db

# Linux/Mac
export MONGO_URI=mongodb://localhost:27017/bank_db
```

---

## 🔄 Seleccionar Adaptador (SQL Server vs MongoDB)

### Ambos adaptadores están disponibles:

- **SqlServer*** → Datos transaccionales (cuentas, préstamos)
- **MongoDb*** → Logs de auditoría, datos flexibles

### Para usar SOLO MongoDB:

1. Comenta la configuración de SQL Server en `application.yml`:

```yaml
# spring:
#   datasource:
#     url: jdbc:sqlserver://localhost:1433;databaseName=BankDB
#     username: sa
#     password: YourPassword123!
#   jpa:
#     hibernate:
#       ddl-auto: update
```

2. Descomenta solo MongoDB:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/bank_db
```

3. Spring Boot inyectará automáticamente los adaptadores MongoDB.

### Para usar AMBAS en paralelo:

Ambas están disponibles simultáneamente. Específica cuál usar:

```java
@Service
public class UserService {
    // Inyecta automáticamente MongoDbUserRepository
    private final UserRepository userRepository;
    
    // También puedes especificar explícitamente
    @Qualifier("mongoDbUserRepository")
    private final UserRepository mongoDbRepo;
    
    @Qualifier("sqlServerUserRepository")
    private final UserRepository sqlRepo;
}
```

---

## 🧪 Crear Base de Datos y Colecciones (Opcional)

MongoDB crea automáticamente las colecciones cuando insertas documentos. Pero puedes crear índices manualmente:

### Desde mongosh (local):

```bash
# 1. Abre mongosh
mongosh

# 2. Cambiar a la base de datos bank_db (se crea si no existe)
use bank_db

# 3. Crear colecciones
db.createCollection("users")
db.createCollection("bank_accounts")
db.createCollection("loans")
db.createCollection("transfers")
db.createCollection("audit_logs")

# 4. Crear índices para mejor performance
db.users.createIndex({ "email": 1 }, { unique: true })
db.users.createIndex({ "role": 1 })

db.bank_accounts.createIndex({ "holderId": 1 })
db.bank_accounts.createIndex({ "status": 1 })

db.loans.createIndex({ "clientId": 1 })
db.loans.createIndex({ "status": 1 })

db.transfers.createIndex({ "originAccount": 1 })
db.transfers.createIndex({ "destinationAccount": 1 })
db.transfers.createIndex({ "status": 1 })

db.audit_logs.createIndex({ "userId": 1 })
db.audit_logs.createIndex({ "operationType": 1 })
db.audit_logs.createIndex({ "timestamp": -1 })

# 5. Verificar
show collections

# 6. Ver documentos (vacío al inicio)
db.users.find()
```

### ⚠️ IMPORTANTE
**No es necesario** crear las colecciones manualmente. Spring Data MongoDB las crea automáticamente cuando insertas el primer documento.

---

## ✅ Verificar Conexión en Spring Boot

### 1. Ejecutar la aplicación

```bash
cd ConstruccionSoftwareII2026LM8/cs2
mvn spring-boot:run
```

### 2. Logs esperados

En la consola deberías ver:

```
Attempting to connect to MongoDB cluster
connected to database
connection pool created
```

### 3. Si hay errores de conexión

Verifica que MongoDB esté corriendo:

```bash
mongosh
```

Si ves error de conexión, inicia MongoDB:

```bash
# PowerShell
Get-Service MongoDB | Start-Service

# O manualmente
mongod
```

### 4. Probar endpoints

Usa **Postman** o **curl**:

```bash
# Crear usuario
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "id": "user-001",
  "name": "Juan Pérez",
  "email": "juan@bank.com",
  "phone": "+34123456789",
  "role": "CUSTOMER"
}
```

**Respuesta esperada:** `200 OK`

---

## 📊 Monitorear MongoDB Local

### Con mongosh:

```bash
# Conectar
mongosh

# Ver estado
db.serverStatus()

# Ver colecciones y documentos
db.bank_db.getCollectionStats("users")
db.users.countDocuments()

# Ver bases de datos
show dbs

# Ver colecciones actuales
show collections

# Ver documentos insertados
db.users.find().pretty()
db.bank_accounts.find().pretty()
```

### Con MongoDB Compass (Interfaz Gráfica)

1. Descarga desde: https://www.mongodb.com/try/download/compass
2. Abre Compass
3. Conecta a: `mongodb://localhost:27017`
4. Explora visualmente tu BD

---

## 🔌 Troubleshooting

### ❌ Error: "connect ECONNREFUSED 127.0.0.1:27017"

MongoDB no está corriendo:

```bash
# Opción 1: Iniciar como servicio
Get-Service MongoDB | Start-Service

# Opción 2: Iniciar manualmente desde CMD/PowerShell
mongod

# Opción 3: Verificar que esté instalado
services.msc  # Busca "MongoDB"
```

### ❌ Error: "Permission denied" o "authorization failed"

MongoDB requiere credenciales. Edita `application.yml`:

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://admin:password@localhost:27017/bank_db?authSource=admin
```

Si no tienes usuario creado:

```bash
mongosh

# Sin autenticación (por defecto)
use admin

# Crear usuario (si es necesario)
db.createUser({
  user: "admin",
  pwd: "password",
  roles: ["root"]
})
```

### ❌ Error: "Failed to create IndexOptions"

Actualiza Spring Data MongoDB en `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>
```

Luego:

```bash
mvn clean install
```

### ❌ Error: "no suitable servers found"

Verifica que MongoDB está en `localhost:27017`:

```bash
mongosh --eval "db.adminCommand('ping')"
```

Deberías ver:
```json
{ ok: 1 }
```

### ❌ Error: "The service cannot be started, either because it is disabled or because it has no enabled devices"

Reinicia MongoDB:

```bash
# PowerShell (como administrador)
Stop-Service MongoDB
Start-Service MongoDB
```

### ❌ Error: "Connection refused" en Spring Boot

1. Verifica que MongoDB está corriendo: `mongosh`
2. Revisa la URL en `application.yml`
3. Reinicia Spring Boot
4. Revisa logs buscando "MongoDB"

---

## 📝 Estructura Final

```
application/adapter/persistence/
├── mongodb/
│   ├── documents/
│   │   ├── UserDocument.java
│   │   ├── BankAccountDocument.java
│   │   ├── LoanDocument.java
│   │   ├── TransferDocument.java
│   │   └── AuditLogDocument.java
│   ├── repositories/
│   │   ├── UserMongoRepository.java
│   │   ├── BankAccountMongoRepository.java
│   │   ├── LoanMongoRepository.java
│   │   ├── TransferMongoRepository.java
│   │   └── AuditLogMongoRepository.java
│   ├── MongoDbUserRepository.java
│   ├── MongoDbBankAccountRepository.java
│   ├── MongoDbLoanRepository.java
│   ├── MongoDbTransferRepository.java
│   └── MongoDbAuditLogRepository.java
└── sqlserver/
    └── [existe ya]
```

---

## 🎯 Próximos Pasos

1. ✅ Instala MongoDB (local o Docker)
2. ✅ Ejecuta: `mvn clean install`
3. ✅ Ejecuta: `mvn spring-boot:run`
4. ✅ Verifica que conecte sin errores
5. ✅ Prueba un endpoint POST para crear datos

---

## 📚 Referencias

- [MongoDB Docs](https://docs.mongodb.com/)
- [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb)
- [Docker MongoDB](https://hub.docker.com/_/mongo)
