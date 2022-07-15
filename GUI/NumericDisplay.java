package GUI;

import Game.Game;
import javax.swing.*;
import java.awt.*;

public abstract class NumericDisplay {

    public static final ImageIcon numDisplayBorderTop = new ImageIcon("Image/numDisplayBorderTop.png");
    public static final ImageIcon numDisplayBorderBottom = new ImageIcon("Image/numDisplayBorderBottom.png");
    public static final ImageIcon numDisplayBorderLeft = new ImageIcon("Image/numDisplayBorderLeft.png");
    public static final ImageIcon numDisplayBorderRight = new ImageIcon("Image/numDisplayBorderRight.png");
    public static final ImageIcon numDisplayBorderCornerTL = new ImageIcon("Image/numDisplayBorderCornerTL.png");
    public static final ImageIcon numDisplayBorderCornerTR = new ImageIcon("Image/numDisplayBorderCornerTR.png");
    public static final ImageIcon numDisplayBorderCornerBR = new ImageIcon("Image/numDisplayBorderCornerBR.png");
    public static final ImageIcon numDisplayBorderCornerBL = new ImageIcon("Image/numDisplayBorderCornerBL.png");

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
    }

    private void initialize() {
        JPanel north = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        north.add(new JLabel(numDisplayBorderCornerTL));
        north.add(new JLabel(numDisplayBorderTop));
        north.add(new JLabel(numDisplayBorderCornerTR));
        outer.add(north, BorderLayout.NORTH);

        outer.add(new JLabel(numDisplayBorderLeft), BorderLayout.WEST);

        outer.add(new JLabel(numDisplayBorderRight), BorderLayout.EAST);

        JPanel south = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        south.add(new JLabel(numDisplayBorderCornerBL));
        south.add(new JLabel(numDisplayBorderBottom));
        south.add(new JLabel(numDisplayBorderCornerBR));
        outer.add(south, BorderLayout.SOUTH);

        JPanel nums = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        nums.add(left);
        nums.add(center);
        nums.add(right);
        outer.add(nums, BorderLayout.CENTER);
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

    public abstract void setNums();

}
