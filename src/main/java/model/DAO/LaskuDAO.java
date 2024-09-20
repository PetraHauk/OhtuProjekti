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
            printLasku(lasku);
            return lasku;

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    public Lasku haeByAsiakasId(int asiakas_id) {
    EntityManager em = MariaDbConnection.getInstance();
        try {
        Lasku lasku = em.find(Lasku.class, asiakas_id);
        printLasku(lasku);
        return lasku;

    } finally {
        if (em != null) {
            em.close();
        }
    }
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

    public void removeById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        Lasku lasku = em.find(Lasku.class, id);
        if (lasku != null) {
            em.remove(lasku);
        }
        em.getTransaction().commit();
    }

    public void printLasku(Lasku Lasku) {
        System.out.println("Lasku id: " + Lasku.getLaskuId());
        System.out.println("Maksu status: " + Lasku.getMaksuStatus());
        System.out.println("Varaus muoto: " + Lasku.getVarausMuoto());
        System.out.println("Valuutta: " + Lasku.getValuutta());
        System.out.println("Asiakas id: " + Lasku.getAsiakasId());
        System.out.println();
    }
}
