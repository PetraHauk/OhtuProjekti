package controller;

import model.enteties.Asiakas;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AsiakasControllerTest {
    AsiakasController asiakasController = new AsiakasController();


    @Test

    public void testAddAsiakas() {
        // Valmistellaan testidata
        String etunimi = "Seppo";
        String sukunimi = "Tapio";
        String sposti = "seppo@hotelli.com";
        String puh = "09154267";
        int henkiloMaara = 2;
        String huomio = "Ei ole";

        asiakasController.addAsiakas(etunimi, sukunimi, sposti, puh, henkiloMaara, huomio);

        // Haetaan kaikki asiakkaat listasta
        List<Asiakas> asiakasLista = asiakasController.findAllAsiakkaat();

        // Tarkistetaan, että listalla on ainakin yksi asiakas
        assertFalse(asiakasLista.isEmpty());

        // Etsitään lisätty asiakas listasta
        boolean asiakasLoytyy = asiakasLista.stream()
                .anyMatch(a -> a.getSposti().equals(sposti) &&
                        a.getEtunimi().equals(etunimi) &&
                        a.getSukunimi().equals(sukunimi) &&
                        a.getPuh().equals(puh) &&
                        a.getHenkiloMaara() == henkiloMaara &&
                        a.getHuomio().equals(huomio));

        // Varmistetaan, että asiakas löytyy listalta
        assertTrue(asiakasLoytyy);
    }

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
        List<Asiakas> asiakasLista = asiakasController.findIdByNimet("Seppo", "Tapio");
        assertFalse(asiakasLista.isEmpty(), "Asiakaslistan pitäisi sisältää Anna Ann");

        // Varmistetaan, että lista sisältää vähintään yhden asiakkaan nimeltä "Anna Ann"
        boolean asiakasLoytyy = asiakasLista.stream()
                .anyMatch(asiakas -> asiakas.getEtunimi().equals("Seppo") && asiakas.getSukunimi().equals("Tapio"));

        // Varmistetaan, että asiakas todella löytyy
        assertTrue(asiakasLoytyy, "Seppo Tapiopitäisi löytyä listasta");
    }

    @Test
    public void updateAsiakasTiedot() {
        // 1. Alustetaan asiakas
        String etunimi = "Seppo";
        String sukunimi = "Tapio";
        String sposti = "seppo@hotelli.com";
        String puh = "09154267";
        int henkiloMaara = 2;
        String huomio = "Ei ole";

        // Lisätään asiakas
        asiakasController.addAsiakas(etunimi, sukunimi, sposti, puh, henkiloMaara, huomio);

        // 2. Päivitetään asiakkaan tiedot
        List<Asiakas> asiakasLista = asiakasController.findIdByNimet("Seppo", "Tapio");
        Asiakas asiakas = asiakasLista.get(0);  // Oletetaan, että asiakas löytyy listasta

        // Uudet tiedot
        String uusiEtunimi = "Pekka";
        String uusiSukunimi = "Tapio";
        String uusiSposti = "pekka@hotelli.com";
        String uusiPuh = "09151234";
        int uusiHenkiloMaara = 3;
        String uusiHuomio = "On allerginen";

        // Päivitetään asiakas
        asiakasController.paivitaAsiakas(asiakas.getAsiakasId(), uusiEtunimi, uusiSukunimi, uusiSposti, uusiPuh, uusiHenkiloMaara, uusiHuomio);

        // 3. Haetaan päivitetty asiakas ja varmistetaan, että tiedot on päivitetty
        List<Asiakas> paivitettyAsiakasLista = asiakasController.findIdByNimet("Pekka", "Tapio");
        Asiakas paivitettyAsiakas = paivitettyAsiakasLista.get(0);

        // Tarkistetaan, että tiedot ovat oikein
        assertEquals("Pekka", paivitettyAsiakas.getEtunimi());
        assertEquals("Tapio", paivitettyAsiakas.getSukunimi());
        assertEquals("pekka@hotelli.com", paivitettyAsiakas.getSposti());
        assertEquals("09151234", paivitettyAsiakas.getPuh());
        assertEquals(3, paivitettyAsiakas.getHenkiloMaara());
        assertEquals("On allerginen", paivitettyAsiakas.getHuomio());
    }

    @Test
    public void poistaAsiakasById() {
        asiakasController.poistaAsiakas(9);
        assertEquals(null, asiakasController.findByLaskuId(9));
    }

    @Test
    public void testFindAllAsiakkaat() {
        List<Asiakas> asiakasLista = asiakasController.findAllAsiakkaat();
        assertFalse(asiakasLista.isEmpty());
        assertEquals(5, asiakasLista.size());
    }

}