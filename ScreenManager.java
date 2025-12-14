package tictactoe;

import javax.swing.*;
import java.awt.*;

public class ScreenManager {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    public ScreenManager(JFrame frame) {
        this.frame = frame;
        this.cardLayout = new CardLayout();
        this.mainPanel = new JPanel(cardLayout);
        frame.add(mainPanel);

        UserSession.logout();

        showLoginScreen();
    }

    public void showLoginScreen() {
        LoginScreen login = new LoginScreen(this);
        mainPanel.add(login, "LOGIN");
        cardLayout.show(mainPanel, "LOGIN");
    }

    public void showWelcomeScreen() {
        WelcomeScreen welcome = new WelcomeScreen(this);
        mainPanel.add(welcome, "WELCOME");
        cardLayout.show(mainPanel, "WELCOME");
    }

    public void showGameModeScreen() {
        // Pass the logged-in username to GameModeScreen
        String username = UserSession.getUsername();
        if (username == null) username = "Guest";

        GameModeScreen modeScreen = new GameModeScreen(this, username);
        mainPanel.add(modeScreen, "MODE_SELECT");
        cardLayout.show(mainPanel, "MODE_SELECT");
    }

    // Updated to accept 5 parameters (added boardSize)
    public void showGameBoard(String mode, String difficulty, String player1, String player2, int boardSize) {
        GameBoard game = new GameBoard(this, mode, difficulty, player1, player2, boardSize);
        mainPanel.add(game, "GAME");
        cardLayout.show(mainPanel, "GAME");
    }

    // Keep old signature for backward compatibility (defaults to 3x3)
    public void showGameBoard(String mode, String difficulty, String player1, String player2) {
        showGameBoard(mode, difficulty, player1, player2, 3);
    }

    public void showScoreboardScreen() {
        ScoreboardScreen score = new ScoreboardScreen(this);
        mainPanel.add(score, "SCOREBOARD");
        cardLayout.show(mainPanel, "SCOREBOARD");
    }

    public void showSettingsScreen() {
        // Get current username
        String username = UserSession.getUsername();
        if (username == null) username = "Guest";

        SettingsScreen settings = new SettingsScreen(this, username);
        mainPanel.add(settings, "SETTINGS");
        cardLayout.show(mainPanel, "SETTINGS");
    }
}