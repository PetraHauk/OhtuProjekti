package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.enteties.Kayttaja;
import model.DAO.KayttajaDAO;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailIdField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField etunimiField;
    @FXML
    private TextField sukunimiField;
    @FXML
    private TextField spostiField;
    @FXML
    private TextField puhField;
    @FXML
    private PasswordField salasanaField;

    private KayttajaDAO kayttajaDAO = new KayttajaDAO();

    // Getters for the fields
    public TextField getEtunimiField() {
        return etunimiField;
    }

    public TextField getSukunimiField() {
        return sukunimiField;
    }

    public TextField getSpostiField() {
        return spostiField;
    }

    public TextField getPuhField() {
        return puhField;
    }

    public PasswordField getSalasanaField() {
        return salasanaField;
    }

    public TextField getEmailIdField() {
        return emailIdField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    @FXML
    public void handleGoToRegister() throws IOException {
        // Navigate to the registration page
        Parent registerPage = FXMLLoader.load(getClass().getResource("/view/register.fxml"));
        Stage stage = (Stage) emailIdField.getScene().getWindow(); // Get current stage
        stage.setScene(new Scene(registerPage, 400, 300)); // Set the new scene (registration page)
        stage.show();
    }

    @FXML
    public void handleBackToLogin() throws IOException {
        // Navigate back to the login page
        Parent loginPage = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        Stage stage = (Stage) etunimiField.getScene().getWindow(); // Get current stage
        stage.setScene(new Scene(loginPage, 400, 300)); // Set the new scene (login page)
        stage.show();
    }

    @FXML
    public void handleRegister() throws IOException {
        String etunimi = etunimiField.getText();
        String sukunimi = sukunimiField.getText();
        String sposti = spostiField.getText();
        String puh = puhField.getText();
        String salasana = salasanaField.getText();

        // Validate input before registering
        if (etunimi.isEmpty() || sukunimi.isEmpty() || sposti.isEmpty() || puh.isEmpty() || salasana.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Rekisteröinti epäonnistui");
            alert.setHeaderText(null);
            alert.setContentText("Täytä kaikki kohdat.");
            alert.showAndWait();
        } else {
            // Create a new Kayttaja object and register it
            Kayttaja kayttaja = new Kayttaja();
            kayttaja.setEtunimi(etunimi);
            kayttaja.setSukunimi(sukunimi);
            kayttaja.setSposti(sposti);
            kayttaja.setPuh(puh);
            kayttaja.setSalasana(salasana);
            kayttaja.setRooli("1");


            kayttajaDAO.persist(kayttaja);

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Rekisteröinti onnistui");
            alert.setHeaderText(null);
            alert.setContentText("Tervetuloa, " + etunimi + " " + sukunimi + "!");
            alert.showAndWait();

            // Load the home page FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            Parent homePage = loader.load();

            // Get the controller of the home page
            HomeController homeController = loader.getController();

            // Pass the username to the home controller
            homeController.setUser(etunimi);

            // Navigate to the home page after successful registration
            Stage stage = (Stage) etunimiField.getScene().getWindow(); // Get current stage
            stage.setScene(new Scene(homePage, 600, 400)); // Set the new scene (home page)
            stage.show();
        }
    }


}