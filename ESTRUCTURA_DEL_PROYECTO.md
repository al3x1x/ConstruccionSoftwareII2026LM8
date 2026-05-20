# Sistema Bancario - Estructura del Proyecto
## Guía Rápida de Archivos

---

## 📁 ÁRBOL DE CARPETAS

```
app/
│
├─── domain/                          ← CAPA DOMINIO (Reglas de Negocio)
│    ├─ models/
│    │  ├─ User.java                  ← Clase abstracta
│    │  ├─ NaturalPersonClient.java   ← ✓ Con assignedCommercialEmployeeId
│    │  ├─ Transfer.java              ← ✓ Con lógica de expiración
│    │  ├─ Loan.java                  ← ✓ Con assignedCommercialEmployeeId
│    │  ├─ BankAccount.java           ← Manejo de balance
│    │  └─ CommercialEmployee.java    ← Asesor (gestiona clientes)
│    │
│    ├─ ports/                        ← Interfaces (contrato con infraestructura)
│    │  ├─ UserRepository.java        ← Define: findById(), save(), etc.
│    │  ├─ TransferRepository.java    ← Define: findByStatus(), update(), etc.
│    │  ├─ LoanRepository.java        ← Define: findByClientId(), update(), etc.
│    │  ├─ BankAccountRepository.java ← Define: findByAccountNumber(), etc.
│    │  └─ AuditLogRepository.java    ← Define: save(), findByUserId(), etc.
│    │
│    ├─ exceptions/
│    │  ├─ UnauthorizedClientAccessException.java  ← ✓ Cuando asesor no autorizado
│    │  ├─ InsufficientFundsException.java
│    │  └─ InvalidTransferException.java
│    │
│    ├─ enums/
│    │  ├─ UserRole.java              ← COMMERCIAL_EMPLOYEE, INTERNAL_ANALYST...
│    │  ├─ UserStatus.java            ← ACTIVE, INACTIVE
│    │  ├─ LoanStatus.java            ← UNDER_REVIEW, APPROVED, DISBURSED...
│    │  └─ TransferStatus.java        ← PENDING, AWAITING_APPROVAL, EXPIRED...
│    │
│    └─ services/commands/            ← DTOs del Dominio
│       ├─ CreateUserCommand.java
│       ├─ CreateBankAccountCommand.java
│       ├─ CreateTransferCommand.java
│       ├─ ApproveLoanCommand.java
│       └─ (otros commands...)
│
├─── application/                     ← CAPA APLICACIÓN (Orquestación)
│    │
│    ├─ usecases/                     ← ✓ NUEVOS (movidos de domain/services)
│    │  ├─ CreateUserUseCase.java
│    │  ├─ CreateBankAccountUseCase.java
│    │  ├─ CreateTransferUseCase.java
│    │  ├─ ApproveTransferUseCase.java
│    │  ├─ ApproveLoanUseCase.java
│    │  ├─ RejectLoanUseCase.java
│    │  ├─ DisburseLoanUseCase.java
│    │  ├─ ExecuteTransferUseCase.java
│    │  └─ NaturalClientUseCase.java  ← ✓ Con validación Asesor-Cliente
│    │
│    └─ adapter/
│        ├─ api/
│        │  ├─ controllers/           ← ✓ NUEVOS (REST endpoints)
│        │  │  ├─ ClientController.java         → GET /api/clients/{id}
│        │  │  ├─ LoanController.java           → POST /api/loans/{id}/approve
│        │  │  ├─ TransferController.java       → POST /api/transfers
│        │  │  ├─ AccountController.java        → POST /api/accounts
│        │  │  └─ GlobalExceptionHandler.java   → Manejo de errores
│        │  │
│        │  └─ dto/                  ← ✓ NUEVOS (traducen JSON)
│        │     ├─ CreateLoanRequestDTO.java
│        │     ├─ CreateTransferDTO.java
│        │     └─ CreateBankAccountDTO.java
│        │
│        └─ persistence/
│           ├─ InMemory*/             ← Para testing
│           │  ├─ InMemoryUserRepository.java
│           │  └─ (otros repos in-memory)
│           │
│           ├─ sqlserver/             ← SQL Server
│           │  ├─ entities/           ← ✓ NUEVOS
│           │  │  ├─ UserEntity.java          → @Entity (usuarios)
│           │  │  ├─ AuditLogEntity.java      → @Entity (auditoría)
│           │  │  ├─ BankAccountEntity.java
│           │  │  ├─ LoanEntity.java
│           │  │  └─ TransferEntity.java
│           │  │
│           │  ├─ repositories/       ← ✓ NUEVOS
│           │  │  ├─ UserJpaRepository.java            → Spring Data JPA
│           │  │  ├─ AuditLogJpaRepository.java
│           │  │  └─ (otros JPA repos)
│           │  │
│           │  └─ Adapters/           ← ✓ NUEVOS (implementan puertos)
│           │     ├─ SqlServerUserRepository.java      → @Component
│           │     ├─ SqlServerAuditLogRepository.java  → @Component
│           │     └─ (otros adapters)
│           │
│           └─ mongodb/               ← MongoDB
│              ├─ documents/          ← Equivalente a Entities
│              │  ├─ UserDocument.java        → @Document + assignedCommercialEmployeeId ✓
│              │  ├─ LoanDocument.java        → @Document + assignedCommercialEmployeeId ✓
│              │  ├─ TransferDocument.java    → @Document + assignedCommercialEmployeeId ✓
│              │  ├─ AuditLogDocument.java
│              │  └─ BankAccountDocument.java
│              │
│              ├─ repositories/       ← Spring Data MongoDB interfaces
│              │  ├─ UserMongoRepository.java
│              │  ├─ LoanMongoRepository.java
│              │  └─ (otros)
│              │
│              └─ Adapters/           ← (implementan puertos)
│                 ├─ MongoDbUserRepository.java
│                 ├─ MongoDbLoanRepository.java
│                 └─ (otros)
│
└─── infrastructure/                  ← CAPA INFRAESTRUCTURA (Config + BD)
     │
     ├─ persistence/
     │  └─ PersistenceConfig.java     ← ✓ NUEVO (dual-DB, separación)
     │
     ├─ scheduler/
     │  └─ TransferExpirationScheduler.java  ← ✓ NUEVO (cada 3 min)
     │
     ├─ config/
     │  └─ SchedulerConfig.java       ← ✓ NUEVO (@EnableScheduling)
     │
     └─ security/
        └─ SecurityConfig.java        ← ✓ NUEVO (roles por endpoint)
```

