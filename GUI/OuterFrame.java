package GUI;

import javax.swing.*;
import Game.*;

public class OuterFrame {

    private final JFrame frame;
    private Game game;

    public OuterFrame() {
        frame = new JFrame();
        initPanel(Game.INTERMEDIATE);
    }

    public void initPanel(int difficulty) {
        int rows, cols, numMines;
        switch (difficulty) {
            case Game.BEGINNER:
                rows = 9; cols = 9; numMines = 10; break;
            case Game.EXPERT:
                rows = 16; cols = 30; numMines = 99; break;
            case Game.INTERMEDIATE:
            default:
                rows = 16; cols = 16; numMines = 40;
        }
        frame.setTitle("jMinesweeper by Kai Sandstrom");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        UpdateTracker updateTracker = new UpdateTracker();
        game = new Game(difficulty, updateTracker);
        GamePanel gamePanel = new GamePanel(rows, cols, game);
        frame.add(gamePanel.getGamePanel());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
