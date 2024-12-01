package view;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.service.UserSession;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OhjelmistoGUITest extends ApplicationTest {

    private OhjelmistoGUI ohjelmistoGUI;

    @Override
    public void start(Stage stage) {
        // Ensure the rooli is set before the GUI is initialized
        UserSession.setRooli("Admin");
        ohjelmistoGUI = new OhjelmistoGUI();
        ohjelmistoGUI.start(stage);
    }
    @Test
    void testShowRoomsButtonAction() {
        // Find the "showRoomsButton" and click it
        Button showRoomsButton = lookup("#showRoomsButton").queryButton();
        assertNotNull(showRoomsButton);
        clickOn(showRoomsButton);

        // Verify that the Huoneet view is displayed
        VBox huoneetView = lookup(".info").queryAs(VBox.class);
        assertNotNull(huoneetView);
    }
    @Test
    void testShowCustomersButtonAction() {
        // Find the "showCustomersButton" and click it
        Button showCustomersButton = lookup("#showCustomersButton").queryButton();
        assertNotNull(showCustomersButton);
        clickOn(showCustomersButton);

        // Verify that the Asiakkaat view is displayed
        VBox asiakasView = lookup(".info").queryAs(VBox.class);
        assertNotNull(asiakasView);
    }
}
