
package controller;
import model.enteties.Asiakas;
import model.enteties.Varaus;
import model.DAO.VarausDAO;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VarausController {
    private VarausDAO varausDAO;
    private AsiakasController asiakasController;
    private LaskuController laskuController;

    private HuoneController huoneController;

    public VarausController() {
        varausDAO = new VarausDAO();
        asiakasController = new AsiakasController();
        laskuController = new LaskuController();
        huoneController = new HuoneController();
    }

    public void AddVaraus(LocalDate alkuPvm, LocalDate loppuPvm, Integer huone_id, int lasku_id) {
        Varaus varaus = new Varaus(0,  alkuPvm, loppuPvm, huone_id, lasku_id);
        varausDAO.persist(varaus);
    }

    public void createVaraus(String asiakasEtunimi, String asiakasSukunimi, String asiakasEmail, String asiakasPuh, String huomio, String laskuMuoto, LocalDate saapumisPvm, LocalDate lahtoPvm) {
        Asiakas asiakas = asiakasController.findByEmail(asiakasEmail);
        if (asiakas == null) {
            asiakasController.addAsiakas(asiakasEtunimi, asiakasSukunimi, asiakasEmail, asiakasPuh, 1, huomio);
            System.out.println("Asiakas luotu");
        }
        asiakas = asiakasController.findByEmail(asiakasEmail);
        System.out.println(asiakas.getEtunimi());

        int lasku_id = laskuController.addLasku("maksamaton", laskuMuoto, "EUR", asiakas.getAsiakasId());
        System.out.println(lasku_id);
        Integer huone_id = null;
        AddVaraus(saapumisPvm, lahtoPvm, huone_id, lasku_id);
    }

    public List<Varaus> findAllVaraukset() {
        List<Varaus> varaukset = varausDAO.haeVaraukset();
        return varaukset;
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

    public Varaus findByVarausId(int varaus_id) {
        return varausDAO.haeByVarausId(varaus_id);

    }

    public List<Varaus> findByLaskuId (int lasku_id) {
        return varausDAO.haeByLaskuId(lasku_id);
    }

    public void updateVarausHuoneById(int varausId, int huoneId) {
        varausDAO.paivitaVarausHuoneId(varausId, huoneId);
    }


    public void RemoveVaraus(int id) {
        varausDAO.removeById(id);

    }

    public void removeVarausById(int varaus_id) {
        varausDAO.removeById(varaus_id);
    }


}
