package model.DAO;

import static org.junit.jupiter.api.Assertions.*;
import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Huone;
import model.service.LocaleManager;
import org.junit.jupiter.api.*;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HuoneDAOTest {

    private HuoneDAO huoneDAO;
    private EntityManager em;

    @BeforeAll
    public void setUp() {
        huoneDAO = new HuoneDAO();
        em = MariaDbConnection.getInstance();
    }

    @Test
    public void testPersistAndFindByRoomId() {
        Huone huone = new Huone();
        // Assuming hotel ID 1 exists
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

        Huone fetchedHuone = huoneDAO.findByRoomId(huone.getHuone_id());
        assertNotNull(fetchedHuone);
        assertEquals(1001, fetchedHuone.getHuone_nro());
        assertEquals("Yhden hengen huone", fetchedHuone.getHuone_tyyppi_fi());
        assertEquals("Single room", fetchedHuone.getHuone_tyyppi_en());
        assertEquals("Одноместный номер", fetchedHuone.getHuone_tyyppi_ru());
        assertEquals("单人间", fetchedHuone.getHuone_tyyppi_zh());
        assertEquals("Vapaa", fetchedHuone.getHuone_tila_fi());
        assertEquals("Free", fetchedHuone.getHuone_tila_en());
        assertEquals("Свободно", fetchedHuone.getHuone_tila_ru());
        assertEquals("空闲", fetchedHuone.getHuone_tila_zh());
        assertEquals(100.0, fetchedHuone.getHuone_hinta());
        assertEquals(2, fetchedHuone.getHotelli_id());
    }

    @Test
    public void testHaeByHotelliIdAndUpdateAndRemove() {
        // Create a new Huone object and set its properties
        Huone huone = new Huone();
        huone.setHuone_nro(1003);
        huone.setHuone_tyyppi_fi("Perhehuone");
        huone.setHuone_hinta(150.0);
        huone.setHotelli_id(3);
        huoneDAO.persist(huone);

        //Find huone by hotelli_id
        List<Huone> huoneet = huoneDAO.haeHuoneetByHotelliId(3);
        assertNotNull(huoneet);

        // Assert that the list contains the room we just persisted
        boolean huoneFound = huoneet.stream()
                .anyMatch(h -> h.getHuone_nro() == huone.getHuone_nro() &&
                        h.getHuone_tyyppi_fi().equals(huone.getHuone_tyyppi_fi()) &&
                        h.getHuone_hinta() == huone.getHuone_hinta() &&
                        h.getHotelli_id() == huone.getHotelli_id());
        assertTrue(huoneFound);

        //update huone by huonee_id
        huoneDAO.updateHuoneById(huone.getHuone_id(), 1003, "Sviitti", "Vapaa", 350.0);
        Huone updatedHuone = huoneDAO.findByRoomId(huone.getHuone_id());

        List<String> huoneTypeList = LocaleManager.getLocalizedTyyppiInput("Sviitti");
        List<String> huoneTilaList = LocaleManager.getLocalizedTilaInput("Vapaa");

        assertNotNull(updatedHuone);
        assertEquals(1003, updatedHuone.getHuone_nro());
        assertEquals(huoneTypeList.get(0), updatedHuone.getHuone_tyyppi_fi());
        assertEquals(huoneTypeList.get(1), updatedHuone.getHuone_tyyppi_en());
        assertEquals(huoneTypeList.get(2), updatedHuone.getHuone_tyyppi_ru());
        assertEquals(huoneTypeList.get(3), updatedHuone.getHuone_tyyppi_zh());
        assertEquals(huoneTilaList.get(0), updatedHuone.getHuone_tila_fi());
        assertEquals(huoneTilaList.get(1), updatedHuone.getHuone_tila_en());
        assertEquals(huoneTilaList.get(2), updatedHuone.getHuone_tila_ru());
        assertEquals(huoneTilaList.get(3), updatedHuone.getHuone_tila_zh());

        assertEquals(350.0, updatedHuone.getHuone_hinta(), 0.0);

        //update huone_tila by huone_id
        huoneDAO.updateHuoneTilaById(huone.getHuone_id(), "Varattu");
        Huone updatedHuone2 = huoneDAO.findByRoomId(huone.getHuone_id());
        List<String> huoneTilaList2 = LocaleManager.getLocalizedTilaInput("Varattu");
        assertEquals(huoneTilaList2.get(0), updatedHuone2.getHuone_tila_fi());
        assertEquals(huoneTilaList2.get(1), updatedHuone2.getHuone_tila_en());
        assertEquals(huoneTilaList2.get(2), updatedHuone2.getHuone_tila_ru());
        assertEquals(huoneTilaList2.get(3), updatedHuone2.getHuone_tila_zh());


        //remove by huone id
        // Check that the room exists
        Huone fetchedHuone = huoneDAO.findByRoomId(huone.getHuone_id());
        assertNotNull(fetchedHuone);

        // Remove the room
        huoneDAO.removeById(huone.getHuone_id());

        // Verify it has been removed
        fetchedHuone = huoneDAO.findByRoomId(huone.getHuone_id());
        assertNull(fetchedHuone);
    }

    @AfterAll
    public void tearDown() {
        if (em != null) {
            em.close();
        }
    }
}
