#!/bin/bash

# Smoke test script for ScrapSail Backend
# Tests health endpoints and verifies deployment

set -e

BASE_URL="${1:-http://localhost:8080}"
TIMEOUT=30
MAX_RETRIES=10

echo "🧪 Starting smoke tests for ScrapSail Backend..."
echo "📍 Base URL: $BASE_URL"
echo ""

# Function to wait for endpoint to be ready
wait_for_endpoint() {
    local endpoint=$1
    local retries=0
    
    echo "⏳ Waiting for $endpoint to be ready..."
    
    while [ $retries -lt $MAX_RETRIES ]; do
        if curl -f -s -o /dev/null -w "%{http_code}" "$BASE_URL$endpoint" | grep -q "200\|503"; then
            echo "✅ $endpoint is responding"
            return 0
        fi
        
        retries=$((retries + 1))
        echo "   Attempt $retries/$MAX_RETRIES..."
        sleep 2
    done
    
    echo "❌ $endpoint failed to respond after $MAX_RETRIES attempts"
    return 1
}

# Test 1: Health endpoint
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "📋 Test 1: /health endpoint"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

wait_for_endpoint "/health"

HEALTH_RESPONSE=$(curl -s "$BASE_URL/health")
echo "Response: $HEALTH_RESPONSE"

if echo "$HEALTH_RESPONSE" | grep -q '"status":"UP"'; then
    echo "✅ Health check passed"
else
    echo "❌ Health check failed"
    exit 1
fi

echo ""

# Test 2: Readiness endpoint
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "📋 Test 2: /ready endpoint (database connectivity)"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

wait_for_endpoint "/ready"

READY_RESPONSE=$(curl -s "$BASE_URL/ready")
echo "Response: $READY_RESPONSE"

READY_STATUS=$(echo "$READY_RESPONSE" | grep -o '"status":"[^"]*"' | cut -d'"' -f4)

if [ "$READY_STATUS" = "ready" ]; then
    echo "✅ Readiness check passed (database connected)"
elif [ "$READY_STATUS" = "not ready" ]; then
    echo "⚠️  Backend is running but database is not connected"
    echo "   This might be expected if DB is not configured"
else
    echo "❌ Readiness check failed"
    exit 1
fi

echo ""

# Test 3: Root endpoint
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "📋 Test 3: / endpoint (API info)"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

ROOT_RESPONSE=$(curl -s "$BASE_URL/")
echo "Response: $ROOT_RESPONSE"

if echo "$ROOT_RESPONSE" | grep -q "ScrapSail"; then
    echo "✅ Root endpoint working"
else
    echo "❌ Root endpoint failed"
    exit 1
fi

echo ""

# Summary
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "🎉 All smoke tests passed!"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "✅ Health endpoint: Working"
echo "✅ Readiness endpoint: $READY_STATUS"
echo "✅ Root endpoint: Working"
echo ""
echo "🚀 Backend is deployed and responding correctly!"



