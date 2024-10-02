//package controller;
//
//import model.enteties.Kayttaja;
//import org.junit.Test;
//import org.mindrot.jbcrypt.BCrypt;
//
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
////import static org.testng.AssertJUnit.assertEquals;
//
//public class kayttajaControllerTest {
//    KayttajaController kayttajaController = new KayttajaController();
//
//    @Test
//    public void testFindByKayttajaId() {
//        int id = 1;
//        Kayttaja kayttaja = kayttajaController.haeKayttajaById(id);
//        assertEquals(1, kayttaja.getKayttajaId());
//    }
//
//    @Test
//    public void testFindPasswordByEmail() {
//        String sposti = "anna@hotelli.com";
//        String salasana = "$2a$10$k99FpQXW3twxaNZ56VwZOO9VLMrYt.Jkg7c90isTwpLbMPDjSVs1G";
//        assertEquals(salasana, kayttajaController.haeSalasanaSpostilla(sposti));
//    }

//    @Test
//    public void testFindAllKayttajat() {
//        List<Kayttaja> kayttajaLista = kayttajaController.haeKaikkiKayttajat();
//        assertEquals(3, kayttajaLista.size());
//    }

//    @Test
//    public void TestPoistaKayttaja() {
//        int id = 5;
//        kayttajaController.poistaKayttaja(id);
//        Kayttaja kayttaja = kayttajaController.haeKayttajaById(id);
//        assertEquals(null, kayttaja);
//
//    }
//}


