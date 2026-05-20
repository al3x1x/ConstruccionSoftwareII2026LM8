# Sistema Bancario - Casos de Uso Detallados
## Con Ejemplos Prácticos de Cada Capa

---

## 📱 CASO DE USO 1: Crear Transferencia Bancaria

### **Escenario Concreto:**
Cliente María (MARIA-CLI-001) quiere transferir $15,000 de su cuenta corriente (ACC-001) a la cuenta de su hermano en otro banco (ACC-002).

**Como María (Cliente):**
```
1. Entra a app móvil
2. Selecciona: "Nueva Transferencia"
3. Ingresa:
   - Cuenta origen: ACC-001
   - Cuenta destino: ACC-002
   - Monto: $15,000
   - Descripción: "Para hermano"
4. Click: "Transferir"
```

### **¿Qué pasa en el Backend? (4 Capas)**

#### **🔴 CAPA 1 - PRESENTACIÓN (API REST)**
```
POST /api/transfers
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
Content-Type: application/json

{
  "originAccount": "ACC-001",
  "destinationAccount": "ACC-002",
  "amount": 15000
}

↓ TransferController.createTransfer()
```

**Código:**
```java
@RestController
@RequestMapping("/api/transfers")
public class TransferController {
    
    @PostMapping
    public ResponseEntity<Transfer> createTransfer(
            @RequestBody CreateTransferDTO dto,    // ← DTO: transforma JSON
            Authentication auth) {                 // ← Extrae usuario
        
        // Extrae JWT → obtiene userId
        String creatorUserId = auth.getName();  // Ej: "MARIA-CLI-001"
        
        // Crea command (puente entre capas)
        CreateTransferCommand command = new CreateTransferCommand(
            UUID.randomUUID().toString(),        // transferId
            dto.getOriginAccount(),              // "ACC-001"
            dto.getDestinationAccount(),         // "ACC-002"
            new BigDecimal("15000"),             // monto
            creatorUserId                        // "MARIA-CLI-001"
        );
        
        // Llama Use Case
        Transfer transfer = createTransferUseCase.execute(command);
        return ResponseEntity.ok(transfer);
    }
}
```

---

#### **🟡 CAPA 2 - APLICACIÓN (Use Cases + Orquestación)**
```
CreateTransferUseCase.execute(command)
│
├─ VALIDACIÓN 1: ¿Son cuentas diferentes?
│  └─ "ACC-001" ≠ "ACC-002" ✓
│
├─ VALIDACIÓN 2: ¿Existen las cuentas?
│  └─ bankAccountRepository.findByAccountNumber("ACC-001") ✓
│  └─ bankAccountRepository.findByAccountNumber("ACC-002") ✓
│
└─ CREACIÓN: Llama al modelo de dominio
   └─ new Transfer(...)
```

**Código:**
```java
@Component
public class CreateTransferUseCase {
    private TransferRepository transferRepository;
    private BankAccountRepository bankAccountRepository;

    public Transfer execute(CreateTransferCommand command) {
        // ✓ Validación: cuentas diferentes
        if (command.getOriginAccount().equals(command.getDestinationAccount())) {
            throw new IllegalArgumentException(
                "Origen y destino deben ser diferentes"
            );
        }

        // ✓ Validación: cuentas existen
        BankAccount origin = bankAccountRepository
            .findByAccountNumber(command.getOriginAccount())
            .orElseThrow(() -> new IllegalArgumentException("Cuenta origen no existe"));
        
        BankAccount destination = bankAccountRepository
            .findByAccountNumber(command.getDestinationAccount())
            .orElseThrow(() -> new IllegalArgumentException("Cuenta destino no existe"));

        // ✓ Crea Transfer (modelo de dominio)
        // → Automáticamente calcula status basado en monto
        Transfer transfer = new Transfer(
            command.getTransferId(),           // "TRN-a1b2c3d4..."
            command.getOriginAccount(),        // "ACC-001"
            command.getDestinationAccount(),   // "ACC-002"
            command.getAmount(),               // $15,000
            command.getCreatorUserId()         // "MARIA-CLI-001"
        );

        // Persiste (infraestructura)
        transferRepository.save(transfer);
        return transfer;
    }
}
```

---

