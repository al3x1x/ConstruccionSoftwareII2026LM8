# Sistema Bancario - Arquitectura Clean Architecture
## Resumen Ejecutivo para Presentación

---

## 🏗️ ARQUITECTURA DE CAPAS

El proyecto implementa **Clean Architecture** con **3 capas independientes**:

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTACIÓN (API REST)                  │
│                   (Controllers - HTTP)                      │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│              APLICACIÓN (Lógica de Negocio)                 │
│        (Use Cases + Adaptadores + DTOs)                    │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│         DOMINIO (Modelos + Reglas de Negocio)              │
│           (Entidades + Servicios de Dominio)               │
└─────────────────────────────────────────────────────────────┘
                              ↕
┌─────────────────────────────────────────────────────────────┐
│           INFRAESTRUCTURA (Datos + Config)                  │
│    (BD SQL Server + MongoDB + Seguridad + Scheduler)       │
└─────────────────────────────────────────────────────────────┘
```

---

## 📌 CADA CAPA Y SU RESPONSABILIDAD

### **1️⃣ CAPA DE DOMINIO** (`app.domain`)
**¿Qué es?** El corazón de la aplicación. Contiene las **reglas de negocio puras** sin depender de frameworks.

**Componentes:**
- **Modelos**: `User`, `NaturalPersonClient`, `Transfer`, `Loan`, `BankAccount`
- **Puertos (Interfaces)**: Define contratos que la infraestructura debe cumplir
- **Excepciones**: `UnauthorizedClientAccessException`, `InsufficientFundsException`
- **Enumeraciones**: Estados (`LoanStatus`, `TransferStatus`, `UserRole`)

**Ejemplo - Modelo Transfer:**
```java
public class Transfer {
    private String transferId;
    private BigDecimal amount;
    private TransferStatus status;        // PENDING, AWAITING_APPROVAL, APPROVED, EXECUTED, EXPIRED
    private LocalDateTime creationDate;
    private static final BigDecimal APPROVAL_THRESHOLD = new BigDecimal("10000.00");
    private static final long EXPIRATION_MINUTES = 60;

    // Regla de negocio 1: Transferencias > $10k requieren aprobación
    public Transfer(...) {
        if (amount.compareTo(APPROVAL_THRESHOLD) > 0) {
            this.status = TransferStatus.AWAITING_APPROVAL;
        }
    }

    // Regla de negocio 2: Expiración automática a los 60 minutos
    public boolean checkExpiration() {
        if (!AWAITING_APPROVAL.equals(status)) return false;
        long minutesElapsed = ChronoUnit.MINUTES.between(creationDate, now());
        if (minutesElapsed > EXPIRATION_MINUTES) {
            status = TransferStatus.EXPIRED;
            return true;
        }
        return false;
    }
}
```

**🔑 Ventaja:** Las reglas de negocio viven aquí, independientes de la BD o framework.

---

### **2️⃣ CAPA DE APLICACIÓN** (`app.application`)
**¿Qué es?** Coordina el flujo de datos. Traduce del mundo exterior (HTTP/DTOs) al mundo del dominio (Modelos).

**Componentes:**

#### **A) Use Cases** (`application/usecases/`)
Orquestan la lógica. Cada Use Case = **1 acción de usuario**.

**Ejemplo - CreateBankAccountUseCase:**
```java
@Component
public class CreateBankAccountUseCase {
    private UserRepository userRepository;
    private BankAccountRepository bankAccountRepository;

