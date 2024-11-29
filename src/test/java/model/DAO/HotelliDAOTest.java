package model.DAO;

import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Hotelli;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HotelliDAOTest {

    private HotelliDAO hotelliDAO;
    private EntityManager em;

    @BeforeAll
    public void setUp() {
        hotelliDAO = new HotelliDAO();
        em = MariaDbConnection.getInstance();
    }

    @Test
    public void testPersistAndFindByIdAndFindByName() {
        Hotelli hotelli = new Hotelli();
        hotelli.setNimi("Testihotelli");
        hotelli.setKaupunki("Testikaupunki");
        hotelli.setOsoite("Testiosoite");
        hotelli.setPuh("123456789");
        hotelli.setMaa("Testimaa");
        hotelliDAO.persist(hotelli);

        //test find hotelli by id
        Hotelli fetchedHotelli = hotelliDAO.findById(hotelli.getHotelliId());
        assertNotNull(fetchedHotelli);
        assertEquals("Testihotelli", fetchedHotelli.getNimi());
        assertEquals("Testikaupunki", fetchedHotelli.getKaupunki());
        assertEquals("Testiosoite", fetchedHotelli.getOsoite());
        assertEquals("123456789", fetchedHotelli.getPuh());
        assertEquals("Testimaa", fetchedHotelli.getMaa());

        //test find hotelli by name
        Hotelli fetchedHotelliByName = hotelliDAO.findByName("Testihotelli");
        assertNotNull(fetchedHotelliByName);
        assertEquals(fetchedHotelli.getNimi(), fetchedHotelliByName.getNimi());
    }

    @Test
    public void testGetRowCount() {
        int rowCount = hotelliDAO.getRowCount();
        int hotelliCount = hotelliDAO.getAllHotellis().size();
        assertEquals(hotelliCount, rowCount);
    }

    @Test
    public void testRemoveById() {
        Hotelli hotelli = new Hotelli();
        hotelli.setNimi("Poistettava Hotelli");
        hotelli.setKaupunki("Poistokaupunki");
        hotelli.setOsoite("Poistosoite");
        hotelli.setPuh("987654321");
        hotelli.setMaa("Poistomaa");
        hotelliDAO.persist(hotelli);

        int id = hotelli.getHotelliId();
        hotelliDAO.removeById(id);
        Hotelli removedHotelli = hotelliDAO.findById(id);
        assertEquals(null, removedHotelli);
    }

    @AfterAll
    public void tearDown() {
        if (em != null) {
            em.close();
        }
    }
}
