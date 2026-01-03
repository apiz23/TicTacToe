package tictactoe;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Properties;

public class SettingsScreen extends JPanel {

    private String loggedInUsername;
    private Properties settings;
    private static final String SETTINGS_FILE = "game_settings.properties";

    // UI Components
    private JComboBox<String> boardSizeCombo;
    private JTextField timerIconField;
    private JTextField spotsTakenIconField;
    private JTextField humanWinIconField;
    private JTextField botWinIconField;
    private JCheckBox musicEnabledCheck;

    // Modern Color Palette
    private final Color BG_COLOR = new Color(248, 250, 252);
    private final Color CARD_BG = Color.WHITE;
    private final Color PRIMARY_COLOR = new Color(41, 128, 185); // Accent Blue from your code
    private final Color SUCCESS_COLOR = new Color(46, 204, 113); // Accent Green from your code
    private final Color TEXT_PRIMARY = new Color(52, 73, 94); // Dark blue-gray
    private final Color TEXT_SECONDARY = new Color(127, 140, 141); // Gray
    private final Color BORDER_COLOR = new Color(226, 232, 240);
    private final Color INPUT_BG = new Color(249, 250, 251);

    public SettingsScreen(ScreenManager manager, String username) {
        this.loggedInUsername = username;
        this.settings = new Properties();
        loadSettings();

        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Main container with proper padding
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(BG_COLOR);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        mainContainer.add(createHeaderPanel(), BorderLayout.NORTH);

        // Content panel in scroll pane
        JPanel contentPanel = createContentPanel();
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainContainer.add(scrollPane, BorderLayout.CENTER);

        mainContainer.add(createFooterPanel(manager), BorderLayout.SOUTH);

        add(mainContainer, BorderLayout.CENTER);
    }

    /* ===================== ICON LOADER ===================== */
    private ImageIcon loadIcon(String path, int width, int height) {
        try {
            return new ImageIcon(
                    new ImageIcon(getClass().getResource(path))
                            .getImage()
                            .getScaledInstance(width, height, Image.SCALE_SMOOTH)
            );
        } catch (Exception e) {
            System.err.println("Could not load icon: " + path);
            return createFallbackIcon(width, height);
        }
    }

    private ImageIcon createFallbackIcon(int width, int height) {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(TEXT_SECONDARY);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
        return new ImageIcon(img);
    }

