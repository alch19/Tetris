import java.awt.BorderLayout;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Tetris game = new Tetris();
        JFrame frame = new JFrame("Tetris");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,800);
        frame.setLocationRelativeTo(null);

        JPanel container = new JPanel(new BorderLayout());
        container.add(game, BorderLayout.CENTER);
        
        frame.setResizable(false);
        frame.add(container, BorderLayout.SOUTH);
        frame.setVisible(true);

        game.start();
    }
}