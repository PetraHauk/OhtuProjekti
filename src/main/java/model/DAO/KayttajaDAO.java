package model.DAO;

import jakarta.persistence.EntityManager;
import model.datasourse.MariaDbConnection;
import model.enteties.Kayttaja;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KayttajaDAO {
    private Connection conn;

    public Kayttaja haeKayttaja(String email, String password) {
        // SQL query to retrieve user based on email and password
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ps.setString(2, password); // In real cases, passwords should be hashed and compared
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Kayttaja(email, password); // Found user
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // No user found
    }

    public void persist(Kayttaja kayttaja) {
        // SQL query to insert user into the database
        String query = "INSERT INTO users (email, password) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, kayttaja.getEmail());
            ps.setString(2, kayttaja.getPassword()); // Password should be hashed
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
