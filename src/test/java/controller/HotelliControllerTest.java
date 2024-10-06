package controller;

import model.enteties.Hotelli;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HotelliControllerTest {
    HotelliController hotelliController = new HotelliController();

    @Test
    public void testFindHotelliById() {
        Hotelli hotelli = hotelliController.findHotelliById(1);
        assertEquals(1, hotelli.getHotelli_id());
    }
}