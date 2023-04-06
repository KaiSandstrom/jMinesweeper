package board;

public class EmptyCell extends Cell {

    //  An EmptyCell represents a Minesweeper board cell that does not contain
    //      a mine. clickCell returns false, indicating that the game has not
    //      been lost.

    //  An EmptyCell contains one additional field not present in its
    //      superclass: int minesAdjacent. This field stores the number of
    //      MineCell instances adjacent to this cell, including diagonally.
    //      The minesAdjacent field is incremented to the correct value through
    //      calls to incMinesAdjacent when the board is populated in Board.

    private int minesAdjacent;

    public EmptyCell() {
        super();
        minesAdjacent = 0;
    }

    @Override
    public boolean isMine() {
        return false;
    }

    public int getMinesAdjacent() {
        return minesAdjacent;
    }

    @Override
    public void incMinesAdjacent() {
        minesAdjacent++;
    }

    @Override
    public boolean clickCell() {
        if (!flagged)
            revealed = true;
        return false;
    }

    // See explanation in Cell.java
    @Override
    public int getViewState() {
        if (flagged && revealed)
            return FALSE_FLAGGED;
        if (flagged)
            return FLAGGED;
        if (!revealed)
            return UNREVEALED;
        if (minesAdjacent == 0)
            return REVEALED_BLANK;
        else
            return minesAdjacent;
    }

    // See explanation in Cell.java
    @Override
    public String toString() {
        if (flagged && revealed)
            return "N";
        if (flagged)
            return "F";
        if (!revealed)
            return "-";
        if (minesAdjacent == 0)
            return ".";
        else
            return Integer.toString(minesAdjacent);
    }

}
