package board;

import game.Posn;
import game.UpdateTracker;

public class Board {

    //  A Board has an array of Cells, and also stores the numbers of rows and
    //      columns as fields, for use in methods. The methods perform all
    //      necessary operations on the cells in the board, and are described
    //      in detail before their declarations.

    private final Cell[][] board;
    private final int nRows, nCols;
    private final UpdateTracker updateTracker;

    public Board(int rows, int cols, UpdateTracker tracker) {
        board = new Cell[rows][cols];
        nRows = rows;
        nCols = cols;
        updateTracker = tracker;
    }

    //  Takes a number of mines, and the location of the first click.
    //      After this method completes execution, every spot on the board will
    //      contain a Cell object, either a MineCell or an EmptyCell. The cells
    //      will be fully initialized and ready for play. It is called upon the
    //      player's first click -- this is done in order ensure that the first
    //      click is not on a mine or adjacent to a mine. The rowPos and colPos
    //      arguments are used to avoid placing mines adjacent to the first
    //      click.
    public void populateBoard(int rowPos, int colPos, int totalMines) {
        int currentMines = 0;
        while (currentMines < totalMines) {
            int randomRow = (int)(Math.random()*nRows);
            int randomCol = (int)(Math.random()*nCols);
            if (board[randomRow][randomCol] == null &&
                    (!(randomRow>=rowPos-1 && randomRow<=rowPos+1 && randomCol>=colPos-1 && randomCol<=colPos+1))) {
                board[randomRow][randomCol] = new MineCell();
                currentMines++;
            }
        }
        for (int row=0; row<nRows; row++)
            for (int col=0; col<nCols; col++)
                if (board[row][col] == null)
                    board[row][col] = new EmptyCell();
        addAdjacent();
    }

    //  This method is called immediately after initially populating the board
    //      with Cell objects. It iterates through the board, and upon finding
    //      a MineCell object, iterates through the 3x3 area surrounding the
    //      mine, incrementing any adjacent EmptyCells' minesAdjacent field.
    private void addAdjacent() {
        for (int row=0; row<nRows; row++)
            for (int col=0; col<nCols; col++)
                if (board[row][col] instanceof MineCell)
                    for (int i=row-1; i<=row+1; i++)
                        for (int j=col-1; j<=col+1; j++)
                            if (i>=0 && j>=0 && i<nRows && j<nCols && (board[i][j] instanceof EmptyCell))
                                ((EmptyCell)board[i][j]).incMinesAdjacent();
    }

    //  Takes row and column indexes and returns a boolean describing whether
    //      the indexes describe a valid location on the board. It returns
    //      false if the indexes are out of bounds, and true otherwise. This
    //      method was written to simplify if statements that include an index
    //      bounds check.
    private boolean isValidCell(int row, int col) {
        return (row>=0 && col>=0 && row<nRows && col<nCols);
    }

    // Used to check a win condition in Game. If a call to this method returns
    //      true, and the correct number of flags have been placed, the game
    //      is won.
    public boolean allUnflaggedRevealed() {
        for (int i=0; i<nRows; i++)
            for(int j=0; j<nCols; j++)
                if (!board[i][j].isFlagged() && !board[i][j].isRevealed())
                    return false;
        return true;
    }

    //  Determines the number of flags adjacent to a given cell and returns
    //      the count as an integer. This method is used to determine when
    //      a revealed cell with the appropriate number of flagged cells has
    //      been clicked.
    private int getFlagsAdjacent(int row, int col) {
        int totalFlagged = 0;
        for (int i=row-1; i<=row+1; i++)
            for (int j=col-1; j<=col+1; j++) {
                if (isValidCell(i, j) && board[i][j].isFlagged())
                    totalFlagged++;}
        return totalFlagged;
    }

    //  Toggles the isFlagged field of the given cell, and returns a value
    //      describing whether the value was toggled off, toggled on, or
    //      not changed.
    public int rightClickCell(int row, int col) {
        int status = board[row][col].toggleFlagged();
        if (status != Cell.FLAG_UNCHANGED)
            updateTracker.addUpdate(new Posn(row, col));
        return status;
    }

