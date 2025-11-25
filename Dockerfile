# =========================
# Stage 1: Build the application
# =========================
FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

# Path to your nested Spring Boot project
ARG PROJECT_PATH=backend/city-style-application

# -------------------------
# Step 1: Copy Maven wrapper and pom.xml first
# -------------------------
COPY ${PROJECT_PATH}/.mvn/ .mvn
COPY ${PROJECT_PATH}/mvnw ${PROJECT_PATH}/pom.xml ./

RUN chmod +x ./mvnw

# -------------------------
# Step 2: Force dependency resolution
# -------------------------
# Purge any old SendGrid versions and update all dependencies
RUN ./mvnw dependency:purge-local-repository -DmanualInclude="com.sendgrid:sendgrid-java" -DreResolve=true
RUN ./mvnw dependency:go-offline -U

# -------------------------
# Step 3: Copy source code (after dependencies to leverage caching)
# -------------------------
COPY ${PROJECT_PATH}/src ./src

# -------------------------
# Step 4: Build JAR (skip tests to save time)
# -------------------------
RUN ./mvnw clean package -DskipTests -U

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
