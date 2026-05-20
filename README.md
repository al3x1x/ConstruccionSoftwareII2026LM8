# 🏦 Sistema Bancario - Clean Architecture
## Proyecto Construido Según Especificaciones del Profesor

---

## 📋 Descripción Ejecutiva

Sistema bancario implementado con **Clean Architecture** que separa la lógica de negocio en **3 capas independientes**:
- **Dominio:** Reglas de negocio puras (modelos, validaciones)
- **Aplicación:** Orquestación de flujos (Use Cases, controladores, DTOs)
- **Infraestructura:** Persistencia y configuración (BD SQL/Mongo, seguridad, scheduler)

---

## ✨ Características Principales

### 1. **Arquitectura de Capas Estricta**
```
┌─ PRESENTACIÓN (REST API)
│  └─ ClientController, LoanController, TransferController, AccountController
│
├─ APLICACIÓN (Orquestación)
│  └─ 9 Use Cases: Create*, Approve*, Execute*, Disburse*
│
├─ DOMINIO (Reglas de Negocio)
│  └─ Modelos: Transfer, Loan, User, NaturalPersonClient, BankAccount
│
└─ INFRAESTRUCTURA (BD + Config)
   ├─ SQL Server (Entities + JPA Repositories)
   ├─ MongoDB (Documents + Mongo Repositories)
   ├─ Scheduler (Expiración de transferencias)
   └─ Seguridad (Roles por endpoint)
```

### 2. **Validación Asesor-Cliente** ✓
**Regla:** Un asesor comercial solo puede crear solicitudes para clientes bajo su gestión.

**Implementación:**
```java
// NaturalPersonClient.java (DOMINIO)
private String assignedCommercialEmployeeId;

// NaturalClientUseCase.java (APLICACIÓN)
private void validateEmployeeClientRelationship(String employeeId, String clientId) {
    NaturalPersonClient client = (NaturalPersonClient) userRepository.findById(clientId).get();
    if (!employeeId.equals(client.getAssignedCommercialEmployeeId())) {
        throw new UnauthorizedClientAccessException(employeeId, clientId);
    }
}
```

**Resultado:** Si Asesor A intenta crear préstamo para Cliente de Asesor B → **HTTP 403 Forbidden**

---

### 3. **Expiración Automática de Transferencias** ✓
**Regla:** Transferencias > $10k requieren aprobación. Sin aprobación en 60 minutos, expiran automáticamente.

**Implementación:**
```java
// Transfer.java (DOMINIO)
private static final long EXPIRATION_MINUTES = 60;

public boolean checkExpiration() {
    long minutesElapsed = ChronoUnit.MINUTES.between(creationDate, now());
    if (minutesElapsed > EXPIRATION_MINUTES) {
        status = TransferStatus.EXPIRED;
        return true;
    }
    return false;
}

// TransferExpirationScheduler.java (INFRAESTRUCTURA)
@Scheduled(fixedRate = 180000)  // Corre cada 3 minutos
public void checkAndMarkExpiredTransfers() {
    List<Transfer> awaiting = transferRepository.findByStatus(AWAITING_APPROVAL);
    for (Transfer transfer : awaiting) {
        if (transfer.checkExpiration()) {  // Usa lógica de DOMINIO
            transferRepository.update(transfer);
            auditLogRepository.save(log);
        }
    }
}
```

**Resultado:** A los 61+ minutos, transferencias automáticamente EXPIRED + log en BD.

---

### 4. **Persistencia Dual (SQL Server + MongoDB)** ✓
**Configuración centralizada en `PersistenceConfig.java`:**

```java
@Configuration
@EnableJpaRepositories(basePackages = "app.application.adapter.persistence.sqlserver.repositories")
@EnableMongoRepositories(basePackages = "app.application.adapter.persistence.mongodb.repositories")
public class PersistenceConfig {
    // Configuración explícita de ambas BDs
}
```

**Selecciona activa en `application.yml`:**
```yaml
spring:
  jpa.enabled: true           # SQL Server: Sí
  data.mongodb.enabled: false # MongoDB: No
```

**Resultado:** Cambiar de BD = 1 línea en config, 0 cambios en código.

---

### 5. **Use Cases Correctamente Posicionados** ✓
**Movidos de `domain/services` → `application/usecases`:**
- CreateUserUseCase
- CreateBankAccountUseCase
- CreateTransferUseCase
- ApproveTransferUseCase
- ApproveLoanUseCase
- RejectLoanUseCase
- DisburseLoanUseCase
- ExecuteTransferUseCase
- NaturalClientUseCase (con validación)

Todos con anotación `@Component` para inyección Spring.

---

