package view;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Window;
import javafx.stage.Stage;
import model.service.UserSession;
import model.DAO.KayttajaDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mindrot.jbcrypt.BCrypt;
import org.testfx.framework.junit5.ApplicationTest;

public class LoginGuiTest extends ApplicationTest {

    @Mock
    private KayttajaDAO kayttajaDAO;

    @InjectMocks
    private LoginGui loginGui;

    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        loginGui = new LoginGui();
        loginGui.start(stage);
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSuccessfulLogin() throws Exception {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String role = "Admin";

        // Mock the behavior of kayttajaDAO
        when(kayttajaDAO.findPasswordByEmail(email)).thenReturn(hashedPassword);
        when(kayttajaDAO.findRooliByEmail(email)).thenReturn(role);

        // Act
        clickOn("#emailField").write(email);
        clickOn("#passwordField").write(password);
        clickOn("#loginButton");

        // Assert
        verify(kayttajaDAO, times(1)).findPasswordByEmail(email);
        verify(kayttajaDAO, times(1)).findRooliByEmail(email);

        // Assert that UserSession has the correct values
        assertEquals(email, UserSession.getUsername());
        assertEquals(role, UserSession.getRooli());

        // Assert that the primary stage is closed (login success)
        assertFalse(primaryStage.isShowing());
    }


    @Test
    public void testFailedLogin() {
        // Arrange
        String email = "test@example.com";
        String password = "wrongpassword";
        String hashedPassword = BCrypt.hashpw("correctpassword", BCrypt.gensalt());

        when(kayttajaDAO.findPasswordByEmail(email)).thenReturn(hashedPassword);

        // Act
        clickOn("#emailField").write(email);
        clickOn("#passwordField").write(password);
        clickOn("#loginButton");

        // Assert that the alert box appears
        verify(kayttajaDAO, times(1)).findPasswordByEmail(email);

        // Get the alert and assert its properties
        Alert alert = getAlert();
        assertNotNull(alert);  // Check that the alert is not null
        assertEquals("Kirjautuminen epäonnistui", alert.getTitle());  // Check the alert's title
        assertEquals("Tarkista sähköposti ja salasana.", alert.getContentText());  // Check the alert's content
    }



    @Test
    public void testOpenRegistrationPage() {
        // Act
        clickOn("#registerButton");

        // Assert that the original login stage is closed
        assertFalse(primaryStage.isShowing());
    }

    @Disabled
    public void testEmptyFields() {
        // Act
        clickOn("#loginButton");

        // Assert that the alert box appears when fields are empty
        verify(kayttajaDAO, times(0)).findPasswordByEmail(anyString());

        Alert alert = getAlert();
        assertNotNull(alert);
        assertEquals("Kirjautuminen epäonnistui", alert.getTitle());
        assertEquals("Tarkista sähköposti ja salasana.", alert.getContentText());
    }

    private Alert getAlert() {
        // Use TestFX to look up the dialog pane
        DialogPane dialogPane = lookup(".dialog-pane").query();

        if (dialogPane != null) {
            // Get the window that contains the dialog
            Window window = dialogPane.getScene().getWindow();
            if (window instanceof Stage) {
                Stage stage = (Stage) window;
                // Check if the stage's user data contains the Alert
                if (stage.getUserData() instanceof Alert) {
                    return (Alert) stage.getUserData();  // Return the Alert instance
                }
            }
        }
        return null;  // Return null if no alert is found
    }





}
