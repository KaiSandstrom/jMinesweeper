package GUI;

import Board.Cell;
import Game.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class CellBoardPanel {

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
    private final UpdateTracker updateTracker;
    private JButton[][] buttons;

    public CellBoardPanel(int nRows, int nCols, Game g) {
        rows = nRows;
        cols = nCols;
        buttons = new JButton[rows][cols];
        game = g;
        updateTracker = game.getUpdateTracker();
        board = new JPanel(new GridLayout(rows, cols, 0, 0));
        initialize();
    }

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

    private void addCellClickHandler(int row, int col) {
        buttons[row][col].addMouseListener(new MouseAdapter() {

            private ArrayList<Posn> clicked = new ArrayList<>();

            @Override
            public void mousePressed(MouseEvent e) {
                if (game.getGameState() != Game.IN_PROGRESS)
                    return;
                if (e.getButton() != MouseEvent.BUTTON1)
                    return;
                if (game.getViewState(row, col) == Cell.UNREVEALED) {
                    clicked.add(new Posn(row, col));
                    buttons[row][col].setIcon(revealedBlank);
                } else if (game.getViewState(row, col) == countFlaggedSurrounding()) {
                    for (int i=row-1; i<=row+1; i++)
                        for (int j=col-1; j<=col+1; j++)
                            if (isValidCell(i, j) && game.getViewState(i, j) == Cell.UNREVEALED) {
                                buttons[i][j].setIcon(revealedBlank);
                                clicked.add(new Posn(i, j));
                            }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                for (Posn pos : clicked) {
                    int r = pos.row();
                    int c = pos.col();
                    buttons[r][c].setIcon(getIconFromViewState(game.getViewState(r, c)));
                }
                clicked.clear();
            }

            public void mouseReleased(MouseEvent e) {
                clicked.clear();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                    game.leftClickCell(row, col);
                else if (e.getButton() == MouseEvent.BUTTON3)
                    game.rightClickCell(row, col);
                updateCells();
            }

            private int countFlaggedSurrounding() {
                int count = 0;
                for (int i=row-1; i<=row+1; i++)
                    for (int j=col-1; j<=col+1; j++)
                        if (isValidCell(i, j) && game.getViewState(i, j) == Cell.FLAGGED)
                            count++;
                return count;
            }

            private boolean isValidCell(int i, int j) {
                return (i>=0 && j>=0 && i<rows && j<cols);
            }
        });
    }

    public JPanel getBoard() {
        return board;
    }

    private void updateCells() {
        for (Posn pos : updateTracker) {
            int row = pos.row();
            int col = pos.col();
            JButton button = buttons[row][col];
            int viewState = game.getViewState(row, col);
            ImageIcon icon = getIconFromViewState(viewState);
            button.setIcon(icon);
        }
        game.printBoard();
    }

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
