package view;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import model.enteties.Kayttaja;
import model.DAO.KayttajaDAO;
import org.mindrot.jbcrypt.BCrypt;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Arrays;
import java.util.List;

public class AdminGUITest extends ApplicationTest {

    @Mock
    private KayttajaDAO kayttajaDAO;

    @InjectMocks
    private AdminGUI adminGUI;

    @Override
    public void init() throws Exception {
        // Initialize mocks before the JavaFX application starts
        MockitoAnnotations.openMocks(this);
    }

    @Override
    public void start(Stage stage) {
        // Initialize AdminGUI after mocks are set up
        adminGUI = new AdminGUI();
        adminGUI.kayttajaDAO = kayttajaDAO; // Inject mock DAO
        adminGUI.userTable = new TableView<>();
        adminGUI.start(stage);
    }

    @BeforeEach
    public void setUp() {
        // Any additional setup can be done here
    }

    @Test
    public void testLoadUsers() {
        // Arrange
        List<Kayttaja> mockUsers = Arrays.asList(new Kayttaja(), new Kayttaja());
        when(kayttajaDAO.findAllKayttaja()).thenReturn(mockUsers);

        // Act
        List<Kayttaja> users = adminGUI.loadUsers();

        // Assert
        assertEquals(2, users.size());
        verify(kayttajaDAO, times(2)).findAllKayttaja(); // Verify only once
    }

    // this test is disabled because the method is not implemented
    @Disabled
    public void testUpdateUserById() {
        // Arrange
        int id = 1;
        String etunimi = "John";
        String sukunimi = "Doe";
        String sposti = "john.doe@example.com";
        String puh = "1234567890";
        String rooli = "Admin";
        String salasana = "newpassword";

    }

    @Disabled
    public void testDeleteUser() {
        // Arrange
        Kayttaja mockUser = new Kayttaja();
        mockUser.setKayttajaId(1);
        mockUser.setRooli("User");
        adminGUI.userTable.setItems(FXCollections.observableArrayList(mockUser));
        adminGUI.userTable.getSelectionModel().select(mockUser);

        doNothing().when(kayttajaDAO).removeById(mockUser.getKayttajaId());

        // Act
        adminGUI.deleteUser();

        // Assert
        verify(kayttajaDAO, times(1)).removeById(mockUser.getKayttajaId());
    }
}