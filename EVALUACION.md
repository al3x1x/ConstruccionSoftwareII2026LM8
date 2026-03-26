# 📋 EVALUACIÓN - Sistema Bancario
**Proyecto:** ConstruccionSoftwareII2026LM8
**Estudiantes:** Alexis González Sánchez, Armando Esteban González Velasquez, Samuel Barrera Quintero
**Rama evaluada:** `main` *(develop existe pero no contiene código Java)*
**Fecha de evaluación:** 23/03/2026
**Nota final: 4.4 / 5.0**

---

## 📊 Tabla de Puntajes

| Criterio | Peso | Puntaje (1-5) | Contribución |
|----------|------|----------------|--------------|
| 1. Modelado de dominio | 25% | 4 | 1.00 |
| 2. Relaciones entre entidades | 15% | 4 | 0.60 |
| 3. Uso de enums | 15% | 4 | 0.60 |
| 4. Manejo de estados | 5% | 5 | 0.25 |
| 5. Tipos de datos | 5% | 2 | 0.10 |
| 6. Separación Usuario vs Cliente | 10% | 3 | 0.30 |
| 7. Bitácora | 5% | 4 | 0.20 |
| 8. Reglas básicas de negocio | 5% | 5 | 0.25 |
| 9. Estructura del proyecto | 10% | 4 | 0.40 |
| 10. Repositorio | 10% | 2 | 0.20 |
| **TOTAL BASE** | 100% | | **3.90** |

### Bonus Aplicados

| Bonus | Puntaje |
|-------|---------|
| Herencia completa (User abstracto → 6 clases concretas con roles diferenciados) | +0.20 |
| Código limpio y legible (métodos de negocio, separación de responsabilidades, toString) | +0.20 |
| Nombres claros y consistentes en inglés | +0.10 |
| **Total bonus** | **+0.50** |

### Penalizaciones Aplicadas
Ninguna.

**NOTA FINAL: 3.90 + 0.50 = 4.4 / 5.0**

---

## 🔍 Análisis Detallado por Criterio

### 1. Modelado de dominio → 4/5
Entidades implementadas:
- ✅ `User` (abstract) — base común con userId, fullName, identificationNumber, email, phone, birthDate, address, role(UserRole), status(UserStatus), username, passwordHash; inicializa `status=ACTIVE`
- ✅ `NaturalPersonClient extends User` — cliente persona natural con `List<BankAccount>`, `List<Loan>`, `List<Transfer>`; `isOfLegalAge()`
- ✅ `TellerEmployee extends User` — cajero con `deposit()`, `withdrawal()`, `openAccount()`
- ✅ `CommercialEmployee extends User` — empleado comercial con `createLoanRequest()`, `createAccountRequest()`, `checkLoanStatus()`
- ✅ `CompanyOperative extends User` — operativo empresa con `createTransfer()`, `createBulkPayment()`
- ✅ `CompanySupervisor extends User` — supervisor con `approveTransfer()`, `rejectTransfer()`
- ✅ `InternalAnalyst extends User` — analista con `approveLoan()`, `rejectLoan()`, `disburseLoan()`
- ✅ `BankAccount` — con accountNumber, accountType(enum), holderId, balance, currency, status, openingDate
- ✅ `Loan` — con loanId, loanType(enum), clientId, amounts, termMonths, loanStatus, dates, disbursementAccountNumber
- ✅ `Transfer` — con transferId, accounts, amount, dates, status, creatorUserId, approverUserId; APPROVAL_THRESHOLD=10000
- ✅ `BankingProduct` — con productCode, productName, category(enum), requiresApproval
- ✅ `AuditLog` — con auditLogId, operationType(enum), operationDateTime, executorUserId, executorUserRole, affectedProductId; detail objects polimórficos
- ✅ `LoanDetail` — detail object con approvedAmount, interestRate, previousState, newState, analystId
- ✅ `TransferDetail` — detail object con amount y balances antes/después de origen y destino
- ✅ `ExpirationDetail` — detail object con reason, expirationDateTime, creatorUserId
- ✅ `Main.java` — demostración completa del flujo (cliente → cuenta → cajero → depósito → préstamo → analista → transferencia → bitácora)

**Observaciones:**
- ⚠️ No existe clase concreta `CompanyClient` para clientes empresariales (solo `UserRole.COMPANY_CLIENT` en el enum)

### 2. Relaciones entre entidades → 4/5
- ✅ `NaturalPersonClient` mantiene `List<BankAccount>`, `List<Loan>`, `List<Transfer>` — navegabilidad directa desde el cliente
- ✅ `NaturalPersonClient.addAccount()`, `addLoan()`, `addTransfer()` — métodos de gestión de colecciones
- ✅ `Transfer` referencia `creatorUserId` y `approverUserId` (quién creó y aprobó)
- ✅ `Loan` referencia `clientId` y `disbursementAccountNumber`
- ✅ `AuditLog` tiene `TransferDetail`, `LoanDetail`, `ExpirationDetail` como objetos de detalle polimórficos (solo uno tiene valor según la operación)
- ✅ `CompanyOperative` y `CompanySupervisor` tienen `companyId`

