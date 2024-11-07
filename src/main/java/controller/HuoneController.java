
package controller;

import model.DAO.HuoneDAO;
import model.enteties.Huone;
import model.enteties.Hotelli;
import model.DAO.HotelliDAO;

import java.util.Iterator;
import java.util.List;

public class HuoneController {
    private HuoneDAO huoneDAO;
    private HotelliDAO hotelliDAO;

    public HuoneController() {
        huoneDAO = new HuoneDAO();
        hotelliDAO = new HotelliDAO();
    }

    public void lisaaHuone(
            int huone_nro,
            String huone_tyyppi_fi,
            String huone_tyyppi_en,
            String huone_tyyppi_ru,
            String huone_tyyppi_zh,
            String huone_tila_fi,
            String huone_tila_en,
            String huone_tila_ru,
            String huone_tila_zh,
            double huone_hinta,
            int hotelli_id) {

        // Tarkista, onko hotelli olemassa
        Hotelli hotelli = hotelliDAO.findById(hotelli_id);
        if (hotelli == null) {
            System.out.println("Hotellia ei löytynyt ID:llä " + hotelli_id);
            return; // Lopetetaan toiminto, jos hotellia ei löydy
        }

        // Jos hotelli löytyy, lisätään huone
        Huone huone = new Huone(0, huone_nro, huone_tyyppi_fi, huone_tyyppi_en, huone_tyyppi_ru, huone_tyyppi_zh,
               huone_tila_fi, huone_tila_en, huone_tila_ru, huone_tila_zh, huone_hinta, hotelli_id);
        huoneDAO.persist(huone);
        System.out.println("Huone lisätty onnistuneesti hotelliin ID:llä " + hotelli_id);
    }

    public List<Huone> FindHuoneetByHoteliId(int hotelli_id) {
        return huoneDAO.haeHuoneetByHotelliId(hotelli_id);
    }

    public Huone findHuoneById(int id) {
        Huone huone = huoneDAO.findByRoomId(id);
        if (huone == null) {
            return null;
        }
        return huone;
    }

    public int getHuoneNroById(int huoneId) {
        // Assume that this method interacts with a DAO or repository to get the room number
        Huone huone = huoneDAO.findByRoomId(huoneId);
        return huone.getHuone_nro();
    }


    public Huone findHuoneByNro(int nro) {
        return huoneDAO.findByRoomNro(nro);
    }
    public List<Huone> findHuoneByTila(String huone_tila) {

        return huoneDAO.findByHuoneTila(huone_tila);
    }

    public List<Huone> findHuoneByTyyppi(String huone_tyyppi) {
        return huoneDAO.findByTyyppi(huone_tyyppi);
    }

    public void updateHuoneById(int id, int huone_nro, String huone_tyyppi, String huone_tila, double huone_hinta) {
        huoneDAO.updateHuoneById(id, huone_nro, huone_tyyppi, huone_tila, huone_hinta);
    }

    public void updateHuoneStatusById(int id, String huone_tila) {
        huoneDAO.updateHuoneTilaById(id, huone_tila);
    }
    public void updateHuoneTilaById(int id, String huone_tila) {
        huoneDAO.updateHuoneTilaById(id, huone_tila);
    }

    public void deleteHuone(int id) {
        huoneDAO.removeById(id);
    }

    public List<Huone> findVapaatHuoneetByHotelliId(int hotelli_id){
        List<Huone> huoneet = huoneDAO.haeHuoneetByHotelliId(hotelli_id);


        Iterator<Huone> huoneIterator = huoneet.iterator();
        while (huoneIterator.hasNext()) {
            Huone huone = huoneIterator.next();
            if ("Varattu".equals(huone.getHuone_tila_fi())
                    || "Reserved".equals(huone.getHuone_tila_fi())
                    || "Сдержанный".equals(huone.getHuone_tila_fi())
                    || "已预订".equals(huone.getHuone_tila_fi())) {
                huoneIterator.remove();
            }
        }
        return huoneet;
    }

}
