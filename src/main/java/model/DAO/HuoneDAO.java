package model.DAO;

import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import jakarta.persistence.NoResultException;
import model.enteties.Huone;

public class HuoneDAO {

    public void persist(Huone huone) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        em.persist(huone);
       System.out.println("Huone lisätty onnistuneesti!");
        em.getTransaction().commit();
    }

    public Huone findById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            return em.find(Huone.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Huone findByHuoneTila(int huone_tila) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            return em.createQuery("SELECT h FROM Huone h WHERE h.huone_id = :huone_id", Huone.class)
                    .setParameter("huone_id", huone_tila)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Huone FindByTyyppi(String huone_tyyppi) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            return em.createQuery("SELECT h FROM Huone h WHERE h.huone_tyyppi = :huone_tyyppi", Huone.class)
                    .setParameter("huone_tyyppi", huone_tyyppi)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void updateTilaById(int id, String huone_tila) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Huone huone = em.find(Huone.class, id);
            if (huone != null) {
                huone.setHuone_tila(huone_tila);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void updateHintaById(int id, double huone_hinta) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Huone huone = em.find(Huone.class, id);
            if (huone != null) {
                huone.setHuone_hinta(huone_hinta);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void removeById(int huone_id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Huone huone = em.find(Huone.class, huone_id);
            if (huone != null) {
                em.remove(huone);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


}
