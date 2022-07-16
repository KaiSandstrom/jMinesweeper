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

    private static final ImageIcon smileyNormal = new ImageIcon("Image/smileyNormal.png");
    private static final ImageIcon smileyPressed = new ImageIcon("Image/smileyPressed.png");
    private static final ImageIcon smileyShocked = new ImageIcon("Image/smileyShocked.png");
    private static final ImageIcon smileyDead = new ImageIcon("Image/smileyDead.png");
    private static final ImageIcon smileyCool = new ImageIcon("Image/smileyCool.png");

    private final JPanel infoPanel;
    private final GamePanel gamePanel;
    private final JButton smiley;
    private final NumDisplayMines mineCount;
    private final NumDisplayTimer timeCount;

    private Game game;


    public InfoPanel(GamePanel gp, Game g) {
        gamePanel = gp;
        game = g;
        infoPanel = new JPanel(new GridLayout(1, 3, 0, 0));
        smiley = new JButton(smileyNormal);
        mineCount = new NumDisplayMines(game);
        timeCount = new NumDisplayTimer();
        initialize();
    }

    //  Initializes the InfoPanel. This currently only initializes the smiley,
    //      but will eventually initialize the numeric displays as well.
    private void initialize() {
        JPanel mineCountHolder = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
        mineCountHolder.add(mineCount.getPanel());
        mineCountHolder.setBackground(new Color(198, 198, 198));
        infoPanel.add(mineCountHolder);
        initSmiley();
        JPanel timeCountHolder = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 8));
        timeCountHolder.add(timeCount.getPanel());
        timeCountHolder.setBackground(new Color(198, 198, 198));
        infoPanel.add(timeCountHolder);
    }

    private void initSmiley() {
        smiley.setMargin(new Insets(0, 0, 0, 0));
        smiley.setBorder(new EmptyBorder(0, 0, 0, 0));
        JPanel smileyHolder = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
        smileyHolder.add(smiley);
        smileyHolder.setBackground(new Color(198, 198, 198));
        infoPanel.add(smileyHolder);
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
        int state = game.getGameState();
        if (state == Game.IN_PROGRESS || state == Game.NOT_STARTED)
            smiley.setIcon(smileyNormal);
        else if (state == Game.OVER_LOSS)
            smiley.setIcon(smileyDead);
        else // state == Game.OVER_WIN
            smiley.setIcon(smileyCool);
    }

    public void startTimer() {
        timeCount.startTimer();
    }

    public void haltTimer() {
        timeCount.haltTimer();
    }

    public void reset(Game g) {
        game = g;
        mineCount.setGame(g);
        mineCount.setNums();
        timeCount.clearTimer();
    }

    public void updateMineCount() {
        mineCount.setNums();
    }

}
