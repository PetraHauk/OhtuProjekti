package view;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import model.DAO.KayttajaDAO;
import model.enteties.Kayttaja;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class
RegistrationGuiTest {

    private RegistrationGui registrationGui;
    private KayttajaDAO mockKayttajaDAO;

    @BeforeAll
    public static void initJavaFX() {
        // Initialize JavaFX environment
        new JFXPanel();
        // Set the system property to indicate test mode
        System.setProperty("test.mode", "true");
    }

    @BeforeEach
    public void setUp() {
        registrationGui = new RegistrationGui();
        mockKayttajaDAO = Mockito.mock(KayttajaDAO.class);
        registrationGui.setKayttajaDAO(mockKayttajaDAO);
    }

    @Test
    public void testHandleRegister_Success() throws InterruptedException {
        // Given valid input
        String etunimi = "John";
        String sukunimi = "Doe";
        String sposti = "john.doe@example.com";
        String puh = "123456789";
        String salasana = "password";

        when(mockKayttajaDAO.onkoEmailOlemassa(sposti)).thenReturn(false);

        // Create a CountDownLatch to wait for the JavaFX thread
        CountDownLatch latch = new CountDownLatch(1);

        // Act
        Platform.runLater(() -> {
            registrationGui.handleRegister(etunimi, sukunimi, sposti, puh, salasana, new Stage());
            latch.countDown(); // Decrement the latch count
        });

        // Wait for the latch to reach zero
        latch.await();

        // Assert that the persist method was called
        verify(mockKayttajaDAO, times(1)).persist(any(Kayttaja.class));
    }

    @Test
    public void testHandleRegister_EmailAlreadyExists() {
        // Given valid input but email already exists
        String etunimi = "Jane";
        String sukunimi = "Doe";
        String sposti = "jane.doe@example.com";
        String puh = "987654321";
        String salasana = "password";

        when(mockKayttajaDAO.onkoEmailOlemassa(sposti)).thenReturn(true);

        // Act
        Platform.runLater(() -> registrationGui.handleRegister(etunimi, sukunimi, sposti, puh, salasana, new Stage()));

        // Wait for the task to complete
        waitForFX();

        // Assert that the persist method was never called
        verify(mockKayttajaDAO, never()).persist(any(Kayttaja.class));
    }

    @Test
    public void testHandleRegister_EmptyFields() {
        // Given empty input
        String etunimi = "";
        String sukunimi = "";
        String sposti = "";
        String puh = "";
        String salasana = "";

        // Act
        Platform.runLater(() -> registrationGui.handleRegister(etunimi, sukunimi, sposti, puh, salasana, new Stage()));

        // Wait for the task to complete
        waitForFX();

        // Assert that the persist method was never called
        verify(mockKayttajaDAO, never()).persist(any(Kayttaja.class));
    }

    private void waitForFX() {
        // This method will wait for the JavaFX thread to process events
        try {
            Thread.sleep(100); // Wait a bit for JavaFX to process events
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}