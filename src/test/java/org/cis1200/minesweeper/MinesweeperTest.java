package org.cis1200.minesweeper;

import org.junit.jupiter.api.*;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class MinesweeperTest {

    private Minesweeper game;
    private JLabel status;

    private void setupTestBoard(Cell[][] testBoard) {
        game.initCells(Minesweeper.ROWS, Minesweeper.COLUMNS);
        for (int i = 0; i < testBoard.length; i++) {
            for (int j = 0; j < testBoard[0].length; j++) {
                game.getBoard()[i][j] = testBoard[i][j];
            }
        }
    }

    private Cell[][] createEmptyBoard() {
        Cell[][] board = new Cell[Minesweeper.ROWS][Minesweeper.COLUMNS];
        for (int i = 0; i < Minesweeper.ROWS; i++) {
            for (int j = 0; j < Minesweeper.COLUMNS; j++) {
                board[i][j] = new Cell();
            }
        }
        return board;
    }

    private Cell[][] createBoardWithAllMines() {
        Cell[][] board = new Cell[Minesweeper.ROWS][Minesweeper.COLUMNS];
        for (int i = 0; i < Minesweeper.ROWS; i++) {
            for (int j = 0; j < Minesweeper.COLUMNS; j++) {
                board[i][j] = new Cell();
                board[i][j].placeMine();
            }
        }
        return board;
    }

    @BeforeEach
    public void setUp() {
        status = new JLabel("setting up...");
        game = new Minesweeper(status);
    }

    @Test
    public void testInitGame() {
        assertNotNull(game);
        assertEquals(Minesweeper.MAX_MINES, game.getFlagsLeft());
        assertEquals(Minesweeper.MAX_MINES, game.getMinesLeft());
        assertFalse(game.isGameOver());
    }

    @Test
    public void testInitStateOfBoard() {

        setupTestBoard(createEmptyBoard());

        for (Cell[] row : game.getBoard()) {
            for (Cell cell : row) {
                assertTrue(cell.isCovered());
                assertFalse(cell.isFlagged());
            }
        }
    }

    @Test
    public void testFlagAndUnflagCell() {
        setupTestBoard(createEmptyBoard());

        game.flag(0, 0);
        assertTrue(game.getBoard()[0][0].isFlagged());
        assertEquals(Minesweeper.MAX_MINES - 1, game.getFlagsLeft());

        game.flag(0, 0);
        assertFalse(game.getBoard()[0][0].isFlagged());
        assertEquals(Minesweeper.MAX_MINES, game.getFlagsLeft());
    }

    @Test
    public void testPlaceMines() {

        int mineCount = 0;
        Cell[][] board = game.getBoard();
        for (int i = 0; i < Minesweeper.ROWS; i++) {
            for (int j = 0; j < Minesweeper.COLUMNS; j++) {
                if (board[i][j].hasMine()) {
                    mineCount++;
                }
            }
        }
        assertEquals(Minesweeper.MAX_MINES, mineCount);
    }

    @Test
    public void testExcavateNonMineCell() {
        Cell[][] board = game.getBoard();
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[0].length; c++) {
                if (!board[r][c].hasMine()) {
                    game.excavate(c, r);
                    assertFalse(board[r][c].isCovered());
                    break;
                }
            }
        }
    }

    @Test
    public void testExcavateCornerCell() {
        setupTestBoard(createEmptyBoard());
        game.excavate(0, 0); // Top left corner
        assertFalse(game.getBoard()[0][0].isCovered());
    }

    @Test
    public void testExcavateMineCell() {
        Cell[][] board = game.getBoard();
        board[0][0].placeMine();
        game.excavate(0, 0);
        assertTrue(game.isGameOver());
    }

    @Test
    public void testFloodFill() {
        game.initGame();
        Cell[][] board = game.getBoard();

        board[0][0].uncover();
        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                int ni = 0 + di;
                int nj = 0 + dj;
                if (ni >= 0 && ni < board.length && nj >= 0 && nj < board[0].length) {
                    board[ni][nj].uncover();
                }
            }
        }

        game.floodFillCells(0, 0);

        for (int di = -1; di <= 1; di++) {
            for (int dj = -1; dj <= 1; dj++) {
                if (di == 0 && dj == 0) {
                    continue;
                }
                int ni = 0 + di;
                int nj = 0 + dj;
                if (ni >= 0 && ni < board.length && nj >= 0 && nj < board[0].length) {
                    assertFalse(board[ni][nj].isCovered(),
                            "cell at (" + ni + "," + nj + ") should be uncovered");
                }
            }
        }
    }

    @Test
    public void testFloodFillOnCellWithMinesAround() {
        setupTestBoard(createEmptyBoard());
        Cell[][] board = game.getBoard();
        board[0][0].placeMine();
        board[0][1].placeMine();
        board[1][0].placeMine();
        game.floodFillCells(1, 1);
        assertFalse(board[1][1].isCovered());
        assertTrue(board[0][0].isCovered());
        assertTrue(board[0][1].isCovered());
        assertTrue(board[1][0].isCovered());
    }

    @Test
    public void testFloodFillEdge() {
        setupTestBoard(createEmptyBoard());
        game.floodFillCells(0, 0); // Top left corner
        assertTrue(!game.getBoard()[0][1].isCovered() && !game.getBoard()[1][0].isCovered(),
                "Adjacent cells should be uncovered after flood fill");
    }



    @Test
    public void testGameOverByFlaggingAllMines() {
        Cell[][] testBoard = createBoardWithAllMines();
        setupTestBoard(testBoard);

        for (int r = 0; r < Minesweeper.ROWS; r++) {
            for (int c = 0; c < Minesweeper.COLUMNS; c++) {
                game.flag(c, r);
            }
        }

        assertTrue(game.isGameOver(), "Game should be over after flagging all mines");
    }

}