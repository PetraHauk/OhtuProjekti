package controller;

import model.enteties.Varaus;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class VarausControllerTest {
    VarausController varausController = new VarausController();

    @Test
    public void testAddVaraus() {
        // Test data initialization
        LocalDate alkuPvm = LocalDate.of(2021, 12, 1);
        LocalDate loppuPvm = LocalDate.of(2021, 12, 2);
        int huoneId = 1;
        int laskuId = 1;

        varausController.AddVaraus(alkuPvm, loppuPvm, huoneId, laskuId);
        List<Varaus> varausLista = varausController.findAllVaraukset();

        assertFalse(varausLista.isEmpty());
        boolean varausLoytyy = varausLista.stream()
                .anyMatch(v ->
                        v.getAlkuPvm().equals(alkuPvm) &&
                        v.getLoppuPvm().equals(loppuPvm) &&
                        v.getHuoneId() == huoneId &&
                        v.getLaskuId() == laskuId);
        assertTrue(varausLoytyy);
    }

    @Test
    public void TestFindByVarausId() {
        int id = 2;
        Varaus varaus = varausController.findByVarausId(id);
        assertEquals(id, varaus.getVarausId());
    }

    @Test
    public void testFindByLaskuId() {
        int id = 1;
        List<Varaus> varausLista = varausController.findByLaskuId(id);
        assertFalse(varausLista.isEmpty());
        for (Varaus varaus : varausLista) {
            assertEquals(id, varaus.getLaskuId());
        }
    }

    @Test
    public void testUpdateVarausById() {

        int id = 2;
        LocalDate alkuPvm = LocalDate.of(2024, 12, 1);
        LocalDate loppuPvm = LocalDate.of(2024, 12, 4);

        Varaus varaus = varausController.findByVarausId(2);
        if (varaus != null) {
            varaus.setAlkuPvm(alkuPvm);  // Ensure this line is correctly updating the start date
            varaus.setLoppuPvm(loppuPvm);  // Ensure this line is updating the end date
        }
        assertEquals(alkuPvm, varaus.getAlkuPvm());
        assertEquals(loppuPvm, varaus.getLoppuPvm());


    }

    @Test
    public void testRemoveVaraus() {
    }

    @Test
    public void testFindAllVaraukset() {
    }

}