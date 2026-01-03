package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class LoginScreen extends JPanel {

    private final Color BG_COLOR = new Color(240, 240, 245);
    private final Color ACCENT_BLUE = new Color(70, 130, 180);

    public LoginScreen(ScreenManager manager) {
        setLayout(new BorderLayout());
        setBackground(BG_COLOR);

        JLabel title = new JLabel("TIC TAC TOE", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 48));
        title.setForeground(new Color(50, 50, 50));
        title.setBorder(BorderFactory.createEmptyBorder(80, 0, 30, 0));
        add(title, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JLabel nameLabel = new JLabel("Enter Player Name:");
        nameLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        nameLabel.setForeground(Color.DARK_GRAY);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(nameLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JTextField nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(250, 45));
        nameField.setMaximumSize(new Dimension(250, 45));
        nameField.setFont(new Font("SansSerif", Font.PLAIN, 18));
        nameField.setHorizontalAlignment(JTextField.CENTER);
        nameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(nameField);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JButton loginBtn = new JButton("Enter Game");
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        loginBtn.setBackground(ACCENT_BLUE);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setPreferredSize(new Dimension(200, 50));
        loginBtn.setMaximumSize(new Dimension(200, 50));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        loginBtn.addActionListener(e -> {
            String rawName = nameField.getText().trim();

            if (rawName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid name.");
                return;
            }

            loginBtn.setEnabled(false);
            loginBtn.setText("Checking ID...");

            String deviceName = getDeviceName();

            new Thread(() -> {
                boolean success = DbCon.loginOrRegister(rawName, deviceName);

                SwingUtilities.invokeLater(() -> {
                    if (success) {
                        UserSession.login(rawName, deviceName);
                        manager.showWelcomeScreen();
                    } else {
                        loginBtn.setEnabled(true);
                        loginBtn.setText("Enter Game");

                        JOptionPane.showMessageDialog(
                                this,
                                "Player name already taken.\nPlease choose another name.",
                                "Name Exists",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                });
            }).start();
        });

        contentPanel.add(loginBtn);
        centerWrapper.add(contentPanel);
        add(centerWrapper, BorderLayout.CENTER);
    }

    private String getDeviceName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "UnknownDevice";
        }
    }
}
