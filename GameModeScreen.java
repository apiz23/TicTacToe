package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class GameModeScreen extends JPanel {

    private String selectedMode = "PvP";
    private String selectedDifficulty = "Easy";
    private String player1Username;
    private String player2Username = "";
    private int boardSize = 3; // Default

    private JButton pvpBtn, botBtn;
    private JButton easyBtn, medBtn, hardBtn;
    private JPanel difficultyPanel;
    private JPanel pvpInputPanel;
    private JTextField player2Field;
    private JLabel player1Label;
    private JLabel validationLabel;
    private JLabel boardSizeLabel;

    private final Color BG_COLOR = new Color(240, 240, 245);
    private final Color ACTIVE_COLOR = new Color(70, 130, 180);
    private final Color INACTIVE_COLOR = new Color(200, 200, 200);

    public GameModeScreen(ScreenManager manager, String loggedInUsername) {
        this.player1Username = loggedInUsername;

        // Load settings from file
        loadSettingsFromFile();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // --- TITLE ---
        JLabel title = new JLabel("Game Setup");
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createRigidArea(new Dimension(0, 20)));

        // --- SHOW LOGGED IN PLAYER ---
        player1Label = new JLabel("Player 1: " + player1Username);
        player1Label.setFont(new Font("SansSerif", Font.PLAIN, 16));
        player1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        player1Label.setForeground(new Color(70, 130, 180));
        add(player1Label);
        add(Box.createRigidArea(new Dimension(0, 10)));

        // --- BOARD SIZE INFO ---
        boardSizeLabel = new JLabel("Board Size: " + boardSize + "x" + boardSize + " (from Settings)");
        boardSizeLabel.setFont(new Font("SansSerif", Font.ITALIC, 14));
        boardSizeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        boardSizeLabel.setForeground(new Color(100, 100, 100));
        add(boardSizeLabel);
        add(Box.createRigidArea(new Dimension(0, 30)));

        // --- OPPONENT SELECTION ---
        add(createLabel("Select Opponent"));
        add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel opponentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        opponentPanel.setBackground(BG_COLOR);

        pvpBtn = createToggleButton("Player vs Player", selectedMode.equals("PvP"));
        botBtn = createToggleButton("Player vs Bot", selectedMode.equals("Bot"));

        pvpBtn.addActionListener(e -> setMode("PvP"));
        botBtn.addActionListener(e -> setMode("Bot"));

        opponentPanel.add(pvpBtn);
        opponentPanel.add(botBtn);
        add(opponentPanel);

        add(Box.createRigidArea(new Dimension(0, 30)));

        // --- PLAYER 2 INPUT PANEL (For PvP Mode) ---
        pvpInputPanel = new JPanel();
        pvpInputPanel.setLayout(new BoxLayout(pvpInputPanel, BoxLayout.Y_AXIS));
        pvpInputPanel.setBackground(BG_COLOR);

        JLabel player2Label = createLabel("Enter Player 2 Username");
        pvpInputPanel.add(player2Label);
        pvpInputPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        player2Field = new JTextField(15);
        player2Field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        player2Field.setMaximumSize(new Dimension(300, 35));
        player2Field.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2Field.setHorizontalAlignment(JTextField.CENTER);
        pvpInputPanel.add(player2Field);

        JLabel infoLabel = new JLabel("(Player 2 must be registered)");
        infoLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pvpInputPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        pvpInputPanel.add(infoLabel);

        validationLabel = new JLabel("");
        validationLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        validationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pvpInputPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        pvpInputPanel.add(validationLabel);

        pvpInputPanel.setVisible(selectedMode.equals("PvP"));
        add(pvpInputPanel);

        add(Box.createRigidArea(new Dimension(0, 30)));

        // --- DIFFICULTY SELECTION ---
        difficultyPanel = new JPanel();
        difficultyPanel.setLayout(new BoxLayout(difficultyPanel, BoxLayout.Y_AXIS));
        difficultyPanel.setBackground(BG_COLOR);

        difficultyPanel.add(createLabel("Select Difficulty"));
        difficultyPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel diffButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        diffButtons.setBackground(BG_COLOR);

        easyBtn = createToggleButton("Easy", selectedDifficulty.equals("Easy"));
        medBtn = createToggleButton("Medium", selectedDifficulty.equals("Medium"));
        hardBtn = createToggleButton("Hard", selectedDifficulty.equals("Hard"));

        easyBtn.addActionListener(e -> setDifficulty("Easy"));
        medBtn.addActionListener(e -> setDifficulty("Medium"));
        hardBtn.addActionListener(e -> setDifficulty("Hard"));

        diffButtons.add(easyBtn);
        diffButtons.add(medBtn);
        diffButtons.add(hardBtn);

        difficultyPanel.add(diffButtons);

        difficultyPanel.setVisible(selectedMode.equals("Bot"));
        add(difficultyPanel);

        add(Box.createVerticalGlue());

        // --- START BUTTON ---
        JButton startBtn = new JButton("Start Game");
        startBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        startBtn.setBackground(new Color(34, 139, 34));
        startBtn.setForeground(Color.WHITE);
        startBtn.setFocusPainted(false);
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.setPreferredSize(new Dimension(200, 50));
        startBtn.setMaximumSize(new Dimension(200, 50));

        startBtn.addActionListener(e -> {
            if (selectedMode.equals("PvP")) {
                player2Username = player2Field.getText().trim();

                if (player2Username.isEmpty()) {
                    showValidationMessage("Please enter Player 2 username!", Color.RED);
                    return;
                }

                if (player2Username.equalsIgnoreCase(player1Username)) {
                    showValidationMessage("Player 2 must have a different username!", Color.RED);
                    return;
                }

                startBtn.setEnabled(false);
                showValidationMessage("Checking user...", new Color(255, 165, 0));

                new Thread(() -> {
                    boolean exists = DbCon.userExists(player2Username);

                    SwingUtilities.invokeLater(() -> {
                        startBtn.setEnabled(true);

                        if (exists) {
                            showValidationMessage("User found! Starting game...", new Color(34, 139, 34));

                            Timer timer = new Timer(500, evt -> {
                                manager.showGameBoard(selectedMode, selectedDifficulty,
                                        player1Username, player2Username, boardSize);
                            });
                            timer.setRepeats(false);
                            timer.start();
                        } else {
                            showValidationMessage("Player 2 not found in database!", Color.RED);
                            JOptionPane.showMessageDialog(this,
                                    "Player 2 '" + player2Username + "' is not registered.\n" +
                                            "They must create an account first.",
                                    "User Not Found",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    });
                }).start();

            } else {
                // Bot mode - no validation needed
                manager.showGameBoard(selectedMode, selectedDifficulty,
                        player1Username, player2Username, boardSize);
            }
        });

        add(startBtn);
        add(Box.createRigidArea(new Dimension(0, 20)));

        JButton backBtn = new JButton("Back");
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.addActionListener(e -> manager.showWelcomeScreen());
        add(backBtn);
    }

    /* ===================== LOAD SETTINGS ===================== */

    private void loadSettingsFromFile() {
        try {
            Properties settings = SettingsScreen.getSettings();

            // Load game mode from settings
            String savedMode = settings.getProperty("gameMode", "Singleplayer (vs Bot)");
            if (savedMode.contains("Multiplayer")) {
                selectedMode = "PvP";
            } else {
                selectedMode = "Bot";
            }

            // Load difficulty from settings
            selectedDifficulty = settings.getProperty("difficulty", "Easy");

            // Load board size from settings
            String boardSizeStr = settings.getProperty("boardSize", "3x3 (Default)");
            // Extract number from "3x3 (Default)" -> "3"
            boardSize = Integer.parseInt(boardSizeStr.substring(0, 1));

            System.out.println("Settings loaded: Mode=" + selectedMode +
                    ", Difficulty=" + selectedDifficulty +
                    ", BoardSize=" + boardSize + "x" + boardSize);

        } catch (Exception e) {
            System.err.println("Error loading settings: " + e.getMessage());
            // Use defaults
            selectedMode = "PvP";
            selectedDifficulty = "Easy";
            boardSize = 3;
        }
    }

    /* ===================== HELPER METHODS ===================== */

    private void setMode(String mode) {
        this.selectedMode = mode;
        updateToggleStyle(pvpBtn, mode.equals("PvP"));
        updateToggleStyle(botBtn, mode.equals("Bot"));

        pvpInputPanel.setVisible(mode.equals("PvP"));
        difficultyPanel.setVisible(mode.equals("Bot"));

        showValidationMessage("", Color.BLACK);

        revalidate();
        repaint();
    }

    private void setDifficulty(String diff) {
        this.selectedDifficulty = diff;
        updateToggleStyle(easyBtn, diff.equals("Easy"));
        updateToggleStyle(medBtn, diff.equals("Medium"));
        updateToggleStyle(hardBtn, diff.equals("Hard"));
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("SansSerif", Font.BOLD, 18));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
        return lbl;
    }

    private JButton createToggleButton(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setFocusPainted(false);
        updateToggleStyle(btn, isActive);
        return btn;
    }

    private void updateToggleStyle(JButton btn, boolean isActive) {
        if (isActive) {
            btn.setBackground(ACTIVE_COLOR);
            btn.setForeground(Color.WHITE);
        } else {
            btn.setBackground(INACTIVE_COLOR);
            btn.setForeground(Color.BLACK);
        }
    }

    private void showValidationMessage(String message, Color color) {
        validationLabel.setText(message);
        validationLabel.setForeground(color);
    }
}