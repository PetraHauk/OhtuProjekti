
package controller;

import model.DAO.HuoneDAO;
import model.enteties.Huone;
import model.enteties.Hotelli;
import model.DAO.HotelliDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

public class HuoneController {
    private static final Logger logger = LoggerFactory.getLogger(HuoneController.class);

    private HuoneDAO huoneDAO;
    private HotelliDAO hotelliDAO;

    public HuoneController() {
        huoneDAO = new HuoneDAO();
        hotelliDAO = new HotelliDAO();
    }

    public void lisaaHuone(
            int huoneNro,
            String huoneTyyppiFi,
            String huoneTyyppiEn,
            String huoneTyyppiRu,
            String huoneTyyppiZh,
            String huoneTilaFi,
            String huoneTilaEn,
            String huoneTilaRu,
            String huoneTilaZh,
            double huoneHinta,
            int hotelliId) {

        // Tarkista, onko hotelli olemassa
        Hotelli hotelli = hotelliDAO.findById(hotelliId);
        if (hotelli == null) {
            logger.info("Hotellia ei löytynyt ID:llä {}", hotelliId);
            return; // Lopetetaan toiminto, jos hotellia ei löydy
        }

        // Jos hotelli löytyy, lisätään huone
        Huone huone = new Huone(0, huoneNro, huoneTyyppiFi, huoneTyyppiEn, huoneTyyppiRu, huoneTyyppiZh,
                huoneTilaFi, huoneTilaEn, huoneTilaRu, huoneTilaZh, huoneHinta, hotelliId);
        huoneDAO.persist(huone);
        logger.info("Huone lisätty onnistuneesti hotelliin ID:llä {}", hotelliId);
    }

    public List<Huone> findHuoneetByHoteliId(int hotelliId) {
        return huoneDAO.haeHuoneetByHotelliId(hotelliId);
    }

    public Huone findHuoneById(int id) {
        Huone huone = huoneDAO.findByRoomId(id);
        if (huone == null) {
            return null;
        }
        return huone;
    }

    public int getTotalRooms(int hotelliId) {
        return huoneDAO.getTotalRooms(hotelliId);
    }

    public void updateHuoneById(int id, int huoneNro, String huoneTyyppi, String huoneTila, double huoneHinta) {
        huoneDAO.updateHuoneById(id, huoneNro, huoneTyyppi, huoneTila, huoneHinta);
    }

    public void updateHuoneStatusById(int id, String huoneTila) {
        huoneDAO.updateHuoneTilaById(id, huoneTila);
    }

    public void deleteHuone(int id) {
        huoneDAO.removeById(id);
    }

    public List<Huone> findVapaatHuoneetByHotelliId(int hotelliId) {
        List<Huone> huoneet = huoneDAO.haeHuoneetByHotelliId(hotelliId);

        Iterator<Huone> huoneIterator = huoneet.iterator();
        while (huoneIterator.hasNext()) {
            Huone huone = huoneIterator.next();
            if (!"Vapaa".equals(huone.getHuoneTilaFi())
                    && !"Free".equals(huone.getHuoneTilaEn())
                    && !"Свободно".equals(huone.getHuoneTilaRu())
                    && !"空闲".equals(huone.getHuoneTilaZh())) {
                huoneIterator.remove();
            }
        }
        return huoneet;
    }
}