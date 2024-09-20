package controller;
import model.enteties.Varaus;
import model.DAO.VarausDAO;
import java.time.LocalDate;

public class VarausController3 {
    private VarausDAO varausDAO;

    public VarausController3() {
        varausDAO = new VarausDAO();
    }

    public void AddVaraus(int huone_maara, LocalDate alkuPvm, LocalDate loppuPvm, int huone_id, int lasku_id) {
           Varaus varaus = new Varaus(0,huone_maara, alkuPvm, loppuPvm, huone_id, lasku_id);
            varausDAO.persist(varaus);
        }

    public Varaus findAllVaraukset() {
        return varausDAO.haeVaraukset();
    }

    public void findByVarausId(int varaus_id) {
        varausDAO.haeByVarausId(varaus_id);
    }

    public void findByLaskuId (int lasku_id) {
        varausDAO.haeByLaskuId(lasku_id);
    }

    public void updateVarausById(int varaus_id, int huone_maara, LocalDate alkuPvm, LocalDate loppuPvm) {
        varausDAO.paivitaVarausById(varaus_id, huone_maara, alkuPvm, loppuPvm);
    }

    public void RemoveVaraus(int id) {
        varausDAO.removeById(id);

    }
}
