package model.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.datasourse.MariaDbConnection;
import model.enteties.Kayttaja;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Data Access Object for the Kayttaja entity
 */
public class KayttajaDAO {
    private static final Logger logger = LoggerFactory.getLogger(KayttajaDAO.class);
    private static final String USER_NOT_FOUND_MESSAGE = "Käyttäjää ei löytynyt sähköpostilla: {}";
    private static final String USER_NOT_FOUND_ID_MESSAGE = "Käyttäjää ei löytynyt ID:llä: {}";
    String spostiStr = "sposti";

    /**
     * Persist a new user to the database
     * @param kayttaja The user to persist
     */
    public void persist(Kayttaja kayttaja) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.persist(kayttaja);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close(); // Ensure the EntityManager is closed
        }
    }

    /**
     * Find a user by their ID
     * @param id The ID of the user
     * @return The user with the given ID, or null if not found
     */
    public Kayttaja findById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            return em.find(Kayttaja.class, id);
        } finally {
            em.close(); // Ensure the EntityManager is closed
        }
    }

    /**
     * Find a user by their email
     * @param sposti The email of the user
     * @return The user with the given email, or null if not found
     */
    public String findPasswordByEmail(String sposti) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            String salasana = em.createQuery("SELECT k.salasana FROM Kayttaja k WHERE k.sposti = :sposti", String.class)
                    .setParameter(spostiStr, sposti)
                    .getSingleResult();
            em.getTransaction().commit();
            return salasana;
        } catch (NoResultException e) {
            logger.error(USER_NOT_FOUND_MESSAGE, sposti);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close(); // Ensure the EntityManager is closed
        }
    }

    /**
     * Update a user's email by their ID
     * @param id The ID of the user
     * @param sposti The new email
     */
    public void updateEmailById(int id, String sposti) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Kayttaja kayttaja = em.find(Kayttaja.class, id);
            if (kayttaja != null) {
                kayttaja.setSposti(sposti);
                logger.info("Käyttäjän tiedot päivitetty onnistuneesti!");
                em.getTransaction().commit();
            } else {
                logger.warn(USER_NOT_FOUND_ID_MESSAGE, id);
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
     * Change a user's password by their email
     * @param sposti The email of the user
     * @param newPassword The new password
     * @return The user with the given email, or null if not found
     */
    public Kayttaja changePasswordByEmail(String sposti, String newPassword) {
        EntityManager em = MariaDbConnection.getInstance();
        String hashattuSalasana = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        Kayttaja kayttaja = null;

        try {
            em.getTransaction().begin();

            // Hae käyttäjä sähköpostin perusteella
            kayttaja = em.createQuery("SELECT k FROM Kayttaja k WHERE k.sposti = :sposti", Kayttaja.class)
                    .setParameter(spostiStr, sposti)
                    .getSingleResult();

            // Jos käyttäjä löytyy, vaihdetaan salasana
            kayttaja.setSalasana(hashattuSalasana); // Aseta uusi hashattu salasana
            logger.info("Salasana vaihdettu onnistuneesti käyttäjälle: {}", kayttaja.getSposti());
            em.getTransaction().commit(); // Varmista muutosten tallentaminen

        } catch (NoResultException e) {
            logger.warn(USER_NOT_FOUND_MESSAGE, sposti);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Perutaan muutokset
            }
        } catch (Exception e) {
            logger.error("Virhe tapahtui: {}", e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Perutaan muutokset virhetilanteessa
            }
        } finally {
            em.close();
        }
        return kayttaja; // Palauta käyttäjä (tai null, jos ei löytynyt)
    }

    /**
     * Remove a user by their ID
     * @param id The ID of the user
     * @return The removed user, or null if not found
     */
    public Kayttaja removeById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        Kayttaja kayttaja = null;

        try {
            em.getTransaction().begin();
            kayttaja = em.find(Kayttaja.class, id);
            if (kayttaja != null) {
                em.remove(kayttaja);
                logger.info("Kayttaja poistettu onnistuneesti!");
                em.getTransaction().commit();  // Commit transaction if removal succeeds
            } else {
                logger.warn("Kayttajaa ei löytynyt ID:llä: {}", id);
            }
        } catch (Exception e) {
            // Handle exceptions, rollback transaction if something goes wrong
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
        return kayttaja;  // Return the removed Kayttaja (or null if not found)
    }

    /**
     * Update a user's information by their ID
     * @param id The ID of the user
     * @param etunimi The new first name
     * @param sukunimi The new last name
     * @param puh The new phone number
     * @param rooli The new role
     */
    public void updateKayttajaById(int id, String etunimi, String sukunimi, String puh, String rooli) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Kayttaja kayttaja = em.find(Kayttaja.class, id);
            if (kayttaja != null) {
                kayttaja.setEtunimi(etunimi);
                kayttaja.setSukunimi(sukunimi);
                kayttaja.setPuh(puh);
                kayttaja.setRooli(rooli);
                logger.info("Käyttäjän tiedot päivitetty onnistuneesti!");
                em.getTransaction().commit();
            } else {
                logger.warn("Kayttajaa ei löytynyt ID:llä: {}", id);
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
     * Find all users in the database
     * @return A list of all users
     */
    public List<Kayttaja> findAllKayttaja() {
        EntityManager em = MariaDbConnection.getInstance();
        List<Kayttaja> kayttajat = null;
        try {
            kayttajat = em.createQuery("SELECT k FROM Kayttaja k", Kayttaja.class).getResultList();
        } finally {
            em.close();
        }
        return kayttajat; // Return the list of users
    }

    public boolean onkoEmailOlemassa(String sposti) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(k) FROM Kayttaja k WHERE k.sposti = :sposti", Long.class);
            query.setParameter(spostiStr, sposti);
            Long count = query.getSingleResult();
            return count > 0;
        } finally {
            em.close(); // Ensure the EntityManager is closed
        }
    }

    /**
     * Find a user by their email
     * @param email The email of the user
     * @return The user with the given email, or null if not found
     */
    public String findRooliByEmail(String email) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            String rooliString = em.createQuery("SELECT k.rooli FROM Kayttaja k WHERE k.sposti = :email", String.class)
                    .setParameter("email", email)
                    .getSingleResult();
            em.getTransaction().commit();
            return rooliString;

        } catch (NoResultException e) {
            logger.warn(USER_NOT_FOUND_MESSAGE, email);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }
}