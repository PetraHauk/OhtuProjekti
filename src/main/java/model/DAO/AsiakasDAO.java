package model.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import java.util.List;
import model.datasourse.MariaDbConnection;
import model.enteties.Asiakas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Access Object for the Asiakas entity
 */
public class AsiakasDAO {
    // Create a logger instance for this class
    private static final Logger logger = LoggerFactory.getLogger(AsiakasDAO.class);

    /**
     * Persist a new customer to the database
     * @param asiakas The customer to be persisted
     */
    public void persist(Asiakas asiakas) {
        EntityManager em = MariaDbConnection.getInstance();

        try {
            em.getTransaction().begin();

            // Tarkistetaan, löytyykö asiakas sähköpostin perusteella
            Asiakas existingAsiakas = em.createQuery(
                            "SELECT a FROM Asiakas a WHERE a.sposti = :sposti", Asiakas.class)
                    .setParameter("sposti", asiakas.getSposti())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (existingAsiakas == null) {
                em.persist(asiakas);
                em.getTransaction().commit();
                logger.info("Asiakas lisätty: {}", asiakas.getSposti());
            } else {
                logger.warn("Asiakas on jo olemassa: {}", asiakas.getSposti());
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Virhe lisättäessä asiakasta: {}", asiakas.getSposti());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Find a customer by their ID
     * @param laskuId The ID of the customer
     * @return The customer with the given ID, or null if not found
     */
    public Asiakas findByLaskuId(int laskuId) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            Asiakas foundAasikas = em.find(Asiakas.class, laskuId);
            if (foundAasikas != null) {
                return foundAasikas;
            }
        } finally {
            em.close();
        }
        return null;
    }

    /**
     * Find a customer by their email address
     * @param email The email address of the customer
     * @return The customer with the given email address, or null if not found
     */
    public Asiakas findByEmail(String email) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            // Execute the query and return the result
            return em.createQuery("SELECT a FROM Asiakas a WHERE a.sposti = :sposti", Asiakas.class)
                    .setParameter("sposti", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            // Return null if no result is found
            return null;
        } finally {
            // Ensure the EntityManager is closed
            em.close();
        }
    }

    /**
     * Find a customer by their first and last name
     * @param etunimi The first name of the customer
     * @param sukunimi The last name of the customer
     * @return A list of customers with the given first and last name
     */
    public List <Asiakas> findByNImet(String etunimi, String sukunimi) {
        EntityManager em = MariaDbConnection.getInstance();
        List <Asiakas> asiakkaat = null;
        try {
            asiakkaat = em.createQuery("SELECT a FROM Asiakas a WHERE a.etunimi = :etunimi AND a.sukunimi = :sukunimi", Asiakas.class)
                    .setParameter("etunimi", etunimi)
                    .setParameter("sukunimi", sukunimi)
                    .getResultList();
            if (!asiakkaat.isEmpty()) {
                return asiakkaat;
            }
        } finally {
            em.close();
        }
        return asiakkaat;
    }

    /**
     * Find customers by a keyword
     * @param keyword The keyword to search for
     * @return A list of customers that match the keyword
     */
    public List<Asiakas> findAsiakasByKeyword(String keyword) {
        EntityManager em = MariaDbConnection.getInstance();
        List<Asiakas> asiakkaat = null;
        try {
            asiakkaat = em.createQuery("SELECT a FROM Asiakas a WHERE a.etunimi LIKE :keyword OR a.sukunimi LIKE :keyword OR a.sposti LIKE :keyword", Asiakas.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getResultList();
            if (!asiakkaat.isEmpty()) {
                return asiakkaat;
            }
        } finally {
            em.close();
        }
        return asiakkaat;
    }

    /**
     * Find all customers
     * @return A list of all customers
     */
    public List<Asiakas> findAsiakkaat() {
        EntityManager em = MariaDbConnection.getInstance();
        List<Asiakas> asiakkaat = null;
        try {
            asiakkaat = em.createQuery("SELECT v FROM Asiakas v", Asiakas.class).getResultList();

            if (!asiakkaat.isEmpty()) {
                return asiakkaat;
            }
        } finally {
            em.close();
        }
        return asiakkaat;
    }

    /**
     * Update a customer by their ID
     * @param id The ID of the customer
     * @param etunimi The first name of the customer
     * @param sukunimi The last name of the customer
     * @param sposti The email address of the customer
     * @param puh The phone number of the customer
     * @param henkiloMaara The number of people in the customer's party
     * @param huomio Any additional notes about the customer
     */
    public void updateAsiakasById(int id, String etunimi, String sukunimi, String sposti, String puh, int henkiloMaara, String huomio) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Asiakas asiakas = em.find(Asiakas.class, id);
            if (asiakas != null) {
                asiakas.setEtunimi(etunimi);
                asiakas.setSukunimi(sukunimi);
                asiakas.setSposti(sposti);
                asiakas.setPuh(puh);
                asiakas.setHenkiloMaara(henkiloMaara);
                asiakas.setHuomio(huomio);

                logger.info("Asiakkaan tiedot päivitetty onnistuneesti!");
                em.getTransaction().commit();
            } else {
                logger.warn("Asiakas ei löytynyt ID:llä: {}", id);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /**
     * Remove a customer by their ID
     * @param id The ID of the customer
     */
    public void removeById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Asiakas asiakas = em.find(Asiakas.class, id);
            if (asiakas != null) {
                em.remove(asiakas);
                logger.info("Asiakas poistettu onnistuneesti!");
                em.getTransaction().commit();
            } else {
                logger.warn("Asiakasta ei löytynyt ID:llä: {}", id);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }
}
