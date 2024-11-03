package view;

import model.service.UserSession;
import controller.HuoneController;
import controller.VarausController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;

import java.util.Locale;
import java.util.ResourceBundle;

import controller.*;
import view.sivut.*;

// Loppusumma kommentoitu pois koska punasta viivaa
public class OhjelmistoGUI extends Application {

    private String selectedLanguage = "Suomi";
    private HuoneController huoneController;
    private VarausController varausController;
    private AsiakasController asiakasController;
    private LaskuController laskuController;
    private HotelliController hotelliController;
    private KayttajaController kayttajaController;

    private ResourceBundle bundle;
    private void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        bundle = ResourceBundle.getBundle("messages", locale);
    }

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

        // Language selection ComboBox
        Label languageLabel = new Label("Kieli:");
        languageLabel.getStyleClass().add("languageLabel");
        ComboBox<String> languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("Suomi", "English", "中文", "Svenska");// Add language options
        languageComboBox.setValue("Suomi"); // Default language

        HBox languageBox = new HBox(20); // Set spacing between Label and ComboBox
        languageBox.getChildren().addAll(languageLabel, languageComboBox);

        Button frontPageButton = createStyledButton("Etusivu");
        Button showRoomsButton = createStyledButton("Huoneiden hallinta");
        Button showCustomersButton = createStyledButton("Asiakasrekisteri");
        Button showVarauksetButton = createStyledButton("Varaukset");
        Button checkInButton = createStyledButton("Check-In");
        Button checkOutButton = createStyledButton("Check-Out");

        Button logoutButton = new Button("Kirjaudu ulos");
        logoutButton.setPrefWidth(200);
        logoutButton.getStyleClass().add("button-log-out");

        // Check if user is an admin
        Button adminButton;
        if (UserSession.getRooli().equals("Admin")) {
            adminButton = new Button("Admin Panel");
            adminButton.setPrefWidth(200);
            adminButton.getStyleClass().add("button-admin");

            // Open AdminGUI on button click
            adminButton.setOnAction(e -> openAdminPanel());
        } else {
            adminButton = null;
        }

        LeftBarOutPut outputGenerator = new LeftBarOutPut();
        languageComboBox.setOnAction(e -> {
            String selectedLanguage = languageComboBox.getValue();
            setSelectedLanguage(selectedLanguage);
            outputGenerator.generateOutput(selectedLanguage, languageLabel, frontPageButton, showRoomsButton, showCustomersButton, showVarauksetButton, checkInButton, checkOutButton, logoutButton, adminButton);

        });

        languageBox = new HBox(20);
        languageBox.getChildren().addAll(languageLabel, languageComboBox);

        VBox leftButtons = new VBox(10);

        leftButtons.getChildren().addAll(languageBox, frontPageButton, showCustomersButton, showVarauksetButton, checkInButton, checkOutButton);
        if (adminButton != null) {
            leftButtons.getChildren().add(adminButton);
        }

        VBox leftBar = new VBox(30);
        leftBar.getChildren().addAll(userBox, leftButtons, logoutButton);
        leftBar.getStyleClass().add("left-bar");

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
        AsiakasSivu asiakasSivu = new AsiakasSivu(selectedLanguage);
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

    public String getSelectedLanguage() {
        return selectedLanguage;
    }

    public void setSelectedLanguage(String selectedLanguage) {
        this.selectedLanguage = selectedLanguage;
        updateUIForSelectedLanguage(selectedLanguage);
    }

    private void updateUIForSelectedLanguage(String selectedLanguage) {
        if ("English".equals(selectedLanguage)) {
            selectedLanguage = "en";
        } else if ("Suomi".equals(selectedLanguage)) {
            selectedLanguage = "fi";
        } else if ("中文".equals(selectedLanguage)) {
            selectedLanguage = "zh";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
