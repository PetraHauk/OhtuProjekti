package view.sivut;

import controller.HuoneController;
import controller.LaskuController;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane; // Import BorderPane
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.enteties.Huone;

import java.util.List;

public class Etusivu {

    private HuoneController huoneController;
    private LaskuController laskuController; // Add the LaskuController

    public Etusivu() {
        this.huoneController = new HuoneController(); // Initialize the controllers
        this.laskuController = new LaskuController();
    }

    public VBox createEtusivu(int hotelliId) {
        VBox etusivuInfo = new VBox(10);
        etusivuInfo.getStyleClass().add("info");

        Label etusivuOtsikkoLabel = new Label("Etusivu");
        etusivuOtsikkoLabel.getStyleClass().add("otsikko");

        Label introLabel = new Label("Tervetuloa hotellin hallintajärjestelmään! Tämä sivu tarjoaa yleiskatsauksen hotellin toiminnasta ja taloudellisesta tilasta.");
        introLabel.setWrapText(true);
        introLabel.setMaxWidth(400);

        // Add room status PieChart
        PieChart huoneStatusChart = createRoomStatusChart(hotelliId);

        // Add total money for all paid invoices
        double totalPaidAmount = laskuController.calculateTotalForPaidLaskut();
        Label totalMoneyLabel = new Label("Tuotto tässä kuussa: " + totalPaidAmount + " EUR");
        totalMoneyLabel.getStyleClass().add("total-money-label");

        // Wrap the label in a BorderPane to create a border
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(totalMoneyLabel);

        // Set the border with specified styles
        borderPane.setStyle("-fx-border-color: #c9c9c9; -fx-border-width: 2; -fx-padding: 10;");

        // Set a bigger font for the label
        totalMoneyLabel.setStyle("-fx-font-size: 16px;");

        // Add labels and chart to the view
        etusivuInfo.getChildren().addAll(etusivuOtsikkoLabel, introLabel, huoneStatusChart, borderPane);

        return etusivuInfo;
    }

    private PieChart createRoomStatusChart(int hotelliId) {
        // Get the room status data
        List<Huone> rooms = huoneController.FindHuoneetByHoteliId(hotelliId);

        // if rooms is null
        if (rooms == null) {
            return new PieChart();
        }

        // Calculate the counts using equalsIgnoreCase for case-insensitive comparison
        long varattuCount = rooms.stream().filter(r -> r.getHuone_tila().equalsIgnoreCase("Varattu")).count();
        long vapaaCount = rooms.stream().filter(r -> r.getHuone_tila().equalsIgnoreCase("Vapaa")).count();
        long kaipaaSiivoustaCount = rooms.stream().filter(r -> r.getHuone_tila().equalsIgnoreCase("Siivous")).count();

        // Create PieChart data with room counts in the label
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Varattu: " + varattuCount, varattuCount),
                new PieChart.Data("Vapaa: " + vapaaCount, vapaaCount),
                new PieChart.Data("Kaipaa Siivousta: " + kaipaaSiivoustaCount, kaipaaSiivoustaCount)
        );

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Huoneiden Tila");

        return pieChart;
    }
}
