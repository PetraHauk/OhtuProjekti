package view.sivut;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class EtusivuTest extends ApplicationTest {

    private Etusivu etusivu;

    @Override
    public void start(Stage stage) {
        etusivu = new Etusivu();
        VBox etusivuInfo = etusivu.createEtusivu(1);
        Scene scene = new Scene(etusivuInfo);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testEtusivuComponents() {
        VBox etusivuInfo = etusivu.createEtusivu(1);

        assertNotNull(etusivuInfo, "Etusivu VBox should not be null");
        assertEquals(4, etusivuInfo.getChildren().size(), "Etusivu VBox should contain 4 children");

        // Check if the first child is a Label with the text "Etusivu"
        Label etusivuOtsikkoLabel = (Label) etusivuInfo.getChildren().get(0);
        assertEquals("Etusivu", etusivuOtsikkoLabel.getText(), "First label should have the text 'Etusivu'");

        // Check if the second child is a Label with the intro text
        Label introLabel = (Label) etusivuInfo.getChildren().get(1);
        assertTrue(introLabel.getText().contains("Tervetuloa hotellin hallintajärjestelmään!"), "Second label should contain the intro text");

        // Check if the third child is a PieChart
        assertTrue(etusivuInfo.getChildren().get(2) instanceof javafx.scene.chart.PieChart, "Third child should be a PieChart");

        // Check if the fourth child is a BorderPane containing the total money label
        assertTrue(etusivuInfo.getChildren().get(3) instanceof javafx.scene.layout.BorderPane, "Fourth child should be a BorderPane");
        javafx.scene.layout.BorderPane borderPane = (javafx.scene.layout.BorderPane) etusivuInfo.getChildren().get(3);
        Label totalMoneyLabel = (Label) borderPane.getCenter();
        assertTrue(totalMoneyLabel.getText().contains("Tuotto tässä kuussa:"), "Total money label should contain the text 'Tuotto tässä kuussa:'");
    }
}