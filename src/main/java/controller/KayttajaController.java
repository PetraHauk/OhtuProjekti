package controller;

import model.DAO.KayttajaDAO;
import model.enteties.Kayttaja;

public class KayttajaController {
    private KayttajaDAO kayttajaDAO;

    public KayttajaController() {
        kayttajaDAO = new KayttajaDAO();
    }

    public Kayttaja haeKayttajaById(int id) {
        return kayttajaDAO.findById(id);
    }

}