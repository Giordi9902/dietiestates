#!/bin/bash

echo "📦 Generazione del pacchetto JAR..."
mvn clean package -q -DskipTests

echo "🐳 Avvio di Docker Compose..."
docker compose up -d --build --remove-orphans >/dev/null 2>&1

echo "🚀 Backend avviato con successo!"