package model.DAO;

import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Varaus;

public class VarausDAO {
    public void persist(Varaus varaus) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        em.persist(varaus);
        em.getTransaction().commit();
    }

    public Varaus findById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        Varaus varaus = em.find(Varaus.class, id);
        return varaus;
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

    /*
    public void updateVarausTilaById(int id, String varaus_status) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        Varaus varaus = em.find(Varaus.class, id);
        if (varaus != null) {
            varaus.setVaraus_status(varaus_status);
        }
        em.getTransaction().commit();
    }

     */
}
