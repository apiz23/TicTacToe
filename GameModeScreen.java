package tictactoe;

import javax.swing.*;
import java.awt.*;

public class GameModeScreen extends JPanel {

    private String selectedMode = "PvP"; // Default
    private String selectedDifficulty = "Easy"; // Default

    // UI Components to toggle state
    private JButton pvpBtn, botBtn;
    private JButton easyBtn, medBtn, hardBtn;
    private JPanel difficultyPanel; // To hide/show difficulty options

    // Colors (Matching your theme)
    private final Color BG_COLOR = new Color(240, 240, 245);
    private final Color ACTIVE_COLOR = new Color(70, 130, 180); // Steel Blue
    private final Color INACTIVE_COLOR = new Color(200, 200, 200); // Gray

    public GameModeScreen(ScreenManager manager) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(BG_COLOR);
        setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // --- TITLE ---
        JLabel title = new JLabel("Game Setup");
        title.setFont(new Font("SansSerif", Font.BOLD, 36));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);
        add(Box.createRigidArea(new Dimension(0, 40)));

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

        add(Box.createRigidArea(new Dimension(0, 40)));

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
            // Pass the selected settings to the Manager -> GameBoard
            manager.showGameBoard(selectedMode, selectedDifficulty);
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

        // Show/Hide difficulty based on mode
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