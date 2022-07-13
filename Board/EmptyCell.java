package Board;

public class EmptyCell extends Cell {

    //  An EmptyCell represents a Minesweeper board cell that does not contain
    //      a mine. clickCell returns false, indicating that the game has not
    //      been lost.

    //  An EmptyCell contains one additional field not present in its
    //      superclass: int minesAdjacent. This field stores the number of
    //      MineCell instances adjacent to this cell, including diagonally.
    //      The minesAdjacent field is incremented to the correct value through
    //      calls to incMinesAdjacent when the board is populated in Board,

    private int minesAdjacent;

    public EmptyCell() {
        super();
        minesAdjacent = 0;
    }

    public int getMinesAdjacent() {
        return minesAdjacent;
    }

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
    public String getViewState() {
        if (flagged && revealed)
            return "falseFlagged";
        if (flagged)
            return "flagged";
        if (!revealed)
            return "unrevealed";
        if (minesAdjacent == 0)
            return "revealedBlank";
        else
            return "revealedNumber" + minesAdjacent;
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
            return "" + minesAdjacent;
    }

}
