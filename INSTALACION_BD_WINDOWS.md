# 🪟 Instalación de Bases de Datos en Windows (Sin Docker)

## 1️⃣ SQL Server 2022 Express (Recomendado)

### Descargar:
1. Ve a: https://www.microsoft.com/es-es/sql-server/sql-server-downloads
2. Descarga **SQL Server 2022 Express** (es gratis)
3. Ejecuta el instalador `.exe`

### Pasos de Instalación:

**Paso 1: Instalación Básica**
- Selecciona "New SQL Server stand-alone installation"
- Acepta los términos
- En "Feature Selection": Selecciona:
  - ✅ Database Engine Services
  - ✅ SQL Server Replication
  - (Deja el resto por defecto)

**Paso 2: Configuración de la Instancia**
- Named Instance: **SQLEXPRESS** (por defecto está bien)
- Service Account: **NT SERVICE\MSSQLSERVER**
- Authentication Mode: **Mixed Mode**
  - SA Password: `YourPassword123!` (puedes cambiar esto)

**Paso 3: Verificar Instalación**

Abre **SQL Server Management Studio** (descárgalo si no lo tienes):
- https://learn.microsoft.com/es-es/sql/ssms/download-sql-server-management-studio-ssms

Conecta con:
- Server name: `localhost` o `localhost\SQLEXPRESS`
- Authentication: SQL Server Authentication
- Login: `sa`
- Password: `YourPassword123!`

---

## 2️⃣ MongoDB Community Edition

### Descargar:
1. Ve a: https://www.mongodb.com/try/download/community
2. Selecciona:
   - **Version**: Latest (o 7.0 LTS)
   - **OS**: Windows 64-bit
   - **Package**: MSI
3. Descarga y ejecuta el `.msi`

### Pasos de Instalación:

**Paso 1: Instalador**
- Next, Next, Next...
- ✅ Install MongoDB as a Service
- ✅ Install MongoDB Compass (herramienta visual)

**Paso 2: Verificar Instalación**

Abre **MongoDB Compass** (se instala automáticamente):
- Conexión por defecto: `mongodb://localhost:27017`
- Debería conectar sin problemas

O usa PowerShell:
```powershell
mongosh
# Deberías ver: test> 
```

---

## ✅ Verificar Conexiones

### Test SQL Server:

Usa una herramienta como **DBeaver** (gratuita):
1. Descarga: https://dbeaver.io/download/
2. New Database Connection → SQL Server
3. Configuración:
   - Host: `localhost`
   - Port: `1433`
   - Username: `sa`
   - Password: `YourPassword123!`
   - Database: (dejar vacío)

### Test MongoDB:

**MongoDB Compass** viene con la instalación - solo abre la aplicación.

O PowerShell:
```powershell
mongosh
> show dbs
> use bank_db
> db.users.insertOne({test: "ok"})
> db.users.find()
```

---

## 🔧 Configurar Application.yml

Una vez instaladas las BDs, tu `application.yml` ya tiene:

```yaml
spring:
  datasource:
    url: jdbc:sqlserver://localhost:1433;databaseName=BankDB;encrypt=true;trustServerCertificate=true
    username: sa
    password: YourPassword123!
    driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver

  data:
    mongodb:
      uri: mongodb://localhost:27017/bank_db
```

### Crear la BD en SQL Server:

Abre **SQL Server Management Studio** y ejecuta:

```sql
CREATE DATABASE BankDB;
GO

USE BankDB;
GO

-- Las tablas se crearán automáticamente con Hibernate (ddl-auto: update)
```

### Crear la BD en MongoDB:

Con MongoDB Compass:
1. Click en "+" al lado de "Databases"
2. Database Name: `bank_db`
3. Collection Name: `users`
4. Create Database

---

## 🚀 Iniciar tu Aplicación Spring

Una vez todo instalado:

```bash
cd c:\Users\miguel\ angel\Desktop\bank\ConstruccionSoftwareII2026LM8\cs2

mvn clean package
mvn spring-boot:run
```

La aplicación:
- ✅ Creará automáticamente las tablas en SQL Server
- ✅ Conectará a MongoDB
- ✅ Los adaptadores funcionarán sin problemas

---

## 📋 Checklist de Instalación

### SQL Server:
- [ ] Descargué SQL Server 2022 Express
- [ ] Instalé con Mixed Mode Authentication
- [ ] Usuario `sa` con contraseña configurada
- [ ] SQL Server Management Studio instalado
- [ ] Puedo conectar con sa/password

### MongoDB:
- [ ] Descargué MongoDB Community Edition
- [ ] Instalé como servicio
- [ ] MongoDB Compass instalado
- [ ] Puedo conectar en localhost:27017

### Aplicación:
- [ ] application.yml tiene credenciales correctas
- [ ] Ejecuté `mvn clean package`
- [ ] La aplicación levanta sin errores de conexión

---

## ❌ Problemas Comunes

### "SQL Server no se conecta"
```
Solución:
1. Verifica que SQL Server está corriendo: 
   - Abre "Services" (services.msc)
   - Busca "SQL Server (SQLEXPRESS)"
   - Debe estar en estado "Running"
2. Verifica el puerto: netstat -ano | findstr 1433
3. Cambia contraseña sa si no recuerdas
```

### "MongoDB no responde"
```
Solución:
1. Verifica que MongoDB está corriendo:
   - services.msc → busca "MongoDB Server"
   - Debe estar "Running"
2. Intenta conectar: mongosh
3. Si falla, reinicia el servicio
```

### "Port 1433 en uso"
```
Solución:
1. Cambia el puerto en SQL Server Configuration Manager
2. Actualiza application.yml con el nuevo puerto
```

---

## 📚 Recursos Útiles

- **SQL Server Management Studio**: https://learn.microsoft.com/sql/ssms/
- **MongoDB Compass**: Viene con la instalación
- **DBeaver**: https://dbeaver.io/ (gestor de BDs visual)
- **mongosh**: Cliente línea de comandos MongoDB

---

## ✅ Una vez instalado, ¿qué sigue?

1. Prueba que ambas BDs están corriendo
2. Ejecuta tu aplicación Spring Boot
3. Los adaptadores que creé se usarán automáticamente
4. Puedes empezar a guardar datos en ambas BDs

¿Necesitas ayuda con algún paso específico de instalación?
