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
    }

    public void showWelcomeScreen() {
        WelcomeScreen welcome = new WelcomeScreen(this);
        mainPanel.add(welcome, "WELCOME");
        cardLayout.show(mainPanel, "WELCOME");
    }

    // NEW: Show the setup screen
    public void showGameModeScreen() {
        GameModeScreen modeScreen = new GameModeScreen(this);
        mainPanel.add(modeScreen, "MODE_SELECT");
        cardLayout.show(mainPanel, "MODE_SELECT");
    }

    // UPDATED: Now accepts settings
    public void showGameBoard(String mode, String difficulty) {
        GameBoard game = new GameBoard(this, mode, difficulty);
        mainPanel.add(game, "GAME");
        cardLayout.show(mainPanel, "GAME");
    }

    public void showScoreboardScreen() {
        ScoreboardScreen score = new ScoreboardScreen(this);
        mainPanel.add(score, "SCOREBOARD");
        cardLayout.show(mainPanel, "SCOREBOARD");
    }
}