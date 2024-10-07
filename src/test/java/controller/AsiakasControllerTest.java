package controller;

import model.enteties.Asiakas;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class AsiakasControllerTest {
    AsiakasController asiakasController = new AsiakasController();

    @Test
    public void findByLaskuId() {
        Asiakas asiakas = asiakasController.findByLaskuId(1);
        assertEquals(1, asiakas.getAsiakasId());
    }

    @Test
    public void findByEmail() {
        Asiakas asiakas = asiakasController.findByEmail("anna@sposti.fi");
        assertEquals("anna@sposti.fi", asiakas.getSposti());
    }

    @Test
    public void findByNimet() {
        List<Asiakas> asiakasLista = asiakasController.findByNimet("Mia", "Makki");
        assertFalse(asiakasLista.isEmpty());
        for (Asiakas asiakas : asiakasLista) {
            assertEquals("Mia", asiakas.getEtunimi());
            assertEquals("Makki", asiakas.getSukunimi());
        }
    }

}