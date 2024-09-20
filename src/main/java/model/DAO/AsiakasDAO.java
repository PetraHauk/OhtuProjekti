package model.DAO;
import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Asiakas;
import java.util.List;

public class AsiakasDAO {
    public void persist(Asiakas asiakas) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        em.persist(asiakas);
        em.getTransaction().commit();
    }

    public Asiakas findByLaskuId(int lasku_id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            Asiakas asikas = em.find(Asiakas.class, lasku_id);
            if (asikas != null) {
                return asikas;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public Asiakas findByEmail(String email) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();

            Asiakas asiakas = em.createQuery("SELECT a FROM Asiakas a WHERE a.sposti = :sposti", Asiakas.class)
                    .setParameter("sposti", email)
                    .getSingleResult();
            em.getTransaction().commit();
          if (asiakas != null) {
              return asiakas;
          }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public List <Asiakas> findByNImet(String etunimi, String sukunimi) {
        EntityManager em = MariaDbConnection.getInstance();
        List <Asiakas> asiakkaat = null;
        try {
            asiakkaat = em.createQuery("SELECT a FROM Asiakas a WHERE a.etunimi = :etunimi AND a.sukunimi = :sukunimi", Asiakas.class)
                    .setParameter("etunimi", etunimi)
                    .setParameter("sukunimi", sukunimi)
                    .getResultList();
            if (!asiakkaat.isEmpty()) {
                return asiakkaat;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;

    }

    public List<Asiakas> findAsukkaat() {
        EntityManager em = MariaDbConnection.getInstance();
        List<Asiakas> asiakkaat = null;
        try {
            asiakkaat = em.createQuery("SELECT v FROM Asiakas v", Asiakas.class).getResultList();

            if (!asiakkaat.isEmpty()) {
                return asiakkaat;
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return null;
    }

    public void updateAsiakasById(int id, String etunimi, String sukunimi, String sposti, String puh, int henkiloMaara, String huomio) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Asiakas asiakas = em.find(Asiakas.class, id);
            if (asiakas != null) {
                asiakas.setEtunimi(etunimi);
                asiakas.setSukunimi(sukunimi);
                asiakas.setSposti(sposti);
                asiakas.setPuh(puh);
                asiakas.setHenkiloMaara(henkiloMaara);
                asiakas.setHuomio(huomio);

                System.out.println("Asiakkaan tiedot päivitetty onnistuneesti!");
                em.getTransaction().commit();
            } else {
                System.out.println("Asiakas ei löytynyt ID:llä: " + id);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}