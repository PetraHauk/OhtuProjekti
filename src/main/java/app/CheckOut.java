package app;
/*
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.enteties.Lasku;

import java.time.LocalDate;

import controller.VarausController;
import controller.LaskuController;
import controller.AsiakasController;


// Creates the content for Check-Out
public class checkOut() {
    private VarausController varausController = new VarausController();
    private LaskuController laskuController = new LaskuController();
    private AsiakasController asiakasController = new AsiakasController();


    // Creates the content for Check-out
    VBox createCheckOut() {
        VBox laskuInfo = new VBox(10);
        laskuInfo.getStyleClass().add("info");
        Label checkOutInfoLabel = new Label("Check-Out");
        checkOutInfoLabel.getStyleClass().add("otsikko");

        VBox hakuInfo = new VBox(0);
        Label huoneNroLabel = new Label("Huoneen numero:");
        TextField huoneNroInput = new TextField();
        huoneNroInput.setPromptText("Syötä huoneen numero");

        Label asiakasEtunimiLabel = new Label("Asiakkaan etunimi:");
        TextField asiakasEtunimiInput = new TextField();
        asiakasEtunimiInput.setPromptText("Syötä asiakkaan etunimi");

        Label asiakasSukunimiLabel = new Label("Asiakkaan sukunimi:");
        TextField asiakasSukunimiInput = new TextField();
        asiakasSukunimiInput.setPromptText("Syötä asiakkaan sukunimi");

        hakuInfo.getChildren().addAll(huoneNroInput, huoneNroLabel, asiakasEtunimiLabel, asiakasEtunimiInput, asiakasSukunimiLabel, asiakasSukunimiInput);
        laskuInfo.getChildren().addAll(checkOutInfoLabel, hakuInfo);

        VBox maksattavalaskut = new VBox(10);
        Label maksattavaLaskuOtsikko = new Label("Maksattavat laskut");
        maksattavaLaskuOtsikko.getStyleClass().add("otsikko");
        TableView<Lasku> laskuTable = creatLaskuTable();
        maksattavalaskut.getChildren().addAll(maksattavaLaskuOtsikko, laskuTable);

        laskuInfo.getChildren().addAll(checkOutInfoLabel, hakuInfo, maksattavalaskut);
        HBox laskuTiedot = new HBox(10);
        laskuTiedot.getChildren().addAll(laskuInfo, maksattavalaskut);

        VBox checkOut = new VBox(10);
        checkOut.getChildren().addAll(laskuTiedot);

        return checkOut;
    }


    // Luo laskutaulukko
    private TableView<Lasku> creatLaskuTable () {
        TableView<Lasku> laskuTable = new TableView<>();
        laskuTable.setPrefWidth(500);

        TableColumn<Lasku, Integer> idColumn = new TableColumn<>("Lasku ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("lasku_id"));

        TableColumn<Lasku, String> asiakasEtunimiColumn = new TableColumn<>("Etunimi");
        asiakasEtunimiColumn.setCellValueFactory(new PropertyValueFactory<>("etunimi"));

        TableColumn<Lasku, String> asiakasSukunimiColumn = new TableColumn<>("Sukunimi");
        asiakasSukunimiColumn.setCellValueFactory(new PropertyValueFactory<>("sukunimi"));

        TableColumn<Lasku, LocalDate> alkuPvmColumn = new TableColumn<>("Alku Pvm");
        alkuPvmColumn.setCellValueFactory(new PropertyValueFactory<>("alku_pvm"));

        TableColumn<Lasku, LocalDate> loppuPvmColumn = new TableColumn<>("Loppu Pvm");
        loppuPvmColumn.setCellValueFactory(new PropertyValueFactory<>("loppu_pvm"));

        TableColumn<Lasku, Integer> paivatColumn = new TableColumn<>("Päivät");
        paivatColumn.setCellValueFactory(new PropertyValueFactory<>("paivat"));

        TableColumn<Lasku, String> maksuStatusColumn = new TableColumn<>("Maksu Status");
        maksuStatusColumn.setCellValueFactory(new PropertyValueFactory<>("maksu_status"));

        TableColumn<Lasku, String> varausMuotoColumn = new TableColumn<>("Varaus Muoto");
        varausMuotoColumn.setCellValueFactory(new PropertyValueFactory<>("varaus_muoto"));

        TableColumn<Lasku, String> valuuttaColumn = new TableColumn<>("Valuutta");
        valuuttaColumn.setCellValueFactory(new PropertyValueFactory<>("valuutta"));

        TableColumn<Lasku, Double> hintaColumn = new TableColumn<>("Hinta");
        hintaColumn.setCellValueFactory(new PropertyValueFactory<>("hinta"));

        laskuTable.getColumns().add(idColumn);
        laskuTable.getColumns().add(asiakasEtunimiColumn);
        laskuTable.getColumns().add(asiakasSukunimiColumn);
        laskuTable.getColumns().add(alkuPvmColumn);
        laskuTable.getColumns().add(loppuPvmColumn);
        laskuTable.getColumns().add(paivatColumn);
        laskuTable.getColumns().add(maksuStatusColumn);
        laskuTable.getColumns().add(varausMuotoColumn);
        laskuTable.getColumns().add(valuuttaColumn);
        laskuTable.getColumns().add(hintaColumn);
        return laskuTable;
    }

    private void populateLaskuTable (TableView < Lasku > laskuTable, Object value){
        laskuTable.getItems().add(new Lasku(value.toString()));

    }

}

 */


