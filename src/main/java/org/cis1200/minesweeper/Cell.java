package org.cis1200.minesweeper;

public class Cell {
    private int surrounding;
    private boolean flagged;
    private boolean covered;
    private boolean mined;

    public Cell() {
        surrounding = 0;
        flagged = false;
        covered = true;
        mined = false;
    }

    public Cell(int s, boolean f, boolean c, boolean m) {
        surrounding = s;
        flagged = f;
        covered = c;
        mined = m;
    }

    public void addSurrounding() {
        surrounding++;
    }

    public boolean hasMine() {
        return mined;
    }

    public void placeMine() {
        mined = true;
    }

    public boolean isCovered() {
        return covered;
    }

    public void uncover() {
        covered = false;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void flag() {
        flagged = !flagged;
    }

    public int getSurroundingMines() {
        return surrounding;
    }

    public String toString() {
        return Integer.toString(surrounding) + " " + Boolean.toString(flagged)
                + " " + Boolean.toString(covered) + " " + Boolean.toString(mined);
    }

}
