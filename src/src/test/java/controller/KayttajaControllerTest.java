package controller;


import model.enteties.Kayttaja;
import org.junit.Assert;
import org.junit.Test;

public class KayttajaControllerTest {
    KayttajaController kayttajaController = new KayttajaController();

    @Test
    public void testFindKayttajaById() {
        Kayttaja kayttaja = kayttajaController.haeKayttajaById(1);
        Assert.assertEquals(1, kayttaja.getKayttajaId());
    }


}