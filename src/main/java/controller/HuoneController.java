package controller;

import model.DAO.HuoneDAO;
import model.enteties.Huone;
import model.enteties.Hotelli;
import model.DAO.HotelliDAO;

public class HuoneController {
   private HuoneDAO huoneDAO;
   private HotelliDAO hotelliDAO;

   public HuoneController() {
       huoneDAO = new HuoneDAO();
         hotelliDAO = new HotelliDAO();

   }

    public void lisaaHuone(int huone_nro, String huone_tyyppi, String huone_tila, double huone_hinta, int hotelli_id) {

        Hotelli hotelli = hotelliDAO.findById(hotelli_id);
        if (hotelli != null) {
            Huone huone = new Huone(0, huone_nro, huone_tyyppi, huone_tila, huone_hinta, hotelli_id);
            huoneDAO.persist(huone);
        } else {
            throw new IllegalArgumentException("Hotelli ID " + hotelli_id + " ei löytyy");
        }
    }


    public Huone findHuoneById(int id) {
         return huoneDAO.findById(id);
    }

    public Huone findHuoneByTila(String tila) {
        return huoneDAO.findByHuoneTila(Integer.parseInt(tila));
    }

    public Huone findHuoneByTyyppi(String huone_tyyppi) {
        return huoneDAO.FindByTyyppi(huone_tyyppi);
    }

    public void updateHuoneTilaById(int id, String huone_tila) {
        huoneDAO.updateTilaById(id, huone_tila);
    }

    public void updateHuoneHintaById(int id, double huone_hinta) {
        huoneDAO.updateHintaById(id, huone_hinta);
    }

    public void deleteHuone(int id) {
        huoneDAO.removeById(id);
    }

}
