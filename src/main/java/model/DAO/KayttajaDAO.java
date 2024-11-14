package model.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.datasourse.MariaDbConnection;
import model.enteties.Kayttaja;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class KayttajaDAO {

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

    public Kayttaja findById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            return em.find(Kayttaja.class, id);
        } finally {
            em.close(); // Ensure the EntityManager is closed
        }
    }

    public String findPasswordByEmail(String sposti) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            String salasana = em.createQuery("SELECT k.salasana FROM Kayttaja k WHERE k.sposti = :sposti", String.class)
                    .setParameter("sposti", sposti)
                    .getSingleResult();
            em.getTransaction().commit();
            return salasana;
        } catch (NoResultException e) {
            System.out.println("Käyttäjää ei löytynyt sähköpostilla: " + sposti);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close(); // Ensure the EntityManager is closed
        }
    }

    public void updateEmailById(int id, String sposti) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Kayttaja kayttaja = em.find(Kayttaja.class, id);
            if (kayttaja != null) {
                kayttaja.setSposti(sposti);
                System.out.println("Käyttäjän tiedot päivitetty onnistuneesti!");
                em.getTransaction().commit();
            } else {
                System.out.println("Kayttajaa ei löytynyt ID:llä: " + id);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Kayttaja changePasswordByEmail(String sposti, String newPassword) {
        EntityManager em = MariaDbConnection.getInstance();
        String hashattuSalasana = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        Kayttaja kayttaja = null;

        try {
            em.getTransaction().begin();

            // Hae käyttäjä sähköpostin perusteella
            kayttaja = em.createQuery("SELECT k FROM Kayttaja k WHERE k.sposti = :sposti", Kayttaja.class)
                    .setParameter("sposti", sposti)
                    .getSingleResult();

            // Jos käyttäjä löytyy, vaihdetaan salasana
            kayttaja.setSalasana(hashattuSalasana); // Aseta uusi hashattu salasana
            System.out.println("Salasana vaihdettu onnistuneesti käyttäjälle: " + kayttaja.getSposti());
            em.getTransaction().commit(); // Varmista muutosten tallentaminen

        } catch (NoResultException e) {
            System.out.println("Käyttäjää ei löytynyt sähköpostilla: " + sposti);
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Perutaan muutokset
            }
        } catch (Exception e) {
            System.out.println("Virhe tapahtui: " + e.getMessage());
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback(); // Perutaan muutokset virhetilanteessa
            }
        } finally {
            if (em != null && em.isOpen()) {
                em.close(); // Suljetaan EntityManager turvallisesti
            }
        }
        return kayttaja; // Palautetaan käyttäjä, jos löytyi ja salasana vaihdettiin
    }

    public Kayttaja removeById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        Kayttaja kayttaja = null;

        try {
            em.getTransaction().begin();
            kayttaja = em.find(Kayttaja.class, id);
            if (kayttaja != null) {
                em.remove(kayttaja);
                System.out.println("Kayttaja poistettu onnistuneesti!");
                em.getTransaction().commit();  // Commit transaction if removal succeeds
            } else {
                System.out.println("Kayttajaa ei löytynyt ID:llä: " + id);
            }
        } catch (Exception e) {
            // Handle exceptions, rollback transaction if something goes wrong
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return kayttaja;  // Return the removed Kayttaja (or null if not found)
    }

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
                System.out.println("Käyttäjän tiedot päivitetty onnistuneesti!");
                em.getTransaction().commit();
            } else {
                System.out.println("Kayttajaa ei löytynyt ID:llä: " + id);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Kayttaja> findAllKayttaja() {
        EntityManager em = MariaDbConnection.getInstance();
        List<Kayttaja> kayttajat = null;
        try {
            kayttajat = em.createQuery("SELECT k FROM Kayttaja k", Kayttaja.class).getResultList();
        } finally {
            if (em != null) {
                em.close(); // Ensure the EntityManager is closed
            }
        }
        return kayttajat; // Return the list of users
    }

    public boolean onkoEmailOlemassa(String sposti) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(k) FROM Kayttaja k WHERE k.sposti = :sposti", Long.class);
            query.setParameter("sposti", sposti);
            Long count = query.getSingleResult();
            return count > 0;
        } finally {
            em.close(); // Ensure the EntityManager is closed
        }
    }

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
            System.out.println("Käyttäjää ei löytynyt sähköpostilla: " + email);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

}