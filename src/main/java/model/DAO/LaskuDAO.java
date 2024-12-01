package model.DAO;

import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Lasku;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * LaskuDAO class is used to manage Lasku objects in the database.
 */
public class LaskuDAO {
    private static final Logger logger = LoggerFactory.getLogger(LaskuDAO.class);

    /**
     * Persist method is used to save Lasku object to the database.
     * @param lasku Lasku object
     */
    public void persist(Lasku lasku) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        em.persist(lasku);
        em.getTransaction().commit();
    }

    /**
     * haeByLaskuId method is used to find Lasku object by id.
     * @param laskuId int
     * @return Lasku object
     */
    public Lasku haeByLaskuId(int laskuId) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            Lasku lasku = em.find(Lasku.class, laskuId);
            if (lasku != null) {
                return lasku;
            }
        } finally {
            em.close();
        }
        return null;
    }

     /**
     * haeByAsiakasId method is used to find Lasku objects by asiakas id.
     * @param asiakasId int
     * @return List of Lasku objects
     */

     public List<Lasku> haeByAsiakasId(int asiakasId) {
         EntityManager em = MariaDbConnection.getInstance();
         List<Lasku> laskut = null;
         try {
             laskut = em.createQuery("SELECT l FROM Lasku l WHERE l.asiakasId = :asiakasId", Lasku.class)
                     .setParameter("asiakasId", asiakasId)
                     .getResultList();
             if (!laskut.isEmpty()) {
                 logger.info("Laskut löytyi asiakas id:llä {}", asiakasId);
                 return laskut;
             } else {
                 logger.warn("Laskua ei löytynyt asiakas id:llä {}", asiakasId);
             }
         } catch (Exception e) {
             logger.error("Error fetching Lasku for asiakas id: {}", asiakasId, e);
         } finally {
             em.close();
         }
         return laskut;
     }


    /**
     * updateLaskuById method is used to update Lasku object by id.
     * @param id int
     * @param maksuStatus String
     * @param varausMuoto String
     * @param valuutta String
     * @param asiakasId int
     */
    public void updateLaskuById(int id, String maksuStatus, String varausMuoto, String valuutta, int asiakasId) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Lasku lasku = em.find(Lasku.class, id);
            if (lasku != null) {
                lasku.setMaksuStatus(maksuStatus);
                lasku.setVarausMuoto(varausMuoto);
                lasku.setValuutta(valuutta);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * updateStatusById method is used to update Lasku object status by id.
     * @param id int
     * @param tila String
     */
    public void updateStatusById(int id, String tila) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Lasku lasku = em.find(Lasku.class, id);
            if (lasku != null) {
                lasku.setMaksuStatus(tila);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * removeById method is used to remove Lasku object from the database by id.
     * @param id int
     */
    public void removeById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        try{
            em.getTransaction().begin();
            Lasku lasku = em.find(Lasku.class, id);
            if (lasku != null) {
                em.remove(lasku);
            } else {
                logger.warn("Laskua ei löytynyt id:llä {}", id);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * haeKaikkilaskut method is used to find all Lasku objects from the database.
     * @return List of Lasku objects
     */
    public List<Lasku> haeKaikkilaskut() {
        EntityManager em = MariaDbConnection.getInstance();
        List<Lasku> laskut = null;
        try {
            laskut = em.createQuery("SELECT l FROM Lasku l", Lasku.class).getResultList();
            if (!laskut.isEmpty()) {
                return laskut;
            } else {
                logger.info("Lasku lista on tyhje.");
            }
        } finally {
            em.close();
        }
        return laskut;
    }
}
