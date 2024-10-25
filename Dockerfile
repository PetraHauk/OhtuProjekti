FROM maven:latest

WORKDIR /app

COPY pom.xml /app/

COPY . /app/

RUN mvn package

cmd ["java", "-jar", "target/test.jar"]
# Start a virtual framebuffer, VNC server, and run the JavaFX application
#CMD ["sh", "-c", "Xvfb :99 -screen 0 1024x768x16 & x11vnc -display :99 -nopw -forever & sleep 5 && java --module-path /usr/share/openjfx/lib --add-modules javafx.controls,javafx.fxml -jar ohtutest.jar"]

