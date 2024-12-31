package com.snakegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private final int TILE_SIZE = 25;
    private final int GRID_SIZE = 20;
    private final int SCREEN_SIZE = TILE_SIZE * GRID_SIZE;

    private LinkedList<Point> snake = new LinkedList<>();
    private Point food;
    private String direction = "RIGHT";
    private boolean gameOver = false;

    private Timer timer;
    private int score = 0;

    public SnakeGame() {
        setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new SnakeKeyAdapter());

        initGame();
    }

    private void initGame() {
        snake.clear();
        snake.add(new Point(5, 5));
        spawnFood();

        direction = "RIGHT";
        gameOver = false;
        score = 0;

        timer = new Timer(150, this);
        timer.start();
    }

    private void spawnFood() {
        Random random = new Random();
        do {
            food = new Point(random.nextInt(GRID_SIZE), random.nextInt(GRID_SIZE));
        } while (snake.contains(food));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGame(g);
    }

    private void drawGame(Graphics g) {
        if (gameOver) {
            showGameOver(g);
            return;
        }

        // Draw snake
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        // Draw score
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 10);
    }

    private void moveSnake() {
        Point head = snake.getFirst();
        Point newHead = new Point(head);

        switch (direction) {
            case "UP" -> newHead.y--;
            case "DOWN" -> newHead.y++;
            case "LEFT" -> newHead.x--;
            case "RIGHT" -> newHead.x++;
        }

        if (newHead.equals(food)) {
            snake.addFirst(newHead);
            score++;
            spawnFood();
        } else {
            if (newHead.x < 0 || newHead.x >= GRID_SIZE || newHead.y < 0 || newHead.y >= GRID_SIZE || snake.contains(newHead)) {
                gameOver = true;
                timer.stop();
            } else {
                snake.addFirst(newHead);
                snake.removeLast();
            }
        }
    }

    private void showGameOver(Graphics g) {
        g.setColor(Color.RED);
        g.drawString("Game Over! Your score: " + score, SCREEN_SIZE / 2 - 50, SCREEN_SIZE / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            moveSnake();
        }
        repaint();
    }

    private class SnakeKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_UP && !direction.equals("DOWN")) direction = "UP";
            if (key == KeyEvent.VK_DOWN && !direction.equals("UP")) direction = "DOWN";
            if (key == KeyEvent.VK_LEFT && !direction.equals("RIGHT")) direction = "LEFT";
            if (key == KeyEvent.VK_RIGHT && !direction.equals("LEFT")) direction = "RIGHT";
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();

        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
