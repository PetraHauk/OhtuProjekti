package model.DAO;

import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Huone;

public class HuoneDAO {

    public void persist(Huone huone) {
        EntityManager em = MariaDbConnection.terminate();
        em.getTransaction().begin();
        em.persist(huone);
        em.getTransaction().commit();
    }
}
