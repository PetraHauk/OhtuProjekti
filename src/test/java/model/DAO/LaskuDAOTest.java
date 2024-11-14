package model.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.datasourse.MariaDbConnection;
import model.enteties.Lasku;
import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LaskuDAOTest {

    private LaskuDAO laskuDAO;
    private EntityManager em;

    @BeforeAll
    public void setUp() {
        laskuDAO = new LaskuDAO();
        em = MariaDbConnection.getInstance();
    }

    @Test
    public void testPersistLaskuAndFindById() {
        // Create a Lasku object to persist
        Lasku lasku = new Lasku();
        lasku.setMaksuStatus("Maksamatta");
        lasku.setVarausMuoto("All_inslusive");
        lasku.setValuutta("EUR");
        lasku.setAsiakasId(1);

        laskuDAO.persist(lasku);
        int id = lasku.getLaskuId();
        Lasku fetchedLasku = laskuDAO.haeByLaskuId(id);
        assertNotNull(fetchedLasku);

        // Find laskut by  asiakas id
        List<Lasku> laskut = laskuDAO.haeByAsiakasId(1);
        for (Lasku l : laskut) {
            assertEquals(1, l.getAsiakasId());
        }

        // Find kaikki laskut
        List<Lasku> kaikkiLaskut = laskuDAO.haeKaikkilaskut();
        assertFalse(kaikkiLaskut.isEmpty());

        //Update lasku by id
        laskuDAO.updateLaskuById(id, "Maksettu", "Only room", "USD", 1);
        Lasku updatedLasku = laskuDAO.haeByLaskuId(id);
        assertEquals("Maksettu", updatedLasku.getMaksuStatus());
        assertEquals("Only room", updatedLasku.getVarausMuoto());
        assertEquals("USD", updatedLasku.getValuutta());
        assertEquals(1, updatedLasku.getAsiakasId());

        //update status by id
        laskuDAO.updateStatusById(id, "Maksamatta");
        Lasku updatedStatus = laskuDAO.haeByLaskuId(id);
        assertEquals("Maksamatta", updatedStatus.getMaksuStatus());

        //remove by id
        laskuDAO.removeById(id);
        Lasku removedLasku = laskuDAO.haeByLaskuId(id);
        assertNull(removedLasku);
    }
}