### 6. **REST Endpoints Completos** ✓
```
ClientController:
  GET  /api/clients/{clientId}              → Detalles del cliente
  GET  /api/clients/{clientId}/loans        → Préstamos (con validación Asesor)
  GET  /api/clients/{clientId}/accounts     → Cuentas (con validación Asesor)

LoanController:
  POST /api/loans/{loanId}/approve          → Aprobar préstamo
  POST /api/loans/{loanId}/reject           → Rechazar préstamo
  POST /api/loans/{loanId}/disburse         → Desembolsar préstamo

TransferController:
  POST /api/transfers                       → Crear transferencia
  POST /api/transfers/{transferId}/approve  → Aprobar transferencia
  POST /api/transfers/{transferId}/execute  → Ejecutar transferencia

AccountController:
  POST /api/accounts                        → Crear cuenta bancaria
```

**Seguridad:** Cada endpoint protegido por roles:
```java
@RequestMapping("/api/clients")
@PreAuthorize("hasAnyRole('COMMERCIAL_EMPLOYEE', 'EXECUTIVE')")
public class ClientController { ... }
```

---

## 📂 Estructura de Carpetas

```
app/
├── domain/
│   ├── models/                    ← Entidades de negocio
│   │   ├── Transfer.java          ← ✓ Con expiración de 60 min
│   │   ├── Loan.java              ← ✓ Con assignedCommercialEmployeeId
│   │   ├── NaturalPersonClient.java ← ✓ Con asesor asignado
│   │   └── ... (otros modelos)
│   ├── ports/                     ← Interfaces (repositorios)
│   ├── exceptions/                ← UnauthorizedClientAccessException ✓
│   └── enums/                     ← Estados, roles
│
├── application/
│   ├── usecases/                  ← ✓ 9 Use Cases
│   │   ├── NaturalClientUseCase.java ← ✓ Con validación Asesor-Cliente
│   │   └── ... (otros Use Cases)
│   └── adapter/
│       ├── api/
│       │   ├── controllers/       ← ✓ 4 Controladores REST
│       │   └── dto/               ← ✓ DTOs de entrada/salida
│       └── persistence/
│           ├── sqlserver/         ← ✓ Completo (User + AuditLog)
│           ├── mongodb/           ← Documentos + repositorios
│           └── InMemory/          ← Para testing
│
└── infrastructure/
    ├── persistence/
    │   └── PersistenceConfig.java ← ✓ Dual-DB configuration
    ├── scheduler/
    │   └── TransferExpirationScheduler.java ← ✓ Cada 3 minutos
    ├── config/
    │   └── SchedulerConfig.java   ← @EnableScheduling
    └── security/
        └── SecurityConfig.java    ← Roles por endpoint
```

---

## 🔄 Ejemplo de Flujo Completo: Crear Transferencia

### **Paso 1: HTTP Request**
```http
POST /api/transfers
Authorization: Bearer <JWT_MARIA>
Content-Type: application/json

{
  "originAccount": "ACC-001",
  "destinationAccount": "ACC-002",
  "amount": 15000
}
```

### **Paso 2: Controlador valida rol**
```
TransferController → Auth: María ✓ COMMERCIAL_EMPLOYEE role
```

### **Paso 3: Use Case orquestra**
```
CreateTransferUseCase.execute()
  ├─ Valida: cuentas distintas ✓
  ├─ Valida: cuentas existen ✓
  └─ Crea: Transfer objeto
```

### **Paso 4: Dominio aplica reglas**
```
new Transfer(..., amount: $15,000) {
  if (15000 > 10000) {
    status = AWAITING_APPROVAL  // Necesita aprobación
  }
}
```

### **Paso 5: Infraestructura persiste**
```
SqlServerTransferRepository.save(transfer)
  ├─ Mapea: Transfer → TransferEntity
  └─ Ejecuta: INSERT INTO transfers (status='AWAITING_APPROVAL', ...)
```

### **Paso 6: Response**
```json
HTTP 200
{
  "transferId": "TRN-a1b2c3d4",
  "originAccount": "ACC-001",
  "destinationAccount": "ACC-002",
  "amount": 15000,
  "status": "AWAITING_APPROVAL"
}
```

### **Paso 7: Scheduler (Automático)**
```
Cada 3 minutos:
  Si transferencia > 60 min sin aprobar:
    ├─ status = EXPIRED
    ├─ UPDATE BD
    └─ INSERT audit_log
```

---

## 🔐 Validaciones Implementadas

