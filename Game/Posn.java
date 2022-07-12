package Game;

//  Simple wrapper object for a row and column coordinate. Used in the
//      UpdateTracker to store the coordinates of updated cells.

public class Posn {
    int row;
    int col;

    public Posn(int r, int c) {
        row = r;
        col = c;
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

}
