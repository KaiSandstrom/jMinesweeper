package gui;

import board.Cell;
import game.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class CellBoardPanel {

    //  A CellBoardPanel represents the area of the game window containing the
    //      grid of cell buttons. On each click, the proper call must be made
    //      to the Game object to process the operation, and the cells must
    //      display correctly.

    //  A CellBoardPanel has a reference to the Game object, in order to make
    //      calls invoking game logic, and a reference to the parent GamePanel.
    //      The reference to the parent GamePanel is used to update the smiley,
    //      mine counter, and timer in the InfoPanel, as well as to pass a call
    //      back to the OuterFrame to check for a high score when the game is
    //      won.

    //  JButtons associated with the individual Cells are stored in a 2D array.

    //  Three boolean values track which mouse buttons have been pressed, and
    //      whether a chord click has been activated.

    private final ImageIcon unrevealed = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/unrevealed.png")));
    private final ImageIcon revealedBlank = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/revealedBlank.png")));
    private final ImageIcon revealedNumber1 = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/revealedNumber1.png")));
    private final ImageIcon revealedNumber2 = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/revealedNumber2.png")));
    private final ImageIcon revealedNumber3 = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/revealedNumber3.png")));
    private final ImageIcon revealedNumber4 = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/revealedNumber4.png")));
    private final ImageIcon revealedNumber5 = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/revealedNumber5.png")));
    private final ImageIcon revealedNumber6 = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/revealedNumber6.png")));
    private final ImageIcon revealedNumber7 = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/revealedNumber7.png")));
    private final ImageIcon revealedNumber8 = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/revealedNumber8.png")));
    private final ImageIcon flagged = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/flagged.png")));
    private final ImageIcon falseFlagged = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/falseFlagged.png")));
    private final ImageIcon revealedMine = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/revealedMine.png")));
    private final ImageIcon revealedExploded = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/revealedExploded.png")));
    private final ImageIcon questionMarked = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/marked.png")));
    private final ImageIcon questionMarkedClicked = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/markedClicked.png")));

    private final JPanel board;
    private final int rows, cols;
    private final GamePanel parent;
    private final JButton[][] buttons;
    private Game game;

    private boolean leftClicked, rightClicked, chordActivated;

    public CellBoardPanel(int nRows, int nCols, Game g, GamePanel parentComponent) {
        rows = nRows;
        cols = nCols;
        buttons = new JButton[rows][cols];
        game = g;
        parent = parentComponent;
        board = new JPanel(new GridLayout(rows, cols, 0, 0));
        leftClicked = false;
        rightClicked = false;
        chordActivated = false;
        initialize();
    }

    //  Initializes the grid of JButtons and makes a call to
    //      addCellClickHandler, which adds the mouse listener to each one.
    private void initialize() {
        for (int i=0; i<rows; i++)
            for (int j=0; j<cols; j++) {
                JButton button = new JButton();
                button.setIcon(unrevealed);
                button.setMargin(new Insets(0, 0, 0, 0));
                button.setBorder(new EmptyBorder(0, 0, 0, 0));
                board.add(button);
                buttons[i][j] = button;
                addCellClickHandler(i, j);
            }
    }

    //  Used when resetting the game by clicking the smiley. A new Game object
    //      is provided, and the cells all get set to unrevealed.
    public void reset(Game g) {
        game = g;
        for (int i=0; i<rows; i++)
            for (int j=0; j<cols; j++)
                buttons[i][j].setIcon(unrevealed);
    }

    //  Used in the parent GamePanel to update the board's view state when
    //      an update is triggered from a menu instead of a board click
    public void forceRefresh() {
        updateCells(game.getUpdateTracker());
    }

    //  This method adds a MouseAdapter to a cell button. Due to the added
    //      complexity of the chord click operation, there is no ActionListener
    //      for handling left clicks -- the MouseAdapter's methods update the
    //      CellBoardPanel click state variables and take the appropriate
    //      action.
    private void addCellClickHandler(int row, int col) {
        buttons[row][col].addMouseListener(new MouseAdapter() {

            //  Special UpdateTracker used to keep track of which cells have
            //      been clicked on but not yet released on. If the player
            //      moves the mouse away from the clicked cell while the left
            //      button is pressed, no click occurs, and the cells that were
            //      previously displayed as "pressed" must be reset.
            private final UpdateTracker clicked = new UpdateTracker();

            //  When any of the three mouse buttons are pressed, one or more of
            //      the three boolean click state variables are manipulated.
            //      left and right clicks set leftClicked and rightClicked
            //      respectively, and a middle click sets chordActivated. Left,
            //      right, and chord are all mutually exclusive, as pressing
            //      left or right while the other is already activated will
            //      clear both and set chordActivated, and setting
            //      chordActivated with a middle click will also clear both
            //      others.
            //  When the player makes a valid left click or valid right click,
            //      one or more cells will be added to the clicked
            //      updateTracker and displayed as pressed. A chord click
            //      activated with both left and right or with the middle
            //      button will show all adjacent unrevealed cells as pressed,
            //      unless chord-flagging is enabled and the click is a valid
            //      chord-flag. When chord-clicking with the left mouse button,
            //      adjacent cells will only be shown as pressed if the
            //      click will generate a valid chord click.
            @Override
            public void mousePressed(MouseEvent e) {
                if (game.getGameState() > Game.IN_PROGRESS)
                    return;
                if (e.getButton() == MouseEvent.BUTTON1)
                    leftClicked = true;
                else if (e.getButton() == MouseEvent.BUTTON3)
                    rightClicked = true;
                if (e.getButton() == MouseEvent.BUTTON2 || (leftClicked && rightClicked)) {
                    chordActivated = true;
                    leftClicked = false;
                    rightClicked = false;
                }
                if (game.getViewState(row, col) == Cell.FLAGGED || rightClicked)
                    return;
                boolean alreadyUpdated = updateClicked(row, col);
                int flags = countFlaggedSurrounding();
                if (!alreadyUpdated &&
                        (!game.getFlagChordEnabled()||!(countClickableSurrounding()==game.getViewState(row,col)-flags))
                        && (chordActivated || (game.getLeftClickChord() && game.getViewState(row, col) == flags))) {
                    for (int i=row-1; i<=row+1; i++)
                        for (int j=col-1; j<=col+1; j++)
                            if (isValidCell(i, j))
                                updateClicked(i, j);
                }
                parent.getInfoPanel().setSmileyShocked();
            }

            //  Most times this method is called, nothing will be done, as the
            //      cursor is simply gliding across the window. When the mouse
            //      has been dragged out of a cell after pressing a button,
            //      however, no matter what combination of left, right, and
            //      middle, this indicates an aborted click and the previous
            //      state must be restored. In the case of a left click or
            //      chord click, this means updating the cells at the
            //      coordinates stored in clicked back to unrevealed (or
            //      question-marked). In all cases, the click variables are
            //      reset to false.
            @Override
            public void mouseExited(MouseEvent e) {
                leftClicked = false;
                rightClicked = false;
                chordActivated = false;
                updateCells(clicked);
                parent.getInfoPanel().updateSmiley();
            }

            //  If one of the click state variables is set, then an entire
            //      click operation has been performed with the cursor inside
            //      one cell, and a Game method must be invoked. If some click
            //      must be performed, updateCells first resets the icons of
            //      the cells in clicked to their pre-clicked state. This
            //      ensures that an invalid chord click does not leave cells
            //      pressed. Next, the appropriate Game click method is called
            //      and its click state variable is reset to false. Finally,
            //      the board and info panel are updated according to the
            //      results of the click.
            public void mouseReleased(MouseEvent e) {
                if (game.getGameState() == Game.OVER_WIN || game.getGameState() == Game.OVER_LOSS)
                    return;
                if (!(leftClicked || rightClicked || chordActivated))
                    return;
                updateCells(clicked);
                if (game.getGameState() == Game.NOT_STARTED)
                    if (!chordActivated)
                        parent.getInfoPanel().startTimer();
                    else
                        return;
                if (leftClicked) {
                    game.leftClickCell(row, col);
                    leftClicked = false;
                } else if (rightClicked) {
                    game.rightClickCell(row, col);
                    rightClicked = false;
                } else if (chordActivated) {
                    game.chordClickCell(row, col);
                    chordActivated = false;
                }
                parent.getInfoPanel().updateMineCount();
                updateCells(game.getUpdateTracker());
                parent.getInfoPanel().updateSmiley();
                if (game.getGameState() > Game.IN_PROGRESS)
                    parent.getInfoPanel().haltTimer();
                if (game.getGameState() == Game.OVER_WIN)
                    parent.processWin();
            }

            //  Sets a cell's icon to the pressed state if appropriate, and
            //      returns a boolean describing whether the operation was
            //      successful. Successfully-pressed cells are added to
            //      the clicked UpdateTracker.
            private boolean updateClicked(int r, int c) {
                if (game.getViewState(r, c) == Cell.UNREVEALED)
                    buttons[r][c].setIcon(revealedBlank);
                else if (game.getViewState(r, c) == Cell.QUESTION_MARKED)
                    buttons[r][c].setIcon(questionMarkedClicked);
                else
                    return false;
                clicked.addUpdate(new Posn(r, c));
                return true;
            }

            //  Private method to count the number of flagged cells surrounding
            //      the current cell. This is used to determine whether the
            //      cells surrounding a clicked revealed cell should be
            //      displayed as clicked, as in some cases they should only be
            //      shown as clicked if the click will result in cells being
            //      revealed.
            private int countFlaggedSurrounding() {
                int count = 0;
                for (int i=row-1; i<=row+1; i++)
                    for (int j=col-1; j<=col+1; j++)
                        if (isValidCell(i, j) && game.getViewState(i, j) == Cell.FLAGGED)
                            count++;
                return count;
            }

            //  Counts the number of "clickable" (unrevealed or question marked) cells
            //      surrounding the cell at a given position. Used to determine whether
            //      a flag-chord click is being performed, as a chord click does not
            //      show cells as pressed when a flag chord is being performed.
            private int countClickableSurrounding() {
                int count = 0;
                for (int i=row-1; i<=row+1; i++)
                    for (int j=col-1; j<=col+1; j++)
                        if (isValidCell(i, j) && (game.getViewState(i, j) == Cell.UNREVEALED ||
                                game.getViewState(i, j) == Cell.QUESTION_MARKED))
                            count++;
                return count;
            }

            //  Simple shorthand to make if statements cleaner
            private boolean isValidCell(int i, int j) {
                return (i>=0 && j>=0 && i<rows && j<cols);
            }
        });
    }

    //  Getter for the board JPanel, used by GamePanel to get its center
    //      element, which is the board panel.
    public JPanel getBoardJPanel() {
        return board;
    }

    //  Updates the cells at all Posns in the given UpdateTracker. This is used
    //      both to display updates from the Game logic, and to restore the
    //      view state of cells after aborted clicks.
    private void updateCells(UpdateTracker updateTracker) {
        for (Posn pos : updateTracker) {
            int r = pos.row();
            int c = pos.col();
            buttons[r][c].setIcon(getIconFromViewState(game.getViewState(r, c)));
        }
    }

    //  Simple switch statement to map viewState values to their associated
    //      ImageIcons.
    private ImageIcon getIconFromViewState(int viewState) {
        ImageIcon icon;
        switch (viewState) {
            case Cell.REVEALED_BLANK:
                icon = revealedBlank; break;
            case Cell.REVEALED_1:
                icon = revealedNumber1; break;
            case Cell.REVEALED_2:
                icon = revealedNumber2; break;
            case Cell.REVEALED_3:
                icon = revealedNumber3; break;
            case Cell.REVEALED_4:
                icon = revealedNumber4; break;
            case Cell.REVEALED_5:
                icon = revealedNumber5; break;
            case Cell.REVEALED_6:
                icon = revealedNumber6; break;
            case Cell.REVEALED_7:
                icon = revealedNumber7; break;
            case Cell.REVEALED_8:
                icon = revealedNumber8; break;
            case Cell.FLAGGED:
                icon = flagged; break;
            case Cell.FALSE_FLAGGED:
                icon = falseFlagged; break;
            case Cell.REVEALED_MINE:
                icon = revealedMine; break;
            case Cell.EXPLODED_MINE:
                icon = revealedExploded; break;
            case Cell.QUESTION_MARKED:
                icon = questionMarked; break;
            default:
                icon = unrevealed;
        }
        return icon;
    }

}
