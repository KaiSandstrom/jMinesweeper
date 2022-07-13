package Game;

import Board.*;

import java.util.Scanner;

public class Game {

    //  A Game object contains a board, a running count of mines minus flags,
    //      a gameState that is either in progress, over (win), or over (loss),
    //      and a flag indicating that the board has not yet been initialized.

    //  The Game's public methods provide an interface for all operations that
    //      a user interface needs to access -- starting a new game,
    //      left-clicking a cell, and right-clicking a cell.

    public static final int IN_PROGRESS = 0;
    public static final int OVER_WIN = 1;
    public static final int OVER_LOSS = 2;

    public static final int BEGINNER = 0;
    public static final int INTERMEDIATE = 1;
    public static final int EXPERT = 2;

    private final Board board;
    private int gameState;
    private boolean emptyBoard;
    private int minesMinusFlags;

    public Game(int difficulty, UpdateTracker tracker) {
        emptyBoard = true;
        gameState = IN_PROGRESS;
        if (difficulty == BEGINNER) {
            board = new Board(9, 9, tracker);
            minesMinusFlags = 10;
        } else if (difficulty == INTERMEDIATE) {
            board = new Board(16, 16, tracker);
            minesMinusFlags = 40;
        } else { // difficulty == EXPERT
            board = new Board(16, 30, tracker);
            minesMinusFlags = 99;
        }
    }

    public int getGameState() {
        return gameState;
    }

    public int getMinesRemaining() {
        return minesMinusFlags;
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
    //      game state flags accordingly.
    public void leftClickCell(int row, int col) {
        if (gameState != IN_PROGRESS)
            return;
        if (emptyBoard) {
            board.populateBoard(row, col, minesMinusFlags);
            emptyBoard = false;
        }
        if (board.leftClickCell(row, col)) {
            board.setRevealed();
            gameState = OVER_LOSS;
        }
        updateWinCondition();
    }

    // Called whenever a cell is right-clicked. If the board is empty, the
    //      board is populated. Invalid row and column indexes are given, so
    //      no cell is avoided when the game starts with a right click. The
    //      return value of the Board's rightClickCell method is used to
    //      determine how to modify the numRemaining variable. If all mines are
    //      flagged and all non-flagged cells are revealed, the game state
    //      flags are set accordingly.
    public void rightClickCell(int row, int col) {
        if (gameState != IN_PROGRESS)
            return;
        if (emptyBoard) {
            board.populateBoard(-2, -2, minesMinusFlags);
            emptyBoard = false;
        }
        int rClickResult = board.rightClickCell(row, col);
        if (rClickResult == Cell.FLAG_SET)
            minesMinusFlags--;
        else if (rClickResult == Cell.FLAG_CLEARED)
            minesMinusFlags++;
        updateWinCondition();
    }

    //  Simply calls the board's getViewState method. Used by the GUI, which
    //      will be implemented in a later update, to determine which image to
    //      draw for each cell.
    public int getViewState(int row, int col) {
        return board.getViewState(row, col);
    }

    //  Used to print the board, for debugging and functionality testing before
    //      the implementation of the GUI.
    public void printBoard() {
        System.out.print(board);
    }

    //  This main method is temporary, as in this early stage of development,
    //      the game is purely text-based. Here, a simple command line
    //      interpreter is used to get commands from the terminal, and these
    //      commands translate into operations on the game board. Eventually,
    //      this command line interpreter will be replaced with a GUI
    //      application.
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        String input;
        while (true) {
            System.out.print("Enter difficulty: beginner, intermediate, expert: ");
            input = kb.nextLine().toLowerCase();
            if ((input.equals("beginner") || input.equals("intermediate") || input.equals("expert")))
                break;
            System.out.println("Invalid difficulty!");
        }
        int diff;
        if (input.equals("beginner"))
            diff = BEGINNER;
        else if (input.equals("intermediate"))
            diff = INTERMEDIATE;
        else // input.equals("expert")
            diff = EXPERT;
        Game g = new Game(diff, new UpdateTracker());
        System.out.println( "Command format: \n" +
                            "   lclick row col       -- left click cell at given pos \n" +
                            "   rclick row col       -- right click cell at given pos \n");
        g.printBoard();
        while (g.getGameState() == IN_PROGRESS) {
            input = kb.nextLine();
            String[] inputSplit = input.split(" ");
            if (inputSplit.length != 3) {
                System.out.println("Invalid command!");
                continue;
            }
            if (inputSplit[0].equals("lclick") || inputSplit[0].equals("rclick")) {
                int row, col;
                try {
                    row = Integer.parseInt(inputSplit[1]);
                    col = Integer.parseInt(inputSplit[2]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid coordinates!");
                    continue;
                }
                if (row<0 || col<0) {
                    System.out.println("Invalid coordinates!");
                    continue;
                }
                if ((diff == BEGINNER && (row>=9 || col>=9)) ||
                        ((diff == INTERMEDIATE && (row>=16 || col>=16))) ||
                        ((diff == EXPERT && (row>=16 || col>=30)))) {
                    System.out.println("Invalid coordinates!");
                    continue;
                }
                if (inputSplit[0].equals("lclick"))
                    g.leftClickCell(row, col);
                else if (inputSplit[0].equals("rclick"))
                    g.rightClickCell(row, col);
            } else {
                System.out.println("Invalid command!");
            }
            if (g.getGameState() == OVER_LOSS) {
                g.board.setRevealed();
            }
            System.out.println("Mines remaining: " + g.getMinesRemaining());
            g.printBoard();
        }
        String winLose;
        if (g.getGameState() == Game.OVER_WIN)
            winLose = "win";
        else
            winLose = "lose";
        System.out.println("Game over! You " + winLose + "!");
    }

}
