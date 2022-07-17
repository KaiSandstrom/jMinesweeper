package board;

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

    public static final int FLAG_SET = 0;
    public static final int FLAG_CLEARED = 1;
    public static final int FLAG_UNCHANGED = 2;

    public static final int REVEALED_BLANK = 0;
    public static final int REVEALED_1 = 1;
    public static final int REVEALED_2 = 2;
    public static final int REVEALED_3 = 3;
    public static final int REVEALED_4 = 4;
    public static final int REVEALED_5 = 5;
    public static final int REVEALED_6 = 6;
    public static final int REVEALED_7 = 7;
    public static final int REVEALED_8 = 8;
    public static final int UNREVEALED = 9;
    public static final int FLAGGED = 10;
    public static final int FALSE_FLAGGED = 11;
    public static final int REVEALED_MINE = 12;
    public static final int EXPLODED_MINE = 13;

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
    //      When the cells surrounding a cell with 0 adjacent mines are being
    //      cleared, this is the only time a flagged cell can be cleared.
    public void unflag() {
        flagged = false;
    }

    //  clickCell will return true if called by a MineCell, and false if called
    //      by an EmptyCell. This return value indicates whether the click
    //      resulted in a loss.
    public abstract boolean clickCell();

    //  This method is used by the GUI to determine which image to display for
    //      each cell.
    public abstract int getViewState();

    //  toString's implementations in Cell's subclasses differ from
    //      getViewState only in their return values -- toString returns
    //      1-character strings, for the purpose of printing the board in the
    //      text-based version of the game, whereas the state constants
    //      returned by getViewState are designed for readability in the
    //      relevant conditional statement in the GUI code.
    public abstract String toString();

}
