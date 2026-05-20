#!/bin/bash
# ============================================
# QUICK REFERENCE - Comandos cURL para Testing
# ============================================
# Copiar y ejecutar en Git Bash o Terminal
# ============================================

# 🟢 SETUP: Guardar Token en Variable
# ============================================

echo "🟢 PASO 1: LOGIN - Obtener Token"
echo "=================================="

TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "juan_perez",
    "password": "password123"
  }' | grep -o '"token":"[^"]*' | cut -d'"' -f4)

echo "✅ Token guardado en variable: \$TOKEN"
echo "Token: ${TOKEN:0:80}..."
echo ""

# 🟢 TEST 1: Acceso a Endpoint CON Token Válido (200 OK)
# ============================================

echo "🟢 TEST 1: GET /clients WITH Token (200 OK)"
echo "=============================================="

curl -X GET http://localhost:8080/api/clients/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -s | jq .

echo ""
echo "Expected: 200 OK - Resource returned"
echo ""

# 🔴 TEST 2: Acceso a Endpoint SIN Token (401 Unauthorized)
# ============================================

echo "🔴 TEST 2: GET /clients WITHOUT Token (401)"
echo "============================================"

curl -X GET http://localhost:8080/api/clients/1 \
  -H "Content-Type: application/json" \
  -s | jq .

echo ""
echo "Expected: 401 - No autorizado - Token inválido o ausente"
echo ""

# 🔴 TEST 3: Token Inválido (401 Unauthorized)
# ============================================

echo "🔴 TEST 3: GET /clients WITH Invalid Token (401)"
echo "=================================================="

curl -X GET http://localhost:8080/api/clients/1 \
  -H "Authorization: Bearer invalid.token.xyz.fake" \
  -H "Content-Type: application/json" \
  -s | jq .

echo ""
echo "Expected: 401 - No autorizado - Token inválido o ausente"
echo ""

# 🔴 TEST 4: Sin Permisos (403 Forbidden)
# ============================================

echo "🔴 TEST 4: GET /clients WITHOUT Permission (403)"
echo "================================================="

# Primero obtener token de TELLER_EMPLOYEE
TOKEN_TELLER=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "carlos_teller",
    "password": "password123"
  }' | grep -o '"token":"[^"]*' | cut -d'"' -f4)

echo "Token de TELLER obtenido: ${TOKEN_TELLER:0:50}..."

# Intentar acceder a /clients (requiere COMMERCIAL_EMPLOYEE)
curl -X GET http://localhost:8080/api/clients/1 \
  -H "Authorization: Bearer $TOKEN_TELLER" \
  -H "Content-Type: application/json" \
  -s | jq .

echo ""
echo "Expected: 403 - Acceso denegado - No tienes los permisos requeridos"
echo ""

# ✅ TEST 5: TELLER_EMPLOYEE accediendo a /accounts (permitido)
# ============================================

echo "✅ TEST 5: GET /accounts WITH TELLER Token (200 OK)"
echo "===================================================="

curl -X GET http://localhost:8080/api/accounts/1 \
  -H "Authorization: Bearer $TOKEN_TELLER" \
  -H "Content-Type: application/json" \
  -s | jq .

echo ""
echo "Expected: 200 OK - TELLER_EMPLOYEE puede acceder a cuentas"
echo ""

# ✅ TEST 6: LOGIN como INTERNAL_ANALYST
# ============================================

echo "✅ TEST 6: LOGIN as INTERNAL_ANALYST"
echo "====================================="

TOKEN_ANALYST=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "ana_analyst",
    "password": "password123"
  }' | grep -o '"token":"[^"]*' | cut -d'"' -f4)

echo "✅ Token de ANALYST obtenido: ${TOKEN_ANALYST:0:50}..."
echo ""

# ✅ TEST 7: ANALYST aprobando préstamo (POST con permiso)
# ============================================

echo "✅ TEST 7: POST /loans/approve WITH ANALYST Token (200/Success)"
echo "=============================================================="

curl -X POST http://localhost:8080/api/loans/LOAN001/approve \
  -H "Authorization: Bearer $TOKEN_ANALYST" \
  -H "Content-Type: application/json" \
  -d '{"reason": "Aprobado por demostración"}' \
  -s | jq .

echo ""
echo "Expected: 200 OK (o estructura del préstamo aprobado)"
echo ""

# 🔴 TEST 8: COMMERCIAL_EMPLOYEE intentando aprobar préstamo (403)
# ============================================

echo "🔴 TEST 8: POST /loans/approve WITH COMMERCIAL Token (403)"
echo "=========================================================="

curl -X POST http://localhost:8080/api/loans/LOAN001/approve \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"reason": "Sin permisos"}' \
  -s | jq .

echo ""
echo "Expected: 403 - Acceso denegado - COMMERCIAL no puede aprobar"
echo ""

# ✅ TEST 9: Ver el contenido del Token (decodificar localmente)
# ============================================

echo "✅ TEST 9: Decodificar Token JWT"
echo "================================"

# Extraer el payload (segunda parte)
PAYLOAD=$(echo $TOKEN | cut -d'.' -f2)

# Agregar padding si es necesario
PADDING=$((4 - ${#PAYLOAD} % 4))
if [ $PADDING -ne 4 ]; then
    PAYLOAD="${PAYLOAD}$(printf '%*s' $PADDING | tr ' ' '=')"
fi

# Decodificar base64
echo "Payload decodificado:"
echo $PAYLOAD | base64 -d | jq .

echo ""
echo "Expected: JSON con sub, document, role, iat, exp"
echo ""

# ============================================
# RESUMEN DE TESTS
# ============================================

echo ""
echo "📊 RESUMEN DE TESTS EJECUTADOS"
echo "=============================="
echo ""
echo "✅ TEST 1: GET /clients CON Token → 200 OK"
echo "❌ TEST 2: GET /clients SIN Token → 401"
echo "❌ TEST 3: GET /clients Token INVÁLIDO → 401"
echo "❌ TEST 4: GET /clients TELLER sin permiso → 403"
echo "✅ TEST 5: GET /accounts TELLER → 200 OK"
echo "✅ TEST 6: LOGIN ANALYST → Token obtenido"
echo "✅ TEST 7: POST /loans/approve ANALYST → Aprobado"
echo "❌ TEST 8: POST /loans/approve COMMERCIAL → 403"
echo "ℹ️  TEST 9: JWT Decodificado → Payload visible"
echo ""
echo "🎉 TODOS LOS TESTS COMPLETADOS"

# ============================================
# VARIABLES DISPONIBLES PARA USAR
# ============================================
# $TOKEN                - Token de COMMERCIAL_EMPLOYEE
# $TOKEN_TELLER         - Token de TELLER_EMPLOYEE
# $TOKEN_ANALYST        - Token de INTERNAL_ANALYST
# ============================================
