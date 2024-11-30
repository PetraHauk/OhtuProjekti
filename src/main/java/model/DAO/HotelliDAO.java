package model.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NonUniqueResultException;
import model.datasourse.MariaDbConnection;
import model.enteties.Hotelli;
import java.util.List;

/**
 * Data Access Object for the Hotelli entity
 */
public class HotelliDAO {
    public void persist(Hotelli hotelli) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        em.persist(hotelli);
        em.getTransaction().commit();
    }

    /**
     * Find a hotel by its ID
     * @param id The ID of the hotel
     * @return The hotel with the given ID, or null if not found
     */
    public Hotelli findById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            return em.find(Hotelli.class, id);
        } finally {
            em.close();
        }
    }

    /**
     * Find a hotel by its name
     * @param name The name of the hotel
     * @return The hotel with the given name, or null if not found
     */
//    public Hotelli findByName(String name) {
//        EntityManager em = MariaDbConnection.getInstance();
//        try {
//            return em.createQuery("SELECT h FROM Hotelli h WHERE h.nimi = :name", Hotelli.class)
//                    .setParameter("name", name)
//                    .getSingleResult();
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }
//    }

    public Hotelli findByName(String name) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            return em.createQuery("SELECT h FROM Hotelli h WHERE h.nimi = :name", Hotelli.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Get the number of hotels in the database
     * @return The number of hotels
     */
    public int getRowCount() {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            return em.createQuery("SELECT COUNT(h) FROM Hotelli h", Long.class).getSingleResult().intValue();
        } finally {
            em.close();
        }
    }

    /**
     * Remove a hotel by its ID
     * @param hotelliId The ID of the hotel to be removed
     */
    public void removeById(int hotelliId) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Hotelli hotelli = em.find(Hotelli.class, hotelliId);
            if (hotelli != null) {
                em.remove(hotelli);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    /**
     * Get all hotels
     */
    public List<Hotelli> getAllHotellis() {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            return em.createQuery("SELECT h FROM Hotelli h", Hotelli.class).getResultList();
        } finally {
            em.close();
        }
    }
}
