package controller;

import model.enteties.Lasku;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class LaskuControllerTest {
    LaskuController laskuController = new LaskuController();
    @Test
    public void testAddLasku() {
        List<Lasku> laskuList1 = laskuController.findAllLaskut();

        // Initialize test data
        String status = "maksamaton";
        String varausMuoto = "no";
        String valuutta = "EUR";
        int asiakasId = 1;

        // Call the method that adds the lasku
        laskuController.addLasku(status, varausMuoto, valuutta, asiakasId);

        // Retrieve the list of all laskut (invoices)
        List<Lasku> laskuList2 = laskuController.findAllLaskut();
        assertEquals(laskuList1.size() + 1, laskuList2.size());
    }

    @Test
    void testFindLaskuById() {
        Lasku lasku = laskuController.findLaskuById(1);
        assertEquals(1, lasku.getLaskuId());
    }

    @Test
    void testFindLaskuByAsiakasId() {
        List<Lasku> laskuList = laskuController.findLaskuByAsiakasId(1);
        assertFalse(laskuList.isEmpty());
        for (Lasku lasku : laskuList) {
            assertEquals(1, lasku.getAsiakasId());
        }
    }

    @Test
    void updateLaskuById() {
        int id = 5;
        String status = "breakfast";
        String varausMuoto = "no";
        String valuutta = "EUR";
        int asiakasId = 1;
        laskuController.updateLaskuById(id, status, varausMuoto, valuutta, asiakasId);
        laskuController.findLaskuById(5);
        assertEquals(status, laskuController.findLaskuById(5).getMaksuStatus());
    }

    @Test
    public void TestRemoveLaskuById() {
        int id = 15;
        laskuController.removeLaskuById(id);
        Lasku lasku = laskuController.findLaskuById(id);
        assertNull(lasku);
    }

    @Test
    void findAllLaskut() {
        List<Lasku> laskuList = laskuController.findAllLaskut();
        assertFalse(laskuList.isEmpty());
        assertEquals(5, laskuList.size());
    }
}