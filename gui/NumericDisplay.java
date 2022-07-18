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

    private final ImageIcon numsBorderTop = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numsBorderTop.png")));
    private final ImageIcon numsBorderBottom = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numsBorderBottom.png")));
    private final ImageIcon numsBorderLeft = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numsBorderLeft.png")));
    private final ImageIcon numsBorderRight = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numsBorderRight.png")));
    private final ImageIcon numsBorderCornerTL = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numsBorderCornerTL.png")));
    private final ImageIcon numsBorderCornerTR = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numsBorderCornerTR.png")));
    private final ImageIcon numsBorderCornerBR = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numsBorderCornerBR.png")));
    private final ImageIcon numsBorderCornerBL = new ImageIcon(Objects.requireNonNull(
            getClass().getResource("/resources/numsBorderCornerBL.png")));

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
        north.add(new JLabel(numsBorderCornerTL));
        north.add(new JLabel(numsBorderTop));
        north.add(new JLabel(numsBorderCornerTR));
        outer.add(north, BorderLayout.NORTH);

        outer.add(new JLabel(numsBorderLeft), BorderLayout.WEST);

        outer.add(new JLabel(numsBorderRight), BorderLayout.EAST);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.add(new JLabel(numsBorderCornerBL));
        south.add(new JLabel(numsBorderBottom));
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
        ImageIcon result;
        switch (num) {
            case 1:
                result = numDisplay1; break;
            case 2:
                result = numDisplay2; break;
            case 3:
                result = numDisplay3; break;
            case 4:
                result = numDisplay4; break;
            case 5:
                result = numDisplay5; break;
            case 6:
                result = numDisplay6; break;
            case 7:
                result = numDisplay7; break;
            case 8:
                result = numDisplay8; break;
            case 9:
                result = numDisplay9; break;
            default:
                result = numDisplay0;
        }
        return result;
    }


}
