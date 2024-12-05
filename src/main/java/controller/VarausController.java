
package controller;
import model.enteties.Asiakas;
import model.enteties.Varaus;
import model.DAO.VarausDAO;
import model.service.LocaleManager;
import utils.Validator;
import utils.ValidatorExeption;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * VarausController luokka, joka sisältää varaus taulun toiminnot.
 */
public class VarausController {
    private static final Logger logger = LoggerFactory.getLogger(VarausController.class);

    private VarausDAO varausDAO;
    private AsiakasController asiakasController;
    private LaskuController laskuController;
    private Validator validate;

    public VarausController() {
        varausDAO = new VarausDAO();
        asiakasController = new AsiakasController();
        laskuController = new LaskuController();

        validate = new Validator();
    }

    /**
     * Lisää uuden varauksen tietokantaan.
     * @param alkuPvm LocalDate
     * @param loppuPvm LocalDate
     * @param huoneId Integer
     * @param laskuId int
     */
    public void addVaraus(LocalDate alkuPvm, LocalDate loppuPvm, Integer huoneId, int laskuId) {
        Varaus varaus = new Varaus(0,  alkuPvm, loppuPvm, huoneId, laskuId);
        varausDAO.persist(varaus);
    }

    /**
     * Luo uuden varauksen tietokantaan. Jos asiakas ei ole olemassa, lisätään asiakas tietokantaan.
     * Jos asiakas on olemassa, haetaan asiakas tietokannasta.
     * @param asiakasEtunimi
     * @param asiakasSukunimi
     * @param asiakasEmail
     * @param asiakasPuh
     * @param huomio
     * @param laskuMuoto
     * @param saapumisPvm
     * @param lahtoPvm
     */
    public void createVaraus(String asiakasEtunimi, String asiakasSukunimi, String asiakasEmail,
                             String asiakasPuh, String huomio, String laskuMuoto,
                             LocalDate saapumisPvm, LocalDate lahtoPvm) {
        if (!validate.validateEmail(asiakasEmail)) {
            throw new ValidatorExeption("error.invalidemail");
        }

        if (!validate.validatePhoneNumber(asiakasPuh)) {
            throw new ValidatorExeption("error.invalidphone");
        }

        Asiakas asiakas = asiakasController.findByEmail(asiakasEmail);
        if (asiakas == null) {
            asiakasController.addAsiakas(asiakasEtunimi, asiakasSukunimi, asiakasEmail, asiakasPuh, 1, huomio);

        }
        asiakas = asiakasController.findByEmail(asiakasEmail);
        logger.info(asiakas.getEtunimi());

        int laskuId = laskuController.addLasku("maksamaton", laskuMuoto, "EUR", asiakas.getAsiakasId());
        logger.info("Lasku id: {}", laskuId);
        Integer huoneId = null;
        addVaraus(saapumisPvm, lahtoPvm, huoneId, laskuId);
    }

    /**
     * Hae kaikki varaukset tietokannasta.
     * @return Lista varaus olioista.
     */
    public List<Varaus> findVarauksetByDate(LocalDate alkuPvm, LocalDate loppuPvm) {
        List<Varaus> varaukset = varausDAO.haeVaraukset();
        List<Varaus> varauksetByDate = new ArrayList<>();


        for (Varaus varaus : varaukset) {
            if ((varaus.getAlkuPvm().isEqual(alkuPvm) || varaus.getAlkuPvm().isAfter(alkuPvm)) &&
                    (varaus.getLoppuPvm().isEqual(loppuPvm) || varaus.getLoppuPvm().isBefore(loppuPvm))) {
                varauksetByDate.add(varaus);
            }
        }
        return varauksetByDate;
    }

    /**
     * Hae kaikki varaukset tietokannasta, jotka menevät päällekkäin annettujen päivämäärien kanssa.
     * @return Lista varaus olioista.
     */
    public int getOverlappingReservationsCount(LocalDate saapumisPvm, LocalDate lahtoPvm) {
        List<Varaus> varaukset = varausDAO.haeVaraukset();
        int overlappingReservationsCount = 0;

        for (Varaus varaus : varaukset) {
            if ((varaus.getAlkuPvm().isEqual(saapumisPvm) || varaus.getAlkuPvm().isAfter(saapumisPvm)) &&
                    (varaus.getLoppuPvm().isEqual(lahtoPvm) || varaus.getLoppuPvm().isBefore(lahtoPvm))) {
                overlappingReservationsCount++;
            }
        }
        return overlappingReservationsCount;
    }

    /**
     * Hae kaikki varaukset tietokannasta.
     * @param varausId
     * @return
     */
    public Varaus findByVarausId(int varausId) {
        return varausDAO.haeByVarausId(varausId);
    }

    /**
     * Hae kaikki varaukset teitokannasta asiakas id:n perusteella.
     */
    public List<Varaus> findByLaskuId (int laskuId) {
        return varausDAO.haeByLaskuId(laskuId);
    }

    /**
     * Päivitä varaus tietokantaan varaus id:n perusteella.
     * @return Lista varaus olioista.
     */
    public void updateVaraus(String laskuMuoto, LocalDate saapumisPvm, LocalDate lahtoPvm, int varausId) {
        Varaus varaus = findByVarausId(varausId);
        varaus.setAlkuPvm(saapumisPvm);
        varaus.setLoppuPvm(lahtoPvm);
        varausDAO.paivitaVaraus(varaus);
    }

    /**
     * Päivitä varaus huone id:n perusteella.
     */
    public void updateVarausHuoneById(int varausId, int huoneId) {
        varausDAO.paivitaVarausHuoneId(varausId, huoneId);
    }

    /**
     * Poista varaus tietokannasta varaus id:n perusteella.
     */
    public void removeVarausById(int varausId) {
        varausDAO.removeById(varausId);
    }
}
