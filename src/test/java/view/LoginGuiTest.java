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
    public void testSuccessfulLoginInChinese() throws Exception {
        // Arrange
        String email = "test@example.com";
        String password = "password123";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String role = "Admin";

        // Mock the behavior of kayttajaDAO
        when(kayttajaDAO.findPasswordByEmail(email)).thenReturn(hashedPassword);
        when(kayttajaDAO.findRooliByEmail(email)).thenReturn(role);

        // Change language to Swedish
        clickOn(".combo-box").clickOn("中文");

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

        // Get the alert dialog pane and assert its properties
        DialogPane alertDialogPane = getAlertDialogPane();
        assertNotNull(alertDialogPane);  // Check that the alert dialog pane is not null
        assertEquals("Väärä käyttäjätunnus tai salasana.", alertDialogPane.getContentText());  // Check the alert's content
    }



    @Test
    public void testOpenRegistrationPage() {
        // Act
        clickOn("#registerButton");

        // Assert that the original login stage is closed
        assertFalse(primaryStage.isShowing());
    }


    private DialogPane getAlertDialogPane() {
        return lookup(".dialog-pane").queryAs(DialogPane.class);
    }




}
