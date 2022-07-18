package game;

public class Difficulty {

    //  Simple wrapper struct for a game difficulty.

    //  Three static final Difficulties are defined, representing the three
    //      default difficulties that come with the game.

    public static final Difficulty BEGINNER = new Difficulty(10, 10, 9);
    public static final Difficulty INTERMEDIATE = new Difficulty(16, 16, 40);
    public static final Difficulty EXPERT = new Difficulty(16, 30, 40);

    private final int nRows, nCols, nMines;

    public Difficulty(int rows, int columns, int mines) {
        nRows = rows;
        nCols = columns;
        nMines = mines;
    }

    public int getRows() {
        return nRows;
    }

    public int getColumns() {
        return nCols;
    }

    public int getMines() {
        return nMines;
    }

    public boolean equals(Difficulty that) {
        return (this.nRows == that.nRows && this.nCols == that.nCols && this.nMines == that.nMines);
    }

    public String toString() {
        if (this == BEGINNER)
            return "Beginner";
        if (this == INTERMEDIATE)
            return "Intermediate";
        if (this == EXPERT)
            return "Expert";
        return "Custom (H:" + nRows + ", W:" + nCols + ", M:" + nMines + ")";
    }

}
