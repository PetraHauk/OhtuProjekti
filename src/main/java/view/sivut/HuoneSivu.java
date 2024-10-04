package view.sivut;

import controller.HuoneController;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
}