    // Paso 1: Valida entrada
    public BankAccount execute(CreateBankAccountCommand command) {
        User holder = userRepository.findById(command.getHolderId())
            .orElseThrow(() -> new IllegalArgumentException("Usuario no existe"));

        // Paso 2: Delega al modelo (dominio)
        BankAccount account = new BankAccount(
            command.getAccountNumber(),
            command.getAccountType(),
            command.getHolderId(),
            command.getCurrency()
        );

        // Paso 3: Persiste usando puerto
        bankAccountRepository.save(account);
        return account;
    }
}
```

#### **B) Controladores REST** (`adapter/api/controllers/`)
Reciben HTTP, validan rol, llaman Use Cases.

**Ejemplo - ClientController:**
```java
@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final NaturalClientUseCase naturalClientUseCase;

    @GetMapping("/{clientId}/loans")
    public ResponseEntity<List<Loan>> listClientLoans(
            @PathVariable String clientId,
            Authentication auth) {  // ← Rol del usuario autenticado
        
        String employeeId = auth.getName();
        
        // Llama Use Case que valida Asesor-Cliente
        List<Loan> loans = naturalClientUseCase.listClientLoans(employeeId, clientId);
        return ResponseEntity.ok(loans);
    }
}
```

#### **C) Adaptadores** (`adapter/persistence/`)
Traducen entre Modelo de Dominio y Entidad de Base de Datos.

**Ejemplo - SqlServerUserRepository:**
```java
@Component
public class SqlServerUserRepository implements UserRepository {
    private UserJpaRepository jpaRepository;

    // Modelo → Entidad BD
    private UserEntity toJpaEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setUserId(user.getUserId());
        entity.setFullName(user.getFullName());
        // Dominio habla de User; BD habla de UserEntity
        return entity;
    }

    // Entidad BD → Modelo
    private User toDomainModel(UserEntity entity) {
        return new NaturalPersonClient(
            entity.getUserId(),
            entity.getFullName(), ...
        );
    }
}
```

#### **D) DTOs** (`adapter/api/dto/`)
Traducen JSON ↔ Modelos de Dominio.

```java
public class CreateLoanRequestDTO {
    private String loanType;
    private BigDecimal amount;
    private int termMonths;
}
// Controller: JSON → DTO → UseCase → Modelo de Dominio
```

**🔑 Ventaja:** La BD (SQL/Mongo) puede cambiar sin tocar lógica de negocio.

---

### **3️⃣ CAPA DE INFRAESTRUCTURA** (`app.infrastructure`)
**¿Qué es?** Detalles técnicos: cómo persisten datos, seguridad, scheduling.

**Componentes:**

#### **A) Persistencia** (`infrastructure/persistence/`)
```java
@Configuration
@EnableJpaRepositories(
    basePackages = "app.application.adapter.persistence.sqlserver.repositories",
    entityManagerFactoryRef = "sqlserverEntityManagerFactory"
)
@EnableMongoRepositories(
    basePackages = "app.application.adapter.persistence.mongodb.repositories"
)
public class PersistenceConfig {
    // Separa SQL Server de MongoDB
    // Permite cambiar BD sin tocar aplicación
}
```

**Resultado:** Mismo código, 2 BDs posibles.

#### **B) Scheduler** (`infrastructure/scheduler/`)
Ejecuta tareas automáticas cada 3 minutos:

```java
@Component
public class TransferExpirationScheduler {
    @Scheduled(fixedRate = 180000)  // 3 minutos
    public void checkAndMarkExpiredTransfers() {
        List<Transfer> awaiting = transferRepository
            .findByStatus(AWAITING_APPROVAL);
        
        for (Transfer transfer : awaiting) {
            if (transfer.checkExpiration()) {  // ← Usa regla de dominio
                transferRepository.update(transfer);
                auditLogRepository.save(log);
            }
        }
    }
}
```

**Resultado:** A los 61+ minutos, transferencias automáticamente EXPIRED.

#### **C) Seguridad** (`infrastructure/security/`)
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.authorizeHttpRequests(authz -> authz
            .requestMatchers("/api/clients/**")
                .hasAnyRole("COMMERCIAL_EMPLOYEE", "EXECUTIVE")
            .requestMatchers("/api/loans/**")
                .hasAnyRole("INTERNAL_ANALYST")
            .anyRequest().authenticated()
        );
        return http.build();
    }
}
```

**Resultado:** Endpoints protegidos por rol.

---

## 🔄 FLUJO COMPLETO: CREAR PRÉSTAMO

### **Escenario:** Asesor comercial Juan crea préstamo para cliente Pedro

