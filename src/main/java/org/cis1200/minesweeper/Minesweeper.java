package org.cis1200.minesweeper;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;



public class Minesweeper extends JPanel {

    public static final int ROWS = 16;
    public static final int COLUMNS = 16;
    public static final int MAX_MINES = 40;


    private Cell[][] board;
    private int flagsLeft;
    private int minesLeft;
    private boolean gameOver;


    private JLabel status; // current status text

    static final String SAVE_PATH = "files/save.csv";
    private static BufferedImage one;
    private static BufferedImage two;
    private static BufferedImage three;
    private static BufferedImage four;
    private static BufferedImage five;
    private static BufferedImage six;
    private static BufferedImage seven;
    private static BufferedImage eight;
    private static BufferedImage empty;
    private static BufferedImage exploded;
    private static BufferedImage flag;
    private static BufferedImage mine;
    private static BufferedImage unknown;



    public Minesweeper(JLabel statusInit) {
        status = statusInit;
        initGame();



        try {
            one = ImageIO.read(new File("files/Tile1.png"));
            two = ImageIO.read(new File("files/Tile2.png"));
            three = ImageIO.read(new File("files/Tile3.png"));
            four = ImageIO.read(new File("files/Tile4.png"));
            five = ImageIO.read(new File("files/Tile5.png"));
            six = ImageIO.read(new File("files/Tile6.png"));
            seven = ImageIO.read(new File("files/Tile7.png"));
            eight = ImageIO.read(new File("files/Tile8.png"));

            empty = ImageIO.read(new File("files/TileEmpty.png"));
            exploded = ImageIO.read(new File("files/TileExploded.png"));
            flag = ImageIO.read(new File("files/TileFlag.png"));
            mine = ImageIO.read(new File("files/TileMine.png"));
            unknown = ImageIO.read(new File("files/TileUnknown.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                if (!gameOver) {
                    // updates the model given the coordinates of the mouseclick
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        excavate(p.x / 16, p.y / 16);
                    }

                    if (SwingUtilities.isRightMouseButton(e)) {
                        flag(p.x / 16, p.y / 16);
                    }

                    repaint(); // repaints the game board
                }
            }
        });
    }

    public void excavate(int c, int r) {
        Cell cell = board[r][c];

        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length
                || cell.isFlagged() || !cell.isCovered()) {
            return;
        }

        if (cell.hasMine()) {
            cell.uncover();
            gameOver = true;
        } else if (cell.getSurroundingMines() == 0) {
            floodFillCells(r, c);
        } else {
            cell.uncover();
        }

        repaint();
    }

    public void flag(int c, int r) {
        Cell cell = board[r][c];

        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length
                || !cell.isCovered()) {
            return;
        }


        if (cell.hasMine() && !cell.isFlagged()) {
            flagsLeft--;
            minesLeft--;
            if (minesLeft == 0) {
                gameOver = true;
            }
        } else if (cell.hasMine() && cell.isFlagged()) {
            flagsLeft++;
            minesLeft++;
        } else if (cell.isFlagged()) {
            flagsLeft++;
        } else {
            flagsLeft--;
        }

        cell.flag();
        status.setText(flagsLeft + " flags remaining");

        repaint();
    }


    public void floodFillCells(int r, int c) {
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) {
            return;
        }

        Cell cell = board[r][c];

        if (!cell.isCovered() || cell.hasMine()) {
            return;
        }

        cell.uncover();

        if (cell.getSurroundingMines() == 0) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if (x == 0 && y == 0) {
                        continue;
                    }
                    floodFillCells(r + x, c + y);
                }
            }
        }

    }
    public void initGame() {
        initCells(ROWS, COLUMNS);
        flagsLeft = MAX_MINES;
        minesLeft = MAX_MINES;
        placeMines(MAX_MINES);
        gameOver = false;

        status.setText(flagsLeft + " flags remaining");
        setSurrounding();
        repaint();
    }

    public void setSurrounding() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].hasMine()) {
                    for (int di = -1; di <= 1; di++) {
                        for (int dj = -1; dj <= 1; dj++) {
                            int ni = i + di;
                            int nj = j + dj;

                            if (ni >= 0 && ni < board.length && nj >= 0 && nj < board[0].length) {
                                if (di != 0 || dj != 0) {
                                    board[ni][nj].addSurrounding();
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public void placeMines(int numMines) {
        int mines = numMines;
        int r;
        int c;

        while (mines > 0) {
            r = (int) (Math.random() * 16);
            c = (int) (Math.random() * 16);
            if (!board[r][c].hasMine()) {
                board[r][c].placeMine();
                mines--;
            }

        }
    }

    public void initCells(int rows, int cols) {
        board = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                board[i][j] = new Cell();
            }
        }
    }

    public void paint(Graphics g) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLUMNS; c++) {
                Cell cell = board[r][c];
                BufferedImage img = getImage(cell);
                int x = c * img.getWidth();
                int y = r * img.getHeight();
                g.drawImage(img, x, y, this);

            }
        }

        if (gameOver) {
            if (minesLeft == 0) {
                status.setText("you won hooray!!!!!!!!!!");
            } else {
                status.setText("you died :(");
            }
        }
    }

    public BufferedImage getImage(Cell cell) {
        BufferedImage img;
        switch (cell.getSurroundingMines()) {
            case 1:
                img = one;
                break;
            case 2:
                img = two;
                break;
            case 3:
                img = three;
                break;
            case 4:
                img = four;
                break;
            case 5:
                img = five;
                break;
            case 6:
                img = six;
                break;
            case 7:
                img = seven;
                break;
            case 8:
                img = eight;
                break;
            default:
                img = empty;
                break;
        }

        if (gameOver) {
            if (cell.isCovered() && cell.hasMine()) {
                cell.uncover();
                img = mine;
            } else if (!cell.isCovered() && cell.hasMine()) {
                img = exploded;
            } else if (cell.isFlagged()) {
                img = flag;
            } else {
                img = empty;
            }
        } else {
            if (cell.isFlagged()) {
                img = flag;
            } else if (cell.isCovered()) {
                img = unknown;
            }
        }

        return img;
    }


    public void saveToFile() {
        try {
            if (gameOver) {
                return;
            } else {
                File save = new File(SAVE_PATH);

                FileWriter writer = new FileWriter(SAVE_PATH);

                if (!save.createNewFile()) {
                    writer.flush();
                }

                for (int r = 0; r < ROWS; r++) {
                    StringBuilder line = new StringBuilder();
                    for (int c = 0; c < COLUMNS; c++) {
                        line.append(board[r][c].toString());
                        if (c != COLUMNS - 1) {
                            line.append(',');
                        }
                    }
                    line.append("\n");
                    writer.write(line.toString());
                }

                writer.write(flagsLeft + " " + minesLeft);

                writer.close();

                status.setText("file saved successfully");

                repaint();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSavedFile() {
        try {
            Scanner reader = new Scanner(new FileReader(SAVE_PATH));
            initGame();

            try {
                for (int r = 0; r < ROWS; r++) {
                    String line = reader.nextLine();
                    String[] cells = line.split(",");
                    for (int c = 0; c < COLUMNS; c++) {

                        String[] vars = cells[c].split(" ");
                        int sur = Integer.valueOf(vars[0]);
                        boolean flag = Boolean.parseBoolean(vars[1]);
                        boolean cover = Boolean.parseBoolean(vars[2]);
                        boolean mine = Boolean.parseBoolean(vars[3]);

                        board[r][c] = new Cell(sur, flag, cover, mine);
                    }
                }

                String line = reader.nextLine();
                String[] vars = line.split(" ");
                flagsLeft = Integer.valueOf(vars[0]);
                minesLeft = Integer.valueOf(vars[1]);

                status.setText("file loaded successfully");
            } catch (Error e) {
                status.setText("incorrect file format");
                e.printStackTrace();
            }

            reader.close();
            repaint();
        } catch (IOException e) {
            status.setText("no saved game");
            repaint();
            e.printStackTrace();
        }
    }

    public Cell[][] getBoard() {
        return board;
    }

    public int getFlagsLeft() {
        return flagsLeft;
    }

    public int getMinesLeft() {
        return minesLeft;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}