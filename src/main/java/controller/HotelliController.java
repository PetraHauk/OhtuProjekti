package controller;

import model.DAO.HotelliDAO;
import model.enteties.Hotelli;

public class HotelliController {
    private HotelliDAO hotelliDAO;

    public HotelliController() {
        hotelliDAO = new HotelliDAO();
    }

    public void addHotelli(String nimi, String osoite, String kaupunki, String puh, String maa) {
        Hotelli hotelli = new Hotelli(0, nimi, osoite, kaupunki, puh, maa);
        hotelliDAO.persist(hotelli);
    }

    public Hotelli findHotelliById(int id) {
        return hotelliDAO.findById(id);
    }

   /* public void poistaHotelli(int id) {
        Hotelli hotelli = hotelliDAO.removeById(id);
        hotelliDAO.removeById(id);
    }

    */

}
