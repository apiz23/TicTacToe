package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class GameModeScreen extends JPanel {

    private String selectedMode = "PvP";
    private String selectedDifficulty = "Easy";
    private String player1Username;
    private String player2Username = "";
    private int boardSize = 3;

    private JButton pvpBtn, botBtn;
    private JButton easyBtn, medBtn, hardBtn;
    private JPanel difficultyPanel;
    private JPanel pvpInputPanel;
    private JTextField player2Field;
    private JLabel validationLabel;

    // Dark Theme Color Palette
    private final Color BG_COLOR = new Color(18, 18, 24);
    private final Color CARD_COLOR = new Color(30, 30, 40);
    private final Color ACCENT_COLOR = new Color(0, 150, 255);
    private final Color ACCENT_HOVER = new Color(0, 170, 255);
    private final Color SUCCESS_COLOR = new Color(0, 200, 150);
    private final Color ERROR_COLOR = new Color(255, 80, 80);
    private final Color TEXT_PRIMARY = new Color(240, 240, 240);
    private final Color TEXT_SECONDARY = new Color(180, 180, 200);
    private final Color BORDER_COLOR = new Color(50, 50, 60);
    private final Color INACTIVE_COLOR = new Color(45, 45, 55);

    public GameModeScreen(ScreenManager manager, String loggedInUsername) {
        this.player1Username = loggedInUsername;
        loadSettingsFromFile();

        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Main content with scroll
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG_COLOR);

        // Header with gradient
        contentPanel.add(createHeaderPanel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Player info in a sleek card
        contentPanel.add(createPlayerCard());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Opponent selection
        contentPanel.add(createOpponentSelectionPanel());
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Player 2 input (PvP mode)
        pvpInputPanel = createPvPInputPanel();
        pvpInputPanel.setVisible(selectedMode.equals("PvP"));
        contentPanel.add(pvpInputPanel);

        // Difficulty selection (Bot mode)
        difficultyPanel = createDifficultyPanel();
        difficultyPanel.setVisible(selectedMode.equals("Bot"));
        contentPanel.add(difficultyPanel);

        contentPanel.add(Box.createVerticalGlue());

        // Action buttons at bottom
        contentPanel.add(createActionPanel(manager));

        // Wrap in scroll pane
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG_COLOR);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Custom scrollbar
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setBackground(BG_COLOR);
        vertical.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = ACCENT_COLOR;
                this.trackColor = CARD_COLOR;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(0, 0));
                btn.setMinimumSize(new Dimension(0, 0));
                btn.setMaximumSize(new Dimension(0, 0));
                return btn;
            }
        });

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Create gradient background
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(30, 30, 40),
                        getWidth(), getHeight(), new Color(18, 18, 24)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Add subtle pattern
                g2d.setColor(new Color(255, 255, 255, 5));
                for (int i = 0; i < getWidth(); i += 20) {
                    for (int j = 0; j < getHeight(); j += 20) {
                        g2d.fillRect(i, j, 1, 1);
                    }
                }
            }
        };
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setMaximumSize(new Dimension(500, 180));

        // Icon with glow effect
        JLabel iconLabel = new JLabel("🎮") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

                // Draw glow
                g2d.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
                g2d.setColor(new Color(0, 150, 255, 30));
                for (int i = 0; i < 5; i++) {
                    g2d.drawString("🎮", 2 + i, 48 + i);
                }

                // Draw main icon
                g2d.setColor(Color.WHITE);
                g2d.drawString("🎮", 5, 50);
                g2d.dispose();
            }
        };
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconLabel.setPreferredSize(new Dimension(60, 60));

        JLabel title = new JLabel("GAME SETUP");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Configure your match settings");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(Box.createVerticalGlue());
        header.add(iconLabel);
        header.add(Box.createRigidArea(new Dimension(0, 10)));
        header.add(title);
        header.add(Box.createRigidArea(new Dimension(0, 5)));
        header.add(subtitle);
        header.add(Box.createVerticalGlue());

        return header;
    }

    private JPanel createPlayerCard() {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        card.setMaximumSize(new Dimension(500, 100));

        JLabel playerIcon = new JLabel("👤");
        playerIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        playerIcon.setForeground(ACCENT_COLOR);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(CARD_COLOR);

        JLabel label = new JLabel("CURRENT PLAYER");
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_SECONDARY);

        JLabel nameLabel = new JLabel(player1Username);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        nameLabel.setForeground(TEXT_PRIMARY);

        infoPanel.add(label);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        infoPanel.add(nameLabel);

        JLabel settingsIcon = new JLabel("⚙️");
        settingsIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        settingsIcon.setForeground(TEXT_SECONDARY);

        card.add(playerIcon, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        card.add(settingsIcon, BorderLayout.EAST);

        return card;
    }

    private JPanel createOpponentSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(500, 150));

        JLabel title = new JLabel("SELECT OPPONENT TYPE");
        title.setFont(new Font("Segoe UI", Font.BOLD, 12));
        title.setForeground(TEXT_SECONDARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setMaximumSize(new Dimension(500, 70));

        pvpBtn = createModernToggleButton("👥 PLAYER", selectedMode.equals("PvP"));
        botBtn = createModernToggleButton("🤖 BOT", selectedMode.equals("Bot"));

        pvpBtn.addActionListener(e -> setMode("PvP"));
        botBtn.addActionListener(e -> setMode("Bot"));

        buttonPanel.add(pvpBtn);
        buttonPanel.add(botBtn);

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(buttonPanel);

        return panel;
    }

    private JPanel createPvPInputPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(500, 200));

        JLabel title = new JLabel("ENTER PLAYER 2 USERNAME");
        title.setFont(new Font("Segoe UI", Font.BOLD, 12));
        title.setForeground(TEXT_SECONDARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        player2Field = new JTextField();
        player2Field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        player2Field.setForeground(TEXT_PRIMARY);
        player2Field.setCaretColor(ACCENT_COLOR);
        player2Field.setBackground(new Color(40, 40, 50));
        player2Field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        player2Field.setMaximumSize(new Dimension(500, 50));
        player2Field.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel hint = new JLabel("Player must be registered in the system");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(150, 150, 170));
        hint.setAlignmentX(Component.CENTER_ALIGNMENT);

        validationLabel = new JLabel(" ");
        validationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        validationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(player2Field);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(hint);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(validationLabel);

        return panel;
    }

    private JPanel createDifficultyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(500, 180));

        JLabel title = new JLabel("SELECT DIFFICULTY LEVEL");
        title.setFont(new Font("Segoe UI", Font.BOLD, 12));
        title.setForeground(TEXT_SECONDARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setMaximumSize(new Dimension(500, 60));

        easyBtn = createDifficultyToggleButton("EASY");
        medBtn = createDifficultyToggleButton("MEDIUM");
        hardBtn = createDifficultyToggleButton("HARD");

        easyBtn.addActionListener(e -> setDifficulty("Easy"));
        medBtn.addActionListener(e -> setDifficulty("Medium"));
        hardBtn.addActionListener(e -> setDifficulty("Hard"));

        buttonPanel.add(easyBtn);
        buttonPanel.add(medBtn);
        buttonPanel.add(hardBtn);

        // Difficulty description
        JPanel descPanel = new JPanel();
        descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
        descPanel.setBackground(CARD_COLOR);
        descPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        descPanel.setMaximumSize(new Dimension(500, 80));
        descPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel diffTitle = new JLabel(getDifficultyTitle());
        diffTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        diffTitle.setForeground(ACCENT_COLOR);
        diffTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel diffDesc = new JLabel(getDifficultyDescription());
        diffDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        diffDesc.setForeground(TEXT_SECONDARY);
        diffDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        descPanel.add(diffTitle);
        descPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        descPanel.add(diffDesc);

        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(buttonPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(descPanel);

        return panel;
    }

    private JPanel createActionPanel(ScreenManager manager) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setMaximumSize(new Dimension(500, 150));

        // Start Game Button
        JButton startBtn = new JButton("LAUNCH GAME") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(ACCENT_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(ACCENT_HOVER);
                } else {
                    g2.setColor(ACCENT_COLOR);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

                // Draw icon
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
                g2.drawString("🚀", 20, getHeight() / 2 + 6);

                // Draw text
                g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                String text = "LAUNCH GAME";
                int textWidth = fm.stringWidth(text);
                g2.drawString(text, (getWidth() - textWidth) / 2, getHeight() / 2 + 6);

                g2.dispose();
            }
        };

        startBtn.setContentAreaFilled(false);
        startBtn.setBorderPainted(false);
        startBtn.setFocusPainted(false);
        startBtn.setForeground(Color.WHITE);
        startBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        startBtn.setMaximumSize(new Dimension(400, 50));
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        startBtn.addActionListener(e -> handleStartGame(manager, startBtn));

        // Back Button
        JButton backBtn = new JButton("RETURN TO MENU");
        backBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        backBtn.setForeground(TEXT_SECONDARY);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setFocusPainted(false);
        backBtn.setMaximumSize(new Dimension(200, 40));
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backBtn.setForeground(ACCENT_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                backBtn.setForeground(TEXT_SECONDARY);
            }
        });

        backBtn.addActionListener(e -> manager.showWelcomeScreen());

        panel.add(startBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(backBtn);

        return panel;
    }

    private JButton createModernToggleButton(String text, boolean isActive) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Check if this button represents the currently selected mode
                boolean isCurrentlyActive = text.contains("PLAYER") ? selectedMode.equals("PvP") : selectedMode.equals("Bot");

                if (isCurrentlyActive) {
                    g2.setColor(ACCENT_COLOR);
                } else if (getModel().isRollover()) {
                    g2.setColor(INACTIVE_COLOR.brighter());
                } else {
                    g2.setColor(INACTIVE_COLOR);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                // Draw border
                if (isCurrentlyActive) {
                    g2.setColor(ACCENT_COLOR.brighter());
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 15, 15);
                }

                // Draw text
                g2.setColor(isCurrentlyActive ? Color.WHITE : TEXT_SECONDARY);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                String buttonText = getText();
                int textWidth = fm.stringWidth(buttonText);
                g2.drawString(buttonText, (getWidth() - textWidth) / 2, getHeight() / 2 + fm.getAscent() / 2 - 2);

                g2.dispose();
            }
        };

        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(200, 50));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return btn;
    }

    private JButton createDifficultyToggleButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Check if this button represents the currently selected difficulty
                boolean isCurrentlyActive = text.equalsIgnoreCase(selectedDifficulty);

                if (isCurrentlyActive) {
                    g2.setColor(ACCENT_COLOR);
                } else if (getModel().isRollover()) {
                    g2.setColor(INACTIVE_COLOR.brighter());
                } else {
                    g2.setColor(INACTIVE_COLOR);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Draw text
                g2.setColor(isCurrentlyActive ? Color.WHITE : TEXT_SECONDARY);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                String buttonText = getText();
                int textWidth = fm.stringWidth(buttonText);
                g2.drawString(buttonText, (getWidth() - textWidth) / 2, getHeight() / 2 + fm.getAscent() / 2 - 2);

                g2.dispose();
            }
        };

        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(100, 40));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return btn;
    }

    private String getDifficultyTitle() {
        switch(selectedDifficulty) {
            case "Easy": return "Beginner Friendly";
            case "Medium": return "Balanced Challenge";
            case "Hard": return "Expert Level";
            default: return "";
        }
    }

    private String getDifficultyDescription() {
        switch(selectedDifficulty) {
            case "Easy": return "AI makes occasional strategic moves";
            case "Medium": return "AI uses basic game theory and strategies";
            case "Hard": return "AI employs advanced algorithms and prediction";
            default: return "";
        }
    }

    private void setMode(String mode) {
        this.selectedMode = mode;

        // Update button appearances
        pvpBtn.repaint();
        botBtn.repaint();

        pvpInputPanel.setVisible(mode.equals("PvP"));
        difficultyPanel.setVisible(mode.equals("Bot"));

        showValidationMessage(" ", TEXT_SECONDARY);
        revalidate();
        repaint();
    }

    private void setDifficulty(String diff) {
        this.selectedDifficulty = diff;

        // Update difficulty button appearances
        easyBtn.repaint();
        medBtn.repaint();
        hardBtn.repaint();

        // Update difficulty panel description
        if (difficultyPanel.isVisible()) {
            Component[] comps = difficultyPanel.getComponents();
            for (Component comp : comps) {
                if (comp instanceof JPanel) {
                    JPanel panel = (JPanel) comp;
                    Component[] inner = panel.getComponents();
                    for (Component c : inner) {
                        if (c instanceof JLabel) {
                            JLabel label = (JLabel) c;
                            if (label.getFont().getSize() == 14 && label.getFont().getStyle() == Font.BOLD) {
                                label.setText(getDifficultyTitle());
                            } else if (label.getFont().getSize() == 12 && label.getFont().getStyle() == Font.PLAIN) {
                                label.setText(getDifficultyDescription());
                            }
                        }
                    }
                }
            }
        }
    }

    private void handleStartGame(ScreenManager manager, JButton startBtn) {
        if (selectedMode.equals("PvP")) {
            player2Username = player2Field.getText().trim();

            if (player2Username.isEmpty()) {
                showValidationMessage("Please enter Player 2 username", ERROR_COLOR);
                return;
            }

            if (player2Username.equalsIgnoreCase(player1Username)) {
                showValidationMessage("Player 2 must be different", ERROR_COLOR);
                return;
            }

            startBtn.setEnabled(false);
            startBtn.setText("VERIFYING...");
            showValidationMessage("Verifying user credentials...", new Color(255, 200, 0));

            new Thread(() -> {
                boolean exists = DbCon.userExists(player2Username);

                SwingUtilities.invokeLater(() -> {
                    startBtn.setEnabled(true);
                    startBtn.setText("LAUNCH GAME");
                    startBtn.repaint();

                    if (exists) {
                        showValidationMessage("✓ User verified successfully", SUCCESS_COLOR);

                        Timer timer = new Timer(1000, evt -> {
                            manager.showGameBoard(selectedMode, selectedDifficulty,
                                    player1Username, player2Username, boardSize);
                        });
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        showValidationMessage("✗ User not found in database", ERROR_COLOR);
                        JOptionPane.showMessageDialog(this,
                                "<html><div style='background:#1e1e28;color:#f0f0f0;padding:15px;border-radius:5px;'>" +
                                        "<b style='color:#ff5050;font-size:14px;'>USER NOT FOUND</b><br><br>" +
                                        "<span style='color:#b4b4c8;'>Player '<font color='#ff8080'>" + player2Username +
                                        "</font>' is not registered.</span><br>" +
                                        "<span style='color:#b4b4c8;font-size:11px;'>Please ask them to create an account first.</span>" +
                                        "</div></html>",
                                "Verification Failed",
                                JOptionPane.ERROR_MESSAGE);
                    }
                });
            }).start();
        } else {
            manager.showGameBoard(selectedMode, selectedDifficulty,
                    player1Username, player2Username, boardSize);
        }
    }

    private void showValidationMessage(String message, Color color) {
        validationLabel.setText(message);
        validationLabel.setForeground(color);
    }

    private void loadSettingsFromFile() {
        try {
            Properties settings = SettingsScreen.getSettings();
            String savedMode = settings.getProperty("gameMode", "Singleplayer (vs Bot)");
            selectedMode = savedMode.contains("Multiplayer") ? "PvP" : "Bot";
            selectedDifficulty = settings.getProperty("difficulty", "Easy");
            String boardSizeStr = settings.getProperty("boardSize", "3x3 (Default)");
            boardSize = Integer.parseInt(boardSizeStr.substring(0, 1));
        } catch (Exception e) {
            selectedMode = "PvP";
            selectedDifficulty = "Easy";
            boardSize = 3;
        }
    }
}