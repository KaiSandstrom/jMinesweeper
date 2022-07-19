package gui;

import javax.swing.*;
import game.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class OuterFrame {

    //  OuterFrame stores and modifies a JFrame, which is the game window.
    //      This JFrame contains one element, a JPanel returned by a GamePanel
    //      object. The JFrame also has JMenus to set the difficulty, reset the
    //      game, and display an info/credits box.

    private final ImageIcon mineIcon = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/mineIcon.png")));

    private final JFrame frame = new JFrame();
    private final JRadioButtonMenuItem beginner, intermediate, expert, custom;
    private GamePanel gamePanel;

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
        custom = new JRadioButtonMenuItem("Custom...", false);
        initializeMenus();

        gamePanel = new GamePanel(Difficulty.INTERMEDIATE);
        frame.add(gamePanel.getGamePanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    //  Populates a JMenuBar with menu options, mnemonics, accelerators, and
    //      ActionListeners, and adds the menu bar to the frame.
    private void initializeMenus() {
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic(KeyEvent.VK_G);
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(gameMenu);
        menuBar.add(helpMenu);

        JMenuItem newGame = new JMenuItem("New");
        newGame.addActionListener(new NewGameListener());
        newGame.setMnemonic(KeyEvent.VK_N);
        newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
        beginner.setMnemonic(KeyEvent.VK_B);
        intermediate.setMnemonic(KeyEvent.VK_I);
        expert.setMnemonic(KeyEvent.VK_E);
        custom.setMnemonic(KeyEvent.VK_C);
        beginner.addActionListener(new DifficultyListener(beginner));
        intermediate.addActionListener(new DifficultyListener(intermediate));
        expert.addActionListener(new DifficultyListener(expert));
        custom.addActionListener(new CustomDiffListener());
        gameMenu.add(newGame);
        gameMenu.addSeparator();
        gameMenu.add(beginner);
        gameMenu.add(intermediate);
        gameMenu.add(expert);
        gameMenu.add(custom);

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
    private void resetFrame(Difficulty difficulty) {
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
    //      this method also prevents a newly-expanded window from jutting off
    //      the edge of the screen.
    private Point getNewLocation(Point oldLocation, Dimension oldSize, Dimension newSize) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = env.getMaximumWindowBounds();
        int maxX = (int)bounds.getX() + (int)bounds.getWidth();
        int maxY = (int)bounds.getY() + (int)bounds.getHeight();
        int newX = (int) ((oldSize.getWidth() - newSize.getWidth())/2 + oldLocation.getX());
        int newY = (int) ((oldSize.getHeight() - newSize.getHeight())/2 + oldLocation.getY());
        int rightEdge = (int)(newX+newSize.getWidth());
        int bottomEdge = (int)(newY+newSize.getHeight());
        if (newX < bounds.getX())
            newX = (int)bounds.getX();
        else if (rightEdge > maxX)
            newX = maxX - (int)newSize.getWidth();
        if (newY < bounds.getY())
            newY = (int)bounds.getY();
        else if (bottomEdge > maxY)
            newY = maxY - (int)newSize.getHeight();
        return new Point(newX, newY);
    }

    //  This ActionListener resets the GamePanel with a new difficulty when the
    //      default difficulty selection options are chosen in the menu.
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
                custom.setSelected(false);
                item.setSelected(true);
                resetFrame(textToDifficulty());
            //  If the item has become unselected, the user clicked the
            //      currently-selected option, and no reset should be
            //      performed. The element is set back to selected.
            } else {
                item.setSelected(true);
            }
        }

        //  Simple method to map menu option text to Difficulty objects.
        private Difficulty textToDifficulty() {
            if (item.getText().equals("Beginner"))
                return Difficulty.BEGINNER;
            if (item.getText().equals("Intermediate"))
                return Difficulty.INTERMEDIATE;
            else // item.getText().equals("Expert")
                return Difficulty.EXPERT;
        }
    }

    //  Resets the game panel object to start a new game of the same difficulty
    //      when the New option is chosen or F2 is pressed. Note that this is
    //      different from resetPanel. In this case, a call is made to the
    //      GamePanel object to reset for a new game, whereas in resetPanel,
    //      the GamePanel is thrown out and replaced with a new GamePanel for
    //      a different difficulty.
    //  Also re-packs the frame, to give the user an easy way to fix visual
    //      glitches. This is a stopgap until a way is found to completely
    //      resolve the issue.
    private class NewGameListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gamePanel.reset();
            frame.pack();
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

    //  ActionListener for the custom board menu option. This ActionListener
    //      displays a popup to input values, checks these values to make sure
    //      they describe a valid board, give error feedback if they don't,
    //      and reset the game panel if they do.
    private class CustomDiffListener implements ActionListener {

        //  These are made fields so that the private methods can access them.
        int rows, cols, mines;

        //  The prompts panel and its components are made final fields so that
        //      the program will remember previous inputs from the same
        //      session.
        final JPanel prompts;
        final JTextField getWidth, getHeight, getMines;

        public CustomDiffListener() {
            prompts = new JPanel(new GridLayout(3, 2, 10, 5));
            prompts.add(new JLabel("Height:"));
            getHeight = new JTextField(3);
            prompts.add(getHeight);
            prompts.add(new JLabel("Width:"));
            getWidth = new JTextField(3);
            prompts.add(getWidth);
            prompts.add(new JLabel("Mines:"));
            getMines = new JTextField(3);
            prompts.add(getMines);
        }

        //  The three fields are set to 0 at the very beginning of
        //      actionPerformed, and are set in the prompt() call immediately
        //      after. This prompt() call handles all user input and input
        //      checking. If it returns true, then the window will be
        //      reset. Next, actionPerformed checks if the input parameters
        //      match one of the default difficulty levels. If they do, the
        //      user is notified and the default difficulty is selected
        //      instead. Finally, the window is reset.
        @Override
        public void actionPerformed(ActionEvent e) {
            rows = cols = mines = 0;
            if (!prompt()) {
                custom.setSelected(false);
                return;
            }
            beginner.setSelected(false);
            intermediate.setSelected(false);
            expert.setSelected(false);
            custom.setSelected(true);
            Difficulty newDiff = new Difficulty(rows, cols, mines);
            if (newDiff.equals(Difficulty.BEGINNER)) {
                custom.setSelected(false);
                JOptionPane.showMessageDialog(frame,
                        "Custom field is the same as Beginner. Starting new Beginner game...\n ",
                        "", JOptionPane.PLAIN_MESSAGE);
                beginner.doClick();
            } else if (newDiff.equals(Difficulty.INTERMEDIATE)) {
                custom.setSelected(false);
                JOptionPane.showMessageDialog(frame,
                        "Custom field is the same as Intermediate. Starting new Intermediate game...\n ",
                        "", JOptionPane.PLAIN_MESSAGE);
                intermediate.doClick();
            } else if (newDiff.equals(Difficulty.EXPERT)) {
                custom.setSelected(false);
                JOptionPane.showMessageDialog(frame,
                        "Custom field is the same as Expert. Starting new Expert game...\n ",
                        "", JOptionPane.PLAIN_MESSAGE);
                expert.doClick();
            }
            resetFrame(newDiff);
        }

        //  The prompt method returns true if the user-input values describe a
        //      valid board, and false otherwise. User input is collected via
        //      JTextFields in a JOptionPane. Various checks are performed to
        //      verify that the values describe a valid board, and various
        //      descriptive error messages are provided in separate
        //      JOptionPanes if they are invalid. Finally, if the user input
        //      more than 1000 mines, a warning is displayed, as the mine
        //      counter display has a maximum value of 999.
        private boolean prompt() {
            int result = JOptionPane.showConfirmDialog(frame, prompts, "Custom Field",
                    JOptionPane.OK_CANCEL_OPTION);
            if (result != JOptionPane.OK_OPTION)
                return false;
            String reason = null;
            try {
                cols = Integer.parseInt(getWidth.getText());
                rows = Integer.parseInt(getHeight.getText());
                mines = Integer.parseInt(getMines.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid inputs:\n\nAll inputs must be whole numbers.\n ",
                        "", JOptionPane.PLAIN_MESSAGE);
                return false;
            }
            if (!isValidScreenSize(rows, cols)) {
                Dimension maxSize = getMaxBoardSize();
                reason = "Custom board is too big to fit on the screen.\n\nLargest possible board for your screen:\n" +
                        "Height: " + (int)maxSize.getHeight() + "\nWidth:  " + (int)maxSize.getWidth() + "\n ";
            } else if (cols < 9 || rows < 2)
                reason = "Custom board must have at least 2 rows and 8 columns.\n ";
            else if (mines > (cols * rows)-9)
                reason = "Too many mines. There must be at least 9 non-mine cells on the board.\n ";
            else if (mines < 1)
                reason = "There must be at least one mine on the board.\n ";
            if (reason != null) {
                JOptionPane.showMessageDialog(frame, "Invalid inputs:\n\n" + reason,
                        "", JOptionPane.PLAIN_MESSAGE);
                return false;
            }
            if (mines > 999) {
                String insert;
                if (mines-999 == 1)
                    insert = " 1 mine has ";
                else
                    insert = " " + (mines - 999) + " mines have ";
                JOptionPane.showMessageDialog(frame, "This board has more than 999 mines.\n\nSince the left " +
                                "numeric display in the info panel can\nonly display numbers up to 999, it will " +
                                "display a\ndashed line until" + insert + "been flagged.\n ",
                        "", JOptionPane.PLAIN_MESSAGE);
            }
            return true;
        }

        //  Private method to check that a board with a given number of rows
        //      and columns can fit on the system's screen.
        private boolean isValidScreenSize(int rows, int cols) {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Rectangle bounds = env.getMaximumWindowBounds();
            int newWidth = getWidthInPixels(cols);
            int newHeight = getHeightInPixels(rows) +
                    (frame.getHeight() - gamePanel.getGamePanel().getHeight());
            return (newWidth <= bounds.getWidth()) && (newHeight <= bounds.getHeight());
        }

        //  Private method that returns the maximum number of rows and columns
        //      that can fit on the screen.
        private Dimension getMaxBoardSize() {
            GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
            Rectangle bounds = env.getMaximumWindowBounds();
            int maxRows = getMaxRowsFromHeightInPixels((int)bounds.getHeight() -
                    (frame.getHeight() - gamePanel.getGamePanel().getHeight()));
            int maxCols = getMaxColsFromWidthInPixels((int)bounds.getWidth());
            return new Dimension(maxCols, maxRows);
        }
    }

    //  Returns the width of a theoretical GamePanel with a given number of
    //      columns.
    private int getWidthInPixels(int nCols) {
        return 2 + (2 * GamePanel.borderCornerTL.getIconWidth()) +
                (nCols * GamePanel.borderEdgeHoriz.getIconWidth());
    }

    //  Returns the height of a theoretical GamePanel with a given number of
    //      rows.
    private int getHeightInPixels(int nRows) {
        return 2 + (3 * GamePanel.borderCornerTL.getIconHeight()) +
                ((nRows+2) * GamePanel.borderEdgeVert.getIconHeight());
    }

    //  Returns the number of columns that can fit into a board of a
    //      theoretical width in pixels.
    private int getMaxColsFromWidthInPixels(int pix) {
        return (pix - (2 + 2*(GamePanel.borderCornerTL.getIconWidth()))) / GamePanel.borderEdgeHoriz.getIconWidth();
    }

    //  Returns the number of rows that can fit into a board of a theoretical
    //      height in pixels.
    private int getMaxRowsFromHeightInPixels(int pix) {
        return (pix - (2 + 3*(GamePanel.borderCornerTL.getIconHeight()) +
                2*GamePanel.borderEdgeVert.getIconHeight())) / GamePanel.borderEdgeVert.getIconHeight();
    }

    private Dimension getNewSize(Difficulty diff) {
        int newX = getWidthInPixels(diff.getColumns());
        int topBarHeight = frame.getHeight() - gamePanel.getGamePanel().getHeight();
        int newY = getHeightInPixels(diff.getRows()) + topBarHeight;
        return new Dimension(newX, newY);
    }

}


