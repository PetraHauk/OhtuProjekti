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
import model.service.LocaleManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class VarausSivu {

    private VarausController varausController;
    private HotelliController hotelliController;
    private AsiakasController asiakasController;

    private LaskuController laskuController;
    private HuoneController huoneController;

    private ResourceBundle bundle;

    public VarausSivu() {
        varausController = new VarausController();
        hotelliController = new HotelliController();
        asiakasController = new AsiakasController();
        laskuController = new LaskuController();
        huoneController = new HuoneController();

        Locale currentLocale = LocaleManager.getCurrentLocale();
        bundle = ResourceBundle.getBundle("messages", currentLocale);
    }

    public VBox createVaraukset() {
        VBox varauksetInfo = new VBox(10);
        varauksetInfo.getStyleClass().add("info");
        Label varauksetOtsikkoLabel = new Label(bundle.getString("varaus.title"));
        varauksetOtsikkoLabel.getStyleClass().add("otsikko");

        HBox varausBox = new HBox(10);

        VBox luoVaraus = new VBox(0);
        Label luoVarausLabel = new Label(bundle.getString("varaus.createtitle"));

        VBox asiakasTiedot = new VBox(0);

        Label asiakasEtunimiLabel = new Label(bundle.getString("varaus.label.firstname"));
        TextField asiakasEtunimiField = new TextField();

        Label asiakasSukunimiLabel = new Label(bundle.getString("varaus.label.lastname"));
        TextField asiakasSukunimiField = new TextField();

        Label asiakasSpostiLabel = new Label(bundle.getString("varaus.label.email"));
        TextField asiakasSpostiField = new TextField();

        Label asiakasPuhLabel = new Label(bundle.getString("varaus.label.phone"));
        TextField asiakasPuhField = new TextField();

        Label asiakasLisatiedotLabel = new Label(bundle.getString("varaus.label.notes"));
        TextField asiakasLisatiedotField = new TextField();

        Button etsiAsiakasButton = new Button(bundle.getString("varaus.button.searchcustomer"));
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

        Label laskuMuotoLabel = new Label(bundle.getString("varaus.label.reservationtype"));
        ComboBox<String> laskuMuotoField = new ComboBox<>();
        laskuMuotoField.setPromptText(bundle.getString("varaus.selector.placeholder"));
        laskuMuotoField.getItems().addAll(
                bundle.getString("varaus.selector.allinclusive"),
                bundle.getString("varaus.selector.breakfast"),
                bundle.getString("varaus.selector.roomonly"));

        laskuTiedot.getChildren().addAll(laskuMuotoLabel, laskuMuotoField);

        VBox varausTiedot = new VBox(0);

        Label saapumisPvmLabel = new Label(bundle.getString("varaus.label.arrival"));
        DatePicker saapumisPvmField = new DatePicker();

        Label lahtoPvmLabel = new Label(bundle.getString("varaus.label.departure"));
        DatePicker lahtoPvmField = new DatePicker();

        varausTiedot.getChildren().addAll(saapumisPvmLabel, saapumisPvmField, lahtoPvmLabel, lahtoPvmField);

        Button luoVarausButton = new Button(bundle.getString("varaus.button.create"));
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
                    System.err.println(bundle.getString("varaus.error.invaliddates"));
                    return;
                }

                // Get total number of rooms in the hotel
                int totalRooms = hotelliController.getRoomCount();

                // Get count of overlapping reservations for the given date range
                int overlappingReservations = varausController.getOverlappingReservationsCount(saapumisPvm, lahtoPvm);

                if (overlappingReservations >= totalRooms) {
                    System.err.println(bundle.getString("varaus.error.noroomsfordates"));
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(bundle.getString("varaus.error.title"));
                    alert.setHeaderText(null);
                    alert.setContentText(bundle.getString(bundle.getString("varaus.error.noroomsfordates")));
                    alert.showAndWait();
                    return;
                }

                System.out.println(bundle.getString("varaus.creating"));
                varausController.createVaraus(asiakasEtunimi, asiakasSukunimi, asiakasEmail, asiakasPuh, huomio, laskuMuoto, saapumisPvm, lahtoPvm);
                System.out.println(bundle.getString("varaus.created"));
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
        searchCustomerStage.setTitle(bundle.getString("varaus.searchcustomer"));

        VBox searchLayout = new VBox(10);
        searchLayout.setAlignment(Pos.CENTER);

        Label searchLabel = new Label(bundle.getString("varaus.searchcustomer"));
        TextField searchField = new TextField();
        Button searchButton = new Button(bundle.getString("varaus.button.search"));
        searchButton.getStyleClass().add("yellow-btn");

        TableView<Asiakas> searchResults = createCustomerTable();

        HBox pageButtons = new HBox(10);
        Button previousButton = new Button(bundle.getString("varaus.button.previous"));
        Button nextButton = new Button(bundle.getString("varaus.button.next"));
        pageButtons.getChildren().addAll(previousButton, nextButton);

        Button selectButton = new Button(bundle.getString("varaus.button.select"));
        selectButton.getStyleClass().add("yellow-btn");

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

        Scene scene = new Scene(searchLayout, 730, 460);
        scene.getStylesheets().add("style.css");
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
                searchResults.setPlaceholder(new Label(bundle.getString("varaus.error.nocustomersfound")));
            }
            loadingIndicator.setVisible(false);
        });

        searchCustomersTask.setOnFailed(event -> {
            searchResults.setPlaceholder(new Label(bundle.getString("varaus.error.loadcustomers")));
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
                    varausTable.setPlaceholder(new Label(bundle.getString("varaus.error.noreservations")));
                }
            });
        });

        fetchVarauksetTask.setOnFailed(event -> {
            varausTable.setPlaceholder(new Label(bundle.getString("varaus.error.loadreservations")));
            System.err.println("Failed to fetch reservations: " + fetchVarauksetTask.getException());
            loadingIndicator.setVisible(false);
        });

        new Thread(fetchVarauksetTask).start();
    }

    private TableView<Varaus> createVarausTable(){
        TableView<Varaus> varausTable = new TableView<>();

        TableColumn<Varaus, Integer> idColumn = new TableColumn<>(bundle.getString("varaus.table.reservationid"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("varausId"));
        idColumn.setMinWidth(135);

        TableColumn<Varaus, String> asiakasColumn = new TableColumn<>(bundle.getString("varaus.table.customer"));
        asiakasColumn.setCellValueFactory(new PropertyValueFactory<>("nimi"));
        asiakasColumn.setMinWidth(150);

        TableColumn<Varaus, Integer> roomColumn = new TableColumn<>(bundle.getString("varaus.table.roomnumber"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("huoneNro"));
        roomColumn.setMinWidth(150);

        TableColumn<Varaus, String> startDateColumn = new TableColumn<>(bundle.getString("varaus.table.arrival"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("alkuPvm"));
        startDateColumn.setMinWidth(150);

        TableColumn<Varaus, String> endDateColumn = new TableColumn<>(bundle.getString("varaus.table.departure"));
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

        TableColumn<Asiakas, Integer> idColumn = new TableColumn<>(bundle.getString("varaus.table.customerid"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("asiakasId"));
        idColumn.setMinWidth(80);

        TableColumn<Asiakas, String> firstNameColumn = new TableColumn<>(bundle.getString("varaus.table.firstname"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("etunimi"));
        firstNameColumn.setMinWidth(120);

        TableColumn<Asiakas, String> lastNameColumn = new TableColumn<>(bundle.getString("varaus.table.lastname"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("sukunimi"));
        lastNameColumn.setMinWidth(120);

        TableColumn<Asiakas, String> emailColumn = new TableColumn<>(bundle.getString("varaus.table.email"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("sposti"));
        emailColumn.setMinWidth(150);

        TableColumn<Asiakas, String> phoneColumn = new TableColumn<>(bundle.getString("varaus.table.phone"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("puh"));
        phoneColumn.setMinWidth(90);

        TableColumn<Asiakas, String> huomioColumn = new TableColumn<>(bundle.getString("varaus.table.notes"));
        huomioColumn.setCellValueFactory(new PropertyValueFactory<>("huomio"));
        huomioColumn.setMinWidth(165);

        customerTable.getColumns().addAll(idColumn, firstNameColumn, lastNameColumn, emailColumn, phoneColumn, huomioColumn);

        return customerTable;
    }
}
