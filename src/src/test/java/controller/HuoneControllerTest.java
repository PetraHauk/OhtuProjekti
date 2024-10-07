package controller;

import model.enteties.Huone;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HuoneControllerTest {
    HuoneController huoneController = new HuoneController();

    @Test
    public void testFindHuoneById() {
        int huoneId = 1;
        huoneController.findHuoneById(huoneId);
        assertEquals(huoneId, huoneController.findHuoneById(huoneId).getHuone_id());
    }

    @Test
    public void testFindHuoneetByHoteliId() {
        int hotelliId = 1;
        huoneController.findHuoneById(hotelliId);
        assertEquals(hotelliId, huoneController.findHuoneById(hotelliId).getHotelli_id());
    }

    @Test
    public void findHuoneByTila() {
        String huoneTila = "vapaa";
        List<Huone>huoneList = huoneController.findHuoneByTila(huoneTila);
        if (huoneList == null || huoneList.isEmpty()) {
            return;
        } else {
            for (Huone huone : huoneList) {
                assertEquals(huoneTila, huone.getHuone_tila());
            }
        }
    }

    @Test
    public void testFindHuoneByTyyppi() {
        String huoneTyyppi = "perhe";
        List<Huone> huoneet = huoneController.findHuoneByTyyppi(huoneTyyppi);
        for (Huone huone : huoneet) {
            assertEquals(huoneTyyppi, huone.getHuone_tyyppi());
        }
    }

}