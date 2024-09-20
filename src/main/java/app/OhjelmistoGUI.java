
package app;

import controller.HuoneController;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;

import model.DAO.HuoneDAO;
import model.DAO.AsiakasDAO;
import model.enteties.Huone;
import model.enteties.Asiakas;

import java.util.List;

public class OhjelmistoGUI extends Application {

    private HuoneController huoneController;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hotel Management System");

        huoneController = new HuoneController();

        HBox mainLayout = new HBox(10);
        VBox leftBar = createLeftBar(mainLayout, primaryStage);
        mainLayout.getChildren().addAll(leftBar, createEtusivu());

        Scene scene = new Scene(mainLayout, 900, 600);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    // Creates the left bar with buttons and user info
    private VBox createLeftBar(HBox mainLayout, Stage primaryStage) {
        Label loggedInUsername = new Label("Username");
        Label loggedInImage = new Label("[IMAGE]");
        HBox userBox = new HBox(10);
        userBox.getStyleClass().add("user-box");
        userBox.getChildren().addAll(loggedInImage, loggedInUsername);

        Button frontPageButton = createStyledButton("Etusivu");
        Button showRoomsButton = createStyledButton("Huoneiden hallinta");
        Button showCustomersButton = createStyledButton("Asiakasrekisteri");
        Button checkInButton = createStyledButton("Check-In");
        Button checkOutButton = createStyledButton("Check-Out");

        VBox leftButtons = new VBox(10);
        leftButtons.getChildren().addAll(frontPageButton, showRoomsButton, showCustomersButton, checkInButton, checkOutButton);
        leftButtons.getStyleClass().add("left-buttons");

        Button logoutButton = new Button("Kirjaudu ulos");
        logoutButton.setPrefWidth(200);
        logoutButton.getStyleClass().add("button-log-out");

        VBox leftBar = new VBox(30);
        leftBar.getChildren().addAll(userBox, leftButtons, logoutButton);
        leftBar.getStyleClass().add("left-bar");

        frontPageButton.setOnAction(e -> handleFrontPageButtonAction(mainLayout, leftBar));
        showRoomsButton.setOnAction(e -> handleShowRoomsButtonAction(mainLayout, leftBar));
        showCustomersButton.setOnAction(e -> handleShowCustomersButtonAction(mainLayout, leftBar));
        checkInButton.setOnAction(e -> handleCheckInButtonAction(mainLayout, leftBar));
        checkOutButton.setOnAction(e -> handleCheckOutButtonAction(mainLayout, leftBar));
        logoutButton.setOnAction(e -> handleLogoutButtonAction(primaryStage));

        return leftBar;
    }


    // Creates the content for Etusivu
    private VBox createEtusivu() {
        VBox etusivuInfo = new VBox(10);
        etusivuInfo.getStyleClass().add("info");
        Label etusivuOtsikkoLabel = new Label("Etusivu");
        etusivuOtsikkoLabel.getStyleClass().add("otsikko");
        Label etusivuKuvausLabel = new Label("Täältä voit hallita hotellin huoneita ja asiakkaita sekä tehdä check-in ja check-out toimintoja.");
        etusivuKuvausLabel.setWrapText(true);
        etusivuKuvausLabel.setMaxWidth(400);
        etusivuInfo.getChildren().addAll(etusivuOtsikkoLabel, etusivuKuvausLabel);
        return etusivuInfo;
    }

    // Creates the content for Huoneiden hallinta
    private VBox createHuoneet() {
        VBox huoneetInfo = new VBox(10);
        huoneetInfo.getStyleClass().add("info");
        Label huoneetOtsikkoLabel = new Label("Huoneet");
        huoneetOtsikkoLabel.getStyleClass().add("otsikko");

        TableView<Huone> roomTable = createRoomTable();

        populateRoomTable(roomTable, 1);

        Button addRoomButton = new Button("Lisää uusi huone");
        addRoomButton.setOnAction(e -> openAddRoomWindow(roomTable));

        huoneetInfo.getChildren().addAll(huoneetOtsikkoLabel, roomTable, addRoomButton);
        return huoneetInfo;
    }

    private void openAddRoomWindow(TableView<Huone> roomTable) {
        Stage addRoomStage = new Stage();
        addRoomStage.setTitle("Lisää uusi huone");

        VBox formLayout = new VBox(10);
        formLayout.setAlignment(Pos.CENTER);

        Label numberLabel = new Label("Huoneen Numero:");
        TextField numberField = new TextField();

        Label typeLabel = new Label("Tyyppi:");
        ComboBox<String> typeField = new ComboBox<>();
        typeField.getItems().addAll("Yhden hengen huone", "Kahden hengen huone", "Kolmen hengen huone", "Perhehuone", "Sviitti");
        typeField.setPromptText("Valitse huonetyyppi...");

        Label priceLabel = new Label("Hinta/Yö (€):");
        TextField priceField = new TextField();

        Button saveButton = new Button("Lisää huone");
        saveButton.setOnAction(e -> {
            saveNewRoom(numberField, typeField, priceField, roomTable, addRoomStage);
        });

        formLayout.getChildren().addAll(numberLabel, numberField, typeLabel, typeField, priceLabel, priceField, saveButton);

        Scene scene = new Scene(formLayout, 400, 400);
        addRoomStage.setScene(scene);
        addRoomStage.show();
    }

