package model.DAO;

import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Hotelli;

public class HotelliDAO {
    public void persist(Hotelli hotelli) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        em.persist(hotelli);
        em.getTransaction().commit();
    }

    public Hotelli findById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            return em.find(Hotelli.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
/*
    public Hotelli removeById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        Hotelli hotelli = null;

        try {
            em.getTransaction().begin();
            hotelli = em.find(Hotelli.class, id);

            if (hotelli != null) {
                em.remove(hotelli);
                System.out.println("Hotelli poistettu onnistuneesti!");
                em.getTransaction().commit();  // Commit transaction if removal succeeds
            } else {
                System.out.println("Hotelli ei löytynyt ID:llä: " + id);
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

        return hotelli;  // Return the removed hotelli (or null if not found)
    }


 */

}
