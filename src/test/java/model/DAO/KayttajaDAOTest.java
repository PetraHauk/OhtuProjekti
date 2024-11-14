package model.DAO;

import model.datasourse.MariaDbConnection;
import model.enteties.Kayttaja;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KayttajaDAOTest {

    private KayttajaDAO kayttajaDAO;

    @BeforeAll
    public void setUp() {
        kayttajaDAO = new KayttajaDAO();
    }

    @Test
    public void testi() {
        Kayttaja kayttaja = new Kayttaja();
        kayttaja.setEtunimi("Test");
        kayttaja.setSukunimi("User");
        kayttaja.setSposti("test.user@example.com");
        kayttaja.setPuh("123456789");
        kayttaja.setRooli("Admin");
        kayttaja.setSalasana("password123");

        kayttajaDAO.persist(kayttaja);
        int kayttajaId = kayttaja.getKayttajaId();

        Kayttaja fetchedKayttaja = kayttajaDAO.findById(kayttaja.getKayttajaId());
        assertNotNull(fetchedKayttaja);
        assertEquals("Test", fetchedKayttaja.getEtunimi());
        assertEquals("User", fetchedKayttaja.getSukunimi());
        assertEquals("test.user@example.com", fetchedKayttaja.getSposti());
        assertEquals("123456789", fetchedKayttaja.getPuh());
        assertEquals("Admin", fetchedKayttaja.getRooli());

        //find passaword by email
        String password = kayttajaDAO.findPasswordByEmail(kayttaja.getSposti());
        assertNotNull(password);

        //update email by id
        kayttajaDAO.updateEmailById(kayttajaId, "user.test@example.com");
        Kayttaja updatedKayttajaById = kayttajaDAO.findById(kayttajaId);
        assertEquals("user.test@example.com", updatedKayttajaById.getSposti());

        //Change password by email
        Kayttaja updatedKayttaja = kayttajaDAO.changePasswordByEmail("user.test@example.com", "test");
        assertNotNull(updatedKayttaja);

        String hashedPassword = kayttajaDAO.findPasswordByEmail("user.test@example.com");
        assertTrue(hashedPassword != null && !hashedPassword.equals("password123"));

        //find all kayttaja
        List<Kayttaja> kayttajat = kayttajaDAO.findAllKayttaja();
        assertNotNull(kayttajat);

        //updatekayttaja by id
        kayttajaDAO.updateKayttajaById(kayttajaId,"Test2", "User2", "25412765", "1");
        Kayttaja updatedkayttaja = kayttajaDAO.findById(kayttajaId);
        assertEquals("Test2", updatedkayttaja.getEtunimi());
        assertEquals("User2", updatedkayttaja.getSukunimi());
        assertEquals("25412765", updatedkayttaja.getPuh());
        assertEquals("1", updatedkayttaja.getRooli());

        //onko email olemassa
        boolean email = kayttajaDAO.onkoEmailOlemassa(updatedKayttaja.getSposti());
        assertTrue(email);
        boolean email2 = kayttajaDAO.onkoEmailOlemassa("test.user@example.com");
        assertFalse(email2);

        //find rooli by email
        String rooli = kayttajaDAO.findRooliByEmail(updatedKayttaja.getSposti());
        assertNotNull(rooli);
        assertEquals("1", rooli);

        //remove by id
        kayttajaDAO.removeById(kayttajaId);
        assertNull(kayttajaDAO.findById(kayttajaId));


    }

    @AfterAll
    public void tearDown() {
        MariaDbConnection.getInstance().close();
    }
}