```
1. HTTP REQUEST
   ┌─────────────────────────────────────────────────────────────┐
   │ POST /api/clients/PEDRO-001/loans                           │
   │ Authorization: Bearer <JWT_JUAN>                            │
   │ Body: {                                                     │
   │   "loanType": "PERSONAL",                                   │
   │   "amount": 50000,                                          │
   │   "termMonths": 24                                          │
   │ }                                                           │
   └─────────────────────────────────────────────────────────────┘
                         ↓
   
2. CONTROLADOR (API REST)
   ┌─────────────────────────────────────────────────────────────┐
   │ ClientController.requestLoan()                              │
   │ - Extrae employeeId del JWT: "JUAN-001"                    │
   │ - Convierte JSON → CreateLoanRequestDTO                    │
   │ - Llama Use Case                                            │
   └─────────────────────────────────────────────────────────────┘
                         ↓
   
3. USE CASE (ORQUESTACIÓN)
   ┌─────────────────────────────────────────────────────────────┐
   │ NaturalClientUseCase.createLoanRequest(                      │
   │    "JUAN-001",      ← Asesor                               │
   │    "PEDRO-001",     ← Cliente                              │
   │    "PERSONAL",      ← Tipo                                 │
   │    50000            ← Monto                                │
   │ )                                                           │
   │                                                             │
   │ VALIDACIÓN 1: ¿Existe Pedro?                               │
   │   userRepository.findById("PEDRO-001") → ✓                 │
   │                                                             │
   │ VALIDACIÓN 2: ¿Juan gestiona a Pedro?                      │
   │   if (!Juan.equals(Pedro.getAssignedCommercialEmployeeId)) │
   │       throw UnauthorizedClientAccessException ✗            │
   │   else ✓                                                    │
   └─────────────────────────────────────────────────────────────┘
                         ↓
   
4. MODELO DE DOMINIO (REGLA DE NEGOCIO)
   ┌─────────────────────────────────────────────────────────────┐
   │ Loan loan = new Loan(                                       │
   │    loanId: "LN-12345",                                     │
   │    clientId: "PEDRO-001",                                  │
   │    loanType: LoanType.PERSONAL,                            │
   │    requestedAmount: $50,000,                               │
   │    termMonths: 24                                          │
   │ );                                                          │
   │                                                             │
   │ Regla: if (50000 > 10000)                                  │
   │   status = LoanStatus.UNDER_REVIEW  ← Necesita aprobación │
   │                                                             │
   │ loan.setAssignedCommercialEmployeeId("JUAN-001")           │
   └─────────────────────────────────────────────────────────────┘
                         ↓
   
5. ADAPTADOR (PERSISTENCIA)
   ┌─────────────────────────────────────────────────────────────┐
   │ SqlServerLoanRepository.save(loan)                          │
   │   ↓ Convierte Loan → LoanEntity (SQL)                      │
   │   ↓ INSERT INTO loans (...) VALUES (...)                   │
   │   ↓ SQL Server                                              │
   │                                                             │
   │ MongoDbLoanRepository.save(loan)  [Opcional]                │
   │   ↓ Convierte Loan → LoanDocument (NoSQL)                  │
   │   ↓ db.loans.insertOne({...})                              │
   │   ↓ MongoDB                                                 │
   └─────────────────────────────────────────────────────────────┘
                         ↓
   
6. RESPUESTA HTTP
   ┌─────────────────────────────────────────────────────────────┐
   │ HTTP 200 OK                                                 │
   │ {                                                           │
   │   "loanId": "LN-12345",                                    │
   │   "clientId": "PEDRO-001",                                 │
   │   "status": "UNDER_REVIEW",                                │
   │   "requestedAmount": 50000,                                │
   │   "termMonths": 24                                         │
   │ }                                                           │
   └─────────────────────────────────────────────────────────────┘
```

---

## 🎯 REGLAS DE NEGOCIO IMPLEMENTADAS

### **Regla 1: Validación Asesor-Cliente**
**Problema:** Un asesor no puede crear préstamos para clientes de otro asesor.

**Solución en Capas:**

| Capa | Ubicación | Código |
|------|-----------|--------|
| **Dominio** | `NaturalPersonClient.java` | `private String assignedCommercialEmployeeId;` |
| **Aplicación** | `NaturalClientUseCase.java` | Valida relación antes de crear |
| **Infraestructura** | `SqlServerUserRepository.java` | Persiste campo en BD |

