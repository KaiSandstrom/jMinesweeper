package Board;

public abstract class Cell {

    //  This abstract class lays out the common elements that all cells share,
    //      whether mine or clear. All cells are either revealed or hidden, and
    //      are either flagged or unflagged. In a future update, a third state,
    //      "questionMarked," will be added, as this third state was present in
    //      the original Microsoft Minesweeper. It will be optional, and turned
    //      off by default.

    //  The non-abstract public methods are for the most part getters and
    //      setters. toggleFlagged is a bit more involved, as it returns a
    //      value; thus, it is explained further down below, as are the
    //      abstract methods.

    public static int FLAG_SET = 0;
    public static int FLAG_CLEARED = 1;
    public static int FLAG_UNCHANGED = 2;

    protected boolean revealed;
    protected boolean flagged;

    public Cell() {
        this.revealed = false;
        this.flagged = false;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setRevealed() {
        revealed = true;
    }

    //  Toggles the flagged state, and returns the appropriate result flag.
    //      These separate return values are used in Game's rightClickCell
    //      method to determine whether to increment or decrement the number
    //      of unflagged mines, or keep it the same if an already-revealed cell
    //      was right-clicked.
    public int toggleFlagged() {
        if (revealed)
            return FLAG_UNCHANGED;
        flagged = !flagged;
        if (flagged)
            return FLAG_SET;
        return FLAG_CLEARED;
    }

    //  Used when clearing larger areas of cells adjacent to 0 mines.
    //      When the cells surrounding a cell with 0 adjacent mines, this is
    //      the only time a flagged cell can be cleared.
    public void unflag() {
        flagged = false;
    }

    //  clickCell will return true if called by a MineCell, and false if called
    //      by an EmptyCell. This return value indicates whether the click
    //      resulted in a loss.
    public abstract boolean clickCell();

    //  This method is used by the GUI, which will be added in a later update,
    //      to determine which image to display for each cell.
    public abstract String getViewState();

    //  toString's implementations in Cell's subclasses differ only in their
    //      return values -- toString returns 1-character strings, for the
    //      purpose of printing the board in the text-based version of the
    //      game, whereas the longer state strings returned by getViewState
    //      are designed for readability in the relevant conditional statement
    //      in the GUI code.
    public abstract String toString();

}
