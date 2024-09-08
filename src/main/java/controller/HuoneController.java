package controller;

import app.MockData;
import model.enteties.Huone;

public class HuoneController {

    public void lisaaHuone(int huoneNro, double hinta, String tyyppi) {
        Huone huone = new Huone(huoneNro, hinta, tyyppi);

        MockData.addHuone(huone); // Lisätään huone tietokantaan
    }

    public void poistaHuone(Huone huone) {
        if (huone == null) {
            System.out.println("Invalid room data");
            return;
        }

        MockData.removeHuone(huone); // Poistetaan huone tietokannasta
    }

    public void muokkaaHuonetta(Huone huone, int huoneNro, double hinta, String tyyppi) {
        if (huone == null) {
            System.out.println("Invalid room data");
            return;
        }
        huone.setHuoneNro(huoneNro);
        huone.setHinta(hinta);
        huone.setHuoneenTyyppi(tyyppi);
    }

    public void muutaHuoneenTilaa(Huone huone, String tila) {
        if (huone == null) {
            System.out.println("Invalid room data");
            return;
        }
        huone.setTila(tila);
    }
}
