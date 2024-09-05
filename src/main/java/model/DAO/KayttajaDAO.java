package model.DAO;

import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Kayttaja;

public class KayttajaDAO {
    public void persist(Kayttaja kayttaja) {
        EntityManager em = MariaDbConnection.terminate();
        em.getTransaction().begin();
        em.persist(kayttaja);
        em.getTransaction().commit();
    }
}
