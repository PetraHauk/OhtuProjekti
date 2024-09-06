package controller;

import model.DAO.HoteliDAO;
import model.enteties.Hoteli;

public class HoteliController {
    private HoteliDAO hoteliDAO;

    public HoteliController() {
        hoteliDAO = new HoteliDAO();
    }

    public void lisaaHoteli(String nimi, String osoite, String kaupunki, String puh, String maa) {
        Hoteli hoteli = new Hoteli(0, nimi, osoite, kaupunki, puh, maa);
        hoteliDAO.persist(hoteli);
    }

    public Hoteli haeHoteliById(int id) {
        return hoteliDAO.findById(id);
    }

   /* public void poistaHoteli(int id) {
        Hoteli hoteli = hoteliDAO.removeById(id);
        hoteliDAO.removeById(id);
    }

    */

}
