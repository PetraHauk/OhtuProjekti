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
            Kayttaja kayttaja = em.find(Kayttaja.class, id);
            printKayttaja(kayttaja);
            return kayttaja;
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
            // Ensure the EntityManager is closed
            if (em != null) {
                em.close();
            }
        }
        return kayttaja;  // Return the removed Kayttaja (or null if not found)
    }

    public void updateKayttajaById(int id, String etunimi, String sukunimi, String sposti, String puh, String rooli, String salasana) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Kayttaja kayttaja = em.find(Kayttaja.class, id);
            if (kayttaja != null) {
                kayttaja.setEtunimi(etunimi);
                kayttaja.setSukunimi(sukunimi);
                kayttaja.setSposti(sposti);
                kayttaja.setPuh(puh);
                kayttaja.setRooli(rooli);
                kayttaja.setSalasana(salasana);
                System.out.println("Käyttäjän tiedot päivitetty onnistuneesti!");
                em.getTransaction().commit();
            } else {
                System.out.println("Kayttäjä ei löytynyt ID:llä: " + id);
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
    public void printKayttaja(Kayttaja kayttaja) {
        System.out.println("Etunimi: " + kayttaja.getEtunimi());
        System.out.println("Sukunimi: " + kayttaja.getSukunimi());
        System.out.println("Sähköposti: " + kayttaja.getSposti());
        System.out.println("Puhelin: " + kayttaja.getPuh());
        System.out.println("Rooli: " + kayttaja.getRooli());
        System.out.println("Salasana: " + kayttaja.getSalasana());
        System.out.println(" ");
    }

}