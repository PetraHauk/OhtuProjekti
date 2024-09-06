package model.DAO;

import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Kayttaja;

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

    public Kayttaja findPasswordByEmail(String sposti) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            // Use a JPQL query to find the user by email
            Kayttaja kayttaja = em.createQuery("SELECT k FROM Kayttaja k WHERE k.sposti = :sposti", Kayttaja.class)
                    .setParameter("sposti", sposti)
                    .getSingleResult();

            return kayttaja;  // Return the Kayttaja object

        } catch (Exception e) {
            e.printStackTrace();  // Log the exception for debugging
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;  // Return null if no user is found or an exception occurs
    }


    //update by id
    public Kayttaja updateById(int id, Kayttaja kayttaja) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Kayttaja kayttaja_update = em.find(Kayttaja.class, id);
            if (kayttaja_update != null) {
                kayttaja_update.setEtunimi(kayttaja.getEtunimi());
                kayttaja_update.setSukunimi(kayttaja.getSukunimi());
                kayttaja_update.setSposti(kayttaja.getSposti());
                kayttaja_update.setPuh(kayttaja.getPuh());
                kayttaja_update.setRooli(kayttaja.getRooli());
                kayttaja_update.setSalasana(kayttaja.getSalasana());
                em.getTransaction().commit();
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

    public Kayttaja changePasswordByEmail(String sposti, String newPassword) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Kayttaja kayttaja = em.createQuery("SELECT k FROM Kayttaja k WHERE k.sposti = :sposti", Kayttaja.class)
                    .setParameter("sposti", sposti)
                    .getSingleResult();
            if (kayttaja != null) {
                kayttaja.setSalasana(newPassword);
                em.getTransaction().commit();
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

