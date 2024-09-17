package model.DAO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
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
            printAsiakas(asikas);
            return asikas;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Asiakas findByEmail(String email) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();

            Asiakas asiakas = em.createQuery("SELECT a FROM Asiakas a WHERE a.sposti = :sposti", Asiakas.class)
                    .setParameter("sposti", email)
                    .getSingleResult();
            em.getTransaction().commit();
            printAsiakas(asiakas);
            return asiakas;
        } catch (NoResultException e) {
            System.out.println("Asiakasta ei löytynyt sähköpostilla: " + email);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Jokin muu virhe tapahtui
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Asiakas findByNImet(String etunimi, String sukunimi) {
        EntityManager em = MariaDbConnection.getInstance();
        List <Asiakas> asiakkaat = null;
        Asiakas palauttavaAsiakkaat = null;
        try {
            asiakkaat = em.createQuery("SELECT a FROM Asiakas a WHERE a.etunimi = :etunimi AND a.sukunimi = :sukunimi", Asiakas.class)
                    .setParameter("etunimi", etunimi)
                    .setParameter("sukunimi", sukunimi)
                    .getResultList();
            if (!asiakkaat.isEmpty()) {
                // Palautetaan ensimmäinen asiakas
                palauttavaAsiakkaat = asiakkaat.get(0);
            } else {
                System.out.println("Asiakasta ei löytynyt etunimellä: " + etunimi + " ja sukunimellä: " + sukunimi);
            }
            for (Asiakas asiakas : asiakkaat) {
                printAsiakas(asiakas);
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return palauttavaAsiakkaat;

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

    public void printAsiakas(Asiakas asiakas) {
        System.out.println("Asiakas ID: " + asiakas.getAsiakasId());
        System.out.println("Etunimi: " + asiakas.getEtunimi());
        System.out.println("Sukunimi: " + asiakas.getSukunimi());
        System.out.println("Sähköposti: " + asiakas.getSposti());
        System.out.println("Puhelin: " + asiakas.getPuh());
        System.out.println("Henkilömäärä: " + asiakas.getHenkiloMaara());
        System.out.println("Huomio: " + asiakas.getHuomio());
        System.out.println();
    }
}