#### **🔵 CAPA 3 - DOMINIO (Reglas de Negocio)**
```
new Transfer(
    "TRN-a1b2c3d4",     transferId
    "ACC-001",          originAccount
    "ACC-002",          destinationAccount
    15000,              amount
    "MARIA-CLI-001"     creatorUserId
)

┌─ Regla 1: ¿Monto > $10,000?
│  └─ 15000 > 10000 → SÍ ✓
│     └─ status = AWAITING_APPROVAL (requiere aprobación)
│
└─ Regla 2: ¿Fecha de creación?
   └─ creationDate = NOW()
   └─ Se usará en 60 minutos para expiración
```

**Código del Modelo:**
```java
public class Transfer {
    private String transferId;
    private String originAccount;
    private String destinationAccount;
    private BigDecimal amount;
    private LocalDateTime creationDate;
    private TransferStatus status;           // PENDING, AWAITING_APPROVAL, EXPIRED...
    private String creatorUserId;
    private String assignedCommercialEmployeeId;

    public static final BigDecimal APPROVAL_THRESHOLD = new BigDecimal("10000.00");
    public static final long EXPIRATION_MINUTES = 60;

    public Transfer(String transferId, String originAccount, String destinationAccount,
                    BigDecimal amount, String creatorUserId) {
        this.transferId = transferId;
        this.originAccount = originAccount;
        this.destinationAccount = destinationAccount;
        this.amount = amount;
        this.creatorUserId = creatorUserId;
        this.creationDate = LocalDateTime.now();

        // ← REGLA DE NEGOCIO: monto alto requiere aprobación
        if (amount.compareTo(APPROVAL_THRESHOLD) > 0) {
            this.status = TransferStatus.AWAITING_APPROVAL;  // Espera supervisor
        } else {
            this.status = TransferStatus.PENDING;            // Se ejecuta ya
        }
    }

    // ← REGLA: expiración automática
    public boolean checkExpiration() {
        if (!TransferStatus.AWAITING_APPROVAL.equals(this.status)) {
            return false;  // Solo se expiran las que esperan aprobación
        }

        long minutesElapsed = ChronoUnit.MINUTES.between(this.creationDate, LocalDateTime.now());
        
        if (minutesElapsed > EXPIRATION_MINUTES) {  // > 60 minutos
            this.status = TransferStatus.EXPIRED;
            return true;
        }
        return false;
    }

    public void approve(String approverUserId) {
        this.approverUserId = approverUserId;
        this.approvalDate = LocalDateTime.now();
        this.status = TransferStatus.APPROVED;
    }
}
```

**¿Por qué aquí y no en la BD?**
- Las reglas pertenecen a la lógica, no a la persistencia
- Si cambias de SQL Server a MongoDB, las reglas siguen igual
- Fácil de testear sin BD

---

#### **🟢 CAPA 4 - INFRAESTRUCTURA (Persistencia)**
```
transferRepository.save(transfer)
│
├─ Identifica: ¿Qué BD está configurada?
│  └─ application.yml → spring.jpa.enabled: true
│
└─ Ejecuta SqlServerTransferRepository
   │
   ├─ Convierte Transfer → TransferEntity
   │  └─ Transfer.transferId → TransferEntity.transferId
   │  └─ Transfer.amount → TransferEntity.amount
   │  └─ Transfer.status → TransferEntity.status (AWAITING_APPROVAL)
   │
   └─ INSERT INTO transfers (...)
      └─ SQL Server ✓
```

**Código del Adaptador:**
```java
@Component
public class SqlServerTransferRepository implements TransferRepository {
    private final TransferJpaRepository jpaRepository;

    public SqlServerTransferRepository(TransferJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    // Guarda modelo de dominio en BD
    @Override
    public void save(Transfer transfer) {
        TransferEntity entity = toJpaEntity(transfer);
        jpaRepository.save(entity);
    }

    // Convierte: Modelo (Transfer) → Entidad BD (TransferEntity)
    private TransferEntity toJpaEntity(Transfer transfer) {
        TransferEntity entity = new TransferEntity();
        entity.setTransferId(transfer.getTransferId());
        entity.setOriginAccount(transfer.getOriginAccount());
        entity.setDestinationAccount(transfer.getDestinationAccount());
        entity.setAmount(transfer.getAmount());
        entity.setStatus(transfer.getStatus());  // AWAITING_APPROVAL
        entity.setCreationDate(transfer.getCreationDate());
        entity.setCreatorUserId(transfer.getCreatorUserId());
        return entity;
    }

    // Convierte: Entidad BD → Modelo (Transfer)
    private Transfer toDomainModel(TransferEntity entity) {
        Transfer transfer = new Transfer(
            entity.getTransferId(),
            entity.getOriginAccount(),
            entity.getDestinationAccount(),
            entity.getAmount(),
            entity.getCreatorUserId()
        );
        transfer.setStatus(entity.getStatus());
        return transfer;
    }
}
```

