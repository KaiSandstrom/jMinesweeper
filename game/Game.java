package game;

import board.Board;
import board.Cell;

public class Game {

    //  A Game object contains a Board, a running count of mines minus flags,
    //      a gameState that is either in progress, over (win), or over (loss),
    //      a flag indicating that the board has not yet been initialized, and
    //      several option flags that determine game behavior. It also has an
    //      UpdateTracker, which keeps track of which cells have been changed
    //      since the board was last drawn in the GUI.

    //  The Game's public methods provide an interface for all operations that
    //      a UI needs to access -- starting a new game, left-clicking a cell,
    //      and right-clicking a cell.

    public static final int NOT_STARTED = 0;
    public static final int IN_PROGRESS = 1;
    public static final int OVER_WIN = 2;
    public static final int OVER_LOSS = 3;

    public static final int FIRST_ALWAYS_BLANK = 1;
    public static final int LEFT_CLICK_CHORD = 2;
    public static final int QUESTION_MARKS_ENABLED = 4;
    public static final int AUTO_FLAG_LAST = 8;
    public static final int FLAG_CHORD_ENABLED = 16;

    private final Board board;
    private final UpdateTracker updateTracker;
    private final Difficulty difficulty;
    private int gameState;
    private int minesMinusFlags;
    private boolean marksEnabled;
    private boolean firstAlwaysBlank;
    private boolean leftClickChord;
    private boolean autoFlagLastCells;
    private boolean flagChordEnabled;

    // Used when first starting the game and after changing difficulty.
    public Game(Difficulty diff, byte optionFlags) {
        difficulty = diff;
        updateTracker = new UpdateTracker();
        board = new Board(diff.getRows(), diff.getColumns(), updateTracker);
        minesMinusFlags = diff.getMines();
        firstAlwaysBlank = ((optionFlags & FIRST_ALWAYS_BLANK) != 0);
        leftClickChord = ((optionFlags & LEFT_CLICK_CHORD) != 0);
        marksEnabled = ((optionFlags & QUESTION_MARKS_ENABLED) != 0);
        autoFlagLastCells = ((optionFlags & AUTO_FLAG_LAST) != 0);
        flagChordEnabled = ((optionFlags & FLAG_CHORD_ENABLED) != 0);
        gameState = NOT_STARTED;
    }

    // Used when starting a new game with the same difficulty as the previous
    //      game.
    public Game(Game g) {
        difficulty = g.difficulty;
        updateTracker = new UpdateTracker();
        board = new Board(difficulty.getRows(), difficulty.getColumns(), updateTracker);
        minesMinusFlags = difficulty.getMines();
        firstAlwaysBlank = g.firstAlwaysBlank;
        leftClickChord = g.leftClickChord;
        marksEnabled = g.marksEnabled;
        autoFlagLastCells = g.autoFlagLastCells;
        flagChordEnabled = g.flagChordEnabled;
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

    // Extra code in this option toggle clears all existing question marks when
    //      the option is disabled. The check for a not-yet-started game
    //      prevents a NullPointerException.
    public void toggleMarksEnabled() {
        marksEnabled = !marksEnabled;
        if (!marksEnabled && gameState != NOT_STARTED)
            board.clearQuestionMarks();
    }

    public void toggleFirstAlwaysBlank() {
        firstAlwaysBlank = !firstAlwaysBlank;
    }

    public void toggleLeftClickChord() {
        leftClickChord = !leftClickChord;
    }

    public void toggleFlagChordEnabled() {
        flagChordEnabled = !flagChordEnabled;
    }

    //  When auto-flagging is enabled, in addition to the simple toggle, the
    //      game must check if the board is ready for auto-flagging without
    //      any new clicks. More info in the updateWinCondition comments.
    public void toggleAutoFlagLastCells() {
        autoFlagLastCells = !autoFlagLastCells;
        if (autoFlagLastCells)
            updateWinCondition();
    }

    public boolean getLeftClickChord() {
        return leftClickChord;
    }

    public boolean getFlagChordEnabled() {
        return flagChordEnabled;
    }

    // Checks if the game is won and sets the game state accordingly. If
    //      the auto-flagging option is enabled, the call to board.checkWin()
    //      will flag the remaining unflagged cells.
    private void updateWinCondition() {
        if (gameState == NOT_STARTED || gameState == OVER_LOSS || (!autoFlagLastCells && minesMinusFlags != 0))
            return;
        if (board.checkWin(minesMinusFlags)) {
            gameState = OVER_WIN;
        } else
            return;
        minesMinusFlags = 0;
        board.flagAllUnrevealed();
    }

    //  Called whenever a cell is left-clicked. If the board is empty, the
    //      board is populated, passing the row and column coordinates of
    //      this first click along to the populateBoard method in order to
    //      avoid placing mines in and/or adjacent to the first cell clicked.
    //      After checking for an empty board, the cell is clicked, and this
    //      method sets the game state flag accordingly.
    public void leftClickCell(int row, int col) {
        if (gameState > IN_PROGRESS) // Game over
            return;
        if (gameState == NOT_STARTED) {
            board.populateBoard(row, col, minesMinusFlags, firstAlwaysBlank);
            gameState = IN_PROGRESS;
        }
        if (leftClickChord && board.checkChord(row, col) != Board.NO_CHORD) {
            chordClickCell(row, col);
            return;
        } else if (board.leftClickCell(row, col)) {
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
            board.populateBoard(-2, -2, minesMinusFlags, firstAlwaysBlank);
            gameState = IN_PROGRESS;
        }
        int rClickResult = board.rightClickCell(row, col, marksEnabled);
        if (rClickResult == Cell.FLAG_SET)
            minesMinusFlags--;
        else if (rClickResult == Cell.FLAG_CLEARED)
            minesMinusFlags++;
        updateWinCondition();
    }

    // Called from the GUI whenever a chord click is performed, or from
    //      leftClickCell if leftClickChord is set. Checks the state of the
    //      cell at the given position, the state of its neighbors, and the
    //      game option flags its surrounding cells, and depending on these,
    //      either does nothing or invokes the board's left or right chord
    //      click methods. Sets game state according to the result.
    public void chordClickCell(int row, int col) {
        if (gameState > IN_PROGRESS)
            return;
        int chordType = board.checkChord(row, col);
        if (chordType == Board.NO_CHORD)
            return;
        if (flagChordEnabled && chordType == Board.FLAG_CHORD)
            minesMinusFlags -= board.chordClickRight(row, col);
        else if (chordType == Board.REVEAL_CHORD && board.chordClickLeft(row, col)) {
            board.setRevealed();
            gameState = OVER_LOSS;
        }
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
