package view.sivut;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;


import static org.junit.jupiter.api.Assertions.*;

class CheckInTest extends ApplicationTest {

    private CheckIn checkIn;

    @BeforeAll
    public static void setUpClass() throws Exception {
        ApplicationTest.launch(TestApp.class);
    }

    @Override
    public void start(Stage stage) {
        checkIn = new CheckIn();
        VBox root = checkIn.createCheckIn();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void testCreateCheckIn() {
        VBox checkInVBox = checkIn.createCheckIn();
        assertNotNull(checkInVBox, "CheckIn VBox should not be null");
        assertTrue(checkInVBox.getChildren().size() > 0, "CheckIn VBox should have children");
    }

    public static class TestApp extends Application {
        @Override
        public void start(Stage primaryStage) {
            // No-op
        }
    }
}