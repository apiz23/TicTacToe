package tictactoe;

import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JPanel {

    public WelcomeScreen(ScreenManager manager) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 245));

        // --- 1. Dynamic Title (Uses UserSession) ---
        String playerName = UserSession.isLoggedIn() ? UserSession.getUsername() : "Player";
        JLabel title = new JLabel("Welcome, " + playerName, SwingConstants.CENTER);

        title.setFont(new Font("SansSerif", Font.BOLD, 40));
        title.setForeground(new Color(50, 50, 50));
        title.setBorder(BorderFactory.createEmptyBorder(80, 0, 50, 0));
        add(title, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel contentPanel = new JPanel(new GridLayout(0, 1, 20, 20));
        contentPanel.setOpaque(false);
        contentPanel.setPreferredSize(new Dimension(350, 400));

        // ==========================
        // BUTTONS
        // ==========================

        JButton startGameBtn = createStyledButton("🎮 Start Game");
        startGameBtn.addActionListener(e -> manager.showGameModeScreen());
        contentPanel.add(startGameBtn);

        JButton scoreboardBtn = createStyledButton("🏆 Scoreboard");
        scoreboardBtn.addActionListener(e -> manager.showScoreboardScreen());
        contentPanel.add(scoreboardBtn);

        // NEW: Settings Button
        JButton settingsBtn = createStyledButton("⚙️ Settings");
        settingsBtn.setBackground(new Color(108, 117, 125)); // Gray color for settings
        settingsBtn.addActionListener(e -> manager.showSettingsScreen());
        contentPanel.add(settingsBtn);

        // Logout Button
        JButton logoutBtn = createStyledButton("🚪 Logout");
        logoutBtn.setBackground(new Color(220, 53, 69)); // Red color for logout
        logoutBtn.addActionListener(e -> {
            UserSession.logout();
            manager.showLoginScreen();
        });
        contentPanel.add(logoutBtn);

        JButton exitBtn = createStyledButton("❌ Exit");
        exitBtn.setBackground(new Color(108, 117, 125)); // Dark gray
        exitBtn.addActionListener(e -> System.exit(0));
        contentPanel.add(exitBtn);

        centerWrapper.add(contentPanel);
        add(centerWrapper, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        footer.setPreferredSize(new Dimension(0, 50));
        footer.setOpaque(false);
        add(footer, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 22));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}