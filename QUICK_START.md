# 🚀 Quick Start - Sistema Bancario con BD

## 📋 Checklist Rápido

### Paso 1: Instalar SQL Server ✅
```
1. Descarga: https://www.microsoft.com/es-es/sql-server/sql-server-downloads
2. Instala SQL Server 2022 Express
3. Mixed Mode Authentication
   - Usuario: sa
   - Contraseña: YourPassword123!
4. Verifica en Services (services.msc) → SQL Server está "Running"
```

### Paso 2: Instalar MongoDB ✅
```
1. Descarga: https://www.mongodb.com/try/download/community
2. Instala MongoDB Community Edition
3. Desactiva antivirus temporalmente si tiene problemas
4. Verifica en Services → MongoDB Server está "Running"
```

### Paso 3: Crear Base de Datos SQL Server ✅
```
1. Abre SQL Server Management Studio
2. Conecta:
   - Server: localhost\SQLEXPRESS
   - Auth: SQL Server Authentication
   - Login: sa
   - Password: YourPassword123!
3. Nueva Query y ejecuta:

CREATE DATABASE BankDB;
GO

4. Verifica que BankDB aparece en la lista
```

### Paso 4: Crear Base de Datos MongoDB ✅
```
1. Abre MongoDB Compass (se instaló con MongoDB)
2. Conecta automáticamente en localhost:27017
3. Click en "+" → Create Database
   - Database: bank_db
   - Collection: users
4. Listo (no necesitas crear tablas manualmente)
```

### Paso 5: Verificar Credenciales en application.yml ✅
```yaml
# Archivo: cs2/src/main/resources/application.yml
# Verifica que está así:

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

### Paso 6: Ejecutar la Aplicación ✅
```bash
# Abre Git Bash o PowerShell

# Navega a la carpeta del proyecto
cd "C:\Users\miguel angel\Desktop\bank\ConstruccionSoftwareII2026LM8\cs2"

# Limpiar y construir
mvn clean package -DskipTests

# Ejecutar
mvn spring-boot:run

# Deberías ver:
# Started BankApplication in XX seconds
# Tomcat started on port(s): 8080
```

### Paso 7: Probar que Funciona ✅
```
1. Abre navegador: http://localhost:8080/api/
2. Deberías recibir respuesta (aunque sea error 404)
3. Verifica en SQL Server Management Studio:
   - BankDB → Tables
   - Deberías ver las tablas creadas
```

---

## 🔍 Verificar que Está Todo Conectado

### Terminal 1: Verifica SQL Server
```powershell
# PowerShell como Administrador
sqlcmd -S localhost\SQLEXPRESS -U sa -P YourPassword123!
# Deberías ver: 1>
# Escribe: quit
```

### Terminal 2: Verifica MongoDB
```powershell
mongosh
# Deberías ver: test>
# Escribe: exit
```

### Terminal 3: Ejecuta la App
```bash
cd "C:\Users\miguel angel\Desktop\bank\ConstruccionSoftwareII2026LM8\cs2"
mvn spring-boot:run
```

---

## 📊 Estructura de Datos

### SQL Server (Datos Transaccionales):
- ✅ users → Usuarios del sistema
- ✅ bank_accounts → Cuentas bancarias
- ✅ loans → Préstamos
- ✅ transfers → Transferencias

### MongoDB (Datos de Auditoría):
- ✅ audit_logs → Registros de auditoría
- ✅ Flexible para futuras colecciones

---

## ❌ Si Algo No Funciona

### "SQL Server no conecta"
```powershell
# Verifica que el servicio está corriendo
Get-Service MSSQLSERVER
# Debería mostrar: Running

# Si no está, inicia el servicio
Start-Service MSSQLSERVER
```

### "MongoDB no conecta"
```powershell
# Verifica que el servicio está corriendo
Get-Service MongoDB
# Debería mostrar: Running

# Si no está, inicia el servicio
Start-Service MongoDB
```

### "Puerto 1433 en uso"
```powershell
# Verifica qué está usando el puerto
netstat -ano | findstr 1433
# Debería ser sqlservr.exe (SQL Server)
```

### "Error de conexión en Spring Boot"
```
1. Verifica que ambos servicios están corriendo
2. Verifica credenciales en application.yml
3. Verifica que BankDB existe en SQL Server
4. Revisa los logs de la aplicación para detalles
```

---

## 📁 Archivos Creados

```
bank/
├── DATABASE_CONNECTIONS_GUIDE.md         # Documentación completa
├── INSTALACION_BD_WINDOWS.md             # Guía instalación Windows
├── cs2/
│   ├── pom.xml                           # ✅ Actualizado con dependencias
│   ├── src/main/resources/
│   │   ├── application.yml               # ✅ Creado con configuración
│   │   └── schema_sqlserver.sql          # Script SQL para crear tablas
│   └── src/main/java/app/application/adapter/persistence/
│       ├── sqlserver/                    # ✅ 11 archivos SQL Server
│       └── mongodb/                      # ✅ 11 archivos MongoDB
```

---

## ✅ Próximos Pasos (Opcional)

Una vez todo funcione:
1. **Crear controladores REST** para probar las operaciones
2. **Escribir tests de integración** con ambas BDs
3. **Implementar migraciones** con Liquibase o Flyway
4. **Configurar logging** con SLF4J

---

## 🆘 ¿Necesitas Ayuda?

Si tienes problemas:
1. Revisa los logs de la aplicación
2. Verifica que ambos servicios están corriendo
3. Asegúrate que las credenciales en application.yml son correctas
4. Prueba conectar manualmente a cada BD

¿Qué paso necesitas hacer primero?
