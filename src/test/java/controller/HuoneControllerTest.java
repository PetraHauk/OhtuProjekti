package controller;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HuoneControllerTest {
    HuoneController huoneController = new HuoneController();

    @Test
    public void testFindHuoneById() {
        int huoneId = 1;
        huoneController.findHuoneById(huoneId);
        assertEquals(huoneId, huoneController.findHuoneById(huoneId).getHuoneId());
    }

    @Test
    public void testFindHuoneetByHoteliId() {
        int hotelliId = 1;
        huoneController.findHuoneById(hotelliId);
        assertEquals(hotelliId, huoneController.findHuoneById(hotelliId).getHotelliId());
    }
}