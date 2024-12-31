import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class EnhancedSnakeGame extends JPanel implements ActionListener {
    private static final int WIDTH = 600;          // Game panel width
    private static final int HEIGHT = 400;         // Game panel height
    private static final int DOT_SIZE = 10;         // Size of each dot (snake part and apple)
    private static final int ALL_DOTS = (WIDTH * HEIGHT) / (DOT_SIZE * DOT_SIZE); // Maximum dots
    private static final int INITIAL_DELAY = 140;   // Initial game loop delay
    private static final int LEVEL_UP_SCORE = 5;    // Score to increase speed

    private int[] x = new int[ALL_DOTS];  // Snake x positions
    private int[] y = new int[ALL_DOTS];  // Snake y positions
    private int dots;                     // Length of the snake
    private int apple_x;                  // Apple x position
    private int apple_y;                  // Apple y position
    private int score;                    // Player's score
    private boolean left = false;         // Direction flags
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;        // Game state
    private Timer timer;                  // Timer for game loop

    public EnhancedSnakeGame() {
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(new TAdapter());  // Register key listener
        initGame();                     // Initialize the game
    }

    private void initGame() {
        dots = 3;                      // Initial length of the snake
        score = 0;                     // Initial score
        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * DOT_SIZE;  // Initial positions of the snake
            y[z] = 50;
        }
        locateApple();                 // Place the first apple
        timer = new Timer(INITIAL_DELAY, this); // Timer for game loop
        timer.start();                 // Start the timer
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            drawApple(g);               // Draw the apple
            drawSnake(g);               // Draw the snake
            drawScore(g);               // Draw the score
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

    private void drawScore(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Score: " + score, 10, 20); // Display score
    }

    private void showGameOver(Graphics g) {
        String msg = "Game Over! Final Score: " + score;
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString(msg, WIDTH / 2 - g.getFontMetrics().stringWidth(msg) / 2, HEIGHT / 2);
    }

    private void checkCollision() {
        // Check for self-collision
        for (int z = dots; z > 0; z--) {
            if (z > 3 && x[0] == x[z] && y[0] == y[z]) { 
                inGame = false;
            }
        }

        // Check for wall collision
        if (y[0] >= HEIGHT || y[0] < 0 || x[0] >= WIDTH || x[0] < 0) { 
            inGame = false;
        }
        
        // Stop the timer if the game is over
        if (!inGame) {
            timer.stop(); 
        }
    }

    private void locateApple() {
        apple_x = new Random().nextInt((WIDTH / DOT_SIZE)) * DOT_SIZE;
        apple_y = new Random().nextInt((HEIGHT / DOT_SIZE)) * DOT_SIZE;
    }

    private void move() {
        // Move the body of the snake
        for (int z = dots; z > 0; z--) {
            x[z] = x[z - 1]; 
            y[z] = y[z - 1];
        }
        // Change the head's position based on the direction
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
        // Check if the snake head has eaten the apple
        if (x[0] == apple_x && y[0] == apple_y) {
            dots++; // Increase the length of the snake
            score++; // Increase the score
            if (score % LEVEL_UP_SCORE == 0) { // Increase speed every LEVEL_UP_SCORE
                timer.setDelay(Math.max(20, timer.getDelay() - 10)); // Speed up the game
            }
            locateApple(); // Place a new apple
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            move();          // Update the snake's position
            checkApple();    // Check if apple is eaten
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
        // Validate and update the direction of the snake based on key press
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
            JFrame frame = new JFrame("Enhanced Snake Game");
            EnhancedSnakeGame gamePanel = new EnhancedSnakeGame();
            frame.add(gamePanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.setResizable(false);
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
        });
    }
}
