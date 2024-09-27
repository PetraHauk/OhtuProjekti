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

    public List<Varaus> haeVaraukset() {
        EntityManager em = MariaDbConnection.getInstance();
        List<Varaus> varaukset = null;
        try {
            varaukset = em.createQuery("SELECT v FROM Varaus v", Varaus.class).getResultList();
            if (!varaukset.isEmpty()) {
                return varaukset;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public Varaus haeByVarausId(int varaus_id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            Varaus varaus = em.find(Varaus.class, varaus_id);
            if (varaus != null) {
                return varaus;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public List<Varaus> haeByLaskuId(int lasku_id) {
        EntityManager em = MariaDbConnection.getInstance();
        List<Varaus> varaukset = null;
        try {
            varaukset = em.createQuery("SELECT v FROM Varaus v WHERE v.laskuId = :lasku_id", Varaus.class)
                    .setParameter("lasku_id", lasku_id)
                    .getResultList();
            if(!varaukset.isEmpty()) {
                // Palautetaan ensimmäinen varaus
                return varaukset;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public void paivitaVarausById(int varaus_id, int huone_maara, LocalDate alkuPvm, LocalDate loppuPvm) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        Varaus varaus = em.find(Varaus.class, varaus_id);
        if (varaus != null) {
            varaus.setHuoneMaara();
            varaus.setAlkuPvm(LocalDate.now());
            varaus.setLoppuPvm(LocalDate.now());
        } else {
            System.out.println("Varausta ei löytynyt id:llä " + varaus_id);
        }
        em.getTransaction().commit();
    }

    public void removeById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        Varaus varaus = em.find(Varaus.class, id);
        if (varaus != null) {
            em.remove(varaus);
        } else {
            System.out.println("Varausta ei löytynyt id:llä " + id);
        }
        em.getTransaction().commit();
    }

    public int varausPaivat( LocalDate alkuPvm, LocalDate loppuPvm) {
        return alkuPvm.compareTo(loppuPvm);
    }
}
