package GUI;

import Board.Cell;
import Game.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePanel {
    public static final ImageIcon borderTop = new ImageIcon("Image/borderTop.png");
    public static final ImageIcon borderRight = new ImageIcon("Image/borderRight.png");
    public static final ImageIcon borderBottom = new ImageIcon("Image/borderBottom.png");
    public static final ImageIcon borderLeft = new ImageIcon("Image/borderLeft.png");
    public static final ImageIcon borderTLCorner = new ImageIcon("Image/borderTLCorner.png");
    public static final ImageIcon borderTRCorner = new ImageIcon("Image/borderTRCorner.png");
    public static final ImageIcon borderBRCorner = new ImageIcon("Image/borderBRCorner.png");
    public static final ImageIcon borderBLCorner = new ImageIcon("Image/borderBLCorner.png");
    public static final ImageIcon borderMid = new ImageIcon("Image/borderMid.png");
    public static final ImageIcon borderMidLeft = new ImageIcon("Image/borderMidLeft.png");
    public static final ImageIcon borderMidRight = new ImageIcon("Image/borderMidRight.png");

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

    private JFrame frame;
    private final int rows, cols;
    private final Game game;
    private final UpdateTracker updateTracker;

    private JButton[][] buttons;

    public GamePanel(int nRows, int nCols) {
        rows = nRows;
        cols = nCols;
        buttons = new JButton[rows][cols];
        updateTracker = new UpdateTracker();
        game = new Game(Game.EXPERT, updateTracker);

        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setTitle("jMinesweeper by Kai Sandstrom");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.add(new JLabel(borderBLCorner));
        for (int i=0; i<cols; i++)
            south.add(new JLabel(borderBottom));
        south.add(new JLabel(borderBRCorner));
        frame.add(south, BorderLayout.SOUTH);

        JPanel west = new JPanel(new GridLayout(rows, 1, 0, 0));
        for (int i=0; i<rows; i++)
            west.add(new JLabel(borderLeft));
        frame.add(west, BorderLayout.WEST);

        JPanel east = new JPanel(new GridLayout(rows, 1, 0, 0));
        for (int i=0; i<rows; i++)
            east.add(new JLabel(borderRight));
        frame.add(east, BorderLayout.EAST);

        JPanel north = new JPanel(new BorderLayout());
        north.setBackground(new Color(198, 198, 198));

        JPanel nNorth = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        nNorth.add(new JLabel(borderTLCorner));
        for (int i=0; i<cols; i++)
            nNorth.add(new JLabel(borderTop));
        nNorth.add(new JLabel(borderTRCorner));
        north.add(nNorth, BorderLayout.NORTH);

        JPanel nWest = new JPanel(new GridLayout(2, 1, 0, 0));
        nWest.add(new JLabel(borderLeft));
        nWest.add(new JLabel(borderLeft));
        north.add(nWest, BorderLayout.WEST);

        JPanel nEast = new JPanel(new GridLayout(2, 1, 0, 0));
        nEast.add(new JLabel(borderRight));
        nEast.add(new JLabel(borderRight));
        north.add(nEast, BorderLayout.EAST);

        JPanel nSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        nSouth.add(new JLabel(borderMidLeft));
        for (int i=0; i<cols; i++)
            nSouth.add(new JLabel(borderMid));
        nSouth.add(new JLabel(borderMidRight));
        north.add(nSouth, BorderLayout.SOUTH);

        JLabel prototype = new JLabel("PROTOTYPE", SwingConstants.CENTER);
        prototype.setFont(new Font("", Font.BOLD, 48));
        north.add(prototype, BorderLayout.CENTER);

        frame.add(north, BorderLayout.NORTH);

        JPanel board = new JPanel(new GridLayout(rows, cols, 0, 0));
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

        frame.add(board, BorderLayout.CENTER);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addCellClickHandler(int row, int col) {
        buttons[row][col].addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)
                    game.leftClickCell(row, col);
                else if (e.getButton() == MouseEvent.BUTTON3)
                    game.rightClickCell(row, col);
                updateCells();
            }
        });
    }

    private void updateCells() {
        for (Posn pos : updateTracker) {
            int row = pos.row();
            int col = pos.col();
            JButton button = buttons[row][col];
            int viewState = game.getViewState(row, col);
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
            button.setIcon(icon);
        }
        game.printBoard();
    }

}
