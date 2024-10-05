package model.datasourse;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class MariaDbConnection {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("hotelli_db");

    public static EntityManager getInstance() {
        return emf.createEntityManager();  // Luo uusi EntityManager jokaiselle operaatiolle
    }
}
