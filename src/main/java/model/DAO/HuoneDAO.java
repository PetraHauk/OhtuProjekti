package model.DAO;
import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Huone;

import java.util.Collections;
import java.util.List;
import model.service.LocaleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Access Object for the Huone entity
 */
public class HuoneDAO {
    private static final Logger logger = LoggerFactory.getLogger(HuoneDAO.class);

    public void persist(Huone huone) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        em.persist(huone);
        em.getTransaction().commit();
    }

    /**
     * Find rooms by hotel ID
     * @param hotelliId The ID of the hotel
     * @return A list of rooms in the hotel with the given ID
     */
    public List<Huone> haeHuoneetByHotelliId(int hotelliId) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            return em.createQuery("SELECT h FROM Huone h WHERE h.hotelliId = :hotelliId", Huone.class)
                    .setParameter("hotelliId", hotelliId)
                    .getResultList();
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return Collections.emptyList();
        } finally {
            em.close();
        }
    }


    /**
     * Find a room by its ID
     * @param id The ID of the room
     * @return The room with the given ID, or null if not found
     */
    public Huone findByRoomId(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            Huone huone = em.find(Huone.class, id);
            if (huone != null) {
                return huone;
            }
        } finally {
            em.close();
        }
        return null;
    }

    /**
     * Update a room by its ID
     * @param id The ID of the room
     * @param huoneNro The room number
     * @param huoneTyyppi The room type
     * @param huoneTila The room status
     * @param huoneHinta The room price
     */
    public void updateHuoneById(int id, int huoneNro,
                                String huoneTyyppi,
                                String huoneTila,
                                double huoneHinta) {
        EntityManager em = MariaDbConnection.getInstance();

        // Hanki käännetyt huone tyyppi ja tila
        List<String> huoneTypeList = LocaleManager.getLocalizedTyyppiInput(huoneTyyppi);
        List<String> huoneTilaList = LocaleManager.getLocalizedTilaInput(huoneTila);
        logger.info("Localized Room Type List: {}", huoneTypeList);
        logger.info("Localized Room Status List: {}", huoneTilaList);


        // Tarkistetaan, että lista ei ole null ja että se sisältää tarvittavat elementit
        String huoneTypeFi = huoneTypeList.get(0);
        String huoneTypeEn = huoneTypeList.get(1);
        String huoneTypeRu = huoneTypeList.get(2);
        String huoneTypeZh = huoneTypeList.get(3);

        String huoneTilaFi = huoneTilaList.get(0);
        String huoneTilaEn = huoneTilaList.get(1);
        String huoneTilaRu = huoneTilaList.get(2);
        String huoneTilaZh = huoneTilaList.get(3);

        logger.info("Localized Room Type Fi: {}", huoneTypeFi);
        logger.info("Localized Room Type En: {}", huoneTypeEn);

        try {
            em.getTransaction().begin();
            Huone huone = em.find(Huone.class, id);
            if (huone != null) {
                huone.setHuoneNro(huoneNro);

                huone.setHuoneTyyppiFi(huoneTypeFi);
                huone.setHuoneTyyppiEn(huoneTypeEn);
                huone.setHuoneTyyppiRu(huoneTypeRu);
                huone.setHuoneTyyppiZh(huoneTypeZh);
                huone.setHuoneTilaFi(huoneTilaFi);
                huone.setHuoneTilaEn(huoneTilaEn);
                huone.setHuoneTilaRu(huoneTilaRu);
                huone.setHuoneTilaZh(huoneTilaZh);
                huone.setHuoneHinta(huoneHinta);
            } else {
                logger.warn("Huonetta ei löytynyt id:llä  {}", id);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Update a room status by its ID
     * @param id The ID of the room
     * @param huoneTila The room status
     */
    public void updateHuoneTilaById(int id, String huoneTila) {
        EntityManager em = MariaDbConnection.getInstance();

        List<String> huoneTilaList = LocaleManager.getLocalizedTilaInput(huoneTila);

        String huoneTilaFi = huoneTilaList.get(0);
        String huoneTilaEn = huoneTilaList.get(1);
        String huoneTilaRu = huoneTilaList.get(2);
        String huoneTilaZh = huoneTilaList.get(3);

        try {
            em.getTransaction().begin();
            Huone huone = em.find(Huone.class, id);
            if (huone != null) {
                huone.setHuoneTilaFi(huoneTilaFi);
                huone.setHuoneTilaEn(huoneTilaEn);
                huone.setHuoneTilaRu(huoneTilaRu);
                huone.setHuoneTilaZh(huoneTilaZh);
            } else {
                logger.warn("Huonetta ei löytynyt id:llä {}", id);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Remove a room by its ID
     * @param huoneId The ID of the room
     */
    public void removeById(int huoneId) {
        VarausDAO varausDAO = new VarausDAO(); // Create an instance of VarausDAO

        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();

            // First, remove all related reservations
            varausDAO.removeByHuoneId(huoneId);

            Huone huone = em.find(Huone.class, huoneId);
            if (huone != null) {
                em.remove(huone);
                logger.info("Huone poistettu id:llä {}", huoneId);
            } else {
                logger.warn("Huonetta ei löytynyt id:llä {}", huoneId);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Rollback in case of an error
            }
            logger.error("Failed to remove huone ID: {}", huoneId);
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}