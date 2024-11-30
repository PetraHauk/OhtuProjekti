package controller;

import model.enteties.Lasku;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LaskuControllerTest {
    LaskuController laskuController = new LaskuController();

    @Test
    public void testFindLaskuById() {
        Lasku lasku = laskuController.findLaskuById(1);
        Assertions.assertEquals(1, lasku.getLaskuId());
    }

    @Test
    public void testFindLaskuByAsiakasId() {
        List<Lasku> laskuList = laskuController.findLaskuByAsiakasId(1);
        assertFalse(laskuList.isEmpty());
        for (Lasku lasku : laskuList) {
            Assertions.assertEquals(1, lasku.getAsiakasId());
        }
    }

}