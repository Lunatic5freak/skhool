# ----------- Stage 1: Build -----------

# FROM eclipse-temurin:17-jdk-alpine as builder

# WORKDIR /app

# # Copy the Maven/Gradle files first to cache dependencies
# COPY mvnw .
# COPY .mvn .mvn
# COPY pom.xml .

# RUN ./mvnw dependency:go-offline

# # Now copy the rest of the source and build
# COPY src ./src

# RUN ./mvnw clean package -DskipTests

# ----------- Stage 2: Run (Distroless JRE base) -----------
# ---------- Final Minimal Image (No shell, ~90 MB) ----------
FROM gcr.io/distroless/java17-debian11:nonroot


WORKDIR /app

# Copy only the built jar (manually built)
COPY target/skhool-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]