    private void saveNewRoom(TextField numberField, ComboBox<String> typeField, TextField priceField, TableView<Huone> roomTable, Stage addRoomStage) {
        try {
            int roomNumber = Integer.parseInt(numberField.getText());
            String roomType = typeField.getValue();
            String roomStatus = "Vapaa";
            double roomPrice = Double.parseDouble(priceField.getText());

            huoneController.lisaaHuone(roomNumber, roomType, roomStatus, roomPrice, 1);

            populateRoomTable(roomTable, 1);  // Assuming hotel ID is 1 for this example

            addRoomStage.close();

        } catch (NumberFormatException e) {
            System.out.println("Virheellinen syöte. Tarkista numero- ja hintakentät.");
        }
    }


    // Populate the room table. Runs it in a thread and shows loading indicator.
    private void populateRoomTable(TableView<Huone> roomTable, int hotelliId) {
        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(true);

        roomTable.getItems().clear();
        roomTable.setPlaceholder(loadingIndicator);

        Task<List<Huone>> fetchRoomsTask = new Task<>() {
            @Override
            protected List<Huone> call() throws Exception {
                return huoneController.FindHuoneetByHoteliId(hotelliId);
            }
        };

        fetchRoomsTask.setOnSucceeded(event -> {
            List<Huone> rooms = fetchRoomsTask.getValue();
            if (rooms != null && !rooms.isEmpty()) {
                roomTable.getItems().setAll(rooms);
            } else {
                roomTable.setPlaceholder(new Label("No rooms found for the given hotel ID"));
            }
            loadingIndicator.setVisible(false);
        });

        fetchRoomsTask.setOnFailed(event -> {
            roomTable.setPlaceholder(new Label("Failed to load room data"));
            System.err.println("Failed to fetch rooms: " + fetchRoomsTask.getException());
            loadingIndicator.setVisible(false);
        });

        new Thread(fetchRoomsTask).start();
    }



    // Creates the content for Asiakasrekisteri
    private VBox createAsiakkaat() {
        VBox asiakkaatInfo = new VBox(10);
        asiakkaatInfo.getStyleClass().add("info");
        Label asiakkaatOtsikkoLabel = new Label("Asiakkaat");
        asiakkaatOtsikkoLabel.getStyleClass().add("otsikko");
        TableView<Asiakas> customerTable = createCustomerTable();
        asiakkaatInfo.getChildren().addAll(asiakkaatOtsikkoLabel, customerTable);
        return asiakkaatInfo;
    }

