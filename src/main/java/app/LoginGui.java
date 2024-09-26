package app;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.enteties.Kayttaja;
import model.DAO.KayttajaDAO;
import org.mindrot.jbcrypt.BCrypt;

public class LoginGui extends Application {

    private KayttajaDAO kayttajaDAO = new KayttajaDAO();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Kirjautuminen");

        // Create UI elements
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.getStyleClass().add("text-field");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Salasana");
        passwordField.getStyleClass().add("password-field");

        Button loginButton = new Button("Kirjaudu");
        loginButton.getStyleClass().add("button-one");

        Button registerButton = new Button("Rekisteröidy");
        registerButton.getStyleClass().add("button-two");

        // Layout
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(emailField, passwordField, loginButton, registerButton);

        // Event Handlers
        loginButton.setOnAction(e -> handleLogin(emailField.getText(), passwordField.getText(), primaryStage));

        registerButton.setOnAction(e -> {
            primaryStage.close();
            new RegistrationGui().start(new Stage()); // Open registration page
        });

        // Set Scene
        Scene scene = new Scene(vbox, 300, 200);
        scene.getStylesheets().add(getClass().getResource("/login.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin(String email, String password, Stage primaryStage) {
        String savedHashedPassword = kayttajaDAO.findPasswordByEmail(email);

        if (savedHashedPassword != null && BCrypt.checkpw(password, savedHashedPassword)) {
            // Login successful
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Kirjautumine onnistui");
            alert.setHeaderText(null);
            alert.setContentText("Tervetuloa!");
            alert.showAndWait();

            // Close the login window
            primaryStage.close();

            // Open next stage
            new OhjelmistoGUI().start(new Stage());
        } else {
            // Login failed
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Kirjautuminen epäonnistui");
            alert.setHeaderText(null);
            alert.setContentText("Tarkista sähköposti ja salasana.");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}