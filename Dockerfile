# Stage 1: Build the application using Maven
FROM maven:3.8.6-openjdk-21 AS build

WORKDIR /app

# Copy only the files needed for dependency resolution
COPY pom.xml /app/
RUN mvn dependency:go-offline

# Copy the source files and build the application
COPY src /app/src
RUN mvn package -DskipTests

# Stage 2: Create a lightweight image for running the application
FROM ubuntu:20.04

WORKDIR /app

# Install only necessary dependencies
RUN apt-get update && apt-get install -y \
    openjdk-21-jdk \
    openjfx \
    xvfb && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/ohtutest.jar /app/ohtutest.jar

ENV DISPLAY=:99

CMD ["sh", "-c", "Xvfb :99 -screen 0 1024x768x16 & java -Dprism.order=sw -Djava.awt.headless=false -jar /app/ohtutest.jar"]
