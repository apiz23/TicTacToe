package tictactoe;

import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JPanel {

    // Dark Theme Color Palette
    private final Color BG_COLOR = new Color(18, 18, 24);
    private final Color CARD_COLOR = new Color(30, 30, 40);
    private final Color ACCENT_COLOR = new Color(0, 150, 255);
    private final Color ACCENT_HOVER = new Color(0, 170, 255);
    private final Color TEXT_PRIMARY = new Color(240, 240, 240);
    private final Color TEXT_SECONDARY = new Color(180, 180, 200);
    private final Color BUTTON_RED = new Color(231, 76, 60);
    private final Color BUTTON_RED_HOVER = new Color(235, 96, 80);
    private final Color BUTTON_GRAY = new Color(108, 117, 125);
    private final Color BUTTON_GRAY_HOVER = new Color(128, 137, 145);

    public WelcomeScreen(ScreenManager manager) {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // Main content with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(20, 20, 30),
                        getWidth(), getHeight(), new Color(15, 15, 25)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Draw subtle grid pattern
                g2d.setColor(new Color(255, 255, 255, 10));
                for (int i = 0; i < getWidth(); i += 40) {
                    for (int j = 0; j < getHeight(); j += 40) {
                        g2d.fillRect(i, j, 1, 1);
                    }
                }

                // Draw diagonal lines
                g2d.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{5}, 0));
                g2d.setColor(new Color(255, 255, 255, 5));
                for (int i = -getHeight(); i < getWidth(); i += 30) {
                    g2d.drawLine(i, 0, i + getHeight(), getHeight());
                }
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Header section
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center button panel
        JPanel buttonPanel = createButtonPanel(manager);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = createFooterPanel();
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(50, 0, 30, 0));

        // Player name with glow effect
        String playerName = UserSession.isLoggedIn() ? UserSession.getUsername() : "Player";
        JLabel welcomeLabel = new JLabel("WELCOME") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setFont(getFont());

                // Draw shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                FontMetrics fm = g2d.getFontMetrics();
                String text = getText();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent() - 10;
                g2d.drawString(text, x + 2, y + 2);

                // Draw main text
                g2d.setColor(TEXT_SECONDARY);
                g2d.drawString(text, x, y);

                g2d.dispose();
            }
        };
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Player name with gradient
        JLabel nameLabel = new JLabel(playerName.toUpperCase()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Create gradient for player name
                GradientPaint gradient = new GradientPaint(
                        0, 0, ACCENT_COLOR,
                        getWidth(), 0, ACCENT_HOVER
                );
                g2d.setPaint(gradient);
                g2d.setFont(getFont());

                FontMetrics fm = g2d.getFontMetrics();
                String text = getText();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();

                // Draw glowing effect
                for (int i = 0; i < 3; i++) {
                    g2d.setColor(new Color(0, 150, 255, 30 - i*10));
                    g2d.drawString(text, x + i, y + i);
                }

                // Draw main text
                g2d.setPaint(gradient);
                g2d.drawString(text, x, y);

                g2d.dispose();
            }
        };
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitle = new JLabel("SELECT YOUR ACTION");
        subtitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Decorative line
        JPanel line = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw gradient line
                GradientPaint gradient = new GradientPaint(
                        0, 0, ACCENT_COLOR,
                        getWidth(), 0, new Color(0, 150, 255, 0)
                );
                g2d.setPaint(gradient);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawLine(0, getHeight()/2, getWidth()/2, getHeight()/2);

                gradient = new GradientPaint(
                        getWidth()/2, 0, new Color(0, 150, 255, 0),
                        getWidth(), 0, ACCENT_COLOR
                );
                g2d.setPaint(gradient);
                g2d.drawLine(getWidth()/2, getHeight()/2, getWidth(), getHeight()/2);
            }
        };
        line.setPreferredSize(new Dimension(300, 4));
        line.setMaximumSize(new Dimension(300, 4));
        line.setOpaque(false);

        header.add(welcomeLabel);
        header.add(Box.createRigidArea(new Dimension(0, 5)));
        header.add(nameLabel);
        header.add(Box.createRigidArea(new Dimension(0, 15)));
        header.add(line);
        header.add(Box.createRigidArea(new Dimension(0, 10)));
        header.add(subtitle);

        return header;
    }

    private JPanel createButtonPanel(ScreenManager manager) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        // Start Game Button
        JButton startGameBtn = createStyledButton("🎮 START GAME", ACCENT_COLOR);
        startGameBtn.addActionListener(e -> manager.showGameModeScreen());
        buttonPanel.add(startGameBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Scoreboard Button
        JButton scoreboardBtn = createStyledButton("🏆 LEADERBOARD", new Color(52, 152, 219));
        scoreboardBtn.addActionListener(e -> manager.showScoreboardScreen());
        buttonPanel.add(scoreboardBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Settings Button
        JButton settingsBtn = createStyledButton("⚙️ SETTINGS", BUTTON_GRAY);
        settingsBtn.addActionListener(e -> manager.showSettingsScreen());
        buttonPanel.add(settingsBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Logout Button
        JButton logoutBtn = createStyledButton("🚪 LOGOUT", BUTTON_RED);
        logoutBtn.addActionListener(e -> {
            // Fade out animation
            Timer fadeTimer = new Timer(20, null);
            float[] alpha = {1.0f};
            fadeTimer.addActionListener(ev -> {
                alpha[0] -= 0.05f;
                if (alpha[0] <= 0) {
                    fadeTimer.stop();
                    UserSession.logout();
                    manager.showLoginScreen();
                } else {
                    logoutBtn.setForeground(new Color(255, 255, 255, (int)(alpha[0] * 255)));
                }
            });
            fadeTimer.start();
        });
        buttonPanel.add(logoutBtn);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Exit Button
        JButton exitBtn = createStyledButton("❌ EXIT", new Color(50, 50, 60));
        exitBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "<html><div style='background:#1e1e28;color:#f0f0f0;padding:15px;border-radius:5px;'>" +
                            "<b style='color:#ff5050;font-size:14px;'>CONFIRM EXIT</b><br><br>" +
                            "<span style='color:#b4b4c8;'>Are you sure you want to exit the game?</span>" +
                            "</div></html>",
                    "Exit Game",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                // Fade out animation before exit
                Timer fadeTimer = new Timer(20, null);
                float[] alpha = {1.0f};
                fadeTimer.addActionListener(ev -> {
                    alpha[0] -= 0.05f;
                    if (alpha[0] <= 0) {
                        fadeTimer.stop();
                        System.exit(0);
                    }
                    setBackground(new Color(18, 18, 24, (int)(alpha[0] * 255)));
                    repaint();
                });
                fadeTimer.start();
            }
        });
        buttonPanel.add(exitBtn);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton btn = new JButton(text) {
            private float glow = 0;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Glow animation
                if (getModel().isRollover() || getModel().isPressed()) {
                    glow = Math.min(glow + 0.1f, 1.0f);
                } else {
                    glow = Math.max(glow - 0.1f, 0.0f);
                }

                // Determine button color based on state
                Color buttonColor;
                if (getModel().isPressed()) {
                    buttonColor = baseColor.darker();
                } else if (getModel().isRollover()) {
                    if (baseColor.equals(BUTTON_RED)) {
                        buttonColor = BUTTON_RED_HOVER;
                    } else if (baseColor.equals(BUTTON_GRAY)) {
                        buttonColor = BUTTON_GRAY_HOVER;
                    } else {
                        buttonColor = ACCENT_HOVER;
                    }
                } else {
                    buttonColor = baseColor;
                }

                // Draw button background
                g2d.setColor(buttonColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Draw glow effect
                if (glow > 0) {
                    g2d.setColor(new Color(255, 255, 255, (int)(50 * glow)));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                }

                // Draw icon
                String icon = text.substring(0, text.indexOf(' '));
                String label = text.substring(text.indexOf(' ') + 1);

                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 22));
                g2d.drawString(icon, 25, getHeight() / 2 + 8);

                // Draw text
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(label);
                int x = (getWidth() - textWidth) / 2;
                int y = getHeight() / 2 + fm.getAscent() / 2 - 2;

                // Text shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.drawString(label, x + 1, y + 1);

                // Main text
                g2d.setColor(Color.WHITE);
                g2d.drawString(label, x, y);

                g2d.dispose();

                // Continue animation
                if (glow > 0 || getModel().isRollover()) {
                    repaint(30);
                }
            }
        };

        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(300, 55));
        btn.setMaximumSize(new Dimension(300, 55));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }

    private JPanel createFooterPanel() {
        JPanel footer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw gradient line at top
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(0, 150, 255, 50),
                        getWidth(), 0, new Color(0, 150, 255, 0)
                );
                g2d.setPaint(gradient);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawLine(0, 0, getWidth(), 0);
            }
        };

        footer.setPreferredSize(new Dimension(0, 60));
        footer.setOpaque(false);
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));

        // Status label
        String status = UserSession.isLoggedIn() ? "Connected" : "Offline";
        Color statusColor = UserSession.isLoggedIn() ? new Color(46, 204, 113) : new Color(127, 140, 141);

        JLabel statusLabel = new JLabel("Status: " + status);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(statusColor);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Copyright
        JLabel copyright = new JLabel("© 2024 Tic Tac Toe Arena");
        copyright.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        copyright.setForeground(new Color(100, 100, 120));
        copyright.setAlignmentX(Component.CENTER_ALIGNMENT);

        footer.add(Box.createVerticalGlue());
        footer.add(statusLabel);
        footer.add(copyright);
        footer.add(Box.createRigidArea(new Dimension(0, 15)));

        return footer;
    }
}