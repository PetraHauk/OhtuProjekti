package model.DAO;

import controller.VarausController;
import model.datasourse.MariaDbConnection;
import model.enteties.Lasku;
import model.enteties.Varaus;
import model.enteties.Huone;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VarausDAOTest {

    private VarausDAO varausDAO;
    private LaskuDAO laskuDAO;
    private HuoneDAO huoneDAO;


    @BeforeAll
    public void setUp() {
        varausDAO = new VarausDAO();
        laskuDAO = new LaskuDAO();
        huoneDAO = new HuoneDAO();
    }

    @BeforeEach
    public void beginTransaction() {
        MariaDbConnection.getInstance().getTransaction().begin();
    }

    @AfterEach
    public void rollbackTransaction() {
        MariaDbConnection.getInstance().getTransaction().rollback();
    }

    @Test
    public void testit() {
        //uusi lasku
        Lasku lasku = new Lasku();
        lasku.setMaksuStatus("Maksamatta");
        lasku.setVarausMuoto("All_inslusive");
        lasku.setValuutta("EUR");
        lasku.setAsiakasId(1);
        laskuDAO.persist(lasku);
        int laskuId = lasku.getLaskuId();

        //uusi huone
        Huone huone = new Huone();
        huone.setHuone_nro(1001);
        huone.setHuone_tyyppi_fi("Yhden hengen huone");
        huone.setHuone_tyyppi_en("Single room");
        huone.setHuone_tyyppi_ru("Одноместный номер");
        huone.setHuone_tyyppi_zh("单人间");
        huone.setHuone_tila_fi("Vapaa");
        huone.setHuone_tila_en("Free");
        huone.setHuone_tila_ru("Свободно");
        huone.setHuone_tila_zh("空闲");
        huone.setHuone_hinta(100.0);
        huone.setHotelli_id(2);
        huoneDAO.persist(huone);
        int huoneId = huone.getHuone_id();

        // Step 1: Create and persist a Varaus
        Varaus varaus = new Varaus();
        varaus.setAlkuPvm(LocalDate.of(2025, 1, 1));
        varaus.setLoppuPvm(LocalDate.of(2025, 1, 10));
        varaus.setHuoneId(huoneId); // Make sure this matches an existing `Huone` if it's a foreign key
        varaus.setLaskuId(laskuId);// Make sure this matches an existing `Lasku` if it's a foreign key
        varausDAO.persist(varaus);

        // Step 2: Fetch the persisted Varaus by ID
        int varausId = varaus.getVarausId();
        Varaus fetchedVaraus = varausDAO.haeByVarausId(varausId);
        assertNotNull(fetchedVaraus);
        assertEquals(LocalDate.of(2025, 1, 1), fetchedVaraus.getAlkuPvm());
        assertEquals(LocalDate.of(2025, 1, 10), fetchedVaraus.getLoppuPvm());
        assertEquals(huoneId, fetchedVaraus.getHuoneId());
        assertEquals(laskuId, fetchedVaraus.getLaskuId());

        //Hae varaukset
        List<Varaus> varaukset = varausDAO.haeVaraukset();
        assertNotNull(varaukset);

        //hae varaus laskun id:llä
        List<Varaus> varauksetByLaskuId = varausDAO.haeByLaskuId(laskuId);
        assertNotNull(varauksetByLaskuId);
        for (Varaus varaus1 : varauksetByLaskuId) {
            assertEquals(laskuId, varaus1.getLaskuId());
        }

        //Update varaus huone id:llä
        varausDAO.paivitaVarausHuoneId(varausId, 2);
        Varaus updatedVarausHuoneId = varausDAO.haeByVarausId(varausId);
        assertEquals(2, updatedVarausHuoneId.getHuoneId());

        // Step 3: Remove the persisted Varaus
        varausDAO.removeById(varausId);
        Varaus removedVaraus = varausDAO.haeByVarausId(varausId);
        assertNull(removedVaraus);

    }

}