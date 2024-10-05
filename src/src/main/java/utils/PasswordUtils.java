package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // Hash the password using BCrypt
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12)); // 12 is the strength factor
    }

    // Check if the password matches the stored hash
    public static boolean checkPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
