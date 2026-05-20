#!/bin/bash
# ========================================
# Script Rápido para Iniciar Pruebas JWT
# ========================================

echo "🚀 INICIANDO SISTEMA BANCARIO CON JWT"
echo "=========================================="
echo ""

# Colores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Variables
PROJECT_PATH="ConstruccionSoftwareII2026LM8/cs2"
PORT=8080

echo -e "${BLUE}📋 CHECKLIST DE CONFIGURACIÓN${NC}"
echo ""

# 1. Verificar si Maven está instalado
echo -n "1. Verificando Maven... "
if command -v mvn &> /dev/null; then
    echo -e "${GREEN}✅ Maven encontrado${NC}"
else
    echo -e "${YELLOW}⚠️  Maven no encontrado. Instálalo o usa tu IDE${NC}"
fi

# 2. Verificar si la BD está conectada
echo -n "2. Verificando conexión a SQL Server... "
# Esto es solo un placeholder, en Windows verificarías con sqlcmd
echo -e "${YELLOW}⚠️  Verifica manualmente en SQL Server Management Studio${NC}"

# 3. Verificar si MongoDB está corriendo (si lo usas)
echo -n "3. Verificando MongoDB (opcional)... "
if command -v mongod &> /dev/null; then
    echo -e "${GREEN}✅ MongoDB encontrado${NC}"
else
    echo -e "${YELLOW}ℹ️  MongoDB no requerido si usas solo SQL Server${NC}"
fi

echo ""
echo -e "${BLUE}📝 PRÓXIMOS PASOS:${NC}"
echo ""
echo "1️⃣  Insertar usuarios de prueba en BD:"
echo "   - Abre: SQL Server Management Studio"
echo "   - Conecta a: SQLEXPRESS"
echo "   - BD: BankDB"
echo "   - Ejecuta: INSERT_TEST_USERS.sql"
echo ""
echo "2️⃣  Iniciar aplicación Spring Boot:"
echo "   - Opción A (Terminal/Git Bash):"
echo "     cd $PROJECT_PATH"
echo "     mvn clean spring-boot:run"
echo ""
echo "   - Opción B (IDE):"
echo "     Click derecho → Run 'Application'"
echo ""
echo "3️⃣  Cuando veas 'Tomcat started on port(s): $PORT' ✅"
echo ""
echo -e "${YELLOW}4️⃣  IMPORTAR POSTMAN:${NC}"
echo "   - Abre Postman"
echo "   - Import → File → JWT_BANKING_POSTMAN_COLLECTION.json"
echo ""
echo "5️⃣  COMENZAR PRUEBAS:"
echo "   - Ejecuta los tests en orden:"
echo "     • SETUP - Guardar Token Commercial"
echo "     • SETUP - Guardar Token Analyst"
echo "     • SETUP - Guardar Token Teller"
echo "     • LOGIN - COMMERCIAL_EMPLOYEE"
echo "     • GET Clients CON Token Válido"
echo "     • GET Clients SIN Token"
echo "     • ... y más"
echo ""
echo "=========================================="
echo -e "${GREEN}¡Presiona Enter cuando todo esté listo!${NC}"
read

# Verificar que la app está corriendo
echo ""
echo -e "${BLUE}✓ Verificando que la app está disponible...${NC}"
echo ""

max_attempts=30
attempt=1

while [ $attempt -le $max_attempts ]; do
    response=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:$PORT/api/auth/login 2>/dev/null)

    if [ "$response" = "405" ]; then
        echo -e "${GREEN}✅ App está corriendo en http://localhost:$PORT${NC}"
        echo ""
        echo -e "${YELLOW}URLs importantes:${NC}"
        echo "  • Base: http://localhost:$PORT"
        echo "  • Login: http://localhost:$PORT/api/auth/login"
        echo "  • Clients: http://localhost:$PORT/api/clients/"
        echo "  • Transfers: http://localhost:$PORT/api/transfers"
        echo ""
        echo -e "${GREEN}🎉 ¡LISTO PARA PRUEBAS!${NC}"
        exit 0
    fi

    if [ $((attempt % 10)) -eq 0 ]; then
        echo "⏳ Esperando app... ($attempt/$max_attempts)"
    fi

    sleep 1
    ((attempt++))
done

echo -e "${YELLOW}⚠️  La app no respondió después de 30 segundos${NC}"
echo "Verifica:"
echo "  1. ¿Está corriendo Spring Boot?"
echo "  2. ¿Puerto 8080 está disponible?"
echo "  3. ¿Hay errores en la consola?"
