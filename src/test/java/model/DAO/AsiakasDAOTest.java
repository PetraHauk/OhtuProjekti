package model.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.datasourse.MariaDbConnection;
import model.enteties.Asiakas;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AsiakasDAOTest {

    private AsiakasDAO asiakasDAO;
    private EntityManager em;

    @BeforeAll
    public void setUp() {
        asiakasDAO = new AsiakasDAO();
        em = MariaDbConnection.getInstance();
    }

    @Test
    public void testPersist() {
        Asiakas asiakas = asiakasDAO.findByEmail("anna.mallinen@example.com");
        if (asiakas.isEmpty()) {
            asiakas.setEtunimi("Anna");
            asiakas.setSukunimi("Mallinen");
            asiakas.setSposti("anna.mallinen@example.com");
            asiakas.setPuh("987654321");
            asiakas.setHenkiloMaara(2);
            asiakas.setHuomio("Unitesti");

            asiakasDAO.persist(asiakas);
        }
        assertNotNull(asiakas);
        assertEquals("anna.mallinen@example.com", asiakas.getSposti());
    }

    @Test
    public void testPersistAndFindById() {
        Asiakas asiakas = asiakasDAO.findByEmail("anna.mallinen@example.com");
        Asiakas fetchedAsiakas = asiakasDAO.findByLaskuId(asiakas.getAsiakasId());
        assertNotNull(fetchedAsiakas);
        assertEquals("anna.mallinen@example.com", fetchedAsiakas.getSposti());
    }

    @Test
    public void testFindByEmail() {
        Asiakas fetchedAsiakas = asiakasDAO.findByEmail("anna.mallinen@example.com");
        assertNotNull(fetchedAsiakas);
        assertEquals("Anna", fetchedAsiakas.getEtunimi());
    }

    @Test
    public void testFindByNimet() {

        List<Asiakas> asiakkaat = asiakasDAO.findByNImet("Anna", "Mallinen");
        for (Asiakas asiakas : asiakkaat) {
            assertEquals("Anna", asiakas.getEtunimi());
            assertEquals("Mallinen", asiakas.getSukunimi());
        }
    }

    @Test
    public void testUpdateAsiakasById() {
        Asiakas asiakas = new Asiakas();
        asiakas.setEtunimi("Pekka");
        asiakas.setSukunimi("Puupää");
        asiakas.setSposti("pekka.puupaa@example.com");
        asiakas.setPuh("123456789");
        asiakas.setHenkiloMaara(2);
        asiakas.setHuomio("Testi");
        asiakasDAO.persist(asiakas);

        asiakasDAO.updateAsiakasById(asiakas.getAsiakasId(), "Pekka", "UusiSukunimi", "pekka.uusi@example.com", "999999999", 5, "Päivitetty huomio");

        Asiakas updatedAsiakas = asiakasDAO.findByLaskuId(asiakas.getAsiakasId());
        assertNotNull(updatedAsiakas);
        assertEquals("UusiSukunimi", updatedAsiakas.getSukunimi());
    }

    @Test
    public void testFindAsiakasByKeyword() {
        Asiakas asiakas1 = new Asiakas();
        asiakas1.setEtunimi("Eero");
        asiakas1.setSukunimi("Esimerkki");
        asiakas1.setSposti("eero.esimerkki@example.com");
        asiakasDAO.persist(asiakas1);

        Asiakas asiakas2 = new Asiakas();
        asiakas2.setEtunimi("Kalle");
        asiakas2.setSukunimi("Kuorma");
        asiakas2.setSposti("kalle.kuorma@example.com");
        asiakasDAO.persist(asiakas2);

        List<Asiakas> asiakkaat = asiakasDAO.findAsiakasByKeyword("Eero");
        assertFalse(asiakkaat.isEmpty());
        assertEquals("Eero", asiakkaat.get(0).getEtunimi());
    }

    @Test
    public void testFindAsiakkaat() {
        List<Asiakas> asiakkaat = asiakasDAO.findAsiakkaat();
        assertFalse(asiakkaat.isEmpty());
    }

    @Test
    public void TestrRemoveAsiakasById() {
        Asiakas asiakas = new Asiakas();
        asiakas.setEtunimi("Pekka");
        asiakas.setSukunimi("Puupää");
        asiakas.setSposti("pekka.puupaa@example.com");
        asiakas.setPuh("123456789");
        asiakas.setHenkiloMaara(2);
        asiakas.setHuomio("Testi");
        asiakasDAO.persist(asiakas);

        int asiakasId = asiakasDAO.findByEmail("pekka.puupaa@example.com").getAsiakasId();
        asiakasDAO.removeById(asiakasId);
        Asiakas removedAsiakas = asiakasDAO.findByLaskuId(asiakasId);
        assertNull(removedAsiakas);
    }

//    @Test
//    public void removeId () {
//        asiakasDAO.removeById(39);
//    }

    @AfterAll
    public void tearDown() {
        if (em != null) {
            em.close();
        }
    }
}
