package tictactoe;

import tictactoe.logic.GameLogic;
// Depending on your package structure, you might need these imports:
// import tictactoe.audio.MusicPlayer;
// import tictactoe.ui.SettingsScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Properties;
import java.util.Random;
import javax.imageio.ImageIO;

public class GameBoard extends JPanel {

    // UI Components
    private JButton[] cells;
    private GameLogic logic;

    private JLabel timerLabel;
    private JLabel player1WinsLabel;
    private JLabel player2WinsLabel;
    private JLabel turnLabel;

    // Music Components
    private MusicPlayer musicPlayer;
    private JButton musicToggleBtn;

    // Game State
    private int spotsTaken = 0;
    private int player1Wins = 0;
    private int player2Wins = 0;

    private String gameMode;
    private String difficulty;
    private String player1Name;
    private String player2Name;

    private int boardSize; // Dynamic board size

    // Colors
    private final Color BG_COLOR = new Color(240, 240, 245);
    private final Color ACCENT_BLUE = new Color(70, 130, 180);
    private final Color X_COLOR = new Color(59, 130, 246);
    private final Color O_COLOR = new Color(239, 68, 68);

    public GameBoard(ScreenManager manager, String mode, String difficulty,
                     String player1, String player2, int boardSize) {
        this.gameMode = mode;
        this.difficulty = difficulty;
        this.player1Name = player1;
        this.player2Name = mode.equals("Bot") ? "Bot" : player2;
        this.boardSize = boardSize;

        // Initialize arrays based on board size
        this.cells = new JButton[boardSize * boardSize];
        this.logic = new GameLogic(boardSize);

        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createBoardPanel(), BorderLayout.CENTER);
        add(createBottomPanel(manager), BorderLayout.SOUTH);

        // Initialize and start music
        initializeMusic();

