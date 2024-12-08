package model.DAO;

import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Varaus;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * VarausDAO luokka, joka sisältää tietokantaan tehtävät toiminnot varaus tauluun.
 */
public class VarausDAO {
    private static final Logger logger = LoggerFactory.getLogger(VarausDAO.class);

    public void persist(Varaus varaus) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        em.persist(varaus);
        em.getTransaction().commit();
    }

    /**
     * Hae kaikki varaukset tietokannasta.
     * @return Lista varaus olioista.
     */
    public List<Varaus> haeVaraukset() {
        EntityManager em = MariaDbConnection.getInstance();
        List<Varaus> varaukset = null;
        try {
            varaukset = em.createQuery("SELECT v FROM Varaus v", Varaus.class).getResultList();
            if (!varaukset.isEmpty()) {
                return varaukset;
            }
        } finally {
            em.close();
        }
        return varaukset;
    }

/**
     * Hae varaus id:n perusteella.
     * @param varausId int
     * @return Varaus olio.
     */
    public Varaus haeByVarausId(int varausId) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            Varaus varaus = em.find(Varaus.class, varausId);
            if (varaus != null) {
                logger.info("Varaus löytyi id:llä {}", varausId);
                return varaus;
            }
        } finally {
            em.close();
        }
        return null;
    }

    /**
     * Hae varaukset asiakas id:n perusteella.
     * @param laskuId int
     * @return Lista varaus olioista.
     */
    public List<Varaus> haeByLaskuId(int laskuId) {
        EntityManager em = MariaDbConnection.getInstance();
        List<Varaus> varaukset = null;
        try {
            varaukset = em.createQuery("SELECT v FROM Varaus v WHERE v.laskuId = :lasku_id", Varaus.class)
                    .setParameter("lasku_id", laskuId)
                    .getResultList();
            if(!varaukset.isEmpty()) {
                // Palautetaan ensimmäinen varaus
                return varaukset;
            }
        } finally {
            em.close();
        }
        return varaukset;
    }

    /**
     * Päivitä varaus id:n perusteella.
     * @param varausId int
     * @param huoneId Integer
     */
    public void paivitaVarausHuoneId(int varausId, Integer huoneId) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        Varaus varaus = em.find(Varaus.class, varausId);
        if (varaus != null) {
            varaus.setHuoneId(huoneId);
        } else {
            logger.warn("Varausta ei löytynyt id:llä {}", varausId);
        }
        em.getTransaction().commit();
    }

    /**
     * Päivitä varaus.
     * @param varaus Varaus
     */
    public void paivitaVaraus(Varaus varaus) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        em.merge(varaus);
        em.getTransaction().commit();
    }

    /**
     * Poista varaus id:n perusteella.
     * @param id int
     */
    public void removeById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        Varaus varaus = em.find(Varaus.class, id);
        if (varaus != null) {
            em.remove(varaus);
        } else {
            logger.warn("Varausta ei löytynyt id:llä {}", id);
        }
        em.getTransaction().commit();
    }

    /**
     * Poista varaukset huone id:n perusteella.
     * @param huoneId int
     */
    public void removeByHuoneId(int huoneId) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            List<Varaus> varaukset = em.createQuery("SELECT v FROM Varaus v WHERE v.huoneId = :huoneId", Varaus.class)
                    .setParameter("huoneId", huoneId)
                    .getResultList();
            for (Varaus varaus : varaukset) {
                em.remove(varaus);
            }
            em.getTransaction().commit();
            logger.info("All reservations for huone ID: {} have been removed.", huoneId);

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Failed to remove reservations for huone ID: {}", huoneId);
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}