**Observaciones:**
- ⚠️ Las relaciones entre entidades usan String IDs (holderId, clientId, creatorUserId) en lugar de referencias directas a objetos — reduce la navegabilidad
- ⚠️ `BankAccount.holderId` es String, no referencia a `User`

### 3. Uso de enums → 4/5
Enums implementados (9 enums):
- ✅ `AccountStatus` — ACTIVE, BLOCKED, CANCELLED
- ✅ `AccountType` — SAVINGS, CHECKING, PERSONAL, CORPORATE
- ✅ `AuditOperationType` — 9 valores: ACCOUNT_OPENING, DEPOSIT, WITHDRAWAL, TRANSFER_EXECUTED, TRANSFER_REJECTED, TRANSFER_EXPIRED, LOAN_APPROVAL, LOAN_REJECTION, LOAN_DISBURSEMENT
- ✅ `LoanStatus` — UNDER_REVIEW, APPROVED, REJECTED, DISBURSED, IN_DEFAULT, CANCELLED (6 estados)
- ✅ `LoanType` — CONSUMER, VEHICLE, MORTGAGE, CORPORATE
- ✅ `ProductCategory` — ACCOUNTS, LOANS, SERVICES
- ✅ `TransferStatus` — PENDING, AWAITING_APPROVAL, APPROVED, EXECUTED, REJECTED, EXPIRED (6 estados)
- ✅ `UserRole` — NATURAL_PERSON_CLIENT, COMPANY_CLIENT, TELLER_EMPLOYEE, COMMERCIAL_EMPLOYEE, COMPANY_OPERATIVE, COMPANY_SUPERVISOR, INTERNAL_ANALYST (7 roles)
- ✅ `UserStatus` — ACTIVE, INACTIVE, BLOCKED

**Faltantes:**
- ❌ `Currency` enum — `BankAccount.currency` es `String` (e.g., `"COP"`)
- ⚠️ `AuditLog.executorUserRole` es `String` en lugar de usar `UserRole` enum

### 4. Manejo de estados → 5/5
- ✅ `Transfer` — constructor auto-asigna `AWAITING_APPROVAL` si `amount > APPROVAL_THRESHOLD (10000.0)`; de lo contrario `PENDING`
- ✅ `Transfer.checkExpiration()` — detecta expiración si status es AWAITING_APPROVAL y han pasado más de 60 minutos; cambia a EXPIRED
- ✅ `Loan` — constructor inicializa `loanStatus = UNDER_REVIEW`
- ✅ `Loan.approve()`, `reject()`, `disburse()` — transiciones de estado con validaciones
- ✅ `BankAccount` — constructor inicializa `status = ACTIVE` y `openingDate = LocalDate.now()`
- ✅ `User` — constructor inicializa `status = ACTIVE`
- ✅ `CompanySupervisor.approveTransfer()` — solo aprueba si estado es `AWAITING_APPROVAL`

Excelente: múltiples entidades con estados bien definidos, inicializados automáticamente y con transiciones validadas.

### 5. Tipos de datos → 2/5
- ✅ `LocalDate` para fechas (openingDate, approvalDate, disbursementDate, birthDate)
- ✅ `LocalDateTime` para operaciones con hora (creationDate, approvalDate en Transfer, expirationDateTime)
- ✅ `boolean` para `requiresApproval` en BankingProduct
- ✅ `int` para `termMonths` en Loan
- ❌ `BankAccount.currentBalance` es `double` — debería ser `BigDecimal`
- ❌ `Loan.requestedAmount`, `approvedAmount`, `interestRate` son `double` — deberían ser `BigDecimal`
- ❌ `Transfer.amount` es `double` — debería ser `BigDecimal`
- ❌ `TransferDetail.amount`, `balanceBeforeOrigin`, `balanceAfterOrigin`, `balanceBeforeDestination`, `balanceAfterDestination` son `double`

El uso de `double` para TODOS los campos monetarios es el principal punto débil técnico del proyecto.

### 6. Separación Usuario vs Cliente → 3/5
- ✅ `User` como clase abstracta con atributos de acceso al sistema (username, passwordHash)
- ✅ `NaturalPersonClient extends User` — rol específico para cliente persona natural
- ✅ `UserRole` enum diferencia claramente `NATURAL_PERSON_CLIENT` de los roles de empleado
- ⚠️ La jerarquía mezcla el concepto de "usuario del sistema" con "cliente del banco" — `NaturalPersonClient` hereda las credenciales del sistema de `User`
- ⚠️ No existe clase concreta `CompanyClient` — los clientes empresariales no tienen representación en el modelo de dominio
- ⚠️ Un cliente y un empleado son ambos `User` sin una jerarquía intermedia que separe `SystemUser` de `BankClient`

La separación existe vía `UserRole` pero no hay separación estructural real con jerarquías independientes.

