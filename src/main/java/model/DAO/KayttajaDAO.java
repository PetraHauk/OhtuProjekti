package model.DAO;

import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Kayttaja;

public class KayttajaDAO {
    public void persist(Kayttaja kayttaja) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        em.persist(kayttaja);
        em.getTransaction().commit();
    }

    public Kayttaja findById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            return em.find(Kayttaja.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    //update by id
    public void updateById(int id, Kayttaja kayttaja) {
        EntityManager em = MariaDbConnection.getInstance();

        try {
            em.getTransaction().begin();
            Kayttaja kayttaja_update = em.find(Kayttaja.class, id);
            if (kayttaja_update != null) {

                //voi muokkaa vielä mitkä tarvitsemme update, ei pakko olla kaikki
                kayttaja_update.setEtunimi(kayttaja.getEtunimi());
                kayttaja_update.setSukunimi(kayttaja.getSukunimi());
                kayttaja_update.setSposti(kayttaja.getSposti());
                kayttaja_update.setPuh(kayttaja.getPuh());
                kayttaja_update.setRooli(kayttaja.getRooli());
                kayttaja_update.setSalasana(kayttaja.getSalasana());
                em.getTransaction().commit();
            } else {
                System.out.println("Kayttajaa ei löytynyt");
            }

        } catch (Exception e) {
            System.out.println("Kayttajaa ei löytynyt");
        } finally {
            if (em != null) {
                em.close();
            }

        }
    }

    public void remove(Kayttaja kayttaja) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.remove(kayttaja);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}