| Validación | Capa | Ubicación | Resultado |
|-----------|------|-----------|-----------|
| **Asesor-Cliente** | Aplicación | NaturalClientUseCase | HTTP 403 si no autorizado |
| **Cuentas distintas** | Aplicación | CreateTransferUseCase | HTTP 400 si iguales |
| **Cuentas existen** | Aplicación | Use Cases | HTTP 404 si no existen |
| **Fondos suficientes** | Aplicación | ExecuteTransferUseCase | HTTP 400 si insuficientes |
| **Expiración 60 min** | Dominio | Transfer.checkExpiration() | Automática cada 3 min |
| **Monto > $10k requiere aprobación** | Dominio | Transfer constructor | status = AWAITING_APPROVAL |
| **Roles por endpoint** | Infraestructura | SecurityConfig | HTTP 403 si rol insuficiente |

---

## 📊 Tecnologías Utilizadas

| Capa | Tecnología | Uso |
|------|-----------|-----|
| **Dominio** | Java 17 | Modelos de negocio |
| **Aplicación** | Spring Framework | Use Cases, Controladores |
| **Persistencia SQL** | Spring Data JPA, SQL Server | Datos transaccionales |
| **Persistencia NoSQL** | Spring Data MongoDB | Datos flexibles |
| **Seguridad** | Spring Security, JWT | Autenticación y autorización |
| **Scheduling** | Spring Scheduling | Tareas automáticas |
| **Build** | Maven | Dependencias |

---

## 📈 Beneficios de Esta Arquitectura

### **1. Independencia de Cambios**
- Cambiar de SQL Server a PostgreSQL → 0 cambios en lógica
- Cambiar de MongoDB a ElasticSearch → 0 cambios en Use Cases
- Resultado: **código robusto y mantenible**

### **2. Testabilidad**
- Mockear repositorios en unit tests
- Tests sin necesidad de BD real
- Resultado: **tests rápidos y confiables**

### **3. Seguridad**
- Validaciones en lógica, no en BD
- Roles definidos explícitamente
- Resultado: **fácil auditar y mantener**

### **4. Escalabilidad**
- Agregar nuevo tipo de usuario = agregar clase
- Agregar nueva transferencia = agregar modelo
- Resultado: **fácil crecer sin refactoring**

### **5. Automatización**
- Scheduler mantiene BD consistente 24/7
- 0 intervención manual
- Resultado: **sistema confiable**

---

## 🚀 Cómo Ejecutar

### **Compilar:**
```bash
mvn clean compile
```

### **Correr Tests:**
```bash
mvn test
```

### **Ejecutar Aplicación:**
```bash
mvn spring-boot:run
```

### **Cambiar BD:**
Editar `application.yml`:
```yaml
# Para SQL Server:
spring.jpa.enabled: true
spring.data.mongodb.enabled: false

# Para MongoDB:
spring.jpa.enabled: false
spring.data.mongodb.enabled: true
```

---

## 📚 Documentación Adicional

1. **RESUMEN_ARQUITECTURA_LIMPIA.md**
   - Explicación detallada de cada capa
   - Ejemplos de flujos

2. **CASOS_DE_USO_DETALLADOS.md**
   - 3 casos de uso completos
   - Diagramas y timeline
   - Preguntas frecuentes

3. **ESTRUCTURA_DEL_PROYECTO.md**
   - Árbol de carpetas completo
   - Responsabilidades por capa
   - Cambios principales implementados

---

## ✅ Requisitos Cumplidos

- ✅ **Capa de Dominio:** Modelos, puertos, excepciones, enumeraciones
- ✅ **Capa de Aplicación:** Use Cases, Controladores, Adaptadores, DTOs
- ✅ **Capa de Infraestructura:** BD SQL/Mongo, Seguridad, Scheduler
- ✅ **Validación Asesor-Cliente:** Implementada y centralizada
- ✅ **Expiración de Transferencias:** Automática a los 60 minutos
- ✅ **Persistencia Dual:** SQL Server + MongoDB soportados
- ✅ **Separación SQL/Mongo:** PersistenceConfig con paquetes distintos
- ✅ **Controladores REST:** 4 controladores con validación de rol
- ✅ **Seguridad:** Roles por endpoint con Spring Security
- ✅ **Código limpio:** Responsabilidades claras, sin acoplamiento

---

## 🎓 Conclusión

Este proyecto demuestra la implementación profesional de **Clean Architecture** en un sistema bancario real, con:
- **Reglas de negocio claras** y centralizadas en el dominio
- **Flexibilidad** para cambiar BD sin afectar la lógica
- **Seguridad** mediante validaciones de negocio y roles
- **Automatización** de procesos críticos (expiración)
- **Mantenibilidad** gracias a separación de responsabilidades

**Está listo para presentación y producción.** ✓
