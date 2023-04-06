package board;

public class MineCell extends Cell {

    //  A MineCell is a Minesweeper board cell that contains a mine.
    //      If a MineCell is clicked using clickCell, clickCell returns true,
    //      signaling to the Game that the game has been lost.

    //  A MineCell has one additional field not present in its superclass:
    //      boolean exploded. This field is used only to keep track of which
    //      specific mine was clicked to lose the game, and the "exploded"
    //      mine is displayed using a different image when the game is over.

    private boolean exploded;

    public MineCell() {
        super();
        this.exploded = false;
    }

    @Override
    public boolean isMine() {
        return true;
    }

    // No-op: Only meaningful when called by an EmptyCell. Included in the
    //      Cell abstract class and in MineCell purely to avoid the use of
    //      instanceof, or less readable means of identifying MineCells, such
    //      as implementing getMinesAdjacent() to return -1.
    @Override
    public void incMinesAdjacent() {}

    @Override
    public boolean clickCell() {
        if (flagged)
            return false;
        exploded = true;
        return true;
    }

    // See explanation in Cell.java
    @Override
    public int getViewState() {
        if (flagged)
            return FLAGGED;
        if (exploded)
            return EXPLODED_MINE;
        if(revealed)
            return REVEALED_MINE;
        return UNREVEALED;
    }

    // See explanation in Cell.java
    @Override
    public String toString() {
        if (flagged)
            return "F";
        if (exploded)
            return "X";
        if (revealed)
            return "*";
        return "-";
    }
}
