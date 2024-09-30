package app;

import controller.HuoneController;
import controller.VarausController;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;

import model.DAO.AsiakasDAO;
import model.enteties.Huone;
import model.enteties.Asiakas;
import model.enteties.Varaus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OhjelmistoGUI extends Application {

    private HuoneController huoneController;
    private VarausController varausController;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hotel Management System");

        huoneController = new HuoneController();
        varausController = new VarausController();

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

        frontPageButton.setOnAction(e -> handleFrontPageButtonAction(mainLayout, leftBar));
        showRoomsButton.setOnAction(e -> handleShowRoomsButtonAction(mainLayout, leftBar));
        showCustomersButton.setOnAction(e -> handleShowCustomersButtonAction(mainLayout, leftBar));
        showVarauksetButton.setOnAction(e -> handleShowVarauksetButtonAction(mainLayout, leftBar));
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

    private VBox createVaraukset() {
        VBox varauksetInfo = new VBox(10);
        varauksetInfo.getStyleClass().add("info");
        Label varauksetOtsikkoLabel = new Label("Varaukset");
        varauksetOtsikkoLabel.getStyleClass().add("otsikko");

        HBox varausBox = new HBox(10);

        VBox luoVaraus = new VBox(0);
        Label luoVarausLabel = new Label("Luo varaus");

        VBox asiakasTiedot = new VBox(0);

        Label asiakasEtunimiLabel = new Label("Asiakas Etunimi:");
        TextField asiakasEtunimiField = new TextField();

        Label asiakasSukunimiLabel = new Label("Asiakas Sukunimi:");
        TextField asiakasSukunimiField = new TextField();

        Label asiakasSpostiLabel = new Label("Asiakas Sähköposti:");
        TextField asiakasSpostiField = new TextField();

        Label asiakasPuhLabel = new Label("Asiakas Puhelin:");
        TextField asiakasPuhField = new TextField();

        Label asiakasLisatiedotLabel = new Label("Lisätiedot:");
        TextField asiakasLisatiedotField = new TextField();

        Button etsiAsiakasButton = new Button("Etsi asiakas");

        asiakasTiedot.getChildren().addAll(
                asiakasEtunimiLabel, asiakasEtunimiField,
                asiakasSukunimiLabel, asiakasSukunimiField,
                asiakasSpostiLabel, asiakasSpostiField,
                asiakasPuhLabel, asiakasPuhField,
                asiakasLisatiedotLabel, asiakasLisatiedotField,
                etsiAsiakasButton);

        etsiAsiakasButton.setOnAction(e -> {
            openCustomerSearchWindow(asiakasEtunimiField, asiakasSukunimiField, asiakasSpostiField, asiakasPuhField);
        });

        VBox laskuTiedot = new VBox(0);

        Label laskuMuotoLabel = new Label("Laskun Muoto:");
        ComboBox<String> laskuMuotoField = new ComboBox<>();
        laskuMuotoField.setPromptText("Valitse laskun muoto...");
        laskuMuotoField.getItems().addAll("All Inclusive", "Bed & Breakfast", "Room Only");

        laskuTiedot.getChildren().addAll(laskuMuotoLabel, laskuMuotoField);

        VBox varausTiedot = new VBox(0);

        Label saapumisPvmLabel = new Label("Saapumispäivä:");
        DatePicker saapumisPvmField = new DatePicker();

        Label lahtoPvmLabel = new Label("Lähtöpäivä:");
        DatePicker lahtoPvmField = new DatePicker();

        varausTiedot.getChildren().addAll(saapumisPvmLabel, saapumisPvmField, lahtoPvmLabel, lahtoPvmField);

        Button luoVarausButton = new Button("Luo varaus");

        luoVaraus.getChildren().addAll(
                luoVarausLabel, asiakasTiedot, laskuTiedot, varausTiedot, luoVarausButton);

        TableView<Varaus> varausTable = createVarausTable();
        populateVarausTable(varausTable, null, null);

        luoVarausButton.setOnAction(e -> {
            try {
                String asiakasEtunimi = asiakasEtunimiField.getText();
                String asiakasSukunimi = asiakasSukunimiField.getText();
                String asiakasEmail = asiakasSpostiField.getText();
                String asiakasPuh = asiakasPuhField.getText();
                String huomio = asiakasLisatiedotField.getText();
                String laskuMuoto = laskuMuotoField.getValue();
                LocalDate saapumisPvm = saapumisPvmField.getValue();
                LocalDate lahtoPvm = lahtoPvmField.getValue();

                System.out.println("Creating reservation...");
                varausController.createVaraus(asiakasEtunimi, asiakasSukunimi, asiakasEmail, asiakasPuh, huomio, laskuMuoto, saapumisPvm, lahtoPvm);
                System.out.println("Reservation created successfully");
                populateVarausTable(varausTable, null, null);
            } catch (Exception ex) {
                System.err.println("Failed to create reservation: " + ex.getMessage());
            }
        });

        varausBox.getChildren().addAll(luoVaraus, varausTable);

        varauksetInfo.getChildren().addAll(varauksetOtsikkoLabel, varausBox);

        return varauksetInfo;
    }

    private void openCustomerSearchWindow(TextField firstNameField, TextField lastNameField, TextField emailField, TextField phoneField) {
        Stage searchCustomerStage = new Stage();
        searchCustomerStage.setTitle("Etsi asiakas");

        VBox searchLayout = new VBox(10);
        searchLayout.setAlignment(Pos.CENTER);

        Label searchLabel = new Label("Etsi asiakas");
        TextField searchField = new TextField();
        Button searchButton = new Button("Etsi");

        TableView<Asiakas> searchResults = createCustomerTable();
        searchResults.setPrefWidth(500);

        HBox pageButtons = new HBox(10);
        Button previousButton = new Button("Edellinen");
        Button nextButton = new Button("Seuraava");
        pageButtons.getChildren().addAll(previousButton, nextButton);

        Button selectButton = new Button("Valitse");

        searchLayout.getChildren().addAll(searchLabel, searchField, searchButton, searchResults, pageButtons, selectButton);

        final int[] currentPage = {1};
        final int resultsPerPage = 20;
        List<Asiakas> allCustomers = new ArrayList<>();

        searchButton.setOnAction(e -> {
            String searchQuery = searchField.getText();
            currentPage[0] = 1; // Reset to the first page
            allCustomers.clear();
            searchCustomers(searchResults, searchQuery, allCustomers, currentPage[0], resultsPerPage, previousButton, nextButton);
        });

        previousButton.setOnAction(e -> {
            if (currentPage[0] > 1) {
                currentPage[0]--;
                updateCustomerTable(searchResults, allCustomers, currentPage[0], resultsPerPage, previousButton, nextButton);
            }
        });

        nextButton.setOnAction(e -> {
            if (currentPage[0] * resultsPerPage < allCustomers.size()) {
                currentPage[0]++;
                updateCustomerTable(searchResults, allCustomers, currentPage[0], resultsPerPage, previousButton, nextButton);
            }
        });

        selectButton.setOnAction(e -> {
            Asiakas selectedCustomer = searchResults.getSelectionModel().getSelectedItem();
            if (selectedCustomer != null) {
                firstNameField.setText(selectedCustomer.getEtunimi());
                lastNameField.setText(selectedCustomer.getSukunimi());
                emailField.setText(selectedCustomer.getSposti());
                phoneField.setText(selectedCustomer.getPuh());

                searchCustomerStage.close();
            }
        });

        Scene scene = new Scene(searchLayout, 500, 400);
        searchCustomerStage.setScene(scene);
        searchCustomerStage.show();
    }

    private void searchCustomers(TableView<Asiakas> searchResults, String searchQuery, List<Asiakas> allCustomers, int currentPage, int resultsPerPage, Button previousButton, Button nextButton) {
        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(true);
        searchResults.getItems().clear();
        searchResults.setPlaceholder(loadingIndicator);

        Task<List<Asiakas>> searchCustomersTask = new Task<>() {
            @Override
            protected List<Asiakas> call() throws Exception {
                // Create DAO object for database access
                AsiakasDAO asiakasDAO = new AsiakasDAO();
                return asiakasDAO.findAsiakasByKeyword(searchQuery);
            }
        };

        searchCustomersTask.setOnSucceeded(event -> {
            List<Asiakas> customers = searchCustomersTask.getValue();
            allCustomers.clear();
            if (customers != null && !customers.isEmpty()) {
                allCustomers.addAll(customers);
                updateCustomerTable(searchResults, allCustomers, currentPage, resultsPerPage, previousButton, nextButton);
            } else {
                searchResults.setPlaceholder(new Label("No customers found for the given search query"));
            }
            loadingIndicator.setVisible(false);
        });

        searchCustomersTask.setOnFailed(event -> {
            searchResults.setPlaceholder(new Label("Failed to load customer data"));
            System.err.println("Failed to fetch customers: " + searchCustomersTask.getException());
            loadingIndicator.setVisible(false);
        });

        new Thread(searchCustomersTask).start();
    }

    private void updateCustomerTable(TableView<Asiakas> table, List<Asiakas> allCustomers, int currentPage, int resultsPerPage, Button previousButton, Button nextButton) {
        int startIndex = (currentPage - 1) * resultsPerPage;
        int endIndex = Math.min(startIndex + resultsPerPage, allCustomers.size());

        List<Asiakas> currentPageCustomers = allCustomers.subList(startIndex, endIndex);
        table.getItems().setAll(currentPageCustomers);

        previousButton.setDisable(currentPage <= 1);
        nextButton.setDisable(endIndex >= allCustomers.size());
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
        tuloDatePicker.setValue(LocalDate.now());
        tuloPaiva.getChildren().addAll(tuloLabel, tuloDatePicker);

        VBox poistumisPaiva = new VBox(0);
        Label poistumisLabel = new Label("Lähtöpäivä:");
        DatePicker poistumisDatePicker = new DatePicker();
        poistumisDatePicker.setValue(LocalDate.now().plusDays(1));
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
        huoneTable.setPrefWidth(400);
        huoneTable.setPrefHeight(250);
        availableRooms.getChildren().addAll(availableRoomsTitle, huoneTable);

        huoneVarausInfo.getChildren().addAll(checkInInfoLabel, huoneTyyppi, tuloPaiva, poistumisPaiva, paivat, huoneHinta);
        HBox huoneTiedot = new HBox(10);
        huoneTiedot.getChildren().addAll(huoneVarausInfo, availableRooms);

        tuloDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (tuloDatePicker.getValue() != null && poistumisDatePicker.getValue() != null) {
                paivatValue.setText(String.valueOf(tuloDatePicker.getValue().until(poistumisDatePicker.getValue()).getDays()));
            }
        });
        poistumisDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (tuloDatePicker.getValue() != null && poistumisDatePicker.getValue() != null) {
                paivatValue.setText(String.valueOf(tuloDatePicker.getValue().until(poistumisDatePicker.getValue()).getDays()));
            }
        });

        huoneTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                double price = newValue.getHuone_hinta() * Integer.parseInt(paivatValue.getText());
                hinta.setText(String.format("%.2f €", price));
            }
        });

        VBox varausInfo = new VBox(5);
        Label varausInfoLabel = new Label("Varaus");
        varausInfoLabel.getStyleClass().add("otsikko");

        TableView<Varaus> varausTable = createVarausTable();
        varausTable.setPrefWidth(400);
        varausTable.setPrefHeight(200);
        populateVarausTable(varausTable, tuloDatePicker.getValue(), poistumisDatePicker.getValue());

        varausInfo.getChildren().addAll(varausInfoLabel, varausTable);

        Button checkInButton = new Button("Check-In");

        tuloDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            populateFreeRoomTable(huoneTable, 1, tuloDatePicker, poistumisDatePicker);
            populateVarausTable(varausTable, tuloDatePicker.getValue(), poistumisDatePicker.getValue());
        });
        poistumisDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            populateFreeRoomTable(huoneTable, 1, tuloDatePicker, poistumisDatePicker);
            populateVarausTable(varausTable, tuloDatePicker.getValue(), poistumisDatePicker.getValue());
        });

        checkInButton.setOnAction(e -> {
            Huone selectedRoom = huoneTable.getSelectionModel().getSelectedItem();
            Varaus selectedVaraus = varausTable.getSelectionModel().getSelectedItem();
            if (selectedRoom != null && selectedVaraus != null) {
                huoneController.updateHuoneStatusById(selectedRoom.getHuone_id(), "Varattu");
                varausController.updateVarausHuoneById(selectedVaraus.getVarausId(), selectedRoom.getHuone_id());
            }
        });
        VBox checkIn = new VBox(20);
        checkIn.getChildren().addAll(huoneTiedot, varausInfo, checkInButton);
        return checkIn;
    }

    private void populateFreeRoomTable(TableView<Huone> huoneTable, int hotelliId, DatePicker tuloDatePicker, DatePicker poistumisDatePicker) {
        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(true);
        huoneTable.getItems().clear();
        huoneTable.setPlaceholder(loadingIndicator);

        if (tuloDatePicker.getValue() != null && poistumisDatePicker.getValue() != null && tuloDatePicker.getValue().isAfter(poistumisDatePicker.getValue())) {
            huoneTable.setPlaceholder(new Label("Check-out date must be after check-in date"));
            loadingIndicator.setVisible(false);
            return;
        }
        Task<List<Huone>> fetchRoomsTask = new Task<>() {
            @Override
            protected List<Huone> call() throws Exception {
                return huoneController.findVapaatHuoneetByHotelliId(hotelliId);
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
    private void populateVarausTable(TableView<Varaus> varausTable, LocalDate alkuPvm, LocalDate loppuPvm) {
        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(true);
        loadingIndicator.setPrefSize(50, 50);

        varausTable.getItems().clear();
        varausTable.setPlaceholder(loadingIndicator);

        LocalDate startDate = (alkuPvm != null) ? alkuPvm : LocalDate.of(1970, 1, 1);
        LocalDate endDate = (loppuPvm != null) ? loppuPvm : LocalDate.of(2100, 1, 1);

        Task<List<Varaus>> fetchVarauksetTask = new Task<>() {
            @Override
            protected List<Varaus> call() throws Exception {
                return varausController.findVarauksetByDate(startDate, endDate);
            }
        };

        fetchVarauksetTask.setOnSucceeded(event -> {
            List<Varaus> varaukset = fetchVarauksetTask.getValue();
            loadingIndicator.setVisible(false);

            if (varaukset != null && !varaukset.isEmpty()) {
                varausTable.getItems().setAll(varaukset);
            } else {
                varausTable.setPlaceholder(new Label("No reservations found for the given dates"));
            }
        });

        fetchVarauksetTask.setOnFailed(event -> {
            varausTable.setPlaceholder(new Label("Failed to load reservation data"));
            System.err.println("Failed to fetch reservations: " + fetchVarauksetTask.getException());
            loadingIndicator.setVisible(false);
        });

        new Thread(fetchVarauksetTask).start();
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

    private TableView<Varaus> createVarausTable(){
        TableView<Varaus> varausTable = new TableView<>();

        TableColumn<Varaus, Integer> idColumn = new TableColumn<>("Varaus ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("varausId"));

        TableColumn<Varaus, Integer> roomColumn = new TableColumn<>("Huone ID");
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("huoneId"));

        TableColumn<Varaus, Integer> invoiceColumn = new TableColumn<>("Lasku ID");
        invoiceColumn.setCellValueFactory(new PropertyValueFactory<>("laskuId"));

        TableColumn<Varaus, String> startDateColumn = new TableColumn<>("Alkupäivämäärä");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("alkuPvm"));

        TableColumn<Varaus, String> endDateColumn = new TableColumn<>("Loppupäivämäärä");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("loppuPvm"));

        varausTable.getColumns().add(idColumn);
        varausTable.getColumns().add(roomColumn);
        varausTable.getColumns().add(invoiceColumn);
        varausTable.getColumns().add(startDateColumn);
        varausTable.getColumns().add(endDateColumn);

        return varausTable;
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

    private void handleShowVarauksetButtonAction(HBox mainLayout, VBox leftBar) {
        updateMainLayout(mainLayout, leftBar, createVaraukset());
    }

    private void handleCheckInButtonAction(HBox mainLayout, VBox leftBar) {
        updateMainLayout(mainLayout, leftBar, createCheckIn());
    }

    private void handleCheckOutButtonAction(HBox mainLayout, VBox leftBar) {
        updateMainLayout(mainLayout, leftBar, createCheckOut());
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