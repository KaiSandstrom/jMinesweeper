package GUI;

import javax.swing.*;
import Game.*;

public class OuterFrame {

    //  This JFrame is the game window. It holds one element, a JPanel returned
    //      by a GamePanel object. Eventually, this panel will include menus,
    //      and selecting a new difficulty will cause a new GamePanel to be
    //      created and replace the previous one, causing the JFrame to resize.

    private final JFrame frame;

    public OuterFrame() {
        frame = new JFrame();
        initPanel(Game.INTERMEDIATE);
    }

    public void initPanel(int difficulty) {
        frame.setTitle("jMinesweeper by Kai Sandstrom");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        GamePanel gamePanel = new GamePanel(difficulty);
        frame.add(gamePanel.getGamePanel());

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
