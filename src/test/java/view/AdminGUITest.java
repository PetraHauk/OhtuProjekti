package view;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import javafx.application.Platform;
import javafx.stage.Stage;
import model.service.LocaleManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javafx.scene.control.TableView;
import model.enteties.Kayttaja;
import model.DAO.KayttajaDAO;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

class AdminGUITest extends ApplicationTest {

    @Mock
    private KayttajaDAO kayttajaDAO;

    @InjectMocks
    private AdminGUI adminGUI;
    private Kayttaja testUser;

    @Override
    public void init() throws Exception {
        // Initialize mocks before the JavaFX application starts
        MockitoAnnotations.openMocks(this);
    }

    @Override
    public void start(Stage stage) {
        // Initialize AdminGUI after mocks are set up
        LocaleManager.setLocale(new Locale("fi", "FI"));
        adminGUI = new AdminGUI();
        adminGUI.kayttajaDAO = kayttajaDAO; // Inject mock DAO
        adminGUI.userTable = new TableView<>();
        adminGUI.start(stage);
    }


    @Test
    void testLoadUsers() {
        // Arrange
        List<Kayttaja> mockUsers = Arrays.asList(new Kayttaja(), new Kayttaja());
        when(kayttajaDAO.findAllKayttaja()).thenReturn(mockUsers);

        // Act
        List<Kayttaja> users = adminGUI.loadUsers();

        // Assert
        assertEquals(2, users.size());
        verify(kayttajaDAO, times(2)).findAllKayttaja(); // Verify only once
    }


    @Test
    void testAddNewUser() {
        // Arrange
        String etunimi = "Test";
        String sukunimi = "User";
        String sposti = "test.user@example.com";
        String puh = "1234567890";
        String salasana = "password";
        String rooli = "Hotellityöntekijä";

        when(kayttajaDAO.onkoEmailOlemassa(sposti)).thenReturn(false);
        doNothing().when(kayttajaDAO).persist(any(Kayttaja.class));

        // Act - Add User
        clickOn("#addUserButton");
        clickOn("#firstnameField").write(etunimi);
        clickOn("#lastnameField").write(sukunimi);
        clickOn("#emailField").write(sposti);
        clickOn("#phoneField").write(puh);
        clickOn("#passwordField").write(salasana);
        clickOn("#roleComboBox").clickOn(rooli);
        clickOn("#addUserSubmitButton");

        // Assert - Verify User Added
        verify(kayttajaDAO, times(1)).persist(any(Kayttaja.class));

        // Save the test user for deletion
        testUser = new Kayttaja();
        testUser.setEtunimi(etunimi);
        testUser.setSukunimi(sukunimi);
        testUser.setSposti(sposti);
        testUser.setPuh(puh);
        testUser.setSalasana(salasana);
        testUser.setRooli(rooli);
    }

    @AfterEach
    void tearDown() {
        if (testUser != null) {
            when(kayttajaDAO.findAllKayttaja()).thenReturn(Arrays.asList(testUser));
            Platform.runLater(() -> {
                adminGUI.loadAndDisplayUsers();
                adminGUI.userTable.getSelectionModel().select(testUser);
                adminGUI.deleteUser();
            });
            WaitForAsyncUtils.waitForFxEvents();
            verify(kayttajaDAO, times(1)).removeById(testUser.getKayttajaId());
        }
    }


}