    // Creates the content for Check-in
    private VBox createCheckIn() {
        VBox huoneVarausInfo = new VBox(5);
        huoneVarausInfo.getStyleClass().add("info");

        Label checkInInfoLabel = new Label("Check-In");
        checkInInfoLabel.getStyleClass().add("otsikko");

        VBox huoneTyyppi = new VBox(0);
        Label huoneLabel = new Label("Huoneen tyyppi:");
        ComboBox<String> huoneField = new ComboBox<>();
        huoneField.getItems().addAll(
                "Yhden hengen huone",
                "Kahden hengen huone",
                "Kolmen hengen huone",
                "Perhehuone",
                "Sviitti"
        );
        huoneField.setPromptText("Valitse huonetyyppi...");
        huoneTyyppi.getChildren().addAll(huoneLabel, huoneField);

        VBox tuloPaiva = new VBox(0);
        Label tuloLabel = new Label("Saapumispäivä:");
        DatePicker tuloDatePicker = new DatePicker();
        tuloPaiva.getChildren().addAll(tuloLabel, tuloDatePicker);

        VBox poistumisPaiva = new VBox(0);
        Label poistumisLabel = new Label("Lähtöpäivä:");
        DatePicker poistumisDatePicker = new DatePicker();
        poistumisPaiva.getChildren().addAll(poistumisLabel, poistumisDatePicker);

        VBox paivat = new VBox(0);
        Label paivatLabel = new Label("Päivät:");
        Label paivatValue = new Label("0");
        paivat.getChildren().addAll(paivatLabel, paivatValue);

        VBox huoneHinta = new VBox(0);
        Label hintaLabel = new Label("Huoneen hinta:");
        Label hinta = new Label("0.00 €");
        huoneHinta.getChildren().addAll(hintaLabel, hinta);

        VBox availableRooms = new VBox(10);
        Label availableRoomsTitle = new Label("Vapaat huoneet:");
        availableRoomsTitle.getStyleClass().add("otsikko");
        TableView<Huone> huoneTable = createRoomTable();
        availableRooms.getChildren().addAll(availableRoomsTitle, huoneTable);

        huoneVarausInfo.getChildren().addAll(checkInInfoLabel, huoneTyyppi, tuloPaiva, poistumisPaiva, paivat, huoneHinta);
        HBox huoneTiedot = new HBox(10);
        huoneTiedot.getChildren().addAll(huoneVarausInfo, availableRooms);

        tuloDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            populateFreeRoomTable(huoneTable, 1, tuloDatePicker, poistumisDatePicker);  // Assuming hotel ID is 1 for this example
        });
        poistumisDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            populateFreeRoomTable(huoneTable, 1, tuloDatePicker, poistumisDatePicker);  // Assuming hotel ID is 1 for this example
        });

        VBox checkIn = new VBox(10);
        checkIn.getChildren().addAll(huoneTiedot);
        return checkIn;
    }

    private void populateFreeRoomTable(TableView<Huone> huoneTable, int hotelliId, DatePicker tuloDatePicker, DatePicker poistumisDatePicker) {
        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(true);

        huoneTable.getItems().clear();
        huoneTable.setPlaceholder(loadingIndicator);

        Task<List<Huone>> fetchRoomsTask = new Task<>() {
            @Override
            protected List<Huone> call() throws Exception {
                return huoneController.findVapaatHuoneetByHotelliId(hotelliId, tuloDatePicker.getValue(), poistumisDatePicker.getValue());
            }
        };

        fetchRoomsTask.setOnSucceeded(event -> {
            List<Huone> rooms = fetchRoomsTask.getValue();
            if (rooms != null && !rooms.isEmpty()) {
                huoneTable.getItems().setAll(rooms);
            } else {
                huoneTable.setPlaceholder(new Label("No free rooms found for the given hotel ID"));
            }
            loadingIndicator.setVisible(false);
        });

        fetchRoomsTask.setOnFailed(event -> {
            huoneTable.setPlaceholder(new Label("Failed to load free room data"));
            System.err.println("Failed to fetch free rooms: " + fetchRoomsTask.getException());
            loadingIndicator.setVisible(false);
        });

        new Thread(fetchRoomsTask).start();
    }


    // Creates the content for Check-out
    private VBox createCheckOut() {
        VBox checkOutInfo = new VBox(10);
        checkOutInfo.getStyleClass().add("info");
        Label checkOutInfoLabel = new Label("Check-Out");
        checkOutInfoLabel.getStyleClass().add("otsikko");
        checkOutInfo.getChildren().add(checkOutInfoLabel);
        return checkOutInfo;
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

    // Method to create the Room table view
    private TableView<Huone> createRoomTable() {
        TableView<Huone> roomTable = new TableView<>();

        roomTable.setPrefWidth(500);

        TableColumn<Huone, Integer> idColumn = new TableColumn<>("Huone ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("huone_id"));

        TableColumn<Huone, Integer> numberColumn = new TableColumn<>("Huoneen Numero");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("huone_nro"));

        TableColumn<Huone, String> typeColumn = new TableColumn<>("Tyyppi");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("huone_tyyppi"));

        TableColumn<Huone, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("huone_tila"));

        TableColumn<Huone, Double> priceColumn = new TableColumn<>("Hinta/Yö");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("huone_hinta"));

        roomTable.getColumns().add(idColumn);
        roomTable.getColumns().add(numberColumn);
        roomTable.getColumns().add(typeColumn);
        roomTable.getColumns().add(statusColumn);
        roomTable.getColumns().add(priceColumn);

        return roomTable;
    }

    // Method to create the Customer table view
    private TableView<Asiakas> createCustomerTable() {
        TableView<Asiakas> customerTable = new TableView<>();

        TableColumn<Asiakas, Integer> idColumn = new TableColumn<>("Asiakas ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("asiakasId"));

        TableColumn<Asiakas, String> firstNameColumn = new TableColumn<>("Etunimi");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("etunimi"));

        TableColumn<Asiakas, String> lastNameColumn = new TableColumn<>("Sukunimi");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("sukunimi"));

        TableColumn<Asiakas, String> emailColumn = new TableColumn<>("Sähköposti");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("sposti"));

        TableColumn<Asiakas, String> phoneColumn = new TableColumn<>("Puhelin");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("puh"));

        customerTable.getColumns().add(idColumn);
        customerTable.getColumns().add(firstNameColumn);
        customerTable.getColumns().add(lastNameColumn);
        customerTable.getColumns().add(emailColumn);
        customerTable.getColumns().add(phoneColumn);

        return customerTable;
    }

    // Button Actions
    private void handleFrontPageButtonAction(HBox mainLayout, VBox leftBar) {
        updateMainLayout(mainLayout, leftBar, createEtusivu());
    }

    private void handleShowRoomsButtonAction(HBox mainLayout, VBox leftBar) {
        updateMainLayout(mainLayout, leftBar, createHuoneet());
    }

    private void handleShowCustomersButtonAction(HBox mainLayout, VBox leftBar) {
        updateMainLayout(mainLayout, leftBar, createAsiakkaat());
    }

    private void handleCheckInButtonAction(HBox mainLayout, VBox leftBar) {
        updateMainLayout(mainLayout, leftBar, createCheckIn());
    }

    private void handleCheckOutButtonAction(HBox mainLayout, VBox leftBar) {
        updateMainLayout(mainLayout, leftBar, createCheckOut());
    }

    private void handleLogoutButtonAction(Stage primaryStage) {
        primaryStage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
