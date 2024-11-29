package view.sivut;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.enteties.Huone;
import model.service.LocaleManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;

public class HuoneSivuTest extends ApplicationTest {

    private HuoneSivu huoneSivu;

    @BeforeAll
    public static void setUpClass() throws Exception {
        ApplicationTest.launch(TestApp.class);

    }

    @Override
    public void start(Stage stage) {
        huoneSivu = new HuoneSivu();
        VBox root = huoneSivu.createHuoneet();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testCreateHuoneet() {
        VBox huoneetVBox = huoneSivu.createHuoneet();
        assertNotNull(huoneetVBox, "Huoneet VBox should not be null");
        assertTrue(huoneetVBox.getChildren().size() > 0, "Huoneet VBox should have children");

        // Check if the TableView is present and has columns
        TableView<Huone> roomTable = (TableView<Huone>) huoneetVBox.getChildren().stream()
                .filter(node -> node instanceof TableView)
                .findFirst()
                .orElse(null);

        assertNotNull(roomTable, "Room TableView should not be null");
        assertTrue(roomTable.getColumns().size() > 0, "Room TableView should have columns");
    }

    @Test
    public void testLocalizationHuoneet() {
        // Define the expected values for each locale
        Map<Locale, String> expectedValues = new HashMap<>();
        expectedValues.put(new Locale("fi", "FI"), "Huoneet");
        expectedValues.put(new Locale("ru", "RU"), "Номера");
        expectedValues.put(new Locale("sv", "SE"), "Rum");
        expectedValues.put(new Locale("zh", "CN"), "房间");
        expectedValues.put(new Locale("en", "GB"), "Rooms");

        for (Map.Entry<Locale, String> entry : expectedValues.entrySet()) {
            Locale locale = entry.getKey();
            String expectedValue = entry.getValue();

            // Set the locale
            LocaleManager.setLocale(locale);

            // Retrieve the actual value from the resource bundle
            ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
            String actualValue = bundle.getString("HuonesivuOtsikkoLabel");

            // Assert that the actual value matches the expected value
            assertEquals(expectedValue, actualValue, "Localization failed for locale: " + locale);
        }
    }

    @Test
    public void testFilterRooms() {
        TableView<Huone> roomTable = huoneSivu.createHuoneTable();
        huoneSivu.populateRoomTable(roomTable, 1); // Assuming hotel ID is 1

        // Simulate filtering by room type
        huoneSivu.filterTable(roomTable, "", "Yhden hengen", "Kaikki tilat");

        for (Huone huone : roomTable.getItems()) {
            assertEquals("Yhden hengen", huone.getHuone_tyyppi_fi(), "Room type should match the filter");
        }
    }

    @Test
    public void testEditRoom() {
        TableView<Huone> roomTable = huoneSivu.createHuoneTable();
        Huone testRoom = new Huone();
        roomTable.getItems().add(testRoom); // Add mock data for testing

        // Verify the room table is populated
        assertFalse(roomTable.getItems().isEmpty(), "Room table should not be empty");

        Huone roomToEdit = roomTable.getItems().get(0);
        int roomId = roomToEdit.getHuone_id();

        // Simulate editing a room
        Platform.runLater(() -> {
            roomToEdit.setHuone_nro(102);
            roomToEdit.setHuone_hinta(150.0);
            huoneSivu.openMuokkaaHuoneWindow(roomToEdit, roomTable);

            // Verify the room was edited
            Huone editedRoom = roomTable.getItems().stream().filter(huone -> huone.getHuone_id() == roomId).findFirst().orElse(null);
            assertNotNull(editedRoom, "Edited room should be present in the table");
            assertEquals(102, editedRoom.getHuone_nro(), "Room number should be updated");
            assertEquals(150.0, editedRoom.getHuone_hinta(), "Room price should be updated");
        });

        // Wait for the JavaFX operations to complete
        WaitForAsyncUtils.waitForFxEvents();
    }



    public static class TestApp extends Application {
        @Override
        public void start(Stage primaryStage) {
            // No-op
        }
    }
}