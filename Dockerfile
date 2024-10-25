# Stage 1: Build the application
FROM maven:3.9.0 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and the source code
COPY pom.xml ./
COPY src ./src

# Download dependencies
RUN mvn dependency:go-offline


# Package the application
RUN mvn package

# Stage 2: Create the runtime image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Install necessary packages for running the JavaFX application
RUN apt-get update && apt-get install -y \
    openjfx \
    x11vnc \
    xvfb \
    && rm -rf /var/lib/apt/lists/*

# Command to run the application
CMD ["sh", "-c", "Xvfb :99 -screen 0 1024x768x16 & x11vnc -display :99 -nopw -forever & sleep 5 && java --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml -jar app.jar"]

