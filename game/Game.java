package game;

import board.*;

public class Game {

    //  A Game object contains a Board, a running count of mines minus flags,
    //      a gameState that is either in progress, over (win), or over (loss),
    //      and a flag indicating that the board has not yet been initialized.
    //      It also has an UpdateTracker, which keeps track of which cells have
    //      been changed since the board was last drawn in the GUI.

    //  The Game's public methods provide an interface for all operations that
    //      a UI needs to access -- starting a new game, left-clicking a cell,
    //      and right-clicking a cell.

    public static final int NOT_STARTED = 0;
    public static final int IN_PROGRESS = 1;
    public static final int OVER_WIN = 2;
    public static final int OVER_LOSS = 3;

    private final Board board;
    private final UpdateTracker updateTracker;
    private final Difficulty difficulty;
    private int gameState;
    private int minesMinusFlags;

    public Game(Difficulty diff, UpdateTracker tracker) {
        difficulty = diff;
        board = new Board(diff.getRows(), diff.getColumns(), tracker);
        minesMinusFlags = diff.getMines();
        updateTracker = tracker;
    }

    public int getGameState() {
        return gameState;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public int getMinesRemaining() {
        return minesMinusFlags;
    }

    public UpdateTracker getUpdateTracker() {
        return updateTracker;
    }

    private void updateWinCondition() {
        if (board.allUnflaggedRevealed() && minesMinusFlags == 0) {
            gameState = OVER_WIN;
        }
    }

    //  Called whenever a cell is left-clicked. If the board is empty, the
    //      board is populated, passing the row and column coordinates of this
    //      first click along to the populateBoard method in order to avoid
    //      placing mines adjacent to the first cell clicked. After checking
    //      for an empty board, the cell is clicked, and this method sets the
    //      game state flag accordingly.
    public void leftClickCell(int row, int col) {
        if (gameState > IN_PROGRESS) // Game over
            return;
        if (gameState == NOT_STARTED) {
            board.populateBoard(row, col, minesMinusFlags);
            gameState = IN_PROGRESS;
        }
        if (board.leftClickCell(row, col)) {
            board.setRevealed();
            gameState = OVER_LOSS;
        }
        updateWinCondition();
    }

    // Called whenever a cell is right-clicked. If the board is empty, the
    //      board is populated. Invalid row and column indexes are given, so
    //      board-populating algorithm does not avoid any cells when the game
    //      starts with a right-click. The return value of the Board's
    //      rightClickCell method determines how to modify the numRemaining
    //      variable. If the player has flagged all mines and revealed all
    //      non-flagged cells, the game state flag is set accordingly.
    public void rightClickCell(int row, int col) {
        if (gameState > IN_PROGRESS) // Game over
            return;
        if (gameState == NOT_STARTED) {
            board.populateBoard(-2, -2, minesMinusFlags);
            gameState = IN_PROGRESS;
        }
        int rClickResult = board.rightClickCell(row, col);
        if (rClickResult == Cell.FLAG_SET)
            minesMinusFlags--;
        else if (rClickResult == Cell.FLAG_CLEARED)
            minesMinusFlags++;
        updateWinCondition();
    }

    //  Simply calls the board's getViewState method. Used by the GUI to
    //      determine which image to draw for each cell.
    public int getViewState(int row, int col) {
        return board.getViewState(row, col);
    }

    //  Used to print the board, for debugging and functionality testing before
    //      the implementation of the GUI. Left in for documentation purposes.
    public void printBoard() {
        System.out.print(board);
    }

}
