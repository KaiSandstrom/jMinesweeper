package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NumDisplayTimer extends NumericDisplay {

    //  A NumDisplayTimer stores and modifies the JPanel displaying the number
    //      of seconds that have elapsed since the start of the current game.
    //      It has an int that stores the value to be displayed, and a Timer
    //      that ticks every second, updating the panel.

    private int timeCount;
    private final Timer timer;

    public NumDisplayTimer() {
        super();
        timeCount = 0;
        timer = new Timer(1000, new ActionListener() {
            //  Timer ticks every second. If more than 1000 seconds have
            //      elapsed, no updates are shown and the timer is stopped.
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeCount < 1000) {
                    timeCount++;
                    setNumsFromInt(timeCount);
                } else {
                    timer.stop();
                }
            }
        });
        clearTimer();
    }

    //  Halts and zeroes out the timer. This is used in the constructor, and
    //      when the game is reset using the smiley, menu option, or F2.
    public void clearTimer() {
        haltTimer();
        timeCount = 0;
        setNumsFromInt(timeCount);
    }

    //  Starts the timer. This is called when the first click is made and the
    //      board is populated.
    public void startTimer() {
        timer.start();
    }

    //  Halts the timer without resetting the displayed value. This happens
    //      when the game is won or lost.
    public void haltTimer() {
        timer.stop();
    }

}
