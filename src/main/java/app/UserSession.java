package app;

public class UserSession {
    private static String username;
    private static int rooli;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserSession.username = username;
    }

    public static int getRooli() {
        return rooli;
    }

    public static void setRooli(int rooli) {
        UserSession.rooli = rooli;
    }
}
