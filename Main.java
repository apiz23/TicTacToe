package tictactoe;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tic Tac Toe");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(650, 700);
            frame.setLocationRelativeTo(null);

            ScreenManager manager = new ScreenManager(frame);

            frame.setVisible(true);
        });
    }
}