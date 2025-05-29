package games.Minesweeper;

import database.ScoreDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class Minesweeper {
    private class MineTile extends JButton {
        int r, c;

        public MineTile(int r, int c) {
            super();
            this.r = r;
            this.c = c;
            setFocusable(false);
            setMargin(new Insets(0, 0, 0, 0));
            setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
            setOpaque(true);
        }
    }

    int tileSize = 70;
    int numRows = 8;
    int numCols = numRows;
    int boardWidth = numCols * tileSize;
    int boardHeight = numRows * tileSize;

    JFrame frame = new JFrame("Minesweeper");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();
    JButton retryButton = new JButton("Retry"); // Retry button

    int mineCount = 10;
    MineTile[][] board = new MineTile[numRows][numCols];
    ArrayList<MineTile> mineList;
    Random random = new Random();

    int tilesClicked = 0;
    boolean gameOver = false;
    private String username; // ✅ Store username for score tracking

    public Minesweeper(String username) { // ✅ Accept username parameter
        this.username = username;
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Minesweeper: " + mineCount);
        textLabel.setOpaque(true);

        retryButton.setFont(new Font("Arial", Font.BOLD, 18));
        retryButton.addActionListener(e -> restartGame()); // Reset game on click

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel, BorderLayout.CENTER);
        textPanel.add(retryButton, BorderLayout.EAST);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(numRows, numCols));
        frame.add(boardPanel);

        initializeBoard();
        frame.setVisible(true);
    }

    void restartGame() {
        gameOver = false;
        tilesClicked = 0;
        textLabel.setText("Minesweeper: " + mineCount);

        boardPanel.removeAll();
        initializeBoard();
        frame.revalidate();
        frame.repaint();
    }

    void initializeBoard() {
        board = new MineTile[numRows][numCols];
        mineList = new ArrayList<>();

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = new MineTile(r, c);
                board[r][c] = tile;

                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) return;
                        MineTile tile = (MineTile) e.getSource();

                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (tile.getText().equals("")) {
                                if (mineList.contains(tile)) {
                                    revealMines();
                                } else {
                                    checkMine(tile.r, tile.c);
                                }
                            }
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            if (tile.getText().equals("") && tile.isEnabled()) {
                                tile.setText("🚩");
                            } else if (tile.getText().equals("🚩")) {
                                tile.setText("");
                            }
                        }
                    }
                });

                boardPanel.add(tile);
            }
        }

        setMines();
    }

    void setMines() {
        mineList = new ArrayList<>();
        int mineLeft = mineCount;
        while (mineLeft > 0) {
            int r = random.nextInt(numRows);
            int c = random.nextInt(numCols);

            MineTile tile = board[r][c];
            if (!mineList.contains(tile)) {
                mineList.add(tile);
                mineLeft--;
            }
        }
    }

    void revealMines() {
        for (MineTile tile : mineList) {
            tile.setText("💣");
        }
        gameOver = true;
        textLabel.setText("Game Over!");

        ScoreDAO.saveScore(username, "Minesweeper", tilesClicked); // ✅ Save score when losing
        System.out.println("✅ Score Saved: " + tilesClicked);
    }

    void checkMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return;
        }

        MineTile tile = board[r][c];
        if (!tile.isEnabled()) {
            return;
        }

        tile.setEnabled(false);
        tilesClicked++;

        int minesFound = countNearbyMines(r, c);
        tile.setText(minesFound > 0 ? Integer.toString(minesFound) : "");

        if (minesFound == 0) {
            checkSurroundingTiles(r, c);
        }

        if (tilesClicked == numRows * numCols - mineList.size()) {
            gameOver = true;
            textLabel.setText("Mines Cleared!");

            ScoreDAO.saveScore(username, "Minesweeper", tilesClicked); // ✅ Save score when winning
            System.out.println("✅ Game Won! Score Saved: " + tilesClicked);
        }
    }

    int countNearbyMines(int r, int c) {
        int count = 0;
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, 
                              {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        for (int[] dir : directions) {
            int newRow = r + dir[0];
            int newCol = c + dir[1];
            if (newRow >= 0 && newRow < numRows && newCol >= 0 && newCol < numCols) {
                if (mineList.contains(board[newRow][newCol])) {
                    count++;
                }
            }
        }
        return count;
    }

    void checkSurroundingTiles(int r, int c) {
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, 
                              {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        for (int[] dir : directions) {
            checkMine(r + dir[0], c + dir[1]);
        }
    }
}
