package game;

import java.io.Serializable;

public class Difficulty implements Serializable, Comparable<Difficulty> {

    //  Simple wrapper struct for a game difficulty.

    //  Three static final Difficulties are defined, representing the three
    //      default difficulties that come with the game.

    public static final Difficulty BEGINNER = new Difficulty(10, 10, 9);
    public static final Difficulty INTERMEDIATE = new Difficulty(16, 16, 40);
    public static final Difficulty EXPERT = new Difficulty(16, 30, 99);

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

    //  Used both to check against the default difficulties, and to make the
    //      hashmap in SaveState work properly.
    @Override
    public boolean equals(Object other) {
        Difficulty that;
        if (other instanceof Difficulty)
            that = (Difficulty) other;
        else
            return false;
        return (this.nRows == that.nRows && this.nCols == that.nCols && this.nMines == that.nMines);
    }

    //  The default save states are rendered as their names. Others are marked
    //      as custom and list their parameters.
    @Override
    public String toString() {
        if (this.equals(BEGINNER))
            return "Beginner";
        if (this.equals(INTERMEDIATE))
            return "Intermediate";
        if (this.equals(EXPERT))
            return "Expert";
        return "Custom (H:" + nRows + ", W:" + nCols + ", M:" + nMines + ")";
    }

    //  Used to make the hashmap in SaveState work properly. The integer
    //      representation is simply a listing of rows, columns, and
    //      mines from left to right.
    @Override
    public int hashCode() {
        int rowComponent = nRows * 10000000;
        int colComponent = nCols * 10000;
        return rowComponent + colComponent + nMines;
    }

    //  Used for displaying high scores for custom difficulties in order of
    //      difficulty. Difficulty is defined as the ratio of mines to total
    //      cells. The higher this ratio, the more difficult the board. This
    //      ratio may be changed later to slightly favor larger boards with
    //      the same ratio.
    @Override
    public int compareTo(Difficulty that) {
        double diffValThis = (double)this.nMines / (this.nRows * this.nCols);
        double diffValThat = (double)that.nMines / (that.nRows * that.nCols);
        return (int) ((diffValThis*100000) - (diffValThat*100000));
    }
}
