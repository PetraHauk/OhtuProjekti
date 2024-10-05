package view.sivut;

import controller.*;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.enteties.*;
import model.service.CurrencyConverter;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

public class CheckOut {

    private LaskuController laskuController;
    private HuoneController huoneController;
    private AsiakasController asiakasController;
    private VarausController varausController;
    private HotelliController hotelliController;

    public CheckOut() {
        laskuController = new LaskuController();
        huoneController = new HuoneController();
        asiakasController = new AsiakasController();
        varausController = new VarausController();
        hotelliController = new HotelliController();
    }

    // Creates the content for Check-out
    public VBox createCheckOut() {
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
                                    kokonaishinta += summa;

                                    String hintaStr = String.format("%.2f %s", hinta, valuutta);
                                    String summaStr = String.format("%.2f %s", summa, valuutta);
                                    String kokonaishintaStr = String.format("%.2f %s", kokonaishinta, valuutta);

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
                                            summaStr,      // Muotoile summa kahdelle desimaalille
                                            kokonaishintaStr
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

        if (!laskuTable.getItems().isEmpty()) {
            // Get the last item from the list
            LaskuData lastLaskuData = laskuTable.getItems().get(laskuTable.getItems().size() - 1);

            String kokonaihinta = lastLaskuData.getKokonaisHinta();
            String valuutta = lastLaskuData.getValuutta();

            // Try to parse kokonaihinta as a double and use it in the format
            try {
                double hintaDouble = Double.parseDouble(kokonaihinta);
                loppuHintaLabel.setText(String.format("Yhdessä: %.2f %s", hintaDouble, valuutta));
            } catch (NumberFormatException e) {
                loppuHintaLabel.setText("Yhdessä: " + kokonaihinta + " ");
            }
        }

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


}