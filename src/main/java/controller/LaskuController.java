package controller;
import model.DAO.LaskuDAO;
import model.enteties.Lasku;

public class LaskuController {
    private LaskuDAO laskuDAO;

    public LaskuController() {
        laskuDAO = new LaskuDAO();
    }

    public void addLasku(String maksu_status, String varaus_muoto, String valuutta) {
        Lasku lasku = new Lasku(0, maksu_status, varaus_muoto, valuutta);
        laskuDAO.persist(lasku);
    }

    public Lasku findLaskuById(int id) {
        return laskuDAO.haeByLaskuId(id);
    }

    public void updateLaskuById(int id, String maksu_status, String varaus_muoto, String valuutta) {
        laskuDAO.updateLaskuById(id, maksu_status, varaus_muoto, valuutta);
    }

    public void removeLaskuById(int lasku_id) {
        laskuDAO.removeById(lasku_id);
    }
}
