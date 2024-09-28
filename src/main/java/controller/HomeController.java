package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {

    @FXML
    private Label usernameLabel;

    @FXML
    private Button logoutButton;

    // This method sets the username in the home page
    public void setUser(String username) {
        usernameLabel.setText("Welcome, " + username + "!");
    }

    @FXML
    public void handleLogout() throws IOException {
        // Navigate back to the login page
        Parent loginPage = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        Stage stage = (Stage) logoutButton.getScene().getWindow(); // Get current stage
        stage.setScene(new Scene(loginPage, 400, 300)); // Set the new scene (login page)
        stage.show();
    }
}
