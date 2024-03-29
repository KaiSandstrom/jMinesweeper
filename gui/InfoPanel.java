package gui;

import game.Game;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class InfoPanel {

    //  An InfoPanel is a wrapper for a JPanel containing the three elements in
    //      the top info bar of the jMinesweeper window: the display showing
    //      the number of unflagged mines remaining, the smiley icon that
    //      serves as a reset button, and the timer.

    private final ImageIcon smileyNormal = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/smileyNormal.png")));
    private final ImageIcon smileyPressed = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/smileyPressed.png")));
    private final ImageIcon smileyShocked = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/smileyShocked.png")));
    private final ImageIcon smileyDead = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/smileyDead.png")));
    private final ImageIcon smileyCool = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/smileyCool.png")));

    private final JPanel infoPanel;
    private final GamePanel gamePanel;
    private final JButton smiley = new JButton(smileyNormal);
    private final NumDisplayMines mineCount;
    private final NumDisplayTimer timeCount = new NumDisplayTimer();

    private Game game;


    public InfoPanel(GamePanel gp, Game g) {
        gamePanel = gp;
        game = g;
        infoPanel = new JPanel(new GridLayout(1, 3, 0, 0));
        mineCount = new NumDisplayMines(game);
        initialize();
    }

    //  Initializes the three elements in the InfoPanel and places them in the
    //      correct locations.
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

    //  Initializes the smiley button, including listeners for mouse events.
    private void initSmiley() {
        smiley.setBorder(new EmptyBorder(0, 0, 0, 0));
        JPanel smileyHolder = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 6));
        smileyHolder.add(smiley);
        smileyHolder.setBackground(new Color(198, 198, 198));
        infoPanel.add(smileyHolder);

        //  ActionListener processes successful left clicks
        smiley.addActionListener(e -> {
            gamePanel.reset();
            smiley.setIcon(smileyNormal);
        });

        //  MouseAdapter adds visual feedback for left clicks.
        smiley.addMouseListener(new MouseAdapter() {
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

    //  Used to fetch the timer's value upon winning, to check for a high score
    public int getTimeCount() {
        return timeCount.getTimeCount();
    }

    //  Start and halt methods are used by the CellBoardPanel to manipulate the
    //      timer based on the results of clicks.
    public void startTimer() {
        timeCount.startTimer();
    }

    public void haltTimer() {
        timeCount.haltTimer();
    }

    //  Resets both the timer and mine count, using a new Game object passed
    //      from the GamePanel.
    public void reset(Game g) {
        game = g;
        mineCount.setGame(g);
        mineCount.setNums();
        timeCount.clearTimer();
        smiley.setIcon(smileyNormal);
    }

    //  Invoked by CellBoardPanel when a successful right click has been
    //      performed.
    public void updateMineCount() {
        mineCount.setNums();
    }

}
