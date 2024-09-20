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

public class RegistrationGui extends Application {

    private KayttajaDAO kayttajaDAO = new KayttajaDAO();
    private Stage primaryStage; // To keep a reference to the current stage

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; // Save the stage reference
        primaryStage.setTitle("Register");

        // Create UI elements
        TextField etunimiField = new TextField();
        etunimiField.setPromptText("First Name");

        TextField sukunimiField = new TextField();
        sukunimiField.setPromptText("Last Name");

        TextField spostiField = new TextField();
        spostiField.setPromptText("Email");

        TextField puhField = new TextField();
        puhField.setPromptText("Phone");

        PasswordField salasanaField = new PasswordField();
        salasanaField.setPromptText("Password");

        Button registerButton = new Button("Register");
        Button backButton = new Button("Back");

        // Layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(etunimiField, 0, 0);
        grid.add(sukunimiField, 0, 1);
        grid.add(spostiField, 0, 2);
        grid.add(puhField, 0, 3);
        grid.add(salasanaField, 0, 4);
        grid.add(registerButton, 0, 5);
        grid.add(backButton, 1, 5);

        // Event Handlers
        registerButton.setOnAction(e -> handleRegister(
                etunimiField.getText(),
                sukunimiField.getText(),
                spostiField.getText(),
                puhField.getText(),
                salasanaField.getText()
        ));

        backButton.setOnAction(e -> {
            primaryStage.close();
            new LoginGui().start(new Stage()); // Return to login page
        });

        // Set Scene
        Scene scene = new Scene(grid, 300, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleRegister(String etunimi, String sukunimi, String sposti, String puh, String salasana) {
        if (etunimi.isEmpty() || sukunimi.isEmpty() || sposti.isEmpty() || puh.isEmpty() || salasana.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Registration Failed");
            alert.setContentText("Please fill out all fields.");
            alert.showAndWait();
        } else {
            Kayttaja kayttaja = new Kayttaja();
            kayttaja.setEtunimi(etunimi);
            kayttaja.setSukunimi(sukunimi);
            kayttaja.setSposti(sposti);
            kayttaja.setPuh(puh);
            kayttaja.setSalasana(salasana);
            kayttaja.setRooli("1");

            kayttajaDAO.persist(kayttaja);

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Registration Successful");
            alert.setContentText("Welcome, " + etunimi + " " + sukunimi + "!");
            alert.showAndWait();

            // Open login page and close the current stage
            new LoginGui().start(new Stage());
            primaryStage.close(); // Close the registration stage
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
