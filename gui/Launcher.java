package gui;

import javax.swing.*;

public class Launcher {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(OuterFrame::new);
    }
}
