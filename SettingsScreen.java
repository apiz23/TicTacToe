package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Properties;

public class SettingsScreen extends JPanel {

    private String loggedInUsername;
    private Properties settings;
    private static final String SETTINGS_FILE = "game_settings.properties";

    // General Settings
    private JComboBox<String> boardSizeCombo;

    // Match Info Settings (Icon labels)
    private JTextField timerIconField;
    private JTextField spotsTakenIconField;
    private JTextField humanWinIconField;
    private JTextField botWinIconField;

    // Music Settings
    private JCheckBox musicEnabledCheck;

    // Colors
    private final Color BG_COLOR = new Color(245, 247, 250);
    private final Color ACCENT_BLUE = new Color(41, 128, 185);
    private final Color ACCENT_GREEN = new Color(46, 204, 113);
    private final Color ACCENT_ORANGE = new Color(230, 126, 34);
    private final Color CARD_BG = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(52, 73, 94);
    private final Color TEXT_SECONDARY = new Color(127, 140, 141);

    public SettingsScreen(ScreenManager manager, String username) {
        this.loggedInUsername = username;
        this.settings = new Properties();
        loadSettings();

        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        setPreferredSize(new Dimension(650, 700));

        // Title Panel
        add(createTitlePanel(), BorderLayout.NORTH);

        // Main Content (Scrollable)
        JScrollPane scrollPane = new JScrollPane(createContentPanel());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom Buttons
        add(createBottomPanel(manager), BorderLayout.SOUTH);
    }

    /* ===================== TITLE PANEL ===================== */

