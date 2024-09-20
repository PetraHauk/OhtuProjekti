package app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.enteties.Kayttaja;
import model.DAO.KayttajaDAO;

public class LoginGui extends Application {

    private KayttajaDAO kayttajaDAO = new KayttajaDAO();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login");

        // Create UI elements
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Button registerButton = new Button("Register");

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(emailField, 0, 0);
        grid.add(passwordField, 0, 1);
        grid.add(loginButton, 0, 2);
        grid.add(registerButton, 1, 2);

        // Event Handlers
        loginButton.setOnAction(e -> handleLogin(emailField.getText(), passwordField.getText(), primaryStage));

        registerButton.setOnAction(e -> {
            primaryStage.close();
            new RegistrationGui().start(new Stage()); // Open registration page
        });

        // Set Scene
        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin(String email, String password, Stage primaryStage) {
        Kayttaja kayttaja = kayttajaDAO.findPasswordByEmail(email);

        if (kayttaja != null && kayttaja.getSalasana().equals(password)) {
            // Login successful
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Login Successful");
            alert.setHeaderText(null);
            alert.setContentText("Welcome, " + kayttaja.getEtunimi() + "!");
            alert.showAndWait();

            // Close the login window
            primaryStage.close();

            // Open OhjelmistoGUI
            try {
                new OhjelmistoGUI().start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            // Login failed
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Please check your email and password.");
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
