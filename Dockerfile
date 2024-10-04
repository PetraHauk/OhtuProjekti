# Base image for ARM64 (Mac M1/M2)
FROM arm64v8/openjdk:21-jdk-slim

# Copy the JavaFX SDK for macOS ARM64 from the host to the container
COPY /Users/anna/openjfx-20.0.2_macos-aarch64_bin-sdk /opt/javafx-sdk

# Set working directory
WORKDIR /app

# Copy the application files
COPY . /app

# Build and run the application with the JavaFX SDK
CMD ["java", "--module-path", "/opt/javafx-sdk/lib", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "target/ohtutest.jar"]
