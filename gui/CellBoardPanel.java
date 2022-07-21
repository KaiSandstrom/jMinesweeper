package gui;

import board.Cell;
import game.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    //      mine counter, and timer in the InfoPanel. Originally, the plan was
    //      to add another mouseListener to the entire GamePanel, but that
    //      mouseListener wasn't registering any clicks. This reference to the
    //      parent GamePanel is also used to pass a call back to the OuterFrame
    //      to check for a high score when the game is won.

    //  JButtons associated with the individual Cells are stored in a 2D array.

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

    private final JPanel board;
    private final int rows, cols;
    private final GamePanel parent;
    private final JButton[][] buttons;
    private Game game;

    public CellBoardPanel(int nRows, int nCols, Game g, GamePanel parentComponent) {
        rows = nRows;
        cols = nCols;
        buttons = new JButton[rows][cols];
        game = g;
        parent = parentComponent;
        board = new JPanel(new GridLayout(rows, cols, 0, 0));
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

    //  This method adds both an ActionListener and a MouseAdapter to a cell
    //      button. The ActionListener processes successful left clicks,
    //      whereas the MouseAdapter processes right clicks and adds visual
    //      feedback for right clicks.
    private void addCellClickHandler(int row, int col) {
        buttons[row][col].addActionListener(new ActionListener() {
            //  This new ActionListener fires when a cell has been successfully
            //      left-clicked. It calls the Game's left click method on the
            //      cell in question, and makes a call to updateCells to redraw
            //      the icons for the affected cells. Depending on the game
            //      state before and after the click, the timer may be started
            //      or halted, and the OuterFrame may be notified to check for
            //      a win.
            @Override
            public void actionPerformed(ActionEvent e) {
                int prevState = game.getGameState();
                game.leftClickCell(row, col);
                updateCells(game.getUpdateTracker());
                parent.getInfoPanel().updateSmiley();
                if (prevState == Game.NOT_STARTED && game.getGameState() == Game.IN_PROGRESS)
                    parent.getInfoPanel().startTimer();
                else if (prevState == Game.IN_PROGRESS && game.getGameState() > Game.IN_PROGRESS)
                    parent.getInfoPanel().haltTimer();
                if (game.getGameState() == Game.OVER_WIN)
                    parent.processWin();
            }
        });

        //  This MouseAdapter applies visual feedback for left clicks, and
        //      is responsible for handling right clicks. Since the
        //      MouseAdapter's mouseClicked method is only called when the
        //      mouse button is pressed and released without moving at all,
        //      it's not a viable way of reliably detecting clicks.
        //      ActionListener can handle left clicks, but for right clicks,
        //      it's necessary to use mousePressed and mouseReleased, and
        //      ensure tha these methods were called with the cursor over
        //      the same cell.
        buttons[row][col].addMouseListener(new MouseAdapter() {

            //  Special UpdateTracker used to keep track of which cells have
            //      been clicked on but not yet released on. If the player
            //      moves the mouse away from the clicked cell while the left
            //      button is pressed, no click occurs, and the cells that were
            //      previously displayed as "pressed" must be reset.
            private final UpdateTracker clicked = new UpdateTracker();

            //  When this boolean is set to true, the right mouse button has
            //      been pressed and the cursor is still on the cell. If this
            //      variable is set to true when the button is released, a
            //      valid right click has occurred.
            private boolean rightClicked = false;

            //  If left click: Changes clicked cells' display state to
            //      "pressed" (same as revealed blank), and places them in the
            //      local UpdateTracker for use by the next two methods. If a
            //      single unrevealed cell is clicked, that single cell is
            //      added. If a revealed cell with the correct number of
            //      adjacent flags is clicked, all adjacent non-flagged
            //      unrevealed cells are displayed as clicked and added to the
            //      UpdateTracker. The smiley icon is also set to the "shocked"
            //      expression.
            //  If right click: rightClicked boolean is set to true. It will be
            //      setback to false either when the mouse leaves the cell
            //      (aborted click), or when the right button is released
            //      (successful click).
            @Override
            public void mousePressed(MouseEvent e) {
                if (game.getGameState() > Game.IN_PROGRESS)
                    return;
                if (e.getButton() == MouseEvent.BUTTON3) {
                    rightClicked = true;
                    return;
                }
                if (e.getButton() != MouseEvent.BUTTON1)
                    return;
                if (game.getViewState(row, col) == Cell.UNREVEALED) {
                    clicked.addUpdate(new Posn(row, col));
                    buttons[row][col].setIcon(revealedBlank);
                } else if (game.getViewState(row, col) == countFlaggedSurrounding()) {
                    for (int i=row-1; i<=row+1; i++)
                        for (int j=col-1; j<=col+1; j++)
                            if (isValidCell(i, j) && game.getViewState(i, j) == Cell.UNREVEALED) {
                                buttons[i][j].setIcon(revealedBlank);
                                clicked.addUpdate(new Posn(i, j));
                            }
                }
                parent.getInfoPanel().setSmileyShocked();
            }

            //  Most times this method is called, nothing will be done, as the
            //      cursor is simply gliding across the window. When the mouse
            //      has been dragged out of a cell after pressing a button,
            //      however, whether left or right, this indicates an aborted
            //      click and the previous state must be restored. In the case
            //      of a left click, this is done by updating the cells at the
            //      coordinates stored in clicked back to unrevealed, and in
            //      the case of a right click, this is done by resetting
            //      rightClicked to false.
            @Override
            public void mouseExited(MouseEvent e) {
                rightClicked = false;
                updateCells(clicked);
                parent.getInfoPanel().updateSmiley();
            }

            //  For left click: When the mouse is finally released, the
            //      UpdateTracker can be cleared, as if the mouse is still on
            //      the initially clicked cell, the view state will be updated
            //      by the ActionListener. If the mouse was already dragged
            //      away, mouseExited was already called, and clicked is empty.
            //  For right click: If rightClicked is true, this means a
            //      successful right click has been performed. This click must
            //      be invoked in the Game object and the affected cells' icons
            //      must be updated. If the right click results in a win, the
            //      timer is halted and a call is passed back up to the
            //      OuterFrame to check for a high score.
            public void mouseReleased(MouseEvent e) {
                clicked.clear();
                if (rightClicked) {
                    if (game.getGameState() == Game.NOT_STARTED)
                        parent.getInfoPanel().startTimer();
                    game.rightClickCell(row, col);
                    parent.getInfoPanel().updateMineCount();
                    updateCells(game.getUpdateTracker());
                    parent.getInfoPanel().updateSmiley();
                    rightClicked = false;
                    if (game.getGameState() == Game.OVER_WIN) {
                        parent.getInfoPanel().haltTimer();
                        parent.processWin();
                    }
                }
            }

            //  Private method to count the number of flagged cells surrounding
            //      the current cell. This is used to determine whether the
            //      cells surrounding a clicked revealed cell should be
            //      displayed as clicked, as they should only be shown as
            //      clicked if the click will result in cells being revealed.
            private int countFlaggedSurrounding() {
                int count = 0;
                for (int i=row-1; i<=row+1; i++)
                    for (int j=col-1; j<=col+1; j++)
                        if (isValidCell(i, j) && game.getViewState(i, j) == Cell.FLAGGED)
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
            default:
                icon = unrevealed;
        }
        return icon;
    }

}
