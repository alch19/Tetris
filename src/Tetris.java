import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Tetris extends JPanel {
    private int[][] board = new int[20][10];
    private Shape currentShape;
    private int curX;
    private int curY;
    private Timer timer;
    private final int blockSize = 30;
    private final int width = blockSize * board[0].length;
    private final int height = blockSize * board.length;

    public Tetris() {
        setFocusable(true);
        setPreferredSize(new Dimension(width, height)); 
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
                    drop();
                    break;

                    case KeyEvent.VK_UP:
                    rotate();
                    break;
                }
                repaint();
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
        if(!isCollision(curX, curY+1)) {
            curY++;
            return true;
        }
        return false;
    }
    private void move(int x) {
        if(!isCollision(curX+x, curY)) {
            curX+=x;
        }
        repaint();
    }
    private void drop() {
        while (down());
        placeShape();
        clearRow();
        if (!spawnNewShape()) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over");
        }
        repaint();
    }

    private void rotate() {
        int[][] rotatedShape = new int[currentShape.getCoordinates().length][2];
        for (int i = 0; i < currentShape.getCoordinates().length; i++) {
            rotatedShape[i][0] = -currentShape.getCoordinates()[i][1];
            rotatedShape[i][1] = currentShape.getCoordinates()[i][0];
        }

        boolean canRotate = true;
        for (int[] coord : rotatedShape) {
            int newX = curX + coord[0];
            int newY = curY + coord[1];
            if (newX < 0 || newX >= board[0].length || newY < 0 || newY >= board.length || board[newY][newX] != 0) {
                canRotate = false;
                break;
            }
        }

        if (canRotate) {
            for (int i = 0; i < currentShape.getCoordinates().length; i++) {
                currentShape.getCoordinates()[i][0] = rotatedShape[i][0];
                currentShape.getCoordinates()[i][1] = rotatedShape[i][1];
            }
        }
        repaint();
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
        drawGridLines(g);
    }
    private void drawBoard(Graphics g) {
        int offsetX = (getWidth()-width)/2;
        int offsetY = (getHeight()-height)/2;
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] != 0) {
                    g.setColor(Color.BLUE);
                    g.fillRect(offsetX + x * blockSize, offsetY + y * blockSize, blockSize, blockSize);
                    g.setColor(Color.BLACK);
                    g.drawRect(offsetX + x * blockSize, offsetY + y * blockSize, blockSize, blockSize);
                }
            }
        }
    }

    private void drawShape(Graphics g) {
        int offsetX = (getWidth() - width) / 2; 
        int offsetY = (getHeight() - height) / 2;
        g.setColor(Color.RED);
        for (int[] coord : currentShape.getCoordinates()) {
            int x = curX + coord[0];
            int y = curY + coord[1];
            g.fillRect(offsetX + x * blockSize, offsetY + y * blockSize, blockSize, blockSize);
            g.setColor(Color.BLACK);
            g.drawRect(offsetX + x * blockSize, offsetY + y * blockSize, blockSize, blockSize);
            g.setColor(Color.RED);
        }
    }

    private void drawGridLines(Graphics g) {
        int offsetX = (getWidth() - width) / 2;
        int offsetY = (getHeight() - height) / 2;
        g.setColor(Color.LIGHT_GRAY);
        for (int y = 0; y <= board.length; y++) {
            g.drawLine(offsetX, offsetY + y * blockSize, offsetX + width, offsetY + y * blockSize);
        }
        for (int x = 0; x <= board[0].length; x++) {
            g.drawLine(offsetX + x * blockSize, offsetY, offsetX + x * blockSize, offsetY + height);
        }
    }
}
