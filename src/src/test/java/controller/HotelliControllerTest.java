package controller;

import model.enteties.Hotelli;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class HotelliControllerTest {
    HotelliController hotelliController = new HotelliController();

    @Test
    public void testAddHotelli() {
        String nimi = "kolmasHotelli";
        String osoite = "lakjiwo 78";
        String kaupunki = "vantaa";
        String maa = "suomi";
        String puh = "123456";

        hotelliController.addHotelli(nimi, osoite, kaupunki, puh, maa);
        // Ensure that the hotel was actually added
        assertEquals(nimi, hotelliController.findHotelliById(15).getNimi());
    }

    @Test
    public void testFindHotelliById() {
        Hotelli hotelli = hotelliController.findHotelliById(1);
        assertEquals(1, hotelli.getHotelli_id());
    }

    @Test
    public void testRemoveHotelliById() {
        hotelliController.removeHotelliById(14);
        Hotelli hotelli = hotelliController.findHotelliById(14);
        assertEquals(null, hotelli);
    }
}