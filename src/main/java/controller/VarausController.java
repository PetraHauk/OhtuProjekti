
package controller;
import model.enteties.Varaus;
import model.DAO.VarausDAO;
import java.time.LocalDate;
import java.util.List;

public class VarausController {
    private VarausDAO varausDAO;

    public VarausController() {
        varausDAO = new VarausDAO();
    }

    public void AddVaraus(LocalDate alkuPvm, LocalDate loppuPvm, int huone_id, int lasku_id) {
        Varaus varaus = new Varaus(0,  alkuPvm, loppuPvm, huone_id, lasku_id);
        varausDAO.persist(varaus);
    }

    public List<Varaus> findAllVaraukset() {
        List<Varaus> varaukset = varausDAO.haeVaraukset();
        return varaukset;
    }

    public Varaus findByVarausId(int varaus_id) {
        return varausDAO.haeByVarausId(varaus_id);
    }

    public List<Varaus> findByLaskuId (int lasku_id) {
        return varausDAO.haeByLaskuId(lasku_id);
    }

    public void updateVarausById(int varaus_id, LocalDate alkuPvm, LocalDate loppuPvm) {
        varausDAO.paivitaVarausById(varaus_id, alkuPvm, loppuPvm);
    }
    public void RemoveVaraus(int id) {
        varausDAO.removeById(id);

    }

    public int varausPaivat (LocalDate alkuPvm, LocalDate loppuPvm) {
        return varausDAO.varausPaivat(alkuPvm, loppuPvm);
    }
}
