import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Tetris extends JPanel {
    private int[][] board = new int[20][10];
    private Shape currentShape;
    private int curX;
    private int curY;
    private Timer timer;

    public Tetris() {
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_LEFT;
                    move(-1);
                    break;

                    case KeyEvent.VK_RIGHT;
                    move(1);
                    break;

                    case KeyEvent.VK_DOWN;
                    down();
                    break;

                    case KeyEvent.VK_UP;
                    rotate();
                    break;
                }
            }
        });

        currentShape = new Shape();
        curX=3;
        curY=0;
    }
    
    public void start() {
        timer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateGameState();
                repaint();
            }
        });
        timer.start();
    }

    private void updateGameState() {
        
    }
    private boolean down() {

    }
    private void move(int x) {

    }
    private void drop() {

    }
    private void rotate() {

    }
    private void isCollision() {

    }
    private void placeShape() {

    }
    private void clearLines() {

    }
    private boolean spawnNewShape() {

    }
    private void paint(Graphics g) {

    }
    private void drawBoard(Graphics g) {

    }
    private void drawShape(Graphics g) {

    }
}
