package model.DAO;

import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Hoteli;
import model.enteties.Kayttaja;


public class HoteliDAO {

    public void persist(Hoteli hoteli) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        em.persist(hoteli);
        em.getTransaction().commit();
    }

    public Hoteli findById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            return em.find(Hoteli.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
/*
    public Hoteli removeById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        Hoteli hoteli = null;

        try {
            em.getTransaction().begin();
            hoteli = em.find(Hoteli.class, id);

            if (hoteli != null) {
                em.remove(hoteli);
                System.out.println("Hoteli poistettu onnistuneesti!");
                em.getTransaction().commit();  // Commit transaction if removal succeeds
            } else {
                System.out.println("Hoteli ei löytynyt ID:llä: " + id);
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

        return hoteli;  // Return the removed hoteli (or null if not found)
    }


 */

}
