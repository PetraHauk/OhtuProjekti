package controller;
import model.DAO.LaskuDAO;
import model.enteties.Lasku;

public class LaskuController {
    private LaskuDAO laskuDAO;

    public LaskuController() {
        laskuDAO = new LaskuDAO();
    }

    public void addLasku(String maksu_status, String varaus_muoto, String valuutta,int asiakas_id) {
        Lasku lasku = new Lasku( 0, maksu_status, varaus_muoto, valuutta, asiakas_id);
        laskuDAO.persist(lasku);
    }

    public Lasku findLaskuById(int id) {
        return laskuDAO.haeByLaskuId(id);
    }

    public Lasku findLaskuByAsiakasId(int asiakas_id) {
        return laskuDAO.haeByAsiakasId(asiakas_id);
    }
    public void updateLaskuById(int id, String maksu_status, String varaus_muoto, String valuutta, int asiakas_id) {
        laskuDAO.updateLaskuById(id, maksu_status, varaus_muoto, valuutta, asiakas_id);
    }

    public void removeLaskuById(int lasku_id) {
        laskuDAO.removeById(lasku_id);
    }
}