**Ejemplo:**
```java
// Juan intenta crear préstamo para cliente de María
NaturalClientUseCase.createLoanRequest(
    "JUAN-001",     // Asesor solicitante
    "PEDRO-001",    // Cliente (asignado a MARIA-001)
    "PERSONAL",
    50000
);

// → UnauthorizedClientAccessException:
//   "Empleado JUAN-001 no está autorizado para gestionar cliente PEDRO-001"
```

### **Regla 2: Expiración de Transferencias a 60 Minutos**
**Problema:** Transferencias > $10k requieren aprobación. Si pasan 60 min sin aprobar, expiran.

**Solución en Capas:**

| Capa | Ubicación | Código |
|------|-----------|--------|
| **Dominio** | `Transfer.checkExpiration()` | Calcula 60 min |
| **Aplicación** | `TransferExpirationScheduler.java` | Corre cada 3 min |
| **Infraestructura** | `SchedulerConfig.java` | `@EnableScheduling` |

**Ejemplo:**
```
Minute 0:   Transfer creada, amount=$15k → status=AWAITING_APPROVAL ✓
Minute 59:  Scheduler corre, no expira aún
Minute 61:  Scheduler corre → checkExpiration()=true → status=EXPIRED ✗
Audit log:  "Transfer LN-12345 expired after 60 minutes without approval"
```

### **Regla 3: Persistencia Dual (SQL + Mongo)**
**Problema:** Diferentes equipos necesitan diferentes BDs.

**Solución:**
```java
// Mismo código funciona con ambas:
@Component  // SqlServerLoanRepository
public class SqlServerLoanRepository implements LoanRepository { ... }

@Component  // MongoDbLoanRepository
public class MongoDbLoanRepository implements LoanRepository { ... }

// Selecciona activa por application.yml:
spring:
  jpa:
    enabled: true    # SQL Server
  data.mongodb:
    enabled: false   # MongoDB desactivado
```

**Resultado:** Cambiar BD = 1 línea en config, 0 cambios en código.

---

## 📊 BENEFICIOS DE ESTA ARQUITECTURA

| Beneficio | Explicación |
|-----------|-------------|
| **Independencia de Cambios** | Cambiar SQL Server por MongoDB = 0 cambios en lógica |
| **Testeable** | Usar Mock Repositories para tests sin BD real |
| **Mantenible** | Reglas de negocio centralizadas en Dominio |
| **Escalable** | Agregar asesor para más clientes = 1 campo, sin refactor |
| **Segura** | Validaciones en capa de aplicación, roles en controladores |
| **Automatizable** | Scheduler ejecuta 24/7 sin intervención |

---

## 🔐 EJEMPLO: SEGURIDAD POR ROLES

```
Rol: COMMERCIAL_EMPLOYEE (Asesor)
├─ /api/clients/{id}             ✓ Ver sus clientes
├─ /api/clients/{id}/loans       ✓ Crear préstamos
├─ /api/transfers                ✓ Crear transferencias
└─ /api/loans/{id}/approve       ✗ NO (solo INTERNAL_ANALYST)

Rol: INTERNAL_ANALYST (Analista)
├─ /api/clients/{id}             ✗ NO
├─ /api/loans/{id}/approve       ✓ Aprobar préstamos
├─ /api/loans/{id}/reject        ✓ Rechazar préstamos
└─ /api/loans/{id}/disburse      ✓ Desembolsar préstamos

Rol: TELLER_EMPLOYEE (Cajero)
├─ /api/accounts                 ✓ Crear cuentas
├─ /api/transfers/{id}/execute   ✓ Ejecutar transferencias
└─ /api/loans/{id}/approve       ✗ NO
```

---

## ✅ CONCLUSIÓN

**Clean Architecture permite que:**
1. **Equipo 1** (Dominio) defina reglas → No importa la BD
2. **Equipo 2** (Aplicación) orqueste flujos → Sin conocer SQL
3. **Equipo 3** (Infraestructura) mantenga BD → Sin tocar lógica
4. **Auditor** verifica reglas en Dominio → Son claras y centralizadas

**Resultado:** Sistema **flexible**, **seguro**, **mantenible** y **profesional**.
