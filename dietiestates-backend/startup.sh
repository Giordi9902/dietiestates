#!/bin/bash

echo "📦 Generazione del pacchetto JAR..."
mvn clean package -DskipTests

echo "🐳 Avvio di Docker Compose..."
docker compose up -d --build --remove-orphans

echo "🚀 Backend avviato con successo!"