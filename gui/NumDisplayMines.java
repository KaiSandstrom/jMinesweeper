package gui;

import game.Game;

public class NumDisplayMines extends NumericDisplay {

    //  A NumDisplayMines stores and modifies the JPanel displaying the number
    //      of unflagged mines remaining on the board. This class is very
    //      simple as most of its functionality is hidden in its superclass,
    //      NumericDisplay.

    private Game game;

    //  Takes one more argument than its superclass, a reference to a Game
    //      object that is used to extract the number to be displayed.
    public NumDisplayMines(Game g) {
        super();
        game = g;
        setNums();
    }

    //  Sets the game field. Used when starting a new game.
    public void setGame(Game g) {
        game = g;
    }

    //  Simply extracts the number of mines remaining, checks that this number
    //      fits on the display, and calls its inherited setNumsFromInt method.
    public void setNums() {
        int mines = game.getMinesRemaining();
        if (mines > 999 || mines < -99)
            return;
        setNumsFromInt(mines);
    }
}