    private JPanel createTitlePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 15, 25));
        panel.setPreferredSize(new Dimension(650, 80));

        JLabel title = new JLabel("⚙️ Game Settings", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(TEXT_PRIMARY);

        // User info
        JLabel userLabel = new JLabel("User: " + loggedInUsername, SwingConstants.RIGHT);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(TEXT_SECONDARY);

        panel.add(title, BorderLayout.CENTER);
        panel.add(userLabel, BorderLayout.EAST);

        return panel;
    }

    /* ===================== CONTENT PANEL ===================== */

    private JPanel createContentPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 20, 25));

        // Section 1: General Settings
        mainPanel.add(createGeneralSettingsCard());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Section 2: Match Info Settings
        mainPanel.add(createMatchInfoCard());
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Section 3: Music Settings
        mainPanel.add(createMusicSettingsCard());
        mainPanel.add(Box.createVerticalGlue());

        return mainPanel;
    }

    /* ===================== GENERAL SETTINGS CARD ===================== */

    private JPanel createGeneralSettingsCard() {
        JPanel card = createCard("🎮 General Game Settings");

        // Info label
        JLabel infoLabel = new JLabel("<html><i>Note: Game mode and difficulty are selected during game setup</i></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(TEXT_SECONDARY);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(infoLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // Board Size with better layout
        JPanel sizePanel = new JPanel();
        sizePanel.setLayout(new BoxLayout(sizePanel, BoxLayout.Y_AXIS));
        sizePanel.setBackground(CARD_BG);
        sizePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sizeLabel = createCardLabel("Board Size:");
        sizeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sizePanel.add(sizeLabel);
        sizePanel.add(Box.createRigidArea(new Dimension(0, 8)));

        String[] sizes = {"3x3 (Default)", "4x4", "5x5", "6x6"};
        boardSizeCombo = new JComboBox<>(sizes);
        boardSizeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        boardSizeCombo.setMaximumSize(new Dimension(250, 35));
        boardSizeCombo.setPreferredSize(new Dimension(250, 35));
        boardSizeCombo.setBackground(Color.WHITE);
        boardSizeCombo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        String savedSize = settings.getProperty("boardSize", "3x3 (Default)");
        boardSizeCombo.setSelectedItem(savedSize);

        sizePanel.add(boardSizeCombo);
        card.add(sizePanel);

        // Board size description
        JLabel descLabel = new JLabel("Applies to all game modes (PvP and vs Bot)");
        descLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        descLabel.setForeground(TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(descLabel);

        return card;
    }

    /* ===================== MATCH INFO CARD ===================== */

    private JPanel createMatchInfoCard() {
        JPanel card = createCard("📊 Match Info Labels");

        JLabel infoLabel = new JLabel("Customize icon labels for the game board display:");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(TEXT_SECONDARY);
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(infoLabel);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        // Create a panel for all text fields to ensure consistent width
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBackground(CARD_BG);
        fieldsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Timer Icon Label
        fieldsPanel.add(createEnhancedFieldRow("⏱️ Timer Label:", "timerLabel", "Timer"));
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Spots Taken Icon Label
        fieldsPanel.add(createEnhancedFieldRow("🎯 Spots Taken Label:", "spotsTakenLabel", "Moves"));
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Human Win Icon Label
        fieldsPanel.add(createEnhancedFieldRow("👤 Human Win Label:", "humanWinLabel", "You"));
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Bot Win Icon Label
        fieldsPanel.add(createEnhancedFieldRow("🤖 Bot Win Label:", "botWinLabel", "Bot"));

        card.add(fieldsPanel);
        return card;
    }

    /* ===================== MUSIC SETTINGS CARD ===================== */

    private JPanel createMusicSettingsCard() {
        JPanel card = createCard("🎵 Audio Settings");

        // Create a panel for the checkbox with better styling
        JPanel musicPanel = new JPanel();
        musicPanel.setLayout(new BoxLayout(musicPanel, BoxLayout.Y_AXIS));
        musicPanel.setBackground(CARD_BG);
        musicPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        musicEnabledCheck = new JCheckBox("Enable Background Music");
        musicEnabledCheck.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        musicEnabledCheck.setBackground(CARD_BG);
        musicEnabledCheck.setFocusPainted(false);
        musicEnabledCheck.setAlignmentX(Component.LEFT_ALIGNMENT);

        boolean musicEnabled = Boolean.parseBoolean(settings.getProperty("musicEnabled", "true"));
        musicEnabledCheck.setSelected(musicEnabled);

        // Style the checkbox
        musicEnabledCheck.setIcon(new javax.swing.ImageIcon()); // Clear default
        musicEnabledCheck.setSelectedIcon(new javax.swing.ImageIcon()); // Clear default
        musicEnabledCheck.setUI(new javax.swing.plaf.basic.BasicCheckBoxUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                super.paint(g, c);
                if (musicEnabledCheck.isSelected()) {
                    g.setColor(ACCENT_GREEN);
                    g.fillRoundRect(0, 2, 20, 20, 4, 4);
                    g.setColor(Color.WHITE);
                    g.fillPolygon(new int[] {5, 9, 15}, new int[] {10, 16, 5}, 3);
                } else {
                    g.setColor(new Color(220, 220, 220));
                    g.fillRoundRect(0, 2, 20, 20, 4, 4);
                }
                g.setColor(new Color(180, 180, 180));
                g.drawRoundRect(0, 2, 20, 20, 4, 4);
            }
        });

        musicPanel.add(musicEnabledCheck);

        JLabel noteLabel = new JLabel("Music will play during gameplay sessions");
        noteLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        noteLabel.setForeground(TEXT_SECONDARY);
        noteLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        musicPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        musicPanel.add(noteLabel);

        card.add(musicPanel);
        return card;
    }

    /* ===================== BOTTOM PANEL ===================== */

    private JPanel createBottomPanel(ScreenManager manager) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 25, 20, 25));
        panel.setPreferredSize(new Dimension(650, 90));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 5, 0, 5);
        gbc.weightx = 1.0;

        // Save Button
        JButton saveBtn = createStyledButton("💾 Save Settings", ACCENT_GREEN);
        saveBtn.addActionListener(e -> {
            saveSettings();
            showToastMessage("Settings saved successfully!");
        });

        // Reset Button
        JButton resetBtn = createStyledButton("🔄 Reset Defaults", ACCENT_ORANGE);
        resetBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "<html><div style='text-align: center; width: 250px;'>"
                            + "Reset all settings to default values?<br>"
                            + "<small>This cannot be undone.</small>"
                            + "</div></html>",
                    "Confirm Reset",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                resetSettings();
                showToastMessage("Settings reset to default!");
            }
        });

        // Back Button
        JButton backBtn = createStyledButton("← Back to Menu", ACCENT_BLUE);
        backBtn.addActionListener(e -> manager.showWelcomeScreen());

        // Layout buttons in grid
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(saveBtn, gbc);

        gbc.gridx = 1;
        panel.add(resetBtn, gbc);

        gbc.gridx = 2;
        panel.add(backBtn, gbc);

        return panel;
    }

    /* ===================== HELPER METHODS ===================== */

    private JPanel createCard(String title) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(225, 230, 235), 1, true),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));

        JLabel cardTitle = new JLabel(title);
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        cardTitle.setForeground(TEXT_PRIMARY);
        cardTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(cardTitle);
        card.add(Box.createRigidArea(new Dimension(0, 15)));

        return card;
    }

    private JLabel createCardLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createEnhancedFieldRow(String labelText, String settingKey, String defaultValue) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(CARD_BG);
        row.setMaximumSize(new Dimension(550, 50));

        // Label with icon
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(TEXT_PRIMARY);

        // Text field with better styling
        JTextField field = new JTextField(settings.getProperty(settingKey, defaultValue));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(200, 35));
        field.setMaximumSize(new Dimension(200, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        // Store reference to the field
        switch(settingKey) {
            case "timerLabel": timerIconField = field; break;
            case "spotsTakenLabel": spotsTakenIconField = field; break;
            case "humanWinLabel": humanWinIconField = field; break;
            case "botWinLabel": botWinIconField = field; break;
        }

        row.add(label, BorderLayout.WEST);
        row.add(field, BorderLayout.EAST);

        return row;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(180, 42));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        return button;
    }

    private void showToastMessage(String message) {
        JOptionPane.showMessageDialog(this,
                "<html><div style='text-align: center; padding: 10px;'>"
                        + "<b>" + message + "</b>"
                        + "</div></html>",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /* ===================== SETTINGS PERSISTENCE ===================== */

    private void loadSettings() {
        try {
            File file = new File(SETTINGS_FILE);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                settings.load(fis);
                fis.close();
            } else {
                setDefaultSettings();
            }
        } catch (IOException e) {
            System.err.println("Error loading settings: " + e.getMessage());
            setDefaultSettings();
        }
    }

    private void saveSettings() {
        try {
            settings.setProperty("boardSize", boardSizeCombo.getSelectedItem().toString());
            settings.setProperty("timerLabel", timerIconField.getText().trim());
            settings.setProperty("spotsTakenLabel", spotsTakenIconField.getText().trim());
            settings.setProperty("humanWinLabel", humanWinIconField.getText().trim());
            settings.setProperty("botWinLabel", botWinIconField.getText().trim());
            settings.setProperty("musicEnabled", String.valueOf(musicEnabledCheck.isSelected()));

            FileOutputStream fos = new FileOutputStream(SETTINGS_FILE);
            settings.store(fos, "Tic-Tac-Toe Game Settings");
            fos.close();

        } catch (IOException e) {
            System.err.println("Error saving settings: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "<html><div style='text-align: center;'>"
                            + "<b>Failed to save settings</b><br>"
                            + "<small>" + e.getMessage() + "</small>"
                            + "</div></html>",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetSettings() {
        setDefaultSettings();

        // Update UI
        boardSizeCombo.setSelectedItem(settings.getProperty("boardSize"));
        timerIconField.setText(settings.getProperty("timerLabel"));
        spotsTakenIconField.setText(settings.getProperty("spotsTakenLabel"));
        humanWinIconField.setText(settings.getProperty("humanWinLabel"));
        botWinIconField.setText(settings.getProperty("botWinLabel"));
        musicEnabledCheck.setSelected(Boolean.parseBoolean(settings.getProperty("musicEnabled")));

        saveSettings();
    }

    private void setDefaultSettings() {
        settings.setProperty("boardSize", "3x3 (Default)");
        settings.setProperty("timerLabel", "Timer");
        settings.setProperty("spotsTakenLabel", "Moves");
        settings.setProperty("humanWinLabel", "You");
        settings.setProperty("botWinLabel", "Bot");
        settings.setProperty("musicEnabled", "true");
    }

    /* ===================== PUBLIC GETTERS ===================== */

    public static Properties getSettings() {
        Properties props = new Properties();
        try {
            File file = new File(SETTINGS_FILE);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                props.load(fis);
                fis.close();
            }
        } catch (IOException e) {
            System.err.println("Error loading settings: " + e.getMessage());
        }
        return props;
    }
}