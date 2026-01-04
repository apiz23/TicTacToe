package tictactoe;

import tictactoe.logic.GameLogic;
import tictactoe.logic.BotAI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;
import java.util.Properties;
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

    private int boardSize;

    // Blue-Black Theme Colors
    private final Color BG_COLOR = new Color(18, 18, 24);
    private final Color CARD_BG = new Color(30, 30, 40);
    private final Color ACCENT_COLOR = new Color(0, 150, 255);
    private final Color ACCENT_HOVER = new Color(0, 170, 255);
    private final Color SUCCESS_COLOR = new Color(46, 204, 113);
    private final Color ERROR_COLOR = new Color(231, 76, 60);
    private final Color TEXT_PRIMARY = new Color(240, 240, 240);
    private final Color TEXT_SECONDARY = new Color(180, 180, 200);
    private final Color BORDER_COLOR = new Color(50, 50, 60);
    private final Color CELL_BG = new Color(40, 40, 50);
    private final Color CELL_HOVER = new Color(50, 50, 60);
    private final Color X_COLOR = new Color(0, 150, 255);
    private final Color O_COLOR = new Color(231, 76, 60);

    public GameBoard(ScreenManager manager, String mode, String difficulty,
                     String player1, String player2, int boardSize) {
        this.gameMode = mode;
        this.difficulty = difficulty;
        this.player1Name = player1;
        this.player2Name = mode.equals("Bot") ? "Bot" : player2;
        this.boardSize = boardSize;

        this.cells = new JButton[boardSize * boardSize];
        this.logic = new GameLogic(boardSize);

        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        add(createTopPanel(), BorderLayout.NORTH);
        add(createBoardPanel(), BorderLayout.CENTER);
        add(createBottomPanel(manager), BorderLayout.SOUTH);

        initializeMusic();

        System.out.println("Started Game: " + mode + " (" + difficulty + ") - Board: " + boardSize + "x" + boardSize);
    }

    /* ---------------------- TOP PANEL ---------------------- */

    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(BG_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_COLOR);

        // Create music button with theme colors
        musicToggleBtn = createMusicToggleButton();
        headerPanel.add(musicToggleBtn, BorderLayout.EAST);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));

        topPanel.add(headerPanel);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Turn label with theme colors
        turnLabel = new JLabel(player1Name + "'s Turn (X)");
        turnLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        turnLabel.setForeground(TEXT_PRIMARY);
        turnLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        topPanel.add(turnLabel);

        topPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Stats panel with theme colors
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        statsPanel.setBackground(BG_COLOR);

        // Use your image icons
        timerLabel = addStat(statsPanel, "src/tictactoe/img/timer.png", "0");
        player1WinsLabel = addStat(statsPanel, "src/tictactoe/img/profile.png", player1Name + ": 0");

        String icon2 = gameMode.equals("Bot") ? "src/tictactoe/img/bot.png" : "src/tictactoe/img/profile.png";
        player2WinsLabel = addStat(statsPanel, icon2, player2Name + ": 0");

        topPanel.add(statsPanel);
        return topPanel;
    }

    private ImageIcon loadScaledIcon(String path, int width, int height) {
        try {
            Image img = ImageIO.read(
                    Objects.requireNonNull(getClass().getResource("/" + path))
            );
            Image scaled = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } catch (Exception e) {
            System.err.println("Icon load failed: " + path);
            return new ImageIcon();
        }
    }

    private JButton createMusicToggleButton() {
        int btnSize = 60;

        ImageIcon musicIcon = loadScaledIcon("tictactoe/img/music.png", btnSize, btnSize);
        ImageIcon muteIcon  = loadScaledIcon("tictactoe/img/mute.png", btnSize, btnSize);

        musicToggleBtn = new JButton(musicIcon);
        musicToggleBtn.setPreferredSize(new Dimension(btnSize, btnSize));

        musicToggleBtn.setBorderPainted(false);
        musicToggleBtn.setFocusPainted(false);
        musicToggleBtn.setContentAreaFilled(false);
        musicToggleBtn.setOpaque(false);
        musicToggleBtn.setMargin(new Insets(0, 0, 0, 0));

        musicToggleBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        musicToggleBtn.setToolTipText("Toggle Music");

        musicToggleBtn.addActionListener(e -> {
            boolean isMuted = musicPlayer.toggleMute();
            musicToggleBtn.setIcon(isMuted ? muteIcon : musicIcon);
        });

        return musicToggleBtn;
    }

    private JLabel addStat(JPanel panel, String iconPath, String value) {
        JPanel statPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        statPanel.setBackground(BG_COLOR);

        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(loadIcon(iconPath, 24, 24));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(TEXT_PRIMARY);

        statPanel.add(iconLabel);
        statPanel.add(valueLabel);
        panel.add(statPanel);
        return valueLabel; // Return the value label for updating
    }

    /* -------------------- BOARD CENTER --------------------------- */

    private JPanel createBoardPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(BG_COLOR);

        int cellSize = calculateCellSize();
        int boardPixelSize = cellSize * boardSize + (boardSize - 1) * 10;

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
        int fontSize = cellSize - 20;
        btn.setFont(new Font("Segoe UI", Font.BOLD, fontSize));
        btn.setBackground(CELL_BG);
        btn.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 2));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (btn.getText().isEmpty()) btn.setBackground(CELL_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (btn.getText().isEmpty()) btn.setBackground(CELL_BG);
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
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        backBtn.setPreferredSize(new Dimension(180, 45));
        backBtn.setFocusPainted(false);
        backBtn.setBackground(new Color(100, 100, 120));
        backBtn.setForeground(Color.WHITE);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backBtn.addActionListener(e -> {
            resetBoard();
            if (musicPlayer != null) {
                musicPlayer.stopMusic();
            }
            manager.showWelcomeScreen();
        });

        bottomPanel.add(backBtn);
        return bottomPanel;
    }

    /* ----------------------------- MUSIC & UTILS ------------------------------ */

    private void initializeMusic() {
        try {
            musicPlayer = MusicPlayer.getInstance();
            Properties settings = SettingsScreen.getSettings();
            boolean musicEnabled = Boolean.parseBoolean(settings.getProperty("musicEnabled", "true"));

            if (musicEnabled) {
                musicPlayer.playMusic("src/tictactoe/audio/background1.wav");

                musicToggleBtn.setEnabled(true);
                musicToggleBtn.setText("🔊");
                musicToggleBtn.setBackground(ACCENT_COLOR);

            } else {
                musicToggleBtn.setText("🔇");
                musicToggleBtn.setBackground(new Color(100, 100, 120));
                musicToggleBtn.setEnabled(false);
            }

        } catch (Exception e) {
            System.err.println("Error initializing music: " + e.getMessage());
        }
    }

    private ImageIcon loadIcon(String path, int width, int height) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            return new ImageIcon(img.getScaledInstance(width, height, Image.SCALE_SMOOTH));
        } catch (Exception e) {
            System.err.println("Could not load icon: " + path);
            // Create a simple colored circle as fallback
            BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            g2.setColor(TEXT_SECONDARY);
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
        // 1. Create a clean String representation of the board for the AI
        String[] boardData = new String[cells.length];
        for (int i = 0; i < cells.length; i++) {
            boardData[i] = cells[i].getText();
        }

        // 2. Ask BotAI for the move
        int botIndex = BotAI.getMove(difficulty, boardData, boardSize, spotsTaken);

        // 3. Apply the move
        if (botIndex != -1) {
            performMove(botIndex);
            checkGameOver(player2Name);

            String nextSymbol = logic.isXTurn() ? "X" : "O";
            turnLabel.setText(player1Name + "'s Turn (" + nextSymbol + ")");
        }
    }

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
                        // Threading fix
                        new Thread(() -> {
                            System.out.println("[PvP] Saving win for: " + player1Name);
                            DbCon.saveScore(player1Name, hw, 1);
                        }).start();
                    }
                }

            } else {
                player2Wins++;
                player2WinsLabel.setText(player2Name + ": " + player2Wins);
                JOptionPane.showMessageDialog(this, player2Name + " Wins!", "Victory", JOptionPane.INFORMATION_MESSAGE);

                if (gameMode.equals("PvP") && !player2Name.equals("Guest")) {
                    String hw = UserSession.getHardwareId();
                    if (hw != null) {
                        // Threading fix applied here too
                        new Thread(() -> {
                            System.out.println("[PvP] Saving win for: " + player2Name);
                            DbCon.saveScore(player2Name, hw, 1);
                        }).start();
                    }
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
            btn.setBackground(CELL_BG);
        }
    }
}