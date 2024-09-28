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
            Hotelli hotelli = em.find(Hotelli.class, id);
            if (hotelli != null) {
                return hotelli;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public void removeById(int hotelli_id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Hotelli hotelli = em.find(Hotelli.class, hotelli_id);
            if (hotelli != null) {
                em.remove(hotelli);
            } else {
                System.out.println("Hotellia ei löytynyt id:llä: " + hotelli_id);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
