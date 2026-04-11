# EVALUACION 2 - ConstruccionSoftwareII2026LM8

## Informacion general
- Estudiante(s): Alexis Gonzalez Sanchez, Armando Esteban Gonzalez Velasquez, Samuel Barrera Quintero
- Rama evaluada: main
- Commit evaluado: c6c2b7b23bfd8c9b0828498f68b9a01b52b4ba96
- Fecha: 2026-04-11

## Tabla de calificacion

| Criterio | Peso | Puntaje (1-5) | Aporte |
|---|---|---|---|
| 1. Modelado de dominio | 20% | 5 | 1.00 |
| 2. Modelado de puertos | 20% | 1 | 0.20 |
| 3. Modelado de servicios de dominio | 20% | 1 | 0.20 |
| 4. Enums y estados | 10% | 5 | 0.50 |
| 5. Reglas de negocio criticas | 10% | 3 | 0.30 |
| 6. Bitacora y trazabilidad | 5% | 3 | 0.15 |
| 7. Estructura interna de dominio | 10% | 3 | 0.30 |
| 8. Calidad tecnica base en domain | 5% | 2 | 0.10 |
| **SUBTOTAL** | | | **2.75** |

## Penalizaciones
- **Codigo en espanol parcial (-10%):** Comentarios en espanol en entidades de dominio ("todo usuario empieza activo", "Metodos de negocio", "Regla de negocio").

Calculo: 2.75 x 0.90 = **2.48**

## Bonus
- Ninguno (sin puertos ni servicios de dominio).

## Nota final
**2.5 / 5.0**

---

## Hallazgos

### Positivos
- **Modelo de dominio muy completo:** 15 entidades con jerarquia extensa.
  - Clientes: `NaturalPersonClient`, `CompanyClient`.
  - Empleados: `TellerEmployee`, `CommercialEmployee`, `CompanyOperative`, `CompanySupervisor`, `InternalAnalyst`.
  - Productos: `BankAccount`, `BankingProduct`, `Loan`, `Transfer`.
  - Soporte: `AuditLog`, `TransferDetail`, `LoanDetail`, `ExpirationDetail`.
- **9+ enums correctos en ingles:** `UserRole` (7 valores), `LoanStatus` (6: UNDER_REVIEW, APPROVED, REJECTED, DISBURSED, IN_DEFAULT, CANCELLED), `TransferStatus` (6: PENDING, AWAITING_APPROVAL, APPROVED, EXECUTED, REJECTED, EXPIRED), `AccountStatus` (ACTIVE, BLOCKED, CANCELLED), `AuditOperationType`, y mas.
- Constante de umbral de aprobacion (`APPROVAL_THRESHOLD = 10000.00`) y tiempo de expiracion (`EXPIRATION_MINUTES = 60`) definidos en el modelo de `Transfer`.
- Logica de transicion de estados en entidades: `approve()`, `reject()`, `disburse()` en `Loan`.

### Negativos
- **No existen puertos.** Sin interfaces de salida para persistencia. Critico.
- **No existen servicios de dominio.** No hay enforcement de flujos de caso de uso.
- Uso de `double` para montos monetarios en lugar de `BigDecimal`. En sistemas financieros esto es un defecto de precision.
- Comentarios en espanol en entidades ("todo usuario empieza activo", etc.). Penaliza -10%.
- `AuditLog` presente pero sin port; no hay mecanismo de persistencia definido en dominio.

## Recomendaciones
1. Crear puertos de salida por agregado.
2. Implementar servicios de dominio por caso de uso.
3. Cambiar todos los `double` a `BigDecimal` para montos.
4. Reemplazar comentarios en espanol por comentarios en ingles o eliminarlos.
5. La amplitud del modelo es un punto fuerte; falta completarlo con las capas de puertos y servicios.
