package tictactoe;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
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

    // Blue-Black Theme Color Palette
    private final Color BG_COLOR = new Color(18, 18, 24);
    private final Color CARD_BG = new Color(30, 30, 40);
    private final Color PRIMARY_COLOR = new Color(0, 150, 255); // Accent Blue
    private final Color SECONDARY_COLOR = new Color(0, 170, 255); // Lighter Blue
    private final Color SUCCESS_COLOR = new Color(46, 204, 113); // Green
    private final Color ERROR_COLOR = new Color(231, 76, 60); // Red
    private final Color TEXT_PRIMARY = new Color(240, 240, 240);
    private final Color TEXT_SECONDARY = new Color(180, 180, 200);
    private final Color BORDER_COLOR = new Color(50, 50, 60);
    private final Color INPUT_BG = new Color(40, 40, 50);
    private final Color HOVER_COLOR = new Color(45, 45, 55);

    // Custom rounded borders
    private final Border ROUNDED_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
    );

    private final int CORNER_RADIUS = 12;

    public SettingsScreen(ScreenManager manager, String username) {
        this.loggedInUsername = username;
        this.settings = new Properties();
        loadSettings();

        setLayout(new BorderLayout());
        setBackground(BG_COLOR);
        setPreferredSize(new Dimension(500, 700));

        // Main container with proper padding
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(BG_COLOR);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        mainContainer.add(createHeaderPanel(), BorderLayout.NORTH);

        // Content panel in scroll pane
        JPanel contentPanel = createContentPanel();
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG_COLOR);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Custom scrollbar
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
        verticalScrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = PRIMARY_COLOR;
                this.trackColor = CARD_BG;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createInvisibleButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createInvisibleButton();
            }

            private JButton createInvisibleButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 4, 4);
                g2.dispose();
            }
        });

        mainContainer.add(scrollPane, BorderLayout.CENTER);
        mainContainer.add(createFooterPanel(manager), BorderLayout.SOUTH);

        add(mainContainer, BorderLayout.CENTER);
    }

    /* ===================== HEADER ===================== */
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Title panel with gradient effect
        JPanel titlePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw rounded background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);

                // Draw subtle shadow
                g2d.setColor(new Color(0, 0, 0, 0.1f));
                g2d.fillRoundRect(0, 1, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);
            }
        };
        titlePanel.setBackground(CARD_BG);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        titlePanel.setPreferredSize(new Dimension(500, 90));

        // Left side: Icon and title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);

        ImageIcon settingsIcon = loadIcon("/tictactoe/img/settings.png", 36, 36);
        JLabel iconLabel = new JLabel(settingsIcon);

        // Title with gradient effect
        JLabel title = new JLabel("Settings") {
            @Override
            public void paint(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Create gradient for title
                GradientPaint gradient = new GradientPaint(
                        0, 0, PRIMARY_COLOR,
                        getWidth() * 0.7f, 0, SECONDARY_COLOR
                );
                g2.setPaint(gradient);
                g2.setFont(getFont());

                FontMetrics fm = g2.getFontMetrics();
                int x = 0;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();

                // Draw text shadow
                g2.setColor(new Color(0, 0, 0, 100));
                g2.drawString(getText(), x + 2, y + 2);

                // Draw main text
                g2.setPaint(gradient);
                g2.drawString(getText(), x, y);
            }
        };
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(PRIMARY_COLOR);

        leftPanel.add(iconLabel);
        leftPanel.add(title);

        // Right side: User info
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setOpaque(false);
        userPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        ImageIcon userIcon = loadIcon("/tictactoe/img/profile.png", 18, 18);
        JLabel userLabel = new JLabel(loggedInUsername);
        userLabel.setIcon(userIcon);
        userLabel.setIconTextGap(10);
        userLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        userLabel.setForeground(TEXT_SECONDARY);

        userPanel.add(userLabel, BorderLayout.EAST);

        titlePanel.add(leftPanel, BorderLayout.WEST);
        titlePanel.add(userPanel, BorderLayout.EAST);

        panel.add(titlePanel, BorderLayout.CENTER);
        return panel;
    }

    /* ===================== CONTENT ===================== */
    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        panel.add(createGeneralSettingsCard());
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(createMatchInfoCard());
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(createMusicSettingsCard());

        return panel;
    }

    /* ===================== GENERAL SETTINGS CARD ===================== */
    private JPanel createGeneralSettingsCard() {
        JPanel card = createStyledCard("General", "gamepad.png");

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(CARD_BG);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 12, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Board Size Label
        JLabel sizeLabel = new JLabel("Board Size");
        sizeLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        sizeLabel.setForeground(TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        content.add(sizeLabel, gbc);

        // Board Size Combo
        String[] sizes = {"3x3 (Default)", "4x4", "5x5", "6x6"};
        boardSizeCombo = new JComboBox<>(sizes);
        boardSizeCombo.setSelectedItem(settings.getProperty("boardSize", "3x3 (Default)"));
        styleComboBox(boardSizeCombo);
        gbc.gridx = 1;
        gbc.weightx = 0.6;
        content.add(boardSizeCombo, gbc);

        // Info text
        JLabel infoLabel = new JLabel("Choose the board size for your game");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoLabel.setForeground(TEXT_SECONDARY);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        content.add(infoLabel, gbc);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    /* ===================== MATCH INFO CARD ===================== */
    private JPanel createMatchInfoCard() {
        JPanel card = createStyledCard("Match Info", "stats.png");

        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(CARD_BG);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 12, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Timer Label
        addLabelField(content, gbc, 0, "Timer", timerIconField = createStyledTextField(settings.getProperty("timerLabel", "Timer")));
        // Moves Label
        addLabelField(content, gbc, 1, "Moves", spotsTakenIconField = createStyledTextField(settings.getProperty("spotsTakenLabel", "Moves")));
        // Your Win Label
        addLabelField(content, gbc, 2, "Your Wins", humanWinIconField = createStyledTextField(settings.getProperty("humanWinLabel", "You")));
        // Bot Win Label
        addLabelField(content, gbc, 3, "Bot Wins", botWinIconField = createStyledTextField(settings.getProperty("botWinLabel", "Bot")));

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private void addLabelField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        label.setForeground(TEXT_PRIMARY);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.4;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        panel.add(field, gbc);
    }

    /* ===================== MUSIC SETTINGS CARD ===================== */
    private JPanel createMusicSettingsCard() {
        JPanel card = createStyledCard("Audio", "music.png");

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(CARD_BG);
        content.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Toggle panel
        JPanel togglePanel = new JPanel(new BorderLayout());
        togglePanel.setBackground(CARD_BG);

        // Left: Label and description
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.setBackground(CARD_BG);
        labelPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel("Background Music");
        titleLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        titleLabel.setForeground(TEXT_PRIMARY);

        JLabel descLabel = new JLabel("Play music during gameplay");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(TEXT_SECONDARY);

        labelPanel.add(titleLabel, BorderLayout.NORTH);
        labelPanel.add(descLabel, BorderLayout.SOUTH);

        // Right: Toggle switch
        JPanel switchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        switchPanel.setBackground(CARD_BG);

        boolean enabled = Boolean.parseBoolean(settings.getProperty("musicEnabled", "true"));
        musicEnabledCheck = createModernToggleSwitch(enabled);

        switchPanel.add(musicEnabledCheck);

        togglePanel.add(labelPanel, BorderLayout.CENTER);
        togglePanel.add(switchPanel, BorderLayout.EAST);

        content.add(togglePanel, BorderLayout.NORTH);
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    /* ===================== FOOTER ===================== */
    private JPanel createFooterPanel(ScreenManager manager) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(BG_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Back Button
        JButton backBtn = createModernButton("← Back", new Color(100, 100, 120));
        backBtn.addActionListener(e -> manager.showWelcomeScreen());

        // Save Button
        JButton saveBtn = createModernButton("Save Changes", PRIMARY_COLOR);
        saveBtn.addActionListener(e -> {
            saveSettings();
            showSuccessMessage();
        });

        buttonPanel.add(backBtn);
        buttonPanel.add(saveBtn);

        panel.add(buttonPanel, BorderLayout.CENTER);
        return panel;
    }

    /* ===================== UI STYLING METHODS ===================== */
    private JPanel createStyledCard(String title, String iconName) {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw rounded dark background
                g2d.setColor(CARD_BG);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);

                // Draw subtle border
                g2d.setColor(BORDER_COLOR);
                g2d.setStroke(new BasicStroke(1));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, CORNER_RADIUS, CORNER_RADIUS);
            }
        };
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        card.setOpaque(false);

        // Card header with icon
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 15, 20));

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

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw background
                g2.setColor(getModel().isPressed() ? bgColor.darker() :
                        getModel().isRollover() ? bgColor.brighter() : bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), CORNER_RADIUS, CORNER_RADIUS);

                // Draw text
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(text, x, y);
            }
        };

        button.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private JTextField createStyledTextField(String text) {
        JTextField field = new JTextField(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw rounded background
                g2.setColor(isEnabled() ? INPUT_BG : new Color(35, 35, 45));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // Draw border
                if (hasFocus()) {
                    g2.setColor(PRIMARY_COLOR);
                    g2.setStroke(new BasicStroke(2));
                    g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 8, 8);
                } else {
                    g2.setColor(BORDER_COLOR);
                    g2.setStroke(new BasicStroke(1));
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                }

                // Draw text
                g2.setColor(TEXT_PRIMARY);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                String displayText = getText();

                if (displayText.isEmpty() && !hasFocus()) {
                    g2.setColor(TEXT_SECONDARY);
                    g2.drawString("Enter text...", 12, getHeight()/2 + fm.getAscent()/2 - 2);
                } else {
                    int y = getHeight()/2 + fm.getAscent()/2 - 2;
                    g2.drawString(displayText, 12, y);
                }
            }
        };

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(PRIMARY_COLOR);
        field.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        field.setOpaque(false);

        return field;
    }

    private JCheckBox createModernToggleSwitch(boolean initialState) {
        JCheckBox checkBox = new JCheckBox() {
            private float animation = initialState ? 1.0f : 0.0f;

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Animate toggle
                if (isSelected() && animation < 1.0f) {
                    animation = Math.min(animation + 0.1f, 1.0f);
                    repaint(20);
                } else if (!isSelected() && animation > 0.0f) {
                    animation = Math.max(animation - 0.1f, 0.0f);
                    repaint(20);
                }

                // Draw track
                int trackWidth = 48;
                int trackHeight = 24;
                int trackX = 0;
                int trackY = (getHeight() - trackHeight) / 2;

                // Track background
                g2.setColor(isSelected() ?
                        new Color(PRIMARY_COLOR.getRed(), PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue(), (int)(200 * animation)) :
                        new Color(60, 60, 70));
                g2.fillRoundRect(trackX, trackY, trackWidth, trackHeight, 12, 12);

                // Draw knob
                int knobSize = 20;
                int knobX = trackX + 2 + (int)(animation * (trackWidth - knobSize - 4));
                int knobY = trackY + 2;

                g2.setColor(Color.WHITE);
                g2.fillOval(knobX, knobY, knobSize, knobSize);

                // Add shadow to knob
                g2.setColor(new Color(0, 0, 0, 30));
                g2.fillOval(knobX + 1, knobY + 1, knobSize - 2, knobSize - 2);
            }
        };

        checkBox.setSelected(initialState);
        checkBox.setBackground(CARD_BG);
        checkBox.setFocusPainted(false);
        checkBox.setContentAreaFilled(false);
        checkBox.setBorderPainted(false);
        checkBox.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkBox.setPreferredSize(new Dimension(48, 30));

        return checkBox;
    }

    private void styleComboBox(JComboBox<String> combo) {
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                label.setForeground(isSelected ? Color.WHITE : TEXT_PRIMARY);
                label.setBackground(isSelected ? PRIMARY_COLOR : INPUT_BG);
                label.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                return label;
            }
        });

        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setBackground(INPUT_BG);
        combo.setForeground(TEXT_PRIMARY);
        combo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        combo.setFocusable(false);
    }

    private ImageIcon loadIcon(String path, int width, int height) {
        try {
            return new ImageIcon(
                    new ImageIcon(getClass().getResource(path))
                            .getImage()
                            .getScaledInstance(width, height, Image.SCALE_SMOOTH)
            );
        } catch (Exception e) {
            System.err.println("Could not load icon: " + path);
            // Create a simple colored circle as fallback
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = img.createGraphics();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(TEXT_SECONDARY);
            g2d.fillOval(0, 0, width, height);
            g2d.dispose();
            return new ImageIcon(img);
        }
    }

    private void showSuccessMessage() {
        JOptionPane.showMessageDialog(this,
                "<html><div style='background:#1e1e28;color:#f0f0f0;padding:15px;border-radius:5px;'>" +
                        "<b style='color:#2ecc71;font-size:14px;'>✓ SETTINGS SAVED</b><br><br>" +
                        "<span style='color:#b4b4c8;'>Your preferences have been updated successfully.</span>" +
                        "</div></html>",
                "Settings Saved",
                JOptionPane.INFORMATION_MESSAGE);
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
                    "<html><div style='background:#1e1e28;color:#f0f0f0;padding:15px;border-radius:5px;'>" +
                            "<b style='color:#ff5050;font-size:14px;'>✗ SAVE FAILED</b><br><br>" +
                            "<span style='color:#b4b4c8;'>Error saving settings: " + e.getMessage() + "</span>" +
                            "</div></html>",
                    "Save Error",
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