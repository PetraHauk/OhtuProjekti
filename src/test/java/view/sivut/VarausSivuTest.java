package view.sivut;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class VarausSivuTest extends ApplicationTest {

    private VarausSivu varausSivu;

    private VBox varaukset;

    @Override
    public void start(Stage stage) {
        varausSivu = new VarausSivu(); // Initialize your class that creates the VBox
        varaukset = varausSivu.createVaraukset();

        // Set the VBox as the root of the scene and show it
        Scene scene = new Scene(varaukset);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setUp() {
        // Initialize the VarausSivu class before each test
        varausSivu = new VarausSivu();
    }

    @Test
    void testCreateVarauksetNotNull() {
        VBox createdVaraukset = varausSivu.createVaraukset();
        assertNotNull(createdVaraukset, "The created VBox should not be null");
    }

    @Test
    void testCreateVarausButtonIsPresent() {
        VBox createdvaraukset = varausSivu.createVaraukset();
        Button createButton = (Button) createdvaraukset.lookup(".button.create");
        assertNotNull(createButton, "Create reservation button should be present");
        assertEquals("Luo varaus", createButton.getText(), "Button should have the label ''");
    }



    @Test
    void testInvalidDateValidation() {
        LocalDate currentDate = LocalDate.now(); // Capture current date at the start

        // Declare DatePicker variables here to ensure they're in the method scope
        DatePicker[] arrivalDate = new DatePicker[1];
        DatePicker[] departureDate = new DatePicker[1];

        Platform.runLater(() -> {
            // Ensure correct identification of distinct DatePicker elements
            arrivalDate[0] = (DatePicker) varaukset.lookup(".date-picker:first-of-type");
            departureDate[0] = (DatePicker) varaukset.lookup(".date-picker:last-of-type");

            if (arrivalDate[0] == null || departureDate[0] == null) {
                throw new AssertionError("DatePicker elements were not found.");
            }

            // Set dates independently
            arrivalDate[0].setValue(currentDate); // Set it explicitly
            departureDate[0].setValue(currentDate); // Set it explicitly

            System.out.println("Arrival Date (after setting): " + arrivalDate[0].getValue());
            System.out.println("Departure Date (after setting): " + departureDate[0].getValue());

            Button createButton = (Button) varaukset.lookup(".button");
            clickOn(createButton);
        });

        // Wait for the UI thread to complete
        WaitForAsyncUtils.waitForFxEvents();
        assertEquals(currentDate, arrivalDate[0].getValue(), "Arrival date should be today");
        assertEquals(currentDate, departureDate[0].getValue(), "Departure date should be today");
    }

    @Test
    void testSearchCustomerButtonIsFunctional() {
        Platform.runLater(() -> {
            VBox createdvaraukset = varausSivu.createVaraukset();
            Scene scene = new Scene(createdvaraukset);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

            // Now look for the button within the VBox
            Button searchCustomerButton = (Button) createdvaraukset.lookup(".button");
            assertNotNull(searchCustomerButton, "Search customer button should be present");

            // Simulate a click event
            clickOn(searchCustomerButton);
        });

        WaitForAsyncUtils.waitForFxEvents();
    }


    @Test
    @SuppressWarnings("unchecked")
    void testDropdownOptions() {
        Platform.runLater(() -> {
            VBox createdvaraukset = varausSivu.createVaraukset();
            ComboBox<String> comboBox = (ComboBox<String>) createdvaraukset.lookup(".combo-box");
            assertNotNull(comboBox, "Dropdown should be present");

            // Check that dropdown has expected items
            comboBox.getItems().forEach(System.out::println);
            assertTrue(comboBox.getItems().contains("Kokopaketti"), "Dropdown should contain 'Kokopaketti'");
            assertTrue(comboBox.getItems().contains("Huone aamiaisella"), "Dropdown should contain 'Huone aamiaisella'");
            assertTrue(comboBox.getItems().contains("Vain huone"), "Dropdown should contain 'Vain huone'");
        });

        // Wait for the UI thread to complete
        WaitForAsyncUtils.waitForFxEvents();
    }

}
