package tictactoe;

public class UserSession {
    private static String username;

    public static boolean isLoggedIn() {
        return username != null && !username.isEmpty();
    }

    public static void login(String name) {
        username = name;
    }

    public static String getUsername() {
        return username;
    }

    public static void logout() {
        username = null;
    }
}