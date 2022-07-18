package gui;

import game.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Objects;

public class GamePanel {

    //  The GamePanel holds a JPanel that contains the two main dynamic regions
    //      of the game window: the board, and the info panel. The
    //      board is described in CellBoardPanel, and is placed in the
    //      BorderLayout.CENTER region of the GamePanel. The East, West, and
    //      South regions contain only border graphics. The North region, also
    //      using BorderLayout, contains the info panel in its Center region,
    //      and border graphics everywhere else.

    //  These first three are static because they are used by the static
    //      methods for calculating the size in pixels of a theoretical
    //      game panel with a given number of rows/columns.
    private static final ImageIcon borderEdgeHoriz = new ImageIcon(Objects.requireNonNull(
            GamePanel.class.getResource("/resources/borderEdgeHoriz.png")));
    private static final ImageIcon borderEdgeVert = new ImageIcon(Objects.requireNonNull(
            GamePanel.class.getResource("/resources/borderEdgeVert.png")));
    private static final ImageIcon borderCornerTL = new ImageIcon(Objects.requireNonNull(
            GamePanel.class.getResource("/resources/borderCornerTL.png")));
    private final ImageIcon borderCornerTR = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/borderCornerTR.png")));
    private final ImageIcon borderCornerBR = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/borderCornerBR.png")));
    private final ImageIcon borderCornerBL = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/borderCornerBL.png")));
    private final ImageIcon borderMidLeft = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/borderMidLeft.png")));
    private final ImageIcon borderMidRight = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/borderMidRight.png")));

    private final int rows, cols;
    private final JPanel gamePanel;
    private Game game;
    private final CellBoardPanel board;
    private final InfoPanel info;

    public GamePanel(Difficulty difficulty) {
        rows = difficulty.getRows();
        cols = difficulty.getColumns();
        gamePanel = new JPanel(new BorderLayout());
        game = new Game(difficulty, new UpdateTracker());
        info = new InfoPanel(this, game);
        board = new CellBoardPanel(rows, cols, game, info);
        initialize();
    }

    //  First creates the border, which varies in size based on difficulty.
    //      Two JPanels containing meaningful information, the panel returned
    //      by the final CellBoardPanel field and the panel returned by the
    //      final InfoPanel field, are inserted into the JPanel stored and
    //      returned by this GamePanel.
    private void initialize() {
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.add(new JLabel(borderCornerBL));
        for (int i=0; i<cols; i++)
            south.add(new JLabel(borderEdgeHoriz));
        south.add(new JLabel(borderCornerBR));
        gamePanel.add(south, BorderLayout.SOUTH);

        JPanel west = new JPanel(new GridLayout(rows, 1, 0, 0));
        for (int i=0; i<rows; i++)
            west.add(new JLabel(borderEdgeVert));
        gamePanel.add(west, BorderLayout.WEST);

        JPanel east = new JPanel(new GridLayout(rows, 1, 0, 0));
        for (int i=0; i<rows; i++)
            east.add(new JLabel(borderEdgeVert));
        gamePanel.add(east, BorderLayout.EAST);

        JPanel north = new JPanel(new BorderLayout());

        JPanel nNorth = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        nNorth.add(new JLabel(borderCornerTL));
        for (int i=0; i<cols; i++)
            nNorth.add(new JLabel(borderEdgeHoriz));
        nNorth.add(new JLabel(borderCornerTR));
        north.add(nNorth, BorderLayout.NORTH);

        JPanel nWest = new JPanel(new GridLayout(2, 1, 0, 0));
        nWest.add(new JLabel(borderEdgeVert));
        nWest.add(new JLabel(borderEdgeVert));
        north.add(nWest, BorderLayout.WEST);

        JPanel nEast = new JPanel(new GridLayout(2, 1, 0, 0));
        nEast.add(new JLabel(borderEdgeVert));
        nEast.add(new JLabel(borderEdgeVert));
        north.add(nEast, BorderLayout.EAST);

        JPanel nSouth = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        nSouth.add(new JLabel(borderMidLeft));
        for (int i=0; i<cols; i++)
            nSouth.add(new JLabel(borderEdgeHoriz));
        nSouth.add(new JLabel(borderMidRight));
        north.add(nSouth, BorderLayout.SOUTH);

        north.add(info.getInfoJPanel(), BorderLayout.CENTER);

        gamePanel.add(north, BorderLayout.NORTH);

        gamePanel.add(board.getBoardJPanel(), BorderLayout.CENTER);

        gamePanel.setBackground(Color.BLACK);
        gamePanel.setBorder(new EmptyBorder(1, 1, 1, 1));
    }

    public Game getGame() {
        return game;
    }

    //  Called when the smiley icon is clicked, the "New" menu option is
    //      clicked, or the F2 key is pressed.
    public void reset() {
        game = new Game(game.getDifficulty(), new UpdateTracker());
        board.reset(game);
        info.reset(game);
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }

    //  Returns the width of a theoretical GamePanel with a given number of
    //      columns.
    public static int getWidthInPixels(int nCols) {
        return 2 + (2 * borderCornerTL.getIconWidth()) + (nCols * borderEdgeHoriz.getIconWidth());
    }

    //  Returns the height of a theoretical GamePanel with a given number of
    //      rows.
    public static int getHeightInPixels(int nRows) {
        return 2 + (3 * borderCornerTL.getIconHeight()) + ((nRows+2) * borderEdgeVert.getIconHeight());
    }

    //  Returns the number of columns that can fit into a board of a
    //      theoretical width in pixels.
    public static int getMaxColsFromWidthInPixels(int pix) {
        return (pix - (2 + 2*(borderCornerTL.getIconWidth()))) / borderEdgeHoriz.getIconWidth();
    }

    //  Returns the number of rows that can fit into a board of a theoretical
    //      height in pixels.
    public static int getMaxRowsFromHeightInPixels(int pix) {
        return (pix - (2 + 3*(borderCornerTL.getIconHeight()) + 2*borderEdgeVert.getIconHeight())) /
                borderEdgeVert.getIconHeight();
    }

}
