# EVALUACION 2 - ConstruccionSoftwareII2026LM8

## Informacion general
- Estudiante(s): Alexis Gonzalez Sanchez, Armando Esteban Gonzalez Velasquez, Samuel Barrera Quintero
- Rama evaluada: main
- Commit evaluado: 169b78b0520cce196a5c854690e8855238467686
- Fecha: 2026-04-11

## Tabla de calificacion

| Criterio | Peso | Puntaje (1-5) | Aporte |
|---|---|---|---|
| 1. Modelado de dominio | 20% | 3 | 0.60 |
| 2. Modelado de puertos | 20% | 1 | 0.20 |
| 3. Modelado de servicios de dominio | 20% | 2 | 0.40 |
| 4. Enums y estados | 10% | 5 | 0.50 |
| 5. Reglas de negocio criticas | 10% | 3 | 0.30 |
| 6. Bitacora y trazabilidad | 5% | 4 | 0.20 |
| 7. Estructura interna de dominio | 10% | 3 | 0.30 |
| 8. Calidad tecnica base en domain | 5% | 1 | 0.05 |
| **SUBTOTAL** | | | **2.55** |

## Penalizaciones
- **Espanol parcial en comentarios (-5%):** presencia de comentarios de dominio en espanol.

Calculo: 2.55 x 0.95 = **2.42**

## Bonus
- Ninguno.

## Nota final
**2.4 / 5.0**

---

## Hallazgos

### Positivos
- Modelo de dominio amplio con buena cobertura de entidades y estados.
- Conjunto de enums robusto, incluyendo tipos de auditoria y estados de prestamos/transferencias.
- Existen reglas de estado implementadas dentro de entidades.

### Negativos
- **No hay puertos** para persistencia ni contratos de salida.
- **No hay servicios de dominio** por casos de uso.
- Uso de `double` en montos monetarios (riesgo de precision financiera).
- Comentarios en espanol dentro de la capa de dominio.

## Recomendaciones
1. Definir puertos por agregado y servicios por caso de uso.
2. Migrar `double` a `BigDecimal` en montos y tasas.
3. Mantener consistencia de idioma tecnico en ingles dentro de dominio.
