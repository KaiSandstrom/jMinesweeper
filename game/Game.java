package game;

import board.Board;
import board.Cell;

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

    public static final int AVOID_FIRST_CLICK = 1;
    public static final int CLICK_SURROUNDING_REVEALED = 2;
    public static final int QUESTION_MARKS_ENABLED = 4;

    private final Board board;
    private final UpdateTracker updateTracker;
    private final Difficulty difficulty;
    private int gameState;
    private int minesMinusFlags;
    private boolean marksEnabled;
    private boolean avoidAroundFirstClick;
    private boolean clickRevealedEnabled;

    public Game(Difficulty diff, byte optionFlags) {
        difficulty = diff;
        updateTracker = new UpdateTracker();
        board = new Board(diff.getRows(), diff.getColumns(), updateTracker);
        minesMinusFlags = diff.getMines();
        avoidAroundFirstClick = ((optionFlags & AVOID_FIRST_CLICK) != 0);
        clickRevealedEnabled = ((optionFlags & CLICK_SURROUNDING_REVEALED) != 0);
        marksEnabled = ((optionFlags & QUESTION_MARKS_ENABLED) != 0);
        gameState = NOT_STARTED;
    }

    public Game(Game g) {
        difficulty = g.difficulty;
        updateTracker = new UpdateTracker();
        board = new Board(difficulty.getRows(), difficulty.getColumns(), updateTracker);
        minesMinusFlags = difficulty.getMines();
        avoidAroundFirstClick = g.avoidAroundFirstClick;
        clickRevealedEnabled = g.clickRevealedEnabled;
        marksEnabled = g.marksEnabled;
        gameState = NOT_STARTED;
    }

    public int getGameState() {
        return gameState;
    }

    public int getMinesRemaining() {
        return minesMinusFlags;
    }

    public UpdateTracker getUpdateTracker() {
        return updateTracker;
    }

    public void toggleMarksEnabled() {
        marksEnabled = !marksEnabled;
        if (!marksEnabled)
            board.clearQuestionMarks();
    }

    public void toggleAvoidAroundFirstClick() {
        avoidAroundFirstClick = !avoidAroundFirstClick;
    }

    public void toggleClickRevealedEnabled() {
        clickRevealedEnabled = !clickRevealedEnabled;
    }

    public boolean getClickRevealedEnabled() {
        return clickRevealedEnabled;
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
            board.populateBoard(row, col, minesMinusFlags, avoidAroundFirstClick);
            gameState = IN_PROGRESS;
        }
        if (board.leftClickCell(row, col, clickRevealedEnabled)) {
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
            board.populateBoard(-2, -2, minesMinusFlags, avoidAroundFirstClick);
            gameState = IN_PROGRESS;
        }
        int rClickResult = board.rightClickCell(row, col, marksEnabled);
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
