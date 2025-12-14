package tictactoe;

public class UserSession {
    private static String username;
    private static String hardwareId;

    public static boolean isLoggedIn() {
        return username != null && !username.isEmpty();
    }

    public static void login(String name, String hwId) {
        username = name;
        hardwareId = hwId;
    }

    public static String getUsername() {
        return username;
    }

    public static String getHardwareId() {
        return hardwareId;
    }

    public static void logout() {
        username = null;
        hardwareId = null;
    }
}