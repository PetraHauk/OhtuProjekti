package model.datasourse;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class MariaDbConnection {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("hotelli_db");
   // private static EntityManager em = null;

    public static EntityManager getInstance() {
        return emf.createEntityManager();  // Luo uusi EntityManager jokaiselle operaatiolle
    }


}



/*



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MariaDbConnection {

    private static Connection conn = null;

    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()) {
                conn = DriverManager.getConnection(

                        "jdbc:mariadb://localhost:3306/hotelli_db?user=root&password=" + System.getenv("root"));

            }
            return conn;
        } catch (SQLException e) {
            System.out.println("Connection failed.");
            e.printStackTrace();
            return null;
        }
    }

    public static void terminate() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
*/





