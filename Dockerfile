# Stage 1: Build the application
# Using Eclipse Temurin for a reliable JDK base for building
FROM eclipse-temurin:21-jdk-jammy AS build

# Set the working directory inside the container
WORKDIR /app

# Copy Maven wrapper files and pom.xml to download dependencies
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Download dependencies to cache them (speeds up future builds)
RUN ./mvnw dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Build the application (creates the JAR file), skipping tests
RUN ./mvnw clean install -DskipTests

# Stage 2: Create the final, smaller runtime image
# Using Eclipse Temurin JRE (smaller than JDK) for production
FROM eclipse-temurin:21-jre-jammy

# Set the argument for the application JAR file name
# This pattern matches the JAR created in the target directory
ARG JAR_FILE=target/*.jar

# === CRITICAL FIX IS HERE ===
# Copy the built JAR file from the 'build' stage using the ARG variable
COPY --from=build /app/${JAR_FILE} app.jar

# Application Configuration
ENV PORT 8080

# The command to run the application when the container starts
ENTRYPOINT ["java", "-jar", "/app.jar"]
