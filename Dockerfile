# ----------- Stage 1: Build -----------

FROM eclipse-temurin:17-jdk-alpine as builder

WORKDIR /app

# Copy the Maven/Gradle files first to cache dependencies
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN ./mvnw dependency:go-offline

# Now copy the rest of the source and build
COPY src ./src

RUN ./mvnw clean package -DskipTests

# ----------- Stage 2: Run (Distroless JRE base) -----------

FROM gcr.io/distroless/java17-debian11:nonroot

WORKDIR /app

# Copy jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
