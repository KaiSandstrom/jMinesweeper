package GUI;
;
import Game.*;
import javax.swing.*;
import java.awt.*;

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

    private final int rows, cols;
    private final Game game;
    private final JPanel gamePanel;

    public GamePanel(int nRows, int nCols, Game g) {
        rows = nRows;
        cols = nCols;
        game = g;
        gamePanel = new JPanel(new BorderLayout());
        initialize();
    }

    private void initialize() {
        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.add(new JLabel(borderBLCorner));
        for (int i=0; i<cols; i++)
            south.add(new JLabel(borderBottom));
        south.add(new JLabel(borderBRCorner));
        gamePanel.add(south, BorderLayout.SOUTH);

        JPanel west = new JPanel(new GridLayout(rows, 1, 0, 0));
        for (int i=0; i<rows; i++)
            west.add(new JLabel(borderLeft));
        gamePanel.add(west, BorderLayout.WEST);

        JPanel east = new JPanel(new GridLayout(rows, 1, 0, 0));
        for (int i=0; i<rows; i++)
            east.add(new JLabel(borderRight));
        gamePanel.add(east, BorderLayout.EAST);

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

        gamePanel.add(north, BorderLayout.NORTH);

        CellBoardPanel cellBoardPanel = new CellBoardPanel(rows, cols, game);
        gamePanel.add(cellBoardPanel.getBoard(), BorderLayout.CENTER);
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }

}
