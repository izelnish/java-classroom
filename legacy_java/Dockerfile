# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the maven executables (if we had the wrapper, but we'll just copy the pom and src for now and build inside, 
# or simpler: copy the jar. Since we don't have a jar built in the artifact interaction easily for the user without them running it, 
# a multi-stage build is best)

# --- Build Stage ---
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# Build the application
RUN mvn clean package -DskipTests

# --- Run Stage ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java","-jar","app.jar"]
