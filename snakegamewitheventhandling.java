import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int WIDTH = 600;
    private final int HEIGHT = 400;
    private final int DOT_SIZE = 10;
    private final int ALL_DOTS = (WIDTH * HEIGHT) / (DOT_SIZE * DOT_SIZE);
    private final int DELAY = 140;

    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];

    private int dots;                   // Length of the snake
    private int apple_x;                // Apple position (x)
    private int apple_y;                // Apple position (y)
    private boolean left = false;       // Direction flags
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;      // Game state
    private Timer timer;                // Timer for game loop

    public SnakeGame() {
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(new TAdapter());  // Register key listener
        initGame();                      // Initialize the game
    }

    private void initGame() {
        dots = 3;                       // Initial length of the snake
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * DOT_SIZE;   // Initial positions of the snake
            y[z] = 50;
        }
        locateApple();                  // Place the first apple
        timer = new Timer(DELAY, this); // Timer for game loop
        timer.start();                  // Start the timer
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            drawApple(g);               // Draw the apple
            drawSnake(g);               // Draw the snake
            Toolkit.getDefaultToolkit().sync(); // Sync the graphics
        } else {
            showGameOver(g);            // Show game over message
        }
    }

    private void drawApple(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(apple_x, apple_y, DOT_SIZE, DOT_SIZE);
    }

    private void drawSnake(Graphics g) {
        for (int z = 0; z < dots; z++) {
            g.setColor(z == 0 ? Color.green : Color.white); // Head vs body color
            g.fillRect(x[z], y[z], DOT_SIZE, DOT_SIZE);
        }
    }

    private void showGameOver(Graphics g) {
        String msg = "Game Over";
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString(msg, WIDTH / 2 - g.getFontMetrics().stringWidth(msg) / 2, HEIGHT / 2);
    }

    private void checkCollision() {
        for (int z = dots; z > 0; z--) {
            if (z > 3 && x[0] == x[z] && y[0] == y[z]) { // Snake bites itself
                inGame = false;
            }
        }
        if (y[0] >= HEIGHT || y[0] < 0 || x[0] >= WIDTH || x[0] < 0) { // Collisions with walls
            inGame = false;
        }
        if (!inGame) {
            timer.stop(); // Stop the game timer
        }
    }

    private void locateApple() {
        apple_x = new Random().nextInt((WIDTH / DOT_SIZE)) * DOT_SIZE;
        apple_y = new Random().nextInt((HEIGHT / DOT_SIZE)) * DOT_SIZE;
    }

    private void move() {
        for (int z = dots; z > 0; z--) {
            x[z] = x[z - 1]; // Move the body of the snake
            y[z] = y[z - 1];
        }
        if (left) {
            x[0] -= DOT_SIZE; // Move left
        }
        if (right) {
            x[0] += DOT_SIZE; // Move right
        }
        if (up) {
            y[0] -= DOT_SIZE; // Move up
        }
        if (down) {
            y[0] += DOT_SIZE; // Move down
        }
    }

    private void checkApple() {
        if (x[0] == apple_x && y[0] == apple_y) {
            dots++; // Increase the length of the snake
            locateApple(); // Place a new apple
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            move();         // Update the snake's position
            checkApple();   // Check if apple is eaten
            checkCollision(); // Check for collisions
        }
        repaint(); // Repaint the game panel
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            processKeyPress(key); // Process the key press event
        }
    }

    private void processKeyPress(int key) {
        if (key == KeyEvent.VK_LEFT && !right) {
            left = true;   // Change direction to left
            up = false;
            down = false;
        }
        if (key == KeyEvent.VK_RIGHT && !left) {
            right = true;  // Change direction to right
            up = false;
            down = false;
        }
        if (key == KeyEvent.VK_UP && !down) {
            up = true;     // Change direction to up
            right = false;
            left = false;
        }
        if (key == KeyEvent.VK_DOWN && !up) {
            down = true;   // Change direction to down
            right = false;
            left = false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Snake Game");
            SnakeGame gamePanel = new SnakeGame();
            frame.add(gamePanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setResizable(false);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        });
    }
}
git add snakegamewitheventhandling.java
git commit -m "Initial commit of Snake game with event handling"
git push origin main
