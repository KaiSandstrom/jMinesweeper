package GUI;

import javax.swing.*;
import java.awt.*;

public abstract class NumericDisplay {

    public static final ImageIcon numsBorderTop = new ImageIcon("Image/numsBorderTop.png");
    public static final ImageIcon numsBorderBottom = new ImageIcon("Image/numsBorderBottom.png");
    public static final ImageIcon numsBorderLeft = new ImageIcon("Image/numsBorderLeft.png");
    public static final ImageIcon numsBorderRight = new ImageIcon("Image/numsBorderRight.png");
    public static final ImageIcon numsBorderCornerTL = new ImageIcon("Image/numsBorderCornerTL.png");
    public static final ImageIcon numsBorderCornerTR = new ImageIcon("Image/numsBorderCornerTR.png");
    public static final ImageIcon numsBorderCornerBR = new ImageIcon("Image/numsBorderCornerBR.png");
    public static final ImageIcon numsBorderCornerBL = new ImageIcon("Image/numsBorderCornerBL.png");

    public static final ImageIcon numDisplayNeg = new ImageIcon("Image/numDisplayNeg.png");
    public static final ImageIcon numDisplay0 = new ImageIcon("Image/numDisplay0.png");
    public static final ImageIcon numDisplay1 = new ImageIcon("Image/numDisplay1.png");
    public static final ImageIcon numDisplay2 = new ImageIcon("Image/numDisplay2.png");
    public static final ImageIcon numDisplay3 = new ImageIcon("Image/numDisplay3.png");
    public static final ImageIcon numDisplay4 = new ImageIcon("Image/numDisplay4.png");
    public static final ImageIcon numDisplay5 = new ImageIcon("Image/numDisplay5.png");
    public static final ImageIcon numDisplay6 = new ImageIcon("Image/numDisplay6.png");
    public static final ImageIcon numDisplay7 = new ImageIcon("Image/numDisplay7.png");
    public static final ImageIcon numDisplay8 = new ImageIcon("Image/numDisplay8.png");
    public static final ImageIcon numDisplay9 = new ImageIcon("Image/numDisplay9.png");

    private final JPanel outer;
    private final JLabel left, center, right;

    public NumericDisplay() {
        outer = new JPanel(new BorderLayout(0, 0));
        left = new JLabel(numDisplay0);
        center = new JLabel(numDisplay0);
        right = new JLabel(numDisplay0);
        initialize();
    }

    private void initialize() {
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
        nums.add(left);
        nums.add(center);
        nums.add(right);
        outer.add(nums, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return outer;
    }

    protected void setNumsFromInt(int num) {
        if (num < 0)
            left.setIcon(numDisplayNeg);
        else {
            left.setIcon(intToIcon(num / 100));
        }
        center.setIcon(intToIcon((num % 100) / 10));
        right.setIcon(intToIcon(num % 10));
    }

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
