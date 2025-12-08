package tictactoe;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class ScoreboardScreen extends JPanel {

    // Theme Colors
    private final Color BG_COLOR = new Color(240, 240, 245);
    private final Color ACCENT_BLUE = new Color(70, 130, 180);
    private final Color TABLE_HEADER = new Color(50, 50, 50);

    private JTable table;
    private DefaultTableModel tableModel;

    public ScoreboardScreen(ScreenManager manager) {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // --- TOP PANEL (Title) ---
        JPanel topPanel = new JPanel();
        topPanel.setBackground(BG_COLOR);
        topPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));

        JLabel title = new JLabel("Global Leaderboard");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setForeground(new Color(50, 50, 50));
        topPanel.add(title);

        add(topPanel, BorderLayout.NORTH);

        // --- CENTER PANEL (Table) ---
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(BG_COLOR);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30)); // Slightly tighter padding

        // UPDATED COLUMNS: Added "Date"
        String[] columns = {"Rank", "Player Name", "Score", "Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing
            }
        };

        table = new JTable(tableModel);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(BG_COLOR);

        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // --- BOTTOM PANEL (Back Button) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(BG_COLOR);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

        JButton backBtn = new JButton("Back to Menu");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        backBtn.setPreferredSize(new Dimension(180, 45));
        backBtn.setFocusPainted(false);
        backBtn.setBackground(ACCENT_BLUE);
        backBtn.setForeground(Color.WHITE);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backBtn.addActionListener(e -> manager.showWelcomeScreen());
        bottomPanel.add(backBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // --- LOAD DATA ---
        loadScores();
    }

    private void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(220, 220, 220));
        table.setFont(new Font("SansSerif", Font.PLAIN, 15)); // Slightly smaller font for more data
        table.setSelectionBackground(new Color(235, 245, 255));
        table.setSelectionForeground(Color.BLACK);

        // Header Styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 16));
        header.setBackground(TABLE_HEADER);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 50));

        // Center Align Cells (Rank, Score, Date)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        TableColumnModel colModel = table.getColumnModel();
        colModel.getColumn(0).setCellRenderer(centerRenderer); // Rank
        colModel.getColumn(2).setCellRenderer(centerRenderer); // Score
        colModel.getColumn(3).setCellRenderer(centerRenderer); // Date

        // Adjust Column Widths
        colModel.getColumn(0).setPreferredWidth(50);  // Rank (Small)
        colModel.getColumn(1).setPreferredWidth(200); // Name (Wide)
        colModel.getColumn(2).setPreferredWidth(80);  // Score (Medium)
        colModel.getColumn(3).setPreferredWidth(150); // Date (Wide)
    }

    /**
     * Loads scores using the DbCon class in a background thread
     */
    private void loadScores() {
        tableModel.setRowCount(0); // Clear table

        new Thread(() -> {
            // Updated to handle 3 columns of data from DB
            Object[][] scores = DbCon.getTopScores();

            SwingUtilities.invokeLater(() -> {
                if (scores.length == 0) {
                    tableModel.addRow(new Object[]{"-", "No Data / Offline", "-", "-"});
                } else {
                    for (int i = 0; i < scores.length; i++) {
                        int rank = i + 1;
                        String player = (String) scores[i][0];
                        int score = (int) scores[i][1];
                        String date = (String) scores[i][2];

                        tableModel.addRow(new Object[]{rank, player, score, date});
                    }
                }
            });
        }).start();
    }
}