    //  Clicks the given cell, and returns a boolean describing whether a mine
    //      was clicked. If a non-revealed cell with no adjacent mines is
    //      clicked, all adjacent cells with the same property are recursively
    //      clicked until the revealed area is bordered entirely by cells with
    //      mines adjacent, or the edge of the board.
    //      If a revealed cell with adjacent mines is clicked, and the number
    //      flagged cells surrounding it is equal to the number of mines
    //      adjacent, all non-flagged non-revealed cells adjacent to that cell
    //      are revealed. If the flags were placed incorrectly and a mine is
    //      clicked, leftClickCell returns true, indicating that a mine was
    //      clicked and the game is over.
    public boolean leftClickCell(int row, int col) {
        boolean wasRevealed = board[row][col].isRevealed();
        if (board[row][col].clickCell()) {
            updateTracker.addUpdate(new Posn(row, col));
            return true;
        }
        if (board[row][col].isFlagged())
            return false;
        int minesAdjacent = ((EmptyCell)board[row][col]).getMinesAdjacent();
        if (minesAdjacent == 0 && !wasRevealed) {
            chainClickCells(row, col);
            return false;
        } else if (wasRevealed && minesAdjacent != 0 && minesAdjacent == getFlagsAdjacent(row, col))
            return clickSurrounding(row, col);
        updateTracker.addUpdate(new Posn(row, col));
        return false;
    }

    // This method reveals the cell specified by the given row and col indexes,
    //      and if the given cell is not adjacent to any mines, this method is
    //      recursively called for all cells adjacent to it.
    private void chainClickCells(int row, int col) {
        if (board[row][col].isFlagged())
            return;
        board[row][col].clickCell();
        updateTracker.addUpdate(new Posn(row, col));
        int minesAdjacent = ((EmptyCell)board[row][col]).getMinesAdjacent();
        if (minesAdjacent == 0)
            for (int i=row-1; i<=row+1; i++)
                for (int j=col-1; j<=col+1; j++)
                    if (isValidCell(i, j) && !board[i][j].isRevealed())
                        chainClickCells(i, j);
    }

    //  This method is used to reveal all non-revealed non-flagged cells
    //      adjacent to a cell with a number of adjacent flags equal to its
    //      number of adjacent mines. If a mine is clicked in this process, the
    //      method returns true, indicating that the game is over. If a mine is
    //      not clicked, the method returns false.
    private boolean clickSurrounding(int row, int col) {
        boolean clickedMine = false;
        for (int i=row-1; i<=row+1; i++)
            for (int j=col-1; j<=col+1; j++)
                if(isValidCell(i, j) && !board[i][j].isFlagged() &&
                        !board[i][j].isRevealed()) {
                    if (clickedMine && board[i][j] instanceof MineCell)
                        continue;
                    clickedMine = (board[i][j].clickCell() || clickedMine);
                    if (board[i][j] instanceof EmptyCell && ((EmptyCell)board[i][j]).getMinesAdjacent() == 0)
                        chainClickCells(i,j);
                    else
                        updateTracker.addUpdate(new Posn(i, j));
                }
        return clickedMine;
    }

    //  Used by the GUI to determine which image to display for the cell at the
    //      given coordinates.
    public int getViewState(int row, int col) {
        if (board[row][col] == null)
            return Cell.UNREVEALED;
        return board[row][col].getViewState();
    }

    //  Used for debugging and model/controller testing before development
    //      of the GUI. Will be left in for documentation purposes.
    public String toString() {
        String output = "     ";
        for (int i=0; i<nCols; i++)
            output += i/10 + " ";
        output += "\n     ";
        for (int i=0; i<nCols; i++)
            output += i%10 + " ";
        output += "\n\n";
        for (int i=0; i<nRows; i++) {
            String rowNumString;
            if (i >= 10)
                rowNumString = "" + i + "   ";
            else
                rowNumString = " " + i + "   ";
            output += rowNumString;
            for (int j = 0; j < nCols; j++)
                if (board[i][j] == null)
                    output += "- ";
                else
                    output += board[i][j].toString() + " ";
            output += "\n";
        }
        return output;
    }

    //  Sets all mines and falsely-flagged cells to be revealed. This is used
    //      when a mine is clicked on, and the mines are is shown to the player
    //      upon game over.
    public void setRevealed() {
        for (int i=0; i<nRows; i++)
            for (int j=0; j<nCols; j++) {
                if (board[i][j] instanceof MineCell || board[i][j].isFlagged()) {
                    board[i][j].setRevealed();
                    updateTracker.addUpdate(new Posn(i, j));
                }
            }
    }

}