**¿Qué es TransferEntity?**
```java
@Entity
@Table(name = "transfers")
public class TransferEntity {
    @Id
    private String transferId;

    @Column(name = "origin_account")
    private String originAccount;

    @Column(name = "destination_account")
    private String destinationAccount;

    @Column(name = "amount", precision = 18, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransferStatus status;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    // ... getters/setters
}
```

---

### **📊 Resumen del Flujo Completo:**

```
API JSON       →  DTO            →  Command      →  Transfer Model  →  Entity BD
┌──────────┐     ┌──────────┐     ┌──────────┐     ┌──────────────┐    ┌──────────┐
│{         │     │{         │     │{         │     │class         │    │@Entity  │
│origin:   │     │origin:   │     │origin:   │     │Transfer {    │    │table:   │
│ACC-001   │     │ACC-001   │  →  │ACC-001   │  →  │ amount:15k   │ →  │transfers│
│dest:     │  →  │dest:     │     │dest:     │     │ >10k?→YES    │    │amount   │
│ACC-002   │     │ACC-002   │     │ACC-002   │     │ status:      │    │status:  │
│amount:   │     │amount:   │     │amount:   │     │AWAITING_     │    │AWAITING_│
│15000     │     │15000     │     │15000     │     │APPROVAL      │    │APPROVAL │
│}         │     │}         │     │}         │     │}             │    │}        │
└──────────┘     └──────────┘     └──────────┘     └──────────────┘    └──────────┘
     ↑                 ↑                ↑                  ↑                  ↑
   CAPA 1          CAPA 1          CAPA 2            CAPA 3            CAPA 4
(PRESENTACIÓN)     (API)         (USE CASE)        (DOMINIO)      (INFRAESTRUCTURA)
```

---

## 🔐 CASO DE USO 2: Validación Asesor-Cliente

### **Escenario Concreto:**
**Problema:** Juan (asesor de María) intenta crear un préstamo para Pedro (cliente de otro asesor).

**¿Qué pasa?**

```
POST /api/clients/PEDRO-001/loans
Authorization: Bearer <JWT_JUAN>

{
  "loanType": "PERSONAL",
  "amount": 50000,
  "termMonths": 24
}

┌─────────────────────────────────────────────────────────┐
│ CAPA 1 - CONTROLADOR                                    │
│ ClientController.requestLoan()                          │
│ - Extrae: employeeId = "JUAN-001"                      │
│ - Extrae: clientId = "PEDRO-001"                       │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│ CAPA 2 - USE CASE                                       │
│ NaturalClientUseCase.createLoanRequest(                 │
│     "JUAN-001", "PEDRO-001", "PERSONAL", 50000         │
│ )                                                       │
│                                                         │
│ Llama: validateEmployeeClientRelationship()            │
│ Busca: User client = userRepository.findById(          │
│        "PEDRO-001"                                     │
│ )                                                       │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│ CAPA 3 - DOMINIO (Modelo)                               │
│ NaturalPersonClient {                                   │
│   userId: "PEDRO-001",                                 │
│   fullName: "Pedro García",                            │
│   assignedCommercialEmployeeId: "MARIA-002"  ← Asignado a María
│ }                                                       │
│                                                         │
│ Validación:                                             │
│ if (!"JUAN-001".equals("MARIA-002")) {                 │
│   throw UnauthorizedClientAccessException              │
│ }                                                       │
│                                                         │
│ ✗ NO AUTORIZADO                                        │
└─────────────────────────────────────────────────────────┘
                         ↓
┌─────────────────────────────────────────────────────────┐
│ RESPUESTA                                               │
│ HTTP 403 FORBIDDEN                                      │
│ {                                                       │
│   "error": "Empleado JUAN-001 no está autorizado       │
│            para gestionar cliente PEDRO-001"            │
│ }                                                       │
└─────────────────────────────────────────────────────────┘
```

