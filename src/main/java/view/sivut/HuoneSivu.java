package view.sivut;

import controller.HuoneController;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.enteties.Asiakas;
import model.enteties.Huone;

import java.util.List;

public class HuoneSivu {

    private HuoneController huoneController;

    public HuoneSivu() {
        this.huoneController = new HuoneController();
    }

    public VBox createHuoneet() {
        VBox huoneetInfo = new VBox(10);
        huoneetInfo.getStyleClass().add("info");
        Label huoneetOtsikkoLabel = new Label("Huoneet");
        huoneetOtsikkoLabel.getStyleClass().add("otsikko");

        // Create the search bar
        TextField searchField = new TextField();
        searchField.setPromptText("Etsi huoneita...");
        searchField.setMinWidth(200);

        // Create ComboBox for room type
        ComboBox<String> roomTypeComboBox = new ComboBox<>();
        roomTypeComboBox.getItems().addAll("Kaikki tyypit", "Yhden hengen huone", "Kahden hengen huone", "Kolmen hengen huone", "Perhehuone", "Sviitti");
        roomTypeComboBox.setValue("Kaikki tyypit"); // Default value
        roomTypeComboBox.setPromptText("Valitse huonetyyppi...");

        // Create ComboBox for room status
        ComboBox<String> roomStatusComboBox = new ComboBox<>();
        roomStatusComboBox.getItems().addAll("Kaikki tilat", "Vapaa", "Varattu", "Siivous");
        roomStatusComboBox.setValue("Kaikki tilat"); // Default value
        roomStatusComboBox.setPromptText("Valitse huoneen tila...");

        // Create a horizontal box to hold searchField, roomTypeComboBox, and roomStatusComboBox
        HBox filterBox = new HBox(10, searchField, roomTypeComboBox, roomStatusComboBox);
        filterBox.setAlignment(Pos.CENTER_LEFT);
        filterBox.setPadding(new Insets(10, 0, 10, 0));

        TableView<Huone> roomTable = createHuoneTable();

        // Assuming hotel ID is 1 for this example
        populateRoomTable(roomTable, 1);

        // Add listeners to filter the table when user interacts with search field or ComboBoxes
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterTable(roomTable, searchField.getText(), roomTypeComboBox.getValue(), roomStatusComboBox.getValue());
        });

        roomTypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            filterTable(roomTable, searchField.getText(), roomTypeComboBox.getValue(), roomStatusComboBox.getValue());
        });

        roomStatusComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            filterTable(roomTable, searchField.getText(), roomTypeComboBox.getValue(), roomStatusComboBox.getValue());
        });

        Button addRoomButton = new Button("Lisää uusi huone");
        addRoomButton.setOnAction(e -> openAddRoomWindow(roomTable));
        addRoomButton.getStyleClass().add("yellow-btn");

        huoneetInfo.getChildren().addAll(huoneetOtsikkoLabel, filterBox, roomTable, addRoomButton);
        return huoneetInfo;
    }


    private void filterTable(TableView<Huone> roomTable, String searchText, String selectedRoomType, String selectedRoomStatus) {
        // If no searchText or filters, populate the full table
        if ((searchText == null || searchText.isEmpty()) &&
                ("Kaikki tyypit".equals(selectedRoomType)) &&
                ("Kaikki tilat".equals(selectedRoomStatus))) {
            populateRoomTable(roomTable, 1); // Assuming hotel ID is 1 for this example
            return;
        }

        // Always filter from the full list of rooms
        List<Huone> allRooms = roomTable.getItems();  // You may want to keep the full list in a separate variable for better filtering
        List<Huone> filteredRooms = allRooms.stream()
                .filter(huone -> {
                    boolean matchesSearchText = searchText == null || searchText.isEmpty() ||
                            String.valueOf(huone.getHuone_nro()).contains(searchText) ||
                            huone.getHuone_tyyppi().toLowerCase().contains(searchText.toLowerCase()) ||
                            String.valueOf(huone.getHuone_hinta()).contains(searchText) ||
                            huone.getHuone_tila().toLowerCase().contains(searchText.toLowerCase());

                    boolean matchesRoomType = "Kaikki tyypit".equals(selectedRoomType) ||
                            huone.getHuone_tyyppi().equalsIgnoreCase(selectedRoomType);

                    boolean matchesRoomStatus = "Kaikki tilat".equals(selectedRoomStatus) ||
                            huone.getHuone_tila().equalsIgnoreCase(selectedRoomStatus);

                    // Return true only if all conditions match
                    return matchesSearchText && matchesRoomType && matchesRoomStatus;
                })
                .toList();

        // Update the table with the filtered rooms
        roomTable.getItems().setAll(filteredRooms);
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

    private TableView<Huone> createHuoneTable() {
        TableView<Huone> huoneTableView = new TableView<>();
        // Set the width of the table
        huoneTableView.setPrefWidth(950);
        huoneTableView.setPrefHeight(400);

        TableColumn<Huone, Integer> numeroColumn = new TableColumn<>("Huoneen Numero");
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("huone_nro"));
        numeroColumn.setMinWidth(100);

        TableColumn<Huone, String> tyyppiColumn = new TableColumn<>("Huone Tyyppi");
        tyyppiColumn.setCellValueFactory(new PropertyValueFactory<>("huone_tyyppi"));
        tyyppiColumn.setMinWidth(293);

        TableColumn<Huone, Double> hintaColumn = new TableColumn<>("Huone Hinta");
        hintaColumn.setCellValueFactory(new PropertyValueFactory<>("huone_hinta"));
        hintaColumn.setMinWidth(200);

        TableColumn<Huone, String> phoneColumn = new TableColumn<>("Huone Status");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("huone_tila"));
        phoneColumn.setMinWidth(200);

        TableColumn<Huone, Void> actionColumn = new TableColumn<>("Toiminnot");
        actionColumn.setMinWidth(140);

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
                    openMuokkaaHuoneWindow(huone, getTableView());
                    System.out.println("Muokkaa-painiketta painettu" + huone.getHuone_id());
                });


                // Poista-painikeen toiminnallisuus
                deleteButton.setOnAction(event -> {
                    Huone huone = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(huone); // Poistetaan asiakas listasta
                    huoneController.deleteHuone(huone.getHuone_id());
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

    private void openMuokkaaHuoneWindow(Huone huone, TableView<Huone> huoneTableView) {
        Stage muokkaaHuoneStage = new Stage();
        muokkaaHuoneStage.setTitle("Muokkaa huonetta");

        // Pääasettelu, joka jakaa ikkunan osiin (yläosa, keskiosa, alaosa)
        BorderPane borderPane = new BorderPane();

        // Lomakkeen kenttien asettelu pystysuoraan (VBox)
        VBox formLayout = new VBox(10);
        formLayout.setAlignment(Pos.CENTER_LEFT); // Keskitetään vasemmalle
        formLayout.setPadding(new Insets(20)); // Asetetaan täyte

        // Kenttien ja tekstikenttien luonti
        Label huoneNroLabel = new Label("Huoneen numero:");
        TextField huoneNro = new TextField();
        huoneNro.setText(String.valueOf(huone.getHuone_nro())); // Convert huone_nro to string

        Label huoneTypeLabel = new Label("Huone tyyppi:");
        ComboBox<String> huoneType = new ComboBox<>();
        huoneType.getItems().addAll("Yhden hengen huone", "Kahden hengen huone", "Kolmen hengen huone", "Perhehuone", "Sviitti");
        huoneType.setValue(huone.getHuone_tyyppi());

        Label huoneTilaLabel = new Label("Huoneen tila:");
        ComboBox<String> huoneTila = new ComboBox<>();
        huoneTila.getItems().addAll("Vapaa", "Varattu", "Siivous");
        huoneTila.setValue(huone.getHuone_tila());

        Label huonePriceLabel = new Label("Huoneen hinta per yö:");
        TextField huonePrice = new TextField();
        huonePrice.setText(String.valueOf(huone.getHuone_hinta()));

        // Lisätään kentät lomakkeeseen (VBox)
        formLayout.getChildren().addAll(
                huoneNroLabel, huoneNro,
                huoneTypeLabel, huoneType,
                huoneTilaLabel, huoneTila,
                huonePriceLabel, huonePrice
        );

        Button saveButton = new Button("Tallenna muutokset");
        Button cancelButton = new Button("Peruuta");

        // HBox save and cancel buttons
        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Toiminnallisuus napille
        saveButton.setOnAction(e -> {
            // Päivitetään asiakastiedot
            huoneController.updateHuoneById(
                    huone.getHuone_id(),
                    Integer.parseInt(huoneNro.getText()),
                    huoneType.getValue(),
                    huoneTila.getValue(),
                    Double.parseDouble(huonePrice.getText())
            );

            // Päivitetään taulukko
            populateRoomTable(huoneTableView, 1); // Assuming hotel ID is 1 for this example
            muokkaaHuoneStage.close();
        });

        cancelButton.setOnAction(e -> muokkaaHuoneStage.close());

        // Asetetaan lomake ja painikkeet BorderPaneen
        borderPane.setCenter(formLayout);
        borderPane.setBottom(buttonBox);
        BorderPane.setMargin(buttonBox, new Insets(10)); // Marginaali nappeihin

        // Aseta BorderPane kohtaukseksi ja näytä ikkuna
        Scene scene = new Scene(borderPane, 400, 500);
        muokkaaHuoneStage.setScene(scene);
        muokkaaHuoneStage.show(); // Tämä avaa muokkausikkunan
    }
}
