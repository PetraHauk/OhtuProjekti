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

        // Tarkista, onko hotelli olemassa
        Hotelli hotelli = hotelliDAO.findById(hotelli_id);
        if (hotelli == null) {
            System.out.println("Hotellia ei löytynyt ID:llä " + hotelli_id);
            return; // Lopetetaan toiminto, jos hotellia ei löydy
        }

        // Jos hotelli löytyy, lisätään huone
        Huone huone = new Huone(0, huone_nro, huone_tyyppi, huone_tila, huone_hinta, hotelli_id);
        huoneDAO.persist(huone);
        System.out.println("Huone lisätty onnistuneesti hotelliin ID:llä " + hotelli_id);
    }

    public void FindHuoneetByHoteliId(int hotelli_id) {
        huoneDAO.haeHuoneetByHotelliId(hotelli_id);
    }

    public void findHuoneById(int id) {
        huoneDAO.findByRoomId(id);
    }

    public Huone findHuoneByTila(String huone_tila) {
        return huoneDAO.findByHuoneTila(huone_tila);
    }

    public Huone findHuoneByTyyppi(String huone_tyyppi) {
        return huoneDAO.findByTyyppi(huone_tyyppi);
    }

    public void updateHuoneById(int id, int huone_nro, String huone_tyyppi, String huone_tila, double huone_hinta) {
        huoneDAO.updateHuoneById(id, huone_nro, huone_tyyppi, huone_tila, huone_hinta);
    }


    public void deleteHuone(int id) {
        huoneDAO.removeById(id);
    }

}
