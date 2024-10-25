package view.sivut;

import controller.*;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.enteties.*;
import model.service.CurrencyConverter;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        VBox checkOutInfo = new VBox(15);  // Main layout for the Check-Out section
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

        // Button for fetching invoices
        Button haeLaskutButton = new Button("Hae laskut");
        haeLaskutButton.getStyleClass().add("yellow-btn");

        // Layout for customer search (on a single row)
        HBox customerSearchRow = new HBox(10);  // Horizontal layout for name inputs and the button
        customerSearchRow.setAlignment(Pos.CENTER_LEFT);
        HBox.setMargin(haeLaskutButton, new Insets(0, 0, -20, 10));  // Set margin for the button to the left
        customerSearchRow.getChildren().addAll(etunimiInfo, sukunimiInfo, haeLaskutButton);

        // Invoice section
        VBox maksattavaLaskut = new VBox(5);
        Label maksattavaLaskuOtsikko = new Label("Laskut");
        maksattavaLaskuOtsikko.getStyleClass().add("otsikko");
        TableView<LaskuData> laskuTable = createLaskuTable();
        Label loppuHintaLabel = new Label("Yhdessä: 0.00");
        Button maksuButton = new Button("Maksaa");
        maksuButton.getStyleClass().add("yellow-btn");
        Button printButton = new Button("Tulosta kuitti");
        printButton.getStyleClass().add("yellow-btn");

        // Adding all invoice-related components to one VBox
        maksattavaLaskut.getChildren().addAll(maksattavaLaskuOtsikko, laskuTable, loppuHintaLabel, maksuButton, printButton);

        // Main layout structure
        VBox checkOutLayout = new VBox(20);  // Main vertical layout with spacing between components
        checkOutLayout.getChildren().addAll(checkOutInfoLabel, customerSearchRow, maksattavaLaskut);

        // Button logic for fetching and displaying invoices
        haeLaskutButton.setOnAction(e -> {
            laskuTable.getItems().clear();

            // Retrieve customers by first and last name
            List<Asiakas> asiakkaat = asiakasController.findByNimet(asiakasEtunimiInput.getText(), asiakasSukunimiInput.getText());
            if (asiakkaat == null || asiakkaat.isEmpty()) {
                showAlert("Virhe", "Asiakkaan nimellä ei löytynyt asiakkaita.");
                return;
            }

            double kokonaishinta = 0.00;
            String valuutta = "";  // Initialize currency

            // Loop through the customers and their invoices
            for (Asiakas asiakas : asiakkaat) {
                int asiakasId = asiakas.getAsiakasId();
                List<Lasku> laskut = laskuController.findLaskuByAsiakasId(asiakasId);

                if (laskut != null) {
                    for (Lasku lasku : laskut) {
                        int laskuId = lasku.getLaskuId();
                        List<Varaus> varaukset = varausController.findByLaskuId(laskuId);
                        valuutta = lasku.getValuutta();  // Set currency

                        // Process reservations and calculate invoice details
                        for (Varaus varaus : varaukset) {
                            LocalDate alkuPvm = varaus.getAlkuPvm();
                            LocalDate loppuPvm = varaus.getLoppuPvm();
                            int paivat = Period.between(alkuPvm, loppuPvm).getDays();

                            if (loppuPvm.isAfter(LocalDate.now()) || loppuPvm.isEqual(LocalDate.now())) {
                                Huone huone = huoneController.findHuoneById(varaus.getHuoneId());
                                if (huone != null) {
                                    double hinta = huone.getHuone_hinta();
                                    if (valuutta.equals("USD")) {
                                        hinta = CurrencyConverter.convertCurrency("EUR", "USD", hinta);
                                    }

                                    double summa = hinta * paivat;
                                    kokonaishinta += summa;

                                    String hintaStr = String.format("%.2f %s", hinta, valuutta);
                                    String summaStr = String.format("%.2f %s", summa, valuutta);
                                    String kokonaishintaStr = String.format("%.2f %s", kokonaishinta, valuutta);

                                    // Populate the table with the invoice data
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
                                            hintaStr,
                                            summaStr,
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

            // Update total price label after all invoices are processed
            loppuHintaLabel.setText(String.format("Yhdessä: %.2f %s", kokonaishinta, valuutta));
        });

        // Handle payment button logic
        maksuButton.setOnAction(e -> {
            if (laskuTable.getItems().isEmpty()) {
                showAlert("Virhe", "Ei laskuja maksettavaksi.");
                return;
            }
            for (LaskuData laskuData : laskuTable.getItems()) {
                laskuController.updateMaksuStatusById(laskuData.getLaskuId(), "Maksettu");
                laskuData.SetMaksuStatus("Maksettu");
                int huoneId = laskuData.getHuoneId();

                // Aseta huoneen tila siivoukselle
                huoneController.updateHuoneTilaById(huoneId, "Siivous");

                // Luodaan ScheduledExecutorService
                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

                // Asetetaan tehtävä, joka suoritetaan 30 minuutin kuluttua
                scheduler.schedule(() -> {
                    huoneController.updateHuoneTilaById(huoneId, "Vapaa");
                }, 30, TimeUnit.MINUTES);

                //refresh table
                laskuTable.refresh();
            }

            showMessage("Maksu", "Laskut on maksettu onnistuneesti.");
        });

        // Handle print button logic
        printButton.setOnAction(e -> openKuittiWindow(laskuTable));

        return checkOutLayout;
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

        Scene scene = new Scene(kuittiLayout, 1010, 500);
        kuittiStage.setScene(scene);
        kuittiStage.show();
    }

    private void tulostaKuitti(TableView<LaskuData> laskuTable) {
        System.out.println("Kuitti");
        System.out.println("Lasku ID | Status | Muoto | Valuutta | Alku Pvm | Loppu Pvm | Päivät | Hinta | Summa");
        for (LaskuData laskuData : laskuTable.getItems()) {
            System.out.println(laskuData.getLaskuId() + " | " +
                    laskuData.getHuoneNro() + " | " +
                    laskuData.getHuoneTyyppi() + " | " +
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
        laskuTable.setPrefWidth(970);
        laskuTable.setPrefHeight(300);

        TableColumn<LaskuData, Integer> laskuIdColumn = new TableColumn<>("Lasku ID");
        laskuIdColumn.setCellValueFactory(new PropertyValueFactory<>("laskuId"));
        laskuIdColumn.setPrefWidth(70);

        TableColumn<LaskuData, Integer> huoneNroColumn = new TableColumn<>("Huone Nro");
        huoneNroColumn.setCellValueFactory(new PropertyValueFactory<>("huoneNro"));
        huoneNroColumn.setPrefWidth(80);

        TableColumn<LaskuData, Integer> huoneTyyppiColumn = new TableColumn<>("Huone tyyppi");
        huoneTyyppiColumn.setCellValueFactory(new PropertyValueFactory<>("huoneTyyppi"));
        huoneTyyppiColumn.setPrefWidth(150);

        TableColumn<LaskuData, String> maksuStatusColumn = new TableColumn<>("Status ");
        maksuStatusColumn.setCellValueFactory(new PropertyValueFactory<>("maksuStatus"));
        maksuStatusColumn.setPrefWidth(100);

        TableColumn<LaskuData, String> varausMuotoColumn = new TableColumn<>("Muoto");
        varausMuotoColumn.setCellValueFactory(new PropertyValueFactory<>("varausMuoto"));
        varausMuotoColumn.setPrefWidth(120);

        TableColumn<LaskuData, String> valuuttaColumn = new TableColumn<>("Valuutta");
        valuuttaColumn.setCellValueFactory(new PropertyValueFactory<>("valuutta"));
        valuuttaColumn.setPrefWidth(30);

        TableColumn<LaskuData, String> alkuPvmColumn = new TableColumn<>("Alku Pvm");
        alkuPvmColumn.setCellValueFactory(new PropertyValueFactory<>("alkuPvm"));
        alkuPvmColumn.setPrefWidth(100);

        TableColumn<LaskuData, String> loppuPvmColumn = new TableColumn<>("Loppu Pvm");
        loppuPvmColumn.setCellValueFactory(new PropertyValueFactory<>("loppuPvm"));
        loppuPvmColumn.setPrefWidth(100);

        TableColumn<LaskuData, Integer> paivatColumn = new TableColumn<>("Päivät");
        paivatColumn.setCellValueFactory(new PropertyValueFactory<>("paivat"));
        paivatColumn.setPrefWidth(50);

        TableColumn<LaskuData, Double> hintaColumn = new TableColumn<>("Hinta");
        hintaColumn.setCellValueFactory(new PropertyValueFactory<>("hinta"));
        hintaColumn.setPrefWidth(100);

        TableColumn<LaskuData, Double> summaColumn = new TableColumn<>("Summa");
        summaColumn.setCellValueFactory(new PropertyValueFactory<>("summa"));
        summaColumn.setPrefWidth(100);

        laskuTable.getColumns().addAll(
                laskuIdColumn, huoneNroColumn, huoneTyyppiColumn, maksuStatusColumn, varausMuotoColumn, alkuPvmColumn, loppuPvmColumn,
                paivatColumn, hintaColumn, summaColumn);
        return laskuTable;
    }
}
