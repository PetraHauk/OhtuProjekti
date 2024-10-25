# Use the latest Maven image as the base image
FROM maven:latest as build

# Set the working directory in the container
WORKDIR /app

# Copy only the pom.xml first to leverage Docker cache
COPY pom.xml /app/

# Download dependencies (this layer will be cached if no changes to pom.xml)
RUN mvn dependency:go-offline

# Copy the rest of the application code
COPY . /app/

# Package the application
RUN mvn package

# Use a lighter Java runtime for the final image
FROM openjdk:17-jdk-slim

# Set the working directory for the runtime environment
WORKDIR /app

# Copy the packaged JAR file from the build stage
COPY --from=build /app/target/test.jar /app/test.jar

# Install necessary packages for running the JavaFX application
RUN apt-get update && apt-get install -y \
    x11vnc \
    xvfb \
    && rm -rf /var/lib/apt/lists/*

# Start a virtual framebuffer and VNC server, then run the JavaFX application
CMD ["sh", "-c", "Xvfb :99 -screen 0 1024x768x16 & x11vnc -display :99 -nopw -forever & sleep 5 && java --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml -jar /app/test.jar"]
