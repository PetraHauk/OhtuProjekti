FROM maven:latest AS build

WORKDIR /app

COPY pom.xml /app/
COPY src /app/src

RUN mvn package

FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/target/ohtutest.jar /app/ohtutest.jar

# Install JavaFX and Xvfb
RUN apt-get update && apt-get install -y openjfx xvfb

# Expose the DISPLAY environment variable to JavaFX
ENV DISPLAY=:99

# Start Xvfb and run the application
CMD ["sh", "-c", "Xvfb :99 -screen 0 1024x768x16 & java -Dprism.order=sw -Djava.awt.headless=false -jar /app/ohtutest.jar"]
