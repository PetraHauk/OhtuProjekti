package app;
/*
import controller.HuoneController;
import controller.AsiakasController;
import controller.VarausController;
import controller.LaskuController;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;

import model.enteties.Huone;
import model.enteties.Asiakas;
import model.enteties.Varaus;
import model.enteties.Lasku;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

public class OhjelmistoGUI extends Application {

    private HuoneController huoneController;
    private AsiakasController asiakasController;
    private VarausController varausController;
    private LaskuController laskuController;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hotel Management System");

        huoneController = new HuoneController();
        asiakasController = new AsiakasController();
        varausController = new VarausController();
        laskuController = new LaskuController();

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

        VBox checkIn = new VBox(10);
        checkIn.getChildren().addAll(huoneTiedot);
        return checkIn;
    }

    // Creates the content for Check-out
    private VBox createCheckOut() {
        VBox checkOutInfo = new VBox(10);
        checkOutInfo.getStyleClass().add("info");
        Label checkOutInfoLabel = new Label("Check-Out");
        checkOutInfoLabel.getStyleClass().add("otsikko");

        // Customer first name input
        VBox etunimiInfo = new VBox(5);
        Label asiakasEtunimiLabel = new Label("Asiakkaan etunimi:");
        TextField asiakasEtunimiInput = new TextField();
        asiakasEtunimiInput.setPromptText("Syötä asiakkaan etunimi");
        etunimiInfo.getChildren().addAll(asiakasEtunimiLabel, asiakasEtunimiInput);

        // Customer last name input
        VBox sukunimiInfo = new VBox(5);
        Label asiakasSukunimiLabel = new Label("Asiakkaan sukunimi:");
        TextField asiakasSukunimiInput = new TextField();
        asiakasSukunimiInput.setPromptText("Syötä asiakkaan sukunimi");
        sukunimiInfo.getChildren().addAll(asiakasSukunimiLabel, asiakasSukunimiInput);

        // Room number input
        VBox huoneNroInfo = new VBox(5);
        Label huoneNroLabel = new Label("Huoneen numero:");
        TextField huoneNroInput = new TextField();
        huoneNroInput.setPromptText("Syötä huoneen numero");
        huoneNroInfo.getChildren().addAll(huoneNroLabel, huoneNroInput);

        // Button for fetching bills
        Button haeLaskutButton = new Button("Hae laskut");

        // VBox for the table
        VBox maksattavaLaskut = new VBox(10);
        Label maksattavaLaskuOtsikko = new Label("Laskut");
        maksattavaLaskuOtsikko.getStyleClass().add("otsikko");
        TableView<String> laskuTable = createLaskuTable();
        maksattavaLaskut.getChildren().addAll(maksattavaLaskuOtsikko, laskuTable);

        // Button action handler
        haeLaskutButton.setOnAction(e -> {
            // Clear table before loading new data
            laskuTable.getItems().clear();


            // Check if the room number is a valid integer
            int huoneNro;
            try {
                huoneNro = Integer.parseInt(huoneNroInput.getText());
            } catch (NumberFormatException ex) {
                System.out.println("Invalid room number format.");
                return;
            }

            // Fetch customers by names
            List<Asiakas> asiakkaat = asiakasController.findIdByNimet(asiakasEtunimiInput.getText(), asiakasSukunimiInput.getText());
            if (asiakkaat == null || asiakkaat.isEmpty()) {
                System.out.println("No customers found for the given name.");
                return;
            }

            double kokonaishinta = 0.00;

            for (Asiakas asiakas : asiakkaat) {
                List<Lasku> laskut = laskuController.findLaskuByAsiakasId(asiakas.getAsiakasId());
                if (laskut != null) {
                    double hinta = 0;
                    int laskuId = 0;
                    String maksuStatus = null;
                    String varausMuoto = null;
                    String valuutta = null;
                    String alkuPvmStr = null;
                    String loppuPvmStr = null;
                    int paivat = 0;

                    String laskuIdStr = null;
                    for (Lasku lasku : laskut) {
                        laskuId = lasku.getLaskuId();
                        laskuIdStr = Integer.toString(laskuId);
                        maksuStatus = lasku.getMaksuStatus();
                        varausMuoto = lasku.getVarausMuoto();
                        valuutta = lasku.getValuutta();

                        List<Varaus> varaukset = varausController.findByLaskuId(lasku.getLaskuId());

                        for (Varaus varaus : varaukset) {
                            LocalDate alkuPvm = varaus.getAlkuPvm(); // assuming varaus.getAlkuPvm() returns a LocalDate
                            alkuPvmStr = alkuPvm.toString();
                            LocalDate loppuPvm = varaus.getLoppuPvm(); // assuming varaus.getLoppuPvm() returns a LocalDate
                            loppuPvmStr = loppuPvm.toString();
                            paivat = Period.between(alkuPvm, loppuPvm).getDays();

                            // Check if room number matches the current reservation
                            Huone huone = huoneController.findHuoneByNro(huoneNro);
                            if (huone != null) {
                                hinta = huone.getHuone_hinta();
                                double summa = hinta * paivat;
                                kokonaishinta += summa;

                            } else {
                                System.out.println("Invalid room number.");
                            }
                        }
                    }
                    populateLaskuTable(laskuTable, laskuIdStr, maksuStatus, varausMuoto, valuutta, alkuPvmStr, loppuPvmStr, paivat, hinta, kokonaishinta);
                }
            }
        });


        // Layout structure
        checkOutInfo.getChildren().addAll(checkOutInfoLabel, etunimiInfo, sukunimiInfo, huoneNroInfo, haeLaskutButton);
        HBox laskuTiedot = new HBox(10);
        laskuTiedot.getChildren().addAll(checkOutInfo, maksattavaLaskut);

        VBox checkOut = new VBox(10);
        checkOut.getChildren().addAll(laskuTiedot);

        return checkOut;
    }


    // Corrected TableView method to ensure correct data types
    private TableView<String> createLaskuTable() {
        TableView<String> laskuTable = new TableView<>();
        laskuTable.setPrefWidth(650);

        TableColumn<String, Integer> laskuIdColumn = new TableColumn<>("Lasku ID");
        laskuIdColumn.setCellValueFactory(new PropertyValueFactory<>("laskuId"));

        TableColumn<String, String> maksuStatusColumn = new TableColumn<>("Maksu Status");
        maksuStatusColumn.setCellValueFactory(new PropertyValueFactory<>("maksuStatus"));

        TableColumn<String, String> varausMuotoColumn = new TableColumn<>("Varaus Muoto");
        varausMuotoColumn.setCellValueFactory(new PropertyValueFactory<>("varausMuoto"));

        TableColumn<String, String> valuuttaColumn = new TableColumn<>("Valuutta");
        valuuttaColumn.setCellValueFactory(new PropertyValueFactory<>("valuutta"));

        TableColumn<String, String> alkuPvmColumn = new TableColumn<>("Alku Pvm");
        alkuPvmColumn.setCellValueFactory(new PropertyValueFactory<>("alkuPvm"));

        TableColumn<String, String> loppuPvmColumn = new TableColumn<>("Loppu Pvm");
        loppuPvmColumn.setCellValueFactory(new PropertyValueFactory<>("loppuPvm"));

        TableColumn<String, Integer> paivatColumn = new TableColumn<>("Päivät");
        paivatColumn.setCellValueFactory(new PropertyValueFactory<>("paivat"));

        TableColumn<String, Double> hintaColumn = new TableColumn<>("Hinta");
        hintaColumn.setCellValueFactory(new PropertyValueFactory<>("hinta"));

        TableColumn<String, Double> kokonaishintaColumn = new TableColumn<>("Kokonaishinta");
        kokonaishintaColumn.setCellValueFactory(new PropertyValueFactory<>("kokonaishinta"));

        laskuTable.getColumns().addAll(
                laskuIdColumn, maksuStatusColumn, varausMuotoColumn,
                valuuttaColumn, alkuPvmColumn, loppuPvmColumn,
                paivatColumn, hintaColumn, kokonaishintaColumn);
        return laskuTable;
    }

    private void populateLaskuTable(TableView<String> laskuTable, String laskuIdStr, String maksuStatus, String varausMuoto, String valuutta, String alkuPvmStr, String loppuPvmStr, int paivat, double hinta, double kokonaishinta) {

        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(true);

        laskuTable.getItems().clear();
        laskuTable.setPlaceholder(loadingIndicator);

        Task<List<String>> fetchLaskutTask = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                List<String> laskuData = new ArrayList<>();
                laskuData.add(laskuIdStr);
                laskuData.add(maksuStatus);
                laskuData.add(varausMuoto);
                laskuData.add(valuutta);
                laskuData.add(alkuPvmStr);
                laskuData.add(loppuPvmStr);
                laskuData.add(Integer.toString(paivat));
                laskuData.add(Double.toString(hinta));
                laskuData.add(Double.toString(kokonaishinta));
                return laskuData;
            }
        };

        fetchLaskutTask.setOnSucceeded(event -> {
            List<String> laskut = fetchLaskutTask.getValue();
            if (laskut != null && !laskut.isEmpty()) {
                laskuTable.getItems().setAll(laskut);
            } else {
                laskuTable.setPlaceholder(new Label("No bills found for the given room number"));
            }
            loadingIndicator.setVisible(false);
        });
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


 */