package view.sivut;

import controller.AsiakasController;
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
import model.enteties.Huone;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import model.service.LocaleManager;

public class HuoneSivu {
    private AsiakasController asiakasController;
    private HuoneController huoneController;
    private ResourceBundle bundle;
    String selectedLanguage = LocaleManager.getLanguageName();

    public HuoneSivu() {
        this.huoneController = new HuoneController();

        Locale currentLocale = LocaleManager.getCurrentLocale();
        bundle = ResourceBundle.getBundle("messages", currentLocale);
    }

    public VBox createHuoneet() {
        VBox huoneetInfo = new VBox(10);
        huoneetInfo.getStyleClass().add("info");
        Label huoneetOtsikkoLabel = new Label(bundle.getString("HuonesivuOtsikkoLabel"));
        huoneetOtsikkoLabel.getStyleClass().add("otsikko");

        // Create the search bar
        TextField searchField = new TextField();
        searchField.setPromptText(bundle.getString("HuonesivuEtsiiHuoneitaLabel"));
        searchField.setMinWidth(200);

        // Create ComboBox for room type
        ComboBox<String> roomTypeComboBox = new ComboBox<>();
        roomTypeComboBox.getItems().addAll(bundle.getString("HuonesivuKaikkiTyypitLabel"), bundle.getString("HuonesivuYhdenHengenLabel"), bundle.getString("HuonesivuKahdenHengenLabel"), bundle.getString("HuonesivuKolmenHengenLabel"), bundle.getString("HuonesivuPerhehuoneLabel"), bundle.getString("HuonesivuSviittiLabel"));
        roomTypeComboBox.setValue(bundle.getString("HuonesivuKaikkiTyypitLabel")); // Default value
        roomTypeComboBox.setPromptText(bundle.getString("HuonesivuValitseTyyppiLabel"));

        // Create ComboBox for room status
        ComboBox<String> roomStatusComboBox = new ComboBox<>();
        roomStatusComboBox.getItems().addAll(bundle.getString("HuonesivuKaikkiTilatLabel"), bundle.getString("EtusivuVapaaLabel"), bundle.getString("EtusivuVarattuLabel"), bundle.getString("EtusivuSiivousLabel"));
        roomStatusComboBox.setValue(bundle.getString("HuonesivuKaikkiTilatLabel")); // Default value
        roomStatusComboBox.setPromptText(bundle.getString("HuonesivuValitseTilaLabel"));

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

        Button addRoomButton = new Button(bundle.getString("HuonesivuLisääHuoneLabel"));
        addRoomButton.setOnAction(e -> openAddRoomWindow(roomTable));
        addRoomButton.getStyleClass().add("yellow-btn");

        huoneetInfo.getChildren().addAll(huoneetOtsikkoLabel, filterBox, roomTable, addRoomButton);
        return huoneetInfo;
    }

    void filterTable(TableView<Huone> roomTable, String searchText, String selectedRoomType, String selectedRoomStatus) {
        // If no searchText or filters, populate the full table
        if ((searchText == null || searchText.isEmpty()) &&
                (bundle.getString("HuonesivuKaikkiTyypitLabel").equals(selectedRoomType)) &&
                (bundle.getString("HuonesivuKaikkiTilatLabel").equals(selectedRoomStatus))) {
            populateRoomTable(roomTable, 1); // Assuming hotel ID is 1 for this example
            return;
        }

        // Always filter from the full list of rooms
        List<Huone> allRooms = roomTable.getItems();  // You may want to keep the full list in a separate variable for better filtering
        List<Huone> filteredRooms = allRooms.stream()
                .filter(huone -> {
                    boolean matchesSearchText = searchText == null || searchText.isEmpty() ||
                            String.valueOf(huone.getHuone_nro()).contains(searchText) ||
                            huone.getHuoneTyyppi(selectedLanguage).toLowerCase().contains(searchText.toLowerCase()) ||
                            String.valueOf(huone.getHuone_hinta()).contains(searchText) ||
                            huone.getHuoneTila(selectedLanguage).toLowerCase().contains(searchText.toLowerCase());

                    boolean matchesRoomType = bundle.getString("HuonesivuKaikkiTyypitLabel").equals(selectedRoomType) ||
                            huone.getHuoneTyyppi(selectedLanguage).equalsIgnoreCase(selectedRoomType);

                    boolean matchesRoomStatus = bundle.getString("HuonesivuKaikkiTilatLabel").equals(selectedRoomStatus) ||
                            huone.getHuoneTila(selectedLanguage).equalsIgnoreCase(selectedRoomStatus);

                    // Return true only if all conditions match
                    return matchesSearchText && matchesRoomType && matchesRoomStatus;
                })
                .toList();

        // Update the table with the filtered rooms
        roomTable.getItems().setAll(filteredRooms);
    }

