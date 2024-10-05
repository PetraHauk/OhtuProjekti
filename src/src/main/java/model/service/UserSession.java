package model.service;

public class UserSession {
    private static String username;
    private static String rooli;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserSession.username = username;
    }

    public static String getRooli() {
        return rooli;
    }

    public static void setRooli(String rooli) {
        UserSession.rooli = rooli;
    }
}