**Código de Validación:**
```java
@Component
public class NaturalClientUseCase {
    private UserRepository userRepository;

    private void validateEmployeeClientRelationship(String employeeId, String clientId) {
        // Paso 1: Obtén cliente del repositorio
        User client = userRepository.findById(clientId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no existe"));

        // Paso 2: Verifica que sea cliente natural
        if (!(client instanceof NaturalPersonClient)) {
            throw new IllegalArgumentException("No es cliente natural");
        }

        // Paso 3: Castea y obtén su asesor asignado
        NaturalPersonClient naturalClient = (NaturalPersonClient) client;
        String assignedEmpId = naturalClient.getAssignedCommercialEmployeeId();

        // Paso 4: Compara con asesor que está haciendo la request
        if (!employeeId.equals(assignedEmpId)) {
            throw new UnauthorizedClientAccessException(employeeId, clientId);
        }
        // ✓ Si llegamos aquí, está autorizado
    }

    public NaturalPersonClient createLoanRequest(String employeeId, String clientId,
                                                 String loanType, BigDecimal amount) {
        // ← VALIDACIÓN EN FORMA DE REGLA DE NEGOCIO
        validateEmployeeClientRelationship(employeeId, clientId);

        // Si pasó validación, continúa...
        User client = userRepository.findById(clientId).get();
        NaturalPersonClient naturalClient = (NaturalPersonClient) client;

        Loan loan = new Loan(...);
        loan.setAssignedCommercialEmployeeId(employeeId);
        naturalClient.addLoan(loan);

        userRepository.save(naturalClient);
        return naturalClient;
    }
}
```

---

## ⏰ CASO DE USO 3: Expiración Automática de Transferencias

### **Escenario:**
Supervisor aprueba transferencia de $15k a Juan. Pero se olvida de ejecutarla en 60 minutos.

**Timeline:**
```
T=0 min:
  - Transfer creada
  - Status: AWAITING_APPROVAL
  - BD: INSERT INTO transfers (status='AWAITING_APPROVAL', creation_date=2025-05-14 14:00:00)

T=3 min:
  - Scheduler corre (cada 3 min)
  - Lee transferencias en AWAITING_APPROVAL
  - Calcula: 3 min elapsed < 60 min → NO expira
  - Continúa...

T=59 min:
  - Scheduler corre nuevamente
  - Calcula: 59 min elapsed < 60 min → NO expira
  - Continúa...

T=61 min:
  - ¡¡¡SCHEDULER CORRE!!!
  - Lee: transfer.creationDate = 2025-05-14 14:00:00
  - Calcula: 61 min elapsed > 60 min → ¡SÍ EXPIRA!
  - transfer.checkExpiration() → status = EXPIRED
  - UPDATE transfers SET status='EXPIRED' WHERE transferId='...'
  - INSERT INTO audit_logs (...) → "Transfer expired after 60 minutes"
  - ✓ Base de datos automáticamente consistente
```

**Código del Scheduler:**
```java
@Component
public class TransferExpirationScheduler {
    private final TransferRepository transferRepository;
    private final AuditLogRepository auditLogRepository;

    // ← Se ejecuta automáticamente cada 3 minutos
    @Scheduled(fixedRate = 180000)  // 180,000 ms = 3 minutos
    public void checkAndMarkExpiredTransfers() {
        // Paso 1: Obtén todas las transferencias pendientes
        List<Transfer> awaitingTransfers = transferRepository
            .findByStatus(TransferStatus.AWAITING_APPROVAL);

        // Paso 2: Para cada una, verifica expiración
        for (Transfer transfer : awaitingTransfers) {
            // ← Usa método del modelo (DOMINIO)
            if (transfer.checkExpiration()) {  // ← Retorna true si expiró

                // Paso 3: Persiste cambio
                transferRepository.update(transfer);

                // Paso 4: Registra en audit
                AuditLog log = new AuditLog(
                    UUID.randomUUID().toString(),
                    "SYSTEM",  // Usuario: sistema automático
                    "TRANSFER_EXPIRED",
                    "Transfer",
                    transfer.getTransferId(),
                    LocalDateTime.now(),
                    "Transfer expirado después de 60 minutos sin aprobación"
                );
                auditLogRepository.save(log);

                System.out.println("Transfer " + transfer.getTransferId() + " EXPIRED");
            }
        }
    }
}
```