    private void openAddRoomWindow(TableView<Huone> roomTable) {
        Stage addRoomStage = new Stage();
        addRoomStage.setTitle(bundle.getString("HuonesivuLisääHuoneLabel"));

        VBox formLayout = new VBox(10);
        formLayout.setAlignment(Pos.CENTER);

        Label numberLabel = new Label(bundle.getString("HuonesivuHuoneNumeroLabel"));
        TextField numberField = new TextField();

        Label typeLabel = new Label(bundle.getString("HuonesivuHuoneTyyppiLabel"));
        ComboBox<String> typeField = new ComboBox<>();
        typeField.getItems().addAll(bundle.getString("HuonesivuYhdenHengenLabel"), bundle.getString("HuonesivuKahdenHengenLabel"), bundle.getString("HuonesivuKolmenHengenLabel"), bundle.getString("HuonesivuPerhehuoneLabel"), bundle.getString("HuonesivuSviittiLabel"));
        typeField.setPromptText(bundle.getString("HuonesivuValitseTyyppiLabel"));

        Label priceLabel = new Label(bundle.getString("HuonesivuHuoneHintaLabel"));
        TextField priceField = new TextField();

        Button saveButton = new Button(bundle.getString("HuonesivuLisääHuoneLabel"));
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
            List <String> tyyppiList = LocaleManager.getLocalizedTyyppiInput(roomType);
            String roomTyyppiFi = tyyppiList.get(0);
            String roomTyyppiEn = tyyppiList.get(1);
            String roomTyyppiRu = tyyppiList.get(2);
            String roomTyyppiZh = tyyppiList.get(3);

            String roomTilaFi= "Vapaa";
            String roomTilaEn= "Free";
            String roomTilaRu= "Свободно";
            String roomTilaZh= "空闲";

            double roomPrice = Double.parseDouble(priceField.getText());

            huoneController.lisaaHuone(roomNumber, roomTyyppiFi, roomTyyppiEn, roomTyyppiRu, roomTyyppiZh, roomTilaFi, roomTilaEn, roomTilaRu, roomTilaZh, roomPrice, 1);

            populateRoomTable(roomTable, 1);  // Assuming hotel ID is 1 for this example

            addRoomStage.close();

        } catch (NumberFormatException e) {
            System.out.println("Virheellinen syöte. Tarkista numero- ja hintakentät.");
        }
    }

    void populateRoomTable(TableView<Huone> roomTable, int hotelliId) {
        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(true);

        roomTable.getItems().clear();
        roomTable.setPlaceholder(loadingIndicator);

        Task<List<Huone>> fetchRoomsTask = new Task<>() {
            @Override
            protected List<Huone> call() throws Exception {
                // Fetch rooms based on hotel ID and locale-specific data
                return huoneController.FindHuoneetByHoteliId(hotelliId);
            }
        };

        fetchRoomsTask.setOnSucceeded(event -> {
            List<Huone> rooms = fetchRoomsTask.getValue();
            if (rooms != null && !rooms.isEmpty()) {
                // Update the table with fetched rooms
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
        huoneTableView.setPrefWidth(950);
        huoneTableView.setPrefHeight(400);

        TableColumn<Huone, Integer> numeroColumn = new TableColumn<>(bundle.getString("HuonesivuHuoneNumeroLabel"));
        numeroColumn.setCellValueFactory(new PropertyValueFactory<>("huone_nro"));
        numeroColumn.setMinWidth(120);

        // Get the current language from LocaleManager
        String selectedLanguageName = LocaleManager.getLanguageName();
        System.out.println(selectedLanguageName);

        // Dynamically set the column for room type based on the selected language
        String huoneTypeColumn = LocaleManager.getLocalColumnName(selectedLanguageName, "huone_tyyppi");
        System.out.println(huoneTypeColumn);

        TableColumn<Huone, String> tyyppiColumn = new TableColumn<>(bundle.getString("HuonesivuHuoneTyyppiLabel"));
        tyyppiColumn.setCellValueFactory(new PropertyValueFactory<>(huoneTypeColumn));
        tyyppiColumn.setMinWidth(293);

        // Dynamically set the column for room status based on the selected language
        String huoneTilaColumn = LocaleManager.getLocalColumnName(selectedLanguageName, "huone_tila");

        TableColumn<Huone, String> tilaColumn = new TableColumn<>(bundle.getString("HuonesivuHuoneStatusLabel"));
        tilaColumn.setCellValueFactory(new PropertyValueFactory<>(huoneTilaColumn));
        tilaColumn.setMinWidth(203);

        TableColumn<Huone, Double> hintaColumn = new TableColumn<>(bundle.getString("HuonesivuHuoneHintaLabel"));
        hintaColumn.setCellValueFactory(new PropertyValueFactory<>("huone_hinta"));
        hintaColumn.setMinWidth(200);

        TableColumn<Huone, Void> actionColumn = new TableColumn<>(bundle.getString("HuonesivuHuoneToiminnotLabel"));
        actionColumn.setMinWidth(140);

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(bundle.getString("HuonesivuMuokkaaHuonettaLabel"));
            private final Button deleteButton = new Button(bundle.getString("HuonesivuPoistaHuoneLabel"));
            private final HBox actionButtons = new HBox(editButton, deleteButton);

            {
                actionButtons.setSpacing(10);
                actionButtons.setAlignment(Pos.CENTER);

                // Handle edit and delete actions
                editButton.setOnAction(event -> {
                    Huone huone = getTableView().getItems().get(getIndex());
                    openMuokkaaHuoneWindow(huone, getTableView());
                });

                deleteButton.setOnAction(event -> {
                    Huone huone = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(huone);
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

        huoneTableView.getColumns().addAll(numeroColumn, tyyppiColumn, tilaColumn, hintaColumn, actionColumn);

        return huoneTableView;
    }


    private void openMuokkaaHuoneWindow(Huone huone, TableView<Huone> huoneTableView) {
        String selectedLanguage = LocaleManager.getCurrentLocale().getLanguage();

        Stage muokkaaHuoneStage = new Stage();
        muokkaaHuoneStage.setTitle(bundle.getString("HuonesivuMuokkaaHuonettaLabel"));

        // Pääasettelu, joka jakaa ikkunan osiin (yläosa, keskiosa, alaosa)
        BorderPane borderPane = new BorderPane();

        // Lomakkeen kenttien asettelu pystysuoraan (VBox)
        VBox formLayout = new VBox(10);
        formLayout.setAlignment(Pos.CENTER_LEFT); // Keskitetään vasemmalle
        formLayout.setPadding(new Insets(20)); // Asetetaan täyte

        // Kenttien ja tekstikenttien luonti
        Label huoneNroLabel = new Label(bundle.getString("HuonesivuHuoneNumeroLabel"));
        TextField huoneNro = new TextField();
        huoneNro.setText(String.valueOf(huone.getHuone_nro())); // Convert huone_nro to string

        Label huoneTypeLabel = new Label(bundle.getString("HuonesivuHuoneTyyppiLabel"));
        ComboBox<String> huoneType = new ComboBox<>();
        huoneType.getItems().addAll(bundle.getString("HuonesivuYhdenHengenLabel"), bundle.getString("HuonesivuKahdenHengenLabel"), bundle.getString("HuonesivuKolmenHengenLabel"), bundle.getString("HuonesivuPerhehuoneLabel"), bundle.getString("HuonesivuSviittiLabel"));
        huoneType.setValue(huone.getHuone_tyyppi_fi());


        Label huoneTilaLabel = new Label(bundle.getString("HuonesivuHuoneStatusLabel"));
        ComboBox<String> huoneTila = new ComboBox<>();
        huoneTila.getItems().addAll(bundle.getString("EtusivuVapaaLabel"), bundle.getString("EtusivuVarattuLabel"), bundle.getString("EtusivuSiivousLabel"));
        //huoneTila.setValue(huone.getHuone_tila_fi());
        huoneTila.setValue(huone.getHuone_tila_fi());

        Label huonePriceLabel = new Label(bundle.getString("HuonesivuHuoneHintaLabel"));
        TextField huonePrice = new TextField();
        huonePrice.setText(String.valueOf(huone.getHuone_hinta()));

        // Lisätään kentät lomakkeeseen (VBox)
        formLayout.getChildren().addAll(
                huoneNroLabel, huoneNro,
                huoneTypeLabel, huoneType,
                huoneTilaLabel, huoneTila,
                huonePriceLabel, huonePrice
        );

        Button saveButton = new Button(bundle.getString("HuonesivuTallennaLabel"));
        Button cancelButton = new Button(bundle.getString("HuonesivuPeruutaLabel"));

        // HBox save and cancel buttons
        HBox buttonBox = new HBox(10, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Toiminnallisuus napille
        saveButton.setOnAction(e -> {
            // Päivitetään huoneen tiedot
            huoneController.updateHuoneById(
                    huone.getHuone_id(),
                    Integer.parseInt(huoneNro.getText()),
                    huoneType.getValue(),
                    huoneTila.getValue(),
                    Double.parseDouble(huonePrice.getText())
            );

            // Päivitetään taulukko
            populateRoomTable(huoneTableView, 1); // Oletetaan hotellin ID olevan 1 esimerkissä
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
