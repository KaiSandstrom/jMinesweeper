package game;

import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class SaveState implements Serializable, Iterable<String[]> {

    //  A SaveState stores all persistent information that should be recovered
    //      from a file when the game application is opened, including the
    //      last-played difficulty, the previous custom board options, and,
    //      perhaps most importantly, a high score table.

    //  SaveState implements Iterable, returning an array of Strings
    //      representing the three displayed entries for each line-entry in the
    //      high score table. Unlike UpdateTracker, iteration does not clear
    //      the structure.

    private Difficulty selected;
    private String[] lastCustomEntry;
    private final HashMap<Difficulty, SaveData> scores;

    //  Simple private wrapper struct for player name and score. This is used
    //      in order to provide a single object type to be stored in a HashMap.
    private static class SaveData implements Serializable {
        String playerName;
        int score;
        public SaveData(String n, int s) {
            playerName = n;
            score = s;
        }
    }

    //  The constructor is private, as all SaveState objects used by the
    //      application are returned in loadFromFile, which either loads a
    //      serialized SaveState from a file or creates a new one with
    //      default values using this private constructor.
    private SaveState() {
        scores = new HashMap<>();
        selected = Difficulty.INTERMEDIATE;
        lastCustomEntry = new String[]{"", "", ""};
    }

    //  Load a saved SaveState from file, or create a new SaveState if the file
    //      does not exist.
    public static SaveState loadFromFile()  {
        try {
            FileInputStream file = new FileInputStream("jMinesweeperSaveData");
            try (file; ObjectInputStream in = new ObjectInputStream(file)) {
                return (SaveState) in.readObject();
            } catch (ClassNotFoundException e) {
                //  Should never happen
                System.err.println("Class not found");
            }
        } catch (IOException e) {
            return new SaveState();
        }
        return new SaveState();
    }

    //  Save SaveState to file.
    public void saveToFile() {
        try {
            FileOutputStream file = new FileOutputStream("jMinesweeperSaveData");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this);
            out.close();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error: File not found");
        }
    }

    //  These next 9 public methods are simple getters, setters, and
    //      manipulators, working on the data stored in a SaveState.

    public int size() {
        return scores.size();
    }

    public boolean containsEntry(Difficulty diff) {
        return scores.containsKey(diff);
    }

    public int getScoreSeconds(Difficulty diff) {
        return scores.get(diff).score;
    }

    public String[] getScoreReport(Difficulty diff) {
        return new String[]{diff.toString(), scores.get(diff).score+" seconds", scores.get(diff).playerName};
    }

    public void addScore(Difficulty diff, String name, int score) {
        SaveData data = new SaveData(name, score);
        scores.put(diff, data);
    }

    public void setSelected(Difficulty diff) {
        selected = diff;
    }

    public Difficulty getSelected() {
        return selected;
    }

    public void setLastCustomEntry(String[] entry) {
        lastCustomEntry = entry;
    }

    public String[] getLastCustomEntry() {
        return lastCustomEntry;
    }

    public void clearScores() {
        scores.clear();
    }

    //  Iterator returns score table entries in the order of difficulty, as
    //      defined in the compareTo method in Difficulty.
    @Override
    public Iterator<String[]> iterator() {
        return new Iterator<>() {

            private final HashSet<Difficulty> visited = new HashSet<>();
            private final ArrayList<Difficulty> keys = new ArrayList<>(scores.keySet());

            @Override
            public boolean hasNext() {
                return (visited.size() != scores.size());
            }

            @Override
            public String[] next() {
                Difficulty key = keys.remove(keys.indexOf(Collections.min(keys)));
                visited.add(key);
                return getScoreReport(key);
            }
        };
    }
}