---

## 🔄 FLUJOS PRINCIPALES

### **Flujo 1: Crear Transferencia**
```
HTTP POST /api/transfers
    ↓
TransferController.createTransfer()
    ↓ (extrae auth)
CreateTransferUseCase.execute()
    ↓ (valida cuentas distintas)
new Transfer()  [DOMINIO]
    ↓ (calcula status: >10k? → AWAITING_APPROVAL)
transferRepository.save()
    ↓ (¿SQL o Mongo? ve PersistenceConfig)
SqlServerTransferRepository.toJpaEntity()
    ↓ (mapea Transfer → TransferEntity)
INSERT INTO transfers
    ↓
HTTP 200 { transferId, status: "AWAITING_APPROVAL", ... }
```

### **Flujo 2: Validación Asesor-Cliente**
```
NaturalClientUseCase.createLoanRequest("JUAN-001", "PEDRO-001", ...)
    ↓
validateEmployeeClientRelationship("JUAN-001", "PEDRO-001")
    ↓
userRepository.findById("PEDRO-001")  [desde SqlServer/Mongo]
    ↓
NaturalPersonClient {
  userId: "PEDRO-001",
  assignedCommercialEmployeeId: "MARIA-002"  ← Otro asesor
}
    ↓
if (!"JUAN-001".equals("MARIA-002")) {
  throw UnauthorizedClientAccessException ✗
}
    ↓
HTTP 403 { error: "No autorizado" }
```

### **Flujo 3: Expiración Automática (cada 3 min)**
```
TransferExpirationScheduler.checkAndMarkExpiredTransfers()
    ↓ (@Scheduled)
transferRepository.findByStatus(AWAITING_APPROVAL)
    ↓
for (Transfer transfer : transfers) {
    if (transfer.checkExpiration()) {  ← Usa lógica de DOMINIO
        transferRepository.update(transfer);
        auditLogRepository.save(log);
    }
}
    ↓
UPDATE transfers SET status='EXPIRED' WHERE ...
INSERT INTO audit_logs ...
    ↓
✓ BD consistente, log automático
```

---

## 📊 TABLA: RESPONSABILIDADES POR CAPA

| Pregunta | DOMINIO | APLICACIÓN | INFRAESTRUCTURA |
|----------|---------|-----------|-----------------|
| **¿Dónde viven las reglas?** | ✓ Transfer.java, Loan.java | DTOs, orquestación | BD, config |
| **¿Dónde valido Asesor-Cliente?** | ✓ En modelo | ✓ En Use Case | No |
| **¿Dónde calculo expiración?** | ✓ transfer.checkExpiration() | Llama | Ejecuta scheduler |
| **¿Dónde tengo puertos/interfaces?** | ✓ UserRepository.java | Implementa | SQLServerUserRepository |
| **¿Dónde traduzco JSON?** | No | ✓ DTOs | No |
| **¿Dónde tengo @Entity/@Document?** | No | No | ✓ UserEntity, UserDocument |
| **¿Dónde tengo SQL/Mongo?** | No | No | ✓ Repositorios |
| **¿Dónde manejo seguridad?** | No | ✓ Controladores | ✓ SecurityConfig |

---

## 🎯 CAMBIOS PRINCIPALES IMPLEMENTADOS

