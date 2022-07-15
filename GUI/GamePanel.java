package GUI;

import Game.*;
import javax.swing.*;
import java.awt.*;

public class GamePanel {

    //  The GamePanel holds a JPanel that contains the two main dynamic regions
    //      of the game window: the board, and the info panel. The
    //      board is described in CellBoardPanel, and is placed in the
    //      BorderLayout.CENTER region of the GamePanel. The East, West, and
    //      South regions contain only border graphics. The North region, also
    //      using BorderLayout, contains the info panel in its Center region,
    //      and border graphics everywhere else.

    public static final ImageIcon borderEdgeHoriz = new ImageIcon("Image/borderEdgeHoriz.png");
    public static final ImageIcon borderEdgeVert = new ImageIcon("Image/borderEdgeVert.png");
    public static final ImageIcon borderCornerTL = new ImageIcon("Image/borderCornerTL.png");
    public static final ImageIcon borderCornerTR = new ImageIcon("Image/borderCornerTR.png");
    public static final ImageIcon borderCornerBR = new ImageIcon("Image/borderCornerBR.png");
    public static final ImageIcon borderCornerBL = new ImageIcon("Image/borderCornerBL.png");
    public static final ImageIcon borderMidLeft = new ImageIcon("Image/borderMidLeft.png");
    public static final ImageIcon borderMidRight = new ImageIcon("Image/borderMidRight.png");

    private final int rows, cols;
    private final JPanel gamePanel;
    private Game game;

    public GamePanel(int difficulty) {
        switch (difficulty) {
            case Game.BEGINNER:
                rows = 9; cols = 9; break;
            case Game.EXPERT:
                rows = 16; cols = 30; break;
            case Game.INTERMEDIATE:
            default:
                rows = 16; cols = 16;
        }
        game = new Game(difficulty, new UpdateTracker());
        gamePanel = new JPanel(new BorderLayout());
        initialize();
    }

    //  First creates the border, which varies in size based on difficulty.
    //      The info panel will eventually be implemented, but for now a
    //      placeholder "PROTOTYPE" label takes its place. Next, the cell board
    //      is added.
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
        north.setBackground(new Color(198, 198, 198));

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

        JLabel prototype = new JLabel("PROTOTYPE", SwingConstants.CENTER);
        prototype.setFont(new Font("", Font.BOLD, 48));
        north.add(prototype, BorderLayout.CENTER);

        gamePanel.add(north, BorderLayout.NORTH);

        CellBoardPanel cellBoardPanel = new CellBoardPanel(rows, cols, game);
        gamePanel.add(cellBoardPanel.getBoard(), BorderLayout.CENTER);
    }

    //  Will be called when the smiley icon in the info panel is pressed.
    private void reset() {
        Game newGame = new Game(game.getDifficulty(), new UpdateTracker());
        CellBoardPanel cellBoardPanel = new CellBoardPanel(rows, cols, newGame);
        gamePanel.add(cellBoardPanel.getBoard(), BorderLayout.CENTER);
        game = newGame;
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }

}
