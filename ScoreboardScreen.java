package tictactoe;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class ScoreboardScreen extends JPanel {

    // Dark Theme Color Palette
    private final Color BG_COLOR = new Color(18, 18, 24);
    private final Color CARD_COLOR = new Color(30, 30, 40);
    private final Color ACCENT_COLOR = new Color(0, 150, 255);
    private final Color TEXT_PRIMARY = new Color(240, 240, 240);
    private final Color TEXT_SECONDARY = new Color(180, 180, 200);
    private final Color TABLE_HEADER_BG = new Color(40, 40, 50);
    private final Color TABLE_ROW_BG = new Color(32, 32, 42); // Single color for all rows
    private final Color GOLD = new Color(255, 215, 0);
    private final Color SILVER = new Color(192, 192, 192);
    private final Color BRONZE = new Color(205, 127, 50);
    private final Color SCORE_GREEN = new Color(46, 204, 113);

    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel loadingLabel;
    private JLabel statsLabel;

    public ScoreboardScreen(ScreenManager manager) {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        // Main content panel with padding optimized for 500x700
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- HEADER PANEL --- (More compact)
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // --- CENTER PANEL (Table Container) ---
        JPanel centerPanel = createCenterPanel();
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // --- BOTTOM PANEL (Controls) --- (More compact)
        JPanel bottomPanel = createBottomPanel(manager);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // --- LOAD DATA ---
        loadScores();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0)); // Reduced padding

        // Trophy icon (smaller)
        JLabel trophyIcon = new JLabel("🏆");
        trophyIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36)); // Smaller icon
        trophyIcon.setForeground(GOLD);
        trophyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title (smaller font)
        JLabel title = new JLabel("LEADERBOARD");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Smaller font
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Subtitle (smaller font)
        JLabel subtitle = new JLabel("Top Players • Live Rankings");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Smaller font
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Stats label (smaller font)
        statsLabel = new JLabel("Loading...");
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11)); // Smaller font
        statsLabel.setForeground(new Color(150, 200, 255));
        statsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(trophyIcon);
        header.add(Box.createRigidArea(new Dimension(0, 8)));
        header.add(title);
        header.add(Box.createRigidArea(new Dimension(0, 3)));
        header.add(subtitle);
        header.add(Box.createRigidArea(new Dimension(0, 8)));
        header.add(statsLabel);

        return header;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Minimal padding

        // Create table container with minimal padding
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(CARD_COLOR);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(50, 50, 60), 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8) // Reduced padding
        ));

        // Create table model
        String[] columns = {"Rank", "Player", "Score", "Date"}; // Shorter column names
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0 || columnIndex == 2) return Integer.class;
                return String.class;
            }
        };

        table = new JTable(tableModel);
        styleTable(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CARD_COLOR);

        // Set preferred size for scroll pane to fit 500x700
        scrollPane.setPreferredSize(new Dimension(450, 350));

        // Custom scrollbar (thinner for small screen)
        JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setUnitIncrement(16); // Faster scrolling
        vertical.setBackground(CARD_COLOR);
        vertical.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(60, 60, 70);
                this.trackColor = CARD_COLOR;
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(0, 0));
                return btn;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(thumbColor);
                g2.fillRoundRect(thumbBounds.x, thumbBounds.y,
                        thumbBounds.width, thumbBounds.height, 6, 6);
                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize(JComponent c) {
                return new Dimension(8, super.getPreferredSize(c).height); // Thinner scrollbar
            }
        });

        // Loading indicator (smaller)
        loadingLabel = new JLabel("Loading data...");
        loadingLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loadingLabel.setForeground(TEXT_SECONDARY);
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loadingLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JPanel loadingPanel = new JPanel(new BorderLayout());
        loadingPanel.setBackground(CARD_COLOR);
        loadingPanel.add(loadingLabel, BorderLayout.CENTER);

        tableContainer.add(loadingPanel, BorderLayout.NORTH);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(tableContainer, BorderLayout.CENTER);

        return centerPanel;
    }

    private JPanel createBottomPanel(ScreenManager manager) {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // Reduced padding

        // Control buttons panel with smaller buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);

        // Refresh button (smaller)
        JButton refreshBtn = createCompactButton("⟳ Refresh", ACCENT_COLOR);
        refreshBtn.addActionListener(e -> loadScores());
        buttonPanel.add(refreshBtn);

        // Back button (smaller)
        JButton backBtn = createCompactButton("← Back", new Color(100, 100, 120));
        backBtn.addActionListener(e -> manager.showWelcomeScreen());
        buttonPanel.add(backBtn);

        bottomPanel.add(buttonPanel);

        // Footer info (smaller)
        String currentUser = UserSession.isLoggedIn() ? UserSession.getUsername() : "Guest";
        if (currentUser.length() > 15) {
            currentUser = currentUser.substring(0, 12) + "...";
        }

        JLabel footerInfo = new JLabel("User: " + currentUser);
        footerInfo.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        footerInfo.setForeground(new Color(100, 100, 120));
        footerInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        bottomPanel.add(footerInfo);

        return bottomPanel;
    }

    private JButton createCompactButton(String text, Color baseColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw button background
                if (getModel().isPressed()) {
                    g2.setColor(baseColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(baseColor.brighter());
                } else {
                    g2.setColor(baseColor);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);

                // Draw text
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                String text = getText();
                int x = (getWidth() - fm.stringWidth(text)) / 2;
                int y = getHeight() / 2 + fm.getAscent() / 2 - 2;
                g2.drawString(text, x, y);

                g2.dispose();
            }
        };

        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Smaller font
        btn.setPreferredSize(new Dimension(120, 35)); // Smaller button
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(35); // Smaller row height for 500x700
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(50, 50, 60));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // Smaller font

        // DISABLE SELECTION
        table.setRowSelectionAllowed(false); // Disable row selection
        table.setColumnSelectionAllowed(false); // Disable column selection
        table.setCellSelectionEnabled(false); // Disable cell selection
        table.setFocusable(false); // Disable focus

        // Remove selection colors since selection is disabled
        table.setSelectionBackground(CARD_COLOR);
        table.setSelectionForeground(TEXT_PRIMARY);

        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        table.setBorder(BorderFactory.createEmptyBorder());

        // Custom cell renderer with NO striping - all rows same color
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                // Minimal padding
                label.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));

                // NO STRIPING - All rows same background color
                label.setBackground(TABLE_ROW_BG);

                // Special styling for top 3 rows (simplified for small screen)
                if (row < 3 && column == 0) {
                    label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    switch(row) {
                        case 0: // 1st place
                            label.setForeground(GOLD);
                            label.setText("🥇");
                            break;
                        case 1: // 2nd place
                            label.setForeground(SILVER);
                            label.setText("🥈");
                            break;
                        case 2: // 3rd place
                            label.setForeground(BRONZE);
                            label.setText("🥉");
                            break;
                    }
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setToolTipText("Rank " + (row + 1));
                }
                // Regular rank styling (4th and below)
                else if (column == 0) {
                    label.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    label.setForeground(TEXT_SECONDARY);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setText("#" + value);
                }
                // Score column
                else if (column == 2) {
                    label.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    label.setForeground(SCORE_GREEN);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                }
                // Date column (compact format)
                else if (column == 3) {
                    label.setFont(new Font("Segoe UI", Font.PLAIN, 10)); // Smaller font for dates
                    label.setForeground(TEXT_SECONDARY);
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    // Shorten date if too long
                    if (value != null && value.toString().length() > 10) {
                        label.setText(value.toString().substring(0, 10));
                        label.setToolTipText(value.toString());
                    }
                }
                // Name column (truncate long names)
                else if (column == 1) {
                    label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    label.setForeground(TEXT_PRIMARY);
                    label.setHorizontalAlignment(SwingConstants.LEFT);

                    // Highlight current user if logged in
                    if (UserSession.isLoggedIn() && value != null) {
                        String currentUser = UserSession.getUsername();
                        if (value.toString().equals(currentUser)) {
                            label.setFont(new Font("Segoe UI", Font.BOLD, 12));
                            label.setForeground(ACCENT_COLOR);
                        }
                    }

                    // Truncate long names
                    if (value != null && value.toString().length() > 15) {
                        String shortName = value.toString().substring(0, 12) + "...";
                        label.setText(shortName);
                        label.setToolTipText(value.toString());
                    }
                }

                // Add subtle separator for rows (thinner)
                if (row < table.getRowCount() - 1) {
                    label.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(50, 50, 60)),
                            BorderFactory.createEmptyBorder(0, 8, 0, 8)
                    ));
                }

                return label;
            }
        });

        // Custom header styling (more compact)
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                label.setBackground(TABLE_HEADER_BG);
                label.setForeground(TEXT_PRIMARY);
                label.setFont(new Font("Segoe UI", Font.BOLD, 12)); // Smaller font
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 2, 0, ACCENT_COLOR),
                        BorderFactory.createEmptyBorder(8, 8, 8, 8) // Reduced padding
                ));

                return label;
            }
        });
        header.setReorderingAllowed(false);
        header.setPreferredSize(new Dimension(0, 35)); // Smaller header

        // Adjust Column Widths for 500px width
        TableColumnModel colModel = table.getColumnModel();
        colModel.getColumn(0).setPreferredWidth(50);   // Rank (compact)
        colModel.getColumn(1).setPreferredWidth(150);  // Name (reduced)
        colModel.getColumn(2).setPreferredWidth(70);   // Score (compact)
        colModel.getColumn(3).setPreferredWidth(100);  // Date (reduced)

        // Auto-adjust columns to fill width
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }

    /**
     * Loads scores using the DbCon class in a background thread
     */
    private void loadScores() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0); // Clear table
            loadingLabel.setVisible(true);
            loadingLabel.setText("Loading...");
            statsLabel.setText("Fetching rankings...");
            statsLabel.setForeground(new Color(150, 200, 255));
        });

        new Thread(() -> {
            try {
                Thread.sleep(300); // Shorter delay

                Object[][] scores = DbCon.getTopScores();

                SwingUtilities.invokeLater(() -> {
                    loadingLabel.setVisible(false);

                    if (scores == null || scores.length == 0) {
                        tableModel.addRow(new Object[]{"-", "No Data", "-", "-"});
                        statsLabel.setText("No data available");
                        statsLabel.setForeground(new Color(231, 76, 60));
                    } else {
                        // Limit to 10 rows for small screen
                        int maxRows = Math.min(scores.length, 10);
                        for (int i = 0; i < maxRows; i++) {
                            int rank = i + 1;
                            String player = (String) scores[i][0];
                            int score = (int) scores[i][1];
                            String date = (String) scores[i][2];

                            tableModel.addRow(new Object[]{rank, player, score, date});
                        }

                        // Update stats (compact)
                        updateStats(scores);
                    }
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    loadingLabel.setText("Error");
                    loadingLabel.setForeground(new Color(231, 76, 60));
                    tableModel.addRow(new Object[]{"!", "Error", "!", "!"});
                    statsLabel.setText("Connection failed");
                    statsLabel.setForeground(new Color(231, 76, 60));
                });
            }
        }).start();
    }

    private void updateStats(Object[][] scores) {
        if (scores == null || scores.length == 0) return;

        int totalPlayers = scores.length;
        int topScore = (int) scores[0][1];
        String topPlayer = (String) scores[0][0];

        // Shorten player name if too long
        if (topPlayer.length() > 12) {
            topPlayer = topPlayer.substring(0, 10) + "..";
        }

        String stats = String.format("Top: %s • Score: %d • Players: %d",
                topPlayer, topScore, totalPlayers);
        statsLabel.setText(stats);
        statsLabel.setForeground(new Color(46, 204, 113));
    }
}