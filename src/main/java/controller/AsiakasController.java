package controller;

import model.DAO.AsiakasDAO;
import model.enteties.Asiakas;

public class AsiakasController {
    private AsiakasDAO asiakasDAO;

    public AsiakasController() {
        asiakasDAO = new AsiakasDAO();
    }

    public void addAsiakas(String etunimi, String sukunimi, String sposti, String puh, int Hmaara, String huomio) {
        Asiakas asiakas = new Asiakas(0, etunimi, sukunimi, sposti, puh, Hmaara, huomio);// asiakasId is set to 0, as it should be auto-generated by the DB
        asiakasDAO.persist(asiakas);
    }
    public Asiakas findByLaskuId(int id) {
        return asiakasDAO.findByLaskuId(id);
    }

    public Asiakas findByEmail(String email) {
        return asiakasDAO.findByEmail(email);
    }

    public Asiakas findByNimet(String etunimi, String sukunimi) {
        return asiakasDAO.findByNImet(etunimi, sukunimi);
    }

    public Asiakas findAllAsiakkaat() {
        return asiakasDAO.findAsukkaat();
    }

    public void paivitaAsiakas(int id, String etunimi, String sukunimi, String sposti, String puh, int henkiloMaara, String huomio) {
        asiakasDAO.updateAsiakasById(id, etunimi, sukunimi, sposti, puh, henkiloMaara, huomio);
    }
}
