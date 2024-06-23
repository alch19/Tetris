import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Tetris extends JPanel {
    private int[][] board = new int[20][10];
    private Shape currentShape;
    private Shape nextShape;
    private Shape heldShape=null;
    private boolean isHolding=false;
    private int curX;
    private int curY;
    private Timer timer;
    private final int blockSize = 30;
    private final int width = blockSize * board[0].length;
    private final int height = blockSize * board.length;
    private boolean keyPressedDown = false;

    public Tetris() {
        setFocusable(true);
        setPreferredSize(new Dimension(width, height+100)); 

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        move(-1);
                    } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        move(1);
                    } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        keyPressedDown = true;
                    } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                        rotate();
                    } else if (e.getKeyCode() == KeyEvent.VK_R) {
                        holdPiece();
                    }

                } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                    if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                        keyPressedDown = false;
                    }
                }
                repaint();
                return false;
            }
        });

        currentShape = new Shape();
        curX=3;
        curY=0;
    }
    
    public void start() {
        timer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(keyPressedDown==false) {
                    updateGameState();
                }
                repaint();
            }
        });
        timer.start();

        Timer downTimer = new Timer(50, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (keyPressedDown) {
                    stepDown();
                }
                repaint();
            }
        });
        downTimer.start();
    }

    private void holdPiece() {
        if(isHolding==false) {
            return;
        }
        if(heldShape==null) {
            heldShape=currentShape;
            currentShape=nextShape;
            nextShape=new Shape();
        } else {
            Shape temp = currentShape;
            currentShape = heldShape;
            heldShape=temp;
        }
        curX=3;
        curY=0;
        isHolding=true;
    }

    private void updateGameState() {
        isHolding=false;
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
    private void stepDown() {
        if(!down()) {
            placeShape();
            clearRow();
            if (!spawnNewShape()) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Game Over");
            }
            repaint();
        }
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
        for (int[] coord : currentShape.getCoordinates()) {
            int theX = x + coord[0];
            int theY = y + coord[1];
            System.out.println("Checking collision at (" + theX + ", " + theY + ")");
            if (theX < 0 || theX >= board[0].length || theY < 0 || theY >= board.length || board[theY][theX] != 0) {
                System.out.println("Collision detected at (" + theX + ", " + theY + ")");
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
        for(int i=0; i<board.length; i++) {
            boolean lineFull = true;
            for(int j=0; j<board[i].length; j++) {
                if(board[i][j] == 0) {
                    lineFull = false;
                    break;
                }
            }
            if(lineFull) {
                for(int k=i; k>0; k--) {
                    for(int x=0; x<board[k].length; x++) {
                        board[k][x]=board[k-1][x];
                    }
                }
                for(int x=0; x<board[0].length; x++) {
                    board[0][x]=0;
                }
            }
        }
    }
    private boolean spawnNewShape() {
        this.currentShape = new Shape();
        curX = 3;
        curY = 0;
        for (int i = 0; i < board.length; i++) {
        if (!isCollision(curX, curY)) {
            break;
        }
        curY++;
    }
        if(isCollision(curX,curY)) {
            return false;
        }
        return true;
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        drawShape(g);
        drawGridLines(g);
        drawNextPiece(g);
        drawHoldPiece(g);
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

    private void drawNextPiece(Graphics g) {
        int offsetX = (getWidth() - width) / 2 + width + 20;
        int offsetY = 20;
        g.setColor(Color.GREEN);
        for (int[] coord : nextShape.getCoordinates()) {
            int x = coord[0];
            int y = coord[1];
            g.fillRect(offsetX + x * blockSize, offsetY + y * blockSize, blockSize, blockSize);
            g.setColor(Color.BLACK);
            g.drawRect(offsetX + x * blockSize, offsetY + y * blockSize, blockSize, blockSize);
            g.setColor(Color.GREEN);
        }
        g.setColor(Color.BLACK);
        g.drawString("Next Piece", offsetX, offsetY - 10);
    }

    private void drawHoldPiece(Graphics g) {
        if (heldShape == null) return;
        int offsetX = (getWidth() - width) / 2 - 120;
        int offsetY = 20;
        g.setColor(Color.YELLOW);
        for (int[] coord : heldShape.getCoordinates()) {
            int x = coord[0];
            int y = coord[1];
            g.fillRect(offsetX + x * blockSize, offsetY + y * blockSize, blockSize, blockSize);
            g.setColor(Color.BLACK);
            g.drawRect(offsetX + x * blockSize, offsetY + y * blockSize, blockSize, blockSize);
            g.setColor(Color.YELLOW);
        }
        g.setColor(Color.BLACK);
        g.drawString("Hold Piece", offsetX, offsetY - 10);
    }
}
