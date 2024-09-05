package model.DAO;

import model.datasourse.MariaDbConnection;
import model.enteties.Asiakas;
import jakarta.persistence.EntityManager;

public class AsiakasDAO {
    public void persist(Asiakas asiakas) {
        EntityManager em = MariaDbConnection.terminate();
        em.getTransaction().begin();
        em.persist(asiakas);
        em.getTransaction().commit();
    }
}
