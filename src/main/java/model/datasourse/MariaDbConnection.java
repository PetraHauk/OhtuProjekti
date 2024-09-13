package model.datasourse;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class MariaDbConnection {

    private static EntityManagerFactory emf = null;
    private static EntityManager em = null;

    public static EntityManager getInstance() {

        if (em==null) {
            if (emf==null) {
                Map<String, String> properties = new HashMap<>();
                properties.put("jakarta.persistence.jdbc.user", "root");
                properties.put("jakarta.persistence.jdbc.password", "ImRoot");

                emf = Persistence.createEntityManagerFactory("hotelli_db", properties);
            }
            em = emf.createEntityManager();
        }
        return em;
    }
}