package model.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import model.datasourse.MariaDbConnection;
import model.enteties.Kayttaja;
import org.mindrot.jbcrypt.BCrypt;

public class KayttajaDAO {
    public void persist(Kayttaja kayttaja) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        em.persist(kayttaja);
        em.getTransaction().commit();
    }

    public Kayttaja findById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            return em.find(Kayttaja.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public String findPasswordByEmail(String sposti) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();

            // Haetaan käyttäjän salasana sähköpostin perusteella
            String salasana = em.createQuery("SELECT k.salasana FROM Kayttaja k WHERE k.sposti = :sposti", String.class)
                    .setParameter("sposti", sposti)
                    .getSingleResult();

            em.getTransaction().commit();

            // Palautetaan haettu salasana
            return salasana;

        } catch (NoResultException e) {
            // Jos käyttäjää ei löytynyt, palautetaan null
            System.out.println("Käyttäjää ei löytynyt sähköpostilla: " + sposti);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Jokin muu virhe tapahtui
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    //update by id
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
        try {
            em.getTransaction().begin();
            //Kayttaja kayttaja = em.find(Kayttaja.class, sposti);
            Kayttaja kayttaja = em.createQuery("SELECT k FROM Kayttaja k WHERE k.sposti = :sposti", Kayttaja.class)
                    .setParameter("sposti", sposti)
                    .getSingleResult();
            if (kayttaja != null) {

                kayttaja.setSalasana(hashattuSalasana);

            } else {
                System.out.println("Kayttajaa ei löytynyt");
            }
        } catch (Exception e) {
            System.out.println("Kayttajaa ei löytynyt");
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
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
            // Ensure the EntityManager is closed
            if (em != null) {
                em.close();
            }
        }

        return kayttaja;  // Return the removed Kayttaja (or null if not found)
    }
}

