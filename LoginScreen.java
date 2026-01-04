package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class LoginScreen extends JPanel {

    // Dark Theme Color Palette
    private final Color BG_COLOR = new Color(18, 18, 24);
    private final Color CARD_COLOR = new Color(30, 30, 40);
    private final Color ACCENT_COLOR = new Color(0, 150, 255);
    private final Color ACCENT_HOVER = new Color(0, 170, 255);
    private final Color TEXT_PRIMARY = new Color(240, 240, 240);
    private final Color TEXT_SECONDARY = new Color(180, 180, 200);
    private final Color BORDER_COLOR = new Color(50, 50, 60);
    private final Color ERROR_COLOR = new Color(255, 80, 80);

    public LoginScreen(ScreenManager manager) {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // Main content panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BG_COLOR);

        // Login card
        JPanel loginCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw gradient background
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(30, 30, 40),
                        getWidth(), getHeight(), new Color(25, 25, 35)
                );
                g2d.setPaint(gradient);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Draw border
                g2d.setColor(ACCENT_COLOR);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

                // Draw subtle grid pattern
                g2d.setColor(new Color(255, 255, 255, 5));
                for (int i = 0; i < getWidth(); i += 30) {
                    for (int j = 0; j < getHeight(); j += 30) {
                        g2d.fillRect(i, j, 1, 1);
                    }
                }
            }
        };

        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setOpaque(false);
        loginCard.setPreferredSize(new Dimension(400, 500));
        loginCard.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        // Header with XO symbol
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Static XO symbol with glow effect
        JLabel xoSymbol = new JLabel("❌⭕") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // Draw glowing effect
                g2d.setFont(new Font("Segoe UI Emoji", Font.BOLD, 60));
                for (int i = 0; i < 3; i++) {
                    g2d.setColor(new Color(0, 150, 255, 50 - i*15));
                    g2d.drawString("❌⭕", i, 60 + i);
                }

                // Draw main text
                g2d.setColor(Color.WHITE);
                g2d.drawString("❌⭕", 0, 60);

                g2d.dispose();
            }
        };
        xoSymbol.setFont(new Font("Segoe UI Emoji", Font.BOLD, 65));
        xoSymbol.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Game title with gradient
        JLabel title = new JLabel("TIC TAC TOE") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Create gradient for text
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

                // Draw text shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.drawString(text, x + 2, y + 2);

                // Draw main text
                g2d.setPaint(gradient);
                g2d.drawString(text, x, y);

                g2d.dispose();
            }
        };
        title.setFont(new Font("Segoe UI", Font.BOLD, 42));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle
        JLabel subtitle = new JLabel("ENTER THE GAME");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(xoSymbol);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        headerPanel.add(title);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(subtitle);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        loginCard.add(headerPanel);

        // Input field section
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setOpaque(false);
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Input label
        JLabel nameLabel = new JLabel("ENTER YOUR PLAYER NAME");
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        nameLabel.setForeground(TEXT_SECONDARY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputPanel.add(nameLabel);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Text field with custom styling
        JTextField nameField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw background
                g2d.setColor(new Color(40, 40, 50));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                // Draw border
                if (hasFocus()) {
                    g2d.setColor(ACCENT_COLOR);
                    g2d.setStroke(new BasicStroke(2));
                } else {
                    g2d.setColor(BORDER_COLOR);
                    g2d.setStroke(new BasicStroke(1));
                }
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

                // Draw text
                g2d.setColor(TEXT_PRIMARY);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                String text = getText();

                if (text.isEmpty() && !hasFocus()) {
                    g2d.setColor(new Color(120, 120, 140));
                    g2d.drawString("Type your username...", 15, getHeight()/2 + fm.getAscent()/2 - 2);
                } else {
                    int y = getHeight()/2 + fm.getAscent()/2 - 2;
                    g2d.drawString(text, 15, y);
                }

                // Draw cursor if focused
                if (hasFocus()) {
                    int textWidth = fm.stringWidth(text);
                    g2d.setColor(ACCENT_COLOR);
                    g2d.fillRect(15 + textWidth + 2, getHeight()/2 - 10, 2, 20);
                }

                g2d.dispose();
            }
        };

        nameField.setPreferredSize(new Dimension(300, 50));
        nameField.setMaximumSize(new Dimension(300, 50));
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        nameField.setOpaque(false);
        nameField.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        nameField.setForeground(TEXT_PRIMARY);
        nameField.setCaretColor(ACCENT_COLOR);
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputPanel.add(nameField);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        loginCard.add(inputPanel);

        // Login button with animation
        JButton loginBtn = new JButton("ENTER ARENA") {
            private float pulse = 0;
            private boolean pulsing = true;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Pulsing animation
                if (pulsing && isEnabled()) {
                    pulse += 0.05f;
                    if (pulse > 1) pulse = 0;
                }

                int alpha = (int)(30 + 20 * Math.sin(pulse * Math.PI));

                // Draw button background
                if (getModel().isPressed()) {
                    g2d.setColor(ACCENT_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(ACCENT_HOVER);
                } else {
                    g2d.setColor(ACCENT_COLOR);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // Draw pulse effect
                if (isEnabled() && pulsing) {
                    g2d.setColor(new Color(0, 150, 255, alpha));
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                }

                // Draw icon
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
                g2d.drawString("⚔️", 25, getHeight() / 2 + 8);

                // Draw text
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
                FontMetrics fm = g2d.getFontMetrics();
                String text = getText();
                int textWidth = fm.stringWidth(text);
                int x = (getWidth() - textWidth) / 2;
                int y = getHeight() / 2 + fm.getAscent() / 2 - 2;

                // Text shadow
                g2d.setColor(new Color(0, 0, 0, 100));
                g2d.drawString(text, x + 1, y + 1);

                // Main text
                g2d.setColor(Color.WHITE);
                g2d.drawString(text, x, y);

                g2d.dispose();

                // Continue animation
                if (isEnabled() && pulsing) {
                    repaint(50);
                }
            }
        };

        loginBtn.setContentAreaFilled(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setFocusPainted(false);
        loginBtn.setPreferredSize(new Dimension(250, 55));
        loginBtn.setMaximumSize(new Dimension(250, 55));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginBtn.addActionListener(e -> {
            String rawName = nameField.getText().trim();

            if (rawName.isEmpty()) {
                // Shake animation for empty field
                Timer shakeTimer = new Timer(20, null);
                final int[] offset = {0};
                final int[] direction = {1};
                shakeTimer.addActionListener(ev -> {
                    offset[0] += 5 * direction[0];
                    if (Math.abs(offset[0]) >= 15) {
                        direction[0] *= -1;
                    }
                    if (offset[0] == 0) {
                        shakeTimer.stop();
                        nameField.setLocation(nameField.getX() - offset[0], nameField.getY());
                    } else {
                        nameField.setLocation(nameField.getX() + 5 * direction[0], nameField.getY());
                    }
                });
                shakeTimer.start();

                // Show error message
                JOptionPane.showMessageDialog(this,
                        "<html><div style='background:#1e1e28;color:#f0f0f0;padding:15px;border-radius:5px;'>" +
                                "<b style='color:#ff5050;font-size:14px;'>INPUT REQUIRED</b><br><br>" +
                                "<span style='color:#b4b4c8;'>Please enter a player name to continue.</span>" +
                                "</div></html>",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            loginBtn.setEnabled(false);
            loginBtn.setText("VERIFYING...");
            loginBtn.repaint();

            String deviceName = getDeviceName();

            new Thread(() -> {
                boolean success = DbCon.loginOrRegister(rawName, deviceName);

                SwingUtilities.invokeLater(() -> {
                    if (success) {
                        UserSession.login(rawName, deviceName);

                        // Success animation
                        loginBtn.setText("✓ WELCOME!");
                        Timer successTimer = new Timer(800, ev -> {
                            manager.showWelcomeScreen();
                        });
                        successTimer.setRepeats(false);
                        successTimer.start();
                    } else {
                        loginBtn.setEnabled(true);
                        loginBtn.setText("ENTER THE GAME");
                        loginBtn.repaint();

                        JOptionPane.showMessageDialog(
                                this,
                                "<html><div style='background:#1e1e28;color:#f0f0f0;padding:15px;border-radius:5px;'>" +
                                        "<b style='color:#ff5050;font-size:14px;'>NAME UNAVAILABLE</b><br><br>" +
                                        "<span style='color:#b4b4c8;'>The name '<font color='#ff8080'>" + rawName +
                                        "</font>' is already taken.</span><br>" +
                                        "<span style='color:#b4b4c8;font-size:11px;'>Please choose another name.</span>" +
                                        "</div></html>",
                                "Registration Failed",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                });
            }).start();
        });

        loginCard.add(loginBtn);

        // Footer text
        JLabel footer = new JLabel("© 2024 Tic Tac Toe Arena");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footer.setForeground(new Color(100, 100, 120));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(Box.createRigidArea(new Dimension(0, 30)));
        loginCard.add(footer);

        mainPanel.add(loginCard);
        add(mainPanel, BorderLayout.CENTER);

        // Set focus to text field
        SwingUtilities.invokeLater(() -> nameField.requestFocusInWindow());
    }

    private String getDeviceName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "UnknownDevice";
        }
    }
}