# Stage 1: Build
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY .mvn .mvn
COPY mvnw ./mvnw
RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/openground-backend-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", \
  "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:local}", \
  "-jar", "app.jar"]
