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
            //  When the timer ticks every second, it updates the stored time
            //      counter value and displays it on the timer display with a
            //      call to its inherited setNumsFromInt method. When the
            //      thousandth second has elapsed, this call sets the display
            //      to a dashed line, and the timer is stopped, as all future
            //      ticks will also display a dashed line.
            @Override
            public void actionPerformed(ActionEvent e) {
                timeCount++;
                setNumsFromInt(timeCount);
                if (timeCount > 1000)
                    timer.stop();
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
