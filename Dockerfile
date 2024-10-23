# Step 1: Build Stage
FROM maven:3.9.5 AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and source files to the container
COPY pom.xml /app/
COPY src /app/src/

# Package the application using Maven (skipping tests)
RUN mvn clean package -DskipTests

# Step 2: Runtime Stage
FROM ubuntu:20.04

# Install the necessary packages for running JavaFX and OpenJDK
RUN apt-get update && apt-get install --no-install-recommends -y \
    openjdk-21-jdk \
    openjfx \
    xorg \
    libgl1-mesa-glx \
    x11vnc \
    xvfb \
    && rm -rf /var/lib/apt/lists/*

# Copy the built JAR file from the Maven container (the "builder" stage)
COPY --from=builder /app/target/ohtutest.jar /app/ohtutest.jar

# Set the working directory for the runtime environment
WORKDIR /app

# Set the DISPLAY environment variable to use Xvfb's virtual display :99
ENV DISPLAY=:99

# Start a virtual framebuffer, VNC server, and run the JavaFX application
CMD ["sh", "-c", "Xvfb :99 -screen 0 1024x768x16 & x11vnc -display :99 -nopw -forever & sleep 5 && java --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml -jar ohtutest.jar"]
