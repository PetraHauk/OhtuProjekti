# Stage 1: Build the application using Maven
FROM maven:latest AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source files into the container
COPY pom.xml /app/
COPY src /app/src

# Build the application and skip tests to prevent failures during image build
RUN mvn package -DskipTests

# Stage 2: Create a lightweight image for running the application
FROM ubuntu:20.04

# Set the working directory
WORKDIR /app

# Install OpenJDK and OpenJFX
RUN apt-get update && \
    apt-get install -y openjdk-21-jdk openjfx xvfb && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Copy the packaged JAR from the build stage
COPY --from=build /app/target/ohtutest.jar /app/ohtutest.jar

# Set the DISPLAY environment variable for JavaFX
ENV DISPLAY=:99

# Start Xvfb and run the Java application
CMD ["sh", "-c", "Xvfb :99 -screen 0 1024x768x16 & java -Dprism.order=sw -Djava.awt.headless=false -jar /app/ohtutest.jar"]