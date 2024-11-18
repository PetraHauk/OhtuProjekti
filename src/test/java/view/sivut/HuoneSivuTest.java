package view.sivut;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.enteties.Huone;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

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

    public static class TestApp extends Application {
        @Override
        public void start(Stage primaryStage) {
            // No-op
        }
    }
}