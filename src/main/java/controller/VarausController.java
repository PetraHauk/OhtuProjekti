
package controller;
import model.enteties.Asiakas;
import model.enteties.Varaus;
import model.DAO.VarausDAO;
import utils.Validator;
import utils.ValidatorExeption;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VarausController {
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

    public void addVaraus(LocalDate alkuPvm, LocalDate loppuPvm, Integer huoneId, int laskuId) {
        Varaus varaus = new Varaus(0,  alkuPvm, loppuPvm, huoneId, laskuId);
        varausDAO.persist(varaus);
    }

    public void createVaraus(String asiakasEtunimi, String asiakasSukunimi, String asiakasEmail, String asiakasPuh, String huomio, String laskuMuoto, LocalDate saapumisPvm, LocalDate lahtoPvm) {
        if (!validate.validateEmail(asiakasEmail)) {
            throw new ValidatorExeption("Virheellinen sähköposti");
        }

        if (!validate.validatePhoneNumber(asiakasPuh)) {
            throw new ValidatorExeption("Virheellinen puhelinnumero");
        }

        Asiakas asiakas = asiakasController.findByEmail(asiakasEmail);
        if (asiakas == null) {
            asiakasController.addAsiakas(asiakasEtunimi, asiakasSukunimi, asiakasEmail, asiakasPuh, 1, huomio);

        }
        asiakas = asiakasController.findByEmail(asiakasEmail);
        System.out.println(asiakas.getEtunimi());

        int laskuId = laskuController.addLasku("maksamaton", laskuMuoto, "EUR", asiakas.getAsiakasId());
        System.out.println(laskuId);
        Integer huoneId = null;
        addVaraus(saapumisPvm, lahtoPvm, huoneId, laskuId);
    }

    public List<Varaus> findAllVaraukset() {
         return varausDAO.haeVaraukset();
    }

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

    public Varaus findByVarausId(int varausId) {
        return varausDAO.haeByVarausId(varausId);

    }

    public List<Varaus> findByLaskuId (int laskuId) {
        return varausDAO.haeByLaskuId(laskuId);
    }

    public void updateVarausHuoneById(int varausId, int huoneId) {
        varausDAO.paivitaVarausHuoneId(varausId, huoneId);
    }


    public void removeVaraus(int id) {
        varausDAO.removeById(id);

    }

    public void removeVarausById(int varausId) {
        varausDAO.removeById(varausId);
    }

}
