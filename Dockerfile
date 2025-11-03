# Use OpenJDK 17 as base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Install Maven wrapper dependencies (if needed)
RUN apt-get update && apt-get install -y wget && rm -rf /var/lib/apt/lists/*

# Copy Maven wrapper files
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x ./mvnw

# Copy source code
COPY src/ ./src/

# Build the application
RUN ./mvnw clean package -DskipTests

# Expose port (Render will set PORT env var)
EXPOSE 8080

# Use shell form for ENTRYPOINT to ensure environment variables are available
# This ensures Render's env vars are properly passed through
ENTRYPOINT exec java -jar target/scrapsail-backend-0.0.1-SNAPSHOT.jar

