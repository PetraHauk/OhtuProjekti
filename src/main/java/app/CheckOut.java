package app;

/*
import app.LaskuData;
import controller.HuoneController;
import controller.AsiakasController;
import controller.VarausController;
import controller.LaskuController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;

import model.enteties.Huone;
import model.enteties.Asiakas;
import model.enteties.Varaus;
import model.enteties.Lasku;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

// Creates the content for Check-out
public class CheckOut {
    public VBox VheckOutInfo() {

        AsiakasController asiakasController = new AsiakasController();
        VarausController varausController = new VarausController();
        LaskuController laskuController = new LaskuController();
        HuoneController huoneController = new HuoneController();

        VBox checkOutInfo = new VBox(10);
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

        // Room number input
        VBox huoneNroInfo = new VBox(5);
        Label huoneNroLabel = new Label("Huoneen numero:");
        TextField huoneNroInput = new TextField();
        huoneNroInput.setPromptText("Syötä huoneen numero");
        huoneNroInfo.getChildren().addAll(huoneNroLabel, huoneNroInput);

        // Button for fetching bills
        Button haeLaskutButton = new Button("Hae laskut");

        // VBox for the table
        VBox maksattavaLaskut = new VBox(10);
        Label maksattavaLaskuOtsikko = new Label("Laskut");
        maksattavaLaskuOtsikko.getStyleClass().add("otsikko");
        TableView<LaskuData> laskuTable = createLaskuTable();
        maksattavaLaskut.getChildren().addAll(maksattavaLaskuOtsikko, laskuTable);

        // Button action handler
        haeLaskutButton.setOnAction(e -> {
            // Clear table before loading new data
            laskuTable.getItems().clear();

            // Check if the room number is a valid integer
            int huoneNro;
            try {
                huoneNro = Integer.parseInt(huoneNroInput.getText());
            } catch (NumberFormatException ex) {
                showAlert("Virhe", "Huoneen numero on virheellinen.");
                return;
            }

            // Fetch customers by names
            List<Asiakas> asiakkaat = asiakasController.findIdByNimet(asiakasEtunimiInput.getText(), asiakasSukunimiInput.getText());
            if (asiakkaat == null || asiakkaat.isEmpty()) {
                showAlert("Virhe", "Asiakkaan nimellä ei löytynyt asiakkaita.");
                return;
            }

            double kokonaishinta = 0.00;

            for (Asiakas asiakas : asiakkaat) {
                List<Lasku> laskut = laskuController.findLaskuByAsiakasId(asiakas.getAsiakasId());
                if (laskut != null) {
                    for (Lasku lasku : laskut) {
                        List<Varaus> varaukset = varausController.findByLaskuId(lasku.getLaskuId());

                        for (Varaus varaus : varaukset) {
                            LocalDate alkuPvm = varaus.getAlkuPvm();
                            LocalDate loppuPvm = varaus.getLoppuPvm();
                            int paivat = Period.between(alkuPvm, loppuPvm).getDays();

                            // Check if room number matches the current reservation
                            Huone huone = huoneController.findHuoneByNro(huoneNro);
                            if (huone != null) {
                                double hinta = huone.getHuone_hinta();
                                double summa = hinta * paivat;
                                kokonaishinta += summa;

                                populateLaskuTable(laskuTable, new LaskuData(
                                        lasku.getLaskuId(),
                                        lasku.getMaksuStatus(),
                                        lasku.getVarausMuoto(),
                                        lasku.getValuutta(),
                                        alkuPvm.toString(),
                                        loppuPvm.toString(),
                                        paivat,
                                        hinta,
                                        kokonaishinta
                                ));

                            } else {
                                showAlert("Virhe", "Virheellinen huoneen numero.");
                            }
                        }
                    }
                }
            }
        });

        // Layout structure
        checkOutInfo.getChildren().addAll(checkOutInfoLabel, etunimiInfo, sukunimiInfo, huoneNroInfo, haeLaskutButton);
        HBox laskuTiedot = new HBox(10);
        laskuTiedot.getChildren().addAll(checkOutInfo, maksattavaLaskut);

        VBox checkOut = new VBox(10);
        checkOut.getChildren().addAll(laskuTiedot);

        return checkOut;
    }

    // Utility method to show alert dialog
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // TableView method to display Lasku data
    private TableView<LaskuData> createLaskuTable() {
        TableView<LaskuData> laskuTable = new TableView<>();
        laskuTable.setPrefWidth(650);
        laskuTable.setPrefHeight(300);

        TableColumn<LaskuData, Integer> laskuIdColumn = new TableColumn<>("Lasku ID");
        laskuIdColumn.setCellValueFactory(new PropertyValueFactory<>("laskuId"));

        TableColumn<LaskuData, String> maksuStatusColumn = new TableColumn<>("Maksu Status");
        maksuStatusColumn.setCellValueFactory(new PropertyValueFactory<>("maksuStatus"));

        TableColumn<LaskuData, String> varausMuotoColumn = new TableColumn<>("Varaus Muoto");
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

        //TableColumn<LaskuData, Double> kokonaishintaColumn = new TableColumn<>("Kokonaishinta");
        //kokonaishintaColumn.setCellValueFactory(new PropertyValueFactory<>("kokonaishinta"));

        laskuTable.getColumns().addAll(
                laskuIdColumn, maksuStatusColumn, varausMuotoColumn,
                valuuttaColumn, alkuPvmColumn, loppuPvmColumn,
                paivatColumn, hintaColumn);

        return laskuTable;
    }

    // Populates the table with LaskuData
    private void populateLaskuTable(TableView<LaskuData> laskuTable, LaskuData laskuData) {
        ObservableList<LaskuData> data = laskuTable.getItems();
        data.add(laskuData);
        laskuTable.setItems(data);
    }

}

 */

