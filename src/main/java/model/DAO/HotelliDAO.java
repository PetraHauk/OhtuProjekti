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


    public void removeById(int hotelli_id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Hotelli hotelli = em.find(Hotelli.class, hotelli_id);
            if (hotelli != null) {
                em.remove(hotelli);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

}
