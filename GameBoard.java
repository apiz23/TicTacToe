package tictactoe;

import tictactoe.logic.GameLogic;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;

public class GameBoard extends JPanel {

    private final JButton[] cells = new JButton[9];
    private final GameLogic logic = new GameLogic();

    private JLabel timerLabel;
    private JLabel humanWinsLabel;
    private JLabel botWinsLabel;

    private int spotsTaken = 0;
    private int humanWins = 0;
    private int botWins = 0;

    private String gameMode;   // "PvP" or "Bot"
    private String difficulty; // "Easy", "Medium", "Hard"

    private final Color BG_COLOR = new Color(240, 240, 245);
    private final Color ACCENT_BLUE = new Color(70, 130, 180);
    private final Color X_COLOR = new Color(59, 130, 246);
    private final Color O_COLOR = new Color(239, 68, 68);
    private final Dimension BOARD_SIZE = new Dimension(450, 450);

    public GameBoard(ScreenManager manager, String mode, String difficulty) {
        this.gameMode = mode;
        this.difficulty = difficulty;

        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createBoardPanel(), BorderLayout.CENTER);
        add(createBottomPanel(manager), BorderLayout.SOUTH);

        System.out.println("Started Game: " + mode + " (" + difficulty + ")");
    }

    /* ---------------------- TOP PANEL ---------------------- */

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(BG_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel title = new JLabel("Your Turn");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(new Color(50, 50, 50));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(title);

        topPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        statsPanel.setBackground(BG_COLOR);

        timerLabel = addStat(statsPanel, "src/tictactoe/img/timer.png", "0");
        humanWinsLabel = addStat(statsPanel, "src/tictactoe/img/profile.png", "0");
        botWinsLabel = addStat(statsPanel, "src/tictactoe/img/bot.png", "0");

        topPanel.add(statsPanel);
        return topPanel;
    }

    private JLabel addStat(JPanel panel, String iconPath, String value) {
        JPanel statPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        statPanel.setBackground(BG_COLOR);

        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(loadIcon(iconPath, 24, 24));
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        valueLabel.setForeground(Color.DARK_GRAY);

        statPanel.add(iconLabel);
        statPanel.add(valueLabel);
        panel.add(statPanel);
        return valueLabel;
    }

    /* -------------------- BOARD CENTER --------------------------- */

    private JPanel createBoardPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_COLOR);

        JPanel boardPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        boardPanel.setBackground(BG_COLOR);
        boardPanel.setPreferredSize(BOARD_SIZE);
        boardPanel.setMinimumSize(BOARD_SIZE);
        boardPanel.setMaximumSize(BOARD_SIZE);

        for (int i = 0; i < 9; i++) {
            JButton btn = createBoardCell();
            int index = i;
            btn.addActionListener(e -> handleHumanMove(index));
            cells[index] = btn;
            boardPanel.add(btn);
        }

        wrapper.add(boardPanel);
        return wrapper;
    }

    private JButton createBoardCell() {
        JButton btn = new JButton("");
        btn.setFont(new Font("SansSerif", Font.BOLD, 70));
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.getText().isEmpty()) btn.setBackground(new Color(235, 245, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.getText().isEmpty()) btn.setBackground(Color.WHITE);
            }
        });
        return btn;
    }

    /* ---------------------------- BOTTOM PANEL ------------------------------ */

    private JPanel createBottomPanel(ScreenManager manager) {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(BG_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 30, 0));

        JButton backBtn = new JButton("Back to Menu");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        backBtn.setPreferredSize(new Dimension(180, 45));
        backBtn.setFocusPainted(false);
        backBtn.setBackground(ACCENT_BLUE);
        backBtn.setForeground(Color.WHITE);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backBtn.addActionListener(e -> {
            resetBoard();
            manager.showWelcomeScreen();
        });

        bottomPanel.add(backBtn);
        return bottomPanel;
    }

    /* ----------------------------- ICON LOADER ------------------------------ */

    private ImageIcon loadIcon(String path, int width, int height) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            return new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillOval(0, 0, width, height);
            g2.dispose();
            return new ImageIcon(img);
        }
    }

    /* ------------------------------ GAME LOGIC ------------------------------ */

    private void handleHumanMove(int index) {
        if (!cells[index].getText().isEmpty()) return;

        performMove(index);
        if (checkGameOver("You")) return;

        if ("Bot".equals(gameMode)) {
            SwingUtilities.invokeLater(() -> handleBotMove());
        }
    }

    private void handleBotMove() {
        int botIndex = -1;

        // Select AI strategy based on difficulty
        if ("Easy".equals(difficulty)) {
            botIndex = easyBot();
        } else if ("Medium".equals(difficulty)) {
            botIndex = mediumBot();
        } else if ("Hard".equals(difficulty)) {
            botIndex = hardBot();
        }

        if (botIndex != -1) {
            performMove(botIndex);
            checkGameOver("Opponent");
        }
    }

    /* ---------------------- AI DIFFICULTY LEVELS ---------------------- */

    // EASY: Random moves
    private int easyBot() {
        Random rand = new Random();
        int botIndex;
        do {
            botIndex = rand.nextInt(9);
        } while (!cells[botIndex].getText().isEmpty());
        return botIndex;
    }

    // MEDIUM: Block player wins, take winning moves, otherwise random
    private int mediumBot() {
        // 1. Check if bot can win
        for (int i = 0; i < 9; i++) {
            if (cells[i].getText().isEmpty()) {
                String currentSymbol = logic.isXTurn() ? "X" : "O";
                cells[i].setText(currentSymbol);

                if (checkWinningMove()) {
                    cells[i].setText("");
                    return i;
                }
                cells[i].setText("");
            }
        }

        // 2. Check if need to block player
        String opponentSymbol = logic.isXTurn() ? "O" : "X";
        for (int i = 0; i < 9; i++) {
            if (cells[i].getText().isEmpty()) {
                cells[i].setText(opponentSymbol);

                if (checkWinningMove()) {
                    cells[i].setText("");
                    return i;
                }
                cells[i].setText("");
            }
        }

        // 3. Otherwise, random move
        return easyBot();
    }

    // Helper method to check if current board state has a winner
    private boolean checkWinningMove() {
        int[][] wins = {
                {0,1,2}, {3,4,5}, {6,7,8}, // rows
                {0,3,6}, {1,4,7}, {2,5,8}, // cols
                {0,4,8}, {2,4,6}           // diagonals
        };

        for (int[] w : wins) {
            String a = cells[w[0]].getText();
            String b = cells[w[1]].getText();
            String c = cells[w[2]].getText();
            if (!a.isEmpty() && a.equals(b) && b.equals(c)) {
                return true;
            }
        }
        return false;
    }

    // HARD: Minimax algorithm (unbeatable)
    private int hardBot() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        String botSymbol = logic.isXTurn() ? "X" : "O";

        for (int i = 0; i < 9; i++) {
            if (cells[i].getText().isEmpty()) {
                cells[i].setText(botSymbol);
                int score = minimax(false, botSymbol);
                cells[i].setText("");

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        return bestMove;
    }

    private int minimax(boolean isMaximizing, String botSymbol) {
        String playerSymbol = botSymbol.equals("X") ? "O" : "X";

        // Check terminal states
        if (checkWinningMove()) {
            // Determine who won by checking whose symbol made the winning line
            String winner = getWinner();
            if (winner.equals(botSymbol)) {
                return 10; // Bot wins
            } else {
                return -10; // Player wins
            }
        }

        // Check for draw
        boolean boardFull = true;
        for (int i = 0; i < 9; i++) {
            if (cells[i].getText().isEmpty()) {
                boardFull = false;
                break;
            }
        }
        if (boardFull) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (cells[i].getText().isEmpty()) {
                    cells[i].setText(botSymbol);
                    int score = minimax(false, botSymbol);
                    cells[i].setText("");
                    bestScore = Math.max(score, bestScore);
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (cells[i].getText().isEmpty()) {
                    cells[i].setText(playerSymbol);
                    int score = minimax(true, botSymbol);
                    cells[i].setText("");
                    bestScore = Math.min(score, bestScore);
                }
            }
            return bestScore;
        }
    }

    private String getWinner() {
        int[][] wins = {
                {0,1,2}, {3,4,5}, {6,7,8},
                {0,3,6}, {1,4,7}, {2,5,8},
                {0,4,8}, {2,4,6}
        };

        for (int[] w : wins) {
            String a = cells[w[0]].getText();
            String b = cells[w[1]].getText();
            String c = cells[w[2]].getText();
            if (!a.isEmpty() && a.equals(b) && b.equals(c)) {
                return a;
            }
        }
        return "";
    }

    /* ------------------------- MOVE & WIN CHECKING ------------------------- */

    private void performMove(int index) {
        if (!logic.makeMove(index)) return;

        String symbol = logic.isXTurn() ? "O" : "X";
        cells[index].setText(symbol);
        cells[index].setForeground(symbol.equals("X") ? X_COLOR : O_COLOR);
        timerLabel.setText(String.valueOf(++spotsTaken));

        logic.switchTurn();
    }

    private boolean checkGameOver(String lastPlayerName) {
        if (logic.checkWinner()) {
            boolean humanWon = lastPlayerName.equals("You");

            if (humanWon) {
                humanWinsLabel.setText(String.valueOf(++humanWins));
                JOptionPane.showMessageDialog(this, "You Win!", "Victory", JOptionPane.INFORMATION_MESSAGE);
            } else {
                botWinsLabel.setText(String.valueOf(++botWins));
                JOptionPane.showMessageDialog(this, lastPlayerName + " Wins!", "Defeat", JOptionPane.INFORMATION_MESSAGE);
            }
            resetBoard();
            return true;
        }

        if (logic.isDraw()) {
            JOptionPane.showMessageDialog(this, "It's a draw!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            resetBoard();
            return true;
        }
        return false;
    }

    private void resetBoard() {
        logic.reset();
        spotsTaken = 0;
        timerLabel.setText("0");

        for (JButton btn : cells) {
            btn.setText("");
            btn.setBackground(Color.WHITE);
        }
    }
}