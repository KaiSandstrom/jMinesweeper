package GUI;

import Game.Game;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InfoPanel {

    //  An InfoPanel is a wrapper for a JPanel containing the three elements in
    //      the top info bar of the jMinesweeper window: the smiley reset
    //      button, the timer, and the display showing the number of unflagged
    //      mines remaining.

    //  The two numeric displays are currently unimplemented.

    public static final ImageIcon smileyNormal = new ImageIcon("Image/smileyNormal.png");
    public static final ImageIcon smileyPressed = new ImageIcon("Image/smileyPressed.png");
    public static final ImageIcon smileyShocked = new ImageIcon("Image/smileyShocked.png");
    public static final ImageIcon smileyDead = new ImageIcon("Image/smileyDead.png");
    public static final ImageIcon smileyCool = new ImageIcon("Image/smileyCool.png");

    private final JPanel infoPanel;
    private final GamePanel gamePanel;
    private final JButton smiley;
    private Game game;


    public InfoPanel(GamePanel gp, Game g) {
        gamePanel = gp;
        game = g;
        infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        smiley = new JButton(smileyNormal);
        initialize();
    }

    //  Initializes the InfoPanel. This currently only initializes the smiley,
    //      but will eventually initialize the numeric displays as well.
    private void initialize() {
        infoPanel.setBackground(new Color(198, 198, 198));
        initSmiley();
    }

    private void initSmiley() {
        smiley.setMargin(new Insets(0, 0, 0, 0));
        smiley.setBorder(new EmptyBorder(0, 0, 0, 0));
        infoPanel.add(smiley);
        smiley.addMouseListener(new MouseAdapter() {

            //  When the smiley is clicked, call the GamePanel's reset method.
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1)
                    return;
                gamePanel.reset();
                smiley.setIcon(smileyNormal);
            }

            //  Pressed but not yet released: Display the smileyPressed icon.
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON1)
                    return;
                smiley.setIcon(smileyPressed);
            }

            //  Click aborted: Restore the previous icon with updateSmiley.
            @Override
            public void mouseExited(MouseEvent e) {
                updateSmiley();
            }

        });
    }

    //  Called in GamePanel when building the initial JPanel
    public JPanel getInfoJPanel() {
        return infoPanel;
    }

    public void setGame(Game g) {
        game = g;
    }

    //  Called by the CellBoardPanel's cell buttons whenever a cell is clicked.
    //      This sets the smiley to the "shocked" expression when a cell is
    //      clicked but not yet released.
    public void setSmileyShocked() {
        smiley.setIcon(smileyShocked);
    }

    //  Called by the smiley button's mouse listener when a smiley click is
    //      aborted, and by the CellBoardPanel's cell buttons' mouse listeners
    //      after either a successful or aborted click.
    public void updateSmiley() {
        if (game.getGameState() == Game.IN_PROGRESS)
            smiley.setIcon(smileyNormal);
        else if (game.getGameState() == Game.OVER_LOSS)
            smiley.setIcon(smileyDead);
        else // game.getGameState() == Game.OVER_WIN
            smiley.setIcon(smileyCool);
    }

}
