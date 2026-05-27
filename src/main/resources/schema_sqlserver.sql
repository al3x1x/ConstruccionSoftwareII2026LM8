-- ============================================================
-- SQL Server - Script de Creación de Tablas para Sistema Bancario
-- ============================================================
-- Ejecutar en SQL Server Management Studio
-- Database: BankDB
-- ============================================================

-- 1. TABLA: users
-- ============================================================
-- IMPORTANTES: Campos deben coincidir con modelo Java
-- NOTA: Hibernate convierte camelCase a snake_case automáticamente
-- ============================================================
CREATE TABLE users (
    user_id VARCHAR(36) PRIMARY KEY,
    user_type VARCHAR(50) NOT NULL,  -- Para herencia SINGLE_TABLE (DISCRIMINATOR)
    full_name NVARCHAR(255) NOT NULL,
    identification_number VARCHAR(50) UNIQUE NOT NULL,
    email NVARCHAR(255) UNIQUE NOT NULL,
    phone NVARCHAR(20),
    birth_date DATE NOT NULL,
    address NVARCHAR(500),
    role VARCHAR(50) NOT NULL,  -- ENUM: NATURAL_PERSON_CLIENT, COMPANY_CLIENT, TELLER_EMPLOYEE, COMMERCIAL_EMPLOYEE, COMPANY_OPERATIVE, COMPANY_SUPERVISOR, INTERNAL_ANALYST
    status VARCHAR(50) NOT NULL,  -- ENUM: ACTIVE, INACTIVE, SUSPENDED
    username VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME NULL
);

-- 2. TABLA: bank_accounts
CREATE TABLE bank_accounts (
    account_number VARCHAR(36) PRIMARY KEY,
    holder_id VARCHAR(36) NOT NULL,
    account_type VARCHAR(50) NOT NULL,
    balance DECIMAL(19, 2) NOT NULL DEFAULT 0.00,
    status VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    FOREIGN KEY (holder_id) REFERENCES users(user_id)
);

-- 3. TABLA: loans
CREATE TABLE loans (
    loan_id VARCHAR(36) PRIMARY KEY,
    client_id VARCHAR(36) NOT NULL,
    loan_type VARCHAR(50) NOT NULL,
    principal_amount DECIMAL(19, 2) NOT NULL,
    interest_rate DECIMAL(5, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    requested_at DATETIME NOT NULL,
    approved_at DATETIME NULL,
    disbursed_at DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT GETDATE(),
    updated_at DATETIME NULL,
    FOREIGN KEY (client_id) REFERENCES users(user_id)
);

-- 4. TABLA: transfers
CREATE TABLE transfers (
    transfer_id VARCHAR(36) PRIMARY KEY,
    origin_account VARCHAR(36) NOT NULL,
    destination_account VARCHAR(36) NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT GETDATE(),
    executed_at DATETIME NULL,
    updated_at DATETIME NULL,
    description NVARCHAR(500) NULL,
    FOREIGN KEY (origin_account) REFERENCES bank_accounts(account_number),
    FOREIGN KEY (destination_account) REFERENCES bank_accounts(account_number)
);

-- 5. TABLA: audit_logs
CREATE TABLE audit_logs (
    log_id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) NOT NULL,
    operation_type VARCHAR(50) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    entity_id VARCHAR(36) NOT NULL,
    description NVARCHAR(500) NULL,
    ip_address VARCHAR(50) NULL,
    timestamp DATETIME NOT NULL DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- ============================================================
-- CREAR ÍNDICES PARA OPTIMIZAR CONSULTAS
-- ============================================================

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_identification ON users(identification_number);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_status ON users(status);
CREATE INDEX idx_bank_accounts_holder ON bank_accounts(holder_id);
CREATE INDEX idx_bank_accounts_status ON bank_accounts(status);
CREATE INDEX idx_loans_client ON loans(client_id);
CREATE INDEX idx_loans_status ON loans(status);
CREATE INDEX idx_transfers_origin ON transfers(origin_account);
CREATE INDEX idx_transfers_destination ON transfers(destination_account);
CREATE INDEX idx_transfers_status ON transfers(status);
CREATE INDEX idx_audit_logs_user ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_operation ON audit_logs(operation_type);

-- ============================================================
-- VERIFICAR CREACIÓN
-- ============================================================

SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'dbo';

-- Deberías ver:
-- users
-- bank_accounts
-- loans
-- transfers
-- audit_logs
