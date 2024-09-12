package model.DAO;

import model.datasourse.MariaDbConnection;
import model.enteties.Asiakas;
import jakarta.persistence.EntityManager;

public class AsiakasDAO {

    public void persist(Asiakas asiakas) {
        EntityManager em = MariaDbConnection.getInstance();
        em.getTransaction().begin();
        em.persist(asiakas);
        em.getTransaction().commit();
    }

    public Asiakas findById(int id) {
        EntityManager em = MariaDbConnection.getInstance();
        try {
            return em.find(Asiakas.class, id);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

        public void updateById(int id) {
            EntityManager em = MariaDbConnection.getInstance();

            try {
                em.getTransaction().begin();
                Asiakas asiakas = em.find(Asiakas.class, id);
                if (asiakas != null) {
                    em.getTransaction().commit();
                } else {
                    System.out.println("Asiakasta ei löytynyt");
                }
            } catch (Exception e) {
                System.out.println("Asiakasta ei löytynyt");
            } finally {
                if (em != null) {
                    em.close();
                }
            }
        }

           /* public void removeById(int id) {
                EntityManager em = MariaDbConnection.getInstance();
                try {
                    em.getTransaction().begin();
                    Asiakas asiakas = em.find(Asiakas.class, id);
                    if (asiakas != null) {
                        em.remove(asiakas);
                        em.getTransaction().commit();
                    } else {
                        System.out.println("Asiakas ei löytynyt");
                    }
                } catch (Exception e) {
                    System.out.println("Asiakas ei löytynyt");
                } finally {
                    if (em != null) {
                        em.close();
                    }
                }
            }
        }

            */
}

