# Use an official Gradle image to build the Spring Boot app
FROM maven:3.9.9-amazoncorretto-17 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy only the gradle build files first to leverage Docker layer caching
COPY ../pom.xml pom.xml

# Download dependencies before copying the entire project
RUN mvn -U clean install
COPY ../src src
RUN mvn package -Dmaven.test.skip

# Copy the rest of the application code
#COPY . .

# Build the Spring Boot application
#RUN gradle build --no-daemon

# Use an official OpenJDK runtime as a base image for running the application
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built application from the previous stage
COPY --from=builder /app/target/*-jar-with-dependencies.jar ./app.jar

# Expose the port Spring Boot is running on
EXPOSE 8080

# Set the entry point for the container to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