### **✓ 1. Movió 9 Servicios → 9 Use Cases**
```
ANTES:
app/domain/services/CreateBankAccountService.java (Servicio en dominio)

AHORA:
app/application/usecases/CreateBankAccountUseCase.java (Use Case en app)
```

**Razón:** Los Use Cases orquestan flujos, pertenecen a aplicación, no dominio.

---

### **✓ 2. Agregó Relación Asesor-Cliente**
```
ANTES:
NaturalPersonClient { userId, fullName, email, ... }

AHORA:
NaturalPersonClient { 
  userId, fullName, email, ...
  assignedCommercialEmployeeId ← NUEVO
}

Validación en NaturalClientUseCase:
if (!employeeId.equals(client.getAssignedCommercialEmployeeId())) {
  throw UnauthorizedClientAccessException
}
```

---

### **✓ 3. Completó SQL Server**
```
ANTES:
- UserRepository (interfaz en dominio)
- SqlServerBankAccountRepository (adapter)
- ❌ SqlServerUserRepository (FALTA)
- ❌ SqlServerAuditLogRepository (FALTA)

AHORA:
- ✓ UserEntity.java + UserJpaRepository.java
- ✓ AuditLogEntity.java + AuditLogJpaRepository.java
- ✓ SqlServerUserRepository.java (adapter)
- ✓ SqlServerAuditLogRepository.java (adapter)
```

---

### **✓ 4. Creó Scheduler para Expiración**
```
ANTES:
- Transfer.checkExpiration() [método en modelo]
- ❌ Nada ejecuta automáticamente

AHORA:
- ✓ TransferExpirationScheduler.java (@Scheduled cada 3 min)
- ✓ SchedulerConfig.java (@EnableScheduling)
- ✓ Automáticamente marca EXPIRED después de 60 min
- ✓ Registra en audit log
```

---

### **✓ 5. Creó 4 Controladores REST**
```
ANTES:
- ❌ HumanResourceController (vacío)

AHORA:
- ✓ ClientController → GET /api/clients/{id}
- ✓ LoanController → POST /api/loans/{id}/approve
- ✓ TransferController → POST /api/transfers
- ✓ AccountController → POST /api/accounts
```

---

### **✓ 6. Configuró Dual-DB**
```
ANTES:
- ❌ PersistenceConfig.java (NO EXISTE)
- Spring auto-detecta BDs (confuso)

AHORA:
- ✓ PersistenceConfig.java (explícita)
- ✓ @EnableJpaRepositories(basePackages="...sqlserver...")
- ✓ @EnableMongoRepositories(basePackages="...mongodb...")
- ✓ application.yml controla cuál activa
```

---

### **✓ 7. Agregó DTOs y Seguridad**
```
ANTES:
- ❌ Controllers directamente con modelos
- ❌ Sin autenticación

AHORA:
- ✓ CreateLoanRequestDTO, CreateTransferDTO, etc.
- ✓ SecurityConfig.java (roles por endpoint)
- ✓ Authentication auth en controladores
```

---

## 📚 ARCHIVOS CLAVE PARA PRESENTAR

```
📁 Mostrar estructura:
   - app/domain/models/Transfer.java        ← Regla expiración (60 min)
   - app/domain/models/NaturalPersonClient.java ← Asignación asesor
   - app/application/usecases/NaturalClientUseCase.java ← Validación
   - app/application/adapter/api/controllers/ClientController.java ← REST
   - app/infrastructure/persistence/PersistenceConfig.java ← Dual-DB
   - app/infrastructure/scheduler/TransferExpirationScheduler.java ← Auto-expire
   - app/infrastructure/security/SecurityConfig.java ← Roles
```

---

## ✅ CHECKLIST FINAL

- ✅ **3 capas implementadas:** Dominio, Aplicación, Infraestructura
- ✅ **Use Cases movidos:** 9 servicios → 9 Use Cases en capa correcta
- ✅ **Validación Asesor-Cliente:** En NaturalClientUseCase + NaturalPersonClient.assignedCommercialEmployeeId
- ✅ **Expiración 60 min:** transfer.checkExpiration() + TransferExpirationScheduler
- ✅ **Dual-DB:** PersistenceConfig + application.yml
- ✅ **SQL Server Completo:** UserEntity, AuditLogEntity, adapters
- ✅ **REST Endpoints:** 4 controladores con seguridad
- ✅ **DTOs:** Traducen JSON ↔ Modelos
- ✅ **Documentación:** 2 archivos markdown (este + casos de uso)

---

## 🚀 Conclusión

El proyecto ahora es **profesional**, **escalable** y **fácil de mantener**:
- **Claro:** Cada capa tiene una responsabilidad
- **Seguro:** Validaciones en lógica, roles en seguridad
- **Flexible:** Cambiar BD es 1 línea en config
- **Automático:** Scheduler mantiene BD consistente
- **Auditable:** Todos los cambios se registran
