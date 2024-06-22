import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Tetris game = new Tetris();
        JFrame frame = new JFrame("Tetris");

        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(400,800);
        frame.add(game);
        frame.setVisible(true);
    }
}