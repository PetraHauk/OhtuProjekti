package model.DAO;

import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Huone;
import model.enteties.Varaus;

import java.util.Date;

public class VarausDAO {
    public void persist(Varaus varaus) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        em.persist(varaus);
        em.getTransaction().commit();
    }

    public Varaus findByVarausId(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            Varaus varaus = em.find(Varaus.class, id);
            printVaraus(varaus);
            return varaus;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Varaus findByLaskuId(int lasku_id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            Varaus varaus = em.find(Varaus.class, lasku_id);
            printVaraus(varaus);
            return varaus;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void updateVarausTilaById(int id, Date alkuPvm, Date loppuPvm) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        Varaus varaus = em.find(Varaus.class, id);
        if (varaus != null) {
            varaus.setAlku_pvm(alkuPvm);
            varaus.setLoppu_pvm(loppuPvm);
        }
        em.getTransaction().commit();
    }

    public void removeById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        Varaus varaus = em.find(Varaus.class, id);
        if (varaus != null) {
            em.remove(varaus);
        }
        em.getTransaction().commit();
    }

    public void printVaraus(Varaus varaus) {
        System.out.println("Huoneen määrä: " + varaus.getVaraus_id());
        System.out.println("Alku pvm: " + varaus.getAlku_pvm());
        System.out.println("Loppu pvm: " + varaus.getLoppu_pvm());
        System.out.println("Huoneen id: " + varaus.getHuoneId());
        System.out.println("Lasku ID: " + varaus.getLaskuId());
    }
}
