#!/bin/bash
# Quick script to verify environment variables are set
# This helps diagnose deployment issues

echo "=== Environment Variables Check ==="
echo "SPRING_PROFILES_ACTIVE: ${SPRING_PROFILES_ACTIVE:-NOT SET}"
echo "MYSQL_URL: ${MYSQL_URL:-NOT SET}"
echo "DB_USERNAME: ${DB_USERNAME:-NOT SET}"
echo "DB_PASSWORD: ${DB_PASSWORD:+SET (hidden)}${DB_PASSWORD:-NOT SET}"
echo "SERVER_PORT: ${SERVER_PORT:-NOT SET}"
echo "PORT: ${PORT:-NOT SET}"
echo "==================================="



