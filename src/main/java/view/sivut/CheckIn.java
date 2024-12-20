package view.sivut;

import controller.HuoneController;
import controller.VarausController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.enteties.Huone;
import model.enteties.Varaus;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import model.service.LocaleManager;

public class CheckIn {

    private final HuoneController huoneController;
    private final VarausController varausController;
    String selectedLanguage = LocaleManager.getLanguageName();
    private ResourceBundle bundle;
    private String otsikkoCss = "otsikko";

    private Logger logger = Logger.getLogger(CheckIn.class.getName());

    public CheckIn() {
        varausController = new VarausController();
        huoneController = new HuoneController();

        Locale currentLocale = LocaleManager.getCurrentLocale();
        bundle = ResourceBundle.getBundle("messages", currentLocale);
    }

    public VBox createCheckIn() {
        VBox huoneVarausInfo = new VBox(5);
        huoneVarausInfo.getStyleClass().add("info");

        Label checkInInfoLabel = new Label(bundle.getString("checkin.title"));
        checkInInfoLabel.getStyleClass().add(otsikkoCss);

        VBox huoneTyyppi = new VBox(0);
        Label huoneLabel = new Label(bundle.getString("checkin.label.huonetyyppi"));
        ComboBox<String> huoneField = new ComboBox<>();
        huoneField.getItems().addAll(
                bundle.getString("checkin.selector.single"),
                bundle.getString("checkin.selector.double"),
                bundle.getString("checkin.selector.triple"),
                bundle.getString("checkin.selector.family"),
                bundle.getString("checkin.selector.suite")
        );
        huoneField.setPromptText(bundle.getString("checkin.selector.roomplacehoder"));
        huoneTyyppi.getChildren().addAll(huoneLabel, huoneField);

        VBox tuloPaiva = new VBox(0);
        Label tuloLabel = new Label(bundle.getString("checkin.label.arrival"));
        DatePicker tuloDatePicker = new DatePicker();
        tuloPaiva.getChildren().addAll(tuloLabel, tuloDatePicker);

        VBox poistumisPaiva = new VBox(0);
        Label poistumisLabel = new Label(bundle.getString("checkin.label.departure"));
        DatePicker poistumisDatePicker = new DatePicker();
        poistumisPaiva.getChildren().addAll(poistumisLabel, poistumisDatePicker);

        VBox paivat = new VBox(0);
        Label paivatLabel = new Label(bundle.getString("checkin.label.days"));
        Label paivatValue = new Label("0");
        paivat.getChildren().addAll(paivatLabel, paivatValue);

        VBox huoneHinta = new VBox(0);
        Label hintaLabel = new Label(bundle.getString("checkin.label.price"));
        Label hinta = new Label("0.00 €");
        huoneHinta.getChildren().addAll(hintaLabel, hinta);

        VBox availableRooms = new VBox(10);
        Label availableRoomsTitle = new Label(bundle.getString("checkin.label.freerooms"));
        availableRoomsTitle.getStyleClass().add(otsikkoCss);
        TableView<Huone> huoneTable = createHuoneTable();
        huoneTable.setPrefWidth(500);
        huoneTable.setPrefHeight(250);
        availableRooms.getChildren().addAll(availableRoomsTitle, huoneTable);

        huoneVarausInfo.getChildren().addAll(checkInInfoLabel, huoneTyyppi, tuloPaiva, poistumisPaiva, paivat, huoneHinta);
        HBox huoneTiedot = new HBox(10);
        huoneTiedot.getChildren().addAll(huoneVarausInfo, availableRooms);

        VBox varausInfo = new VBox(5);
        Label varausInfoLabel = new Label(bundle.getString("checkin.label.reservations"));
        varausInfoLabel.getStyleClass().add(otsikkoCss);

        TableView<Varaus> varausTable = createVarausTable();
        varausTable.setPrefWidth(500);
        varausTable.setPrefHeight(200);
        populateVarausTable(varausTable, tuloDatePicker, poistumisDatePicker);

        varausInfo.getChildren().addAll(varausInfoLabel, varausTable);

        Button checkInButton = new Button(bundle.getString("checkin.button.checkin"));
        checkInButton.getStyleClass().add("yellow-btn");

        huoneField.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                List<Huone> filteredRooms = huoneTable.getItems().stream()
                        .filter(huone -> huone.getHuoneTyyppiFi().equals(newValue))
                        .toList();
                huoneTable.getItems().setAll(filteredRooms);
            }
        });

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

        // Event listener for the room table. Calculate the price of the selected room
        huoneTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                double price = newValue.getHuoneHinta() * Integer.parseInt(paivatValue.getText());
                hinta.setText(String.format("%.2f €", price));
            }
        });

        // Event listeners for the date pickers. Populate the free room table and the reservation table
        tuloDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            populateFreeRoomTable(huoneTable, 1, tuloDatePicker, poistumisDatePicker);
            populateVarausTable(varausTable, tuloDatePicker, poistumisDatePicker);
        });
        poistumisDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            populateFreeRoomTable(huoneTable, 1, tuloDatePicker, poistumisDatePicker);
            populateVarausTable(varausTable, tuloDatePicker, poistumisDatePicker);
        });

        checkInButton.setOnAction(e -> checkInButtonAction(huoneTable, varausTable, tuloDatePicker));
        VBox checkIn = new VBox(20);
        checkIn.getChildren().addAll(huoneTiedot, varausInfo, checkInButton);
        return checkIn;
    }

    void populateFreeRoomTable(TableView<Huone> huoneTable, int hotelliId, DatePicker tuloDatePicker, DatePicker poistumisDatePicker) {
        ProgressIndicator loadingIndicator = initializeLoadingIndicator(huoneTable);

        if (tuloDatePicker.getValue() != null && poistumisDatePicker.getValue() != null && tuloDatePicker.getValue().isAfter(poistumisDatePicker.getValue())) {
            huoneTable.setPlaceholder(new Label(bundle.getString("checkin.error.dateorder")));
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
                huoneTable.setPlaceholder(new Label(bundle.getString("checkin.error.norooms")));
            }
            loadingIndicator.setVisible(false);
        });
        fetchRoomsTask.setOnFailed(event -> {
            huoneTable.setPlaceholder(new Label(bundle.getString("checkin.error.loadrooms")));
            logger.severe("Failed to fetch free rooms: " + fetchRoomsTask.getException());
            loadingIndicator.setVisible(false);
        });
        new Thread(fetchRoomsTask).start();
    }

    private void populateVarausTable(TableView<Varaus> varausTable, DatePicker tuloDatePicker, DatePicker lahtoDatePicker) {
        ProgressIndicator loadingIndicator = initializeLoadingIndicator(varausTable);

        LocalDate alkuPvm = tuloDatePicker.getValue();
        LocalDate loppuPvm = lahtoDatePicker.getValue();

        LocalDate startDate = (alkuPvm != null) ? alkuPvm : LocalDate.of(1970, 1, 1);
        LocalDate endDate = (loppuPvm != null) ? loppuPvm : LocalDate.of(2100, 1, 1);

        Task<List<Varaus>> fetchVarauksetTask = new Task<>() {
            @Override
            protected List<Varaus> call() throws Exception {
                // Delegate to controller to fetch and enrich the data
                return varausController.getVarauksetWithDates(startDate, endDate);
            }
        };

        fetchVarauksetTask.setOnSucceeded(event -> {
            List<Varaus> varaukset = fetchVarauksetTask.getValue();

            Platform.runLater(() -> {
                loadingIndicator.setVisible(false);
                varausTable.getSelectionModel().clearSelection();

                if (varaukset != null && !varaukset.isEmpty()) {
                    varausTable.getItems().setAll(varaukset); // Update table with enriched data
                } else {
                    varausTable.setPlaceholder(new Label(bundle.getString("checkin.error.noreservations")));
                }
            });
        });

        fetchVarauksetTask.setOnFailed(event ->
            Platform.runLater(() -> {
                varausTable.setPlaceholder(new Label(bundle.getString("checkin.error.loadreservations")));
                logger.severe("Failed to fetch reservations: " + fetchVarauksetTask.getException());
                loadingIndicator.setVisible(false);
            })
        );

        new Thread(fetchVarauksetTask).start();
    }

    private void checkInButtonAction(TableView<Huone> huoneTable, TableView<Varaus> varausTable, DatePicker today) {
        Huone selectedRoom = huoneTable.getSelectionModel().getSelectedItem();
        Varaus selectedVaraus = varausTable.getSelectionModel().getSelectedItem();

        if (selectedRoom != null && selectedVaraus != null) {

            if (selectedVaraus.getHuoneId() != null) {
                huoneController.updateHuoneStatusById(selectedVaraus.getHuoneId(), "Vapaa");
            }

            huoneController.updateHuoneStatusById(selectedRoom.getHuoneId(), "Varattu");
            varausController.updateVarausHuoneById(selectedVaraus.getVarausId(), selectedRoom.getHuoneId());

            DatePicker endDate = new DatePicker(selectedVaraus.getLoppuPvm());

            populateFreeRoomTable(huoneTable, 1, today, endDate);
            populateVarausTable(varausTable, today, endDate);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(bundle.getString("error.title"));
            alert.setHeaderText(null);
            alert.setContentText(bundle.getString("checkin.error.noselection"));
            alert.showAndWait();
        }

    }

    private TableView<Huone> createHuoneTable() {
        TableView<Huone> huoneTableView = new TableView<>();
        // Set the width of the table
        huoneTableView.setPrefWidth(950);
        huoneTableView.setPrefHeight(400);

        TableColumn<Huone, Integer> numeroColumn = new TableColumn<>(bundle.getString("checkin.table.roomnumber"));
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("huoneNro"));
        numeroColumn.setMinWidth(94);

        TableColumn<Huone, String> tyyppiColumn = new TableColumn<>(bundle.getString("checkin.table.roomtype"));
        String huoneTypeColumn = LocaleManager.getLocalColumnName(selectedLanguage, "huoneTyyppi");
        tyyppiColumn.setCellValueFactory(new PropertyValueFactory<>(huoneTypeColumn));
        tyyppiColumn.setMinWidth(150);

        TableColumn<Huone, Double> hintaColumn = new TableColumn<>(bundle.getString("checkin.table.price"));
        hintaColumn.setCellValueFactory(new PropertyValueFactory<>("huoneHinta"));
        hintaColumn.setMinWidth(150);

        TableColumn<Huone, String> statusColumn = new TableColumn<>(bundle.getString("checkin.table.status"));
        String huoneTilaColumn = LocaleManager.getLocalColumnName(selectedLanguage, "huoneTila");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>(huoneTilaColumn));
        statusColumn.setMinWidth(100);

        huoneTableView.getColumns().add(numeroColumn);
        huoneTableView.getColumns().add(tyyppiColumn);
        huoneTableView.getColumns().add(hintaColumn);
        huoneTableView.getColumns().add(statusColumn);

        return huoneTableView;
    }


    private TableView<Varaus> createVarausTable(){
        TableView<Varaus> varausTable = new TableView<>();
        varausTable.setPrefWidth(950);

        TableColumn<Varaus, Integer> idColumn = new TableColumn<>(bundle.getString("checkin.table.reservationid"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("varausId"));
        idColumn.setMinWidth(90);

        TableColumn<Varaus, String> asiakasColumn = new TableColumn<>(bundle.getString("checkin.table.customer"));
        asiakasColumn.setCellValueFactory(new PropertyValueFactory<>("nimi"));
        asiakasColumn.setMinWidth(230);

        TableColumn<Varaus, Integer> roomColumn = new TableColumn<>(bundle.getString("checkin.table.roomnumber"));
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("huoneNro"));
        roomColumn.setMinWidth(90);

        TableColumn<Varaus, String> startDateColumn = new TableColumn<>(bundle.getString("checkin.table.arrival"));
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("alkuPvm"));
        startDateColumn.setMinWidth(153);

        TableColumn<Varaus, String> endDateColumn = new TableColumn<>(bundle.getString("checkin.table.departure"));
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("loppuPvm"));
        endDateColumn.setMinWidth(153);

        varausTable.getColumns().add(idColumn);
        varausTable.getColumns().add(roomColumn);
        varausTable.getColumns().add(asiakasColumn);
        varausTable.getColumns().add(startDateColumn);
        varausTable.getColumns().add(endDateColumn);

        return varausTable;
    }

    private <T> ProgressIndicator initializeLoadingIndicator(TableView<T> table) {
        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(true);
        loadingIndicator.setPrefSize(50, 50);
        table.getItems().clear();
        table.setPlaceholder(loadingIndicator);
        return loadingIndicator;
    }

}
