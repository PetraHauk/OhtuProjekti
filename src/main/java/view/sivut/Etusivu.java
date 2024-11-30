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
import model.service.LocaleManager;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class Etusivu {

    private HuoneController huoneController;
    private LaskuController laskuController; // Add the LaskuController

    private ResourceBundle bundle;

    public Etusivu() {
        this.huoneController = new HuoneController(); // Initialize the controllers
        this.laskuController = new LaskuController();

        Locale currentLocale = LocaleManager.getCurrentLocale();
        bundle = ResourceBundle.getBundle("messages", currentLocale);
    }

    public VBox createEtusivu(int hotelliId) {
        VBox etusivuInfo = new VBox(10);
        etusivuInfo.getStyleClass().add("info");

        Label etusivuOtsikkoLabel = new Label(bundle.getString("EtusivuOtsikkoLabel"));
        etusivuOtsikkoLabel.getStyleClass().add("otsikko");

        Label introLabel = new Label(bundle.getString("EtusivuIntroLabel"));
        introLabel.setWrapText(true);
        introLabel.setMaxWidth(400);

        // Add room status PieChart
        PieChart huoneStatusChart = createRoomStatusChart(hotelliId);

        // Add total money for all paid invoices
        double totalPaidAmount = laskuController.calculateTotalForPaidLaskut();
        Label totalMoneyLabel = new Label(bundle.getString("EtusivuTuottoLabel") + " " + totalPaidAmount + " " + bundle.getString("EtusivuEURLabel"));
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
        List<Huone> rooms = huoneController.findHuoneetByHoteliId(hotelliId);

        // if rooms is null
        if (rooms == null) {
            return new PieChart();
        }

        // Calculate the counts using equalsIgnoreCase for case-insensitive comparison
        long varattuCount = rooms.stream().filter(r -> r.getHuoneTilaFi().equalsIgnoreCase("Varattu")).count();
        long vapaaCount = rooms.stream().filter(r -> r.getHuoneTilaFi().equalsIgnoreCase("Vapaa")).count();
        long kaipaaSiivoustaCount = rooms.stream().filter(r -> r.getHuoneTilaFi().equalsIgnoreCase("Siivous")).count();

        // Create PieChart data with room counts in the label
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data(bundle.getString("EtusivuVarattuLabel") + ": " + varattuCount, varattuCount),
                new PieChart.Data(bundle.getString("EtusivuVapaaLabel") + ": " + vapaaCount, vapaaCount),
                new PieChart.Data(bundle.getString("EtusivuSiivousLabel") + ": " + kaipaaSiivoustaCount, kaipaaSiivoustaCount)
        );

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle(bundle.getString("EtusivuHuoneidenTilaLabel"));

        return pieChart;
    }
}