        System.out.println("Started Game: " + mode + " (" + difficulty + ") - Board: " + boardSize + "x" + boardSize);
        System.out.println("Player 1: " + player1Name + " | Player 2: " + player2Name);
    }

    /* ---------------------- TOP PANEL ---------------------- */

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(BG_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        // Add music toggle button at top-right
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_COLOR);
        headerPanel.add(createMusicToggleButton(), BorderLayout.EAST);

        // Add padding to header so button isn't stuck to the edge
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        topPanel.add(headerPanel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        turnLabel = new JLabel(player1Name + "'s Turn (X)");
        turnLabel.setFont(new Font("SansSerif", Font.BOLD, 32));
        turnLabel.setForeground(new Color(50, 50, 50));
        turnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(turnLabel);

        topPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        statsPanel.setBackground(BG_COLOR);

        timerLabel = addStat(statsPanel, "src/tictactoe/img/timer.png", "0");
        player1WinsLabel = addStat(statsPanel, "src/tictactoe/img/profile.png", player1Name + ": 0");

        String icon2 = gameMode.equals("Bot") ? "src/tictactoe/img/bot.png" : "src/tictactoe/img/profile.png";
        player2WinsLabel = addStat(statsPanel, icon2, player2Name + ": 0");

        topPanel.add(statsPanel);
        return topPanel;
    }

    private JButton createMusicToggleButton() {
        musicToggleBtn = new JButton("🔊");
        musicToggleBtn.setFont(new Font("SansSerif", Font.BOLD, 20));
        musicToggleBtn.setPreferredSize(new Dimension(50, 50));
        musicToggleBtn.setFocusPainted(false);
        musicToggleBtn.setBackground(new Color(70, 130, 180));
        musicToggleBtn.setForeground(Color.WHITE);
        musicToggleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        musicToggleBtn.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 2));
        musicToggleBtn.setToolTipText("Toggle Music");

        musicToggleBtn.addActionListener(e -> {
            boolean isMuted = musicPlayer.toggleMute();
            musicToggleBtn.setText(isMuted ? "🔇" : "🔊");
            musicToggleBtn.setBackground(isMuted ? new Color(200, 200, 200) : new Color(70, 130, 180));
        });

        return musicToggleBtn;
    }

    private JLabel addStat(JPanel panel, String iconPath, String value) {
        JPanel statPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        statPanel.setBackground(BG_COLOR);

        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(loadIcon(iconPath, 24, 24));
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
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

        // Dynamic board size calculation
        int cellSize = calculateCellSize();
        int boardPixelSize = cellSize * boardSize + (boardSize - 1) * 10; // 10px gap

        JPanel boardPanel = new JPanel(new GridLayout(boardSize, boardSize, 10, 10));
        boardPanel.setBackground(BG_COLOR);
        boardPanel.setPreferredSize(new Dimension(boardPixelSize, boardPixelSize));
        boardPanel.setMinimumSize(new Dimension(boardPixelSize, boardPixelSize));
        boardPanel.setMaximumSize(new Dimension(boardPixelSize, boardPixelSize));

        for (int i = 0; i < boardSize * boardSize; i++) {
            JButton btn = createBoardCell(cellSize);
            int index = i;
            btn.addActionListener(e -> handlePlayerMove(index));
            cells[index] = btn;
            boardPanel.add(btn);
        }

        wrapper.add(boardPanel);
        return wrapper;
    }

    private int calculateCellSize() {
        // Adjust cell size based on board size
        switch (boardSize) {
            case 3: return 120;
            case 4: return 90;
            case 5: return 70;
            case 6: return 60;
            default: return 100;
        }
    }

    private JButton createBoardCell(int cellSize) {
        JButton btn = new JButton("");

        // Dynamic font size based on cell size
        int fontSize = cellSize - 20;
        btn.setFont(new Font("SansSerif", Font.BOLD, fontSize));

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
            // Stop music when leaving game
            if (musicPlayer != null) {
                musicPlayer.stopMusic();
            }
            manager.showWelcomeScreen();
        });

        bottomPanel.add(backBtn);
        return bottomPanel;
    }

    /* ----------------------------- MUSIC LOGIC ------------------------------ */

    private void initializeMusic() {
        try {
            // Get music player instance
            musicPlayer = MusicPlayer.getInstance();

            // Check if music is enabled in settings
            Properties settings = SettingsScreen.getSettings();
            boolean musicEnabled = Boolean.parseBoolean(settings.getProperty("musicEnabled", "true"));

            if (musicEnabled) {
                // Start background music
                musicPlayer.playMusic("src/tictactoe/audio/background.wav");
            } else {
                // Music disabled, update button
                if (musicToggleBtn != null) {
                    musicToggleBtn.setText("🔇");
                    musicToggleBtn.setBackground(new Color(200, 200, 200));
                }
            }
        } catch (Exception e) {
            System.err.println("Error initializing music: " + e.getMessage());
        }
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

    private void handlePlayerMove(int index) {
        if (!cells[index].getText().isEmpty()) return;

        String currentPlayer = logic.isXTurn() ? player1Name : player2Name;

        performMove(index);

        if (checkGameOver(currentPlayer)) return;

        String nextPlayer = logic.isXTurn() ? player1Name : player2Name;
        String nextSymbol = logic.isXTurn() ? "X" : "O";
        turnLabel.setText(nextPlayer + "'s Turn (" + nextSymbol + ")");

        if ("Bot".equals(gameMode) && !logic.isXTurn()) {
            SwingUtilities.invokeLater(() -> handleBotMove());
        }
    }

    private void handleBotMove() {
        int botIndex = -1;

        if ("Easy".equals(difficulty)) {
            botIndex = easyBot();
        } else if ("Medium".equals(difficulty)) {
            botIndex = mediumBot();
        } else if ("Hard".equals(difficulty)) {
            botIndex = hardBot();
        }

        if (botIndex != -1) {
            performMove(botIndex);
            checkGameOver(player2Name);

            String nextSymbol = logic.isXTurn() ? "X" : "O";
            turnLabel.setText(player1Name + "'s Turn (" + nextSymbol + ")");
        }
    }

    /* ---------------------- AI DIFFICULTY LEVELS ---------------------- */

    private int easyBot() {
        Random rand = new Random();
        int botIndex;
        if (spotsTaken >= boardSize * boardSize) return -1;

        do {
            botIndex = rand.nextInt(boardSize * boardSize);
        } while (!cells[botIndex].getText().isEmpty());
        return botIndex;
    }

    private int mediumBot() {
        if (spotsTaken >= boardSize * boardSize) return -1;

        String symbol = logic.isXTurn() ? "X" : "O";

        // 1. Check if bot can win
        for (int i = 0; i < boardSize * boardSize; i++) {
            if (cells[i].getText().isEmpty()) {
                cells[i].setText(symbol);
                if (checkWinningMove()) {
                    cells[i].setText("");
                    return i;
                }
                cells[i].setText("");
            }
        }

        // 2. Block player
        String opponentSymbol = logic.isXTurn() ? "O" : "X";
        for (int i = 0; i < boardSize * boardSize; i++) {
            if (cells[i].getText().isEmpty()) {
                cells[i].setText(opponentSymbol);
                if (checkWinningMove()) {
                    cells[i].setText("");
                    return i;
                }
                cells[i].setText("");
            }
        }

        return easyBot();
    }

    private int hardBot() {
        if (spotsTaken >= boardSize * boardSize) return -1;

        // For larger boards, use limited depth minimax
        int maxDepth = (boardSize <= 3) ? 9 : 4;

        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        String botSymbol = logic.isXTurn() ? "X" : "O";

        for (int i = 0; i < boardSize * boardSize; i++) {
            if (cells[i].getText().isEmpty()) {
                cells[i].setText(botSymbol);
                int score = minimax(false, botSymbol, 0, maxDepth);
                cells[i].setText("");

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        if (bestMove == -1) return easyBot();
        return bestMove;
    }

    private int minimax(boolean isMaximizing, String botSymbol, int depth, int maxDepth) {
        // Limit depth for larger boards
        if (depth >= maxDepth) return 0;

        String playerSymbol = botSymbol.equals("X") ? "O" : "X";

        if (checkWinningMove()) {
            String winner = getWinner();
            if (winner.equals(botSymbol)) return 10 - depth;
            else if (winner.equals(playerSymbol)) return depth - 10;
        }

        boolean boardFull = true;
        for (int i = 0; i < boardSize * boardSize; i++) {
            if (cells[i].getText().isEmpty()) {
                boardFull = false;
                break;
            }
        }
        if (boardFull) return 0;

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < boardSize * boardSize; i++) {
                if (cells[i].getText().isEmpty()) {
                    cells[i].setText(botSymbol);
                    int score = minimax(false, botSymbol, depth + 1, maxDepth);
                    cells[i].setText("");
                    bestScore = Math.max(score, bestScore);
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < boardSize * boardSize; i++) {
                if (cells[i].getText().isEmpty()) {
                    cells[i].setText(playerSymbol);
                    int score = minimax(true, botSymbol, depth + 1, maxDepth);
                    cells[i].setText("");
                    bestScore = Math.min(score, bestScore);
                }
            }
            return bestScore;
        }
    }

    /* ---------------------- DYNAMIC WIN CHECKING ---------------------- */

    private boolean checkWinningMove() {
        // Check all rows
        for (int row = 0; row < boardSize; row++) {
            boolean rowWin = true;
            String first = cells[row * boardSize].getText();
            if (first.isEmpty()) continue;

            for (int col = 1; col < boardSize; col++) {
                if (!cells[row * boardSize + col].getText().equals(first)) {
                    rowWin = false;
                    break;
                }
            }
            if (rowWin) return true;
        }

        // Check all columns
        for (int col = 0; col < boardSize; col++) {
            boolean colWin = true;
            String first = cells[col].getText();
            if (first.isEmpty()) continue;

            for (int row = 1; row < boardSize; row++) {
                if (!cells[row * boardSize + col].getText().equals(first)) {
                    colWin = false;
                    break;
                }
            }
            if (colWin) return true;
        }

        // Check main diagonal (top-left to bottom-right)
        boolean diagWin1 = true;
        String first1 = cells[0].getText();
        if (!first1.isEmpty()) {
            for (int i = 1; i < boardSize; i++) {
                if (!cells[i * boardSize + i].getText().equals(first1)) {
                    diagWin1 = false;
                    break;
                }
            }
            if (diagWin1) return true;
        }

        // Check anti-diagonal (top-right to bottom-left)
        boolean diagWin2 = true;
        String first2 = cells[boardSize - 1].getText();
        if (!first2.isEmpty()) {
            for (int i = 1; i < boardSize; i++) {
                if (!cells[i * boardSize + (boardSize - 1 - i)].getText().equals(first2)) {
                    diagWin2 = false;
                    break;
                }
            }
            if (diagWin2) return true;
        }

        return false;
    }

    private String getWinner() {
        // Check rows
        for (int row = 0; row < boardSize; row++) {
            String first = cells[row * boardSize].getText();
            if (first.isEmpty()) continue;
            boolean match = true;
            for (int col = 1; col < boardSize; col++) {
                if (!cells[row * boardSize + col].getText().equals(first)) {
                    match = false;
                    break;
                }
            }
            if (match) return first;
        }

        // Check columns
        for (int col = 0; col < boardSize; col++) {
            String first = cells[col].getText();
            if (first.isEmpty()) continue;
            boolean match = true;
            for (int row = 1; row < boardSize; row++) {
                if (!cells[row * boardSize + col].getText().equals(first)) {
                    match = false;
                    break;
                }
            }
            if (match) return first;
        }

        // Check main diagonal
        String first1 = cells[0].getText();
        if (!first1.isEmpty()) {
            boolean match = true;
            for (int i = 1; i < boardSize; i++) {
                if (!cells[i * boardSize + i].getText().equals(first1)) {
                    match = false;
                    break;
                }
            }
            if (match) return first1;
        }

        // Check anti-diagonal
        String first2 = cells[boardSize - 1].getText();
        if (!first2.isEmpty()) {
            boolean match = true;
            for (int i = 1; i < boardSize; i++) {
                if (!cells[i * boardSize + (boardSize - 1 - i)].getText().equals(first2)) {
                    match = false;
                    break;
                }
            }
            if (match) return first2;
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
            boolean isPlayer1 = lastPlayerName.equals(player1Name);

            if (isPlayer1) {
                player1Wins++;
                player1WinsLabel.setText(player1Name + ": " + player1Wins);
                JOptionPane.showMessageDialog(this, player1Name + " Wins!", "Victory", JOptionPane.INFORMATION_MESSAGE);

                if (gameMode.equals("PvP")) {
                    String hw = UserSession.getHardwareId();
                    if (player1Name != null && hw != null) {
                        System.out.println("[PvP] Saving win for: " + player1Name);
                        DbCon.saveScore(player1Name, hw, 1);
                    }
                } else {
                    System.out.println("[Bot Mode] No points awarded for: " + player1Name);
                }

            } else {
                player2Wins++;
                player2WinsLabel.setText(player2Name + ": " + player2Wins);
                JOptionPane.showMessageDialog(this, player2Name + " Wins!", "Victory", JOptionPane.INFORMATION_MESSAGE);

                if (gameMode.equals("PvP") && !player2Name.equals("Guest")) {
                    String hw = UserSession.getHardwareId();
                    if (hw != null) {
                        System.out.println("[PvP] Saving win for: " + player2Name);
                        DbCon.saveScore(player2Name, hw, 1);
                    }
                } else if (gameMode.equals("Bot")) {
                    System.out.println("[Bot Mode] No points awarded for Bot");
                }
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

        turnLabel.setText(player1Name + "'s Turn (X)");

        for (JButton btn : cells) {
            btn.setText("");
            btn.setBackground(Color.WHITE);
        }
    }
}