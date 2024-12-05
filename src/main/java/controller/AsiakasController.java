package controller;

import model.DAO.AsiakasDAO;
import model.enteties.Asiakas;
import model.enteties.Varaus;
import utils.Validator;
import utils.ValidatorExeption;

import java.util.List;

public class AsiakasController {
    private AsiakasDAO asiakasDAO;

    private Validator validate;
    public AsiakasController() {
        asiakasDAO = new AsiakasDAO();
        validate = new Validator();
    }

    public void addAsiakas(String etunimi, String sukunimi, String sposti, String puh, int hMaara, String huomio) {
        if (etunimi == null || etunimi.isEmpty() || sukunimi == null || sukunimi.isEmpty() || sposti == null || sposti.isEmpty() || puh == null || puh.isEmpty()) {
            throw new ValidatorExeption("error.requiredfields");
        }

        if (!validate.validateEmail(sposti)) {
            throw new ValidatorExeption("error.invalidemail");
        }

        if (!validate.validatePhoneNumber(puh)) {
            throw new ValidatorExeption("error.invalidphone");
        }

        Asiakas asiakas = new Asiakas(0, etunimi, sukunimi, sposti, puh, hMaara, huomio);// asiakasId is set to 0, as it should be auto-generated by the DB
        asiakasDAO.persist(asiakas);
    }
    public Asiakas findByLaskuId(int id) {
        return asiakasDAO.findByLaskuId(id);
    }

    public Asiakas findByEmail(String email) {return asiakasDAO.findByEmail(email);}

    public List<Asiakas> findByNimet(String etunimi, String sukunimi) {
        return asiakasDAO.findByNImet(etunimi, sukunimi);
    }

    public List<Asiakas> findByKeyword(String keyword) {
        return asiakasDAO.findAsiakasByKeyword(keyword);
    }
    public List<Asiakas> findAllAsiakkaat() {
        return asiakasDAO.findAsiakkaat();
    }
    public void paivitaAsiakas(int id, String etunimi, String sukunimi, String sposti, String puh, int henkiloMaara, String huomio) {
        if (etunimi == null || etunimi.isEmpty() || sukunimi == null || sukunimi.isEmpty() || sposti == null || sposti.isEmpty() || puh == null || puh.isEmpty()) {
            throw new ValidatorExeption("error.requiredfields");
        }

        if (!validate.validateEmail(sposti)) {
            throw new ValidatorExeption("error.invalidemail");
        }

        if (!validate.validatePhoneNumber(puh)) {
            throw new ValidatorExeption("error.invalidphone");
        }

        asiakasDAO.updateAsiakasById(id, etunimi, sukunimi, sposti, puh, henkiloMaara, huomio);
    }

    public void poistaAsiakas(int id) {
        asiakasDAO.removeById(id);
    }
}