**¿Cómo lo activas?**
```java
@Configuration
@EnableScheduling  // ← Activa @Scheduled
public class SchedulerConfig {
}
```

**Verificación en BD:**
```sql
-- Antes de los 60 minutos:
SELECT * FROM transfers WHERE transferId = 'TRN-001';
-- transferId | status            | creation_date
-- TRN-001    | AWAITING_APPROVAL | 2025-05-14 14:00:00

-- Después de 60+ minutos (scheduler corre):
SELECT * FROM transfers WHERE transferId = 'TRN-001';
-- transferId | status  | creation_date
-- TRN-001    | EXPIRED | 2025-05-14 14:00:00  ← Sin cambiar creation_date

-- Audit log registra:
SELECT * FROM audit_logs WHERE entityId = 'TRN-001';
-- auditId    | userId | action             | entityType | entityId | timestamp
-- AUD-00123  | SYSTEM | TRANSFER_EXPIRED   | Transfer   | TRN-001  | 2025-05-14 15:01:00
```

---

## 🎓 PREGUNTAS TÍPICAS DEL PROFESOR

### **P: ¿Por qué 3 capas? ¿No basta con Controlador → BD?**
**R:** Con 3 capas:
- **Independencia:** Cambias BD sin tocar lógica
- **Testeable:** Mockeas repositorios, testas modelos sin BD
- **Profesional:** Código limpio, separación de responsabilidades

```
❌ MAL (acoplado):
Controller → SQL Server
    (cambias a Postgres = refactor todo el controller)

✓ BIEN (desacoplado):
Controller → Use Case → Transfer Model → Repositorio → BD
    (cambias a Postgres = solo adapta repositorio)
```

### **P: ¿Dónde quedan las reglas de negocio?**
**R:** En **DOMINIO**, no en BD.

```
❌ MALO: Regla en trigger SQL
CREATE TRIGGER check_transfer_amount
BEFORE INSERT ON transfers
BEGIN
  IF amount > 10000 THEN status = 'AWAITING_APPROVAL'
END;
    → Difícil testear, acoplado a SQL Server

✓ BIEN: Regla en Transfer.java
public Transfer(...) {
  if (amount.compareTo(10000) > 0) {
    status = TransferStatus.AWAITING_APPROVAL;
  }
}
    → Fácil testear, portable
```

### **P: ¿Se puede usar MongoDB y SQL Server juntos?**
**R:** Sí, pero configurados en `PersistenceConfig`.

```java
@EnableJpaRepositories(
    basePackages = "app.application.adapter.persistence.sqlserver.repositories"
)
@EnableMongoRepositories(
    basePackages = "app.application.adapter.persistence.mongodb.repositories"
)
```

Activas 1 u otro en `application.yml`:
```yaml
spring:
  jpa.enabled: true              # Activa SQL Server
  data.mongodb.enabled: false    # Desactiva MongoDB
```

---

## 📋 CHECKLIST PARA PRESENTAR AL PROFESOR

- ✅ **Arquitectura de 3 capas:** Dominio, Aplicación, Infraestructura
- ✅ **Validación Asesor-Cliente:** Implementada en Use Case
- ✅ **Expiración de Transferencias:** Scheduler automático cada 3 min
- ✅ **Persistencia Dual:** SQL Server + MongoDB soportados
- ✅ **Seguridad:** Roles configurados por endpoint
- ✅ **Código Limpio:** Reglas en Dominio, no en BD
- ✅ **Testeable:** Mock Repositories para unit tests
- ✅ **DTOs:** Traducen JSON ↔ Modelos
- ✅ **Documentación:** Este documento + código comentado

---

## 🚀 Conclusión

Con esta arquitectura, el sistema es:
1. **Flexible** - Cambiar BD es cambiar 1 archivo
2. **Seguro** - Reglas validadas en lógica, no en BD
3. **Escalable** - Agregar nuevos tipos de usuarios es agregar clases
4. **Mantenible** - Responsabilidades claras por capa
5. **Profesional** - Listo para producción
