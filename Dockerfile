
# Stage 1: Build the application using Maven
2

FROM maven:latest AS build
3


4

# Set the working directory
5

WORKDIR /app
6


7

# Copy the pom.xml and source files into the container
8

COPY pom.xml /app/
9

COPY src /app/src
10


11

# Build the application and skip tests to prevent failures during image build
12

RUN mvn package -DskipTests
13


14

# Stage 2: Create a lightweight image for running the application
15

FROM ubuntu:20.04
16


17

# Set the working directory
18

WORKDIR /app
19


20

# Install OpenJDK and OpenJFX
21

RUN apt-get update && \
22

    apt-get install -y openjdk-21-jdk openjfx xvfb && \
23

    apt-get clean && \
24

    rm -rf /var/lib/apt/lists/*
25


26

# Copy the packaged JAR from the build stage
27

COPY --from=build /app/target/ohtutest.jar /app/ohtutest.jar
28


29

# Set the DISPLAY environment variable for JavaFX
30

ENV DISPLAY=:99
31


32

# Start Xvfb and run the Java application
33

CMD ["sh", "-c", "Xvfb :99 -screen 0 1024x768x16 & java -Dprism.order=sw -Djava.awt.headless=false -jar /app/ohtutest.jar"]