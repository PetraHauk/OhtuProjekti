package controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Kayttaja;
import model.KayttajaDAO;

public class LoginController {

    @FXML
    private TextField emailIdField;
    @FXML
    private PasswordField passwordField;

    private KayttajaDAO kayttajaDAO = new KayttajaDAO();

    @FXML
    public void login() {
        String email = emailIdField.getText();
        String password = passwordField.getText();

        Kayttaja kayttaja = kayttajaDAO.haeKayttaja(email, password);

        if (kayttaja != null) {
            // Login successful
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Login Successful");
            alert.setHeaderText(null);
            alert.setContentText("Welcome " + email + "!");
            alert.showAndWait();
        } else {
            // Login failed
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Login Failed");
            alert.setHeaderText(null);
            alert.setContentText("Invalid email or password.");
            alert.showAndWait();
        }
    }

    public void register(String email, String password) {
        kayttajaDAO.persist(new Kayttaja(email, password));
    }
}
