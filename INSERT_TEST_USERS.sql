-- ============================================
-- SCRIPT DE INSERCIÓN - USUARIOS DE PRUEBA
-- PARA TESTING MANUAL DEL SISTEMA JWT
-- ============================================
-- Base de Datos: BankDB
-- IMPORTANTE: Ejecutar en SQL Server Management Studio
-- Password hasheado: $2a$10$slYQmyNdGzin7olVMwhK.OPST9/PgBkqquzi.Ss74KUgO7jNCRFom
-- Corresponde a: password123

USE BankDB;

-- Limpiar usuarios anteriores (OPCIONAL - comentar si quieres preservar)
-- DELETE FROM [users];

-- ============================================
-- 1. COMMERCIAL_EMPLOYEE (Acceso a Clientes)
-- ============================================
INSERT INTO [users]
(id, username, password_hash, role, status, email, phone, address, birth_date, created_at, updated_at)
VALUES
(
    'USR-COMM-001',
    'juan_perez',
    '$2a$10$slYQmyNdGzin7olVMwhK.OPST9/PgBkqquzi.Ss74KUgO7jNCRFom',
    'COMMERCIAL_EMPLOYEE',
    'ACTIVE',
    'juan.perez@bank.com',
    '+57 320 123 4567',
    'Calle 123 #45-67, Bogotá',
    '1990-05-15',
    GETDATE(),
    GETDATE()
);

-- ============================================
-- 2. INTERNAL_ANALYST (Aprobación de Préstamos)
-- ============================================
INSERT INTO [users]
(id, username, password_hash, role, status, email, phone, address, birth_date, created_at, updated_at)
VALUES
(
    'USR-ANAL-002',
    'ana_analyst',
    '$2a$10$slYQmyNdGzin7olVMwhK.OPST9/PgBkqquzi.Ss74KUgO7jNCRFom',
    'INTERNAL_ANALYST',
    'ACTIVE',
    'ana.analyst@bank.com',
    '+57 320 987 6543',
    'Calle 456 #78-90, Medellín',
    '1991-08-22',
    GETDATE(),
    GETDATE()
);

-- ============================================
-- 3. TELLER_EMPLOYEE (Solo Cuentas)
-- ============================================
INSERT INTO [users]
(id, username, password_hash, role, status, email, phone, address, birth_date, created_at, updated_at)
VALUES
(
    'USR-TELL-003',
    'carlos_teller',
    '$2a$10$slYQmyNdGzin7olVMwhK.OPST9/PgBkqquzi.Ss74KUgO7jNCRFom',
    'TELLER_EMPLOYEE',
    'ACTIVE',
    'carlos.teller@bank.com',
    '+57 320 555 7890',
    'Calle 789 #12-34, Cali',
    '1992-03-10',
    GETDATE(),
    GETDATE()
);

-- ============================================
-- 4. COMPANY_SUPERVISOR (Supervisión)
-- ============================================
INSERT INTO [users]
(id, username, password_hash, role, status, email, phone, address, birth_date, created_at, updated_at)
VALUES
(
    'USR-SUPER-004',
    'luis_supervisor',
    '$2a$10$slYQmyNdGzin7olVMwhK.OPST9/PgBkqquzi.Ss74KUgO7jNCRFom',
    'COMPANY_SUPERVISOR',
    'ACTIVE',
    'luis.supervisor@bank.com',
    '+57 320 666 8901',
    'Calle 012 #34-56, Barranquilla',
    '1989-11-05',
    GETDATE(),
    GETDATE()
);

-- ============================================
-- 5. NATURAL_PERSON_CLIENT (Cliente)
-- ============================================
INSERT INTO [users]
(id, username, password_hash, role, status, email, phone, address, birth_date, created_at, updated_at)
VALUES
(
    'USR-CLI-005',
    'cliente_juan',
    '$2a$10$slYQmyNdGzin7olVMwhK.OPST9/PgBkqquzi.Ss74KUgO7jNCRFom',
    'NATURAL_PERSON_CLIENT',
    'ACTIVE',
    'cliente@example.com',
    '+57 320 444 2345',
    'Calle 345 #56-78, Bucaramanga',
    '1985-07-14',
    GETDATE(),
    GETDATE()
);

-- ============================================
-- Verificar usuarios creados
-- ============================================
SELECT
    id,
    username,
    role,
    status,
    email,
    created_at
FROM [users]
ORDER BY created_at DESC;

-- ============================================
-- NOTAS IMPORTANTES
-- ============================================
-- ✅ Todos los usuarios tienen password: password123
-- ✅ Todos los usuarios están ACTIVOS
-- ✅ Puedes cambiar el password hasheado para usar otro
-- ✅ Para generar nuevo hash BCrypt: https://www.bcryptoniline.com/
-- ✅ Para cambiar rol: UPDATE [users] SET role = 'NUEVO_ROL' WHERE username = 'juan_perez'
-- ✅ Para desactivar usuario: UPDATE [users] SET status = 'INACTIVE' WHERE username = 'juan_perez'

-- ============================================
-- ROLES DISPONIBLES
-- ============================================
-- 1. NATURAL_PERSON_CLIENT      - Cliente persona natural
-- 2. COMPANY_CLIENT             - Cliente empresa
-- 3. TELLER_EMPLOYEE            - Empleado de caja
-- 4. COMMERCIAL_EMPLOYEE        - Empleado comercial
-- 5. COMPANY_OPERATIVE          - Operativo de empresa
-- 6. COMPANY_SUPERVISOR         - Supervisor de empresa
-- 7. INTERNAL_ANALYST           - Analista interno
