package GUI;

import Game.Game;

public class NumDisplayMines extends NumericDisplay {

    private Game game;

    public NumDisplayMines(Game g) {
        super();
        game = g;
    }

    public void setGame(Game g) {
        game = g;
    }

    @Override
    public void setNums() {
        int mines = game.getMinesRemaining();
        if (mines > 999 || mines < -99)
            return;
        setNumsFromInt(mines);
    }
}
