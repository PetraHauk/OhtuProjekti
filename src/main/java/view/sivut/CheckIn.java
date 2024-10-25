package view.sivut;

import controller.AsiakasController;
import controller.HuoneController;
import controller.LaskuController;
import controller.VarausController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.DAO.AsiakasDAO;
import model.enteties.Asiakas;
import model.enteties.Huone;
import model.enteties.Lasku;
import model.enteties.Varaus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CheckIn {

    private final HuoneController huoneController;
    private final VarausController varausController;
    private final AsiakasController asiakasController;
    private final LaskuController laskuController;

    public CheckIn() {
        varausController = new VarausController();
        huoneController = new HuoneController();
        laskuController = new LaskuController();
        asiakasController = new AsiakasController();
    }

    public VBox createCheckIn() {
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
        checkInButton.getStyleClass().add("yellow-btn");

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
                double price = newValue.getHuone_hinta() * Integer.parseInt(paivatValue.getText());
                hinta.setText(String.format("%.2f €", price));
            }
        });

        // Event listeners for the date pickers. Populate the free room table and the reservation table
        tuloDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            populateFreeRoomTable(huoneTable, 1, tuloDatePicker, poistumisDatePicker);
            populateVarausTable(varausTable, tuloDatePicker.getValue(), poistumisDatePicker.getValue());
        });
        poistumisDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            populateFreeRoomTable(huoneTable, 1, tuloDatePicker, poistumisDatePicker);
            populateVarausTable(varausTable, tuloDatePicker.getValue(), poistumisDatePicker.getValue());
        });

        // Event listener for the reservation table. Set the check-out date to the selected reservation
        /*varausTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                poistumisDatePicker.setValue(newValue.getLoppuPvm());
            }
        });

         */

        checkInButton.setOnAction(e -> {
            CheckInButtonAction(huoneTable, varausTable);
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



    private void CheckInButtonAction(TableView<Huone> huoneTable, TableView<Varaus> varausTable) {
        Huone selectedRoom = huoneTable.getSelectionModel().getSelectedItem();
        Varaus selectedVaraus = varausTable.getSelectionModel().getSelectedItem();
        if (selectedRoom != null && selectedVaraus != null) {
            Integer previousRoom = selectedRoom.getHuone_id();

            if (selectedVaraus.getHuoneId() != null) {
                huoneController.updateHuoneStatusById(selectedVaraus.getHuoneId(), "Vapaa");
            }

            huoneController.updateHuoneStatusById(selectedRoom.getHuone_id(), "Varattu");
            varausController.updateVarausHuoneById(selectedVaraus.getVarausId(), selectedRoom.getHuone_id());
        }

    }

    private TableView<Huone> createHuoneTable() {
        TableView<Huone> huoneTableView = new TableView<>();
        // Set the width of the table
        huoneTableView.setPrefWidth(950);
        huoneTableView.setPrefHeight(400);

        TableColumn<Huone, Integer> numeroColumn = new TableColumn<>("Huoneen Numero");
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("huone_nro"));
        numeroColumn.setMinWidth(94);

        TableColumn<Huone, String> tyyppiColumn = new TableColumn<>("Huone Tyyppi");
        tyyppiColumn.setCellValueFactory(new PropertyValueFactory<>("huone_tyyppi"));
        tyyppiColumn.setMinWidth(150);

        TableColumn<Huone, Double> hintaColumn = new TableColumn<>("Huone Hinta");
        hintaColumn.setCellValueFactory(new PropertyValueFactory<>("huone_hinta"));
        hintaColumn.setMinWidth(150);

        TableColumn<Huone, String> statusColumn = new TableColumn<>("Huone Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("huone_tila"));
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

        TableColumn<Varaus, Integer> idColumn = new TableColumn<>("Varaus ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("varausId"));
        idColumn.setMinWidth(90);

        TableColumn<Varaus, String> asiakasColumn = new TableColumn<>("Nimi");
        asiakasColumn.setCellValueFactory(new PropertyValueFactory<>("nimi"));
        asiakasColumn.setMinWidth(230);

        TableColumn<Varaus, Integer> roomColumn = new TableColumn<>("Huone Nro");
        roomColumn.setCellValueFactory(new PropertyValueFactory<>("huoneNro"));
        roomColumn.setMinWidth(90);

        TableColumn<Varaus, String> startDateColumn = new TableColumn<>("Alkupäivämäärä");
        startDateColumn.setCellValueFactory(new PropertyValueFactory<>("alkuPvm"));
        startDateColumn.setMinWidth(153);

        TableColumn<Varaus, String> endDateColumn = new TableColumn<>("Loppupäivämäärä");
        endDateColumn.setCellValueFactory(new PropertyValueFactory<>("loppuPvm"));
        endDateColumn.setMinWidth(153);

        varausTable.getColumns().add(idColumn);
        varausTable.getColumns().add(roomColumn);
        varausTable.getColumns().add(asiakasColumn);
        varausTable.getColumns().add(startDateColumn);
        varausTable.getColumns().add(endDateColumn);

        return varausTable;
    }

}
