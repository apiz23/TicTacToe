package tictactoe;

import javax.swing.*;
import java.awt.*;

public class WelcomeScreen extends JPanel {

    public WelcomeScreen(ScreenManager manager) {
        setLayout(new BorderLayout());
        setBackground(new Color(240, 240, 245));

        JLabel title = new JLabel("TIC TAC TOE", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 48));
        title.setForeground(new Color(50, 50, 50));
        title.setBorder(BorderFactory.createEmptyBorder(80, 0, 50, 0));
        add(title, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel contentPanel = new JPanel(new GridLayout(0, 1, 20, 20));
        contentPanel.setOpaque(false);
        contentPanel.setPreferredSize(new Dimension(350, 250));

        // ==========================
        // BUTTONS
        // ==========================

        JButton startGameBtn = createStyledButton("Start Game");
        startGameBtn.addActionListener(e -> manager.showGameModeScreen());
        contentPanel.add(startGameBtn);

        JButton scoreboardBtn = createStyledButton("Scoreboard");
        scoreboardBtn.addActionListener(e -> manager.showScoreboardScreen());
        contentPanel.add(scoreboardBtn);

        JButton exitBtn = createStyledButton("Exit");
        exitBtn.addActionListener(e -> System.exit(0));
        contentPanel.add(exitBtn);

        centerWrapper.add(contentPanel);
        add(centerWrapper, BorderLayout.CENTER);

        JPanel footer = new JPanel();
        footer.setPreferredSize(new Dimension(0, 100));
        footer.setOpaque(false);
        add(footer, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("SansSerif", Font.BOLD, 22));
        btn.setFocusPainted(false);
        btn.setBackground(new Color(70, 130, 180));
        btn.setForeground(Color.WHITE);
        return btn;
    }
}