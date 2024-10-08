package view;

import model.service.UserSession;
import controller.HuoneController;
import controller.VarausController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;

import model.DAO.AsiakasDAO;
import model.enteties.Huone;
import model.enteties.Asiakas;
import model.enteties.Varaus;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.time.Period;
import java.util.List;
import java.util.Locale;
import java.util.function.ToDoubleFunction;

import model.enteties.*;
import controller.*;
import model.service.CurrencyConverter;
import view.sivut.*;

// Loppusumma kommentoitu pois koska punasta viivaa
public class OhjelmistoGUI extends Application {

    private HuoneController huoneController;
    private VarausController varausController;
    private AsiakasController asiakasController;
    private LaskuController laskuController;
    private HotelliController hotelliController;
    private KayttajaController kayttajaController;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hotel Management System");

        asiakasController = new AsiakasController();
        varausController = new VarausController();
        huoneController = new HuoneController();
        laskuController = new LaskuController();
        hotelliController = new HotelliController();

        Etusivu etusivu = new Etusivu();

        HBox mainLayout = new HBox(10);
        VBox leftBar = createLeftBar(mainLayout, primaryStage);
        mainLayout.getChildren().addAll(leftBar, etusivu.createEtusivu(1));

        Scene scene = new Scene(mainLayout, 1250, 630);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    // Creates the left bar with buttons and user info
    private VBox createLeftBar(HBox mainLayout, Stage primaryStage) {
        Label loggedInUsername = new Label(UserSession.getUsername());
        Label loggedInImage = new Label("[IMAGE]");
        HBox userBox = new HBox(10);
        userBox.getStyleClass().add("user-box");
        userBox.getChildren().addAll(loggedInImage, loggedInUsername);

        Button frontPageButton = createStyledButton("Etusivu");
        Button showRoomsButton = createStyledButton("Huoneiden hallinta");
        Button showCustomersButton = createStyledButton("Asiakasrekisteri");
        Button showVarauksetButton = createStyledButton("Varaukset");
        Button checkInButton = createStyledButton("Check-In");
        Button checkOutButton = createStyledButton("Check-Out");

        VBox leftButtons = new VBox(10);
        leftButtons.getChildren().addAll(frontPageButton, showRoomsButton, showCustomersButton, showVarauksetButton, checkInButton, checkOutButton);
        leftButtons.getStyleClass().add("left-buttons");

        Button logoutButton = new Button("Kirjaudu ulos");
        logoutButton.setPrefWidth(200);
        logoutButton.getStyleClass().add("button-log-out");

        VBox leftBar = new VBox(30);
        leftBar.getChildren().addAll(userBox, leftButtons, logoutButton);
        leftBar.getStyleClass().add("left-bar");


        // Check if user is an admin
        if (UserSession.getRooli().equals("Admin")) {
            Button adminButton = new Button("Admin Panel");
            adminButton.setPrefWidth(200);
            adminButton.getStyleClass().add("button-admin");

            // Open AdminGUI on button click
            adminButton.setOnAction(e -> openAdminPanel());

            // Add the admin button below the logout button
            leftBar.getChildren().add(adminButton);
        }

        frontPageButton.setOnAction(e -> handleFrontPageButtonAction(mainLayout, leftBar));
        showRoomsButton.setOnAction(e -> handleShowRoomsButtonAction(mainLayout, leftBar));
        showCustomersButton.setOnAction(e -> handleShowCustomersButtonAction(mainLayout, leftBar));
        showVarauksetButton.setOnAction(e -> handleShowVarauksetButtonAction(mainLayout, leftBar));
        checkInButton.setOnAction(e -> handleCheckInButtonAction(mainLayout, leftBar));
        checkOutButton.setOnAction(e -> handleCheckOutButtonAction(mainLayout, leftBar));
        logoutButton.setOnAction(e -> handleLogoutButtonAction(primaryStage));


        return leftBar;
    }

    private void openAdminPanel() {
        new AdminGUI().start(new Stage());
    }

    // Updates the main layout with new content
    private void updateMainLayout(HBox mainLayout, VBox leftBar, VBox info) {
        mainLayout.getChildren().clear();
        mainLayout.getChildren().addAll(leftBar, info);
    }

    // Creates a button with custom styling
    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setAlignment(Pos.CENTER_LEFT);
        button.getStyleClass().add("button-front-page");
        return button;
    }

    // Button Actions
    private void handleFrontPageButtonAction(HBox mainLayout, VBox leftBar) {
        Etusivu etusivu = new Etusivu();
        updateMainLayout(mainLayout, leftBar, etusivu.createEtusivu(1));
    }

    private void handleShowRoomsButtonAction(HBox mainLayout, VBox leftBar) {
        HuoneSivu huoneSivu = new HuoneSivu();
        updateMainLayout(mainLayout, leftBar, huoneSivu.createHuoneet());
    }

    private void handleShowCustomersButtonAction(HBox mainLayout, VBox leftBar) {
        AsiakasSivu asiakasSivu = new AsiakasSivu();
        updateMainLayout(mainLayout, leftBar, asiakasSivu.createAsiakkaat());
    }

    private void handleShowVarauksetButtonAction(HBox mainLayout, VBox leftBar) {
        VarausSivu varausSivu = new VarausSivu();
        updateMainLayout(mainLayout, leftBar, varausSivu.createVaraukset());
    }

    private void handleCheckInButtonAction(HBox mainLayout, VBox leftBar) {
        CheckIn checkIn = new CheckIn();
        updateMainLayout(mainLayout, leftBar, checkIn.createCheckIn());
    }

    private void handleCheckOutButtonAction(HBox mainLayout, VBox leftBar) {
        CheckOut checkOut = new CheckOut();
        updateMainLayout(mainLayout, leftBar, checkOut.createCheckOut());
    }

    private void handleLogoutButtonAction(Stage primaryStage) {
        primaryStage.close();

        // Start a new LoginGui
        try {
            new LoginGui().start(new Stage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}