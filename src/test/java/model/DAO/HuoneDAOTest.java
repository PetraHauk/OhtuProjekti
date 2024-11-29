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
        huone.setHuoneNro(1001);
        huone.setHuoneTyyppiFi("Yhden hengen huone");
        huone.setHuoneTyyppiEn("Single room");
        huone.setHuoneTyyppiRu("Одноместный номер");
        huone.setHuoneTyyppiZh("单人间");
        huone.setHuone_tila_fi("Vapaa");
        huone.setHuone_tila_en("Free");
        huone.setHuone_tila_ru("Свободно");
        huone.setHuone_tila_zh("空闲");
        huone.setHuone_hinta(100.0);
        huone.setHotelli_id(2);

        huoneDAO.persist(huone);

        Huone fetchedHuone = huoneDAO.findByRoomId(huone.getHuoneId());
        assertNotNull(fetchedHuone);
        assertEquals(1001, fetchedHuone.getHuoneNro());
        assertEquals("Yhden hengen huone", fetchedHuone.getHuoneTyyppiFi());
        assertEquals("Single room", fetchedHuone.getHuoneTyyppiEn());
        assertEquals("Одноместный номер", fetchedHuone.getHuoneTyyppiRu());
        assertEquals("单人间", fetchedHuone.getHuoneTyyppiZh());
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
        huone.setHuoneNro(1003);
        huone.setHuoneTyyppiFi("Perhehuone");
        huone.setHuone_hinta(150.0);
        huone.setHotelli_id(3);
        huoneDAO.persist(huone);

        //Find huone by hotelli_id
        List<Huone> huoneet = huoneDAO.haeHuoneetByHotelliId(3);
        assertNotNull(huoneet);

        // Assert that the list contains the room we just persisted
        boolean huoneFound = huoneet.stream()
                .anyMatch(h -> h.getHuoneNro() == huone.getHuoneNro() &&
                        h.getHuoneTyyppiFi().equals(huone.getHuoneTyyppiFi()) &&
                        h.getHuone_hinta() == huone.getHuone_hinta() &&
                        h.getHotelli_id() == huone.getHotelli_id());
        assertTrue(huoneFound);

        //update huone by huonee_id
        huoneDAO.updateHuoneById(huone.getHuoneId(), 1003, "Sviitti", "Vapaa", 350.0);
        Huone updatedHuone = huoneDAO.findByRoomId(huone.getHuoneId());

        List<String> huoneTypeList = LocaleManager.getLocalizedTyyppiInput("Sviitti");
        List<String> huoneTilaList = LocaleManager.getLocalizedTilaInput("Vapaa");

        assertNotNull(updatedHuone);
        assertEquals(1003, updatedHuone.getHuoneNro());
        assertEquals(huoneTypeList.get(0), updatedHuone.getHuoneTyyppiFi());
        assertEquals(huoneTypeList.get(1), updatedHuone.getHuoneTyyppiEn());
        assertEquals(huoneTypeList.get(2), updatedHuone.getHuoneTyyppiRu());
        assertEquals(huoneTypeList.get(3), updatedHuone.getHuoneTyyppiZh());
        assertEquals(huoneTilaList.get(0), updatedHuone.getHuone_tila_fi());
        assertEquals(huoneTilaList.get(1), updatedHuone.getHuone_tila_en());
        assertEquals(huoneTilaList.get(2), updatedHuone.getHuone_tila_ru());
        assertEquals(huoneTilaList.get(3), updatedHuone.getHuone_tila_zh());

        assertEquals(350.0, updatedHuone.getHuone_hinta(), 0.0);

        //update huone_tila by huone_id
        huoneDAO.updateHuoneTilaById(huone.getHuoneId(), "Varattu");
        Huone updatedHuone2 = huoneDAO.findByRoomId(huone.getHuoneId());
        List<String> huoneTilaList2 = LocaleManager.getLocalizedTilaInput("Varattu");
        assertEquals(huoneTilaList2.get(0), updatedHuone2.getHuone_tila_fi());
        assertEquals(huoneTilaList2.get(1), updatedHuone2.getHuone_tila_en());
        assertEquals(huoneTilaList2.get(2), updatedHuone2.getHuone_tila_ru());
        assertEquals(huoneTilaList2.get(3), updatedHuone2.getHuone_tila_zh());


        //remove by huone id
        // Check that the room exists
        Huone fetchedHuone = huoneDAO.findByRoomId(huone.getHuoneId());
        assertNotNull(fetchedHuone);

        // Remove the room
        huoneDAO.removeById(huone.getHuoneId());

        // Verify it has been removed
        fetchedHuone = huoneDAO.findByRoomId(huone.getHuoneId());
        assertNull(fetchedHuone);
    }

    @AfterAll
    public void tearDown() {
        if (em != null) {
            em.close();
        }
    }
}
