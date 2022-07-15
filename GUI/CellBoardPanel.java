package GUI;

import Board.Cell;
import Game.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CellBoardPanel {

    //  A CellBoardPanel represents the area of the game window containing the
    //      grid of cell buttons. On each click, the proper call must be made
    //      to the Game object to process the operation, and the cells must
    //      display correctly.

    public static final ImageIcon unrevealed = new ImageIcon("Image/unrevealed.png");
    public static final ImageIcon revealedBlank = new ImageIcon("Image/revealedBlank.png");
    public static final ImageIcon revealedNumber1 = new ImageIcon("Image/revealedNumber1.png");
    public static final ImageIcon revealedNumber2 = new ImageIcon("Image/revealedNumber2.png");
    public static final ImageIcon revealedNumber3 = new ImageIcon("Image/revealedNumber3.png");
    public static final ImageIcon revealedNumber4 = new ImageIcon("Image/revealedNumber4.png");
    public static final ImageIcon revealedNumber5 = new ImageIcon("Image/revealedNumber5.png");
    public static final ImageIcon revealedNumber6 = new ImageIcon("Image/revealedNumber6.png");
    public static final ImageIcon revealedNumber7 = new ImageIcon("Image/revealedNumber7.png");
    public static final ImageIcon revealedNumber8 = new ImageIcon("Image/revealedNumber8.png");
    public static final ImageIcon flagged = new ImageIcon("Image/flagged.png");
    public static final ImageIcon falseFlagged = new ImageIcon("Image/falseFlagged.png");
    public static final ImageIcon revealedMine = new ImageIcon("Image/revealedMine.png");
    public static final ImageIcon revealedExploded = new ImageIcon("Image/revealedExploded.png");

    private final JPanel board;
    private final int rows, cols;
    private final Game game;
    private final JButton[][] buttons;

    public CellBoardPanel(int nRows, int nCols, Game g) {
        rows = nRows;
        cols = nCols;
        buttons = new JButton[rows][cols];
        game = g;
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

    //  A new MouseAdapter is defined in an anonymous class. The mouseClicked
    //      method is where all game logic is invoked; the other methods are
    //      here simply to add visual feedback when clicking and releasing the
    //      mouse.
    private void addCellClickHandler(int row, int col) {
        buttons[row][col].addMouseListener(new MouseAdapter() {

            //  Special UpdateTracker used to keep track of which cells have
            //      been clicked on but not yet released on. If the player
            //      moves the mouse away from the clicked cell while the left
            //      button is pressed, no click occurs, and the cells that were
            //      previously displayed as "pressed" must be reset.
            private final UpdateTracker clicked = new UpdateTracker();

            //  This is the most important method in this inner class.
            //      mouseClicked determines whether the left or right mouse
            //      button was clicked, and calls the relevant method in the
            //      Game. After this operation, updateCells is called, changing
            //      the icons of any affected cells.
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                    game.leftClickCell(row, col);
                else if (e.getButton() == MouseEvent.BUTTON3)
                    game.rightClickCell(row, col);
                updateCells(game.getUpdateTracker());
            }

            //  Changes clicked cells' display state to "pressed" (same as
            //      revealed blank), and places them in the local UpdateTracker
            //      for use by the next two methods. If a single unrevealed
            //      cell is clicked, that single cell is added. If a revealed
            //      cell with the correct number of adjacent flags is clicked,
            //      all adjacent non-flagged unrevealed cells are displayed as
            //      clicked and added to the UpdateTracker.
            @Override
            public void mousePressed(MouseEvent e) {
                if (game.getGameState() != Game.IN_PROGRESS)
                    return;
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
            }

            //  When this method is called and clicked is nonempty, the mouse
            //      was dragged away before being released, and the icons of
            //      the affected cells must be restored.
            @Override
            public void mouseExited(MouseEvent e) {
                updateCells(clicked);
            }

            //  When the mouse is finally released, the UpdateTracker can be
            //      cleared, as if the mouse is still on the initially clicked
            //      cell, the view state will be updated by the click
            //      operation. If the mouse was already dragged away,
            //      mouseExited was already called, and clicked is empty.
            public void mouseReleased(MouseEvent e) {
                clicked.clear();
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
    public JPanel getBoard() {
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
