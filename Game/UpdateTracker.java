package Game;

import java.util.ArrayList;
import java.util.Iterator;

public class UpdateTracker implements Iterable<Posn> {

    private ArrayList<Posn> updated;

    public UpdateTracker() {
        updated = new ArrayList<Posn>();
    }

    @Override
    public Iterator<Posn> iterator() {
        return new Iterator<Posn>() {

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

    public void addUpdate(Posn pos) {

    }
}
