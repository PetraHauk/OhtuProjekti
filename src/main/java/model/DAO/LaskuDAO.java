package model.DAO;

import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Lasku;
import java.util.List;

public class LaskuDAO {
    public void persist(Lasku lasku) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        em.persist(lasku);
        em.getTransaction().commit();
    }

    public Lasku haeByLaskuId(int lasku_id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            Lasku lasku = em.find(Lasku.class, lasku_id);
            if (lasku != null) {
                return lasku;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public List<Lasku> haeByAsiakasId(int asiakas_id) {
        EntityManager em = MariaDbConnection.getInstance();
        List<Lasku> laskut = null;
        try {
            laskut = em.createQuery("SELECT l FROM Lasku l WHERE l.asiakas_id = :asiakas_id", Lasku.class)
                    .setParameter("asiakas_id", asiakas_id)
                    .getResultList();
            if (!laskut.isEmpty()) {
                return laskut;
            } else {
                System.out.println("Laskua ei löytynyt asiakas id:llä " + asiakas_id);
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public void updateLaskuById(int id, String maksuStatus, String varausMuoto, String valuutta, int asiakasId) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Lasku lasku = em.find(Lasku.class, id);
            if (lasku != null) {
                lasku.setMaksuStatus(maksuStatus);
                lasku.setVarausMuoto(varausMuoto);
                lasku.setValuutta(valuutta);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void updateStatusById(int id, String tila) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Lasku lasku = em.find(Lasku.class, id);
            if (lasku != null) {
                lasku.setMaksuStatus(tila);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void removeById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        Lasku lasku = em.find(Lasku.class, id);
        if (lasku != null) {
            em.remove(lasku);
        } else {
            System.out.println("Laskua ei löytynyt id:llä " + id);
        }
        em.getTransaction().commit();
    }

    public List<Lasku> haeKaikkilaskut() {
        EntityManager em = MariaDbConnection.getInstance();
        List<Lasku> laskut = null;
        try {
            laskut = em.createQuery("SELECT l FROM Lasku l", Lasku.class).getResultList();
            if (!laskut.isEmpty()) {
                return laskut;
            } else {
                System.out.println("Lasku lista on tyhje.");
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }
}
