package view.sivut;

import controller.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.DAO.AsiakasDAO;
import model.enteties.Asiakas;
import model.enteties.Huone;
import model.enteties.Lasku;
import model.enteties.Varaus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VarausSivu {

    private VarausController varausController;
    private HotelliController hotelliController;
    private AsiakasController asiakasController;

    private LaskuController laskuController;
    private HuoneController huoneController;

    public VarausSivu() {
        varausController = new VarausController();
        hotelliController = new HotelliController();
        asiakasController = new AsiakasController();
        laskuController = new LaskuController();
        huoneController = new HuoneController();
    }

    public VBox createVaraukset() {
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
        etsiAsiakasButton.getStyleClass().add("yellow-btn");

        asiakasTiedot.getChildren().addAll(
                asiakasEtunimiLabel, asiakasEtunimiField,
                asiakasSukunimiLabel, asiakasSukunimiField,
                asiakasSpostiLabel, asiakasSpostiField,
                asiakasPuhLabel, asiakasPuhField,
                asiakasLisatiedotLabel, asiakasLisatiedotField,
                etsiAsiakasButton);
        asiakasTiedot.setMargin(etsiAsiakasButton, new Insets(10));

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
        luoVarausButton.getStyleClass().add("yellow-btn");

        luoVaraus.getChildren().addAll(
                luoVarausLabel, asiakasTiedot, laskuTiedot, varausTiedot, luoVarausButton);
        luoVaraus.setMargin(luoVarausButton, new Insets(10));


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

                if (saapumisPvm == null || lahtoPvm == null) {
                    System.err.println("Please enter a valid check-in and check-out date.");
                    return;
                }

                System.out.println("Checking room availability...");

                // Get total number of rooms in the hotel
                int totalRooms = hotelliController.getRoomCount();

                // Get count of overlapping reservations for the given date range
                int overlappingReservations = varausController.getOverlappingReservationsCount(saapumisPvm, lahtoPvm);

                if (overlappingReservations >= totalRooms) {
                    System.err.println("No rooms available for the given date range.");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Virhe");
                    alert.setHeaderText(null);
                    alert.setContentText("Ei huoneita saatavilla valitulle päivämäärälle.");
                    alert.showAndWait();
                    return;
                }

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
                return asiakasController.findByKeyword(searchQuery);
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

    // Check In was moved to separate class


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

            Platform.runLater(() -> {
                loadingIndicator.setVisible(false);
                varausTable.getSelectionModel().clearSelection(); // Clear selection before updating items

                if (varaukset != null && !varaukset.isEmpty()) {
                    for (Varaus varaus : varaukset) {
                        if (varaus.getHuoneId() != null) {
                            Huone huone = huoneController.findHuoneById(varaus.getHuoneId());
                            varaus.setHuone(huone);
                        }

                        Lasku lasku = laskuController.findLaskuById(varaus.getLaskuId());
                        if (lasku != null) {
                            Asiakas asiakas = asiakasController.findByLaskuId(lasku.getAsiakasId());
                            varaus.setNimi(asiakas.getEtunimi() + " " + asiakas.getSukunimi());
                        }


                    }
                    varausTable.getItems().setAll(varaukset); // Update table with new data
                } else {
                    varausTable.setPlaceholder(new Label("No reservations found for the given dates"));
                }
            });
        });

        fetchVarauksetTask.setOnFailed(event -> {
            varausTable.setPlaceholder(new Label("Failed to load reservation data"));
            System.err.println("Failed to fetch reservations: " + fetchVarauksetTask.getException());
            loadingIndicator.setVisible(false);
        });

        new Thread(fetchVarauksetTask).start();
    }

    private TableView<Varaus> createVarausTable(){
        TableView<Varaus> varausTable = new TableView<>();

        TableColumn<Varaus, Integer> idColumn = new TableColumn<>("Varaus ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("varausId"));
        idColumn.setMinWidth(135);

        TableColumn<Varaus, String> asiakasColumn = new TableColumn<>("Nimi");
        asiakasColumn.setCellValueFactory(new PropertyValueFactory<>("nimi"));
        asiakasColumn.setMinWidth(150);

        TableColumn<Varaus, Integer> roomColumn = new TableColumn<>("Huone Nro");
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("huoneNro"));
        roomColumn.setMinWidth(150);

        TableColumn<Varaus, String> startDateColumn = new TableColumn<>("Alkupäivämäärä");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("alkuPvm"));
        startDateColumn.setMinWidth(150);

        TableColumn<Varaus, String> endDateColumn = new TableColumn<>("Loppupäivämäärä");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("loppuPvm"));
        endDateColumn.setMinWidth(150);

        varausTable.getColumns().add(idColumn);
        varausTable.getColumns().add(roomColumn);
        varausTable.getColumns().add(asiakasColumn);
        varausTable.getColumns().add(startDateColumn);
        varausTable.getColumns().add(endDateColumn);

        return varausTable;
    }

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

        customerTable.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, emailColumn, phoneColumn);

        return customerTable;
    }
}
