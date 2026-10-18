package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public abstract class NumericDisplay {

    //  A NumericDisplay is an abstract class that handles the basic graphical
    //      and mathematical operations shared by both the timer display and
    //      the remaining mines display. It has no abstract methods, but is
    //      still declared abstract, as it is meant to be extended, not
    //      instantiated.

    //  The two subclasses of NumericDisplay inherit the setNumsFromInt method,
    //      which takes an int, and if possible, modifies the numeric display
    //      JPanel to reflect the given int.
    private final ImageIcon numDisplayNeg = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numDisplayNeg.png")));
    private final ImageIcon numDisplay0 = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numDisplay0.png")));
    private final ImageIcon numDisplay1 = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numDisplay1.png")));
    private final ImageIcon numDisplay2 = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numDisplay2.png")));
    private final ImageIcon numDisplay3 = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numDisplay3.png")));
    private final ImageIcon numDisplay4 = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numDisplay4.png")));
    private final ImageIcon numDisplay5 = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numDisplay5.png")));
    private final ImageIcon numDisplay6 = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numDisplay6.png")));
    private final ImageIcon numDisplay7 = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numDisplay7.png")));
    private final ImageIcon numDisplay8 = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numDisplay8.png")));
    private final ImageIcon numDisplay9 = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numDisplay9.png")));

    private final JPanel outer = new JPanel(new BorderLayout(0, 0));
    private final JLabel left, center, right;

    //  Constructor initializes the numeric display with a border and three
    //      zeroes.
    public NumericDisplay() {
        JPanel north = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        ImageIcon numsBorderCornerTL = new ImageIcon(Objects.requireNonNull(
                getClass().getResource("/resources/numsBorderCornerTL.png")));
        north.add(new JLabel(numsBorderCornerTL));
        ImageIcon numsBorderTop = new ImageIcon(Objects.requireNonNull(
                getClass().getResource("/resources/numsBorderTop.png")));
        north.add(new JLabel(numsBorderTop));
        ImageIcon numsBorderCornerTR = new ImageIcon(Objects.requireNonNull(
                getClass().getResource("/resources/numsBorderCornerTR.png")));
        north.add(new JLabel(numsBorderCornerTR));
        outer.add(north, BorderLayout.NORTH);

        ImageIcon numsBorderLeft = new ImageIcon(Objects.requireNonNull(
                getClass().getResource("/resources/numsBorderLeft.png")));
        outer.add(new JLabel(numsBorderLeft), BorderLayout.WEST);

        ImageIcon numsBorderRight = new ImageIcon(Objects.requireNonNull(
                getClass().getResource("/resources/numsBorderRight.png")));
        outer.add(new JLabel(numsBorderRight), BorderLayout.EAST);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        ImageIcon numsBorderCornerBL = new ImageIcon(Objects.requireNonNull(
                getClass().getResource("/resources/numsBorderCornerBL.png")));
        south.add(new JLabel(numsBorderCornerBL));
        ImageIcon numsBorderBottom = new ImageIcon(Objects.requireNonNull(
                getClass().getResource("/resources/numsBorderBottom.png")));
        south.add(new JLabel(numsBorderBottom));
        ImageIcon numsBorderCornerBR = new ImageIcon(Objects.requireNonNull(
                getClass().getResource("/resources/numsBorderCornerBR.png")));
        south.add(new JLabel(numsBorderCornerBR));
        outer.add(south, BorderLayout.SOUTH);

        JPanel nums = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        left = new JLabel(numDisplay0);
        center = new JLabel(numDisplay0);
        right = new JLabel(numDisplay0);
        nums.add(left);
        nums.add(center);
        nums.add(right);
        outer.add(nums, BorderLayout.CENTER);
    }

    //  Returns a reference to the store JPanel. Used in InfoPanel to place
    //      these panels into the wider board.
    public JPanel getPanel() {
        return outer;
    }

    //  Inherited by subclasses. Sets the icons in the panel based on the int
    //      provided. If the number is out of the displayable range, a dashed
    //      line is displayed.
    protected void setNumsFromInt(int num) {
        if (num > 999 || num < -99) {
            left.setIcon(numDisplayNeg);
            center.setIcon(numDisplayNeg);
            right.setIcon(numDisplayNeg);
            return;
        }
        if (num < 0)
            left.setIcon(numDisplayNeg);
        else {
            left.setIcon(intToIcon(num / 100));
        }
        center.setIcon(intToIcon((num % 100) / 10));
        right.setIcon(intToIcon(num % 10));
    }

    //  Simple switch statement wrapper to map digits to icons.
    private ImageIcon intToIcon(int num) {
        num = Math.abs(num);
        return switch (num) {
            case 1 -> numDisplay1;
            case 2 -> numDisplay2;
            case 3 -> numDisplay3;
            case 4 -> numDisplay4;
            case 5 -> numDisplay5;
            case 6 -> numDisplay6;
            case 7 -> numDisplay7;
            case 8 -> numDisplay8;
            case 9 -> numDisplay9;
            default -> numDisplay0;
        };
    }


}
