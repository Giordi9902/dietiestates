# Usa un'immagine base con OpenJDK 21
FROM eclipse-temurin:21-jdk-alpine

# Imposta l'argomento per il file JAR
ARG JAR_FILE=target/DietiEstates25-Backend-App.jar

# Copia il file JAR nell'immagine
COPY ${JAR_FILE} app.jar

# Imposta la porta su cui l'applicazione sarà in ascolto
EXPOSE 8080

# Comando per eseguire l'applicazione
ENTRYPOINT ["java", "-jar", "/app.jar"]