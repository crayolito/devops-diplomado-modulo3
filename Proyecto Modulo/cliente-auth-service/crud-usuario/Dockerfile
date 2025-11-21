# ============================================
# STAGE 1: Build (compilación)
# ============================================
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copiar archivos de Gradle
COPY build.gradle settings.gradle ./
COPY gradlew ./
COPY gradle ./gradle

# Dar permisos de ejecución
RUN chmod +x gradlew

# Descargar dependencias (se cachea si build.gradle no cambia)
RUN ./gradlew dependencies --no-daemon || return 0

# Copiar el código fuente
COPY src ./src

# Compilar la aplicación
RUN ./gradlew clean build -x test --no-daemon

# ============================================
# STAGE 2: Runtime (imagen final pequeña)
# ============================================
FROM eclipse-temurin:21-jre-alpine

# Información del mantenedor
LABEL maintainer="josalejandroapp2017@gmail.com"
LABEL description="CRUD de Clientes - Spring Boot API"
LABEL version="1.0"

WORKDIR /app

# Copiar SOLO el JAR compilado desde el stage de build
COPY --from=builder /app/build/libs/crud-usuario-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto 8080
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]