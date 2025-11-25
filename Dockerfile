# =========================
# Stage 1: Build the application
# =========================
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

# Path to your nested Spring Boot project
ARG PROJECT_PATH=backend/city-style-application

# Copy Maven wrapper and pom.xml
COPY ${PROJECT_PATH}/.mvn/ .mvn
COPY ${PROJECT_PATH}/mvnw ${PROJECT_PATH}/pom.xml ./

# Make Maven wrapper executable
RUN chmod +x ./mvnw

# Download dependencies (cached in a separate layer)
RUN ./mvnw dependency:go-offline

# Copy the source code
COPY ${PROJECT_PATH}/src ./src

# Build the Spring Boot application (skip tests to speed up build)
RUN ./mvnw clean package -DskipTests

# =========================
# Stage 2: Runtime image
# =========================
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Set environment variables for Render
ENV PORT=8080 \
    SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL} \
    SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME} \
    SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD} \
    JWT_SECRET_KEY=${JWT_SECRET_KEY} \
    STRIPE_SECRET_KEY=${STRIPE_SECRET_KEY} \
    SENDGRID_API_KEY=${SENDGRID_API_KEY}  
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