### 7. Bitácora → 4/5
- ✅ `AuditLog` con `AuditOperationType` enum que cubre 9 operaciones del dominio
- ✅ `operationDateTime = LocalDateTime.now()` auto-asignado en constructor
- ✅ `executorUserId`, `executorUserRole`, `affectedProductId` — contexto de quién hizo qué sobre qué
- ✅ Patrón de detail objects polimórficos: solo uno de `TransferDetail`, `LoanDetail`, `ExpirationDetail` tiene valor según la operación
- ✅ `TransferDetail` captura balances antes y después de origen y destino — muy detallado
- ✅ `LoanDetail` captura previousState, newState, analystId
- ✅ `ExpirationDetail` con reason hardcodeado para expiración de transferencias
- ✅ `Main.java` demuestra el uso correcto de AuditLog

**Observaciones:**
- ⚠️ `executorUserRole` es `String` en lugar de `UserRole` enum
- ⚠️ El patrón de detail objects con campos nulos implica que dos de los tres detail siempre son null

### 8. Reglas básicas de negocio → 5/5
Implementación más completa de reglas de negocio del grupo:
- ✅ `InternalAnalyst.approveLoan()` — valida que `loanStatus == UNDER_REVIEW` antes de aprobar; registra analista aprobador
- ✅ `InternalAnalyst.disburseLoan()` — triple validación: estado APPROVED + cuenta activa (`isActive()`) + cuenta pertenece al cliente del préstamo (`clientId`)
- ✅ `InternalAnalyst.rejectLoan()` — valida estado UNDER_REVIEW
- ✅ `CompanySupervisor.approveTransfer()` — valida estado `AWAITING_APPROVAL` + saldo suficiente en cuenta origen (`currentBalance >= amount`)
- ✅ `CompanySupervisor.rejectTransfer()` — valida estado AWAITING_APPROVAL
- ✅ `Transfer` constructor — auto-asigna estado según umbral de aprobación (`APPROVAL_THRESHOLD = 10000.0`)
- ✅ `Transfer.checkExpiration()` — detecta expiración de 60 minutos y cambia estado
- ✅ `TellerEmployee.deposit()` — valida cuenta activa antes de acreditar
- ✅ `TellerEmployee.withdrawal()` — valida cuenta activa y saldo suficiente
- ✅ `NaturalPersonClient.isOfLegalAge()` — regla de negocio bancaria (mayoría de edad)
- ✅ `Loan.disburse()` — registra fecha de desembolso automáticamente

### 9. Estructura del proyecto → 4/5
```
src/main/java/
  app/
    domain/
      enums/    ← 9 enums
      models/   ← 15 clases modelo + 3 detail objects
    BankApplication.java  ← Spring Boot entry point
    Main.java             ← Demo completa del dominio
```
- ✅ Separación clara `domain.enums` / `domain.models`
- ✅ `BankApplication.java` con `@SpringBootApplication`
- ✅ `Main.java` con demostración completa del flujo bancario
- ✅ Tests en `src/test/`
- ⚠️ Paquete raíz `app` demasiado genérico (debería ser `com.tdea.banking` o similar)
- ⚠️ Sin capa de servicios (toda la lógica está en los modelos de dominio) — aceptable para el alcance del ejercicio

### 10. Repositorio → 2/5
- ✅ Nombre `ConstruccionSoftwareII2026LM8` — incluye materia, año y grupo
- ✅ 3 autores identificados en README
- ⚠️ README contiene solo título y nombres de autores — sin descripción de la materia, tecnologías usadas ni instrucciones de compilación/ejecución
- ⚠️ 6 commits — ninguno sigue formato ADD/CHG/FIX
- ⚠️ Rama `develop` creada pero vacía (solo tiene bootstrap de Spring Boot) — todo el código está en `main`
- ❌ Sin tags de versión
- ❌ Mensaje de commit `"first comit"` (typo) y `"Creacion de clases y enums"` (en español)

---

## 📌 Puntos Fuertes
1. **Reglas de negocio más completas del grupo** — validaciones en cadena, umbrales automáticos, verificaciones de pertenencia
2. `Transfer` con `APPROVAL_THRESHOLD` como constante y auto-asignación de estado en constructor
3. `AuditLog` con detail objects polimórficos para diferentes tipos de operación
4. Jerarquía de 6 clases concretas de `User` cubriendo todos los roles del sistema
5. `Main.java` demuestra flujo completo de extremo a extremo

## ⚠️ Puntos a Mejorar
1. Cambiar `double` por `BigDecimal` en TODOS los campos monetarios
2. Agregar enum `Currency` y usarlo en `BankAccount.currency`
3. Crear clase concreta `CompanyClient` para clientes empresariales
4. Usar `UserRole` enum en `AuditLog.executorUserRole` (actualmente String)
5. Mejorar README con descripción del proyecto, tecnologías e instrucciones
6. Usar formato de commits ADD/CHG/FIX y agregar tags de versión
7. Trabajar en la rama `develop` y hacer merge a `main` cuando esté listo
