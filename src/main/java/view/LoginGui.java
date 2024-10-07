package view;

import javafx.scene.control.*;
import model.service.UserSession;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.DAO.KayttajaDAO;
import org.mindrot.jbcrypt.BCrypt;

public class LoginGui extends Application {

    private KayttajaDAO kayttajaDAO = new KayttajaDAO();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Kirjautuminen");

        Label loginLabel = new Label("Kirjaudu sisään");
        loginLabel.getStyleClass().add("otsikko");

        // Create UI elements
        Label emailLabel = new Label("Email:");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.getStyleClass().add("text-field");

        Label passwordLabel = new Label("Password:");

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
        vbox.getChildren().addAll(loginLabel, emailLabel, emailField, passwordLabel, passwordField, loginButton, registerButton);

        // Event Handlers
        loginButton.setOnAction(e -> handleLogin(emailField.getText(), passwordField.getText(), primaryStage));

        registerButton.setOnAction(e -> {
            primaryStage.close();
            new RegistrationGui().start(new Stage()); // Open registration page
        });

        // Set Scene
        Scene scene = new Scene(vbox, 300, 300);
        scene.getStylesheets().add(getClass().getResource("/login.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

       private void handleLogin(String email, String password, Stage primaryStage) {
        String savedHashedPassword = kayttajaDAO.findPasswordByEmail(email);

        if (savedHashedPassword != null && !savedHashedPassword.isEmpty() && BCrypt.checkpw(password, savedHashedPassword)) {
            // Retrieve the user role (rooli)
            String rooli = kayttajaDAO.findRooliByEmail(email); // Assuming this method exists in KayttajaDAO

            // Save the username and role to UserSession
            UserSession.setUsername(email);
            UserSession.setRooli(rooli);

            // Login successful
            //Alert alert = new Alert(Alert.AlertType.INFORMATION);
            //alert.setTitle("Kirjautuminen onnistui");
            //alert.setHeaderText(null);
            //alert.setContentText("Tervetuloa!");
            //alert.showAndWait();

            // Close the login window
            primaryStage.close();

            // Open next stage and pass the username
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