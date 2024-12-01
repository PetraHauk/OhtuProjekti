package view.sivut;

import controller.AsiakasController;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import model.enteties.Asiakas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for AsiakasSivu
 */
class AsiakasSivuTest extends ApplicationTest {

    private AsiakasSivu asiakasSivu;
    private AsiakasController mockController;

    @BeforeEach
    public void setUp() {
        asiakasSivu = new AsiakasSivu();
        mockController = mock(AsiakasController.class);
        asiakasSivu.setAsiakasController(mockController); // Use setter method
    }

    @Test
    void testPopulateCustomerTable_Success() {
        // Arrange
        List<Asiakas> mockCustomers = Arrays.asList(
                new Asiakas(1, "John", "Doe", "john.doe@example.com", "123456789", 4, "VIP"),
                new Asiakas(2, "Jane", "Smith", "jane.smith@example.com", "987654321", 2, "Regular")
        );
        when(mockController.findAllAsiakkaat()).thenReturn(mockCustomers);

        TableView<Asiakas> customerTable = asiakasSivu.createCustomerTable();

        // Act
        Platform.runLater(() -> asiakasSivu.populateCustomerTable(customerTable));
        WaitForAsyncUtils.waitForFxEvents();

        // Assert
        assertEquals(2, customerTable.getItems().size(), "Customer table should contain 2 items.");
    }

    @Test
    void testPopulateCustomerTable_Failure() {
        // Arrange
        when(mockController.findAllAsiakkaat()).thenThrow(new RuntimeException("Database error"));

        TableView<Asiakas> customerTable = asiakasSivu.createCustomerTable();

        // Act
        Platform.runLater(() -> asiakasSivu.populateCustomerTable(customerTable));

        // Assert
        Platform.runLater(() -> {
            assertEquals(0, customerTable.getItems().size(), "Customer table should be empty when an exception occurs.");
            assertTrue(customerTable.getPlaceholder() instanceof Label, "Placeholder should be a Label when loading fails.");
        });
    }

    @Test
    void testAddCustomerButton_TriggersAction() {
        Platform.runLater(() -> {
            VBox vbox = asiakasSivu.createAsiakkaat();
            Button addButton = (Button) vbox.getChildren().get(2); // Assuming button is the third child
            assertNotNull(addButton, "Add Customer button should exist.");

            // Mocking action event
            addButton.fire();
            verify(mockController, times(0)).addAsiakas(anyString(), anyString(), anyString(), anyString(), anyInt(), anyString());
        });
    }
}
