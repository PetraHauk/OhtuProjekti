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

    public int addLasku(String maksuStatus, String varausMuoto, String valuutta, int asiakasId) {
        Lasku lasku = new Lasku(0, maksuStatus, varausMuoto, valuutta, asiakasId);
        laskuDAO.persist(lasku);
        // Return the id of the new lasku
        return lasku.getLaskuId();
    }

    public Lasku findLaskuById(int id) {
        return laskuDAO.haeByLaskuId(id);
    }

    public List<Lasku> findLaskuByAsiakasId(int asiakasId) {
        return laskuDAO.haeByAsiakasId(asiakasId);
    }

    public void updateLaskuById(int id, String maksuStatus, String varausMuoto, String valuutta, int asiakasId) {
        laskuDAO.updateLaskuById(id, maksuStatus, varausMuoto, valuutta, asiakasId);
    }

    public void updateMaksuStatusById(int id, String maksuStatus) {
        laskuDAO.updateStatusById(id, maksuStatus);
    }

    public void removeLaskuById(int laskuId) {
        laskuDAO.removeById(laskuId);
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
                        totalAmount += days * huone.getHuoneHinta();
                    }
                }
            }

        }
        return totalAmount; // Return the total amount for paid invoices
    }
}