    /* ===================== HEADER ===================== */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 20, 30));

        // Title with icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(BG_COLOR);

        ImageIcon settingsIcon = loadIcon("/tictactoe/img/settings.png", 32, 32);
        JLabel iconLabel = new JLabel(settingsIcon);

        JLabel title = new JLabel("Game Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);

        titlePanel.add(iconLabel);
        titlePanel.add(title);

        // User info panel
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(BG_COLOR);
        userPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        ImageIcon userIcon = loadIcon("/tictactoe/img/profile.png", 16, 16);
        JLabel userLabel = new JLabel(loggedInUsername);
        userLabel.setIcon(userIcon);
        userLabel.setIconTextGap(8);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(TEXT_SECONDARY);

        userPanel.add(userLabel, BorderLayout.WEST);

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(BG_COLOR);
        leftPanel.add(titlePanel, BorderLayout.NORTH);
        leftPanel.add(userPanel, BorderLayout.SOUTH);

        panel.add(leftPanel, BorderLayout.WEST);

        // Add decorative separator
        JSeparator separator = new JSeparator();
        separator.setForeground(BORDER_COLOR);
        panel.add(separator, BorderLayout.SOUTH);

        return panel;
    }

    /* ===================== CONTENT ===================== */
    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 30, 20, 30));

        panel.add(createGeneralSettingsCard());
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(createMatchInfoCard());
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(createMusicSettingsCard());

        return panel;
    }

    /* ===================== GENERAL SETTINGS CARD ===================== */
    private JPanel createGeneralSettingsCard() {
        JPanel card = createStyledCard("General Settings", "gamepad.png");

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(CARD_BG);
        content.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Board Size Label
        JLabel sizeLabel = new JLabel("Board Size:");
        sizeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        sizeLabel.setForeground(TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        content.add(sizeLabel, gbc);

        // Board Size Combo
        String[] sizes = {"3x3 (Default)", "4x4", "5x5", "6x6"};
        boardSizeCombo = new JComboBox<>(sizes);
        boardSizeCombo.setSelectedItem(settings.getProperty("boardSize", "3x3 (Default)"));
        styleComboBox(boardSizeCombo);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        content.add(boardSizeCombo, gbc);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    /* ===================== MATCH INFO CARD ===================== */
    private JPanel createMatchInfoCard() {
        JPanel card = createStyledCard("Match Info Labels", "stats.png");

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(CARD_BG);
        content.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 15);
        gbc.anchor = GridBagConstraints.WEST;

        // Timer Label
        addLabelField(content, gbc, 0, "Timer Label:", timerIconField = createStyledTextField(settings.getProperty("timerLabel", "Timer")));
        // Spots Taken Label
        addLabelField(content, gbc, 1, "Moves Label:", spotsTakenIconField = createStyledTextField(settings.getProperty("spotsTakenLabel", "Moves")));
        // Human Win Label
        addLabelField(content, gbc, 2, "Your Win Label:", humanWinIconField = createStyledTextField(settings.getProperty("humanWinLabel", "You")));
        // Bot Win Label
        addLabelField(content, gbc, 3, "Bot Win Label:", botWinIconField = createStyledTextField(settings.getProperty("botWinLabel", "Bot")));

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private void addLabelField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_PRIMARY);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    /* ===================== MUSIC SETTINGS CARD ===================== */
    private JPanel createMusicSettingsCard() {
        JPanel card = createStyledCard("Audio Settings", "music.png");

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(CARD_BG);
        content.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));

        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        togglePanel.setBackground(CARD_BG);

        // Music toggle switch
        musicEnabledCheck = new JCheckBox("Enable Background Music");
        musicEnabledCheck.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        musicEnabledCheck.setBackground(CARD_BG);
        musicEnabledCheck.setFocusPainted(false);
        musicEnabledCheck.setBorderPainted(false);
        musicEnabledCheck.setContentAreaFilled(false);
        musicEnabledCheck.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Load switch icons
        ImageIcon onIcon = loadIcon("/tictactoe/img/switch_on.png", 48, 24);
        ImageIcon offIcon = loadIcon("/tictactoe/img/switch_off.png", 48, 24);

        musicEnabledCheck.setIcon(offIcon);
        musicEnabledCheck.setSelectedIcon(onIcon);

        boolean enabled = Boolean.parseBoolean(settings.getProperty("musicEnabled", "true"));
        musicEnabledCheck.setSelected(enabled);

        togglePanel.add(musicEnabledCheck);

        // Status indicator
        JLabel statusLabel = new JLabel(enabled ? "• Music is ON" : "• Music is OFF");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        statusLabel.setForeground(enabled ? SUCCESS_COLOR : TEXT_SECONDARY);
        togglePanel.add(statusLabel);

        // Update status when toggle changes
        musicEnabledCheck.addActionListener(e -> {
            boolean isSelected = musicEnabledCheck.isSelected();
            statusLabel.setText(isSelected ? "• Music is ON" : "• Music is OFF");
            statusLabel.setForeground(isSelected ? SUCCESS_COLOR : TEXT_SECONDARY);
        });

        content.add(togglePanel, BorderLayout.NORTH);

        // Note label
        JLabel note = new JLabel("Background music will play during gameplay sessions");
        note.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        note.setForeground(TEXT_SECONDARY);
        note.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        content.add(note, BorderLayout.SOUTH);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    /* ===================== FOOTER ===================== */
    private JPanel createFooterPanel(ScreenManager manager) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));

        // Add separator
        JSeparator separator = new JSeparator();
        separator.setForeground(BORDER_COLOR);
        panel.add(separator, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        buttonPanel.setBackground(BG_COLOR);

        // Back Button
        ImageIcon backIcon = loadIcon("/tictactoe/img/back.png", 16, 16);
        JButton backBtn = createStyledButton("Back", TEXT_SECONDARY, backIcon);
        backBtn.addActionListener(e -> manager.showWelcomeScreen());

        // Save Button
        ImageIcon saveIcon = loadIcon("/tictactoe/img/save.png", 16, 16);
        JButton saveBtn = createStyledButton("Save Changes", PRIMARY_COLOR, saveIcon);
        saveBtn.addActionListener(e -> {
            saveSettings();
            JOptionPane.showMessageDialog(this,
                    "Settings saved successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        buttonPanel.add(backBtn);
        buttonPanel.add(saveBtn);

        panel.add(buttonPanel, BorderLayout.EAST);
        return panel;
    }

    /* ===================== UI STYLING METHODS ===================== */
    private JPanel createStyledCard(String title, String iconName) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD_BG);

        // Card with shadow effect
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        BorderFactory.createEmptyBorder(1, 1, 1, 1)
                ),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        // Card header with icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titlePanel.setBackground(CARD_BG);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        ImageIcon icon = loadIcon("/tictactoe/img/" + iconName, 20, 20);
        JLabel iconLabel = new JLabel(icon);

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
        titleLabel.setForeground(TEXT_PRIMARY);

        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);

        card.add(titlePanel, BorderLayout.NORTH);

        return card;
    }

    private JButton createStyledButton(String text, Color bgColor, ImageIcon icon) {
        JButton button = new JButton(text);
        if (icon != null) {
            button.setIcon(icon);
            button.setIconTextGap(8);
        }
        button.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private JTextField createStyledTextField(String text) {
        JTextField field = new JTextField(text);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        field.setBackground(INPUT_BG);
        field.setForeground(TEXT_PRIMARY);
        return field;
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(INPUT_BG);
        combo.setForeground(TEXT_PRIMARY);
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                return label;
            }
        });
    }

    /* ===================== SETTINGS IO ===================== */
    private void loadSettings() {
        try {
            File file = new File(SETTINGS_FILE);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                settings.load(fis);
                fis.close();
            }
        } catch (Exception e) {
            System.err.println("Error loading settings: " + e.getMessage());
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
            settings.store(fos, "Game Settings");
            fos.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving settings: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static Properties getSettings() {
        Properties props = new Properties();
        try {
            File file = new File(SETTINGS_FILE);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                props.load(fis);
                fis.close();
            } else {
                // Default values
                props.setProperty("boardSize", "3x3 (Default)");
                props.setProperty("timerLabel", "Timer");
                props.setProperty("spotsTakenLabel", "Moves");
                props.setProperty("humanWinLabel", "You");
                props.setProperty("botWinLabel", "Bot");
                props.setProperty("musicEnabled", "true");
            }
        } catch (IOException e) {
            System.err.println("Failed to load settings: " + e.getMessage());
        }
        return props;
    }
}