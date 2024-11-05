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

import java.util.Locale;
import java.util.ResourceBundle;

import controller.*;
import view.sivut.*;
import model.service.LocaleManager;
import view.sivut.outPut.LeftBarOutPut;

// Loppusumma kommentoitu pois koska punasta viivaa
public class OhjelmistoGUI extends Application {

    //private String selectedLanguage = "Suomi";
    private HuoneController huoneController;
    private VarausController varausController;
    private AsiakasController asiakasController;
    private LaskuController laskuController;
    private HotelliController hotelliController;
    private KayttajaController kayttajaController;
    private ResourceBundle bundle;
    private String selectedlanguage;
    private LeftBarOutPut leftBarOutPut;
    private LocaleManager localeManager;

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
        leftBarOutPut = new LeftBarOutPut();
        localeManager = new LocaleManager();

        selectedlanguage = localeManager.getLanguageName();

        Etusivu etusivu = new Etusivu();
        HBox mainLayout = new HBox(10);
        VBox leftBar = createLeftBar(mainLayout, primaryStage);
        mainLayout.getChildren().addAll(leftBar, etusivu.createEtusivu(1));

        Scene scene = new Scene(mainLayout, 1250, 630);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createLeftBar(HBox mainLayout, Stage primaryStage) {
        // Display the username and a placeholder for the profile image
        Label loggedInUsername = new Label(UserSession.getUsername());
        Label loggedInImage = new Label("[IMAGE]");
        HBox userBox = new HBox(10);
        userBox.getStyleClass().add("user-box");
        userBox.getChildren().addAll(loggedInImage, loggedInUsername);

        // Create the buttons for the left sidebar
        Button frontPageButton = createStyledButton("Etusivu");
        Button showRoomsButton = createStyledButton("Huoneiden hallinta");
        Button showCustomersButton = createStyledButton("Asiakasrekisteri");
        Button showVarauksetButton = createStyledButton("Varaukset");
        Button checkInButton = createStyledButton("Check-In");
        Button checkOutButton = createStyledButton("Check-Out");

        // Add the Admin Panel button only if the user is an admin
        Button adminButton = new Button("Admin Panel");
        adminButton.setPrefWidth(200);
        adminButton.getStyleClass().add("button-admin");

        // Log out button
        Button logOutButton = new Button("Kirjaudu ulos");
        logOutButton.setPrefWidth(200);
        logOutButton.getStyleClass().add("button-log-out");
        logOutButton.setOnAction(e -> handleLogoutButtonAction(primaryStage));

        VBox leftButtons = new VBox(10);
        leftBarOutPut.generateOutput(selectedlanguage, frontPageButton, showRoomsButton, showCustomersButton, showVarauksetButton, checkInButton, checkOutButton, logOutButton, adminButton);
        leftButtons.getChildren().addAll(
                frontPageButton,
                showRoomsButton,
                showCustomersButton,
                showVarauksetButton,
                checkInButton,
                checkOutButton
        );
        leftButtons.getStyleClass().add("left-buttons");

        if (UserSession.getRooli().equalsIgnoreCase("Admin")) {
            // Only show admin panel button for admin users
            adminButton.setOnAction(e -> openAdminPanel());
            leftButtons.getChildren().add(adminButton);
        }

        // Create the left sidebar layout
        VBox leftBar = new VBox(30);
        leftBar.getStyleClass().add("left-bar");
        leftBar.getChildren().addAll(userBox, leftButtons, logOutButton);

        // Set up action listeners for the other navigation buttons
        frontPageButton.setOnAction(e -> handleFrontPageButtonAction(mainLayout, leftBar));
        showRoomsButton.setOnAction(e -> handleShowRoomsButtonAction(mainLayout, leftBar));
        showCustomersButton.setOnAction(e -> handleShowCustomersButtonAction(mainLayout, leftBar));
        showVarauksetButton.setOnAction(e -> handleShowVarauksetButtonAction(mainLayout, leftBar));
        checkInButton.setOnAction(e -> handleCheckInButtonAction(mainLayout, leftBar));
        checkOutButton.setOnAction(e -> handleCheckOutButtonAction(mainLayout, leftBar));

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
        AsiakasSivu asiakasSivu = new AsiakasSivu(selectedlanguage);
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
        CheckOut checkOut = new CheckOut(selectedlanguage);
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