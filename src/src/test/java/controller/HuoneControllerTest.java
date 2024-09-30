package controller;

import model.enteties.Huone;
import org.junit.jupiter.api.Test;
import controller.HuoneController;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class HuoneControllerTest {
    HuoneController huoneController = new HuoneController();

    @Test
    public void testLisaaHuone() {
        int huoneNro = 110;
        String huoneTyyppi = "perhe";
        String huoneTila = "vapaa";
        double hinta = 100.0;
        int hotelliId = 2;
        huoneController.lisaaHuone(huoneNro, huoneTyyppi, huoneTila, hinta, hotelliId);
        assertEquals(huoneNro, huoneController.findHuoneByNro(huoneNro).getHuone_nro());
    }

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
    public void testFindHuoneByNro() {
        int huoneNro = 101;
        huoneController.findHuoneByNro(huoneNro);
        assertEquals(huoneNro, huoneController.findHuoneByNro(huoneNro).getHuone_nro());
    }

    @Test
    public void testFindHuoneByTila() {
        String huoneTila = "vapaa";
       List<Huone>huoneList = huoneController.findHuoneByTila(huoneTila);
        for (Huone huone : huoneList) {
            assertEquals(huoneTila, huone.getHuone_tila());
        }
    }

    @Test
    public void testFindHuoneByTyyppi() {
        List<Huone> huoneet = huoneController.findHuoneByTyyppi("perhe");
        for (Huone huone : huoneet) {
            assertEquals("perhe", huone.getHuone_tyyppi());
        }

    }

    @Test
    public void testUpdateHuoneById() {
        int huoneId = 4;
        int huoneNro = 303;
        String huoneTyyppi = "sviittihe";
        String huoneTila = "varattu";
        double hinta = 350.0;
        huoneController.updateHuoneById(huoneId, huoneNro, huoneTyyppi, huoneTila, hinta);
        assertEquals(huoneNro, huoneController.findHuoneById(huoneId).getHuone_nro());
    }

    @Test
    public void testDeleteHuone() {
        int huoneId = 8;
        huoneController.deleteHuone(huoneId);
        assertNull(huoneController.findHuoneById(huoneId));
    }

}