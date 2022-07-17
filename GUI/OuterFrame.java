package GUI;

import javax.swing.*;
import Game.*;
import java.awt.*;
import java.awt.event.*;

public class OuterFrame {

    //  OuterFrame stores and modifies a JFrame, which is the game window.
    //      This JFrame contains one element, a JPanel returned by a GamePanel
    //      object. The JFrame also has JMenus to set the difficulty, reset the
    //      game, and display an info/credits box.

    private static final ImageIcon mineIcon = new ImageIcon("Image/mineIcon.png");

    private final JFrame frame = new JFrame();
    private final JRadioButtonMenuItem beginner, intermediate, expert;
    private GamePanel gamePanel = new GamePanel(Game.INTERMEDIATE);

    //  This constructor initializes the JFrame for an intermediate difficulty
    //      game and centers the frame on the screen. For the sake of
    //      readability, the menus are initialized in a call to a separate
    //      private method.
    public OuterFrame() {
        frame.setTitle("jMinesweeper by Kai Sandstrom");
        frame.setIconImage(mineIcon.getImage());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
        frame.setResizable(false);

        beginner = new JRadioButtonMenuItem("Beginner", false);
        intermediate = new JRadioButtonMenuItem("Intermediate", true);
        expert = new JRadioButtonMenuItem("Expert", false);
        initializeMenus();

        gamePanel = new GamePanel(Game.INTERMEDIATE);
        frame.add(gamePanel.getGamePanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    //  Populates a JMenuBar with menu options, mnemonics, accelerators, and
    //      ActionListeners, and adds the menu bar to the frame.
    private void initializeMenus() {
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
    }

    //  When the difficulty is changed, the entire GamePanel object is
    //      replaced, and the new JPanel that it returns is placed into the
    //      frame in place of the old one. In addition to simply swapping out
    //      the JPanel, the JFrame must be resized and re-centered.
    private void resetFrame(int difficulty) {
        Point oldLocation = frame.getLocation();
        Dimension oldSize = frame.getSize();
        frame.remove(gamePanel.getGamePanel());
        gamePanel = new GamePanel(difficulty);
        frame.add(gamePanel.getGamePanel());
        frame.pack();
        Dimension newSize = frame.getSize();
        frame.setLocation(getNewLocation(oldLocation, oldSize, newSize));
    }

    //  This method returns a Point representing the top-left corner of the new
    //      window such that the newly-resized window is centered on the same
    //      point as the old one. This makes starting a new game on a different
    //      difficulty less jarring after moving the window on the screen.
    private Point getNewLocation(Point oldLocation, Dimension oldSize, Dimension newSize) {
        int newX = (int) ((oldSize.getWidth()/2.0) - (newSize.getWidth()/2.0) + oldLocation.getX());
        int newY = (int) ((oldSize.getHeight()/2.0) - (newSize.getHeight()/2.0) + oldLocation.getY());
        return new Point(newX, newY);
    }

    //  This ActionListener resets the GamePanel with a new difficulty when the
    //      difficulty selection options are chosen in the menu.
    private class DifficultyListener implements ActionListener {
        //  DifficultyListener takes the individual object it's observing as an
        //      argument for easy reference. Each difficulty setting button is
        //      given a separate instance of DifficultyListener.
        private final JRadioButtonMenuItem item;
        public DifficultyListener(JRadioButtonMenuItem listened) {
            item = listened;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            //  If the item has become selected, the previously-selected option
            //      must be unselected before resetting the panel. Since we
            //      don't have an easy reference to the previously-selected
            //      option, it's easiest to unselect all three and re-select
            //      the one this ActionListener is processing.
            if (item.isSelected()) {
                beginner.setSelected(false);
                intermediate.setSelected(false);
                expert.setSelected(false);
                item.setSelected(true);
                resetFrame(textToDifficulty());
            //  If the item has become unselected, the user clicked the
            //      currently-selected option, and no reset should be
            //      performed. The element is set back to selected.
            } else {
                item.setSelected(true);
            }
        }

        //  Simple method to map menu option text to difficulty levels.
        private int textToDifficulty() {
            if (item.getText().equals("Beginner"))
                return Game.BEGINNER;
            if (item.getText().equals("Intermediate"))
                return Game.INTERMEDIATE;
            else // item.getText().equals("Expert")
                return Game.EXPERT;
        }
    }

    //  Resets the game panel object to start a new game of the same difficulty
    //      when the New option is chosen or F2 is pressed. Note that this is
    //      different from resetPanel. In this case, a call is made to the
    //      GamePanel object to reset for a new game, whereas in resetPanel,
    //      the GamePanel is thrown out and replaced with a new GamePanel for
    //      a different difficulty.
    private class NewGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gamePanel.reset();
        }
    }

    //  Displays a simple dialogue box with credit information when the
    //      "About jMinesweeper..." option is chosen.
    private class AboutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(frame, "jMinesweeper\nBy Kai Sandstrom\n2022",
                    "About jMinesweeper", JOptionPane.PLAIN_MESSAGE, mineIcon);
        }
    }

}


