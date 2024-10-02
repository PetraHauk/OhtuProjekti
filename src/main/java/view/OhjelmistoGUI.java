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

        huoneController = new HuoneController();
        asiakasController = new AsiakasController();
        varausController = new VarausController();
        laskuController = new LaskuController();
        hotelliController = new HotelliController();

        HBox mainLayout = new HBox(10);
        VBox leftBar = createLeftBar(mainLayout, primaryStage);
        mainLayout.getChildren().addAll(leftBar, createEtusivu());

        Scene scene = new Scene(mainLayout, 1250, 600);
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

        TableView<Huone> roomTable = createHuoneTable();

        // Assuming hotel ID is 1 for this example
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
        Button addCustomerButton = new Button("Lisää uusi asiakas");

        addCustomerButton.setOnAction(e -> openAddCustomerWindow());

        TableView<Asiakas> customerTable = createCustomerTable();
        populateCustomerTable(customerTable);
        asiakkaatInfo.getChildren().addAll(asiakkaatOtsikkoLabel, customerTable, addCustomerButton);
        return asiakkaatInfo;
    }

    private void openAddCustomerWindow() {
        Stage addCustomerStage = new Stage();
        addCustomerStage.setTitle("Lisää uusi asiakas");

        // Pääasettelu, joka jakaa ikkunan osiin (yläosa, keskiosa, alaosa)
        BorderPane borderPane = new BorderPane();

        // Lomakkeen kenttien asettelu pystysuoraan (VBox)
        VBox formLayout = new VBox(10);
        formLayout.setAlignment(Pos.CENTER_LEFT); // Keskitetään vasemmalle
        formLayout.setPadding(new Insets(20)); // Asetetaan täyte

        // Kenttien ja tekstikenttien luonti
        Label firstNameLabel = new Label("Etunimi:");
        TextField firstNameField = new TextField();

        Label lastNameLabel = new Label("Sukunimi:");
        TextField lastNameField = new TextField();

        Label emailLabel = new Label("Sähköposti:");
        TextField emailField = new TextField();

        Label phoneLabel = new Label("Puhelin:");
        TextField phoneField = new TextField();

        Label henkiloMaaraLabel = new Label("Henkilömäärä:");
        TextField henkiloMaaraField = new TextField();

        Label huomioLabel = new Label("Huomio:");
        TextField huomioField = new TextField();

        // Lisätään kentät lomakkeeseen (VBox)
        formLayout.getChildren().addAll(
                firstNameLabel, firstNameField,
                lastNameLabel, lastNameField,
                emailLabel, emailField,
                phoneLabel, phoneField,
                henkiloMaaraLabel, henkiloMaaraField,
                huomioLabel, huomioField
        );

        Button saveButton = new Button("Lisää uusi asiakas");

        // Toiminnallisuus napille
        saveButton.setOnAction(e -> {
            asiakasController.addAsiakas(
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    Integer.parseInt(henkiloMaaraField.getText()), // Muunnetaan teksti kokonaisluvuksi
                    huomioField.getText());
        });

        // Asetetaan lomake BorderPane:n keskelle
        borderPane.setCenter(formLayout);

        // Asetetaan painike BorderPane:n alaosaan ja lisätään täyte nappulan yläpuolelle
        BorderPane.setMargin(saveButton, new Insets(10, 10, 20, 10)); // Marginaalit
        borderPane.setBottom(saveButton);
        BorderPane.setAlignment(saveButton, Pos.CENTER); // Keskitetään nappi alaosaan

        // Luodaan ja näytetään käyttöliittymän näkymä (Scene)
        Scene scene = new Scene(borderPane, 400, 460);
        addCustomerStage.setScene(scene);
        addCustomerStage.show();
    }

    // Populate the customer table. Runs it in a thread and shows loading indicator.
    private void populateCustomerTable(TableView<Asiakas> customerTable) {
        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(true);
        customerTable.getItems().clear();
        customerTable.setPlaceholder(loadingIndicator);

        Task<List<Asiakas>> fetchCustomersTask = new Task<>() {
            @Override
            protected List<Asiakas> call() throws Exception {
                return asiakasController.findAllAsiakkaat();
            }
        };

        fetchCustomersTask.setOnSucceeded(event -> {
            List<Asiakas> customers = fetchCustomersTask.getValue();
            if (customers != null && !customers.isEmpty()) {
                customerTable.getItems().setAll(customers);
            } else {
                customerTable.setPlaceholder(new Label("No customers found"));
            }
            loadingIndicator.setVisible(false);
        });

        fetchCustomersTask.setOnFailed(event -> {
            customerTable.setPlaceholder(new Label("Failed to load customer data"));
            System.err.println("Failed to fetch customers: " + fetchCustomersTask.getException());
            loadingIndicator.setVisible(false);
        });

        new Thread(fetchCustomersTask).start();
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
        TableView<Huone> huoneTable = createHuoneTable();
        huoneTable.setPrefWidth(500);
        huoneTable.setPrefHeight(250);
        availableRooms.getChildren().addAll(availableRoomsTitle, huoneTable);

        huoneVarausInfo.getChildren().addAll(checkInInfoLabel, huoneTyyppi, tuloPaiva, poistumisPaiva, paivat, huoneHinta);
        HBox huoneTiedot = new HBox(10);
        huoneTiedot.getChildren().addAll(huoneVarausInfo, availableRooms);

        VBox varausInfo = new VBox(5);
        Label varausInfoLabel = new Label("Varaus");
        varausInfoLabel.getStyleClass().add("otsikko");

        TableView<Varaus> varausTable = createVarausTable();
        varausTable.setPrefWidth(500);
        varausTable.setPrefHeight(200);
        populateVarausTable(varausTable, tuloDatePicker.getValue(), poistumisDatePicker.getValue());

        varausInfo.getChildren().addAll(varausInfoLabel, varausTable);

        Button checkInButton = new Button("Check-In");

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

        varausTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                poistumisDatePicker.setValue(newValue.getLoppuPvm());
            }
        });

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
        VBox checkOutInfo = new VBox(15);
        checkOutInfo.getStyleClass().add("info");
        Label checkOutInfoLabel = new Label("Check-Out");
        checkOutInfoLabel.getStyleClass().add("otsikko");

        // Customer first name input
        VBox etunimiInfo = new VBox(5);
        Label asiakasEtunimiLabel = new Label("Asiakkaan etunimi:");
        TextField asiakasEtunimiInput = new TextField();
        asiakasEtunimiInput.setPromptText("Syötä etunimi");
        etunimiInfo.getChildren().addAll(asiakasEtunimiLabel, asiakasEtunimiInput);

        // Customer last name input
        VBox sukunimiInfo = new VBox(5);
        Label asiakasSukunimiLabel = new Label("Asiakkaan sukunimi:");
        TextField asiakasSukunimiInput = new TextField();
        asiakasSukunimiInput.setPromptText("Syötä sukunimi");
        sukunimiInfo.getChildren().addAll(asiakasSukunimiLabel, asiakasSukunimiInput);

        Button haeLaskutButton = new Button("Hae laskut");

        VBox maksattavaLaskut = new VBox(5);
        Label maksattavaLaskuOtsikko = new Label("Laskut");
        maksattavaLaskuOtsikko.getStyleClass().add("otsikko");
        TableView<LaskuData> laskuTable = createLaskuTable();
        Label loppuHintaLabel = new Label("Yhdessä: 0.00");
        String loppuHinta = "";
        Button maksuButton = new Button("Maksaa");
        Button printButton = new Button("Tulosta kuitti");

        maksattavaLaskut.getChildren().addAll(maksattavaLaskuOtsikko, laskuTable, loppuHintaLabel, maksuButton, printButton);

        haeLaskutButton.setOnAction(e -> {
            laskuTable.getItems().clear();

            List<Asiakas> asiakkaat = asiakasController.findByNimet(asiakasEtunimiInput.getText(), asiakasSukunimiInput.getText());
            if (asiakkaat == null || asiakkaat.isEmpty()) {
                showAlert("Virhe", "Asiakkaan nimellä ei löytynyt asiakkaita.");
                return;
            }

            double kokonaishinta = 0.00;
            String valuutta = ""; // Alustetaan valuutta

            for (Asiakas asiakas : asiakkaat) {
                int asiakasId = asiakas.getAsiakasId();

                System.out.println("Asiakas id: " + asiakasId);
                List<Lasku> laskut = laskuController.findLaskuByAsiakasId(asiakasId);

                if (laskut != null) {
                    for (Lasku lasku : laskut) {
                        int laskuId = lasku.getLaskuId();
                        System.out.println("Lasku id: " + laskuId);

                        List<Varaus> varaukset = varausController.findByLaskuId(lasku.getLaskuId());
                        valuutta = lasku.getValuutta(); // Asetetaan valuutta

                        for (Varaus varaus : varaukset) {
                            LocalDate alkuPvm = varaus.getAlkuPvm();
                            LocalDate loppuPvm = varaus.getLoppuPvm();
                            int paivat = Period.between(alkuPvm, loppuPvm).getDays();

                            if (loppuPvm.isAfter(LocalDate.now()) || loppuPvm.isEqual(LocalDate.now())) {
                                //Check if room number matches the current reservation
                                Huone huone = huoneController.findHuoneById(varaus.getHuoneId());
                                if (huone != null) {

                                    double hinta = huone.getHuone_hinta();
                                    if (valuutta.equals("USD")) {
                                        hinta= CurrencyConverter.convertCurrency("EUR", "USD", hinta);

                                    }

                                    double summa = hinta * paivat;
                                    String hintaStr = String.format("%.2f %s", hinta, valuutta);
                                    String summaStr = String.format("%.2f %s", summa, valuutta);
                                    kokonaishinta += summa;
                                    String kokonaishintaStr = String.format("%.2f %s", kokonaishinta, valuutta);


                                    //String summaStr = String.format("EUR", "%.2f USD", summa);

                                    populateLaskuTable(laskuTable, new LaskuData(
                                            lasku.getLaskuId(),
                                            huone.getHotelli_id(),
                                            huone.getHuone_id(),
                                            huone.getHuone_nro(),
                                            asiakas.getEtunimi(),
                                            asiakas.getSukunimi(),
                                            huone.getHuone_tyyppi(),
                                            lasku.getMaksuStatus(),
                                            lasku.getVarausMuoto(),
                                            lasku.getValuutta(),
                                            alkuPvm,
                                            loppuPvm,
                                            paivat,
                                            hintaStr,  // Muotoile hinta kahdelle desimaalille ja lisää USD
                                            summaStr,
                                            kokonaishintaStr
                                            // Muotoile summa kahdelle desimaalille
                                    ));


                                } else {
                                    showAlert("Virhe", "Virheellinen huoneen numero.");
                                }
                            }
                        }
                    }
                }
            }
            // Päivitetään loppuhinta label, kun kaikki laskut on käsitelty
            loppuHintaLabel.setText(String.format("Yhdessä: %.2f %s", kokonaishinta, valuutta));
        });

        // Layout structure
        checkOutInfo.getChildren().addAll(checkOutInfoLabel, etunimiInfo, sukunimiInfo, haeLaskutButton, maksattavaLaskut);
        HBox laskuTiedot = new HBox(2);
        laskuTiedot.getChildren().addAll(checkOutInfo, maksattavaLaskut);

        maksuButton.setOnAction(e -> {
            if (laskuTable.getItems().isEmpty()) {
                showAlert("Virhe", "Ei laskuja maksettavaksi.");
                return;
            }
            for (LaskuData laskuData : laskuTable.getItems()) {
                laskuController.updateMaksuStatusById(laskuData.getLaskuId(), "Maksettu");
                laskuData.SetMaksuStatus("Maksettu");
                int huoneId = laskuData.getHuoneId();

                huoneController.updateHuoneTilaById(huoneId, "Vapaa" );


                //refresh table
                laskuTable.refresh();
            }

            // Maksaa laskut
            showMessage("Maksu","Laskut on maksettu onnistuneesti.");
        });

        printButton.setOnAction(e -> openKuittiWindow(laskuTable));
        printButton.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                openKuittiWindow(laskuTable);
            }
        });

        VBox checkOut = new VBox(2);
        checkOut.getChildren().addAll(laskuTiedot);
        return checkOut;
    }

    private void openKuittiWindow(TableView<LaskuData> laskuTable) {
        Stage kuittiStage = new Stage();
        kuittiStage.setTitle("Kuitti");

        VBox kuittiLayout = new VBox(10);
        kuittiLayout.setAlignment(Pos.TOP_CENTER); // Kuitin otsikko keskellä
        kuittiLayout.setPadding(new Insets(20)); // Lisää padding koko asetteluun

        Label kuittiTitle = new Label("Kuitti");
        kuittiTitle.getStyleClass().add("otsikko");

        // Asetetaan suurempi fonttikoko ja paksu fontti (Bold)
        kuittiTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Hotellin tiedot vasemmalle (VBox, jossa lisätään padding)
        VBox hotelliInfo = new VBox(5);
        hotelliInfo.setPadding(new Insets(10));
        Label hotelliNimiLabel = new Label("Hotelli nimi: ");
        Label hotelliosoiteLabel = new Label("Osoite: ");
        Label hotelliKaupunkiLabel = new Label("Kaupunki: ");
        Label hotelliMaaLabel = new Label("Maa: ");
        Label hotelliPuhLabel = new Label("Puhelin: ");

        String asiakasNimet = "";
        int hotelliId = 0;
        for (LaskuData laskuData : laskuTable.getItems()) {
            asiakasNimet = laskuData.getEtunimi() + " " + laskuData.getSukunimi();
            hotelliId =  laskuData.getHotelliId();
        }

        Hotelli hotelli = hotelliController.findHotelliById(hotelliId);

        hotelliNimiLabel.setText("Hotelli nimi: " + hotelli.getNimi());
        hotelliosoiteLabel.setText("Osoite: " + hotelli.getOsoite());
        hotelliKaupunkiLabel.setText("Kaupunki: " + hotelli.getKaupunki());
        hotelliMaaLabel.setText("Maa: " + hotelli.getMaa());
        hotelliPuhLabel.setText("Puhelin: " + hotelli.getPuh());

        hotelliInfo.getChildren().addAll(hotelliNimiLabel, hotelliosoiteLabel,
                hotelliKaupunkiLabel, hotelliMaaLabel, hotelliPuhLabel);

        VBox asiakasInfo = new VBox(5);
        asiakasInfo.setPadding(new Insets(10));
        Label asiakasNimetLabel = new Label("Asiakkaan nimi: ");
        Label maksuPvmLabel = new Label("Maksupäivämäärä: ");
        Label loppuHintaLabel = new Label("Yhdessä:  ");
        LocalDate maksuPvm = LocalDate.now();
        maksuPvmLabel.setText("Maksupäivämäärä: " + maksuPvm.toString());

        asiakasNimetLabel.setText("Asiakkaan nimi: " + asiakasNimet);
        asiakasInfo.getChildren().addAll(asiakasNimetLabel, maksuPvmLabel);

        TableView<LaskuData> kuittiTable = createLaskuTable();
        kuittiTable.getItems().addAll(laskuTable.getItems());


        double loppusumma = 0.00;
        for (LaskuData laskuData : laskuTable.getItems()) {
            try {
                String kokonaisHinta = laskuData.getKokonaisHinta().split(" ")[0];
                Number number = NumberFormat.getInstance().parse(kokonaisHinta);
                loppusumma += number.doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        loppuHintaLabel.setText(String.format("Yhdessä: %.2f %s", loppusumma, laskuTable.getItems().get(0).getValuutta()));

        Button tulostaButton = new Button("Tulosta");
        tulostaButton.setOnAction(e -> {
            tulostaKuitti(kuittiTable);
            kuittiStage.close();
        });

        kuittiLayout.getChildren().addAll(kuittiTitle, hotelliInfo, asiakasInfo, kuittiTable, loppuHintaLabel, tulostaButton);

        Scene scene = new Scene(kuittiLayout, 830, 500);
        kuittiStage.setScene(scene);
        kuittiStage.show();
    }

    private void tulostaKuitti(TableView<LaskuData> laskuTable) {
        System.out.println("Kuitti");
        System.out.println("Lasku ID | Status | Muoto | Valuutta | Alku Pvm | Loppu Pvm | Päivät | Hinta | Summa");
        for (LaskuData laskuData : laskuTable.getItems()) {
            System.out.println(laskuData.getLaskuId() + " | " +
                    laskuData.getMaksuStatus() + " | " +
                    laskuData.getVarausMuoto() + " | " +
                    laskuData.getValuutta() + " | " +
                    laskuData.getAlkuPvm() + " | " +
                    laskuData.getLoppuPvm() + " | " +
                    laskuData.getPaivat() + " | " +
                    laskuData.getHinta() + " | " +
                    laskuData.getSumma());
        }
        showMessage("Kuitti", "Kuitti on tulostettu.");
    }

    // TableView method to display Lasku data
    private TableView<LaskuData> createLaskuTable() {
        TableView<LaskuData> laskuTable = new TableView<>();
        laskuTable.setPrefWidth(800);
        laskuTable.setPrefHeight(300);

        TableColumn<LaskuData, Integer> laskuIdColumn = new TableColumn<>("Lasku ID");
        laskuIdColumn.setCellValueFactory(new PropertyValueFactory<>("laskuId"));

        TableColumn<LaskuData, Integer> huoneNroColumn = new TableColumn<>("Huone Nro");
        huoneNroColumn.setCellValueFactory(new PropertyValueFactory<>("huoneNro"));

        TableColumn<LaskuData, Integer> huoneTyyppiColumn = new TableColumn<>("Huone tyyppi");
        huoneTyyppiColumn.setCellValueFactory(new PropertyValueFactory<>("huoneTyyppi"));

        TableColumn<LaskuData, String> maksuStatusColumn = new TableColumn<>("Status ");
        maksuStatusColumn.setCellValueFactory(new PropertyValueFactory<>("maksuStatus"));

        TableColumn<LaskuData, String> varausMuotoColumn = new TableColumn<>("Muoto");
        varausMuotoColumn.setCellValueFactory(new PropertyValueFactory<>("varausMuoto"));

        TableColumn<LaskuData, String> valuuttaColumn = new TableColumn<>("Valuutta");
        valuuttaColumn.setCellValueFactory(new PropertyValueFactory<>("valuutta"));

        TableColumn<LaskuData, String> alkuPvmColumn = new TableColumn<>("Alku Pvm");
        alkuPvmColumn.setCellValueFactory(new PropertyValueFactory<>("alkuPvm"));

        TableColumn<LaskuData, String> loppuPvmColumn = new TableColumn<>("Loppu Pvm");
        loppuPvmColumn.setCellValueFactory(new PropertyValueFactory<>("loppuPvm"));

        TableColumn<LaskuData, Integer> paivatColumn = new TableColumn<>("Päivät");
        paivatColumn.setCellValueFactory(new PropertyValueFactory<>("paivat"));

        TableColumn<LaskuData, Double> hintaColumn = new TableColumn<>("Hinta");
        hintaColumn.setCellValueFactory(new PropertyValueFactory<>("hinta"));

        TableColumn<LaskuData, Double> summaColumn = new TableColumn<>("Summa");
        summaColumn.setCellValueFactory(new PropertyValueFactory<>("summa"));

        laskuTable.getColumns().addAll(
                laskuIdColumn, huoneNroColumn, huoneTyyppiColumn, maksuStatusColumn, varausMuotoColumn, alkuPvmColumn, loppuPvmColumn,
                paivatColumn, hintaColumn, summaColumn);
        return laskuTable;
    }

    // Populates the table with LaskuData
    private void populateLaskuTable(TableView<LaskuData> laskuTable, LaskuData laskuData) {
        ObservableList<LaskuData> data = laskuTable.getItems();
        data.add(laskuData);
        laskuTable.setItems(data);
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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

    private TableView<Huone> createHuoneTable() {
        TableView<Huone> huoneTableView = new TableView<>();
        // Set the width of the table
        huoneTableView.setPrefWidth(950);
        huoneTableView.setPrefHeight(400);

        TableColumn<Huone, Integer> numeroColumn = new TableColumn<>("Huoneen Numero");
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("huone_nro"));

        TableColumn<Huone, String> tyyppiColumn = new TableColumn<>("Huone Tyyppi");
        tyyppiColumn.setCellValueFactory(new PropertyValueFactory<>("huone_tyyppi"));

        TableColumn<Huone, Double> hintaColumn = new TableColumn<>("Huone Hinta");
        hintaColumn.setCellValueFactory(new PropertyValueFactory<>("huone_hinta"));
        hintaColumn.setMinWidth(150);

        TableColumn<Huone, String> phoneColumn = new TableColumn<>("Huone Status");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("huone_tila"));
        phoneColumn.setMinWidth(100);

        TableColumn<Huone, Void> actionColumn = new TableColumn<>("Toiminnot");

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Muokkaa");
            private final Button deleteButton = new Button("Poista");
            private final HBox actionButtons = new HBox(editButton, deleteButton);

            {
                actionButtons.setSpacing(10);
                actionButtons.setAlignment(Pos.CENTER);

                // Muokkaa-painikeen toiminnallisuus
                editButton.setOnAction(event -> {
                    Huone huone = getTableView().getItems().get(getIndex());
                    // openMuokkaaHuoneWindow(huone, getTableView());
                    System.out.println("Muokkaa-painiketta painettu" + huone.getHuone_id());
                });


                // Poista-painikeen toiminnallisuus
                deleteButton.setOnAction(event -> {
                    Huone huone = getTableView().getItems().get(getIndex());
                    huoneController.deleteHuone(huone.getHuone_id());
                    getTableView().getItems().remove(huone); // Poistetaan asiakas listasta
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionButtons);
                }
            }
        });

        actionColumn.setMinWidth(150);

        huoneTableView.getColumns().addAll(numeroColumn, tyyppiColumn, hintaColumn, phoneColumn, actionColumn);

        return huoneTableView;
    }




    // Method to create the Customer table view
    private TableView<Asiakas> createCustomerTable() {
        TableView<Asiakas> customerTable = new TableView<>();
        // Set the width of the table
        customerTable.setPrefWidth(950);
        customerTable.setPrefHeight(400);

        TableColumn<Asiakas, Integer> idColumn = new TableColumn<>("Asiakas ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("asiakasId"));


        TableColumn<Asiakas, String> firstNameColumn = new TableColumn<>("Etunimi");
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("etunimi"));

        TableColumn<Asiakas, String> lastNameColumn = new TableColumn<>("Sukunimi");
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("sukunimi"));

        TableColumn<Asiakas, String> emailColumn = new TableColumn<>("Sähköposti");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("sposti"));
        emailColumn.setMinWidth(150);

        TableColumn<Asiakas, String> phoneColumn = new TableColumn<>("Puhelin");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("puh"));
        phoneColumn.setMinWidth(100);

        TableColumn<Asiakas, Integer> henkiloMaara = new TableColumn<>("Henkilömäärä");
        henkiloMaara.setCellValueFactory(new PropertyValueFactory<>("henkiloMaara"));

        TableColumn<Asiakas, String> huomio = new TableColumn<>("Huomio");
        huomio.setCellValueFactory(new PropertyValueFactory<>("huomio"));
        huomio.setMinWidth(230);

        // Create the "Actions" column for edit/delete
        TableColumn<Asiakas, Void> actionColumn = new TableColumn<>("Toiminnot");

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Muokkaa");
            private final Button deleteButton = new Button("Poista");
            private final HBox actionButtons = new HBox(editButton, deleteButton);

            {
                actionButtons.setSpacing(10);
                actionButtons.setAlignment(Pos.CENTER);

                // Muokkaa-painikeen toiminnallisuus
                editButton.setOnAction(event -> {
                    Asiakas asiakas = getTableView().getItems().get(getIndex());
                    openMuokkaaAsiakasWindow(asiakas, getTableView()); // Välitetään taulukko muokkausikkunaan
                });


                // Poista-painikeen toiminnallisuus
                deleteButton.setOnAction(event -> {
                    Asiakas asiakas = getTableView().getItems().get(getIndex());
                    poistaAsiakas(asiakas);
                    getTableView().getItems().remove(asiakas); // Poistetaan asiakas listasta
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(actionButtons);
                }
            }
        });

        actionColumn.setMinWidth(150);

        customerTable.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, emailColumn, phoneColumn, henkiloMaara, huomio, actionColumn);

        return customerTable;
    }

    private void openMuokkaaAsiakasWindow(Asiakas asiakas, TableView<Asiakas> customerTable) {
        Stage muokkaaAsiakasStage = new Stage();
        muokkaaAsiakasStage.setTitle("Muokkaa asiakasta");

        // Pääasettelu, joka jakaa ikkunan osiin (yläosa, keskiosa, alaosa)
        BorderPane borderPane = new BorderPane();

        // Lomakkeen kenttien asettelu pystysuoraan (VBox)
        VBox formLayout = new VBox(10);
        formLayout.setAlignment(Pos.CENTER_LEFT); // Keskitetään vasemmalle
        formLayout.setPadding(new Insets(20)); // Asetetaan täyte

        // Kenttien ja tekstikenttien luonti
        Label firstNameLabel = new Label("Etunimi:");
        TextField firstNameField = new TextField();
        firstNameField.setText(asiakas.getEtunimi());

        Label lastNameLabel = new Label("Sukunimi:");
        TextField lastNameField = new TextField();
        lastNameField.setText(asiakas.getSukunimi());

        Label emailLabel = new Label("Sähköposti:");
        TextField emailField = new TextField();
        emailField.setText(asiakas.getSposti());

        Label phoneLabel = new Label("Puhelin:");
        TextField phoneField = new TextField();
        phoneField.setText(asiakas.getPuh());

        Label henkiloMaaraLabel = new Label("Henkilömäärä:");
        TextField henkiloMaaraField = new TextField();
        henkiloMaaraField.setText(String.valueOf(asiakas.getHenkiloMaara()));

        Label huomioLabel = new Label("Huomio:");
        TextField huomioField = new TextField();
        huomioField.setText(asiakas.getHuomio());

        // Lisätään kentät lomakkeeseen (VBox)
        formLayout.getChildren().addAll(
                firstNameLabel, firstNameField,
                lastNameLabel, lastNameField,
                emailLabel, emailField,
                phoneLabel, phoneField,
                henkiloMaaraLabel, henkiloMaaraField,
                huomioLabel, huomioField
        );

        Button saveButton = new Button("Tallenna muutokset");
        Button cancelButton = new Button("Peruuta");

        // HBox save and cancel buttons
        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Toiminnallisuus napille
        saveButton.setOnAction(e -> {
            // Päivitetään asiakastiedot
            asiakasController.paivitaAsiakas(
                    asiakas.getAsiakasId(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    Integer.parseInt(henkiloMaaraField.getText()), // Muunnetaan teksti kokonaisluvuksi
                    huomioField.getText()
            );

            // Päivitetään taulukko
            populateCustomerTable(customerTable);
            muokkaaAsiakasStage.close();
        });

        cancelButton.setOnAction(e -> muokkaaAsiakasStage.close());

        // Asetetaan lomake ja painikkeet BorderPaneen
        borderPane.setCenter(formLayout);
        borderPane.setBottom(buttonBox);
        BorderPane.setMargin(buttonBox, new Insets(10)); // Marginaali nappeihin

        // Aseta BorderPane kohtaukseksi ja näytä ikkuna
        Scene scene = new Scene(borderPane, 400, 500);
        muokkaaAsiakasStage.setScene(scene);
        muokkaaAsiakasStage.show(); // Tämä avaa muokkausikkunan
    }

    private void poistaAsiakas(Asiakas asiakas) {
        asiakasController.poistaAsiakas(asiakas.getAsiakasId());

    }

    private TableView<Varaus> createVarausTable(){
        TableView<Varaus> varausTable = new TableView<>();

        TableColumn<Varaus, Integer> idColumn = new TableColumn<>("Varaus ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("varausId"));

        TableColumn<Varaus, Integer> roomColumn = new TableColumn<>("Huone nro");
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("huoneNro"));

        TableColumn<Varaus, Integer> invoiceColumn = new TableColumn<>("Asiakas Nimi");
        invoiceColumn.setCellValueFactory(new PropertyValueFactory<>("asiakasNimi"));

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