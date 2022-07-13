package Game;

import java.util.ArrayList;
import java.util.Iterator;

public class UpdateTracker implements Iterable<Posn> {

    //  The UpdateTracker is an iterable wrapper object for an ArrayList of
    //      Posns, representing the coordinates of the cells that need their
    //      icons updated on the board GUI. The constructor simply creates a
    //      new ArrayList of Posns, which is final.

    private final ArrayList<Posn> updated;

    public UpdateTracker() {
        updated = new ArrayList<>();
    }

    //  The UpdateTracker is designed to be iterated through in an enhanced
    //      for loop in the GUI. The iterator deletes an item from the
    //      ArrayList each time next() is called, ensuring that the ArrayList
    //      will be empty when iteration is complete.
    @Override
    public Iterator<Posn> iterator() {
        return new Iterator<>() {

            @Override
            public boolean hasNext() {
                return (updated.size() > 0);
            }

            @Override
            public Posn next() {
                return updated.remove(0);
            }
        };
    }

    //  addUpdate checks the ArrayList for a duplicate posn, and returns
    //      without adding anything if the argument is a duplicate. I tried to
    //      avoid any scenarios where this method would be called multiple
    //      on the same cell, but it's good practice to make sure.
    public void addUpdate(Posn pos) {
        if (!contains(pos))
            updated.add(pos);
    }

    //  Returns true if the ArrayList contains the given Posn. This is used
    //      only in addUpdate to check for duplicates. Equality is checked
    //      according to the equals method in Posn -- both row and col are
    //      equal.
    public boolean contains(Posn pos) {
        for (Posn p : updated) {
            if (p.equals(pos))
                return true;
        }
        return false;
    }
}
