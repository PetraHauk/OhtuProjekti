package controller;

import org.testng.annotations.Test;

import static org.junit.Assert.*;

public class KayttajaControllerTest {

    KayttajaController kayttajaController = new KayttajaController();

    @Test
    public void testFindKayttajaByI() {
        int id = 2;
        assertEquals(2, kayttajaController.haeKayttajaById(2).getKayttajaId());
    }
}