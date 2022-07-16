package GUI;

import javax.swing.*;
import Game.*;
import java.awt.*;
import java.awt.event.*;

public class OuterFrame {

    //  This JFrame is the game window. It holds one element, a JPanel returned
    //      by a GamePanel object. Eventually, this panel will include menus,
    //      and selecting a new difficulty will cause a new GamePanel to be
    //      created and replace the previous one, causing the JFrame to resize.

    public static final ImageIcon mineIcon = new ImageIcon("Image/mineIcon.png");

    private final JFrame frame;
    private final JRadioButtonMenuItem beginner, intermediate, expert;
    private GamePanel gamePanel = new GamePanel(Game.INTERMEDIATE);

    public OuterFrame() {
        frame = new JFrame();
        beginner = new JRadioButtonMenuItem("Beginner", false);
        intermediate = new JRadioButtonMenuItem("Intermediate", true);
        expert = new JRadioButtonMenuItem("Expert", false);
        init();
    }

    private void init() {
        frame.setTitle("jMinesweeper by Kai Sandstrom");
        frame.setIconImage(mineIcon.getImage());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
        frame.setResizable(false);

        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        menuBar.add(gameMenu);
        menuBar.add(helpMenu);

        JMenuItem newGame = new JMenuItem("New");
        newGame.addActionListener(new NewGameListener());
        newGame.setMnemonic(KeyEvent.VK_N);
        newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        beginner.setMnemonic(KeyEvent.VK_B);
        intermediate.setMnemonic(KeyEvent.VK_I);
        expert.setMnemonic(KeyEvent.VK_E);
        beginner.addActionListener(new DifficultyListener(beginner));
        intermediate.addActionListener(new DifficultyListener(intermediate));
        expert.addActionListener(new DifficultyListener(expert));
        gameMenu.add(newGame);
        gameMenu.addSeparator();
        gameMenu.add(beginner);
        gameMenu.add(intermediate);
        gameMenu.add(expert);

        JMenuItem about = new JMenuItem("About jMinesweeper...");
        about.setMnemonic(KeyEvent.VK_A);
        about.addActionListener(new AboutListener());
        helpMenu.add(about);

        frame.setJMenuBar(menuBar);

        gamePanel = new GamePanel(Game.INTERMEDIATE);
        frame.add(gamePanel.getGamePanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void resetPanel(int difficulty) {
        Point oldLocation = frame.getLocation();
        Dimension oldSize = frame.getSize();
        frame.remove(gamePanel.getGamePanel());
        gamePanel = new GamePanel(difficulty);
        frame.add(gamePanel.getGamePanel());
        frame.pack();
        Dimension newSize = frame.getSize();
        frame.setLocation(getNewLocation(oldLocation, oldSize, newSize));
    }

    private Point getNewLocation(Point oldLocation, Dimension oldSize, Dimension newSize) {
        int newX = (int) ((oldSize.getWidth()/2.0) - (newSize.getWidth()/2.0) + oldLocation.getX());
        int newY = (int) ((oldSize.getHeight()/2.0) - (newSize.getHeight()/2.0) + oldLocation.getY());
        return new Point(newX, newY);
    }

    private class DifficultyListener implements ActionListener {
        private final JRadioButtonMenuItem item;
        public DifficultyListener(JRadioButtonMenuItem listened) {
            item = listened;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if (item.isSelected()) {
                beginner.setSelected(false);
                intermediate.setSelected(false);
                expert.setSelected(false);
                item.setSelected(true);
                resetPanel(textToDifficulty());
            } else {
                item.setSelected(true);
            }
        }

        private int textToDifficulty() {
            if (item.getText().equals("Beginner"))
                return Game.BEGINNER;
            if (item.getText().equals("Intermediate"))
                return Game.INTERMEDIATE;
            else // item.getText().equals("Expert")
                return Game.EXPERT;
        }
    }

    private class NewGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gamePanel.reset();
        }
    }

    private class AboutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(frame, "jMinesweeper\nBy Kai Sandstrom\n2022",
                    "About jMinesweeper", JOptionPane.PLAIN_MESSAGE, mineIcon);
        }
    }

}


