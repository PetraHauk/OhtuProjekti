
package model.DAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import model.datasourse.MariaDbConnection;
import model.enteties.Asiakas;

import java.util.List;

public class AsiakasDAO {

    public void persist(Asiakas asiakas) {
        EntityManager em = MariaDbConnection.getInstance();

        try {
            em.getTransaction().begin();

            // Tarkistetaan, löytyykö asiakas sähköpostin perusteella
            Asiakas existingAsiakas = em.createQuery(
                            "SELECT a FROM Asiakas a WHERE a.sposti = :sposti", Asiakas.class)
                    .setParameter("sposti", asiakas.getSposti())
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (existingAsiakas == null) {
                em.persist(asiakas);
                em.getTransaction().commit();
                System.out.println("Asiakas lisätty: " + asiakas.getSposti());
            } else {
                System.out.println("Asiakas on jo olemassa: " + asiakas.getSposti());
                em.getTransaction().rollback();
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
            // Execute the query and return the result
            return em.createQuery("SELECT a FROM Asiakas a WHERE a.sposti = :sposti", Asiakas.class)
                    .setParameter("sposti", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            // Return null if no result is found
            return null;
        } finally {
            // Ensure the EntityManager is closed
            if (em != null) {
                em.close();
            }
        }
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

    public List<Asiakas> findAsiakasByKeyword(String keyword) {
        EntityManager em = MariaDbConnection.getInstance();
        List<Asiakas> asiakkaat = null;
        try {
            asiakkaat = em.createQuery("SELECT a FROM Asiakas a WHERE a.etunimi LIKE :keyword OR a.sukunimi LIKE :keyword OR a.sposti LIKE :keyword", Asiakas.class)
                    .setParameter("keyword", "%" + keyword + "%")
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

    public List<Asiakas> findAsiakkaat() {
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

//    public void createAsiakas(String etunimi, String sukunimi, String sposti, String puh, int henkiloMaara, String huomio) {
//        EntityManager em = MariaDbConnection.getInstance();
//        try {
//            em.getTransaction().begin();
//            Asiakas asiakas = new Asiakas();
//            asiakas.setEtunimi(etunimi);
//            asiakas.setSukunimi(sukunimi);
//            asiakas.setSposti(sposti);
//            asiakas.setPuh(puh);
//            asiakas.setHenkiloMaara(henkiloMaara);
//            asiakas.setHuomio(huomio);
//            em.persist(asiakas);
//            em.getTransaction().commit();
//        } catch (Exception e) {
//            if (em.getTransaction().isActive()) {
//                em.getTransaction().rollback();
//            }
//            e.printStackTrace();
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }
    //}

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
    public void removeById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            em.getTransaction().begin();
            Asiakas asiakas = em.find(Asiakas.class, id);
            if (asiakas != null) {
                em.remove(asiakas);
                System.out.println("Asiakas poistettu onnistuneesti!");
                em.getTransaction().commit();
            } else {
                System.out.println("Asiakasta ei löytynyt ID:llä: " + id);
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
