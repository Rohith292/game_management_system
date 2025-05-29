package games.FlappyBird;

import database.ScoreDAO;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    private final int boardWidth = 360;
    private final int boardHeight = 640;
    private int score = 0;
    private boolean gameOver = false;
    private String username;

    private Image backgroundImg, birdImg, topPipeImg, bottomPipeImg;
    private int birdX = boardWidth / 8, birdY = boardHeight / 2;
    private int birdWidth = 34, birdHeight = 24, velocityY = 0;
    private final int gravity = 1;

    private final int pipeWidth = 64, pipeHeight = 512;
    private final ArrayList<Pipe> pipes = new ArrayList<>();
    private final Random random = new Random();
    private final Timer gameLoop, pipeSpawnTimer;

    class Bird { int x = birdX, y = birdY, width = birdWidth, height = birdHeight; Image img; Bird(Image img) { this.img = img; } }
    class Pipe { int x, y, width = pipeWidth, height = pipeHeight; Image img; boolean passed = false; Pipe(Image img, int x, int y) { this.img = img; this.x = x; this.y = y; } }
    private Bird bird;

    public FlappyBird(String username) {
        this.username = username;
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        backgroundImg = new ImageIcon(getClass().getResource("/games/FlappyBird/flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("/games/FlappyBird/flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("/games/FlappyBird/toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("/games/FlappyBird/bottompipe.png")).getImage();
        bird = new Bird(birdImg);

        pipeSpawnTimer = new Timer(1500, e -> spawnPipes());
        pipeSpawnTimer.start();

        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(boardWidth, boardHeight);
    }

    private void spawnPipes() {
        int randomPipeY = -pipeHeight / 4 - random.nextInt(pipeHeight / 2);
        int gapSize = boardHeight / 4;
        int pipeX = boardWidth;
        pipes.add(new Pipe(topPipeImg, pipeX, randomPipeY));
        pipes.add(new Pipe(bottomPipeImg, pipeX, randomPipeY + pipeHeight + gapSize));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), null); // ✅ Properly scales background
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);
        for (Pipe pipe : pipes) g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        g.drawString(gameOver ? "Game Over: " + score : String.valueOf(score), 10, 35);
    }

    private void move() {
        if (gameOver) return;

        velocityY += gravity;
        bird.y += velocityY;

        for (Pipe pipe : pipes) {
            pipe.x -= 4;
            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score++;
                pipe.passed = true;
            }
            if (checkCollision(bird, pipe)) endGame();
        }

        if (bird.y > boardHeight || bird.y < 0) endGame();
    }

    private boolean checkCollision(Bird a, Pipe b) {
        return a.x < b.x + b.width && a.x + a.width > b.x &&
               a.y < b.y + b.height && a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9;
            if (gameOver) restartGame();
        }
    }

    private void restartGame() {
        bird.y = birdY;
        velocityY = 0;
        pipes.clear();
        gameOver = false;
        score = 0;
        gameLoop.start();
        pipeSpawnTimer.start();
    }

    private void endGame() {
        gameOver = true;
        pipeSpawnTimer.stop();
        gameLoop.stop();
        JOptionPane.showMessageDialog(null, "Game Over! Your score: " + score);
        ScoreDAO.saveScore(username, "flappybird", score);
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}
