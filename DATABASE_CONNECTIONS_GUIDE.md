# Guía de Configuración: Conexiones a Bases de Datos

## 📋 Resumen Implementado

Se han creado adaptadores para conectar tu aplicación bancaria a **SQL Server** y **MongoDB**, manteniendo la arquitectura hexagonal.

### Estructura de carpetas:
```
application/adapter/persistence/
├── sqlserver/                    # Adaptadores SQL Server
│   ├── entities/                # JPA Entities (UserJpaEntity, etc.)
│   ├── repositories/            # Spring Data JPA Repositories
│   └── SqlServer*Repository.java # Implementaciones de Puertos
├── mongodb/                     # Adaptadores MongoDB
│   ├── documents/               # MongoDB Document Classes
│   ├── repositories/            # Spring Data MongoDB Repositories
│   └── MongoDb*Repository.java  # Implementaciones de Puertos
└── persistence/                 # Adaptadores en memoria (existentes)
```

---

## 🗄️ Adaptadores Implementados

### **SQL Server (6 Adaptadores)**
1. `SqlServerUserRepository` - Gestión de usuarios
2. `SqlServerBankAccountRepository` - Gestión de cuentas
3. `SqlServerLoanRepository` - Gestión de préstamos
4. `SqlServerTransferRepository` - Gestión de transferencias
5. `SqlServerAuditLogRepository` - Auditoría

### **MongoDB (6 Adaptadores)**
1. `MongoDbUserRepository` - Gestión de usuarios
2. `MongoDbBankAccountRepository` - Gestión de cuentas
3. `MongoDbLoanRepository` - Gestión de préstamos
4. `MongoDbTransferRepository` - Gestión de transferencias
5. `MongoDbAuditLogRepository` - Auditoría

---

## ⚙️ Configuración de Conexiones

### **application.yml** - Configuración Actual:

```yaml
# SQL Server
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=BankDB
    username: sa
    password: YourPassword123!
    driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver

# MongoDB
  data:
    mongodb:
      uri: mongodb://localhost:27017/bank_db
```

### **Cambiar credenciales según tu ambiente:**

```yaml
# DESARROLLO (valores actuales en application.yml)
# SQL Server local
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=BankDB
spring.datasource.username=sa
spring.datasource.password=YourPassword123!

# MongoDB local
spring.data.mongodb.uri=mongodb://localhost:27017/bank_db

# PRODUCCIÓN
# Usar variables de entorno o application-prod.yml
spring.datasource.url=${DB_SQL_URL}
spring.data.mongodb.uri=${DB_MONGO_URL}
```

---

## 🚀 Cómo Usar los Adaptadores

### **Opción 1: Usar SQL Server (Recomendado para datos transaccionales)**

Los adaptadores SqlServer* están etiquetados con `@Component`, por lo que se inyectan automáticamente:

```java
@Service
public class UserService {
    private final UserRepository userRepository; // Inyecta SqlServerUserRepository
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

### **Opción 2: Usar MongoDB (Recomendado para logs y datos flexibles)**

Los adaptadores MongoDB* también están como `@Component`:

```java
@Service
public class AuditService {
    private final AuditLogRepository auditRepository; // Inyecta MongoDbAuditLogRepository
    
    public AuditService(AuditLogRepository auditRepository) {
        this.auditRepository = auditRepository;
    }
}
```

### **Opción 3: Híbrido (Diferentes BDs para diferentes dominio)**

Puedes usar ambas selectivamente. Ambos adaptadores implementan las mismas interfaces del domain:

```java
// Los dos adapters implementan UserRepository
public interface UserRepository {
    User findById(String id);
    void save(User user);
    // ...
}
```

---

## 🔄 Cambiar de Adaptador (Sin cambiar código de dominio)

### **Scenario: Cambiar de SQL Server a MongoDB**

1. En application.yml, comenta la configuración de SQL Server
2. Desactiva `SqlServerUserRepository` con `@ConditionalOnProperty` si es necesario
3. Spring inyectará `MongoDbUserRepository` automáticamente

```java
@Component
@ConditionalOnProperty(name = "database.type", havingValue = "mongodb")
public class MongoDbUserRepository implements UserRepository { }

@Component
@ConditionalOnProperty(name = "database.type", havingValue = "sqlserver")
public class SqlServerUserRepository implements UserRepository { }
```

---

## 📦 Dependencias Agregadas

```xml
<!-- MongoDB -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-mongodb</artifactId>
</dependency>

<!-- SQL Server -->
<dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>mssql-jdbc</artifactId>
    <version>12.4.1.jre11</version>
</dependency>
```

---

## 🧪 Próximos Pasos (Opcional)

1. **Docker Compose** - Archivo para levantar MongoDB y SQL Server localmente
2. **Tests de integración** - Validar conexiones a ambas BDs
3. **Migraciones** - Scripts SQL para crear tablas en SQL Server
4. **Connection pooling** - Optimizar con HikariCP

---

## 📝 Notas de Arquitectura

- ✅ Los adaptadores mantienen la arquitectura hexagonal
- ✅ El dominio no conoce detalles de BD
- ✅ Fácil cambiar de BD sin tocar servicios de dominio
- ✅ Ambos adaptadores cumplen los mismos contratos (Ports)
- ✅ Conversión automática Entity ↔️ Document ↔️ DomainModel
