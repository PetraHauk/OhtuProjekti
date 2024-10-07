package controller;
import model.DAO.LaskuDAO;
import model.enteties.Huone;
import model.enteties.Lasku;
import model.enteties.Varaus;

import java.util.List;

public class LaskuController {
    private LaskuDAO laskuDAO;

    private VarausController varausController;
    private HuoneController huoneController;

    public LaskuController() {
        laskuDAO = new LaskuDAO();
    }

    public int addLasku(String maksu_status, String varaus_muoto, String valuutta, int asiakas_id) {
        Lasku lasku = new Lasku(0, maksu_status, varaus_muoto, valuutta, asiakas_id);
        laskuDAO.persist(lasku);
        // Return the id of the new lasku
        return lasku.getLaskuId();
    }

    public Lasku findLaskuById(int id) {
        return laskuDAO.haeByLaskuId(id);
    }

    public List<Lasku> findLaskuByAsiakasId(int asiakas_id) {
        return laskuDAO.haeByAsiakasId(asiakas_id);
    }
    public void updateLaskuById(int id, String maksu_status, String varaus_muoto, String valuutta, int asiakas_id) {
        laskuDAO.updateLaskuById(id, maksu_status, varaus_muoto, valuutta, asiakas_id);
    }

    public void updateMaksuStatusById(int id, String maksu_status) {
        laskuDAO.updateStatusById(id, maksu_status);
    }

    public void removeLaskuById(int lasku_id) {
        laskuDAO.removeById(lasku_id);
    }

    public List<Lasku> findAllLaskut() {return laskuDAO.haeKaikkilaskut();
    }

    public double calculateTotalForPaidLaskut() {
        huoneController = new HuoneController(); // Only create it when needed
        varausController = new VarausController(); // Only create it when needed
        List<Lasku> allLaskut = laskuDAO.haeKaikkilaskut(); // Get all invoices
        if (allLaskut == null || allLaskut.isEmpty()) {
            return 0; // Return 0 if there are no invoices
        }
        double totalAmount = 0;

        if (allLaskut == null || allLaskut.isEmpty()) {
            return totalAmount; // Return 0 if there are no invoices
        } else {
            for (Lasku lasku : allLaskut) {
                if (lasku.getMaksuStatus().equalsIgnoreCase("Maksettu")) {
                    List<Varaus> varaukset = varausController.findByLaskuId(lasku.getLaskuId());

                    // if varaukset is empty, continue to the next lasku
                    if (varaukset == null || varaukset.isEmpty()) {
                        continue;
                    }

                    for (Varaus varaus : varaukset) {
                        Huone huone = huoneController.findHuoneById(varaus.getHuoneId());

                        // Calculate the number of days for the reservation
                        long days = java.time.temporal.ChronoUnit.DAYS.between(varaus.getAlkuPvm(), varaus.getLoppuPvm());

                        // Multiply the number of days by the room price and add to total
                        totalAmount += days * huone.getHuone_hinta();
                    }
                }
            }

        }
        return totalAmount; // Return the total amount for paid invoices
    }
}
