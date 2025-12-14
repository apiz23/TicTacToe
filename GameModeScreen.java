package tictactoe;

import javax.swing.*;
import java.awt.*;

public class GameModeScreen extends JPanel {

    private String selectedMode = "PvP"; // Default
    private String selectedDifficulty = "Easy"; // Default
    private String player1Username; // Logged in user
    private String player2Username = ""; // For PvP

    // UI Components to toggle state
    private JButton pvpBtn, botBtn;
    private JButton easyBtn, medBtn, hardBtn;
    private JPanel difficultyPanel; // To hide/show difficulty options
    private JPanel pvpInputPanel; // To show Player 2 input for PvP
    private JTextField player2Field;
    private JLabel player1Label;

    // Colors (Matching your theme)
    private final Color BG_COLOR = new Color(240, 240, 245);
    private final Color ACTIVE_COLOR = new Color(70, 130, 180); // Steel Blue
    private final Color INACTIVE_COLOR = new Color(200, 200, 200); // Gray

    public GameModeScreen(ScreenManager manager, String loggedInUsername) {
        this.player1Username = loggedInUsername;

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
        add(Box.createRigidArea(new Dimension(0, 30)));

        // --- OPPONENT SELECTION ---
        add(createLabel("Select Opponent"));
        add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel opponentPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        opponentPanel.setBackground(BG_COLOR);

        pvpBtn = createToggleButton("Player vs Player", true);
        botBtn = createToggleButton("Player vs Bot", false);

        // Add Logic
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

        // Info label
        JLabel infoLabel = new JLabel("(Optional: Leave blank for Guest)");
        infoLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        infoLabel.setForeground(Color.GRAY);
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        pvpInputPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        pvpInputPanel.add(infoLabel);

        // Visible by default since PvP is default
        add(pvpInputPanel);

        add(Box.createRigidArea(new Dimension(0, 30)));

        // --- DIFFICULTY SELECTION (Hidden by default for PvP) ---
        difficultyPanel = new JPanel();
        difficultyPanel.setLayout(new BoxLayout(difficultyPanel, BoxLayout.Y_AXIS));
        difficultyPanel.setBackground(BG_COLOR);

        difficultyPanel.add(createLabel("Select Difficulty"));
        difficultyPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel diffButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        diffButtons.setBackground(BG_COLOR);

        easyBtn = createToggleButton("Easy", true);
        medBtn = createToggleButton("Medium", false);
        hardBtn = createToggleButton("Hard", false);

        easyBtn.addActionListener(e -> setDifficulty("Easy"));
        medBtn.addActionListener(e -> setDifficulty("Medium"));
        hardBtn.addActionListener(e -> setDifficulty("Hard"));

        diffButtons.add(easyBtn);
        diffButtons.add(medBtn);
        diffButtons.add(hardBtn);

        difficultyPanel.add(diffButtons);

        // Initially invisible because PvP is default
        difficultyPanel.setVisible(false);
        add(difficultyPanel);

        add(Box.createVerticalGlue()); // Push start button to bottom area

        // --- START BUTTON ---
        JButton startBtn = new JButton("Start Game");
        startBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        startBtn.setBackground(new Color(34, 139, 34)); // Forest Green
        startBtn.setForeground(Color.WHITE);
        startBtn.setFocusPainted(false);
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.setPreferredSize(new Dimension(200, 50));
        startBtn.setMaximumSize(new Dimension(200, 50));

        startBtn.addActionListener(e -> {
            if (selectedMode.equals("PvP")) {
                // Get Player 2 username
                player2Username = player2Field.getText().trim();
                if (player2Username.isEmpty()) {
                    player2Username = "Guest"; // Default if empty
                }

                // Validation: check if same username
                if (player2Username.equalsIgnoreCase(player1Username)) {
                    JOptionPane.showMessageDialog(this,
                            "Player 2 must have a different username!",
                            "Invalid Input",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Pass settings to GameBoard
            manager.showGameBoard(selectedMode, selectedDifficulty, player1Username, player2Username);
        });

        add(startBtn);
        add(Box.createRigidArea(new Dimension(0, 20)));

        JButton backBtn = new JButton("Back");
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        backBtn.addActionListener(e -> manager.showWelcomeScreen());
        add(backBtn);
    }

    // --- HELPER METHODS ---

    private void setMode(String mode) {
        this.selectedMode = mode;
        updateToggleStyle(pvpBtn, mode.equals("PvP"));
        updateToggleStyle(botBtn, mode.equals("Bot"));

        // Show/Hide panels based on mode
        pvpInputPanel.setVisible(mode.equals("PvP"));
        difficultyPanel.setVisible(mode.equals("Bot"));

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
}