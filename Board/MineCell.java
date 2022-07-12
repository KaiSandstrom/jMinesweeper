package Board;

public class MineCell extends Cell {

    //  A MineCell is a Minesweeper board cell that contains a mine.
    //      If a MineCell is clicked using clickCell, true is returned,
    //      signaling to the Game that the game has been lost.

    // A MineCell has one additional field not present in it superclass:
    //      boolean exploded. This field is used only to keep track of which
    //      mine was clicked on resulting in a game over, and the "exploded"
    //      mine is displayed using a different image when the game is over.

    private boolean exploded;

    public MineCell() {
        super();
        this.exploded = false;
    }

    public boolean clickCell() {
        if (flagged)
            return false;
        exploded = true;
        return true;
    }

    // See explanation in Cell.java
    public String getViewState() {
        if (flagged)
            return "flagged";
        if (exploded)
            return "revealedExploded";
        if(revealed)
            return "revealedMine";
        return "unrevealed";
    }

    // See explanation in Cell.java
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
