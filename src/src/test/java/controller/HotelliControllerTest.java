package controller;

import model.enteties.Hotelli;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HotelliControllerTest {
    HotelliController hotelliController = new HotelliController();

    @Test
    public void testAddHotelli() {
        String nimi = "kolmasHotelli";
        String osoite = "lakjiwo 78";
        String kaupunki = "vantaa";
        String maa = "suomi";
        String puh = "123456";
        int id = 3;
        hotelliController.addHotelli(nimi, osoite, kaupunki, puh, maa);
        Hotelli hoteli = hotelliController.findHotelliById(id);

        boolean hotelliLoytyy = hoteli.getNimi().equals(nimi) &&
                hoteli.getOsoite().equals(osoite) &&
                hoteli.getKaupunki().equals(kaupunki) &&
                hoteli.getPuh().equals(puh) &&
                hoteli.getMaa().equals(maa);
    }

    @Test
    public void testFindHotelliById() {
        Hotelli hotelli = hotelliController.findHotelliById(1);
        assertEquals(1, hotelli.getHotelli_id());
    }

    @Test
    public void testRemoveHotelliById() {
        hotelliController.removeHotelliById(3);
        Hotelli hotelli = hotelliController.findHotelliById(3);
        assertEquals(null, hotelli);
    }
}