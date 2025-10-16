# Stage 1: Build the application
# Using Eclipse Temurin for a reliable JDK base for building
FROM eclipse-temurin:21-jdk-jammy AS build

# Set the working directory inside the container
WORKDIR /app

# The path to your nested Spring Boot project folder
ARG PROJECT_PATH=backend/city-style-application

# Copy Maven wrapper files and pom.xml from the nested directory
COPY ${PROJECT_PATH}/.mvn/ .mvn
COPY ${PROJECT_PATH}/mvnw ${PROJECT_PATH}/pom.xml ./

# === CRITICAL FIX: Add executable permission to the Maven Wrapper ===
RUN chmod +x ./mvnw

# Download dependencies to cache them
RUN ./mvnw dependency:go-offline

# Copy the rest of the source code
COPY ${PROJECT_PATH}/src ./src

# Build the application (creates the JAR file), skipping tests
RUN ./mvnw clean install -DskipTests

# Stage 2: Create the final, smaller runtime image
FROM eclipse-temurin:21-jre-jammy

# Set the argument for the application JAR file name
ARG JAR_FILE=target/*.jar

# Copy the built JAR file from the 'build' stage into the final image
COPY --from=build /app/${JAR_FILE} app.jar

# Application Configuration
ENV PORT 8080

# The command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "/app.jar"]
