# Use Maven image for building the project
FROM maven:latest AS build

WORKDIR /app

COPY pom.xml /app/
COPY . /app/

# Build the project
RUN mvn package

# Use OpenJDK for running the Java application
FROM openjdk:21-jdk

# Download and install JavaFX SDK
RUN apt-get update && apt-get install -y wget unzip \
    && wget https://download2.gluonhq.com/openjfx/20.0.2/openjfx-20.0.2_linux-x64_bin-sdk.zip -O /tmp/openjfx.zip \
    && unzip /tmp/openjfx.zip -d /opt/ && rm /tmp/openjfx.zip

# Copy the built JAR from the Maven image
COPY --from=build /app/target/ohtutest.jar /app/ohtutest.jar

# Set the path to JavaFX SDK
ENV PATH_TO_FX=/opt/javafx-sdk-20.0.2/lib

# Command to run the Java application with JavaFX
CMD ["java", "--module-path", "/opt/javafx-sdk-20.0.2/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "/app/ohtutest.jar"]
