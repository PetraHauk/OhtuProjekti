package model.DAO;

import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Varaus;

import java.time.LocalDate;
import java.util.List;

public class VarausDAO {
    public void persist(Varaus varaus) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        em.persist(varaus);
        em.getTransaction().commit();
    }


    public Varaus haeVaraukset() {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            // Hae kaikki varaukset käyttäen JPQL-kyselyä
            List<Varaus> varaukset = em.createQuery("SELECT v FROM Varaus v", Varaus.class).getResultList();
            for (Varaus varaus : varaukset) {
                printVaraus(varaus);  // Tulosta varaus, jos tarpeellista
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }



    public void haeByVarausId(int varaus_id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            Varaus varaus = em.find(Varaus.class, varaus_id);
            printVaraus(varaus);

        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void haeByLaskuId(int lasku_id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            Varaus varaus = em.find(Varaus.class, lasku_id);
            printVaraus(varaus);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void paivitaVarausById(int varaus_id, int huone_maara, LocalDate alkuPvm, LocalDate loppuPvm) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        Varaus varaus = em.find(Varaus.class, varaus_id);
        if (varaus != null) {
            varaus.setHuoneMaara();
            varaus.setAlkuPvm(LocalDate.now());
            varaus.setLoppuPvm(LocalDate.now());
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
        System.out.println("Huoneen määrä: " + varaus.getVarausId());
        System.out.println("Alku pvm: " + varaus.getAlkuPvm());
        System.out.println("Loppu pvm: " + varaus.getLoppuPvm());
        System.out.println("Huoneen id: " + varaus.getHuoneId());
        System.out.println("Lasku ID: " + varaus.getLaskuId());
    }
}
