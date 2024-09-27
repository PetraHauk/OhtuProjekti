package app;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import controller.*;
import model.enteties.*;

public class OhjelmistoGUI extends Application {

    private HuoneController huoneController;
    private AsiakasController asiakasController;
    private VarausController varausController;
    private LaskuController laskuController;
    private HotelliController hotelliController;

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
        VBox checkOutInfo = new VBox(15);
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

            // Fetch customers by names
            List<Asiakas> asiakkaat = asiakasController.findIdByNimet(asiakasEtunimiInput.getText(), asiakasSukunimiInput.getText());
            if (asiakkaat == null || asiakkaat.isEmpty()) {
                showAlert("Virhe", "Asiakkaan nimellä ei löytynyt asiakkaita.");
                return;
            }

            double kokonaishinta = 0.00;
            String valuutta = ""; // Alustetaan valuutta

            for (Asiakas asiakas : asiakkaat) {
                List<Lasku> laskut = laskuController.findLaskuByAsiakasId(asiakas.getAsiakasId());
                if (laskut != null) {
                    for (Lasku lasku : laskut) {
                        List<Varaus> varaukset = varausController.findByLaskuId(lasku.getLaskuId());
                        valuutta = lasku.getValuutta(); // Asetetaan valuutta

                        for (Varaus varaus : varaukset) {
                            LocalDate alkuPvm = varaus.getAlkuPvm();
                            LocalDate loppuPvm = varaus.getLoppuPvm();
                            int paivat = Period.between(alkuPvm, loppuPvm).getDays();

                            //if (loppuPvm.isAfter(LocalDate.now()) || loppuPvm.isEqual(LocalDate.now())) {
                                // Check if room number matches the current reservation
                                Huone huone = huoneController.findHuoneById(varaus.getHuoneId());
                                if (huone != null) {
                                    double hinta = huone.getHuone_hinta();
                                    double summa = hinta * paivat;
                                    kokonaishinta += summa;

                                    populateLaskuTable(laskuTable, new LaskuData(
                                            lasku.getLaskuId(),
                                            huone.getHotelli_id(),
                                            huone.getHuone_id(),
                                            asiakas.getEtunimi(),
                                            asiakas.getSukunimi(),
                                            lasku.getMaksuStatus(),
                                            lasku.getVarausMuoto(),
                                            lasku.getValuutta(),
                                            alkuPvm,
                                            loppuPvm,
                                            paivat,
                                            hinta,
                                            summa
                                    ));

                                } else {
                                    showAlert("Virhe", "Virheellinen huoneen numero.");
                                }
                            //}
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
            }

            // Maksaa laskut
            showAlert("Lasku maksettu","Laskut on maksettu onnistuneesti. Tervetuloa uudelleen!");
        });

        printButton.setOnAction(e -> openKuittiWindow(laskuTable));

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
        Label asiakasNimetLabel = new Label("Asiakkaan nimi: ");
        Label maksuPvmLabel = new Label("Maksupäivämäärä: ");
        Label loppuHintaLabel = new Label("Yhdessä:  ");
        LocalDate maksuPvm = LocalDate.now();
        maksuPvmLabel.setText("Maksupäivämäärä: " + maksuPvm.toString());

        String asiakasNimet = "";
        int hotelliId = 0;
        for (LaskuData laskuData : laskuTable.getItems()) {
            asiakasNimet = laskuData.getEtunimi() + " " + laskuData.getSukunimi();
            hotelliId =  laskuData.getHotelliId();
        }

        Hotelli hotelli = hotelliController.findHotelliById(hotelliId);
        asiakasNimetLabel.setText("Asiakkaan nimi: " + asiakasNimet);
        hotelliNimiLabel.setText("Hotelli nimi: " + hotelli.getNimi());
        hotelliosoiteLabel.setText("Osoite: " + hotelli.getOsoite());
        hotelliKaupunkiLabel.setText("Kaupunki: " + hotelli.getKaupunki());
        hotelliMaaLabel.setText("Maa: " + hotelli.getMaa());
        hotelliPuhLabel.setText("Puhelin: " + hotelli.getPuh());

        hotelliInfo.getChildren().addAll(hotelliNimiLabel, hotelliosoiteLabel,
                hotelliKaupunkiLabel, hotelliMaaLabel, hotelliPuhLabel, asiakasNimetLabel, maksuPvmLabel);


        TableView<LaskuData> kuittiTable = createLaskuTable();
        kuittiTable.getItems().addAll(laskuTable.getItems());



        double loppusumma = 0;
        for (LaskuData laskuData : laskuTable.getItems()) {
            loppusumma += laskuData.getSumma();
            loppuHintaLabel.setText("Yhdessä:   " + loppusumma + " "  + laskuData.getValuutta());
        }

        Button tulostaButton = new Button("Tulosta");
        tulostaButton.setOnAction(e -> {
            tulostaKuitti(kuittiTable);
            kuittiStage.close();
        });

        kuittiLayout.getChildren().addAll(kuittiTitle, hotelliInfo, kuittiTable, loppuHintaLabel, tulostaButton);

        Scene scene = new Scene(kuittiLayout, 700, 500);
        kuittiStage.setScene(scene);
        kuittiStage.show();
    }

    private void tulostaKuitti(TableView<LaskuData> laskuTable) {
        System.out.println("Kuitti");
        System.out.println("Lasku ID | Status | Muoto | Valuutta | Alku Pvm | Loppu Pvm | Päivät | Hinta | Summa");
        for (LaskuData laskuData : laskuTable.getItems()) {
            System.out.println(laskuData.getLaskuId() + " | " + laskuData.getMaksuStatus() + " | " + laskuData.getVarausMuoto() + " | " + laskuData.getValuutta() + " | " + laskuData.getAlkuPvm() + " | " + laskuData.getLoppuPvm() + " | " + laskuData.getPaivat() + " | " + laskuData.getHinta() + " | " + laskuData.getSumma());
        }
        showAlert("Kuitti", "Kuitti on tulostettu.");
    }

    // TableView method to display Lasku data
    private TableView<LaskuData> createLaskuTable() {
        TableView<LaskuData> laskuTable = new TableView<>();
        laskuTable.setPrefWidth(570);
        laskuTable.setPrefHeight(300);

        TableColumn<LaskuData, Integer> laskuIdColumn = new TableColumn<>("Lasku ID");
        laskuIdColumn.setCellValueFactory(new PropertyValueFactory<>("laskuId"));

        TableColumn<LaskuData, String> maksuStatusColumn = new TableColumn<>("Status");
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
                 laskuIdColumn, maksuStatusColumn, varausMuotoColumn, alkuPvmColumn, loppuPvmColumn,
                paivatColumn, hintaColumn, summaColumn);

        return laskuTable;
    }

    // Populates the table with LaskuData
    private void populateLaskuTable(TableView<LaskuData> laskuTable, LaskuData laskuData) {
        ObservableList<LaskuData> data = laskuTable.getItems();
        data.add(laskuData);
        laskuTable.setItems(data);
    }














    // Utility method to show alert dialog
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