package model.DAO;

import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Huone;
import java.util.List;

public class HuoneDAO {

    public void persist(Huone huone) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        em.persist(huone);
        em.getTransaction().commit();
    }

    public List<Huone> haeHuoneetByHotelliId(int hotelli_id) {
        EntityManager em = MariaDbConnection.getInstance();
        List<Huone> huoneet = null;
        try {
            huoneet = em.createQuery("SELECT h FROM Huone h WHERE h.hotelli_id = :hotelli_id", Huone.class)
                    .setParameter("hotelli_id", hotelli_id)
                    .getResultList();
            if (!huoneet.isEmpty()) {
                // Voit palauttaa koko listan
                return huoneet;
            } else {
                System.out.println("Huoneita ei löytynyt hotellista ID:llä " + hotelli_id);
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return huoneet;
    }

    public Huone findByRoomId(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            Huone huone = em.find(Huone.class, id);
            return huone;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    public List<Huone> findByHuoneTila(String huone_tila) {
        EntityManager em = MariaDbConnection.getInstance();
        List<Huone> huoneet = null;
        try {
            huoneet = em.createQuery("SELECT h FROM Huone h WHERE h.huone_tila = :huone_tila", Huone.class)
                    .setParameter("huone_tila", huone_tila)
                    .getResultList();
            if(!huoneet.isEmpty()) {
                return huoneet;
            } else {
                System.out.println("Huonetta ei löytynyt tilalla: " + huone_tila);
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public List<Huone> findByTyyppi(String huone_tyyppi) {
        EntityManager em = MariaDbConnection.getInstance();
        List<Huone> huoneet = null;
        try {
            huoneet = em.createQuery("SELECT h FROM Huone h WHERE h.huone_tyyppi = :huone_tyyppi", Huone.class)
                    .setParameter("huone_tyyppi", huone_tyyppi)
                    .getResultList();
            if (!huoneet.isEmpty()) {
                return huoneet;
            } else {
                System.out.println("Huonetta ei löytynyt tyypillä: " + huone_tyyppi);
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public void updateHuoneById(int id, int huone_nro, String huone_tyyppi, String huone_tila, double huone_hinta) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Huone huone = em.find(Huone.class, id);
            if (huone != null) {
                huone.setHuone_nro(huone_nro);
                huone.setHuone_tyyppi(huone_tyyppi);
                huone.setHuone_tila(huone_tila);
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


