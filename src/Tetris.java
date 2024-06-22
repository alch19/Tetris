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
                    case KeyEvent.VK_LEFT:
                    move(-1);
                    break;

                    case KeyEvent.VK_RIGHT:
                    move(1);
                    break;

                    case KeyEvent.VK_DOWN:
                    down();
                    break;

                    case KeyEvent.VK_UP:
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
        if(!down()) {
            placeShape();
            clearRow();
            if(!spawnNewShape()) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game Over!");
            }
        }
    }
    private boolean down() {
        if(!isCollision(curX, curY+1)) { // checks if it can move
            curY++;
            return true;
        }
        return false;
    }
    private void move(int x) {
        if(!isCollision(curX+x, curY)) { // checks if it can move
            curX+=x;
        }
        repaint();
    }
    private void drop() {
        while (down()); // keeps running until false
        placeShape();
        clearRow();
        if (!spawnNewShape()) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over");
        }
    }
    private void rotate() {
        // rotate shape
    }
    private boolean isCollision(int x, int y) {
        for(int[] coord : currentShape.getCoordinates()) {
            int theX = x + coord[0];
            int theY = y + coord[1];
            if (theX < 0 || theX >= board[0].length || theY < 0 || theY >= board.length || board[theY][theX] != 0) {
                return true;
            }
        }
        return false;
    }
    private void placeShape() {
        for(int[] coord : currentShape.getCoordinates()) {
            board[curY + coord[1]][curX+coord[0]] = 1;
        }
    }
    private void clearRow() {
        // clear row
    }
    private boolean spawnNewShape() {
        this.currentShape = new Shape();
        curX = 3;
        curY = 0;
        return !isCollision(curX, curY);
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawShape(g);
    }
    private void drawBoard(Graphics g) {
        
    }
    private void drawShape(Graphics g) {

    }
}
