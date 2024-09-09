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

    public Huone findByRoomId(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            Huone huone = em.find(Huone.class, id);
            printHuone(huone);
            return huone;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    public void findByHuoneTila(String huone_tila) {
        EntityManager em = MariaDbConnection.getInstance();
        List<Huone> huoneet = null;
        try {
            huoneet = em.createQuery("SELECT h FROM Huone h WHERE h.huone_tila = :huone_tila", Huone.class)
                    .setParameter("huone_tila", huone_tila)
                    .getResultList();
            for (Huone huone : huoneet) {
                printHuone(huone);
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void findByTyyppi(String huone_tyyppi) {
        EntityManager em = MariaDbConnection.getInstance();
        List<Huone> huoneet = null;
        try {
            huoneet = em.createQuery("SELECT h FROM Huone h WHERE h.huone_tyyppi = :huone_tyyppi", Huone.class)
                    .setParameter("huone_tyyppi", huone_tyyppi)
                    .getResultList();
            for (Huone huone : huoneet) {
                printHuone(huone);
            }
        } finally {
            if (em != null) {
                em.close();  // Sulje EntityManager
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
    public void printHuone(Huone huone) {
        System.out.println("Huoneen numero: " + huone.getHuone_nro());
        System.out.println("Huoneen tyyppi: " + huone.getHuone_tyyppi());
        System.out.println("Huoneen tila: " + huone.getHuone_tila());
        System.out.println("Huoneen hinta: " + huone.getHuone_hinta());
        System.out.println("Hotelli ID: " + huone.getHotelli_id());
    }

}


