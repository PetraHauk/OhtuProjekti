package controller;
import model.enteties.Varaus;
import model.DAO.VarausDAO;

import java.util.Date;

public class VarausController {
    private VarausDAO varausDAO;

    public VarausController() {
        varausDAO = new VarausDAO();
    }

    public void AddVaraus(int huone_maara, Date alku_pvm, Date loppu_pvm, int huone_id, int lasku_id) {
           Varaus varaus = new Varaus(0, huone_maara, alku_pvm, loppu_pvm, huone_id, lasku_id);
            varausDAO.persist(varaus);
        }

    public Varaus findByVarausId(int id) {
        return varausDAO.findByVarausId(id);
    }

    public Varaus findByLaskuId (int lasku_id) {
        return varausDAO.findByLaskuId(lasku_id);
    }

    public void updateVarausDurationById(int id, Date alku_pvm, Date loppu_pvm) {
        varausDAO.updateVarausTilaById(id, alku_pvm, loppu_pvm);
    }

    public void RemoveVaraus(int id) {
        varausDAO.removeById(id);

    }
}
